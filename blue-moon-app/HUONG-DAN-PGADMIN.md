# Hướng dẫn kết nối PostgreSQL trong pgAdmin

## Bước 1: Mở pgAdmin và tạo Server mới

1. Mở pgAdmin
2. Right-click vào "Servers" → "Register" → "Server..."

## Bước 2: Tab "General"
- **Name:** `blue_moon` (hoặc tên bất kỳ bạn muốn)
- Có thể bỏ qua các mục khác

## Bước 3: Tab "Connection" (QUAN TRỌNG!)
Click vào tab **"Connection"** và điền:

- **Host name/address:** `localhost` (hoặc `127.0.0.1`)
- **Port:** `5432` (mặc định của PostgreSQL)
- **Maintenance database:** `postgres` (hoặc để mặc định)
- **Username:** `postgres` (hoặc username PostgreSQL của bạn)
- **Password:** Mật khẩu PostgreSQL của bạn (nếu có)

**Lưu ý:** Nếu bạn chưa đặt mật khẩu, có thể để trống hoặc nhập mật khẩu bạn đã đặt khi cài PostgreSQL.

## Bước 4: Tab "Save password"
- Tích vào "Save password" nếu muốn lưu mật khẩu (không phải nhập lại mỗi lần)

## Bước 5: Lưu
- Click nút **"Save"** ở góc dưới bên phải

## Bước 6: Tạo Database
Sau khi kết nối thành công:

1. Mở rộng server "blue_moon" (hoặc tên bạn đã đặt)
2. Right-click vào "Databases" → "Create" → "Database..."
3. **Database name:** `blue_moon`
4. **Encoding:** `UTF8`
5. Click "Save"

## Xong!
Bây giờ bạn có thể chạy ứng dụng:
```powershell
cd blue-moon-app
mvn clean javafx:run
```

