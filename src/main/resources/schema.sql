/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  tedi
 * Created: Dec 21, 2025
 */
/* 1. Create the Dependency Tables first */
CREATE TABLE IF NOT EXISTS roles (
    id          INTEGER AUTO_INCREMENT PRIMARY KEY,
    role        VARCHAR(50) NOT NULL UNIQUE,
    is_staff    BOOLEAN DEFAULT FALSE
);
CREATE TABLE IF NOT EXISTS shipment_statuses (
    id          INTEGER AUTO_INCREMENT PRIMARY KEY,
    status_name VARCHAR(50) NOT NULL UNIQUE,
    is_initial  BOOLEAN DEFAULT FALSE,
    is_final    BOOLEAN DEFAULT FALSE
);

/* 5. DELIVERY TYPES TABLE */
/* Stores: TO_OFFICE, TO_ADDRESS */
CREATE TABLE IF NOT EXISTS delivery_types (
    id          INTEGER AUTO_INCREMENT PRIMARY KEY,
    type_name   VARCHAR(50) NOT NULL UNIQUE,
    requires_office BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS system_parameters (
    param_key VARCHAR(50) PRIMARY KEY,
    param_value VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS offices (
    id          INTEGER AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    city        VARCHAR(100) NOT NULL,
    address     VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS users (
    id                  INTEGER AUTO_INCREMENT PRIMARY KEY,
    username            VARCHAR(50) NOT NULL UNIQUE,
    email               VARCHAR(100) NOT NULL UNIQUE,
    password            VARCHAR(255) NOT NULL,
    first_name          VARCHAR(255) NOT NULL,
    last_name           VARCHAR(255) NOT NULL,
    enabled             BOOLEAN DEFAULT TRUE,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    role_id             INTEGER NOT NULL,
    office_id           INTEGER, /* Nullable: Only Office Employees have an office */
    
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles(id),
    CONSTRAINT fk_users_office FOREIGN KEY (office_id) REFERENCES offices(id)
);

CREATE TABLE IF NOT EXISTS shipments (
    id              INTEGER AUTO_INCREMENT PRIMARY KEY,
    tracking_number VARCHAR(100) NOT NULL UNIQUE,
    weight          DOUBLE NOT NULL,
    price           DECIMAL(10, 2) NOT NULL, /* Using DECIMAL is better for money */
    date_registered DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_delivered  DATETIME NULL,
    
    /* Receiver Details (if not a registered user) */
    receiver_name   VARCHAR(100),
    receiver_phone  VARCHAR(20),
    
    /* Delivery Logic */
    delivery_address VARCHAR(255), /* Nullable: Filled only if TO_ADDRESS */
    
    /* Foreign Keys */
    sender_id           INTEGER NOT NULL,
    receiver_id         INTEGER,         /* Nullable: Receiver might not be a registered user */
    office_id           INTEGER,           /* Nullable: Filled only if TO_OFFICE */
    status_id           INTEGER NOT NULL,
    delivery_type_id    INTEGER NOT NULL,
    employee_id         INTEGER, 
    
    /* Constraints */
    CONSTRAINT fk_shipment_sender FOREIGN KEY (sender_id) REFERENCES users(id),
    CONSTRAINT fk_shipment_receiver FOREIGN KEY (receiver_id) REFERENCES users(id),
    CONSTRAINT fk_shipment_office FOREIGN KEY (office_id) REFERENCES offices(id),
    CONSTRAINT fk_shipment_status FOREIGN KEY (status_id) REFERENCES shipment_statuses(id),
    CONSTRAINT fk_shipment_type FOREIGN KEY (delivery_type_id) REFERENCES delivery_types(id),
    CONSTRAINT fk_employee FOREIGN KEY (employee_id) REFERENCES users(id)
);



-- INSERT INTO roles (role, is_staff) VALUES ('client', false);
-- INSERT INTO roles (role, is_staff) VALUES ('courier', true);
-- INSERT INTO roles (role, is_staff) VALUES ('office employee', true);
-- 
-- -- Set requires_office = true for Office delivery
-- INSERT INTO delivery_types (type_name, requires_office) VALUES ('TO_OFFICE', true);
-- INSERT INTO delivery_types (type_name, requires_office) VALUES ('TO_ADDRESS', false);
-- 
-- -- Set is_initial = true for Registered
-- INSERT INTO shipment_statuses (status_name, is_initial) VALUES ('REGISTERED', true);
-- 
-- -- Add Prices
-- INSERT INTO system_parameters (param_key, param_value) VALUES ('BASE_PRICE', '5.00');
-- INSERT INTO system_parameters (param_key, param_value) VALUES ('WEIGHT_FACTOR', '2.00');
-- INSERT INTO system_parameters (param_key, param_value) VALUES ('ADDRESS_SURCHARGE', '10.00');

