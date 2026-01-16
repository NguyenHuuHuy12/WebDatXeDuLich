package dao;

import model.Combo;
import until.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// ComboDAO.java
public class ComboDAO {

    /**
     * Lấy tất cả combo
     */
    public List<Combo> getAllCombos() {
        List<Combo> combos = new ArrayList<>();
        String sql = "SELECT c.*, ct.name as car_name FROM combos c " +
                "LEFT JOIN car_types ct ON c.car_type_id = ct.car_type_id " +
                "WHERE c.status = 'ACTIVE' ORDER BY c.number_of_people ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                combos.add(extractComboFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return combos;
    }

    /**
     * Lấy combo theo ID
     */
    public Combo getComboById(int comboId) {
        String sql = "SELECT c.*, ct.name as car_name FROM combos c " +
                "LEFT JOIN car_types ct ON c.car_type_id = ct.car_type_id " +
                "WHERE c.combo_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, comboId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractComboFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy combo theo số người
     */
    public List<Combo> getCombosByNumberOfPeople(int numberOfPeople) {
        List<Combo> combos = new ArrayList<>();
        String sql = "SELECT c.*, ct.name as car_name FROM combos c " +
                "LEFT JOIN car_types ct ON c.car_type_id = ct.car_type_id " +
                "WHERE c.number_of_people >= ? AND c.status = 'ACTIVE' " +
                "ORDER BY c.number_of_people ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, numberOfPeople);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                combos.add(extractComboFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return combos;
    }

    /**
     * Thêm combo mới
     */
    public boolean addCombo(Combo combo) {
        String sql = "INSERT INTO combos (name, description, number_of_people, car_type_id, price, image_url, benefits) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, combo.getName());
            ps.setString(2, combo.getDescription());
            ps.setInt(3, combo.getNumberOfPeople());

            if (combo.getCarTypeId() != null) {
                ps.setInt(4, combo.getCarTypeId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }

            ps.setDouble(5, combo.getPrice());
            ps.setString(6, combo.getImageUrl());
            ps.setString(7, combo.getBenefits());

            int result = ps.executeUpdate();

            if (result > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    combo.setComboId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật combo
     */
    public boolean updateCombo(Combo combo) {
        String sql = "UPDATE combos SET name = ?, description = ?, number_of_people = ?, car_type_id = ?, price = ?, image_url = ?, benefits = ?, status = ? WHERE combo_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, combo.getName());
            ps.setString(2, combo.getDescription());
            ps.setInt(3, combo.getNumberOfPeople());

            if (combo.getCarTypeId() != null) {
                ps.setInt(4, combo.getCarTypeId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }

            ps.setDouble(5, combo.getPrice());
            ps.setString(6, combo.getImageUrl());
            ps.setString(7, combo.getBenefits());
            ps.setString(8, combo.getStatus());
            ps.setInt(9, combo.getComboId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa combo
     */
    public boolean deleteCombo(int comboId) {
        String sql = "UPDATE combos SET status = 'INACTIVE' WHERE combo_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, comboId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Đếm tổng số combo
     */
    public int getTotalCombos() {
        String sql = "SELECT COUNT(*) FROM combos WHERE status = 'ACTIVE'";

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

    private Combo extractComboFromResultSet(ResultSet rs) throws SQLException {
        Combo combo = new Combo();
        combo.setComboId(rs.getInt("combo_id"));
        combo.setName(rs.getString("name"));
        combo.setDescription(rs.getString("description"));
        combo.setNumberOfPeople(rs.getInt("number_of_people"));
        combo.setCarTypeId((Integer) rs.getObject("car_type_id"));
        combo.setPrice(rs.getDouble("price"));
        combo.setImageUrl(rs.getString("image_url"));
        combo.setBenefits(rs.getString("benefits"));
        combo.setStatus(rs.getString("status"));
        combo.setCreatedAt(rs.getTimestamp("created_at"));
        return combo;
    }
}
