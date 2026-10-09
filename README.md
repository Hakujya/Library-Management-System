na# 📚 Kaizen LMS — Library Management System

A full-stack **Library Management System** built as an academic college project using **Java 21** and **Spring Boot**. Kaizen LMS allows librarians to manage books, members, and loan circulation from a clean, modern dark-themed web interface.

---

## ✨ Features

- 📖 **Book Catalog** — Add, edit, search, and delete books with category and shelf tracking
- 👥 **Member Management** — Register and manage library members with status tracking
- 🔄 **Loan Circulation** — Issue and return books, track due dates and overdue loans
- 📊 **Dashboard** — Real-time stats: total books, available copies, active members, overdue loans
- 🌑 **Dark Theme UI** — Modern dark interface built with Bootstrap 5
- 🔐 **Sign-In Page** — Opens first; any entered username and password continue to the dashboard
- ⚠️ **Overdue Alerts** — Automatic overdue detection with visual warnings

---

## 🛠 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.x |
| ORM | Spring Data JPA / Hibernate |
| Templating | Thymeleaf |
| Database | H2 (dev) / MySQL (prod) |
| Frontend | Bootstrap 5, Bootstrap Icons |
| Build Tool | Maven |

---

## 🚀 How to Run

### Prerequisites
- Git
- Java 21 or newer

### Steps

Clone the repository and enter its folder:

```bash
git clone https://github.com/Hakujya/Library-Management-System.git
cd Library-Management-System
```

Run the application using the included Maven wrapper:

```powershell
# Windows
.\mvnw.cmd spring-boot:run
```

```bash
# macOS/Linux
./mvnw spring-boot:run
```

No separate database setup is needed for the default development configuration; it uses an in-memory H2 database. Once the app has started, open **http://localhost:8080** in your browser. The sign-in page is visual only: enter any username and password to continue. Data may reset when the application stops.

---

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/kaizen/lms/
│   │   ├── config/          # Data initializer
│   │   ├── controller/      # MVC Controllers
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── entity/          # JPA Entities
│   │   ├── enums/           # Status & Category enums
│   │   ├── exception/       # Custom exceptions & handler
│   │   ├── repository/      # Spring Data JPA Repositories
│   │   └── service/         # Service layer + implementations
│   └── resources/
│       ├── static/          # CSS, JS assets
│       └── templates/       # Thymeleaf HTML templates
└── test/                    # Unit & Integration tests
```

---

## 📸 Screenshots

> Dashboard · Book Catalog · Loan Circulation — all with dark theme

---

## 👨‍💻 Author

Built as an Academic College Project — 2026
