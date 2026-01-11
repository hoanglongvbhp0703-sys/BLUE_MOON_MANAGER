# Hướng dẫn Demo - Blue Moon Apartment Management System

## Thứ tự Demo (Thời gian: 15-20 phút)

---

## PHẦN 1: GIỚI THIỆU VÀ ĐĂNG NHẬP (2 phút)

### 1.1. Giới thiệu hệ thống
- **Mục đích**: Giới thiệu tổng quan về hệ thống
- **Nội dung**:
  - Hệ thống quản lý nhân khẩu và thu phí chung cư Blue Moon
  - Ứng dụng Web sử dụng Spring Boot + Thymeleaf
  - Hỗ trợ 3 vai trò: Quản trị viên, Tổ trưởng, Kế toán

### 1.2. Đăng nhập
- **URL**: `http://localhost:8080/login`
- **Tài khoản demo**:
  - Username: `admin`
  - Password: `admin123`
- **Thao tác**:
  1. Nhập username và password
  2. Click "Đăng nhập"
  3. Hệ thống chuyển đến Dashboard

---

## PHẦN 2: DASHBOARD VÀ TỔNG QUAN (2 phút)

### 2.1. Giới thiệu Dashboard
- **URL**: `http://localhost:8080/main`
- **Nội dung trình bày**:
  - Biểu đồ thống kê thu phí
  - Các chỉ số tổng hợp:
    - Tổng số hộ dân
    - Tổng số nhân khẩu
    - Tổng số khoản phí
    - Tổng số tiền đã thu
  - Menu sidebar với các chức năng

### 2.2. Giới thiệu phân quyền
- **Quản trị viên**: Toàn quyền
- **Tổ trưởng**: Quản lý nhân khẩu, khoản thu, thu phí
- **Kế toán**: Quản lý thu phí, đánh dấu đã thu

---

## PHẦN 3: QUẢN LÝ KHOẢN THU (3 phút)

### 3.1. Xem danh sách khoản thu
- **URL**: `http://localhost:8080/fee-types`
- **Thao tác**:
  1. Click menu "Quản lý khoản thu" ở sidebar
  2. Xem danh sách các khoản thu hiện có
  3. Giải thích các cột: Tên khoản thu, Mô tả, Số tiền mặc định, Trạng thái

### 3.2. Tạo khoản thu mới
- **Thao tác**:
  1. Click nút "Thêm khoản thu"
  2. Điền thông tin:
     - Tên khoản thu: "Phí quản lý chung cư"
     - Mô tả: "Phí quản lý hàng tháng"
     - Số tiền mặc định: 500000
     - Kích hoạt: ✓
  3. Click "Lưu"
  4. Xác nhận khoản thu đã được tạo

### 3.3. Xóa khoản thu
- **Thao tác**:
  1. Tìm khoản thu cần xóa
  2. Click nút "Xóa"
  3. Xác nhận xóa
  4. Xác nhận khoản thu đã bị xóa

**Lưu ý**: Giải thích rằng chức năng "Sửa" đã có trong code nhưng chưa có UI (có thể đề cập như một điểm cải thiện)

---

## PHẦN 4: QUẢN LÝ NHÂN KHẨU (3 phút)

### 4.1. Xem danh sách nhân khẩu
- **URL**: `http://localhost:8080/residents`
- **Thao tác**:
  1. Click menu "Quản lý nhân khẩu" ở sidebar
  2. Xem danh sách các chủ hộ
  3. Giải thích các cột: Mã căn hộ, Mã hộ, Họ tên, CMND/CCCD, Ngày sinh, Quan hệ, Trạng thái

### 4.2. Tìm kiếm nhân khẩu
- **Thao tác**:
  1. Nhập tên nhân khẩu vào ô tìm kiếm
  2. Click "Tìm kiếm"
  3. Xem kết quả tìm kiếm
  4. Thử tìm kiếm theo mã căn hộ

### 4.3. Xem chi tiết nhân khẩu
- **Thao tác**:
  1. Click nút "Xem chi tiết" của một nhân khẩu
  2. Xem thông tin đầy đủ:
     - Thông tin cá nhân
     - Thông tin địa chỉ
     - Tình trạng tạm trú/tạm vắng (nếu có)
  3. Giải thích các thông tin hiển thị

