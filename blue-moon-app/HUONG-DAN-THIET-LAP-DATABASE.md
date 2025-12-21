# Hướng dẫn thiết lập Database

## Cách 1: Sử dụng MySQL Workbench (Khuyến nghị)

### Bước 1: Mở MySQL Workbench
- Mở MySQL Workbench trên máy tính của bạn
- Kết nối với MySQL server (localhost, user: root, password: mật khẩu của bạn hoặc để trống)

### Bước 2: Tạo Database và các bảng
1. Mở file `src/main/resources/sql/schema.sql` trong MySQL Workbench
2. Chọn toàn bộ nội dung (Ctrl+A)
3. Chạy script (Ctrl+Shift+Enter hoặc click nút Execute)
4. Đợi cho đến khi thấy thông báo "Query OK"

### Bước 3: Thêm dữ liệu mẫu (Tùy chọn)
1. Mở file `src/main/resources/sql/seed.sql` trong MySQL Workbench
2. Chọn toàn bộ nội dung (Ctrl+A)
3. Chạy script (Ctrl+Shift+Enter hoặc click nút Execute)
4. Đợi cho đến khi thấy thông báo "Query OK"

### Bước 4: Kiểm tra
Chạy query sau để kiểm tra database đã được tạo:
```sql
USE blue_moon;
SHOW TABLES;
```

Bạn sẽ thấy các bảng: users, function_groups, functions, groups, user_roles, group_functions, menus, password_reset_tokens, sessions

## Cách 2: Sử dụng Command Line (Nếu MySQL đã có trong PATH)

Mở PowerShell hoặc Command Prompt và chạy:

```powershell
cd blue-moon-app
mysql -u root -p < src/main/resources/sql/schema.sql
mysql -u root -p < src/main/resources/sql/seed.sql
```

## Cách 3: Sử dụng Script PowerShell tự động

Chạy script tự động (yêu cầu MySQL command line có sẵn):

```powershell
cd blue-moon-app
.\setup-database.ps1
```

## Kiểm tra cấu hình

Sau khi thiết lập database, kiểm tra file `src/main/resources/application.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/blue_moon?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
db.username=root
db.password=  # Điền mật khẩu MySQL của bạn nếu có
```

## Tài khoản mặc định (sau khi chạy seed.sql)

- **Username:** `admin`
- **Password:** `admin123`

## Khắc phục lỗi

### Lỗi: "Failed to connect to database"
- Đảm bảo MySQL service đang chạy
- Kiểm tra username và password trong `application.properties`
- Kiểm tra port MySQL (mặc định là 3306)

### Lỗi: "Unknown database 'blue_moon'"
- Chạy lại file `schema.sql` để tạo database

### Lỗi: "Table 'users' doesn't exist"
- Chạy lại file `schema.sql` để tạo các bảng

