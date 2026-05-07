package com.advising.scheduler.repository;

import com.advising.scheduler.model.Appointment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class AppointmentRepository {

    private final JdbcTemplate db;

    public AppointmentRepository(JdbcTemplate db) {
        this.db = db;
    }

    public void save(Appointment a) {
        KeyHolder keys = new GeneratedKeyHolder();
        db.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO appointments (slot_id, student_name, advisor_name, start_time, end_time, status, create_time) VALUES (?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            ps.setLong(1, a.getSlotId());
            ps.setString(2, a.getStudentName());
            ps.setString(3, a.getAdvisorName());
            ps.setString(4, a.getStartTime());
            ps.setString(5, a.getEndTime());
            ps.setString(6, a.getStatus());
            ps.setString(7, a.getCreateTime());
            return ps;
        }, keys);
        Number key = keys.getKey();
        if (key != null) a.setAppId(key.longValue());
    }

    public List<Appointment> findAll() {
        return db.query("SELECT * FROM appointments ORDER BY app_id DESC", this::map);
    }

    public Optional<Appointment> findById(Long id) {
        List<Appointment> rows = db.query("SELECT * FROM appointments WHERE app_id = ?", this::map, id);
        return rows.isEmpty() ? Optional.empty() : Optional.of(rows.get(0));
    }

    public void updateStatus(Long appId, String status) {
        db.update("UPDATE appointments SET status = ? WHERE app_id = ?", status, appId);
    }

    private Appointment map(ResultSet rs, int n) throws SQLException {
        Appointment a = new Appointment();
        a.setAppId(rs.getLong("app_id"));
        a.setSlotId(rs.getLong("slot_id"));
        a.setStudentName(rs.getString("student_name"));
        a.setAdvisorName(rs.getString("advisor_name"));
        a.setStartTime(rs.getString("start_time"));
        a.setEndTime(rs.getString("end_time"));
        a.setStatus(rs.getString("status"));
        a.setCreateTime(rs.getString("create_time"));
        return a;
    }
}
