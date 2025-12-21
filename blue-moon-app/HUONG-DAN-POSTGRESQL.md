# Hướng dẫn sử dụng PostgreSQL

## Cấu hình PostgreSQL

### Bước 1: Cập nhật application.properties

File `src/main/resources/application.properties` đã được cấu hình sẵn cho PostgreSQL:

```properties
# Database Configuration
# For PostgreSQL (uncomment these lines):
db.url=jdbc:postgresql://localhost:5432/blue_moon
db.username=postgres
db.password=your_password_here
db.driver=org.postgresql.Driver
```

**Lưu ý:** 
- Thay `your_password_here` bằng mật khẩu PostgreSQL của bạn
- Nếu PostgreSQL của bạn chạy trên port khác, thay `5432` bằng port đó

### Bước 2: Tạo Database

#### Cách 1: Sử dụng psql (Command Line)

```bash
# Kết nối đến PostgreSQL
psql -U postgres

# Tạo database
CREATE DATABASE blue_moon WITH ENCODING 'UTF8';

# Thoát
\q
```

#### Cách 2: Sử dụng pgAdmin

1. Mở pgAdmin
2. Kết nối với PostgreSQL server
3. Right-click vào "Databases" → "Create" → "Database"
4. Đặt tên: `blue_moon`
5. Encoding: `UTF8`
6. Click "Save"

### Bước 3: Chạy Schema

#### Cách 1: Tự động (Khuyến nghị)

Ứng dụng sẽ tự động tạo các bảng khi khởi động nếu database đã tồn tại.

#### Cách 2: Thủ công

1. Mở pgAdmin hoặc psql
2. Kết nối với database `blue_moon`
3. Mở file `src/main/resources/sql/schema-postgresql.sql`
4. Chạy toàn bộ script

### Bước 4: Thêm dữ liệu mẫu (Tùy chọn)

1. Mở pgAdmin hoặc psql
2. Kết nối với database `blue_moon`
3. Mở file `src/main/resources/sql/seed-postgresql.sql`
4. Chạy toàn bộ script

## Chạy ứng dụng

```powershell
cd blue-moon-app
mvn clean javafx:run
```

Ứng dụng sẽ tự động:
- Kiểm tra kết nối database
- Tạo database nếu chưa tồn tại (cần quyền admin)
- Tạo các bảng từ schema-postgresql.sql

## Tài khoản mặc định (sau khi chạy seed-postgresql.sql)

- **Username:** `admin`
- **Password:** `admin123`

## Khắc phục lỗi

### Lỗi: "Failed to connect to database"
- Đảm bảo PostgreSQL service đang chạy
- Kiểm tra username và password trong `application.properties`
- Kiểm tra port PostgreSQL (mặc định là 5432)

### Lỗi: "Database does not exist"
- Tạo database `blue_moon` thủ công (xem Bước 2)
- Hoặc đảm bảo user có quyền tạo database

### Lỗi: "Permission denied"
- Đảm bảo user `postgres` (hoặc user bạn dùng) có quyền tạo database và bảng
- Hoặc chạy schema-postgresql.sql thủ công

## Chuyển đổi giữa MySQL và PostgreSQL

Để chuyển từ PostgreSQL sang MySQL (hoặc ngược lại):

1. Cập nhật `application.properties`:
   - Comment/uncomment các dòng phù hợp
   - Thay đổi `db.url`, `db.username`, `db.password`, `db.driver`

2. Chạy lại ứng dụng:
   ```powershell
   mvn clean javafx:run
   ```

## Lưu ý

- PostgreSQL và MySQL có một số khác biệt về SQL syntax
- Schema files đã được tối ưu cho từng database
- Ứng dụng tự động phát hiện loại database từ driver

