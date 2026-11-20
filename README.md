# Gym Membership Management System

A complete, console-based Gym Membership Management System built with **Core Java**.
This project is a college team assignment demonstrating OOP, DSA, Collections, and Custom Exceptions.

---

## Project Structure

```
GymMembershipManagement/
├── src/
│   ├── Main.java                          # Application entry point & menu loop
│   ├── model/
│   │   ├── Person.java                    # Abstract base class (Abstraction + Encapsulation)
│   │   ├── Member.java                    # Extends Person (Inheritance + Polymorphism)
│   │   ├── Trainer.java                   # Extends Person (Inheritance + Polymorphism)
│   │   └── MembershipPlan.java            # Encapsulated plan model
│   ├── service/
│   │   ├── MemberService.java             # Registration, search, renewal, display
│   │   ├── AttendanceService.java         # Mark & view attendance
│   │   ├── TrainerService.java            # Add trainer, assign trainer, waiting list
│   │   └── ReportService.java             # Aggregated system reports
│   ├── exception/
│   │   ├── InvalidMemberException.java    # Thrown on bad input data
│   │   ├── MemberNotFoundException.java   # Thrown when member ID not found
│   │   └── DuplicateAttendanceException.java # Thrown on duplicate attendance
│   ├── dsa/
│   │   └── WaitingQueue.java              # Custom linked-node Queue (manual DSA)
│   └── util/
│       ├── DateUtil.java                  # Date formatting & validation helpers
│       └── ConsoleUtil.java               # Console UI printing helpers
├── out/                                   # Compiled .class files (auto-generated)
└── README.md
```

---

## How to Compile and Run

### Prerequisites
- Java JDK 17 or above installed.
- `javac` and `java` available in your PATH.

### Step 1: Navigate to the project folder
```bash
cd GymMembershipManagement
```

### Step 2: Compile all source files

**Windows (PowerShell):**
```powershell
$files = Get-ChildItem -Path src -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName
javac -d out $files
```

**Linux / macOS / Git Bash:**
```bash
find src -name "*.java" | xargs javac -d out
```

### Step 3: Run the application
```bash
java -cp out Main
```

### Run the regression tests

From the project folder in PowerShell:

```powershell
$files = Get-ChildItem -Path src, tests -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName
javac -d out $files
java -cp out RegressionTests
```

---

## Features

| # | Feature                  | Description                                          |
|---|--------------------------|------------------------------------------------------|
| 1 | Register Member          | Collects details, auto-generates member ID (GYMxxx)  |
| 2 | View All Members         | Lists all members with active/expired status         |
| 3 | View Membership Plans    | Displays Monthly, Quarterly, Half-Yearly, Yearly     |
| 4 | Renew Membership         | Updates expiry date; extends from current expiry     |
| 5 | Mark Attendance          | Records today's date; prevents duplicate entries     |
| 6 | View Attendance          | View by member or view all; shows days present       |
| 7 | Add Trainer              | Registers a trainer; auto-processes waiting list     |
| 8 | Assign Trainer           | Assigns trainer; queues member if none available     |
| 9 | Search Member            | Search by member ID; shows full details              |
|10 | View Reports             | Total/active/expired members, attendance, trainers   |
|11 | View Trainer Assignments | Shows trainer-member pairs and waiting queue         |
|12 | Exit                     | Gracefully exits the application                     |

---

## OOP Concepts Demonstrated

### 1. Encapsulation
- **Where:** `Person`, `Member`, `Trainer`, `MembershipPlan`
- **How:** All fields are `private`. Access is controlled through public getters and setters only.

### 2. Abstraction
- **Where:** `Person.java` (abstract class)
- **How:** `Person` declares the abstract method `getRole()`. Concrete subclasses `Member` and `Trainer` must implement it. Users of `Person` don't need to know its internal structure.

### 3. Inheritance
- **Where:** `Member extends Person`, `Trainer extends Person`
- **How:** Both `Member` and `Trainer` inherit `name`, `age`, `phone`, getters/setters, and `toString()` from `Person`. They add their own specialised fields on top.

### 4. Polymorphism
- **Where:** `Member.getRole()`, `Trainer.getRole()`, both `toString()` methods
- **How:** The same method name `getRole()` behaves differently depending on whether the object is a `Member` or a `Trainer`. Same for `toString()`.

---

## Custom Exceptions

| Exception Class                  | Thrown When                                                   |
|----------------------------------|---------------------------------------------------------------|
| `InvalidMemberException`         | Input validation fails (empty name, bad age, invalid phone)   |
| `MemberNotFoundException`        | A member ID search yields no results                          |
| `DuplicateAttendanceException`   | Attendance is marked twice for the same member on the same date |

