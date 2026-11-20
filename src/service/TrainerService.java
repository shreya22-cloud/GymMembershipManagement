package service;

import dsa.WaitingQueue;
import exception.MemberNotFoundException;
import model.Member;
import model.Trainer;
import util.ConsoleUtil;
import util.DateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Service class managing trainer-related operations.
 *
 * COLLECTIONS USED:
 *   - HashMap<String, Trainer>       : stores trainers by their ID.
 *   - HashMap<String, List<String>>  : maps trainerId -> list of assigned memberIds.
 *
 * DSA USED:
 *   - WaitingQueue<String>           : custom linked-node queue used as a trainer
 *                                      waiting list for members who could not be
 *                                      assigned a trainer at registration time.
 */
public class TrainerService {

    /** Trainer registry: trainerId -> Trainer. (HashMap requirement) */
    private final Map<String, Trainer> trainers = new HashMap<>();

    /** Assignment map: trainerId -> list of assigned memberIds. (HashMap requirement) */
    private final Map<String, List<String>> trainerAssignments = new HashMap<>();

    /**
     * Custom linked-node queue acting as the trainer waiting list.
     * When no trainer is selected, the member's ID is enqueued here.
     * (DSA requirement)
     */
    private final WaitingQueue<String> waitingList = new WaitingQueue<>();

    private int trainerCounter = 1;

    // ─── Trainer ID generator ─────────────────────────────────────────────

    private String generateTrainerId() {
        return String.format("TR%03d", trainerCounter++);
    }

    // ─── Feature 7: Add Trainer ───────────────────────────────────────────

    /**
     * Interactively adds a new trainer to the system.
     *
     * @param scanner Console scanner.
     */
    public void addTrainer(Scanner scanner) {
        ConsoleUtil.printHeader("ADD NEW TRAINER");

        System.out.print("  Enter trainer name        : ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            ConsoleUtil.printError("Trainer name cannot be empty.");
            return;
        }

        System.out.print("  Enter age                 : ");
        int age;
        try {
            age = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            ConsoleUtil.printError("Invalid age. Enter a numeric value.");
            return;
        }
        if (age < 18 || age > 65) {
            ConsoleUtil.printError("Age must be between 18 and 65.");
            return;
        }

        System.out.print("  Enter phone (10 digits)   : ");
        String phone = scanner.nextLine().trim();
        if (!DateUtil.isValidPhone(phone)) {
            ConsoleUtil.printError("Phone must be exactly 10 digits.");
            return;
        }

        System.out.print("  Enter speciality          : ");
        String speciality = scanner.nextLine().trim();
        if (speciality.isEmpty()) {
            ConsoleUtil.printError("Speciality cannot be empty.");
            return;
        }

        String trainerId = generateTrainerId();
        Trainer trainer  = new Trainer(trainerId, name, age, phone, speciality);
        trainers.put(trainerId, trainer);
        trainerAssignments.put(trainerId, new ArrayList<>());

        ConsoleUtil.printSuccess("Trainer added successfully! Trainer ID: " + trainerId);

        // Auto-process waiting list: assign this new trainer to waiting members
        processWaitingList();
    }

    // ─── Feature 8: Assign Trainer ───────────────────────────────────────

    /**
     * Assigns a trainer to a member.
     * If the member chooses to skip, their ID is added to the waiting queue.
     *
     * @param scanner       Console scanner.
     * @param memberService Reference to MemberService for member lookup.
     */
    public void assignTrainer(Scanner scanner, MemberService memberService) {
        ConsoleUtil.printHeader("ASSIGN TRAINER TO MEMBER");

        System.out.print("  Enter Member ID: ");
        String memberId = scanner.nextLine().trim().toUpperCase();

        try {
            Member member = memberService.findMemberById(memberId);

            if (trainers.isEmpty()) {
                ConsoleUtil.printInfo("No trainers available. Adding member to waiting list.");
                enqueueToWaitingList(memberId);
                return;
            }

            displayTrainers();
            System.out.print("  Enter Trainer ID to assign (or press Enter to join waiting list): ");
            String trainerId = scanner.nextLine().trim().toUpperCase();

            if (trainerId.isEmpty()) {
                enqueueToWaitingList(memberId);
                return;
            }

            if (!trainers.containsKey(trainerId)) {
                ConsoleUtil.printError("Trainer not found: " + trainerId);
                return;
            }

            // Assign trainer
            member.setAssignedTrainerId(trainerId);
            trainerAssignments.get(trainerId).add(memberId);

            ConsoleUtil.printSuccess("Trainer " + trainers.get(trainerId).getName()
                    + " assigned to " + member.getName() + ".");

        } catch (MemberNotFoundException e) {
            ConsoleUtil.printError(e.getMessage());
        }
    }

