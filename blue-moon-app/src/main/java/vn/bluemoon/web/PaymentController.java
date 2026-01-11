package vn.bluemoon.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.bluemoon.exception.DbException;
import vn.bluemoon.model.entity.FeeCollection;
import vn.bluemoon.model.entity.User;
import vn.bluemoon.security.Authorization;
import vn.bluemoon.service.PaymentService;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

/**
 * Web controller for payment
 */
@Controller
public class PaymentController {
    
    private final PaymentService paymentService = new PaymentService();
    
    @GetMapping("/payment")
    public String paymentPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        // Khởi tạo isAdmin mặc định là false
        boolean isAdmin = false;
        model.addAttribute("user", user);
        
        // Kiểm tra nếu user là admin
        try {
            isAdmin = Authorization.isAdmin(user);
            if (isAdmin) {
                model.addAttribute("isAdmin", true);
                model.addAttribute("hasFees", false); // Admin không có phí
                model.addAttribute("unpaidFees", new java.util.ArrayList<>());
                model.addAttribute("totalRemaining", BigDecimal.ZERO);
                model.addAttribute("message", "Quản trị viên không cần phải đóng tiền.");
                return "payment";
            }
        } catch (DbException e) {
            // Nếu có lỗi khi kiểm tra admin, vẫn tiếp tục xử lý bình thường
            isAdmin = false;
        }
        
        // Đảm bảo isAdmin luôn được set
        model.addAttribute("isAdmin", false);
        
        try {
            // Lấy danh sách phí chưa thanh toán đủ
            List<FeeCollection> unpaidFees = paymentService.getUnpaidFeesForUser(user.getId());
            
            // Tính tổng số tiền còn lại cần đóng
            BigDecimal totalRemaining = paymentService.getTotalRemainingAmount(user.getId());
            
            model.addAttribute("unpaidFees", unpaidFees);
            model.addAttribute("totalRemaining", totalRemaining);
            model.addAttribute("hasFees", !unpaidFees.isEmpty());
        } catch (DbException e) {
            model.addAttribute("unpaidFees", new java.util.ArrayList<>());
            model.addAttribute("totalRemaining", BigDecimal.ZERO);
            model.addAttribute("hasFees", false);
            model.addAttribute("error", "Lỗi khi tải danh sách phí: " + e.getMessage());
        }
        
        return "payment";
    }
    
    @PostMapping("/payment/pay")
    public String processPayment(
            @RequestParam Integer feeId,
            @RequestParam String paymentAmount,
            @RequestParam String paymentMethod,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        try {
            // Kiểm tra fee có thuộc về user này không
            FeeCollection fee = paymentService.getFeeById(feeId);
            if (fee == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy bản ghi thu phí");
                return "redirect:/payment";
            }
            
            // Kiểm tra user có quyền đóng phí này không (thông qua household)
            List<FeeCollection> userFees = paymentService.getUnpaidFeesForUser(user.getId());
            boolean hasPermission = userFees.stream()
                    .anyMatch(f -> f.getId().equals(feeId));
            
            if (!hasPermission) {
                redirectAttributes.addFlashAttribute("error", "Bạn không có quyền đóng phí này");
                return "redirect:/payment";
            }
            
            // Parse số tiền
            BigDecimal amount = new BigDecimal(paymentAmount);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                redirectAttributes.addFlashAttribute("error", "Số tiền phải lớn hơn 0");
                return "redirect:/payment";
            }
            
            // Xử lý thanh toán
            String method = paymentMethod.equals("Chuyển khoản") ? "bank_transfer" : 
                           paymentMethod.equals("Thẻ tín dụng") ? "credit_card" : "cash";
            
            paymentService.processPayment(feeId, amount, method);
            redirectAttributes.addFlashAttribute("success", "Đóng phí thành công!");
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("error", "Số tiền không hợp lệ");
        } catch (DbException e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi đóng phí: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        
        return "redirect:/payment";
    }
}







