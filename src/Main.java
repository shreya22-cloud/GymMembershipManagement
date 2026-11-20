import service.AttendanceService;
import service.MemberService;
import service.ReportService;
import service.TrainerService;
import util.ConsoleUtil;

import java.util.Scanner;

/**
 * ============================================================
 *           GYM MEMBERSHIP MANAGEMENT SYSTEM
 * ============================================================
 * Entry point of the application.
 * Displays the main menu in a continuous loop and delegates
 * each user action to the appropriate service class.
 *
 * Compile: javac -d out -sourcepath src src/Main.java
 * Run    : java -cp out Main
 * ============================================================
 */
public class Main {

    public static void main(String[] args) {

        // ─── Service layer initialisation ─────────────────────────────────
        MemberService     memberService     = new MemberService();
        AttendanceService attendanceService = new AttendanceService();
        TrainerService    trainerService    = new TrainerService();
        ReportService     reportService     = new ReportService(
                                                memberService,
                                                attendanceService,
                                                trainerService);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // ─── Main application loop ────────────────────────────────────────
        while (running) {
            printMainMenu();
            System.out.print("Enter your choice: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1"  -> memberService.registerMember(scanner);
                case "2"  -> memberService.displayAllMembers();
                case "3"  -> memberService.displayPlans();
                case "4"  -> memberService.renewMembership(scanner);
                case "5"  -> attendanceService.markAttendance(scanner, memberService);
                case "6"  -> attendanceService.viewAttendance(scanner, memberService);
                case "7"  -> trainerService.addTrainer(scanner, memberService);
                case "8"  -> trainerService.assignTrainer(scanner, memberService);
                case "9"  -> memberService.searchMember(scanner);
                case "10" -> reportService.generateReport();
                case "11" -> trainerService.viewAssignments(memberService);
                case "12" -> {
                    ConsoleUtil.printBorder();
                    System.out.println("  Thank you for using the Gym Membership Management System.");
                    System.out.println("  Goodbye!");
                    ConsoleUtil.printBorder();
                    running = false;
                }
                default -> ConsoleUtil.printError(
                        "Invalid choice '" + input + "'. Please enter a number between 1 and 12.");
            }

            if (running) {
                System.out.println();
                System.out.print("  Press Enter to continue...");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    /**
     * Prints the main menu to the console.
     */
    private static void printMainMenu() {
        System.out.println();
        ConsoleUtil.printBorder();
        System.out.println("       GYM MEMBERSHIP MANAGEMENT SYSTEM");
        ConsoleUtil.printBorder();
        System.out.println("   1.  Register Member");
        System.out.println("   2.  View All Members");
        System.out.println("   3.  View Membership Plans");
        System.out.println("   4.  Renew Membership");
        System.out.println("   5.  Mark Attendance");
        System.out.println("   6.  View Attendance");
        System.out.println("   7.  Add Trainer");
        System.out.println("   8.  Assign Trainer");
        System.out.println("   9.  Search Member");
        System.out.println("   10. View Reports");
        System.out.println("   11. View Trainer Assignments");
        System.out.println("   12. Exit");
        ConsoleUtil.printBorder();
    }
}
