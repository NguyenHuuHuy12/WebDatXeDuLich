package dao;
import model.Booking;
import until.DBConnection;
import until.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    /**
     * Tạo booking mới
     */
    public boolean createBooking(Booking booking) {
        String sql = "INSERT INTO bookings (booking_code, user_id, driver_id, car_type_id, combo_id, from_location, to_location, departure_date, return_date, number_of_seats, total_price, promo_id, discount_amount, final_price, payment_method, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Generate booking code nếu chưa có
            if (booking.getBookingCode() == null) {
                booking.setBookingCode(PasswordUtil.generateBookingCode());
            }

            ps.setString(1, booking.getBookingCode());
            ps.setInt(2, booking.getUserId());

            if (booking.getDriverId() != null) {
                ps.setInt(3, booking.getDriverId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            ps.setInt(4, booking.getCarTypeId());

            if (booking.getComboId() != null) {
                ps.setInt(5, booking.getComboId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            ps.setString(6, booking.getFromLocation());
            ps.setString(7, booking.getToLocation());
            ps.setDate(8, booking.getDepartureDate());
            ps.setDate(9, booking.getReturnDate());
            ps.setInt(10, booking.getNumberOfSeats());
            ps.setDouble(11, booking.getTotalPrice());

            if (booking.getPromoId() != null) {
                ps.setInt(12, booking.getPromoId());
            } else {
                ps.setNull(12, Types.INTEGER);
            }

            ps.setDouble(13, booking.getDiscountAmount());
            ps.setDouble(14, booking.getFinalPrice());
            ps.setString(15, booking.getPaymentMethod());
            ps.setString(16, booking.getNotes());

            int result = ps.executeUpdate();

            if (result > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    booking.setBookingId(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy tất cả bookings của user
     */
    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, u.full_name as user_name, d.full_name as driver_name, c.name as car_name " +
                "FROM bookings b " +
                "LEFT JOIN users u ON b.user_id = u.user_id " +
                "LEFT JOIN drivers d ON b.driver_id = d.driver_id " +
                "LEFT JOIN car_types c ON b.car_type_id = c.car_type_id " +
                "WHERE b.user_id = ? " +
                "ORDER BY b.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                bookings.add(extractBookingFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    /**
     * Lấy booking theo ID
     */
    public Booking getBookingById(int bookingId) {
        String sql = "SELECT b.*, u.full_name as user_name, d.full_name as driver_name, c.name as car_name " +
                "FROM bookings b " +
                "LEFT JOIN users u ON b.user_id = u.user_id " +
                "LEFT JOIN drivers d ON b.driver_id = d.driver_id " +
                "LEFT JOIN car_types c ON b.car_type_id = c.car_type_id " +
                "WHERE b.booking_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractBookingFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy tất cả bookings (cho admin)
     */
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, u.full_name as user_name, d.full_name as driver_name, c.name as car_name " +
                "FROM bookings b " +
                "LEFT JOIN users u ON b.user_id = u.user_id " +
                "LEFT JOIN drivers d ON b.driver_id = d.driver_id " +
                "LEFT JOIN car_types c ON b.car_type_id = c.car_type_id " +
                "ORDER BY b.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                bookings.add(extractBookingFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    /**
     * Cập nhật trạng thái booking
     */
    public boolean updateBookingStatus(int bookingId, String status) {
        String sql = "UPDATE bookings SET booking_status = ? WHERE booking_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, bookingId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật payment status
     */
    public boolean updatePaymentStatus(int bookingId, String paymentStatus) {
        String sql = "UPDATE bookings SET payment_status = ? WHERE booking_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paymentStatus);
            ps.setInt(2, bookingId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Assign driver
     */
    public boolean assignDriver(int bookingId, int driverId) {
        String sql = "UPDATE bookings SET driver_id = ? WHERE booking_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, driverId);
            ps.setInt(2, bookingId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Booking extractBookingFromResultSet(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setBookingId(rs.getInt("booking_id"));
        booking.setBookingCode(rs.getString("booking_code"));
        booking.setUserId(rs.getInt("user_id"));
        booking.setDriverId((Integer) rs.getObject("driver_id"));
        booking.setCarTypeId(rs.getInt("car_type_id"));
        booking.setComboId((Integer) rs.getObject("combo_id"));
        booking.setFromLocation(rs.getString("from_location"));
        booking.setToLocation(rs.getString("to_location"));
        booking.setDepartureDate(rs.getDate("departure_date"));
        booking.setReturnDate(rs.getDate("return_date"));
        booking.setNumberOfSeats(rs.getInt("number_of_seats"));
        booking.setTotalPrice(rs.getDouble("total_price"));
        booking.setPromoId((Integer) rs.getObject("promo_id"));
        booking.setDiscountAmount(rs.getDouble("discount_amount"));
        booking.setFinalPrice(rs.getDouble("final_price"));
        booking.setPaymentMethod(rs.getString("payment_method"));
        booking.setPaymentStatus(rs.getString("payment_status"));
        booking.setBookingStatus(rs.getString("booking_status"));
        booking.setNotes(rs.getString("notes"));
        booking.setCreatedAt(rs.getTimestamp("created_at"));
        booking.setUpdatedAt(rs.getTimestamp("updated_at"));
        return booking;
    }
}