    // ─── Feature 7b: Display Trainers ─────────────────────────────────────

    /**
     * Displays all available trainers.
     */
    public void displayTrainers() {
        ConsoleUtil.printHeader("AVAILABLE TRAINERS");
        if (trainers.isEmpty()) {
            ConsoleUtil.printInfo("No trainers registered yet.");
            return;
        }
        for (Trainer t : trainers.values()) {
            System.out.println("  " + t);
        }
    }

    // ─── Feature 11: View Trainer-Member Assignments ──────────────────────

    /**
     * Displays all trainer-member assignment pairs and the current waiting list.
     *
     * @param memberService Reference to MemberService.
     */
    public void viewAssignments(MemberService memberService) {
        ConsoleUtil.printHeader("TRAINER - MEMBER ASSIGNMENTS");

        if (trainerAssignments.isEmpty()) {
            ConsoleUtil.printInfo("No assignments yet.");
        } else {
            for (Map.Entry<String, List<String>> entry : trainerAssignments.entrySet()) {
                String trainerId = entry.getKey();
                Trainer trainer  = trainers.get(trainerId);
                System.out.printf("%n  Trainer: %-6s | %s%n",
                        trainerId, trainer != null ? trainer.getName() : "Unknown");

                List<String> assigned = entry.getValue();
                if (assigned.isEmpty()) {
                    System.out.println("    (No members assigned)");
                } else {
                    for (String mId : assigned) {
                        String memberName = mId;
                        try {
                            memberName = memberService.findMemberById(mId).getName();
                        } catch (MemberNotFoundException ignored) {}
                        System.out.printf("    -> %s (%s)%n", mId, memberName);
                    }
                }
            }
        }

        // Show the DSA waiting queue
        System.out.println();
        ConsoleUtil.printLine();
        System.out.println("  TRAINER WAITING LIST (Custom Queue - DSA):");
        ConsoleUtil.printLine();
        if (waitingList.isEmpty()) {
            System.out.println("  (No members in the waiting list)");
        } else {
            System.out.println("  Members waiting for a trainer assignment:");
            waitingList.display();
        }
    }

    // ─── DSA: Waiting Queue helpers ───────────────────────────────────────

    /**
     * Adds a member's ID to the custom linked-node waiting queue.
     *
     * @param memberId The member to enqueue.
     */
    private void enqueueToWaitingList(String memberId) {
        if (waitingList.contains(memberId)) {
            ConsoleUtil.printInfo("Member " + memberId + " is already on the trainer waiting list."
                    + " No duplicate entry was added.");
            return;
        }
        waitingList.enqueue(memberId);
        ConsoleUtil.printInfo("Member " + memberId + " added to trainer waiting list."
                + " Position: " + waitingList.size());
    }

    /**
     * Processes the waiting queue when a new trainer is added.
     * Dequeues members and assigns the latest trainer to them.
     */
    private void processWaitingList() {
        if (waitingList.isEmpty()) return;

        // Find the latest trainer (the one just added)
        String latestTrainerId = "TR" + String.format("%03d", trainerCounter - 1);
        Trainer latestTrainer  = trainers.get(latestTrainerId);
        if (latestTrainer == null) return;

        System.out.println();
        ConsoleUtil.printInfo("Processing waiting list...");

        while (!waitingList.isEmpty()) {
            String waitingMemberId = waitingList.dequeue();
            trainerAssignments.get(latestTrainerId).add(waitingMemberId);
            ConsoleUtil.printSuccess("Auto-assigned " + latestTrainer.getName()
                    + " to waiting member: " + waitingMemberId);
        }
    }

    /**
     * Returns all trainer data. Used by ReportService.
     */
    public Map<String, Trainer> getAllTrainers() {
        return trainers;
    }

    /**
     * Returns all trainer-member assignment data. Used by ReportService.
     */
    public Map<String, List<String>> getTrainerAssignments() {
        return trainerAssignments;
    }

    /**
     * Returns the custom waiting queue. Used by ReportService.
     */
    public WaitingQueue<String> getWaitingList() {
        return waitingList;
    }
}
