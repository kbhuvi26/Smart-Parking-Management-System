CREATE DATABASE IF NOT EXISTS smart_parking_system;

USE smart_parking_system;

CREATE TABLE IF NOT EXISTS slots (
    slot_id VARCHAR(255) NOT NULL,
    slot_type VARCHAR(255),
    slot_status VARCHAR(255),
    distance INT NOT NULL,
    PRIMARY KEY (slot_id)
);

CREATE TABLE IF NOT EXISTS vehicles (
    vehicle_id INT NOT NULL AUTO_INCREMENT,
    vehicle_number VARCHAR(255),
    vehicle_type VARCHAR(255),
    slot_id VARCHAR(255),
    entry_time DATETIME(6),
    exit_time DATETIME(6),
    fee DOUBLE,
    payment_method VARCHAR(255),
    PRIMARY KEY (vehicle_id)
);

CREATE TABLE IF NOT EXISTS waiting_queue (
    waiting_id INT NOT NULL AUTO_INCREMENT,
    vehicle_number VARCHAR(255),
    vehicle_type VARCHAR(255),
    join_time DATETIME(6),
    estimated_time INT NOT NULL,
    PRIMARY KEY (waiting_id)
);

USE smart_parking_system;

INSERT IGNORE INTO slots (slot_id, slot_type, slot_status, distance)
VALUES
('C1', 'CAR', 'FREE', 1),
('C2', 'CAR', 'FREE', 2),
('C3', 'CAR', 'FREE', 3),
('B1', 'BIKE', 'FREE', 1),
('B2', 'BIKE', 'FREE', 2),
('B3', 'BIKE', 'FREE', 3);