package dao;

import model.Driver;
import model.Combo;
import model.Promotion;
import until.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// DriverDAO.java
public class DriverDAO {

    /**
     * Lấy tất cả tài xế
     */
    public List<Driver> getAllDrivers() {
        List<Driver> drivers = new ArrayList<>();
        String sql = "SELECT * FROM drivers ORDER BY full_name ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                drivers.add(extractDriverFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }

    /**
     * Lấy tài xế theo ID
     */
    public Driver getDriverById(int driverId) {
        String sql = "SELECT * FROM drivers WHERE driver_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, driverId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractDriverFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy tài xế available
     */
    public List<Driver> getAvailableDrivers() {
        List<Driver> drivers = new ArrayList<>();
        String sql = "SELECT * FROM drivers WHERE status = 'AVAILABLE' ORDER BY rating DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                drivers.add(extractDriverFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }

    /**
     * Thêm tài xế mới
     */
    public boolean addDriver(Driver driver) {
        String sql = "INSERT INTO drivers (full_name, phone, license_number, experience_years) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, driver.getFullName());
            ps.setString(2, driver.getPhone());
            ps.setString(3, driver.getLicenseNumber());
            ps.setInt(4, driver.getExperienceYears());

            int result = ps.executeUpdate();

            if (result > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    driver.setDriverId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật tài xế
     */
    public boolean updateDriver(Driver driver) {
        String sql = "UPDATE drivers SET full_name = ?, phone = ?, license_number = ?, experience_years = ?, rating = ?, status = ? WHERE driver_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, driver.getFullName());
            ps.setString(2, driver.getPhone());
            ps.setString(3, driver.getLicenseNumber());
            ps.setInt(4, driver.getExperienceYears());
            ps.setDouble(5, driver.getRating());
            ps.setString(6, driver.getStatus());
            ps.setInt(7, driver.getDriverId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa tài xế
     */
    public boolean deleteDriver(int driverId) {
        String sql = "UPDATE drivers SET status = 'INACTIVE' WHERE driver_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, driverId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật trạng thái tài xế
     */
    public boolean updateDriverStatus(int driverId, String status) {
        String sql = "UPDATE drivers SET status = ? WHERE driver_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, driverId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Đếm tổng số tài xế
     */
    public int getTotalDrivers() {
        String sql = "SELECT COUNT(*) FROM drivers WHERE status != 'INACTIVE'";

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

    private Driver extractDriverFromResultSet(ResultSet rs) throws SQLException {
        Driver driver = new Driver();
        driver.setDriverId(rs.getInt("driver_id"));
        driver.setFullName(rs.getString("full_name"));
        driver.setPhone(rs.getString("phone"));
        driver.setLicenseNumber(rs.getString("license_number"));
        driver.setExperienceYears(rs.getInt("experience_years"));
        driver.setRating(rs.getDouble("rating"));
        driver.setStatus(rs.getString("status"));
        driver.setCreatedAt(rs.getTimestamp("created_at"));
        return driver;
    }
}


