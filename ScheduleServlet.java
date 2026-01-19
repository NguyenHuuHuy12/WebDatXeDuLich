package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Booking;
import services.BookingService;
import services.CarTypeService;

import java.io.IOException;

@WebServlet("/schedule")
class ScheduleServlet extends HttpServlet {
    private BookingService bookingService;
    private CarTypeService carTypeService;

    @Override
    public void init() throws ServletException {
        bookingService = new BookingService();
        carTypeService = new CarTypeService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("carTypes", carTypeService.getAllCarTypes());
        request.getRequestDispatcher("/WEB-INF/views/schedule.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // Check login
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            // Chưa login, lưu thông tin và redirect
            request.setAttribute("error", "Vui lòng đăng nhập để đặt lịch");
            doGet(request, response);
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");

        try {
            // Tạo booking đơn giản
            Booking booking = new Booking();
            booking.setUserId(userId);
            booking.setFromLocation(request.getParameter("start"));
            booking.setToLocation(request.getParameter("end"));
            booking.setDepartureDate(java.sql.Date.valueOf(request.getParameter("date")));

            // Tự động chọn xe 4 chỗ
            booking.setCarTypeId(1); // Default car type
            booking.setNumberOfSeats(1);
            booking.setPaymentMethod("CASH");

            String result = bookingService.createBooking(booking);

            if (result.startsWith("success")) {
                String bookingCode = result.split(":")[1];
                request.setAttribute("success", "Đăng ký thành công! Mã đặt xe: " + bookingCode);
                request.setAttribute("bookingCode", bookingCode);
            } else {
                request.setAttribute("error", result);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra. Vui lòng thử lại");
        }

        doGet(request, response);
    }
}


