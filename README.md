# 📚 Athena Library Management System (LMS)

A college-project-grade, full-stack **Library Management System** engineered with **Java 21**, **Spring Boot**, **Spring Data JPA / Hibernate**, **Thymeleaf**, and **MySQL**.

This project provides real Java backend business logic, database transaction integrity, automated overdue detection, inventory tracking, and a cohesive, responsive UI built with a modern slate-and-blue theme.

---

## 🌟 Key Highlights & Features

### 1. 📊 Interactive Real-Time Dashboard
- **Computed Database Statistics**: Computes total titles, total copy volume, available shelf copies, active member registrations, and active borrowings dynamically.
- **Automated Overdue Tracking**: Real-time identification of overdue borrowings with alert banners and overdue day counters.
- **Category Distribution**: Dynamic breakdown of library inventory across categories computed using Java Streams `Collectors.groupingBy`.
- **Circulation Activity Feed**: Live audit stream of the most recent book issuances and returns with one-click return actions.

### 2. 📖 Comprehensive Book Catalog (CRUD)
- **Catalog Management**: Add, view, edit, and safely delete books.
- **Inventory & Copy Tracking**: Tracks total copies vs. available copies.
- **Search & Filter**: Real-time search by Title, Author, or ISBN, plus categorical genre filtering.
- **Active Loan Protection**: Prevents deletion of books that have active, unreturned borrowings.
- **Integrity Validation**: Strict ISBN formatting validation and duplicate ISBN detection.

### 3. 👥 Member Management
- **Registration & Profiles**: Registers students and faculty with auto-generated membership card numbers (`MEM-YYYY-XXXX`).
- **Contact Validation**: Validates RFC-compliant email addresses and phone numbers.
- **Duplicate Prevention**: Guaranteed email uniqueness across the system.
- **Borrower Audit**: Detailed member profile page showing current active borrowings and lifetime borrowing history.
- **Safe Deletion**: Prevents deletion of any member with active unreturned books.

### 4. 🔄 Circulation & Book Loan System
- **ACID Transactional Issuing**: Atomic operations decrement available inventory and create circulation records in a single database transaction.
- **Zero-Stock Prevention**: Strictly prevents issuing books when zero copies remain on shelves.
- **Membership Status Verification**: Ensures only active members can borrow books.
- **Safe Returns**: Atomic return processing that marks return dates, updates status, and restores shelf inventory (+1 copy).
- **Duplicate Return Protection**: Rejects duplicate returns on already returned loan records.
- **Tabbed Loan Management**: Dedicated views for **Active Loans**, **Overdue Loans**, **Returned Loans**, and **All Records**.

### 5. 🎨 Modern User Interface
- **Cohesive Color Palette**: Professional Slate (`#0f172a`), Deep Blue (`#1d4ed8`), Emerald Green, and Amber Warning accents.
- **Responsive Layout**: Collapsible sidebar, mobile drawer navigation, responsive data tables.
- **Safety Confirmations**: Modal dialogs for destructive actions (Delete, Return).
- **Feedback & Alerts**: Dismissible flash alerts for all database actions.
- **Zero Raw Stack Traces**: Friendly error templates (`error/error.html`) for 404, 409, and 500 scenarios.

---

## ☕ Java Knowledge & Architecture Demonstrated

This project is built to demonstrate Java and software architecture principles:

