-- =====================================================================
-- Athena Library Management System - Sample Initial Data Script
-- Target RDBMS: MySQL 8.0+
-- Database Name: library_management
-- =====================================================================

USE `library_management`;

-- 1. Insert Sample Books
INSERT INTO `books` (`id`, `title`, `author`, `isbn`, `category`, `publisher`, `publication_year`, `total_copies`, `available_copies`, `location_shelf`, `description`, `created_at`, `updated_at`) VALUES
(1, 'Clean Architecture: A Craftsman\'s Guide', 'Robert C. Martin', '978-0134494166', 'COMPUTER_SCIENCE', 'Prentice Hall', 2017, 5, 4, 'Shelf CS-101', 'Practical rules for crafting lasting software architectures and system boundaries.', NOW(), NOW()),
(2, 'Introduction to Algorithms (CLRS)', 'Thomas H. Cormen, Charles E. Leiserson', '978-0262033848', 'COMPUTER_SCIENCE', 'MIT Press', 2022, 4, 2, 'Shelf CS-102', 'Comprehensive reference text on data structures, graph theory, and algorithmic complexity.', NOW(), NOW()),
(3, 'Effective Java (3rd Edition)', 'Joshua Bloch', '978-0134685991', 'COMPUTER_SCIENCE', 'Addison-Wesley Professional', 2018, 3, 2, 'Shelf CS-103', 'The definitive guide to best practices and idiomatic design patterns in Java.', NOW(), NOW()),
(4, 'The Pragmatic Programmer', 'David Thomas, Andrew Hunt', '978-0135957059', 'COMPUTER_SCIENCE', 'Addison-Wesley', 2019, 4, 4, 'Shelf CS-104', 'Your journey to mastery in software craftsmanship, testing, and continuous learning.', NOW(), NOW()),
(5, 'A Brief History of Time', 'Stephen Hawking', '978-0553380163', 'SCIENCE', 'Bantam Books', 1998, 3, 3, 'Shelf SCI-201', 'Explores black holes, the big bang, general relativity, and space-time.', NOW(), NOW()),
(6, 'Sapiens: A Brief History of Humankind', 'Yuval Noah Harari', '978-0062316097', 'HISTORY', 'Harper', 2015, 4, 3, 'Shelf HIST-302', 'Explores how biology and history have shaped modern humanity over millennia.', NOW(), NOW()),
(7, 'Meditations', 'Marcus Aurelius', '978-0140449334', 'PHILOSOPHY', 'Penguin Classics', 2006, 2, 2, 'Shelf PHIL-401', 'Private philosophical reflections and stoic principles from the Roman emperor.', NOW(), NOW()),
(8, 'Design Patterns: Elements of Reusable Object-Oriented Software', 'Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides', '978-0201633610', 'COMPUTER_SCIENCE', 'Addison-Wesley', 1994, 3, 1, 'Shelf CS-105', 'Foundational catalogue of 23 classic software design patterns.', NOW(), NOW());

-- 2. Insert Sample Members
INSERT INTO `members` (`id`, `membership_number`, `name`, `email`, `phone`, `registration_date`, `status`, `notes`, `created_at`, `updated_at`) VALUES
(1, 'MEM-2026-1001', 'Aarav Sharma', 'aarav.sharma@college.edu', '+91 98765 43210', '2026-04-01', 'ACTIVE', 'Computer Science Undergraduate, 3rd Year', NOW(), NOW()),
(2, 'MEM-2026-1002', 'Priya Patel', 'priya.patel@college.edu', '+91 98765 12345', '2026-06-15', 'ACTIVE', 'Information Technology Graduate Scholar', NOW(), NOW()),
(3, 'MEM-2026-1003', 'Dr. Vikram Malhotra', 'vikram.malhotra@college.edu', '+91 98111 22334', '2025-08-10', 'ACTIVE', 'Faculty - Department of Computer Engineering', NOW(), NOW()),
(4, 'MEM-2026-1004', 'Sneha Rao', 'sneha.rao@college.edu', '+91 98222 33445', '2026-08-01', 'ACTIVE', 'Physics & Mathematics Student', NOW(), NOW()),
(5, 'MEM-2026-1005', 'Rohan Verma', 'rohan.verma@college.edu', '+91 98333 44556', '2026-02-14', 'ACTIVE', 'Mechanical Engineering, 2nd Year', NOW(), NOW());

-- 3. Insert Sample Loans
INSERT INTO `loans` (`id`, `book_id`, `member_id`, `issue_date`, `due_date`, `return_date`, `status`, `notes`, `created_at`, `updated_at`) VALUES
(1, 1, 1, DATE_SUB(CURDATE(), INTERVAL 7 DAY), DATE_ADD(CURDATE(), INTERVAL 7 DAY), NULL, 'ISSUED', 'Standard borrow for course project', NOW(), NOW()),
(2, 2, 2, DATE_SUB(CURDATE(), INTERVAL 11 DAY), DATE_ADD(CURDATE(), INTERVAL 3 DAY), NULL, 'ISSUED', 'Reference material for midterm exams', NOW(), NOW()),
(3, 2, 5, DATE_SUB(CURDATE(), INTERVAL 21 DAY), DATE_SUB(CURDATE(), INTERVAL 7 DAY), NULL, 'ISSUED', 'Automated overdue reminder sent', NOW(), NOW()),
(4, 3, 3, DATE_SUB(CURDATE(), INTERVAL 4 DAY), DATE_ADD(CURDATE(), INTERVAL 10 DAY), NULL, 'ISSUED', 'Faculty course prep reference', NOW(), NOW()),
(5, 8, 4, DATE_SUB(CURDATE(), INTERVAL 9 DAY), DATE_ADD(CURDATE(), INTERVAL 5 DAY), NULL, 'ISSUED', 'Design patterns study', NOW(), NOW()),
(6, 8, 1, DATE_SUB(CURDATE(), INTERVAL 2 DAY), DATE_ADD(CURDATE(), INTERVAL 12 DAY), NULL, 'ISSUED', 'Assigned reading', NOW(), NOW()),
(7, 6, 2, DATE_SUB(CURDATE(), INTERVAL 30 DAY), DATE_SUB(CURDATE(), INTERVAL 16 DAY), DATE_SUB(CURDATE(), INTERVAL 17 DAY), 'RETURNED', 'Returned on time in pristine condition', NOW(), NOW()),
(8, 5, 4, DATE_SUB(CURDATE(), INTERVAL 25 DAY), DATE_SUB(CURDATE(), INTERVAL 11 DAY), DATE_SUB(CURDATE(), INTERVAL 12 DAY), 'RETURNED', 'Returned on time', NOW(), NOW());

