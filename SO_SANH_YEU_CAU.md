# So sánh Project với Yêu cầu Bài tập

## Tổng quan

Dựa trên file "08. Bo Bai Tap.pdf", đây là bảng so sánh chi tiết:

---

## Chương 6: Kỹ nghệ Yêu cầu Phần mềm

### ✅ Đã đủ

| Yêu cầu | Trạng thái | Ghi chú |
|---------|-----------|---------|
| **Quản lý nhân khẩu và hộ khẩu** | ✅ Đã có | - Xem danh sách nhân khẩu<br>- Tìm kiếm nhân khẩu<br>- Xem chi tiết nhân khẩu<br>- Đăng ký tạm trú/tạm vắng<br>- Xóa nhân khẩu |
| **Quản lý thu phí và đóng góp** | ✅ Đã có | - Xem danh sách thu phí<br>- Tìm kiếm thu phí<br>- Thu phí cho hộ dân<br>- Đánh dấu đã thu<br>- Thống kê thu phí |
| **Use case: Tạo khoản thu** | ✅ Đã có | - Tạo khoản thu mới<br>- Thiết lập số tiền mặc định<br>- Kích hoạt/Vô hiệu hóa |
| **Use case: Quản lý khoản thu** | ⚠️ Thiếu một phần | - ✅ Tạo khoản thu<br>- ❌ **Sửa khoản thu** (có endpoint nhưng không có UI)<br>- ✅ Xóa khoản thu |
| **Use case: Quản lý đợt thu phí** | ✅ Đã có | - Thu phí theo tháng/năm<br>- Thu phí cho tất cả hộ dân<br>- Thu phí cho từng hộ dân<br>- Hạn thu phí |

---

## Chương 7: Thiết kế Phần mềm

### ✅ Đã đủ

| Yêu cầu | Trạng thái | Ghi chú |
|---------|-----------|---------|
| **Thiết kế "Quản lý khoản thu"** | ✅ Đã có | - Sơ đồ lớp<br>- Biểu đồ trình tự<br>- Repository pattern |
| **Thiết kế "Quản lý thu phí"** | ✅ Đã có | - Sơ đồ lớp<br>- Entity-Relationship<br>- Database schema |
| **Use case "Thêm khoản thu" (thu phí)** | ✅ Đã có | - Chọn khoản thu<br>- Chọn hộ dân<br>- Thiết lập tháng/năm<br>- Thiết lập hạn thu phí |

---

## Chương 9: Đảm bảo Chất lượng Phần mềm

### ❌ Còn thiếu

| Yêu cầu | Trạng thái | Ghi chú |
|---------|-----------|---------|
| **Test case cho "Tạo khoản thu"** | ❌ Chưa có | PDF yêu cầu:<br>- Test case kiểm thử hộp trắng<br>- Test case kiểm thử hộp đen<br>- Bảng quyết định |
| **Test case cho "Thêm khoản thu" (thu phí)** | ❌ Chưa có | PDF yêu cầu:<br>- Test case kiểm thử hộp trắng<br>- Test case kiểm thử hộp đen<br>- Bảng quyết định<br>- Sơ đồ luồng điều khiển |

---

## Tóm tắt

### ✅ Đã đủ (90%)

1. **Quản lý nhân khẩu**: ✅ Đầy đủ
2. **Quản lý thu phí**: ✅ Đầy đủ
3. **Quản lý khoản thu**: ⚠️ Thiếu nút "Sửa" trong UI
4. **Thiết kế phần mềm**: ✅ Đầy đủ
5. **Kiến trúc**: ✅ Đầy đủ (MVC, 3-layer)

### ❌ Còn thiếu (10%)

1. **Chức năng Sửa khoản thu**: 
   - ✅ Có endpoint `/fee-types/update` 
   - ❌ Không có nút "Sửa" trong UI (`fee-types.html`)
   - ❌ Không có modal để sửa khoản thu

2. **Test Cases**:
   - ❌ Chưa có tài liệu test cases
   - ❌ Chưa có test case cho "Tạo khoản thu"
   - ❌ Chưa có test case cho "Thêm khoản thu" (thu phí)
   - ❌ Chưa có bảng quyết định
   - ❌ Chưa có sơ đồ luồng điều khiển

---

## Khuyến nghị

### Ưu tiên cao (Cần bổ sung ngay)

1. **Thêm nút "Sửa" khoản thu**:
   - Thêm nút "Sửa" vào bảng `fee-types.html`
   - Thêm hàm JavaScript `openEditModal(feeTypeId)`
   - Cập nhật modal để hỗ trợ cả thêm và sửa

2. **Tạo tài liệu Test Cases**:
   - Tạo file `TEST_CASES.md` hoặc thư mục `docs/test-cases/`
   - Viết test cases cho "Tạo khoản thu" (theo mẫu trong PDF)
   - Viết test cases cho "Thêm khoản thu" (theo mẫu trong PDF)
   - Vẽ sơ đồ luồng điều khiển
   - Tạo bảng quyết định

### Ưu tiên thấp (Có thể bổ sung sau)

1. **Unit Tests**: Viết JUnit tests cho các service methods
2. **Integration Tests**: Viết tests cho các controller endpoints
3. **Test Coverage Report**: Tạo báo cáo coverage

---

## Kết luận

**Project đã đáp ứng ~90% yêu cầu của bài tập.**

**Còn thiếu:**
- Chức năng Sửa khoản thu (UI)
- Tài liệu Test Cases

**Đề xuất:** Bổ sung 2 phần còn thiếu để đạt 100% yêu cầu.








