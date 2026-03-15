# 🏨 Hotel Management System (HMS)

A robust PostgreSQL-based backend architecture designed for multi-tenant hotel management. This system handles complex room configurations, dynamic pricing (Rate Plans), and high-density file management (supporting 60+ files per room setup).

---

## 🏗️ Database Architecture

This project uses a grouped relational structure to maintain high performance and data integrity.

### 1. Schema Definition (DDL)
Copy and run this script to initialize the database tables.

```sql
-- =====================================================
-- GROUP 1: IDENTITY & ACCESS
-- =====================================================
CREATE TABLE merchants (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150),
    email VARCHAR(100)
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(120),
    email VARCHAR(120),
    role VARCHAR(20)
);

-- =====================================================
-- GROUP 2: PROPERTY & INVENTORY
-- =====================================================
CREATE TABLE hotels (
    id BIGSERIAL PRIMARY KEY,
    merchant_id BIGINT,
    name VARCHAR(200),
    city VARCHAR(120),
    address TEXT
);

CREATE TABLE room_types_master (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200),
    room_size INT,
    max_adults INT
);

CREATE TABLE room_types (
    id BIGSERIAL PRIMARY KEY,
    hotel_id BIGINT,
    room_type_master_id BIGINT
);

CREATE TABLE rooms (
    id BIGSERIAL PRIMARY KEY,
    room_type_id BIGINT,
    room_number VARCHAR(20),
    status VARCHAR(20)
);

-- =====================================================
-- GROUP 3: CONFIGURATION & ASSETS (60+ Files Capacity)
-- =====================================================
CREATE TABLE room_type_images (
    id BIGSERIAL PRIMARY KEY,
    room_type_id BIGINT,
    image_url TEXT,
    is_cover BOOLEAN DEFAULT false,
    display_order INT DEFAULT 1
);

CREATE TABLE bed_types_master (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE room_type_bed_configs (
    id SERIAL PRIMARY KEY,
    room_type_id BIGINT,
    bed_type_master_id BIGINT,
    bed_count INTEGER
);

CREATE TABLE amenities_master (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE room_type_amenities (
    id BIGSERIAL PRIMARY KEY,
    room_type_id BIGINT,
    amenity_master_id BIGINT
);

CREATE TABLE files (
    id BIGSERIAL PRIMARY KEY,
    file_name TEXT,
    file_path TEXT,
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT NOW()
);

-- =====================================================
-- GROUP 4: FINANCE & TRANSACTIONS
-- =====================================================
CREATE TABLE rate_plans_master (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100),
    refundable BOOLEAN
);

CREATE TABLE rate_plans (
    id BIGSERIAL PRIMARY KEY,
    room_type_id BIGINT,
    rate_plan_master_id BIGINT,
    price NUMERIC(10,2)
);

CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    room_id BIGINT,
    rate_plan_id BIGINT,
    user_id BIGINT,
    check_in_date DATE,
    check_out_date DATE,
    total_price NUMERIC(10,2)
);

-- STEP 1: POPULATE GLOBAL MASTERS
INSERT INTO room_types_master (id, name, room_size, max_adults) VALUES 
(1,'Deluxe Queen Room',34,4), 
(2,'Standard Double Room',28,2), 
(3,'Family Suite',60,6);

INSERT INTO bed_types_master (id, name) VALUES 
(1,'Single Bed'), (2,'Double Bed'), (3,'Queen Bed'), (4,'King Bed'), (5,'Sofa Bed');

INSERT INTO amenities_master (id, name) VALUES 
(1,'Garden view'), (2,'Electric kettle'), (3,'Private bathroom'), (4,'Free WiFi');

INSERT INTO rate_plans_master (id, name, refundable) VALUES 
(1,'Non-refundable', false), 
(2,'Free cancellation', true);

-- STEP 2: SIMULATE MERCHANT & HOTEL SETUP
INSERT INTO merchants (id, name, email) VALUES 
(1,'SMEY Group','reasmeysambath@gmail.com');

INSERT INTO hotels (id, merchant_id, name, city, address) VALUES 
(1, 1, 'SMEY Hotel Central Dubai', 'Dubai', 'Central Beach');

-- STEP 3: LINKING ROOMS & RATES
INSERT INTO room_types (id, hotel_id, room_type_master_id) VALUES (1, 1, 1);
INSERT INTO rooms (id, room_type_id, room_number, status) VALUES (1, 1, '101', 'available');
INSERT INTO rate_plans (id, room_type_id, rate_plan_master_id, price) VALUES (1, 1, 1, 150.00);

-- STEP 4: SIMULATE CUSTOMER & BOOKING
INSERT INTO users (id, full_name, email, role) VALUES 
(1,'Jonh Monh Everyday','customer@example.com','customer');

INSERT INTO bookings (id, room_id, rate_plan_id, user_id, check_in_date, check_out_date, total_price) 
VALUES (1, 1, 1, 1, '2026-03-15', '2026-03-16', 150.00);

-- STEP 5: CUSTOMER SEARCH ROOM
SELECT
    rtm.name AS room_type,
    rtm.room_size,
    rtm.max_adults,

    btm.name AS bed_type,
    bc.bed_count,

    rpm.name AS rate_plan,
    rp.price,

    img.image_url AS cover_image,

    COUNT(r.id) AS available_rooms

FROM room_types rt

         JOIN room_types_master rtm
              ON rtm.id = rt.room_type_master_id

         JOIN room_type_bed_configs bc
              ON bc.room_type_id = rt.id

         JOIN bed_types_master btm
              ON btm.id = bc.bed_type_master_id

         JOIN rate_plans rp
              ON rp.room_type_id = rt.id

         JOIN rate_plans_master rpm
              ON rpm.id = rp.rate_plan_master_id

         JOIN rooms r
              ON r.room_type_id = rt.id

         LEFT JOIN room_type_images img
                   ON img.room_type_id = rt.id
                       AND img.is_cover = true

WHERE rt.hotel_id = 1

  AND NOT EXISTS (
    SELECT 1
    FROM bookings b
    WHERE b.room_id = r.id
      AND b.check_in_date < DATE '2026-03-16'
      AND b.check_out_date > DATE '2026-03-15'
)

GROUP BY
    rtm.name,
    rtm.room_size,
    rtm.max_adults,
    btm.name,
    bc.bed_count,
    rpm.name,
    rp.price,
    img.image_url;
```