# BÁO CÁO CÀI ĐẶT VÀ TRIỂN KHAI HỆ THỐNG
## Blue Moon Apartment Management System

**Phiên bản:** 1.0.0  
**Ngày cập nhật:** 05/01/2026  
**Nhóm:** 24

---

## 1. Mô hình hệ thống

### 1.1 Mô hình kiến trúc dịch vụ hệ thống

Blue Moon Apartment Management System là một ứng dụng web được xây dựng trên nền tảng Spring Boot, sử dụng kiến trúc 3-layer:

**Presentation Layer (Lớp trình bày):**
- Spring MVC Controllers xử lý các HTTP requests
- Thymeleaf Templates để render HTML
- Static resources (CSS, JavaScript) cho giao diện người dùng

**Business Logic Layer (Lớp logic nghiệp vụ):**
- Service classes xử lý các logic nghiệp vụ
- Validation và xử lý dữ liệu
- Quản lý session và phân quyền

**Data Access Layer (Lớp truy cập dữ liệu):**
- Repository classes sử dụng JDBC để truy cập database
- Hỗ trợ PostgreSQL và MySQL
- Quản lý kết nối và transaction

**Công nghệ sử dụng:**
- Java 11+
- Spring Boot 2.7.18
- Thymeleaf (Template Engine)
- PostgreSQL 12+ (khuyến nghị) hoặc MySQL 8.0+
- Maven 3.6+ (Build tool)
- BCrypt (Password hashing)
- Spring Mail (Gửi email)

**Mô hình triển khai:**
- Ứng dụng web chạy trên embedded Tomcat server
- Port mặc định: 8080
- Truy cập qua trình duyệt web tại `http://localhost:8080`

---

## 2. Cài đặt hạ tầng

### 2.1 Cài đặt hệ điều hành máy chủ vật lý

**Yêu cầu hệ điều hành:**
- Windows 10 trở lên
- Linux (Ubuntu 18.04+, CentOS 7+)
- macOS 10.14+

**Yêu cầu phần cứng tối thiểu:**
- CPU: 2 cores trở lên
- RAM: 4GB trở lên
- Ổ cứng: 10GB dung lượng trống
- Kết nối mạng: Có (cho chức năng gửi email)

**Yêu cầu phần mềm:**
- Java Development Kit (JDK) 11 hoặc cao hơn
- Maven 3.6+ hoặc cao hơn
- PostgreSQL 12+ hoặc MySQL 8.0+
- Trình duyệt web: Chrome, Firefox, Edge (phiên bản mới nhất)

---

## 3. Cài đặt Java và Maven

### 3.1 Cài đặt Java Development Kit (JDK)

**Bước 1: Tải JDK**
- Truy cập trang web Oracle hoặc OpenJDK
- Tải JDK 11 hoặc phiên bản cao hơn phù hợp với hệ điều hành

**Bước 2: Cài đặt JDK**
- Windows: Chạy file installer và làm theo hướng dẫn
- Linux: Sử dụng package manager (apt, yum, etc.)
- macOS: Sử dụng Homebrew hoặc tải từ trang chủ

**Bước 3: Cấu hình biến môi trường**
- Thiết lập biến môi trường `JAVA_HOME` trỏ đến thư mục cài đặt JDK
- Thêm `%JAVA_HOME%\bin` (Windows) hoặc `$JAVA_HOME/bin` (Linux/macOS) vào PATH
- Kiểm tra cài đặt: `java -version` và `javac -version`

### 3.2 Cài đặt Maven

**Bước 1: Tải Maven**
- Truy cập https://maven.apache.org/download.cgi
- Tải Apache Maven 3.6+ (file zip hoặc tar.gz)

**Bước 2: Giải nén và cấu hình**
- Giải nén vào thư mục (ví dụ: `C:\Program Files\Apache\maven` hoặc `/opt/maven`)
- Thiết lập biến môi trường `MAVEN_HOME` trỏ đến thư mục Maven
- Thêm `%MAVEN_HOME%\bin` (Windows) hoặc `$MAVEN_HOME/bin` (Linux/macOS) vào PATH

**Bước 3: Kiểm tra cài đặt**
- Mở Command Prompt hoặc Terminal
- Chạy lệnh: `mvn -version`
- Kiểm tra phiên bản Maven và Java hiển thị đúng

---

## 4. Cài đặt phần mềm pgAdmin4

### 4.1 Tải và cài đặt pgAdmin4

