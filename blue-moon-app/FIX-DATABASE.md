# Hướng dẫn sửa lỗi Database - Thêm cột paid_amount

## Lỗi
```
Error finding fee collections by user: The column name paid_amount...
```

## Nguyên nhân
Cột `paid_amount` chưa được thêm vào bảng `fee_collections` trong database.

## Cách sửa

### Bước 1: Mở psql hoặc pgAdmin

**Cách A: Dùng psql (Command Line)**
```powershell
psql -U postgres -d blue_moon
```

**Cách B: Dùng pgAdmin**
- Mở pgAdmin
- Kết nối database `blue_moon`
- Mở Query Tool (Tools → Query Tool hoặc click phải database → Query Tool)

### Bước 2: Chạy lệnh SQL

**Cách A: Dùng psql**
```powershell
psql -U postgres -d blue_moon
```

**Cách B: Dùng pgAdmin Query Tool**

Sau đó chạy các lệnh sau:
```sql
-- Thêm cột paid_amount (nếu chưa có)
ALTER TABLE fee_collections 
ADD COLUMN IF NOT EXISTS paid_amount DECIMAL(15, 2) NOT NULL DEFAULT 0;

-- Cập nhật dữ liệu cho các bản ghi đã thanh toán
UPDATE fee_collections 
SET paid_amount = amount 
WHERE status = 'paid' AND paid_amount = 0;
```

### Bước 3: Kiểm tra

Chạy lệnh sau để kiểm tra cột đã được thêm chưa:
```sql
SELECT column_name, data_type 
FROM information_schema.columns 
WHERE table_name = 'fee_collections' 
AND column_name = 'paid_amount';
```

Nếu kết quả trả về 1 dòng, nghĩa là đã thành công.

### Bước 4: Khởi động lại ứng dụng

Sau khi chạy script, khởi động lại ứng dụng JavaFX:
```powershell
cd blue-moon-app
mvn javafx:run
```

## Lưu ý
- Nếu gặp lỗi "column already exists", có nghĩa là cột đã được thêm rồi, bạn có thể bỏ qua.
- Nếu gặp lỗi khác, vui lòng copy toàn bộ thông báo lỗi và gửi lại.