| Concept | Implementation in Code |
| :--- | :--- |
| **Object-Oriented Programming** | Encapsulated entity classes (`Book`, `Member`, `Loan`) with private fields, validation constraints, and domain logic methods. |
| **Inheritance** | `BaseEntity` mapped superclass providing common IDs and `@PrePersist`/`@PreUpdate` auditing across entities. |
| **Interface-Driven Design** | Clean service layer abstraction: `BookService`, `MemberService`, `LoanService`, `DashboardService` with distinct implementations. |
| **Polymorphism** | Dynamic dispatch across service interfaces, status check methods, and polymorphic equals/hashCode methods. |
| **Generics & Collections** | Java Collections (`List<T>`, `Map<K,V>`, `ArrayList<T>`), parameterized `JpaRepository<T, ID>`, and `Optional<T>` handling. |
| **Java Enums** | Type-safe enumerations with custom properties and badge styling: `LoanStatus`, `MemberStatus`, and `BookCategory`. |
| **Java Streams API** | Stream operations (`filter()`, `map()`, `sorted()`, `groupingBy()`, `collect()`) for computing catalog distribution and overdue records. |
| **Custom Exceptions** | Hierarchical unchecked exceptions: `LibraryException` &rarr; `ResourceNotFoundException`, `BookNotAvailableException`, `DuplicateResourceException`, `InvalidLoanOperationException`. |
| **Global Exception Handling** | `@ControllerAdvice` (`GlobalExceptionHandler`) translating domain exceptions into user-friendly error views with zero internal leaks. |
| **Transactional Semantics** | `@Transactional` annotations on circulation mutations to preserve database consistency. |
| **Bean Validation** | Jakarta Validation (`@NotBlank`, `@Min`, `@Email`, `@Pattern`, `@FutureOrPresent`) with inline UI feedback. |

---

## 🛠️ Technology Stack

- **Backend**: Java 21 LTS, Spring Boot 4.1.1 (Spring Framework 7)
- **Persistence**: Spring Data JPA, Hibernate 7, Jakarta Persistence
- **Template Engine**: Thymeleaf 3.1
- **Database**: 
  - **MySQL 8.0+** (Production Profile)
  - **H2 Database** (Development & Automated Testing Profile)
- **Build & Dependency Management**: Apache Maven (via included Maven Wrapper `mvnw.cmd` / `mvnw`)
- **Frontend / Styling**: HTML5, CSS3, JavaScript, Bootstrap 5.3.3, Bootstrap Icons 1.11.3, Inter Font
- **Testing**: JUnit 5, Mockito, Spring Boot Test, Spring MVC MockMvc

---

## 📂 Project Directory Structure

```text
project-java/
├── .gitignore                          # Production-grade Git ignore file
├── application.properties.example      # Example database configuration template
├── mvnw & mvnw.cmd                     # Maven wrapper scripts (no separate Maven required)
├── pom.xml                             # Maven project dependencies and build configuration
├── README.md                           # Comprehensive documentation
│
├── database/                           # Database migration and seed scripts
│   ├── schema.sql                      # MySQL database schema (DDL)
│   └── sample-data.sql                 # Sample MySQL seed data (DML)
│
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── DemoApplication.java    # Spring Boot Main Entry Point
│   │   │   ├── config/
│   │   │   │   └── DataInitializer.java# Development sample data seeder
│   │   │   ├── controller/
│   │   │   │   ├── DashboardController.java
│   │   │   │   ├── BookController.java
│   │   │   │   ├── MemberController.java
│   │   │   │   └── LoanController.java
│   │   │   ├── dto/
│   │   │   │   ├── BookFormDto.java
│   │   │   │   ├── MemberFormDto.java
│   │   │   │   ├── LoanIssueFormDto.java
│   │   │   │   └── DashboardStatsDto.java
│   │   │   ├── entity/
│   │   │   │   ├── BaseEntity.java
│   │   │   │   ├── Book.java
│   │   │   │   ├── Member.java
│   │   │   │   └── Loan.java
│   │   │   ├── enums/
│   │   │   │   ├── BookCategory.java
│   │   │   │   ├── LoanStatus.java
│   │   │   │   └── MemberStatus.java
│   │   │   ├── exception/
│   │   │   │   ├── LibraryException.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── BookNotAvailableException.java
│   │   │   │   ├── DuplicateResourceException.java
│   │   │   │   ├── InvalidLoanOperationException.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── repository/
│   │   │   │   ├── BookRepository.java
│   │   │   │   ├── MemberRepository.java
│   │   │   │   └── LoanRepository.java
│   │   │   └── service/
│   │   │       ├── BookService.java & impl/BookServiceImpl.java
│   │   │       ├── MemberService.java & impl/MemberServiceImpl.java
│   │   │       ├── LoanService.java & impl/LoanServiceImpl.java
│   │   │       └── DashboardService.java & impl/DashboardServiceImpl.java
│   │   │
│   │   └── resources/
│   │       ├── application.yml         # Base application configuration
│   │       ├── application-dev.yml     # Zero-setup H2 dev profile (default)
│   │       ├── application-prod.yml    # Production MySQL profile
│   │       ├── static/
│   │       │   ├── css/styles.css      # Cohesive Slate & Royal Blue theme
│   │       │   └── js/app.js           # Client interactions and modal wiring
│   │       └── templates/
│   │           ├── dashboard.html      # Overview with KPIs and recent loans
│   │           ├── layout/fragments.html # Sidebar, navbar, alerts, modals
│   │           ├── books/
│   │           │   ├── list.html       # Catalog with search & filter
│   │           │   ├── form.html       # Add/Edit book form
│   │           │   └── details.html    # Book profile & circulation history
│   │           ├── members/
│   │           │   ├── list.html       # Member directory
│   │           │   ├── form.html       # Member register/edit form
│   │           │   └── details.html    # Member profile & borrowing records
│   │           ├── loans/
│   │           │   ├── list.html       # Active, overdue, returned tabs
│   │           │   └── issue-form.html # Issue book form
│   │           └── error/
│   │               └── error.html      # Friendly error view (no stack traces)
│   │
│   └── test/java/com/example/demo/
│       ├── DemoApplicationTests.java   # Spring Context Bootstrapping Test
│       ├── controller/
│       │   └── WebControllerIntegrationTest.java # 9 UI End-to-end tests
│       └── service/
│           ├── BookServiceTest.java    # 7 Unit tests
│           ├── MemberServiceTest.java  # 6 Unit tests
│           ├── LoanServiceTest.java    # 7 Unit tests
│           └── DashboardServiceTest.java # 1 Analytics test
```