**Bước 1: Tải pgAdmin4**
- Truy cập https://www.pgadmin.org/download/
- Tải phiên bản phù hợp với hệ điều hành

**Bước 2: Cài đặt pgAdmin4**
- Windows: Chạy file installer và làm theo hướng dẫn
- Linux: Sử dụng package manager hoặc tải từ trang chủ
- macOS: Tải file .dmg và cài đặt

**Bước 3: Khởi động và cấu hình**
- Khởi động pgAdmin4
- Thiết lập master password cho pgAdmin4 (lần đầu sử dụng)
- Thêm PostgreSQL server:
  - Host: localhost
  - Port: 5432 (hoặc 5433 tùy cấu hình)
  - Username: postgres
  - Password: mật khẩu PostgreSQL của bạn

**Lưu ý:** pgAdmin4 là công cụ quản lý database PostgreSQL. Nếu sử dụng MySQL, có thể sử dụng MySQL Workbench hoặc phpMyAdmin thay thế.

---

## 5. Tải và cài đặt project từ Github project repository

### 5.1 Tải project từ repository

**Cách 1: Clone repository (khuyến nghị)**
```bash
git clone <repository-url>
cd blue_moon
```

**Cách 2: Tải file ZIP**
- Truy cập repository trên GitHub
- Click "Code" → "Download ZIP"
- Giải nén file ZIP vào thư mục mong muốn

### 5.2 Cấu trúc thư mục project

```
blue_moon/
├── blue-moon-app/          # Thư mục chính của ứng dụng
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/       # Source code Java
│   │   │   └── resources/  # Configuration files, templates, SQL scripts
│   │   └── test/           # Test files
│   └── pom.xml            # Maven configuration
└── README.md              # Tài liệu hướng dẫn
```

### 5.3 Kiểm tra cấu hình project

**Kiểm tra file pom.xml:**
- Đảm bảo file `blue-moon-app/pom.xml` tồn tại
- Kiểm tra các dependencies đã được khai báo đúng

**Kiểm tra cấu hình:**
- File `blue-moon-app/src/main/resources/application.properties` phải tồn tại
- File này chứa cấu hình database và các thông số khác

---

## 6. Thiết lập kết nối đối với database

### 6.1 Cài đặt và cấu hình PostgreSQL

**Bước 1: Cài đặt PostgreSQL**
- Tải PostgreSQL 12+ từ https://www.postgresql.org/download/
- Cài đặt PostgreSQL với các tùy chọn mặc định
- Ghi nhớ mật khẩu superuser (postgres) đã thiết lập

**Bước 2: Tạo database**
- Mở pgAdmin4 hoặc psql
- Tạo database mới:
```sql
CREATE DATABASE blue_moon WITH ENCODING 'UTF8';
```

**Bước 3: Chạy schema và seed data**
- Mở pgAdmin4, kết nối với database `blue_moon`
- Chạy file schema: `blue-moon-app/src/main/resources/sql/schema-postgresql.sql`
- Chạy file seed data: `blue-moon-app/src/main/resources/sql/seed-postgresql.sql`

### 6.2 Cấu hình kết nối trong application.properties

**Chỉnh sửa file:** `blue-moon-app/src/main/resources/application.properties`

**Cấu hình cho PostgreSQL:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/blue_moon
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver
```

**Cấu hình cho MySQL (nếu sử dụng):**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/blue_moon?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

**Lưu ý:**
- Thay `your_password` bằng mật khẩu thực tế của database
- Kiểm tra port database (PostgreSQL mặc định 5432, có thể là 5433)
- Đảm bảo database service đang chạy trước khi khởi động ứng dụng

### 6.3 Cấu hình email (tùy chọn)

Nếu muốn sử dụng chức năng quên mật khẩu, cần cấu hình email trong `application.properties`:
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_email_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

---

## 7. Triển khai (deploy) ứng dụng

### 7.1 Deploy Web Application

**Bước 1: Build project**
- Mở Command Prompt hoặc Terminal
- Di chuyển đến thư mục project:
```bash
cd blue-moon-app
```
- Chạy lệnh build:
```bash
mvn clean package
```
- Đợi quá trình build hoàn tất, file JAR sẽ được tạo tại `target/blue-moon-app-1.0.0.jar`

**Bước 2: Chạy ứng dụng**

**Cách 1: Sử dụng Maven (khuyến nghị cho development)**
```bash
mvn spring-boot:run
```

**Cách 2: Chạy file JAR**
```bash
java -jar target/blue-moon-app-1.0.0.jar
```

**Cách 3: Chạy từ IDE**
- Mở project trong IDE (IntelliJ IDEA, Eclipse, VS Code)
- Tìm class `vn.bluemoon.BlueMoonApplication`
- Click Run hoặc Debug

**Bước 3: Kiểm tra ứng dụng đã chạy**
- Đợi thông báo "Started BlueMoonApplication" trong console
- Mở trình duyệt và truy cập: `http://localhost:8080`
- Nếu hiển thị trang đăng nhập, ứng dụng đã chạy thành công

