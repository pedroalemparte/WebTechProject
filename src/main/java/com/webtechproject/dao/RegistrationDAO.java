package com.webtechproject.dao;

import com.webtechproject.model.Registration;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDAO {

    // Register user to an event
    public boolean register(int userId, int eventId, double pricePaid) {
        String sql = "INSERT INTO registrations (user_id, event_id, ticket_type, price_paid, status) " +
                     "VALUES (?, ?, 'GENERAL', ?, 'CONFIRMED')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, eventId);
            stmt.setDouble(3, pricePaid);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Verify if a user is registred to an event
    public boolean isAlreadyRegistered(int userId, int eventId) {
        String sql = "SELECT id FROM registrations WHERE user_id = ? AND event_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, eventId);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true if a row exists
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get amount of registration to an event (to verify capacity)
    public int countRegistrations(int eventId) {
        String sql = "SELECT COUNT(*) FROM registrations WHERE event_id = ? AND status != 'CANCELLED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // List registred events of the user to "my events"
    public List<Registration> getRegistrationsByUser(int userId) {
        List<Registration> list = new ArrayList<>();
        String sql = "SELECT * FROM registrations WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Registration r = new Registration();
                r.setId(rs.getInt("id"));
                r.setUserId(rs.getInt("user_id"));
                r.setEventId(rs.getInt("event_id"));
                r.setTicketType(rs.getString("ticket_type"));
                r.setPricePaid(rs.getDouble("price_paid"));
                r.setStatus(rs.getString("status"));
                r.setRegisteredAt(rs.getTimestamp("registered_at").toLocalDateTime());
                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}