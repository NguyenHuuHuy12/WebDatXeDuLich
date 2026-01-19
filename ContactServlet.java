package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import services.FeedbackService;

import java.io.IOException;

@WebServlet("/contact")
class ContactServlet extends HttpServlet {
    private FeedbackService feedbackService;

    @Override
    public void init() throws ServletException {
        feedbackService = new FeedbackService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy feedback gần đây để hiển thị
        request.setAttribute("recentFeedback", feedbackService.getRecentFeedback(10));
        request.setAttribute("ratingStats", feedbackService.getRatingStats());

        request.getRequestDispatcher("/WEB-INF/views/contact.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // Check if user is logged in
        HttpSession session = request.getSession(false);
        Integer userId = null;

        if (session != null && session.getAttribute("userId") != null) {
            userId = (Integer) session.getAttribute("userId");
        }

        // Get parameters
        String name = request.getParameter("name");
        String message = request.getParameter("message");
        int rating = Integer.parseInt(request.getParameter("rating"));

        if (userId != null) {
            // User đã đăng nhập, lưu feedback
            String result = feedbackService.createFeedback(userId, null, rating, message);

            if ("success".equals(result)) {
                request.setAttribute("success", "Cảm ơn bạn đã gửi đánh giá! Chúng tôi sẽ xem xét và phản hồi sớm nhất.");
            } else {
                request.setAttribute("error", result);
            }
        } else {
            // User chưa đăng nhập, thông báo
            request.setAttribute("error", "Vui lòng đăng nhập để gửi đánh giá");
        }

        doGet(request, response);
    }
}
