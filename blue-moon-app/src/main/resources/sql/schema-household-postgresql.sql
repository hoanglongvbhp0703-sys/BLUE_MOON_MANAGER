-- Schema for Household Management (Quản lý hộ dân)
-- Thêm vào schema hiện có

-- Table: apartments - Quản lý căn hộ
CREATE TABLE IF NOT EXISTS apartments (
    id SERIAL PRIMARY KEY,
    building_number VARCHAR(10) NOT NULL,
    floor_number INT NOT NULL,
    room_number VARCHAR(10) NOT NULL,
    apartment_code VARCHAR(50) UNIQUE NOT NULL,
    area DECIMAL(10, 2) NOT NULL,
    number_of_rooms INT DEFAULT 2,
    status VARCHAR(20) DEFAULT 'available',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_apartments_code ON apartments(apartment_code);
CREATE INDEX IF NOT EXISTS idx_apartments_building ON apartments(building_number, floor_number);
CREATE INDEX IF NOT EXISTS idx_apartments_status ON apartments(status);

CREATE TRIGGER update_apartments_updated_at BEFORE UPDATE ON apartments
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Table: households - Quản lý hộ dân
CREATE TABLE IF NOT EXISTS households (
    id SERIAL PRIMARY KEY,
    apartment_id INT NOT NULL,
    household_code VARCHAR(50) UNIQUE NOT NULL,
    owner_name VARCHAR(255) NOT NULL,
    owner_id_card VARCHAR(20),
    owner_phone VARCHAR(20),
    owner_email VARCHAR(255),
    number_of_members INT DEFAULT 1,
    registration_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'active',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (apartment_id) REFERENCES apartments(id) ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS idx_households_apartment_id ON households(apartment_id);
CREATE INDEX IF NOT EXISTS idx_households_code ON households(household_code);
CREATE INDEX IF NOT EXISTS idx_households_owner_name ON households(owner_name);
CREATE INDEX IF NOT EXISTS idx_households_status ON households(status);

CREATE TRIGGER update_households_updated_at BEFORE UPDATE ON households
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Table: residents - Quản lý nhân khẩu
CREATE TABLE IF NOT EXISTS residents (
    id SERIAL PRIMARY KEY,
    household_id INT NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    id_card VARCHAR(20),
    date_of_birth DATE,
    gender VARCHAR(10),
    relationship VARCHAR(50),
    phone VARCHAR(20),
    email VARCHAR(255),
    occupation VARCHAR(255),
    permanent_address TEXT,
    temporary_address TEXT,
    status VARCHAR(20) DEFAULT 'active',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (household_id) REFERENCES households(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_residents_household_id ON residents(household_id);
CREATE INDEX IF NOT EXISTS idx_residents_name ON residents(full_name);
CREATE INDEX IF NOT EXISTS idx_residents_id_card ON residents(id_card);
CREATE INDEX IF NOT EXISTS idx_residents_status ON residents(status);

CREATE TRIGGER update_residents_updated_at BEFORE UPDATE ON residents
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

