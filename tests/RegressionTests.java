import dsa.WaitingQueue;
import model.Member;
import service.AttendanceService;
import service.MemberService;
import service.TrainerService;
import util.DateUtil;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class RegressionTests {

    public static void main(String[] args) {
        testDateAndPhoneValidation();
        testWaitingQueue();
        testMemberAndAttendanceServices();
        System.out.println("All regression tests passed.");
    }

    private static void testDateAndPhoneValidation() {
        check(DateUtil.isValidDate("2024-02-29"), "Valid leap day should be accepted.");
        check(!DateUtil.isValidDate("2025-02-29"), "Invalid leap day should be rejected.");
        check(!DateUtil.isValidDate("2024-02-30"), "Invalid calendar day should be rejected.");
        check(DateUtil.addMonths("2024-01-31", 1).equals("2024-02-29"),
                "Month addition should handle leap-year month ends.");
        check(DateUtil.isValidPhone("0123456789"), "ASCII 10-digit phone should be accepted.");
        check(!DateUtil.isValidPhone("١٢٣٤٥٦٧٨٩٠"), "Non-ASCII numerals should be rejected.");
    }

    private static void testWaitingQueue() {
        WaitingQueue<String> queue = new WaitingQueue<>();
        check(queue.isEmpty(), "New queue should be empty.");
        expectNoSuchElement(queue::dequeue, "Dequeue from an empty queue should fail.");
        expectNoSuchElement(queue::peek, "Peek at an empty queue should fail.");

        queue.enqueue("GYM001");
        queue.enqueue("GYM002");
        check(queue.contains("GYM002"), "Queue should find an enqueued member.");
        check(queue.dequeue().equals("GYM001"), "Queue should preserve FIFO order.");
        check(!queue.contains("GYM001"), "Dequeued member should no longer be present.");
        check(queue.size() == 1, "Queue size should reflect dequeue.");
        check(queue.dequeue().equals("GYM002"), "Queue should return its final item.");
        check(queue.isEmpty(), "Queue should be empty after removing all items.");
    }

    private static void testMemberAndAttendanceServices() {
        MemberService members = new MemberService();
        members.registerMember(new Scanner("Asha\n25\n1234567890\n\n1\n"));
        Member member = members.getAllMembers().get(0);

        check(List.copyOf(members.getAllPlans().keySet()).equals(List.of("1", "2", "3", "4")),
                "Plans should be listed in selection order.");
        expectUnsupportedOperation(members.getAllMembers()::clear,
                "Member collection should be read-only.");
        expectUnsupportedOperation(members.getAllPlans()::clear,
                "Plan collection should be read-only.");

        TrainerService trainers = new TrainerService();
        trainers.assignTrainer(new Scanner("GYM001\n"), members);
        trainers.assignTrainer(new Scanner("GYM001\n"), members);
        check(trainers.getWaitingList().size() == 1, "A member should only be queued once.");

        trainers.addTrainer(new Scanner("Raj\n30\n1234567890\nStrength\n"), members);
        check("TR001".equals(member.getAssignedTrainerId()),
                "Waitlist assignment should update the member.");
        check(trainers.getTrainerAssignments().get("TR001").equals(List.of("GYM001")),
                "Trainer assignment map should include the waiting member.");

        trainers.addTrainer(new Scanner("Mira\n29\n1234567891\nYoga\n"), members);
        trainers.assignTrainer(new Scanner("GYM001\nTR002\n"), members);
        check("TR002".equals(member.getAssignedTrainerId()),
                "Manual reassignment should update the member.");
        check(trainers.getTrainerAssignments().get("TR001").isEmpty(),
                "Manual reassignment should remove the old mapping.");
        check(trainers.getTrainerAssignments().get("TR002").equals(List.of("GYM001")),
                "Manual reassignment should add the new mapping.");

        AttendanceService attendance = new AttendanceService();
        attendance.markAttendance(new Scanner("GYM001\n"), members);
        expectUnsupportedOperation(attendance.getAttendanceRecords()::clear,
                "Attendance map should be read-only.");
        expectUnsupportedOperation(
                () -> attendance.getAttendanceRecords().get("GYM001").clear(),
                "Attendance date lists should be read-only.");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void expectNoSuchElement(Runnable action, String message) {
        try {
            action.run();
        } catch (NoSuchElementException expected) {
            return;
        }
        throw new AssertionError(message);
    }

    private static void expectUnsupportedOperation(Runnable action, String message) {
        try {
            action.run();
        } catch (UnsupportedOperationException expected) {
            return;
        }
        throw new AssertionError(message);
    }
}
