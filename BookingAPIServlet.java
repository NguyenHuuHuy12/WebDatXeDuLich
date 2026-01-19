package controller;

import dao.BookingDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Booking;
import services.BookingService;

import java.io.IOException;

// API Servlet để xử lý AJAX requests
@WebServlet("/api/booking/*")
class BookingAPIServlet extends HttpServlet {
    private BookingService bookingService;

    @Override
    public void init() throws ServletException {
        bookingService = new BookingService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.startsWith("/")) {
            String[] parts = pathInfo.split("/");
            if (parts.length >= 2) {
                try {
                    int bookingId = Integer.parseInt(parts[1]);
                    BookingDAO bookingDAO = new BookingDAO();
                    Booking booking = bookingDAO.getBookingById(bookingId);

                    if (booking != null) {
                        // Trả về JSON
                        String json = String.format(
                                "{\"bookingCode\":\"%s\",\"fromLocation\":\"%s\",\"toLocation\":\"%s\",\"departureDate\":\"%s\",\"numberOfSeats\":%d,\"finalPrice\":%.2f}",
                                booking.getBookingCode(),
                                booking.getFromLocation(),
                                booking.getToLocation(),
                                booking.getDepartureDate().toString(),
                                booking.getNumberOfSeats(),
                                booking.getFinalPrice()
                        );
                        response.getWriter().write(json);
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        response.getWriter().write("{\"error\":\"Booking not found\"}");
                    }
                } catch (NumberFormatException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\":\"Invalid booking ID\"}");
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();

        // Handle cancel booking
        if (pathInfo != null && pathInfo.contains("/cancel/")) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"success\":false,\"message\":\"Not authenticated\"}");
                return;
            }

            Integer userId = (Integer) session.getAttribute("userId");

            try {
                String[] parts = pathInfo.split("/");
                int bookingId = Integer.parseInt(parts[parts.length - 1]);

                String result = bookingService.cancelBooking(bookingId, userId);

                if ("success".equals(result)) {
                    response.getWriter().write("{\"success\":true,\"message\":\"Đã hủy chuyến đi\"}");
                } else {
                    response.getWriter().write("{\"success\":false,\"message\":\"" + result + "\"}");
                }

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"success\":false,\"message\":\"Server error\"}");
            }
        }
    }
}
