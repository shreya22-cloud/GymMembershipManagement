package model;

/**
 * Abstract base class for all persons in the gym system.
 *
 * ABSTRACTION  : Abstract class with abstract method getRole().
 * ENCAPSULATION: Private fields exposed through public getters/setters.
 * INHERITANCE  : Extended by Member and Trainer classes.
 */
public abstract class Person {

    private String name;
    private int age;
    private String phone;

    /**
     * Parameterised constructor for Person.
     *
     * @param name  Full name of the person.
     * @param age   Age in years.
     * @param phone Contact phone number.
     */
    public Person(String name, int age, String phone) {
        this.name  = name;
        this.age   = age;
        this.phone = phone;
    }

    // ─── Getters and Setters ───────────────────────────────────────────────

    public String getName()  { return name;  }
    public int    getAge()   { return age;   }
    public String getPhone() { return phone; }

    public void setName(String name)   { this.name  = name;  }
    public void setAge(int age)        { this.age   = age;   }
    public void setPhone(String phone) { this.phone = phone; }

    /**
     * Abstract method: each subclass must declare its role.
     * Demonstrates ABSTRACTION.
     *
     * @return A string describing the person's role.
     */
    public abstract String getRole();

    /**
     * Polymorphic display method.
     * Overridden in subclasses for POLYMORPHISM.
     */
    @Override
    public String toString() {
        return String.format("Name: %-20s | Age: %3d | Phone: %s | Role: %s",
                name, age, phone, getRole());
    }
}
