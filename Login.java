package controller;

import model.User;
import services.UserService;

import jakarta
.servlet.ServletException;
import jakarta
.servlet.annotation.WebServlet;
import jakarta
.servlet.http.HttpServlet;
import jakarta
.servlet.http.HttpServletRequest;
import jakarta
.servlet.http.HttpServletResponse;
import jakarta
.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userService.login(username, password);

        if (user != null) {
            // Đăng nhập thành công
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());

            // Redirect theo role
            if (user.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            }
        } else {
            // Đăng nhập thất bại
            request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}

// RegisterServlet.java
@WebServlet("/register")
class RegisterServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        // Kiểm tra confirm password
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        String result = userService.register(username, password, fullName, email, phone);

        if ("success".equals(result)) {
            request.setAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", result);
            request.setAttribute("username", username);
            request.setAttribute("fullName", fullName);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}

// LogoutServlet.java
@WebServlet("/logout")
class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        response.sendRedirect(request.getContextPath() + "/login");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

// HomeServlet.java
@WebServlet({"/", "/index"})
class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);
    }
}

// CarServlet.java
@WebServlet("/cars")
class CarServlet extends HttpServlet {
    private com.autocars.service.CarTypeService carTypeService;

    @Override
    public void init() throws ServletException {
        carTypeService = new com.autocars.service.CarTypeService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String seatsParam = request.getParameter("seats");

        if (seatsParam != null && !seatsParam.isEmpty()) {
            try {
                int seats = Integer.parseInt(seatsParam);
                request.setAttribute("carTypes", carTypeService.getCarTypesBySeats(seats));
                request.setAttribute("selectedSeats", seats);
            } catch (NumberFormatException e) {
                request.setAttribute("carTypes", carTypeService.getAllCarTypes());
            }
        } else {
            request.setAttribute("carTypes", carTypeService.getAllCarTypes());
        }

        request.getRequestDispatcher("/WEB-INF/views/car.jsp").forward(request, response);
    }
}

// BookingServlet.java
@WebServlet("/booking")
class BookingServlet extends HttpServlet {
    private com.autocars.service.BookingService bookingService;
    private com.autocars.service.CarTypeService carTypeService;

    @Override
    public void init() throws ServletException {
        bookingService = new com.autocars.service.BookingService();
        carTypeService = new com.autocars.service.CarTypeService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check login
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setAttribute("carTypes", carTypeService.getAllCarTypes());
        request.getRequestDispatcher("/WEB-INF/views/booking.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // Check login
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");

        try {
            com.autocars.model.Booking booking = new com.autocars.model.Booking();
            booking.setUserId(userId);
            booking.setCarTypeId(Integer.parseInt(request.getParameter("carTypeId")));
            booking.setFromLocation(request.getParameter("fromLocation"));
            booking.setToLocation(request.getParameter("toLocation"));
            booking.setDepartureDate(java.sql.Date.valueOf(request.getParameter("departureDate")));

            String returnDate = request.getParameter("returnDate");
            if (returnDate != null && !returnDate.isEmpty()) {
                booking.setReturnDate(java.sql.Date.valueOf(returnDate));
            }

            booking.setNumberOfSeats(Integer.parseInt(request.getParameter("numberOfSeats")));
            booking.setPaymentMethod(request.getParameter("paymentMethod"));
            booking.setNotes(request.getParameter("notes"));

            String result = bookingService.createBooking(booking);

            if (result.startsWith("success")) {
                String bookingCode = result.split(":")[1];
                request.setAttribute("success", "Đặt xe thành công! Mã đặt xe: " + bookingCode);
                request.setAttribute("bookingCode", bookingCode);
                request.getRequestDispatcher("/WEB-INF/views/booking-success.jsp").forward(request, response);
            } else {
                request.setAttribute("error", result);
                request.setAttribute("carTypes", carTypeService.getAllCarTypes());
                request.getRequestDispatcher("/WEB-INF/views/booking.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra. Vui lòng thử lại");
            request.setAttribute("carTypes", carTypeService.getAllCarTypes());
            request.getRequestDispatcher("/WEB-INF/views/booking.jsp").forward(request, response);
        }
    }
}

// HistoryServlet.java
@WebServlet("/history")
class HistoryServlet extends HttpServlet {
    private com.autocars.service.BookingService bookingService;

    @Override
    public void init() throws ServletException {
        bookingService = new com.autocars.service.BookingService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check login
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");
        request.setAttribute("bookings", bookingService.getUserBookings(userId));
        request.getRequestDispatcher("/WEB-INF/views/history.jsp").forward(request, response);
    }
}