**Bước 4: Đăng nhập lần đầu**
- Username: `admin`
- Password: `admin123`

### 7.2 Deploy trên server production (tùy chọn)

**Cấu hình server:**
- Cài đặt JDK và Maven trên server
- Cấu hình firewall để mở port 8080 (hoặc port khác)
- Đảm bảo database server có thể truy cập từ server ứng dụng

**Chạy ứng dụng như service:**
- Windows: Sử dụng NSSM hoặc Windows Service
- Linux: Sử dụng systemd hoặc init.d script
- Có thể sử dụng reverse proxy (Nginx, Apache) để chạy trên port 80/443

---

## 8. Các quy trình vận hành hệ thống

### 8.1 Quy trình khởi động ứng dụng

**Bước 1: Kiểm tra tiền điều kiện**
- Đảm bảo PostgreSQL/MySQL service đang chạy
- Kiểm tra database `blue_moon` đã được tạo và có dữ liệu
- Kiểm tra cấu hình trong `application.properties` đúng

**Bước 2: Khởi động ứng dụng**
- Mở Command Prompt hoặc Terminal
- Di chuyển đến thư mục `blue-moon-app`
- Chạy lệnh: `mvn spring-boot:run` hoặc `java -jar target/blue-moon-app-1.0.0.jar`

**Bước 3: Kiểm tra ứng dụng đã khởi động**
- Đợi thông báo "Started BlueMoonApplication" trong console
- Kiểm tra không có lỗi trong console
- Mở trình duyệt và truy cập `http://localhost:8080`
- Kiểm tra trang đăng nhập hiển thị đúng

**Bước 4: Đăng nhập và kiểm tra chức năng**
- Đăng nhập với tài khoản admin
- Kiểm tra các menu và chức năng hoạt động bình thường

**Lưu ý:**
- Ứng dụng mặc định chạy trên port 8080
- Có thể thay đổi port trong `application.properties`: `server.port=8081`
- Nếu port đã được sử dụng, ứng dụng sẽ không khởi động được

### 8.2 Quy trình tắt ứng dụng

**Cách 1: Tắt từ console**
- Nếu đang chạy từ Command Prompt/Terminal, nhấn `Ctrl + C`
- Đợi ứng dụng tắt hoàn toàn (thông báo "Process finished")

**Cách 2: Tắt từ IDE**
- Click nút Stop trong IDE
- Hoặc đóng cửa sổ Run/Debug

**Cách 3: Tắt process đang chạy**
- Windows: Mở Task Manager, tìm process `java.exe` và End Task
- Linux/macOS: Tìm process ID: `ps aux | grep java`, sau đó `kill <PID>`

**Lưu ý:**
- Nên tắt ứng dụng một cách an toàn để đảm bảo dữ liệu được lưu đúng
- Không nên force kill process trừ khi ứng dụng bị treo
- Sau khi tắt, có thể kiểm tra database để đảm bảo dữ liệu không bị mất

### 8.3 Quy trình khởi động lại ứng dụng

**Bước 1: Tắt ứng dụng**
- Thực hiện quy trình tắt ứng dụng như mục 8.2

**Bước 2: Kiểm tra và khởi động lại**
- Kiểm tra không còn process Java nào đang chạy
- Thực hiện quy trình khởi động như mục 8.1

**Lưu ý:**
- Khởi động lại thường cần thiết sau khi thay đổi cấu hình
- Sau khi thay đổi code, cần build lại project trước khi chạy

### 8.4 Quy trình backup database

**PostgreSQL:**
```bash
pg_dump -U postgres -d blue_moon > backup_blue_moon.sql
```

**MySQL:**
```bash
mysqldump -u root -p blue_moon > backup_blue_moon.sql
```

**Khuyến nghị:**
- Backup database định kỳ (hàng ngày hoặc hàng tuần)
- Lưu backup ở vị trí an toàn
- Test restore backup để đảm bảo backup hoạt động đúng

---

**Kết thúc báo cáo**




