# Hướng dẫn tạo database nhanh cho PostgreSQL

## Bước 1: Mở pgAdmin
- Tìm "pgAdmin" trong Start Menu và mở
- Kết nối với PostgreSQL server (có thể cần nhập mật khẩu)

## Bước 2: Tạo Database
1. Trong pgAdmin, mở rộng "Servers" → "PostgreSQL 17" (hoặc 18)
2. Right-click vào "Databases" → "Create" → "Database..."
3. Trong tab "General":
   - Database: `blue_moon`
4. Trong tab "Definition":
   - Encoding: `UTF8`
5. Click "Save"

## Bước 3: Cập nhật mật khẩu (nếu có)
Mở file `src/main/resources/application.properties` và cập nhật dòng 5:
```properties
db.password=your_postgres_password_here
```

## Bước 4: Chạy ứng dụng
```powershell
cd blue-moon-app
mvn clean javafx:run
```

Ứng dụng sẽ tự động tạo các bảng!

