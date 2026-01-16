package dao;

import model.Feedback;
import model.User;
import until.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// FeedbackDAO.java
public class FeedbackDAO {

    /**
     * Tạo feedback mới
     */
    public boolean createFeedback(Feedback feedback) {
        String sql = "INSERT INTO feedback (user_id, booking_id, rating, message) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, feedback.getUserId());

            if (feedback.getBookingId() != null) {
                ps.setInt(2, feedback.getBookingId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            ps.setInt(3, feedback.getRating());
            ps.setString(4, feedback.getMessage());

            int result = ps.executeUpdate();

            if (result > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    feedback.setFeedbackId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy tất cả feedback
     */
    public List<Feedback> getAllFeedback() {
        List<Feedback> feedbacks = new ArrayList<>();
        String sql = "SELECT f.*, u.full_name as user_name " +
                "FROM feedback f " +
                "INNER JOIN users u ON f.user_id = u.user_id " +
                "WHERE f.status != 'HIDDEN' " +
                "ORDER BY f.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                feedbacks.add(extractFeedbackFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbacks;
    }

    /**
     * Lấy feedback theo user
     */
    public List<Feedback> getFeedbackByUserId(int userId) {
        List<Feedback> feedbacks = new ArrayList<>();
        String sql = "SELECT f.*, u.full_name as user_name " +
                "FROM feedback f " +
                "INNER JOIN users u ON f.user_id = u.user_id " +
                "WHERE f.user_id = ? " +
                "ORDER BY f.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                feedbacks.add(extractFeedbackFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbacks;
    }

    /**
     * Lấy feedback theo booking
     */
    public Feedback getFeedbackByBookingId(int bookingId) {
        String sql = "SELECT f.*, u.full_name as user_name " +
                "FROM feedback f " +
                "INNER JOIN users u ON f.user_id = u.user_id " +
                "WHERE f.booking_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractFeedbackFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy feedback gần đây (public)
     */
    public List<Feedback> getRecentFeedback(int limit) {
        List<Feedback> feedbacks = new ArrayList<>();
        String sql = "SELECT f.*, u.full_name as user_name " +
                "FROM feedback f " +
                "INNER JOIN users u ON f.user_id = u.user_id " +
                "WHERE f.status = 'REVIEWED' " +
                "ORDER BY f.created_at DESC " +
                "LIMIT ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                feedbacks.add(extractFeedbackFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbacks;
    }

    /**
     * Cập nhật trạng thái feedback
     */
    public boolean updateFeedbackStatus(int feedbackId, String status) {
        String sql = "UPDATE feedback SET status = ? WHERE feedback_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, feedbackId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa feedback
     */
    public boolean deleteFeedback(int feedbackId) {
        String sql = "DELETE FROM feedback WHERE feedback_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, feedbackId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Tính rating trung bình
     */
    public double getAverageRating() {
        String sql = "SELECT AVG(rating) FROM feedback WHERE status = 'REVIEWED'";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
     * Đếm feedback theo rating
     */
    public int countByRating(int rating) {
        String sql = "SELECT COUNT(*) FROM feedback WHERE rating = ? AND status = 'REVIEWED'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, rating);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Feedback extractFeedbackFromResultSet(ResultSet rs) throws SQLException {
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(rs.getInt("feedback_id"));
        feedback.setUserId(rs.getInt("user_id"));
        feedback.setBookingId((Integer) rs.getObject("booking_id"));
        feedback.setRating(rs.getInt("rating"));
        feedback.setMessage(rs.getString("message"));
        feedback.setCreatedAt(rs.getTimestamp("created_at"));
        feedback.setStatus(rs.getString("status"));

        // Set user info if available
        try {
            String userName = rs.getString("user_name");
            if (userName != null) {
                User user = new User();
                user.setFullName(userName);
                feedback.setUser(user);
            }
        } catch (SQLException e) {
            // Column not available, ignore
        }

        return feedback;
    }
}

