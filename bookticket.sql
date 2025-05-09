-- Cơ sở dữ liệu: auth_db (Authentication Service)
CREATE DATABASE IF NOT EXISTS auth_db;
USE auth_db;

CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id BIGINT NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NULL,
    address VARCHAR(255) NULL,
    date_of_birth DATE NULL,
    facebook_account_id BIGINT NULL,
    google_account_id BIGINT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Cơ sở dữ liệu: bus_db (Bus Service)
CREATE DATABASE IF NOT EXISTS bus_db;
USE bus_db;

CREATE TABLE routes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    origin VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    distance DECIMAL(10,2) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE companies (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    contact_info VARCHAR(255) NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE bus_types (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255) NULL,
    seat_capacity INT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE buses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    route_id BIGINT NOT NULL,
    company_id BIGINT NOT NULL,
    bus_type_id BIGINT NOT NULL,
    departure_time DATETIME NOT NULL,
    arrival_time DATETIME NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    status ENUM('ACTIVE', 'CANCELLED', 'COMPLETED') NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (route_id) REFERENCES routes(id),
    FOREIGN KEY (company_id) REFERENCES companies(id),
    FOREIGN KEY (bus_type_id) REFERENCES bus_types(id)
);

CREATE TABLE seats (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    bus_id BIGINT NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (bus_id) REFERENCES buses(id)
);

CREATE TABLE bus_images (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    bus_id BIGINT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (bus_id) REFERENCES buses(id)
);

-- Cơ sở dữ liệu: booking_db (Booking Service)
CREATE DATABASE IF NOT EXISTS booking_db;
USE booking_db;

CREATE TABLE bookings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    bus_id BIGINT NOT NULL,
    seat_numbers VARCHAR(255) NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    total_price DECIMAL(10,2) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP
    -- Không sử dụng FOREIGN KEY cho user_id và bus_id vì chúng thuộc auth_db và bus_db
    -- Liên kết được xử lý qua REST API hoặc Kafka
);

-- Cơ sở dữ liệu: payment_db (Payment Service)
CREATE DATABASE IF NOT EXISTS payment_db;
USE payment_db;

CREATE TABLE payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    booking_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    status ENUM('PENDING', 'SUCCESS', 'FAILED') NOT NULL DEFAULT 'PENDING',
    transaction_id VARCHAR(100) NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP
    -- Không sử dụng FOREIGN KEY cho booking_id vì nó thuộc booking_db
    -- Liên kết được xử lý qua REST API hoặc Kafka
);

-- Cơ sở dữ liệu: admin_db (Admin Service)
CREATE DATABASE IF NOT EXISTS admin_db;
USE admin_db;

CREATE TABLE analytics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    metric_name VARCHAR(100) NOT NULL,
    value DECIMAL(15,2) NOT NULL,
    recorded_at DATETIME NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tạo chỉ mục để tối ưu hóa truy vấn
CREATE INDEX idx_users_email ON auth_db.users(email);
CREATE INDEX idx_users_role_id ON auth_db.users(role_id);
CREATE INDEX idx_bookings_user_id ON booking_db.bookings(user_id);
CREATE INDEX idx_bookings_bus_id ON booking_db.bookings(bus_id);
CREATE INDEX idx_payments_booking_id ON payment_db.payments(booking_id);
CREATE INDEX idx_buses_route_id ON bus_db.buses(route_id);
CREATE INDEX idx_buses_company_id ON bus_db.buses(company_id);
CREATE INDEX idx_buses_bus_type_id ON bus_db.buses(bus_type_id);
CREATE INDEX idx_seats_bus_id ON bus_db.seats(bus_id);
CREATE INDEX idx_bus_images_bus_id ON bus_db.bus_images(bus_id);