package dao;

import model.Promotion;
import until.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// PromotionDAO.java
public class PromotionDAO {

    /**
     * Lấy tất cả khuyến mãi
     */
    public List<Promotion> getAllPromotions() {
        List<Promotion> promotions = new ArrayList<>();
        String sql = "SELECT * FROM promotions ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                promotions.add(extractPromotionFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promotions;
    }

    /**
     * Lấy khuyến mãi đang active
     */
    public List<Promotion> getActivePromotions() {
        List<Promotion> promotions = new ArrayList<>();
        String sql = "SELECT * FROM promotions WHERE status = 'ACTIVE' AND valid_from <= CURDATE() AND valid_to >= CURDATE() AND (usage_limit IS NULL OR used_count < usage_limit)";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                promotions.add(extractPromotionFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promotions;
    }

    /**
     * Lấy khuyến mãi theo code
     */
    public Promotion getPromotionByCode(String code) {
        String sql = "SELECT * FROM promotions WHERE code = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractPromotionFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Validate và lấy khuyến mãi
     */
    public Promotion validatePromotion(String code, double orderAmount) {
        String sql = "SELECT * FROM promotions WHERE code = ? AND status = 'ACTIVE' " +
                "AND valid_from <= CURDATE() AND valid_to >= CURDATE() " +
                "AND min_amount <= ? " +
                "AND (usage_limit IS NULL OR used_count < usage_limit)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, code);
            ps.setDouble(2, orderAmount);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractPromotionFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tăng số lần sử dụng
     */
    public boolean incrementUsedCount(int promoId) {
        String sql = "UPDATE promotions SET used_count = used_count + 1 WHERE promo_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, promoId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Thêm khuyến mãi
     */
    public boolean addPromotion(Promotion promo) {
        String sql = "INSERT INTO promotions (code, description, discount_type, discount_value, min_amount, max_discount, valid_from, valid_to, usage_limit) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, promo.getCode());
            ps.setString(2, promo.getDescription());
            ps.setString(3, promo.getDiscountType());
            ps.setDouble(4, promo.getDiscountValue());
            ps.setDouble(5, promo.getMinAmount());

            if (promo.getMaxDiscount() != null) {
                ps.setDouble(6, promo.getMaxDiscount());
            } else {
                ps.setNull(6, Types.DOUBLE);
            }

            ps.setDate(7, promo.getValidFrom());
            ps.setDate(8, promo.getValidTo());

            if (promo.getUsageLimit() != null) {
                ps.setInt(9, promo.getUsageLimit());
            } else {
                ps.setNull(9, Types.INTEGER);
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật khuyến mãi
     */
    public boolean updatePromotion(Promotion promo) {
        String sql = "UPDATE promotions SET code = ?, description = ?, discount_type = ?, discount_value = ?, min_amount = ?, max_discount = ?, valid_from = ?, valid_to = ?, usage_limit = ?, status = ? WHERE promo_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, promo.getCode());
            ps.setString(2, promo.getDescription());
            ps.setString(3, promo.getDiscountType());
            ps.setDouble(4, promo.getDiscountValue());
            ps.setDouble(5, promo.getMinAmount());

            if (promo.getMaxDiscount() != null) {
                ps.setDouble(6, promo.getMaxDiscount());
            } else {
                ps.setNull(6, Types.DOUBLE);
            }

            ps.setDate(7, promo.getValidFrom());
            ps.setDate(8, promo.getValidTo());

            if (promo.getUsageLimit() != null) {
                ps.setInt(9, promo.getUsageLimit());
            } else {
                ps.setNull(9, Types.INTEGER);
            }

            ps.setString(10, promo.getStatus());
            ps.setInt(11, promo.getPromoId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa khuyến mãi
     */
    public boolean deletePromotion(int promoId) {
        String sql = "UPDATE promotions SET status = 'INACTIVE' WHERE promo_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, promoId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Đếm tổng số khuyến mãi
     */
    public int getTotalPromotions() {
        String sql = "SELECT COUNT(*) FROM promotions WHERE status = 'ACTIVE'";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Promotion extractPromotionFromResultSet(ResultSet rs) throws SQLException {
        Promotion promo = new Promotion();
        promo.setPromoId(rs.getInt("promo_id"));
        promo.setCode(rs.getString("code"));
        promo.setDescription(rs.getString("description"));
        promo.setDiscountType(rs.getString("discount_type"));
        promo.setDiscountValue(rs.getDouble("discount_value"));
        promo.setMinAmount(rs.getDouble("min_amount"));
        promo.setMaxDiscount((Double) rs.getObject("max_discount"));
        promo.setValidFrom(rs.getDate("valid_from"));
        promo.setValidTo(rs.getDate("valid_to"));
        promo.setUsageLimit((Integer) rs.getObject("usage_limit"));
        promo.setUsedCount(rs.getInt("used_count"));
        promo.setStatus(rs.getString("status"));
        return promo;
    }
}