### 4.4. Đăng ký tạm trú/tạm vắng (Nếu có dữ liệu)
- **Thao tác**:
  1. Trong trang chi tiết nhân khẩu
  2. Điền form đăng ký tạm trú:
     - Ngày bắt đầu
     - Ngày kết thúc
     - Lý do
  3. Click "Đăng ký tạm trú"
  4. Xác nhận trạng thái đã thay đổi

---

## PHẦN 5: QUẢN LÝ THU PHÍ (5 phút) - PHẦN QUAN TRỌNG NHẤT

### 5.1. Xem danh sách thu phí
- **URL**: `http://localhost:8080/fees`
- **Thao tác**:
  1. Click menu "Quản lý thu phí" ở sidebar
  2. Xem thống kê ở phía trên:
     - Tổng số
     - Đã thu phí
     - Chưa thu phí
     - Tổng số tiền
     - Đã thu
  3. Xem danh sách các khoản phí
  4. Giải thích các cột: Mã căn hộ, Mã hộ, Tên chủ hộ, Tháng/Năm, Số tiền, Trạng thái, Hạn thu phí, Ngày thu

### 5.2. Tìm kiếm thu phí
- **Thao tác**:
  1. Tìm kiếm theo tên chủ hộ
  2. Tìm kiếm theo tháng/năm
  3. Tìm kiếm theo trạng thái (Đã thu phí / Chưa thu phí)
  4. Kết hợp nhiều tiêu chí tìm kiếm

### 5.3. Thu phí cho hộ dân (Tính năng chính)
- **Thao tác**:
  1. Click nút "Thêm thu phí"
  2. **Bước 1 - Chọn khoản thu**:
     - Chọn một khoản thu từ danh sách (ví dụ: "Phí quản lý chung cư")
     - Xem thông tin: Tên, Mô tả, Số tiền
  3. **Bước 2 - Chọn hộ dân**:
     - Option 1: Chọn "Thu phí cho tất cả các hộ dân" (checkbox)
     - Option 2: Chọn một hộ dân cụ thể từ dropdown
  4. **Bước 3 - Thiết lập thời gian**:
     - Chọn tháng (ví dụ: Tháng 1)
     - Chọn năm (ví dụ: 2026)
  5. **Bước 4 - Thiết lập hạn thu phí** (tùy chọn):
     - Chọn ngày hạn thu phí (ví dụ: 15/01/2026)
  6. Click "Thu phí"
  7. Xác nhận hệ thống đã tạo khoản phí
  8. Xem danh sách đã được cập nhật với khoản phí mới

### 5.4. Đánh dấu đã thu phí (Vai trò Kế toán)
- **Thao tác**:
  1. Tìm một khoản phí có trạng thái "Chưa thu phí"
  2. Click nút "Đánh dấu đã thu"
  3. Điền thông tin:
     - Ngày thu: Chọn ngày hiện tại
     - Phương thức thanh toán: Chọn "Tiền mặt"
     - Ghi chú: (tùy chọn)
  4. Click "Xác nhận"
  5. Xác nhận trạng thái đã chuyển sang "Đã thu phí"
  6. Xem thống kê đã được cập nhật

### 5.5. Thu phí cùng tháng nhưng khác loại phí
- **Thao tác**:
  1. Tạo một khoản thu mới (ví dụ: "Phí gửi xe")
  2. Thu phí cho cùng hộ dân, cùng tháng/năm nhưng khác loại phí
  3. Xác nhận hệ thống cho phép (không báo lỗi duplicate)
  4. Giải thích: Hệ thống cho phép thu nhiều loại phí khác nhau trong cùng tháng

---

## PHẦN 6: THỐNG KÊ (2 phút)

### 6.1. Xem trang thống kê
- **URL**: `http://localhost:8080/statistics`
- **Thao tác**:
  1. Click menu "Thống kê" ở sidebar
  2. Xem các chỉ số:
     - Tổng số hộ dân
     - Tổng số nhân khẩu
     - Tổng số khoản phí
     - Tổng số tiền đã thu
  3. Xem biểu đồ trực quan về tình hình thu phí
  4. Xem bảng thống kê chi tiết

