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

@WebServlet("/booking")
class BookingServlet extends HttpServlet {
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
            Booking booking = new Booking();
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