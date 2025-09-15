-- V1__init_table.sql

-- ==========================
-- Table: dispatch_entity
-- ==========================
CREATE TABLE dispatch_entity (
    id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    vehicle_id BIGINT,
    driver_id BIGINT,
    status VARCHAR(50),
    created_at TIMESTAMP DEFAULT now() NOT NULL,
    updated_at TIMESTAMP DEFAULT now() NOT NULL
);

-- ==========================
-- Table: dispatch_log_entity
-- ==========================
CREATE TABLE dispatch_log_entity (
    id BIGSERIAL PRIMARY KEY,
    dispatch_id BIGINT NOT NULL,
    booking_id BIGINT NOT NULL,
    vehicle_id BIGINT,
    driver_id BIGINT,
    status VARCHAR(50),
    cycle INT
);

-- ==========================
-- Index & Constraints
-- ==========================
-- Giới hạn 1 record duy nhất trong dispatch_entity cho mỗi booking
ALTER TABLE dispatch_entity
ADD CONSTRAINT uk_booking UNIQUE (booking_id);

-- Optional: unique constraint để không duplicate log cùng cycle/driver
ALTER TABLE dispatch_log_entity
ADD CONSTRAINT uk_booking_cycle_driver UNIQUE (booking_id, cycle, driver_id);