---

## PHẦN 7: QUẢN LÝ NGƯỜI DÙNG (2 phút)

### 7.1. Xem danh sách người dùng
- **URL**: `http://localhost:8080/users`
- **Thao tác**:
  1. Click menu "Quản lý người dùng" ở sidebar
  2. Xem danh sách người dùng
  3. Tìm kiếm người dùng theo username, email, họ tên

### 7.2. Xóa người dùng (Nếu cần demo)
- **Thao tác**:
  1. Tìm một người dùng test (không phải admin)
  2. Click nút "Xóa"
  3. Xác nhận xóa
  4. Giải thích: Xóa người dùng sẽ xóa toàn bộ dữ liệu liên quan

---

## PHẦN 8: TÍNH NĂNG BỔ SUNG (1 phút)

### 8.1. Hạn thu phí
- **Giải thích**:
  - Mỗi khoản phí có thể có hạn thu phí
  - Hiển thị trong cột "Hạn thu phí" ở bảng quản lý thu phí
  - Giúp theo dõi các khoản phí sắp đến hạn

### 8.2. Tạm trú/Tạm vắng
- **Giải thích**:
  - Hệ thống hỗ trợ quản lý tình trạng tạm trú và tạm vắng của cư dân
  - Có thể đăng ký và hủy tạm trú/tạm vắng

---

## PHẦN 9: TÓM TẮT VÀ KẾT LUẬN (1 phút)

### 9.1. Tóm tắt các tính năng đã demo
- ✅ Quản lý khoản thu (Tạo, Xóa)
- ✅ Quản lý nhân khẩu (Xem, Tìm kiếm, Chi tiết, Tạm trú/Tạm vắng)
- ✅ Quản lý thu phí (Xem, Tìm kiếm, Thu phí, Đánh dấu đã thu)
- ✅ Thống kê
- ✅ Quản lý người dùng
- ✅ Phân quyền theo vai trò

### 9.2. Điểm mạnh của hệ thống
- Giao diện hiện đại, dễ sử dụng
- Phân quyền linh hoạt
- Hỗ trợ nhiều loại phí dịch vụ
- Thống kê trực quan
- Hỗ trợ tạm trú/tạm vắng

### 9.3. Điểm cần cải thiện (Nếu có)
- Chức năng Sửa khoản thu (có endpoint nhưng chưa có UI)
- Test cases (cần bổ sung tài liệu)

---

## Lưu ý khi Demo

### Chuẩn bị trước khi demo:
1. ✅ Đảm bảo ứng dụng đang chạy
2. ✅ Đảm bảo database có dữ liệu mẫu
3. ✅ Chuẩn bị tài khoản admin: `admin` / `admin123`
4. ✅ Kiểm tra các chức năng hoạt động bình thường

### Tips khi demo:
- **Nói rõ ràng**: Giải thích từng bước một cách dễ hiểu
- **Tập trung vào tính năng chính**: Quản lý thu phí là phần quan trọng nhất
- **Xử lý lỗi**: Nếu có lỗi, giải thích và xử lý một cách chuyên nghiệp
- **Tương tác**: Hỏi xem người xem có câu hỏi gì không

### Thời gian đề xuất:
- **Tổng thời gian**: 15-20 phút
- **Phần quan trọng nhất**: Quản lý thu phí (5 phút)
- **Có thể rút ngắn**: Bỏ qua một số phần tìm kiếm nếu thiếu thời gian

---

## Script Demo (Gợi ý)

### Mở đầu:
"Xin chào, hôm nay tôi sẽ giới thiệu hệ thống quản lý nhân khẩu và thu phí chung cư Blue Moon. Đây là một ứng dụng web được xây dựng bằng Spring Boot và Thymeleaf, hỗ trợ quản lý toàn diện cho Ban quản trị chung cư."

### Kết thúc:
"Cảm ơn các bạn đã lắng nghe. Hệ thống đã đáp ứng được các yêu cầu cơ bản về quản lý nhân khẩu và thu phí. Nếu có câu hỏi, tôi sẵn sàng trả lời."

---

**Chúc bạn demo thành công! 🎉**








