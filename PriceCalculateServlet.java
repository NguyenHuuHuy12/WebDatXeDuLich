package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.BookingService;
import services.PromotionService;

import java.io.IOException;

// API Servlet để check giá và apply promo
@WebServlet("/api/price/calculate")
class PriceCalculateServlet extends HttpServlet {
    private BookingService bookingService;
    private PromotionService promotionService;

    @Override
    public void init() throws ServletException {
        bookingService = new BookingService();
        promotionService = new PromotionService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            int carTypeId = Integer.parseInt(request.getParameter("carTypeId"));
            String fromLocation = request.getParameter("fromLocation");
            String toLocation = request.getParameter("toLocation");
            int numberOfSeats = Integer.parseInt(request.getParameter("numberOfSeats"));
            String promoCode = request.getParameter("promoCode");

            // Calculate base price
            double basePrice = bookingService.calculatePrice(carTypeId, fromLocation, toLocation, numberOfSeats);
            double finalPrice = basePrice;
            double discount = 0;
            String message = "";

            // Apply promo if provided
            if (promoCode != null && !promoCode.trim().isEmpty()) {
                PromotionService.ApplyPromotionResult promoResult =
                        promotionService.applyPromotion(promoCode, basePrice);

                if (promoResult.isSuccess()) {
                    discount = promoResult.getDiscountAmount();
                    finalPrice = promoResult.getFinalAmount();
                    message = promoResult.getMessage();
                } else {
                    message = promoResult.getMessage();
                }
            }

            String json = String.format(
                    "{\"success\":true,\"basePrice\":%.2f,\"discount\":%.2f,\"finalPrice\":%.2f,\"message\":\"%s\"}",
                    basePrice, discount, finalPrice, message
            );

            response.getWriter().write(json);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false,\"message\":\"Invalid parameters\"}");
        }
    }
}