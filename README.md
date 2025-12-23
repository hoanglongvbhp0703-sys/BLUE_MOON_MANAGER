# Blue Moon Apartment Management System

Phần mềm quản lý nhân khẩu và thu phí chung cư Blue Moon

**Phiên bản:** 1.0.0  
**Ngày cập nhật:** 23/12/2025  
**Nhóm:** 24

---

## Mục lục

1. [Giới thiệu](#giới-thiệu)
2. [Tính năng chính](#tính-năng-chính)
3. [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
4. [Cài đặt và cấu hình](#cài-đặt-và-cấu-hình)
5. [Tài khoản mặc định](#tài-khoản-mặc-định)
6. [Hướng dẫn sử dụng](#hướng-dẫn-sử-dụng)
7. [Phân quyền người dùng](#phân-quyền-người-dùng)
8. [Cấu trúc dự án](#cấu-trúc-dự-án)
9. [Cấu trúc Database](#cấu-trúc-database)
10. [Khắc phục lỗi](#khắc-phục-lỗi)
11. [Phát triển](#phát-triển)

---

## Giới thiệu

Blue Moon Apartment Management System là phần mềm quản lý toàn diện cho Ban quản trị chung cư, hỗ trợ:

- **Quản lý nhân khẩu**: Quản lý thông tin hộ gia đình và nhân khẩu trong chung cư
- **Quản lý thu phí**: Theo dõi và quản lý thu phí dịch vụ, phí quản lý hàng tháng
- **Quản lý người dùng**: Quản lý tài khoản và phân quyền truy cập hệ thống
- **Quản lý chức năng**: Cấu hình các chức năng hệ thống và menu động
- **Đóng tiền trực tuyến**: Người dùng có thể đóng phí trực tiếp qua hệ thống

---

## Tính năng chính

### ✅ Đã triển khai

#### 1. Xác thực và Bảo mật
- **Đăng nhập (UC001)**: Đăng nhập bằng username/password
- **Đăng ký (UC003)**: Đăng ký tài khoản mới với validation đầy đủ
- **Quên mật khẩu**: Gửi email đặt lại mật khẩu với token có thời hạn 24 giờ
- **Đổi mật khẩu bắt buộc**: Admin có thể yêu cầu user đổi mật khẩu (ngay lập tức, tại ngày cụ thể, hoặc định kỳ)
- **Session Management**: Quản lý phiên đăng nhập với thời hạn 8 giờ
- **Password Hashing**: Mật khẩu được hash bằng BCrypt

#### 2. Quản lý Người dùng
- **Tìm kiếm người dùng (UC004)**: Tìm kiếm theo username, email, họ tên, số điện thoại
- **Quản lý người dùng (UC006)**: 
  - Vô hiệu hóa/kích hoạt tài khoản
  - Yêu cầu đổi mật khẩu
  - Thiết lập định kỳ đổi mật khẩu
- **Phân quyền**: Hệ thống phân quyền linh hoạt theo nhóm người dùng

#### 3. Quản lý Chức năng
- **CRUD chức năng (UC005)**: Thêm, sửa, xóa chức năng hệ thống
- **Quản lý nhóm chức năng**: Tổ chức chức năng theo nhóm
- **Validation**: Kiểm tra đầy đủ khi tạo/sửa chức năng

#### 4. Quản lý Nhân khẩu
- **Xem danh sách nhân khẩu**: Hiển thị tất cả nhân khẩu (chỉ chủ hộ)
- **Tìm kiếm**: Tìm kiếm theo tên, mã căn hộ, mã hộ dân
- **Thông tin chi tiết**: Hiển thị đầy đủ thông tin nhân khẩu
- **Đăng ký thông tin cá nhân**: Người dùng có thể đăng ký/cập nhật thông tin cá nhân

#### 5. Quản lý Thu phí
- **Xem danh sách thu phí**: Hiển thị tất cả khoản thu phí
- **Tìm kiếm**: Tìm kiếm theo nhiều tiêu chí (tên chủ hộ, mã căn hộ, mã hộ dân, tháng/năm, trạng thái)
- **Thống kê**: Thống kê tổng số khoản phí, đã thu, chưa thu
- **Trạng thái thanh toán**: Hỗ trợ 4 trạng thái: chưa đóng, đã đóng, đóng một phần, đóng thừa
- **Đồng bộ dữ liệu**: Tự động đồng bộ giữa nhân khẩu và thu phí

#### 6. Đóng tiền (Cho người dùng)
- **Xem khoản phí cần đóng**: Hiển thị danh sách các khoản phí chưa đóng
- **Đóng tiền**: Người dùng có thể đóng phí trực tiếp qua hệ thống
- **Hỗ trợ nhiều phương thức thanh toán**: Tiền mặt, chuyển khoản, thẻ tín dụng
- **Xử lý thanh toán**: Hỗ trợ đóng đủ, đóng một phần, đóng thừa

#### 7. Thông tin Cá nhân
- **Đăng ký thông tin**: Người dùng có thể đăng ký thông tin cá nhân lần đầu
- **Cập nhật thông tin**: Cập nhật thông tin cá nhân đã có
- **Tự động tạo fee collection**: Khi đăng ký thông tin, hệ thống tự động tạo fee collection cho tháng hiện tại

### ⚠️ Đang phát triển

- **Tạo menu (UC002)**: Tạo menu động cho hệ thống

---

## Yêu cầu hệ thống

- **Java:** JDK 11 hoặc cao hơn
- **Database:** PostgreSQL 12+ (khuyến nghị) hoặc MySQL 8.0+
- **Build tool:** Maven 3.6+
- **UI Framework:** JavaFX 17.0.2
- **Hệ điều hành:** Windows, Linux, macOS

---

## Cài đặt và cấu hình

### 1. Cài đặt Database

#### PostgreSQL (Khuyến nghị)

**Bước 1: Tạo Database**

Sử dụng pgAdmin hoặc psql:

```sql
CREATE DATABASE blue_moon WITH ENCODING 'UTF8';
```

Hoặc qua psql:
```bash
psql -U postgres -p 5433
CREATE DATABASE blue_moon WITH ENCODING 'UTF8';
\q
```

**Bước 2: Chạy Schema**

1. Mở pgAdmin hoặc psql
2. Kết nối với database `blue_moon`
3. Chạy file: `blue-moon-app/src/main/resources/sql/schema-postgresql.sql`

**Bước 3: Chạy Seed Data**

Chạy file: `blue-moon-app/src/main/resources/sql/seed-postgresql.sql`

**Lưu ý:** File seed đã bao gồm:
- Dữ liệu mặc định (chức năng, nhóm, admin user)
- Dữ liệu hộ dân (đã comment, uncomment khi cần)
- Dữ liệu thu phí (đã comment, uncomment khi cần)

#### MySQL

**Bước 1: Tạo Database**

```sql
CREATE DATABASE blue_moon CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**Bước 2: Chạy Schema**

```bash
mysql -u root -p blue_moon < blue-moon-app/src/main/resources/sql/schema.sql
```

**Bước 3: Chạy Seed Data**

```bash
mysql -u root -p blue_moon < blue-moon-app/src/main/resources/sql/seed.sql
```

### 2. Cấu hình application.properties

Chỉnh sửa file `blue-moon-app/src/main/resources/application.properties`:

```properties
# Database Configuration
# Cho PostgreSQL (khuyến nghị):
db.url=jdbc:postgresql://localhost:5433/blue_moon
db.username=postgres
db.password=your_password
db.driver=org.postgresql.Driver

# Cho MySQL:
#db.url=jdbc:mysql://localhost:3306/blue_moon?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
#db.username=root
#db.password=your_password
#db.driver=com.mysql.cj.jdbc.Driver

# Email Configuration (for password reset)
email.smtp.host=smtp.gmail.com
email.smtp.port=587
email.smtp.auth=true
email.smtp.starttls.enable=true
email.from=your_email@gmail.com
email.from.password=your_email_password

# Security Configuration
password.reset.token.expiry.hours=24
session.expiry.hours=8
```

### 3. Build và chạy

```bash
# Build project
cd blue-moon-app
mvn clean compile

# Chạy ứng dụng
mvn javafx:run
```

Hoặc sử dụng IDE để chạy class `vn.bluemoon.App`

---

## Tài khoản mặc định

Sau khi chạy seed data:

- **Username:** `admin`
- **Password:** `admin123`

**Lưu ý:** Khi đăng nhập, bạn chỉ cần nhập mật khẩu gốc `admin123`, không cần nhập hash!

---

## Hướng dẫn sử dụng

### Đăng nhập

1. Mở ứng dụng
2. Nhập username và password
3. Click "Đăng nhập"
4. Nếu có yêu cầu đổi mật khẩu, hệ thống sẽ hiển thị dialog yêu cầu đổi mật khẩu

### Quản lý Nhân khẩu (Admin)

1. Vào menu **Nhân khẩu** → **Quản lý nhân khẩu**
2. Sử dụng thanh tìm kiếm để tìm nhân khẩu theo:
   - Tên nhân khẩu
   - Mã căn hộ
   - Mã hộ dân
3. Click "Làm mới" để xem tất cả nhân khẩu

### Quản lý Thu phí (Admin/Kế toán)

1. Vào menu **Thu phí** → **Quản lý thu phí**
2. Sử dụng thanh tìm kiếm để tìm khoản phí theo:
   - Tên chủ hộ
   - Mã căn hộ
   - Mã hộ dân
   - Tháng/Năm
   - Trạng thái (chưa đóng, đã đóng, đóng một phần, đóng thừa)
3. Xem thống kê ở phía dưới bảng

### Đóng tiền (Người dùng)

1. Vào menu **Cá nhân** → **Đóng tiền**
2. Xem danh sách các khoản phí cần đóng
3. Chọn khoản phí và nhập số tiền đóng
4. Chọn phương thức thanh toán
5. Click "Xác nhận thanh toán"

### Thông tin Cá nhân (Người dùng)

1. Vào menu **Cá nhân** → **Thông tin cá nhân**
2. Điền thông tin cá nhân (nếu chưa đăng ký)
3. Hoặc cập nhật thông tin (nếu đã có)
4. Click "Lưu"

**Lưu ý:** 
- Khi đăng ký thông tin lần đầu, hệ thống sẽ tự động tạo fee collection cho tháng hiện tại
- Chỉ có thể đăng ký với vai trò "Chủ hộ"

---

## Phân quyền người dùng

### Các nhóm người dùng

| Nhóm | Mô tả | Quyền hạn |
|------|-------|-----------|
| **Quản trị viên** | Nhóm quản trị viên hệ thống | Toàn quyền - có thể sử dụng tất cả chức năng |
| **Tổ trưởng** | Nhóm tổ trưởng quản lý chung cư | Tìm kiếm người dùng, Quản lý người dùng, Quản lý nhân khẩu |
| **Kế toán** | Nhóm kế toán quản lý thu phí | Tìm kiếm người dùng, Quản lý thu phí |
| **Ban quản trị** | Nhóm ban quản trị chung cư | Tìm kiếm người dùng, Quản lý người dùng, Quản lý nhân khẩu |

### Bảng phân quyền

| Chức năng | Khách | Người dùng | Quản trị viên | Tổ trưởng | Kế toán | Ban quản trị |
|-----------|-------|------------|---------------|-----------|---------|--------------|
| Đăng nhập | ✅ | - | ✅ | ✅ | ✅ | ✅ |
| Đăng ký | ✅ | - | - | - | - | - |
| Quên mật khẩu | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| Tìm kiếm người dùng | - | ❌* | ✅ | ✅ | ✅ | ✅ |
| Quản lý người dùng | - | ❌ | ✅ | ✅ | ❌ | ✅ |
| CRUD chức năng | - | ❌ | ✅ | ❌ | ❌ | ❌ |
| Tạo menu | - | ❌ | ✅ | ❌ | ❌ | ❌ |
| Quản lý nhân khẩu | - | ❌ | ✅ | ✅ | ❌ | ✅ |
| Quản lý thu phí | - | ❌ | ✅ | ❌ | ✅ | ❌ |
| Thông tin cá nhân | - | ✅ | ✅ | ✅ | ✅ | ✅ |
| Đóng tiền | - | ✅ | ❌ | ❌ | ❌ | ❌ |

*Người dùng chỉ có quyền nếu được phân quyền qua nhóm (group).

### Quy tắc phân quyền

- Một người dùng có thể thuộc nhiều nhóm
- Một nhóm có thể có nhiều chức năng
- Người dùng có quyền sử dụng chức năng nếu ít nhất một trong các nhóm của họ có quyền đó
- Quản trị viên có toàn quyền, không cần kiểm tra phân quyền

---

## Cấu trúc dự án

```
blue-moon-app/
├── src/main/
│   ├── java/vn/bluemoon/
│   │   ├── App.java                    # Main class
│   │   ├── config/                     # Cấu hình
│   │   ├── exception/                  # Exception classes
│   │   ├── model/
│   │   │   ├── dto/                     # Data Transfer Objects
│   │   │   └── entity/                  # Entity classes
│   │   ├── repository/                  # Data access layer
│   │   ├── security/                   # Security và authorization
│   │   ├── service/                     # Business logic
│   │   ├── ui/                          # JavaFX UI Controllers
│   │   ├── util/                        # Utilities
│   │   └── validation/                  # Validation
│   └── resources/
│       ├── application.properties       # Cấu hình
│       ├── css/styles.css               # CSS styles
│       ├── sql/                          # SQL scripts
│       └── ui/                           # FXML files
└── pom.xml
```

---

## Cấu trúc Database

### Core Tables (Bảng cốt lõi)

- **`users`**: Quản lý người dùng
  - Hỗ trợ đổi mật khẩu bắt buộc (ngay lập tức, tại ngày cụ thể, định kỳ)
  - Lưu ngày đổi mật khẩu lần cuối
  
- **`groups`**: Quản lý nhóm người dùng
- **`user_roles`**: Quan hệ nhiều-nhiều giữa users và groups
- **`function_groups`**: Nhóm chức năng
- **`functions`**: Chức năng hệ thống
- **`group_functions`**: Quan hệ nhiều-nhiều giữa groups và functions
- **`menus`**: Menu động
- **`sessions`**: Session người dùng
- **`password_reset_tokens`**: Token đặt lại mật khẩu

### Household Tables (Bảng quản lý hộ dân)

- **`apartments`**: Căn hộ
- **`households`**: Hộ dân
- **`residents`**: Nhân khẩu
  - Liên kết với `users` qua `user_id` (nullable)
  - Chỉ hiển thị chủ hộ (relationship = 'Chủ hộ') trong quản lý

### Fee Collection Tables (Bảng quản lý thu phí)

- **`fee_collections`**: Thu phí
  - Hỗ trợ 4 trạng thái: `unpaid`, `paid`, `partial_paid`, `overpaid`
  - Lưu số tiền đã nộp (`paid_amount`)
  - Tự động đồng bộ với nhân khẩu
  
- **`fee_types`**: Loại phí

---

## Khắc phục lỗi

### Lỗi kết nối Database

**PostgreSQL:**
- Đảm bảo PostgreSQL service đang chạy
- Kiểm tra username và password trong `application.properties`
- Kiểm tra port PostgreSQL (mặc định là 5432, có thể là 5433)
- Kiểm tra database `blue_moon` đã được tạo chưa

**MySQL:**
- Đảm bảo MySQL service đang chạy
- Kiểm tra username và password trong `application.properties`
- Kiểm tra port MySQL (mặc định là 3306)

### Lỗi: "Database does not exist"

**PostgreSQL:**
```sql
CREATE DATABASE blue_moon WITH ENCODING 'UTF8';
```

**MySQL:**
```sql
CREATE DATABASE blue_moon CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Lỗi: "Table 'users' doesn't exist"

Chạy lại file schema:
- PostgreSQL: `schema-postgresql.sql`
- MySQL: `schema.sql`

### Lỗi: "The column name paid_amount..."

**Nguyên nhân:** Cột `paid_amount` chưa được thêm vào bảng `fee_collections` trong database.

**Cách sửa:**

Chạy lệnh SQL sau trong pgAdmin hoặc psql:

```sql
-- Thêm cột paid_amount (nếu chưa có)
ALTER TABLE fee_collections 
ADD COLUMN IF NOT EXISTS paid_amount DECIMAL(15, 2) NOT NULL DEFAULT 0;

-- Cập nhật dữ liệu cho các bản ghi đã thanh toán
UPDATE fee_collections 
SET paid_amount = amount 
WHERE status = 'paid' AND paid_amount = 0;
```

**Lưu ý:** File `schema-postgresql.sql` đã bao gồm migration này ở cuối file, nên chỉ cần chạy lại schema nếu gặp lỗi này.

### Lỗi: "The column name password_change_required_date..."

**Nguyên nhân:** Các cột quản lý đổi mật khẩu chưa được thêm vào bảng `users`.

**Cách sửa:**

Chạy lệnh SQL sau:

```sql
-- Add password_change_required_date column
ALTER TABLE users 
ADD COLUMN IF NOT EXISTS password_change_required_date DATE NULL;

-- Add password_change_period_days column
ALTER TABLE users 
ADD COLUMN IF NOT EXISTS password_change_period_days INTEGER NULL;

-- Add last_password_change_date column
ALTER TABLE users 
ADD COLUMN IF NOT EXISTS last_password_change_date DATE NULL;

-- Update existing users
UPDATE users 
SET last_password_change_date = DATE(created_at) 
WHERE last_password_change_date IS NULL AND created_at IS NOT NULL;
```

**Lưu ý:** File `schema-postgresql.sql` đã bao gồm migration này ở cuối file.

### Lỗi: "Cannot login with admin account"

- Kiểm tra user admin đã được tạo chưa (chạy seed data)
- Mật khẩu mặc định: `admin123`
- **Lưu ý:** Chỉ nhập mật khẩu gốc, không nhập hash!

### Lỗi: "Permission denied"

- Đảm bảo user có quyền tạo database và bảng
- Hoặc chạy schema thủ công

### Lỗi encoding trong psql

Khi chạy SQL trong psql, set encoding:
```sql
SET client_encoding TO 'UTF8';
```

---

## Phát triển

### Kiến trúc

Dự án sử dụng kiến trúc 3-layer:

1. **Presentation Layer (UI)**: JavaFX Controllers và FXML
2. **Business Logic Layer (Service)**: Xử lý logic nghiệp vụ
3. **Data Access Layer (Repository)**: Truy cập database

### Công nghệ sử dụng

- **Java 11+**: Ngôn ngữ lập trình
- **JavaFX 17.0.2**: UI Framework
- **Maven 3.6+**: Build tool
- **PostgreSQL 12+**: Database (khuyến nghị)
- **BCrypt**: Password hashing
- **JavaMail**: Gửi email

### Coding Standards

- Tuân thủ Java naming conventions
- Package structure rõ ràng
- Separation of concerns
- Error handling đầy đủ
- Validation cho tất cả input

### Tài liệu tham khảo

- SRS Document v2.1 - Nhóm 24
- File SRS: `blue-moon-app/docs/SRS.pdf`

---

## Lưu ý

- Đảm bảo database service đang chạy trước khi khởi động ứng dụng
- Cấu hình email đúng để sử dụng chức năng quên mật khẩu
- Font mặc định: Arial 14px, màu đen, nền trắng (theo SRS)
- Ứng dụng tự động tạo database và schema nếu chưa tồn tại (cần quyền admin)
- Khi đăng nhập, cửa sổ sẽ tự động hiển thị toàn màn hình
- Khi đăng xuất, cửa sổ sẽ reset về kích thước bình thường

---

**Kết thúc tài liệu**
