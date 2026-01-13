package dao;

import model.CarType;
import model.Booking;
import until.DBConnection;
import until.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// CarTypeDAO.java
public class CarTypeDAO {

    /**
     * Lấy tất cả loại xe
     */
    public List<CarType> getAllCarTypes() {
        List<CarType> carTypes = new ArrayList<>();
        String sql = "SELECT * FROM car_types WHERE status = 'ACTIVE' ORDER BY seats ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                carTypes.add(extractCarTypeFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carTypes;
    }

    /**
     * Lấy loại xe theo số chỗ
     */
    public List<CarType> getCarTypesBySeats(int seats) {
        List<CarType> carTypes = new ArrayList<>();
        String sql = "SELECT * FROM car_types WHERE seats = ? AND status = 'ACTIVE'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, seats);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                carTypes.add(extractCarTypeFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carTypes;
    }

    /**
     * Lấy loại xe theo ID
     */
    public CarType getCarTypeById(int carTypeId) {
        String sql = "SELECT * FROM car_types WHERE car_type_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, carTypeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractCarTypeFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Thêm loại xe mới
     */
    public boolean addCarType(CarType carType) {
        String sql = "INSERT INTO car_types (name, seats, image_url, description, price_per_km, specifications) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, carType.getName());
            ps.setInt(2, carType.getSeats());
            ps.setString(3, carType.getImageUrl());
            ps.setString(4, carType.getDescription());
            ps.setDouble(5, carType.getPricePerKm());
            ps.setString(6, carType.getSpecifications());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật loại xe
     */
    public boolean updateCarType(CarType carType) {
        String sql = "UPDATE car_types SET name = ?, seats = ?, image_url = ?, description = ?, price_per_km = ?, specifications = ?, status = ? WHERE car_type_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, carType.getName());
            ps.setInt(2, carType.getSeats());
            ps.setString(3, carType.getImageUrl());
            ps.setString(4, carType.getDescription());
            ps.setDouble(5, carType.getPricePerKm());
            ps.setString(6, carType.getSpecifications());
            ps.setString(7, carType.getStatus());
            ps.setInt(8, carType.getCarTypeId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa loại xe
     */
    public boolean deleteCarType(int carTypeId) {
        String sql = "UPDATE car_types SET status = 'INACTIVE' WHERE car_type_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, carTypeId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Đếm tổng số loại xe
     */
    public int getTotalCarTypes() {
        String sql = "SELECT COUNT(*) FROM car_types WHERE status = 'ACTIVE'";

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

    private CarType extractCarTypeFromResultSet(ResultSet rs) throws SQLException {
        CarType carType = new CarType();
        carType.setCarTypeId(rs.getInt("car_type_id"));
        carType.setName(rs.getString("name"));
        carType.setSeats(rs.getInt("seats"));
        carType.setImageUrl(rs.getString("image_url"));
        carType.setDescription(rs.getString("description"));
        carType.setPricePerKm(rs.getDouble("price_per_km"));
        carType.setSpecifications(rs.getString("specifications"));
        carType.setStatus(rs.getString("status"));
        carType.setCreatedAt(rs.getTimestamp("created_at"));
        return carType;
    }

}