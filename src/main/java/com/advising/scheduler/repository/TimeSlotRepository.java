package com.advising.scheduler.repository;

import com.advising.scheduler.model.TimeSlot;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class TimeSlotRepository {

    private final JdbcTemplate db;

    public TimeSlotRepository(JdbcTemplate db) {
        this.db = db;
    }

    public List<TimeSlot> findOpenSlots() {
        return db.query("SELECT * FROM time_slots WHERE open = 1 ORDER BY slot_id", this::map);
    }

    public List<TimeSlot> findAll() {
        return db.query("SELECT * FROM time_slots ORDER BY slot_id", this::map);
    }

    public Optional<TimeSlot> findById(Long id) {
        List<TimeSlot> rows = db.query("SELECT * FROM time_slots WHERE slot_id = ?", this::map, id);
        return rows.isEmpty() ? Optional.empty() : Optional.of(rows.get(0));
    }

    public boolean markBooked(Long id, int ver) {
        int rows = db.update(
            "UPDATE time_slots SET open = 0, version = version + 1 WHERE slot_id = ? AND version = ? AND open = 1",
            id, ver
        );
        return rows == 1;
    }

    public void markOpen(Long id) {
        db.update("UPDATE time_slots SET open = 1, version = version + 1 WHERE slot_id = ?", id);
    }

    public void save(TimeSlot ts) {
        db.update(
            "INSERT INTO time_slots (advisor_name, start_time, end_time, open, version) VALUES (?, ?, ?, 1, 0)",
            ts.getAdvisorName(), ts.getStartTime(), ts.getEndTime()
        );
    }

    public boolean delete(Long id) {
        int rows = db.update("DELETE FROM time_slots WHERE slot_id = ? AND open = 1", id);
        return rows == 1;
    }

    private TimeSlot map(ResultSet rs, int n) throws SQLException {
        TimeSlot ts = new TimeSlot();
        ts.setSlotId(rs.getLong("slot_id"));
        ts.setAdvisorName(rs.getString("advisor_name"));
        ts.setStartTime(rs.getString("start_time"));
        ts.setEndTime(rs.getString("end_time"));
        ts.setOpen(rs.getInt("open") == 1);
        ts.setVersion(rs.getInt("version"));
        return ts;
    }
}
