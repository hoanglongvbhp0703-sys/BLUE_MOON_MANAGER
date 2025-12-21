-- Schema for Fee Collection Management (Quản lý thu phí)
-- Thêm vào schema hiện có

-- Table: fee_collections - Quản lý thu phí
CREATE TABLE IF NOT EXISTS fee_collections (
    id SERIAL PRIMARY KEY,
    household_id INT NOT NULL,
    month INT NOT NULL CHECK (month >= 1 AND month <= 12),
    year INT NOT NULL,
    amount DECIMAL(15, 2) NOT NULL DEFAULT 0,
    status VARCHAR(20) DEFAULT 'unpaid', -- unpaid, paid
    payment_date DATE,
    payment_method VARCHAR(50), -- cash, bank_transfer, credit_card
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (household_id) REFERENCES households(id) ON DELETE CASCADE,
    UNIQUE (household_id, month, year)
);

CREATE INDEX IF NOT EXISTS idx_fee_collections_household_id ON fee_collections(household_id);
CREATE INDEX IF NOT EXISTS idx_fee_collections_month_year ON fee_collections(year, month);
CREATE INDEX IF NOT EXISTS idx_fee_collections_status ON fee_collections(status);

CREATE TRIGGER update_fee_collections_updated_at BEFORE UPDATE ON fee_collections
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Table: fee_types - Loại phí (phí quản lý, phí dịch vụ, v.v.)
CREATE TABLE IF NOT EXISTS fee_types (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    default_amount DECIMAL(15, 2) DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_fee_types_name ON fee_types(name);
CREATE INDEX IF NOT EXISTS idx_fee_types_active ON fee_types(is_active);

CREATE TRIGGER update_fee_types_updated_at BEFORE UPDATE ON fee_types
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Table: fee_collection_details - Chi tiết các loại phí trong một lần thu
CREATE TABLE IF NOT EXISTS fee_collection_details (
    id SERIAL PRIMARY KEY,
    fee_collection_id INT NOT NULL,
    fee_type_id INT NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fee_collection_id) REFERENCES fee_collections(id) ON DELETE CASCADE,
    FOREIGN KEY (fee_type_id) REFERENCES fee_types(id) ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS idx_fee_collection_details_fee_collection_id ON fee_collection_details(fee_collection_id);
CREATE INDEX IF NOT EXISTS idx_fee_collection_details_fee_type_id ON fee_collection_details(fee_type_id);

