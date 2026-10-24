-- ====================================================================
-- MediCartPro - Normalized Database Schema for medicart_db
-- ====================================================================

CREATE DATABASE IF NOT EXISTS `medicart_db`
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE `medicart_db`;

-- 1. USERS TABLE
CREATE TABLE IF NOT EXISTS `users` (
    `user_id` INT AUTO_INCREMENT PRIMARY KEY,
    `full_name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(150) NOT NULL UNIQUE,
    `password_hash` VARCHAR(255) NOT NULL,
    `phone` VARCHAR(20) DEFAULT NULL,
    `address` TEXT DEFAULT NULL,
    `role` ENUM('CUSTOMER', 'ADMIN') NOT NULL DEFAULT 'CUSTOMER',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_users_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. MEDICINES TABLE
CREATE TABLE IF NOT EXISTS `medicines` (
    `medicine_id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(150) NOT NULL UNIQUE,
    `description` TEXT DEFAULT NULL,
    `category` VARCHAR(50) NOT NULL DEFAULT 'All Medicines',
    `dosage_form` VARCHAR(50) DEFAULT NULL,
    `price` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    `prescription_required` BOOLEAN NOT NULL DEFAULT FALSE,
    `image_file` VARCHAR(255) DEFAULT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_medicines_category` (`category`),
    INDEX `idx_medicines_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. PHARMACIES TABLE
CREATE TABLE IF NOT EXISTS `pharmacies` (
    `pharmacy_id` INT AUTO_INCREMENT PRIMARY KEY,
    `pharmacy_name` VARCHAR(150) NOT NULL,
    `address` TEXT NOT NULL,
    `contact_number` VARCHAR(20) NOT NULL,
    `email` VARCHAR(150) DEFAULT NULL,
    `registration_status` ENUM('PENDING', 'ACTIVE', 'SUSPENDED') NOT NULL DEFAULT 'ACTIVE',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_pharmacies_status` (`registration_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. PHARMACY INVENTORY TABLE
CREATE TABLE IF NOT EXISTS `pharmacy_inventory` (
    `inventory_id` INT AUTO_INCREMENT PRIMARY KEY,
    `pharmacy_id` INT NOT NULL,
    `medicine_id` INT NOT NULL,
    `available_quantity` INT NOT NULL DEFAULT 0,
    `price` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY `uq_pharmacy_medicine` (`pharmacy_id`, `medicine_id`),
    CONSTRAINT `fk_inventory_pharmacy` FOREIGN KEY (`pharmacy_id`)
        REFERENCES `pharmacies` (`pharmacy_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_inventory_medicine` FOREIGN KEY (`medicine_id`)
        REFERENCES `medicines` (`medicine_id`) ON DELETE CASCADE,
    CONSTRAINT `chk_inventory_stock_non_negative` CHECK (`available_quantity` >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. PRESCRIPTIONS TABLE
CREATE TABLE IF NOT EXISTS `prescriptions` (
    `prescription_id` INT AUTO_INCREMENT PRIMARY KEY,
    `customer_id` INT NOT NULL,
    `file_path` VARCHAR(500) NOT NULL,
    `verification_status` ENUM('PENDING', 'VERIFIED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    `reviewer_id` INT DEFAULT NULL,
    `reviewer_note` TEXT DEFAULT NULL,
    `verified_at` TIMESTAMP NULL DEFAULT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_prescriptions_status` (`verification_status`),
    CONSTRAINT `fk_prescriptions_customer` FOREIGN KEY (`customer_id`)
        REFERENCES `users` (`user_id`) ON DELETE RESTRICT,
    CONSTRAINT `fk_prescriptions_reviewer` FOREIGN KEY (`reviewer_id`)
        REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. PRESCRIPTION ITEMS TABLE
CREATE TABLE IF NOT EXISTS `prescription_items` (
    `prescription_item_id` INT AUTO_INCREMENT PRIMARY KEY,
    `prescription_id` INT NOT NULL,
    `medicine_id` INT NOT NULL,
    `required_quantity` INT NOT NULL DEFAULT 1,
    `dosage_instructions` TEXT DEFAULT NULL,
    CONSTRAINT `fk_prescription_items_prescription` FOREIGN KEY (`prescription_id`)
        REFERENCES `prescriptions` (`prescription_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_prescription_items_medicine` FOREIGN KEY (`medicine_id`)
        REFERENCES `medicines` (`medicine_id`) ON DELETE RESTRICT,
    CONSTRAINT `chk_prescription_item_qty` CHECK (`required_quantity` > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. CART ITEMS TABLE
CREATE TABLE IF NOT EXISTS `cart_items` (
    `cart_item_id` INT AUTO_INCREMENT PRIMARY KEY,
    `customer_id` INT NOT NULL,
    `medicine_id` INT NOT NULL,
    `quantity` INT NOT NULL DEFAULT 1,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uq_cart_customer_medicine` (`customer_id`, `medicine_id`),
    CONSTRAINT `fk_cart_customer` FOREIGN KEY (`customer_id`)
        REFERENCES `users` (`user_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_cart_medicine` FOREIGN KEY (`medicine_id`)
        REFERENCES `medicines` (`medicine_id`) ON DELETE CASCADE,
    CONSTRAINT `chk_cart_qty` CHECK (`quantity` > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8. ORDERS TABLE
CREATE TABLE IF NOT EXISTS `orders` (
    `order_id` VARCHAR(50) PRIMARY KEY,
    `customer_id` INT NOT NULL,
    `pharmacy_id` INT NOT NULL,
    `order_status` ENUM('Pending', 'Confirmed', 'Shipped', 'Delivered', 'Cancelled') NOT NULL DEFAULT 'Pending',
    `total_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    `customer_name` VARCHAR(100) NOT NULL,
    `customer_phone` VARCHAR(20) NOT NULL,
    `delivery_address` TEXT NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_orders_customer` (`customer_id`),
    INDEX `idx_orders_status` (`order_status`),
    CONSTRAINT `fk_orders_customer` FOREIGN KEY (`customer_id`)
        REFERENCES `users` (`user_id`) ON DELETE RESTRICT,
    CONSTRAINT `fk_orders_pharmacy` FOREIGN KEY (`pharmacy_id`)
        REFERENCES `pharmacies` (`pharmacy_id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 9. ORDER ITEMS TABLE
CREATE TABLE IF NOT EXISTS `order_items` (
    `order_item_id` INT AUTO_INCREMENT PRIMARY KEY,
    `order_id` VARCHAR(50) NOT NULL,
    `medicine_id` INT NOT NULL,
    `quantity` INT NOT NULL,
    `price_at_order` DECIMAL(10,2) NOT NULL,
    CONSTRAINT `fk_order_items_order` FOREIGN KEY (`order_id`)
        REFERENCES `orders` (`order_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_order_items_medicine` FOREIGN KEY (`medicine_id`)
        REFERENCES `medicines` (`medicine_id`) ON DELETE RESTRICT,
    CONSTRAINT `chk_order_item_qty` CHECK (`quantity` > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 10. PAYMENTS TABLE
CREATE TABLE IF NOT EXISTS `payments` (
    `payment_id` INT AUTO_INCREMENT PRIMARY KEY,
    `payment_reference` VARCHAR(100) NOT NULL UNIQUE,
    `order_id` VARCHAR(50) NOT NULL,
    `amount` DECIMAL(10,2) NOT NULL,
    `payment_method` VARCHAR(50) NOT NULL DEFAULT 'Cash on Delivery',
    `payment_status` ENUM('Pending', 'Completed', 'Failed', 'Refunded') NOT NULL DEFAULT 'Completed',
    `payment_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_payments_order` FOREIGN KEY (`order_id`)
        REFERENCES `orders` (`order_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ====================================================================
-- SEED DATA
-- ====================================================================

-- Seed Users
INSERT INTO `users` (`user_id`, `full_name`, `email`, `password_hash`, `phone`, `address`, `role`)
VALUES
(1, 'MediCart Admin', 'admin@medicart.com', '55208b9bc572f9599564ddda6b11cbd7:d75cebbf1a3b54c62540176063e3c8ae9c9d2e9ba36b1f8f7212f4e3d79d47ea', '9876543210', 'MediCart Headquarters, Tech Park', 'ADMIN'),
(2, 'Prashant Customer', 'customer@medicart.com', '43e8a0a6eb224e383b290db1c468c3d9:269bf15471dc4c3f267620092a4dd234a044286293d8a1dc55b1d692b86be3cf', '9123456789', '221B Baker Street, City', 'CUSTOMER')
ON DUPLICATE KEY UPDATE `email` = VALUES(`email`);

-- Seed Medicines (Matching UI categories & image files)
INSERT INTO `medicines` (`medicine_id`, `name`, `description`, `category`, `dosage_form`, `price`, `prescription_required`, `image_file`)
VALUES
(1, 'Amoxicillin', 'Antibiotic medicine. Use only under appropriate medical guidance.', 'Capsules', '500mg Capsule', 80.00, TRUE, 'Amoxicillin.png'),
(2, 'Dermadol', 'Topical medicine. Check the exact formulation, ingredients and directions on the product label.', 'Ointments', '20g Ointment', 120.00, FALSE, 'Dermadol.png'),
(3, 'Syrup', 'Liquid medicine. Confirm the product name, strength and directions before use.', 'Syrups', '100ml Syrup', 90.00, FALSE, 'Syrup.png'),
(4, 'Paracetamol', 'Fever reducer and mild pain reliever for symptomatic relief.', 'Tablets', '500mg Tablet', 30.00, FALSE, NULL),
(5, 'Vitamin C', 'Dietary supplement supporting immune health.', 'Tablets', '500mg Chewable', 50.00, FALSE, NULL),
(6, 'Eye Drops', 'Sterile soothing ophthalmic drops for eye dryness and irritation.', 'Drops', '10ml Solution', 75.00, FALSE, NULL)
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- Seed Registered Pharmacies
INSERT INTO `pharmacies` (`pharmacy_id`, `pharmacy_name`, `address`, `contact_number`, `email`, `registration_status`)
VALUES
(1, 'CityCare Pharmacy', 'Shop 12, Metro Mall, Central City', '9876123450', 'citycare@medicart.com', 'ACTIVE'),
(2, 'HealthPlus Pharmacy', 'Plot 45, Green Avenue, North Sector', '9876234561', 'healthplus@medicart.com', 'ACTIVE'),
(3, 'Wellness Pharmacy', 'Opposite General Hospital, South Gate', '9876345672', 'wellness@medicart.com', 'ACTIVE')
ON DUPLICATE KEY UPDATE `pharmacy_name` = VALUES(`pharmacy_name`);

-- Seed Pharmacy Inventory
-- Scenario:
-- Pharmacy 1 (CityCare): Has Amoxicillin (20) & Dermadol (15), but Syrup is 0 (Out of stock).
-- Pharmacy 2 (HealthPlus): 100% stock of all items in large quantities (50 each).
-- Pharmacy 3 (Wellness): Has Dermadol (40) & Syrup (40), but 0 Amoxicillin.
INSERT INTO `pharmacy_inventory` (`pharmacy_id`, `medicine_id`, `available_quantity`, `price`)
VALUES
-- CityCare Pharmacy (Pharmacy 1)
(1, 1, 20, 80.00),
(1, 2, 15, 120.00),
(1, 3, 0, 90.00),
(1, 4, 30, 30.00),
(1, 5, 25, 50.00),
(1, 6, 10, 75.00),

-- HealthPlus Pharmacy (Pharmacy 2) - Full stock provider
(2, 1, 50, 80.00),
(2, 2, 50, 120.00),
(2, 3, 50, 90.00),
(2, 4, 50, 30.00),
(2, 5, 50, 50.00),
(2, 6, 50, 75.00),

-- Wellness Pharmacy (Pharmacy 3)
(3, 1, 0, 80.00),
(3, 2, 40, 120.00),
(3, 3, 40, 90.00),
(3, 4, 60, 30.00),
(3, 5, 40, 50.00),
(3, 6, 20, 75.00)
ON DUPLICATE KEY UPDATE
`available_quantity` = VALUES(`available_quantity`),
`price` = VALUES(`price`);
