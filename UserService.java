package services;

import dao.UserDAO;
import model.User;
import until.ValidationUtil;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Đăng nhập
     */
    public User login(String username, String password) {
        if (ValidationUtil.isEmpty(username) || ValidationUtil.isEmpty(password)) {
            return null;
        }
        return userDAO.login(username, password);
    }

    /**
     * Đăng ký
     */
    public String register(String username, String password, String fullName, String email, String phone) {
        // Validate input
        if (ValidationUtil.isEmpty(username)) {
            return "Username không được để trống";
        }
        if (!ValidationUtil.isValidUsername(username)) {
            return "Username không hợp lệ (3-50 ký tự, chỉ chứa chữ, số, dấu chấm, gạch dưới)";
        }
        if (ValidationUtil.isEmpty(password)) {
            return "Mật khẩu không được để trống";
        }
        if (!ValidationUtil.isStrongPassword(password)) {
            return "Mật khẩu phải có ít nhất 6 ký tự";
        }
        if (ValidationUtil.isEmpty(fullName)) {
            return "Họ tên không được để trống";
        }
        if (ValidationUtil.isEmpty(email) || !ValidationUtil.isValidEmail(email)) {
            return "Email không hợp lệ";
        }
        if (!ValidationUtil.isEmpty(phone) && !ValidationUtil.isValidPhone(phone)) {
            return "Số điện thoại không hợp lệ";
        }

        // Check duplicate
        if (userDAO.isUsernameExists(username)) {
            return "Username đã tồn tại";
        }
        if (userDAO.isEmailExists(email)) {
            return "Email đã được đăng ký";
        }

        // Create user
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRole("USER");

        if (userDAO.register(user)) {
            return "success";
        }
        return "Đăng ký thất bại. Vui lòng thử lại";
    }

   
}
