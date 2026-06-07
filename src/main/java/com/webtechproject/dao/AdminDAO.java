package com.webtechproject.dao;

import com.webtechproject.dao.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    public int countUsers() {
        return countQuery("SELECT COUNT(*) FROM users");
    }

    public int countOrganizers() {
        return countQuery("SELECT COUNT(*) FROM users WHERE role = 'ORGANIZER'");
    }

    public int countEvents() {
        return countQuery("SELECT COUNT(*) FROM events");
    }

    public int countRegistrations() {
        return countQuery("SELECT COUNT(*) FROM registrations");
    }

    public List<OrganizerRow> getAllOrganizers() {
        List<OrganizerRow> list = new ArrayList<>();
        String sql = "SELECT u.id, u.full_name, u.email, " +
                     "COUNT(DISTINCT e.id) AS event_count, " +
                     "COUNT(DISTINCT r.id) AS registration_count " +
                     "FROM users u " +
                     "LEFT JOIN events e ON e.organizer_id = u.id " +
                     "LEFT JOIN registrations r ON r.event_id = e.id " +
                     "WHERE u.role = 'ORGANIZER' " +
                     "GROUP BY u.id, u.full_name, u.email " +
                     "ORDER BY u.full_name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                OrganizerRow row = new OrganizerRow();
                row.id = rs.getInt("id");
                row.fullName = rs.getString("full_name");
                row.email = rs.getString("email");
                row.eventCount = rs.getInt("event_count");
                row.registrationCount = rs.getInt("registration_count");
                list.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<EventRow> getAllEvents() {
        List<EventRow> list = new ArrayList<>();
        String sql = "SELECT e.id, e.title, e.date_time, e.location, e.virtual, " +
                     "u.full_name AS organizer_name, " +
                     "COUNT(r.id) AS registration_count, e.capacity " +
                     "FROM events e " +
                     "JOIN users u ON u.id = e.organizer_id " +
                     "LEFT JOIN registrations r ON r.event_id = e.id " +
                     "GROUP BY e.id, e.title, e.date_time, e.location, e.virtual, u.full_name " +
                     "ORDER BY e.date_time DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                EventRow row = new EventRow();
                row.id = rs.getInt("id");
                row.title = rs.getString("title");
                row.dateTime = rs.getTimestamp("date_time").toLocalDateTime().toString().replace("T", " ").substring(0, 16);
                row.location = rs.getString("location");
                row.isVirtual = rs.getBoolean("virtual");
                row.organizerName = rs.getString("organizer_name");
                row.registrationCount = rs.getInt("registration_count");
                row.capacity = rs.getInt("capacity");
                list.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean revokeOrganizer(int userId) {
        String sql = "UPDATE users SET role = 'ATTENDEE' WHERE id = ? AND role = 'ORGANIZER'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private int countQuery(String sql) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static class OrganizerRow {
        public int id;
        public String fullName;
        public String email;
        public int eventCount;
        public int registrationCount;
    }

    public static class EventRow {
        public int id;
        public String title;
        public String dateTime;
        public String location;
        public boolean isVirtual;
        public String organizerName;
        public int registrationCount;
        public int capacity;
    }
}
