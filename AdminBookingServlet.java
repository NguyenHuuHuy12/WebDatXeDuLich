package controller;

import dao.BookingDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet("/admin/bookings")
class AdminBookingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        BookingDAO bookingDAO = new BookingDAO();
        request.setAttribute("bookings", bookingDAO.getAllBookings());
        request.getRequestDispatcher("/WEB-INF/views/admin/bookings.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        int bookingId = Integer.parseInt(request.getParameter("bookingId"));

        BookingDAO bookingDAO = new BookingDAO();

        if ("confirm".equals(action)) {
            bookingDAO.updateBookingStatus(bookingId, "CONFIRMED");
        } else if ("complete".equals(action)) {
            bookingDAO.updateBookingStatus(bookingId, "COMPLETED");
        } else if ("cancel".equals(action)) {
            bookingDAO.updateBookingStatus(bookingId, "CANCELLED");
        } else if ("assignDriver".equals(action)) {
            int driverId = Integer.parseInt(request.getParameter("driverId"));
            bookingDAO.assignDriver(bookingId, driverId);
        }

        response.sendRedirect(request.getContextPath() + "/admin/bookings");
    }
}
