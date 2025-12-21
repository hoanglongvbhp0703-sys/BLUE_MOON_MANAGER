# Blue Moon Apartment Management System

Phần mềm quản lý nhân khẩu và thu phí chung cư Blue Moon

## Yêu cầu hệ thống

- Java 11 hoặc cao hơn
- MySQL 8.0 hoặc cao hơn
- Maven 3.6+

## Cài đặt

### 1. Cài đặt database

1. Tạo database MySQL:
```sql
CREATE DATABASE blue_moon CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Chạy script schema:
```bash
mysql -u root -p blue_moon < src/main/resources/sql/schema.sql
```

3. Chạy script seed data:
```bash
mysql -u root -p blue_moon < src/main/resources/sql/seed.sql
```

### 2. Cấu hình

Chỉnh sửa file `src/main/resources/application.properties`:

```properties
# Database Configuration
db.url=jdbc:mysql://localhost:3306/blue_moon?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
db.username=root
db.password=your_password

# Email Configuration (for password reset)
email.smtp.host=smtp.gmail.com
email.smtp.port=587
email.smtp.auth=true
email.smtp.starttls.enable=true
email.from=your_email@gmail.com
email.from.password=your_email_password
```

### 3. Build và chạy

```bash
# Build project
mvn clean compile

# Chạy ứng dụng
mvn javafx:run
```

Hoặc sử dụng IDE để chạy class `vn.bluemoon.App`

## Tài khoản mặc định

- Username: `admin`
- Password: `admin123`

## Tính năng

### Đã triển khai

1. **Đăng nhập (UC001)**
   - Đăng nhập bằng username/password
   - Đăng nhập bằng Facebook (cần cấu hình)

2. **Đăng ký (UC003)**
   - Đăng ký tài khoản mới
   - Validation đầy đủ

3. **Tìm kiếm người dùng (UC004)**
   - Tìm kiếm theo username, email, họ tên, số điện thoại
   - Hiển thị danh sách kết quả

4. **CRUD chức năng (UC005)**
   - Thêm, sửa, xóa chức năng
   - Quản lý nhóm chức năng
   - Validation đầy đủ

5. **Quản lý người dùng**
   - Vô hiệu hóa/kích hoạt tài khoản
   - Yêu cầu đổi mật khẩu

6. **Quên mật khẩu**
   - Gửi email đặt lại mật khẩu
   - Token có thời hạn 24 giờ

### Chưa triển khai

- Tạo menu (UC002) - UI đang được phát triển
- Quản lý nhân khẩu
- Quản lý thu phí

## Cấu trúc dự án

```
blue-moon-app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── vn/bluemoon/
│   │   │       ├── config/          # Cấu hình
│   │   │       ├── controller/       # Controllers
│   │   │       ├── exception/        # Exception classes
│   │   │       ├── model/           # Entity và DTO
│   │   │       ├── repository/      # Data access layer
│   │   │       ├── security/        # Security và authorization
│   │   │       ├── service/         # Business logic
│   │   │       ├── ui/              # JavaFX UI
│   │   │       ├── util/            # Utilities
│   │   │       └── validation/     # Validation
│   │   └── resources/
│   │       ├── application.properties
│   │       └── sql/
│   │           ├── schema.sql
│   │           └── seed.sql
│   └── test/
└── pom.xml
```

## Lưu ý

- Đảm bảo MySQL đang chạy trước khi khởi động ứng dụng
- Cấu hình email đúng để sử dụng chức năng quên mật khẩu
- Font mặc định: Arial 14px, màu đen, nền trắng (theo SRS)

## Phát triển

Dự án tuân thủ theo tài liệu SRS v2.0 - Nhóm 24


