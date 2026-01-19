package controller;

import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.*;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// AdminDashboardServlet.java
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private UserService userService;
    private DriverService driverService;
    private CarTypeService carTypeService;
    private BookingService bookingService;
    private ComboService comboService;
    private PromotionService promotionService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
        driverService = new DriverService();
        carTypeService = new CarTypeService();
        bookingService = new BookingService();
        comboService = new ComboService();
        promotionService = new PromotionService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy thống kê
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalUsers", getUserCount());
        stats.put("totalDrivers", getDriverCount());
        stats.put("totalCarTypes", getCarTypeCount());
        stats.put("totalBookings", getBookingCount());
        stats.put("totalCombos", getComboCount());
        stats.put("totalPromotions", getPromotionCount());

        request.setAttribute("stats", stats);
        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
    }

    private int getUserCount() {
        try {
            return new UserDAO().getTotalUsers();
        } catch (Exception e) {
            return 0;
        }
    }

    private int getDriverCount() {
        try {
            return new DriverDAO().getTotalDrivers();
        } catch (Exception e) {
            return 0;
        }
    }

    private int getCarTypeCount() {
        try {
            return new CarTypeDAO().getTotalCarTypes();
        } catch (Exception e) {
            return 0;
        }
    }

    private int getBookingCount() {
        try {
            return new BookingDAO().getAllBookings().size();
        } catch (Exception e) {
            return 0;
        }
    }

    private int getComboCount() {
        try {
            return new ComboDAO().getTotalCombos();
        } catch (Exception e) {
            return 0;
        }
    }

    private int getPromotionCount() {
        try {
            return new PromotionDAO().getTotalPromotions();
        } catch (Exception e) {
            return 0;
        }
    }
}
