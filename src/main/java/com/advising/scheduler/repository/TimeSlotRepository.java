package com.advising.scheduler.repository;

import com.advising.scheduler.model.TimeSlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class TimeSlotRepository {

    private static final Logger logger = LoggerFactory.getLogger(TimeSlotRepository.class);

    private final JdbcTemplate jdbc;

    public TimeSlotRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<TimeSlot> findOpenSlots() {
        logger.info("Querying all open time slots");
        String sql = "SELECT ts.slot_id, ts.advis_id, u.name AS advisor_name, " +
                     "ts.start_time, ts.end_time, ts.is_open " +
                     "FROM time_slots ts " +
                     "JOIN advisors a ON ts.advis_id = a.advis_id " +
                     "JOIN users u ON a.user_id = u.user_id " +
                     "WHERE ts.is_open = 1 " +
                     "ORDER BY ts.start_time";
        return jdbc.query(sql, new TimeSlotRowMapper());
    }

    public Optional<TimeSlot> findById(Long slotId) {
        logger.info("Querying time slot with id={}", slotId);
        String sql = "SELECT ts.slot_id, ts.advis_id, u.name AS advisor_name, " +
                     "ts.start_time, ts.end_time, ts.is_open " +
                     "FROM time_slots ts " +
                     "JOIN advisors a ON ts.advis_id = a.advis_id " +
                     "JOIN users u ON a.user_id = u.user_id " +
                     "WHERE ts.slot_id = ?";
        List<TimeSlot> results = jdbc.query(sql, new TimeSlotRowMapper(), slotId);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public void markSlotBooked(Long slotId) {
        logger.info("Marking slot id={} as booked", slotId);
        jdbc.update("UPDATE time_slots SET is_open = 0 WHERE slot_id = ?", slotId);
    }

    public void markSlotOpen(Long slotId) {
        logger.info("Marking slot id={} as open", slotId);
        jdbc.update("UPDATE time_slots SET is_open = 1 WHERE slot_id = ?", slotId);
    }

    private static class TimeSlotRowMapper implements RowMapper<TimeSlot> {
        @Override
        public TimeSlot mapRow(ResultSet rs, int rowNum) throws SQLException {
            TimeSlot slot = new TimeSlot();
            slot.setSlotId(rs.getLong("slot_id"));
            slot.setAdvisId(rs.getLong("advis_id"));
            slot.setAdvisorName(rs.getString("advisor_name"));
            slot.setStartTime(rs.getString("start_time"));
            slot.setEndTime(rs.getString("end_time"));
            slot.setOpen(rs.getInt("is_open") == 1);
            return slot;
        }
    }
}
