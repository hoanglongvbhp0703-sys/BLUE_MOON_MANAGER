# Blue Moon Apartment Management System

Phần mềm quản lý nhân khẩu và thu phí chung cư Blue Moon

**Phiên bản:** 1.0.0  
**Ngày cập nhật:** 22/12/2025  
**Nhóm:** 24

---

## Mục lục

1. [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
2. [Cài đặt và cấu hình](#cài-đặt-và-cấu-hình)
3. [Tài khoản mặc định](#tài-khoản-mặc-định)
4. [Tính năng](#tính-năng)
5. [Phân quyền người dùng](#phân-quyền-người-dùng)
6. [Cấu trúc dự án](#cấu-trúc-dự-án)
7. [Tài liệu SRS](#tài-liệu-srs)
8. [Báo cáo kiểm tra project](#báo-cáo-kiểm-tra-project)
9. [Khắc phục lỗi](#khắc-phục-lỗi)

---

## Yêu cầu hệ thống

- **Java:** 11 hoặc cao hơn
- **Database:** PostgreSQL 12+ (khuyến nghị) hoặc MySQL 8.0+
- **Build tool:** Maven 3.6+
- **UI Framework:** JavaFX

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
3. Chạy file: `src/main/resources/sql/schema-postgresql.sql`

**Bước 3: Chạy Seed Data**

Chạy file: `src/main/resources/sql/seed-postgresql.sql`

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
mysql -u root -p blue_moon < src/main/resources/sql/schema.sql
```

**Bước 3: Chạy Seed Data**

```bash
mysql -u root -p blue_moon < src/main/resources/sql/seed.sql
```

### 2. Cấu hình application.properties

Chỉnh sửa file `src/main/resources/application.properties`:

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

## Tính năng

### Đã triển khai

1. **Đăng nhập (UC001)**
   - Đăng nhập bằng username/password
   - Đăng nhập bằng Facebook (cần cấu hình)

2. **Đăng ký (UC003)**
   - Đăng ký tài khoản mới
   - Validation đầy đủ

3. **Quên mật khẩu**
   - Gửi email đặt lại mật khẩu
   - Token có thời hạn 24 giờ

4. **Tìm kiếm người dùng (UC004)**
   - Tìm kiếm theo username, email, họ tên, số điện thoại
   - Hiển thị danh sách kết quả

5. **Quản lý người dùng**
   - Vô hiệu hóa/kích hoạt tài khoản
   - Yêu cầu đổi mật khẩu

6. **CRUD chức năng (UC005)**
   - Thêm, sửa, xóa chức năng
   - Quản lý nhóm chức năng
   - Validation đầy đủ

7. **Quản lý nhân khẩu**
   - Xem danh sách nhân khẩu
   - Tìm kiếm theo tên, mã căn hộ, mã hộ dân
   - Hiển thị thông tin chi tiết

8. **Quản lý thu phí**
   - Xem danh sách thu phí
   - Tìm kiếm theo nhiều tiêu chí
   - Đánh dấu đã thu phí
   - Thống kê thu phí

### Chưa hoàn thiện

- **Tạo menu (UC002)** - UI đang được phát triển

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
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── vn/bluemoon/
│   │   │       ├── App.java                    # Main class
│   │   │       ├── config/                     # Cấu hình
│   │   │       ├── controller/                 # Controllers (legacy)
│   │   │       ├── exception/                   # Exception classes
│   │   │       ├── model/
│   │   │       │   ├── dto/                     # Data Transfer Objects
│   │   │       │   └── entity/                  # Entity classes
│   │   │       ├── repository/                  # Data access layer
│   │   │       ├── security/                   # Security và authorization
│   │   │       ├── service/                     # Business logic
│   │   │       ├── ui/                          # JavaFX UI
│   │   │       ├── util/                        # Utilities
│   │   │       └── validation/                  # Validation
│   │   └── resources/
│   │       ├── application.properties           # Cấu hình
│   │       ├── sql/
│   │       │   ├── schema-postgresql.sql       # Schema PostgreSQL
│   │       │   ├── schema.sql                  # Schema MySQL
│   │       │   ├── seed-postgresql.sql         # Seed data PostgreSQL
│   │       │   └── seed.sql                    # Seed data MySQL
│   │       └── ui/                              # FXML files
│   └── test/
└── pom.xml
```

---

## Tài liệu SRS

### Tổng quan

Phần mềm hỗ trợ Ban quản trị chung cư Blue Moon quản lý:
- Thông tin hộ gia đình và nhân khẩu
- Thu phí dịch vụ, phí quản lý
- Người dùng hệ thống và phân quyền
- Chức năng hệ thống và menu động

### Các Use Case chính

1. **UC001 - Đăng nhập**: Đăng nhập vào hệ thống bằng username/password
2. **UC002 - Tạo menu**: Tạo menu động cho hệ thống (đang phát triển)
3. **UC003 - Đăng ký**: Đăng ký tài khoản mới
4. **UC004 - Tìm kiếm người dùng**: Tìm kiếm người dùng theo nhiều tiêu chí
5. **UC005 - CRUD chức năng**: Quản lý các chức năng hệ thống
6. **UC006 - Quản lý người dùng**: Vô hiệu hóa/kích hoạt tài khoản
7. **UC007 - Quản lý nhân khẩu**: Xem và tìm kiếm thông tin nhân khẩu
8. **UC008 - Quản lý thu phí**: Quản lý thu phí hàng tháng

### Cấu trúc Database

**Core Tables:**
- `users`: Quản lý người dùng
- `groups`: Quản lý nhóm người dùng
- `user_roles`: Quan hệ nhiều-nhiều giữa users và groups
- `function_groups`: Nhóm chức năng
- `functions`: Chức năng hệ thống
- `group_functions`: Quan hệ nhiều-nhiều giữa groups và functions
- `menus`: Menu động
- `sessions`: Session người dùng
- `password_reset_tokens`: Token đặt lại mật khẩu

**Household Tables:**
- `apartments`: Căn hộ
- `households`: Hộ dân
- `residents`: Nhân khẩu

**Fee Collection Tables:**
- `fee_collections`: Thu phí
- `fee_types`: Loại phí
- `fee_collection_details`: Chi tiết các loại phí

### Yêu cầu kỹ thuật

- **Công nghệ:** Java 11+, JavaFX
- **Database:** PostgreSQL 12+ (hoặc MySQL 8.0+)
- **Build tool:** Maven 3.6+
- **Kiến trúc:** 3-layer (Presentation, Business Logic, Data Access)

### Yêu cầu giao diện

- **Font:** Arial 14px, màu đen
- **Nền:** Trắng
- **Căn lề:** Số căn phải, chữ căn trái
- **Thông báo lỗi:** Rõ ràng, cụ thể

### Yêu cầu bảo mật

- Mật khẩu được hash bằng BCrypt
- Session có thời hạn (8 giờ)
- Kiểm tra quyền truy cập cho mọi chức năng
- Thông tin nhạy cảm không được hiển thị trong log

---

## Báo cáo kiểm tra project

### Trạng thái tổng thể

✅ **Project đang ở trạng thái tốt và sẵn sàng sử dụng**

### Kiểm tra Code

✅ **Không có lỗi compile nghiêm trọng**

⚠️ **Warnings (không ảnh hưởng):**
- Unused imports trong một số Repository files
- JRE version mismatch (compile với Java 11, chạy với Java 21) - không ảnh hưởng

✅ **Kiến trúc 3-layer:** Tốt
✅ **Separation of Concerns:** Tốt
✅ **Naming Convention:** Tuân thủ Java conventions
✅ **Package Structure:** Rõ ràng, dễ hiểu

### Kiểm tra Chức năng

| STT | Chức năng | Trạng thái |
|-----|-----------|------------|
| 1 | Đăng nhập (UC001) | ✅ Hoàn thành |
| 2 | Đăng ký (UC003) | ✅ Hoàn thành |
| 3 | Quên mật khẩu | ✅ Hoàn thành |
| 4 | Tìm kiếm người dùng (UC004) | ✅ Hoàn thành |
| 5 | Quản lý người dùng | ✅ Hoàn thành |
| 6 | CRUD chức năng (UC005) | ✅ Hoàn thành |
| 7 | Quản lý nhân khẩu | ✅ Hoàn thành |
| 8 | Quản lý thu phí | ✅ Hoàn thành |
| 9 | Tạo menu (UC002) | ⚠️ Đang phát triển |

### Kiểm tra Database

✅ **Schema:** Đầy đủ, tất cả bảng đã được định nghĩa
✅ **Seed Data:** Đầy đủ, phân quyền đúng theo SRS

### Đánh giá tổng thể

**Điểm mạnh:**
- ✅ Kiến trúc code rõ ràng, dễ bảo trì
- ✅ Phân quyền đúng với SRS
- ✅ Database schema đầy đủ
- ✅ UI/UX nhất quán
- ✅ Error handling tốt
- ✅ Validation đầy đủ

**Điểm cần cải thiện:**
- ⚠️ Một số unused imports
- ⚠️ Chức năng "Tạo menu" chưa hoàn thiện

**Kết luận:** ✅ **Project sẵn sàng để sử dụng và demo**

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

### Lỗi: "Permission denied"

- Đảm bảo user có quyền tạo database và bảng
- Hoặc chạy schema thủ công

### Lỗi: "Password authentication failed"

- Kiểm tra lại password trong `application.properties`
- Đảm bảo password đúng với database

### Lỗi: "Cannot login with admin account"

- Kiểm tra user admin đã được tạo chưa (chạy seed data)
- Mật khẩu mặc định: `admin123`
- **Lưu ý:** Chỉ nhập mật khẩu gốc, không nhập hash!

### Lỗi encoding trong psql

Khi chạy SQL trong psql, set encoding:
```sql
SET client_encoding TO 'UTF8';
```

---

## Lưu ý

- Đảm bảo database service đang chạy trước khi khởi động ứng dụng
- Cấu hình email đúng để sử dụng chức năng quên mật khẩu
- Font mặc định: Arial 14px, màu đen, nền trắng (theo SRS)
- Ứng dụng tự động tạo database và schema nếu chưa tồn tại (cần quyền admin)

---

## Phát triển

Dự án tuân thủ theo tài liệu SRS v2.1 - Nhóm 24

**Tài liệu chi tiết:** Xem file `docs/SRS-UPDATED.md` (nếu có)

---

**Kết thúc tài liệu**