All exceptions extend `Exception` (checked), so callers must handle them explicitly.

---

## DSA Implementation

**Class:** `dsa/WaitingQueue.java`

A **custom singly-linked-node Queue** (not `java.util.LinkedList` or `java.util.Queue`).

| Method      | Description                                 |
|-------------|---------------------------------------------|
| `enqueue()` | Adds a node at the rear of the queue        |
| `dequeue()` | Removes and returns the node at the front   |
| `peek()`    | Returns the front node without removing it  |
| `isEmpty()` | Returns true if no nodes exist              |
| `size()`    | Returns current node count                  |
| `display()` | Traverses and prints all nodes              |

**Usage in the System:**
When a member tries to get a trainer assigned but no trainers are available, their member ID is enqueued into the `WaitingQueue`. When a new trainer is added via option 7, the system automatically dequeues all waiting members and assigns the new trainer to them.

---

## Collections Used

| Collection                        | Used In              | Purpose                                    |
|-----------------------------------|----------------------|--------------------------------------------|
| `ArrayList<Member>`               | `MemberService`      | Stores all registered members              |
| `HashMap<String, MembershipPlan>` | `MemberService`      | Maps plan code (1-4) to plan objects       |
| `HashMap<String, List<String>>`   | `AttendanceService`  | Maps memberId to list of attendance dates  |
| `HashSet<String>`                 | `AttendanceService`  | Detects duplicate attendance entries       |
| `HashMap<String, Trainer>`        | `TrainerService`     | Trainer registry by trainer ID             |
| `HashMap<String, List<String>>`   | `TrainerService`     | Maps trainerId to assigned memberIds       |

---

## Membership Plans

| Code | Plan Name    | Duration | Fee (INR) |
|------|-------------|----------|-----------|
| 1    | Monthly     | 1 month  | ₹999      |
| 2    | Quarterly   | 3 months | ₹2,499    |
| 3    | Half-Yearly | 6 months | ₹4,499    |
| 4    | Yearly      | 12 months| ₹7,999    |

---

## Sample Console Output

```
======================================================================
       GYM MEMBERSHIP MANAGEMENT SYSTEM
======================================================================
   1.  Register Member
   2.  View All Members
   ...
   12. Exit
======================================================================
Enter your choice: 1

----------------------------------------------------------------------
  REGISTER NEW MEMBER
----------------------------------------------------------------------
  Enter name           : Rahul Sharma
  Enter age            : 25
  Enter phone (10 digits): 9876543210
  Enter joining date (yyyy-MM-dd) [leave blank for today]:

  [1] Monthly         | Duration:  1 month(s)  | Fee: Rs. 999.00
  [2] Quarterly       | Duration:  3 month(s)  | Fee: Rs. 2499.00
  [3] Half-Yearly     | Duration:  6 month(s)  | Fee: Rs. 4499.00
  [4] Yearly          | Duration: 12 month(s)  | Fee: Rs. 7999.00
  Select plan (1-4)    : 2
[OK]  Member registered successfully!
  Member ID  : GYM001
  Expiry Date: 2027-02-20
  Plan       : Quarterly
  Fee Paid   : Rs. 2499.00
```

---

## Viva Questions & Answers

**Q1. What is Encapsulation? Where is it used in this project?**
A: Encapsulation is the OOP principle of bundling data (fields) and methods together and restricting direct access to the data. In this project, all model classes (`Person`, `Member`, `Trainer`, `MembershipPlan`) have `private` fields accessed only through public getters and setters.

**Q2. What is Abstraction? How is it demonstrated?**
A: Abstraction hides implementation details and exposes only what is necessary. In this project, `Person` is an abstract class with the abstract method `getRole()`. Callers work with `Person` references without knowing whether the actual object is a `Member` or `Trainer`.

**Q3. What is Inheritance? Which classes use it?**
A: Inheritance allows a subclass to reuse and extend the properties and methods of a parent class. `Member` and `Trainer` both inherit from `Person`, gaining `name`, `age`, `phone`, and common getters without duplicating code.

**Q4. What is Polymorphism? Give an example from the project.**
A: Polymorphism means the same method name behaves differently depending on the object type. `getRole()` returns "Member" when called on a `Member` object and "Trainer" when called on a `Trainer` object. Same method, different behaviour.

