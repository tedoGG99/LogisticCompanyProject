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
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    authority VARCHAR(50) NOT NULL UNIQUE
);
CREATE TABLE shipment_statuses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    status_name VARCHAR(50) NOT NULL UNIQUE
);

/* 5. DELIVERY TYPES TABLE */
/* Stores: TO_OFFICE, TO_ADDRESS */
CREATE TABLE delivery_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS offices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS users (
    id                  INTEGER AUTO_INCREMENT PRIMARY KEY,
    username            VARCHAR(50) NOT NULL UNIQUE,
    email               VARCHAR(100) NOT NULL UNIQUE,
    password            VARCHAR(255) NOT NULL,
    first_name          VARCHAR(255) NOT NULL,
    last_name           VARCHAR(255) NOT NULL,
    enabled             BOOLEAN DEFAULT TRUE,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    role_id             BIGINT NOT NULL,
    office_id           BIGINT, /* Nullable: Only Office Employees have an office */
    
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles(id),
    CONSTRAINT fk_users_office FOREIGN KEY (office_id) REFERENCES offices(id)
);

CREATE TABLE shipments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tracking_number VARCHAR(100) NOT NULL UNIQUE,
    weight DOUBLE NOT NULL,
    price DECIMAL(10, 2) NOT NULL, /* Using DECIMAL is better for money */
    date_registered DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_delivered DATETIME NULL,
    
    /* Receiver Details (if not a registered user) */
    receiver_name VARCHAR(100),
    receiver_phone VARCHAR(20),
    
    /* Delivery Logic */
    delivery_address VARCHAR(255), /* Nullable: Filled only if TO_ADDRESS */
    
    /* Foreign Keys */
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT,         /* Nullable: Receiver might not be a registered user */
    office_id BIGINT,           /* Nullable: Filled only if TO_OFFICE */
    status_id BIGINT NOT NULL,
    delivery_type_id BIGINT NOT NULL,
    
    /* Constraints */
    CONSTRAINT fk_shipment_sender FOREIGN KEY (sender_id) REFERENCES users(id),
    CONSTRAINT fk_shipment_receiver FOREIGN KEY (receiver_id) REFERENCES users(id),
    CONSTRAINT fk_shipment_office FOREIGN KEY (office_id) REFERENCES offices(id),
    CONSTRAINT fk_shipment_status FOREIGN KEY (status_id) REFERENCES shipment_statuses(id),
    CONSTRAINT fk_shipment_type FOREIGN KEY (delivery_type_id) REFERENCES delivery_types(id)
);
