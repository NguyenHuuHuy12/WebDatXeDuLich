package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.PromotionService;

import java.io.IOException;


@WebServlet("/admin/promotions")
class AdminPromotionServlet extends HttpServlet {
    private PromotionService promotionService;

    @Override
    public void init() throws ServletException {
        promotionService = new PromotionService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("promotions", promotionService.getAllPromotions());
        request.getRequestDispatcher("/WEB-INF/views/admin/promotions.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            int promoId = Integer.parseInt(request.getParameter("promoId"));
            promotionService.deletePromotion(promoId);
        }

        doGet(request, response);
    }
}