---

## 🚀 Getting Started on Windows

### Prerequisites
1. **Java Development Kit (JDK 21)**
   Verify installation in PowerShell or Command Prompt:
   ```powershell
   java -version
   ```
2. **Git** (for version control):
   ```powershell
   git --version
   ```
3. *(Optional for MySQL)* **MySQL Server 8.0+** running locally.

---

### Option A: Quick Start (Zero Setup with In-Memory H2 DB)
The project comes pre-configured with a development profile (`dev`) that runs immediately without installing or configuring MySQL:

1. Open PowerShell in the project directory:
   ```powershell
   cd "C:\Users\ayush\Downloads\project java"
   ```
2. Run the application using the Maven wrapper:
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```
3. Open your browser and navigate to:
   ```text
   http://localhost:8080/
   ```
   *The application will automatically start with sample books, members, active loans, and overdue records ready for immediate exploration!*

---

### Option B: Running with MySQL 8.0

1. **Start MySQL Server**:
   Ensure your local MySQL service is running. In PowerShell (Administrator):
   ```powershell
   Start-Service MySQL80
   ```
   Or launch MySQL through Windows Services (`services.msc`).

2. **Create the Database**:
   Open MySQL Command Line Client or MySQL Workbench and run:
   ```sql
   CREATE DATABASE IF NOT EXISTS library_management DEFAULT CHARACTER SET utf8mb4;
   ```
   *(Optional)* You can run `database/schema.sql` and `database/sample-data.sql` to import data manually.

3. **Configure Database Credentials**:
   You can supply your MySQL credentials via Windows environment variables or an `application.properties` file:

   **Method 1: Environment Variables (Recommended & Secure)**
   ```powershell
   $env:SPRING_DATASOURCE_URL="jdbc:mysql://localhost:3306/library_management?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
   $env:SPRING_DATASOURCE_USERNAME="root"
   $env:SPRING_DATASOURCE_PASSWORD="your_mysql_password_here"
   ```

   **Method 2: Local Configuration File**
   Copy `application.properties.example` to `src/main/resources/application.properties` (this file is excluded from Git in `.gitignore`):
   ```properties
   spring.profiles.active=prod
   spring.datasource.url=jdbc:mysql://localhost:3306/library_management?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   spring.datasource.username=root
   spring.datasource.password=your_mysql_password_here
   ```

4. **Launch the Application**:
   ```powershell
   .\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod
   ```

---

## 🧪 Running Automated Tests

A test suite of **31 unit and integration tests** is included covering all business logic, validation rules, stock constraints, safe deletions, and UI template rendering:

Run all tests from PowerShell:
```powershell
.\mvnw.cmd test
```

### Test Coverage Summary:
- **`BookServiceTest` (7 tests)**: Adding books, rejecting duplicate ISBNs, enforcing minimum copies constraints, safe deletion with active loan locks, Java Streams filtering.
- **`MemberServiceTest` (6 tests)**: Member registration, duplicate email rejection, active borrowing deletion lock, Streams filtering.
- **`LoanServiceTest` (7 tests)**: Issuing books with copy decrements, preventing issues when stock is 0, blocking inactive members, returning books with copy restoration, duplicate return protection, overdue stream identification.
- **`DashboardServiceTest` (1 test)**: Live statistical aggregations and category groupings.
- **`WebControllerIntegrationTest` (9 tests)**: End-to-end MVC endpoint rendering and Thymeleaf template validation.
- **`DemoApplicationTests` (1 test)**: Spring Boot context initialization and database schema bootstrap.

---

## 🖼️ User Interface Screenshots

*Below are UI layout previews of the Athena Library Management System:*

| Dashboard Overview | Book Catalog Directory |
| :---: | :---: |
| ![Dashboard Preview](https://placehold.co/600x380/0f172a/ffffff?text=Dashboard+Overview+%26+Live+KPIs) | ![Catalog Preview](https://placehold.co/600x380/1d4ed8/ffffff?text=Book+Catalog+%26+Stock+Badges) |

| Book Circulation & Loans | Member Details & Audit |
| :---: | :---: |
| ![Loans Preview](https://placehold.co/600x380/0284c7/ffffff?text=Circulation+Loans+%26+Overdue+Tabs) | ![Member Details](https://placehold.co/600x380/334155/ffffff?text=Member+Profile+%26+Borrowing+History) |

*(To replace with your own screenshots: save PNG screenshots into `docs/screenshots/` and update the markdown image paths).*

---

## 🔧 Common Errors & Troubleshooting

### 1. `Port 8080 is already in use`
- **Cause**: Another process or background instance of Spring Boot is running on port 8080.
- **Fix**: Check and stop the process using PowerShell:
  ```powershell
  Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue | Select-Object OwningProcess
  Stop-Process -Id <PID> -Force
  ```
  Or change the port in `application.yml` (`server.port: 8081`).

### 2. `Communications link failure` (MySQL)
- **Cause**: MySQL service is not running or the port (3306) is incorrect.
- **Fix**: Run `Get-Service MySQL80` and `Start-Service MySQL80`. If MySQL is not installed, simply run using the default dev profile (`.\mvnw.cmd spring-boot:run`) which runs with zero setup.

### 3. `Access denied for user 'root'@'localhost'`
- **Cause**: Incorrect database password.
- **Fix**: Verify your password or reset the environment variable `$env:SPRING_DATASOURCE_PASSWORD="your_password"`.

---

## 📦 Publishing to GitHub

To publish this project to your GitHub account:

1. **Initialize Git** in the project folder:
   ```powershell
   git init
   ```
2. **Review files to stage** (passwords and build artifacts are automatically excluded by `.gitignore`):
   ```powershell
   git status
   git add .
   git commit -m "feat: complete Library Management System with Java 21, Spring Boot, JPA, and Thymeleaf"
   ```
3. **Create a new empty repository** on [GitHub](https://github.com/new) named `library-management-system`.
4. **Push the code**:
   ```powershell
   git branch -M main
   git remote add origin https://github.com/<your-username>/library-management-system.git
   git push -u origin main
   ```

---

## 🔮 Future Enhancements
- Fine calculation system for overdue loans.
- Barcode / QR scanner integration for rapid book checkout.
- Member self-service portal with student login.
- Automated email alerts for upcoming return dates.

---
**Academic Project** &bull; Department of Computer Science & Engineering &bull; 2026
