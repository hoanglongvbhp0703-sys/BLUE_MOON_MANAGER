-- Script tạo dữ liệu mẫu cho quản lý thu phí
-- Chạy file này sau khi đã chạy schema-fee-postgresql.sql và có dữ liệu households

-- Tạo các loại phí mặc định
INSERT INTO fee_types (name, description, default_amount, is_active) VALUES
('Phí quản lý', 'Phí quản lý chung cư hàng tháng', 500000, TRUE),
('Phí dịch vụ', 'Phí dịch vụ (thang máy, bảo vệ, vệ sinh)', 300000, TRUE),
('Phí điện', 'Phí điện công cộng', 200000, TRUE),
('Phí nước', 'Phí nước công cộng', 150000, TRUE),
('Phí gửi xe', 'Phí gửi xe (nếu có)', 100000, TRUE)
ON CONFLICT (name) DO NOTHING;

-- Tạo bản ghi thu phí cho các hộ dân (tháng hiện tại và 3 tháng trước)
WITH household_list AS (
    SELECT id FROM households WHERE status = 'active' LIMIT 1000
),
month_year_combos AS (
    SELECT 
        hl.id as household_id,
        EXTRACT(MONTH FROM (CURRENT_DATE - (offset_months || ' months')::INTERVAL))::int as month,
        EXTRACT(YEAR FROM (CURRENT_DATE - (offset_months || ' months')::INTERVAL))::int as year,
        offset_months
    FROM household_list hl
    CROSS JOIN generate_series(0, 3) as offset_months
)
INSERT INTO fee_collections (household_id, month, year, amount, status, payment_date, payment_method)
SELECT 
    myc.household_id,
    myc.month,
    myc.year,
    -- Số tiền ngẫu nhiên từ 500,000 đến 2,000,000
    (500000 + (random() * 1500000)::int)::decimal(15,2) as amount,
    -- 70% đã thu phí, 30% chưa thu
    CASE WHEN random() < 0.7 THEN 'paid' ELSE 'unpaid' END as status,
    -- Nếu đã thu thì có ngày thanh toán (trong tháng đó)
    CASE 
        WHEN random() < 0.7 THEN 
            DATE_TRUNC('month', CURRENT_DATE - (myc.offset_months || ' months')::INTERVAL) + 
            ((random() * 28)::int || ' days')::INTERVAL
        ELSE NULL
    END as payment_date,
    -- Phương thức thanh toán ngẫu nhiên
    CASE (random() * 3)::int
        WHEN 0 THEN 'cash'
        WHEN 1 THEN 'bank_transfer'
        ELSE 'credit_card'
    END as payment_method
FROM month_year_combos myc
ON CONFLICT (household_id, month, year) DO NOTHING;

-- Hiển thị kết quả
DO $$
DECLARE
    total_fees INT;
    paid_fees INT;
    unpaid_fees INT;
    total_amount NUMERIC;
BEGIN
    SELECT COUNT(*) INTO total_fees FROM fee_collections;
    SELECT COUNT(*) INTO paid_fees FROM fee_collections WHERE status = 'paid';
    SELECT COUNT(*) INTO unpaid_fees FROM fee_collections WHERE status = 'unpaid';
    SELECT COALESCE(SUM(amount), 0) INTO total_amount FROM fee_collections;
    
    RAISE NOTICE '========================================';
    RAISE NOTICE 'Kết quả tạo dữ liệu thu phí:';
    RAISE NOTICE 'Tổng số bản ghi: %', total_fees;
    RAISE NOTICE 'Đã thu phí: %', paid_fees;
    RAISE NOTICE 'Chưa thu phí: %', unpaid_fees;
    RAISE NOTICE 'Tổng số tiền: %', total_amount;
    RAISE NOTICE '========================================';
END $$;

