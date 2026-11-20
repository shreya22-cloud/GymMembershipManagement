package model;

/**
 * Represents a gym trainer.
 *
 * INHERITANCE  : Extends Person (inherits name, age, phone).
 * ENCAPSULATION: Trainer-specific fields are private with getters/setters.
 * POLYMORPHISM : Overrides getRole() and toString() from Person.
 */
public class Trainer extends Person {

    private final String trainerId;    // Unique trainer ID
    private String       speciality;   // Trainer's area of expertise

    /**
     * Constructor for a Trainer.
     *
     * @param trainerId   Unique trainer ID.
     * @param name        Full name.
     * @param age         Age in years.
     * @param phone       Phone number.
     * @param speciality  Area of expertise (e.g., Yoga, Weight Training).
     */
    public Trainer(String trainerId, String name, int age, String phone, String speciality) {
        super(name, age, phone);
        this.trainerId   = trainerId;
        this.speciality  = speciality;
    }

    // ─── Getters and Setters ───────────────────────────────────────────────

    public String getTrainerId()  { return trainerId;  }
    public String getSpeciality() { return speciality; }

    public void setSpeciality(String speciality) { this.speciality = speciality; }

    /**
     * POLYMORPHISM: Overrides abstract method from Person.
     */
    @Override
    public String getRole() {
        return "Trainer";
    }

    /**
     * POLYMORPHISM: Overrides toString() from Person.
     */
    @Override
    public String toString() {
        return String.format("Trainer ID: %-6s | %-20s | Age: %3d | Phone: %-12s | Speciality: %s",
                trainerId, getName(), getAge(), getPhone(), speciality);
    }
}
