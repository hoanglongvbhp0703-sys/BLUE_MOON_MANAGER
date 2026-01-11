package vn.bluemoon.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.bluemoon.exception.DbException;
import vn.bluemoon.model.entity.Resident;
import vn.bluemoon.model.entity.User;
import vn.bluemoon.security.Authorization;
import vn.bluemoon.service.PersonalInfoService;
import vn.bluemoon.validation.ValidationException;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;

/**
 * Web controller for personal information
 */
@Controller
public class PersonalController {
    
    private final PersonalInfoService personalInfoService = new PersonalInfoService();
    
    @GetMapping("/personal-info")
    public String personalInfoPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        
        // Khởi tạo các biến mặc định
        boolean isAdmin = false;
        model.addAttribute("isAdmin", false);
        model.addAttribute("hasResident", false);
        model.addAttribute("resident", null);
        
        // Kiểm tra nếu user là admin
        try {
            isAdmin = Authorization.isAdmin(user);
            if (isAdmin) {
                model.addAttribute("isAdmin", true);
                model.addAttribute("hasResident", false);
                model.addAttribute("resident", null);
                model.addAttribute("message", "Quản trị viên không cần phải đăng ký thông tin cá nhân. Vui lòng sử dụng chức năng 'Quản lý nhân khẩu' để quản lý thông tin.");
                return "personal-info";
            }
        } catch (DbException e) {
            // Nếu có lỗi khi kiểm tra admin, vẫn tiếp tục xử lý bình thường
            isAdmin = false;
        }
        
        model.addAttribute("isAdmin", false);
        
        try {
            // Kiểm tra xem user đã có resident record chưa
            Resident resident = personalInfoService.getPersonalInfo(user.getId());
            model.addAttribute("resident", resident);
            model.addAttribute("hasResident", resident != null);
        } catch (DbException e) {
            model.addAttribute("resident", null);
            model.addAttribute("hasResident", false);
            model.addAttribute("error", "Lỗi khi tải thông tin: " + e.getMessage());
        }
        
        return "personal-info";
    }
    
    @PostMapping("/personal-info/register")
    public String registerPersonalInfo(
            HttpSession session,
            @RequestParam String fullName,
            @RequestParam String idCard,
            @RequestParam(required = false) String dateOfBirth,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String occupation,
            @RequestParam(required = false) String apartmentCode,
            @RequestParam(required = false) String householdCode,
            RedirectAttributes redirectAttributes) {
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        // Kiểm tra nếu user là admin thì không cho phép đăng ký
        try {
            if (Authorization.isAdmin(user)) {
                redirectAttributes.addFlashAttribute("error", "Quản trị viên không cần phải đăng ký thông tin cá nhân.");
                return "redirect:/personal-info";
            }
        } catch (DbException e) {
            // Nếu có lỗi khi kiểm tra admin, vẫn tiếp tục xử lý bình thường
        }
        
        try {
            vn.bluemoon.model.dto.PersonalInfoRequest request = new vn.bluemoon.model.dto.PersonalInfoRequest();
            request.setFullName(fullName);
            request.setIdCard(idCard);
            
            if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
                request.setDateOfBirth(LocalDate.parse(dateOfBirth));
            }
            
            request.setGender(gender);
            request.setPhone(phone);
            request.setEmail(email);
            request.setOccupation(occupation);
            request.setApartmentCode(apartmentCode);
            request.setHouseholdCode(householdCode);
            request.setStatus("active");
            
            // Kiểm tra xem đã có resident record chưa để hiển thị thông báo phù hợp
            Resident existingResident = personalInfoService.getPersonalInfo(user.getId());
            boolean isUpdate = existingResident != null;
            
            personalInfoService.registerOrUpdatePersonalInfo(user.getId(), request);
            
            if (isUpdate) {
                redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin cá nhân thành công!");
            } else {
                redirectAttributes.addFlashAttribute("success", "Đăng ký thông tin cá nhân thành công!");
            }
        } catch (ValidationException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (DbException e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi định dạng dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
        
        return "redirect:/personal-info";
    }
}

