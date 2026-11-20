package model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Represents a gym member.
 *
 * INHERITANCE  : Extends Person (inherits name, age, phone, getRole).
 * ENCAPSULATION: Member-specific private fields with getters/setters.
 * POLYMORPHISM : Overrides getRole() and toString() from Person.
 */
public class Member extends Person {

    private final String memberId;        // Unique system-generated ID
    private String       joiningDate;     // Format: YYYY-MM-DD
    private String       expiryDate;      // Computed from plan duration
    private String       planName;        // Plan selected by the member
    private String       assignedTrainerId; // Trainer assigned (nullable)

    /**
     * Full constructor for creating a new member.
     *
     * @param memberId    Unique member ID.
     * @param name        Full name.
     * @param age         Age in years.
     * @param phone       Phone number.
     * @param joiningDate Date of joining (YYYY-MM-DD).
     * @param planName    Name of the selected membership plan.
     * @param expiryDate  Calculated expiry date (YYYY-MM-DD).
     */
    public Member(String memberId, String name, int age, String phone,
                  String joiningDate, String planName, String expiryDate) {
        super(name, age, phone);
        this.memberId          = memberId;
        this.joiningDate       = joiningDate;
        this.planName          = planName;
        this.expiryDate        = expiryDate;
        this.assignedTrainerId = null;
    }

    // ─── Getters and Setters ───────────────────────────────────────────────

    public String getMemberId()          { return memberId;          }
    public String getJoiningDate()       { return joiningDate;       }
    public String getExpiryDate()        { return expiryDate;        }
    public String getPlanName()          { return planName;          }
    public String getAssignedTrainerId() { return assignedTrainerId; }

    public void setExpiryDate(String expiryDate)        { this.expiryDate        = expiryDate;    }
    public void setPlanName(String planName)             { this.planName          = planName;      }
    public void setAssignedTrainerId(String trainerId)   { this.assignedTrainerId = trainerId;     }

    /**
     * Checks whether the membership is currently active.
     * Compares today's date with the expiry date.
     *
     * @return true if the membership has not expired, false otherwise.
     */
    public boolean isActive() {
        try {
            LocalDate expiry = LocalDate.parse(expiryDate);
            return !LocalDate.now().isAfter(expiry);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * POLYMORPHISM: Overrides abstract method from Person.
     */
    @Override
    public String getRole() {
        return "Member";
    }

    /**
     * POLYMORPHISM: Overrides toString() from Person.
     * Provides a detailed, formatted representation of the member.
     */
    @Override
    public String toString() {
        String status   = isActive() ? "ACTIVE" : "EXPIRED";
        String trainer  = (assignedTrainerId != null) ? assignedTrainerId : "None";
        return String.format(
                "ID: %-8s | %-20s | Age: %3d | Phone: %-12s | Plan: %-12s | Joined: %s | Expiry: %s | Status: %-7s | Trainer: %s",
                memberId, getName(), getAge(), getPhone(),
                planName, joiningDate, expiryDate, status, trainer);
    }
}
