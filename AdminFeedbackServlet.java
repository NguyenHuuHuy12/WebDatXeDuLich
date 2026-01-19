package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.FeedbackService;

import java.io.IOException;

// AdminFeedbackServlet.java
@WebServlet("/admin/feedback")
class AdminFeedbackServlet extends HttpServlet {
    private FeedbackService feedbackService;

    @Override
    public void init() throws ServletException {
        feedbackService = new FeedbackService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("feedbacks", feedbackService.getAllFeedback());
        request.setAttribute("stats", feedbackService.getRatingStats());
        request.getRequestDispatcher("/WEB-INF/views/admin/feedback.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        int feedbackId = Integer.parseInt(request.getParameter("feedbackId"));

        if ("approve".equals(action)) {
            feedbackService.updateFeedbackStatus(feedbackId, "REVIEWED");
        } else if ("hide".equals(action)) {
            feedbackService.updateFeedbackStatus(feedbackId, "HIDDEN");
        } else if ("delete".equals(action)) {
            feedbackService.deleteFeedback(feedbackId);
        }

        doGet(request, response);
    }
}