-- ============================================
-- SCRIPT SỬA LỖI: Thêm cột paid_amount
-- ============================================
-- Chạy script này trong database blue_moon để sửa lỗi
-- "Error finding fee collections by user: The column name paid_amount..."

-- Bước 1: Thêm cột paid_amount (nếu chưa có)
ALTER TABLE fee_collections 
ADD COLUMN IF NOT EXISTS paid_amount DECIMAL(15, 2) NOT NULL DEFAULT 0;

-- Bước 2: Cập nhật dữ liệu cho các bản ghi đã thanh toán
UPDATE fee_collections 
SET paid_amount = amount 
WHERE status = 'paid' AND paid_amount = 0;

-- Bước 3: Kiểm tra (tùy chọn - xem kết quả)
SELECT 
    column_name, 
    data_type, 
    column_default
FROM information_schema.columns 
WHERE table_name = 'fee_collections' 
AND column_name = 'paid_amount';

-- Nếu thấy 1 dòng kết quả, nghĩa là đã thành công!
-- Sau đó khởi động lại ứng dụng.