**Q5. What are Custom Exceptions? Why are they used?**
A: Custom exceptions are user-defined exception classes that extend `Exception`. They make error messages meaningful and allow callers to catch specific error types. For example, `DuplicateAttendanceException` clearly signals that a member's attendance was already marked today, which a generic `Exception` would not.

**Q6. Explain your DSA implementation.**
A: `WaitingQueue<T>` is a manually coded Queue using singly-linked `Node` objects (not `java.util.Queue`). It has `enqueue()` (insert at rear), `dequeue()` (remove from front), and `peek()` operations. It is used as a trainer waiting list. Members who cannot be immediately assigned a trainer are added to this queue and auto-assigned when a new trainer registers.

**Q7. What is the difference between ArrayList and LinkedList?**
A: `ArrayList` uses a dynamic array internally. It offers O(1) random access but O(n) insert/delete in the middle. `LinkedList` uses doubly-linked nodes. It offers O(1) insert/delete at ends but O(n) random access. We use `ArrayList` for members because we mostly iterate and rarely insert in the middle.

**Q8. Why use HashSet for attendance checking instead of iterating the list?**
A: `HashSet` uses hashing, so `contains()` is O(1) on average. If we used a List and looped to check every element for duplicates, it would be O(n). HashSet is significantly faster for large datasets.

**Q9. What is a HashMap? How is it used in this project?**
A: A `HashMap` stores key-value pairs with O(1) average lookup by key. This project uses it to map plan codes to `MembershipPlan` objects, trainer IDs to `Trainer` objects, and member IDs to lists of attendance dates.

**Q10. How does membership renewal work?**
A: The system finds the member by ID, then asks for a new plan. If the current membership is still active, the new expiry is calculated from the existing expiry date (so the member doesn't lose remaining days). If expired, renewal starts from today.

---

## GitHub Workflow

### Individual Repository Setup (15+ Commits)

```bash
# 1. Create a new repo on GitHub (via browser), then:
git init
git remote add origin https://github.com/YOUR_USERNAME/GymMembershipManagement.git

# Suggested Commit Sequence:
git add README.md && git commit -m "Initial commit: Add project README"
git add src/model/Person.java && git commit -m "Add Person abstract base class (OOP: Abstraction)"
git add src/model/Member.java && git commit -m "Add Member class extending Person (OOP: Inheritance)"
git add src/model/Trainer.java && git commit -m "Add Trainer class extending Person (OOP: Inheritance)"
git add src/model/MembershipPlan.java && git commit -m "Add MembershipPlan model (Encapsulation)"
git add src/exception/ && git commit -m "Add custom exceptions: InvalidMember, MemberNotFound, DuplicateAttendance"
git add src/dsa/WaitingQueue.java && git commit -m "Implement custom linked-node Queue for trainer waiting list (DSA)"
git add src/util/ && git commit -m "Add DateUtil and ConsoleUtil utility classes"
git add src/service/MemberService.java && git commit -m "Implement MemberService: register, display, search, renew"
git add src/service/AttendanceService.java && git commit -m "Implement AttendanceService with HashSet duplicate prevention"
git add src/service/TrainerService.java && git commit -m "Implement TrainerService with HashMap and WaitingQueue DSA"
git add src/service/ReportService.java && git commit -m "Implement ReportService: member stats, attendance summary, trainer report"
git add src/Main.java && git commit -m "Add Main.java: menu-driven console interface"
git add . && git commit -m "Fix compilation: update Javadoc to remove illegal unicode escapes"
git add . && git commit -m "Test all 12 menu options and verify exception handling"
git add . && git commit -m "Final review: code comments, naming conventions, clean structure"

git push -u origin main
```

### Creating a PR in a Teammate's Repository

```bash
# 1. Fork your teammate's repository on GitHub (click Fork button)
# 2. Clone your fork
git clone https://github.com/YOUR_USERNAME/TEAMMATE_REPO.git
cd TEAMMATE_REPO

# 3. Create a feature branch
git checkout -b review/add-viva-notes

# 4. Make a small, meaningful change (e.g., add a viva Q&A section to README)
# Edit a file, then:
git add README.md
git commit -m "Add viva Q&A section to README for college submission"
git push origin review/add-viva-notes

# 5. Go to GitHub -> Open Pull Request from your branch to teammate's main
```

### Reviewing and Merging a PR (as the Repository Owner)

1. Go to your GitHub repository → **Pull Requests** tab.
2. Click the incoming PR.
3. Review the **Files changed** tab — read every changed line.
4. Add comments or approval using the **Review changes** button.
5. Click **Approve** then **Merge pull request** → **Confirm merge**.
6. Optionally delete the feature branch after merging.
