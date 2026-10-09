-- =====================================================================
-- Athena Library Management System - Database Schema DDL
-- Target RDBMS: MySQL 8.0+
-- Database Name: library_management
-- =====================================================================

CREATE DATABASE IF NOT EXISTS `library_management` 
    DEFAULT CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

USE `library_management`;

-- 1. Books Table
DROP TABLE IF EXISTS `loans`;
DROP TABLE IF EXISTS `books`;
CREATE TABLE `books` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(255) NOT NULL,
    `author` VARCHAR(255) NOT NULL,
    `isbn` VARCHAR(30) NOT NULL UNIQUE,
    `category` VARCHAR(50) NOT NULL,
    `publisher` VARCHAR(200) NULL,
    `publication_year` INT NULL,
    `total_copies` INT NOT NULL DEFAULT 1,
    `available_copies` INT NOT NULL DEFAULT 1,
    `location_shelf` VARCHAR(100) NULL,
    `description` TEXT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_books_category` (`category`),
    INDEX `idx_books_title` (`title`),
    INDEX `idx_books_author` (`author`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Members Table
DROP TABLE IF EXISTS `members`;
CREATE TABLE `members` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `membership_number` VARCHAR(50) NOT NULL UNIQUE,
    `name` VARCHAR(150) NOT NULL,
    `email` VARCHAR(150) NOT NULL UNIQUE,
    `phone` VARCHAR(30) NOT NULL,
    `registration_date` DATE NOT NULL,
    `status` VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    `notes` TEXT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_members_email` (`email`),
    INDEX `idx_members_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Loans Table (Circulation Transactions)
CREATE TABLE `loans` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `book_id` BIGINT NOT NULL,
    `member_id` BIGINT NOT NULL,
    `issue_date` DATE NOT NULL,
    `due_date` DATE NOT NULL,
    `return_date` DATE NULL,
    `status` VARCHAR(30) NOT NULL DEFAULT 'ISSUED',
    `notes` TEXT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_loans_book` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE RESTRICT,
    CONSTRAINT `fk_loans_member` FOREIGN KEY (`member_id`) REFERENCES `members` (`id`) ON DELETE RESTRICT,
    INDEX `idx_loans_status` (`status`),
    INDEX `idx_loans_due_date` (`due_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

