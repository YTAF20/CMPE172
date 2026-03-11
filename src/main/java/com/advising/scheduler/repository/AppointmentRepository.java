package com.advising.scheduler.repository;

import com.advising.scheduler.model.Appointment;
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
public class AppointmentRepository {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentRepository.class);

    private final JdbcTemplate jdbc;

    public AppointmentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void save(Long slotId, Long studId) {
        logger.info("Inserting appointment for slotId={}, studId={}", slotId, studId);
        String sql = "INSERT INTO appointments (slot_id, stud_id, status, create_time) " +
                     "VALUES (?, ?, 'SCHEDULED', datetime('now'))";
        jdbc.update(sql, slotId, studId);
    }

    public List<Appointment> findAll() {
        logger.info("Querying all appointments");
        return jdbc.query(buildSelectSql(""), new AppointmentRowMapper());
    }

    public List<Appointment> findByStudentId(Long studId) {
        logger.info("Querying appointments for studId={}", studId);
        return jdbc.query(buildSelectSql("WHERE a.stud_id = ?"), new AppointmentRowMapper(), studId);
    }

    public Optional<Appointment> findBySlotId(Long slotId) {
        List<Appointment> results = jdbc.query(
            buildSelectSql("WHERE a.slot_id = ?"), new AppointmentRowMapper(), slotId);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public void updateStatus(Long appId, String status) {
        logger.info("Updating appointment id={} to status={}", appId, status);
        jdbc.update("UPDATE appointments SET status = ? WHERE app_id = ?", status, appId);
    }

    private String buildSelectSql(String whereClause) {
        return "SELECT a.app_id, a.slot_id, a.stud_id, " +
               "u_stud.name AS student_name, u_adv.name AS advisor_name, " +
               "ts.start_time, ts.end_time, a.status, a.create_time " +
               "FROM appointments a " +
               "JOIN time_slots ts ON a.slot_id = ts.slot_id " +
               "JOIN advisors adv ON ts.advis_id = adv.advis_id " +
               "JOIN users u_adv ON adv.user_id = u_adv.user_id " +
               "JOIN users u_stud ON a.stud_id = u_stud.user_id " +
               whereClause +
               " ORDER BY ts.start_time";
    }

    private static class AppointmentRowMapper implements RowMapper<Appointment> {
        @Override
        public Appointment mapRow(ResultSet rs, int rowNum) throws SQLException {
            Appointment app = new Appointment();
            app.setAppId(rs.getLong("app_id"));
            app.setSlotId(rs.getLong("slot_id"));
            app.setStudId(rs.getLong("stud_id"));
            app.setStudentName(rs.getString("student_name"));
            app.setAdvisorName(rs.getString("advisor_name"));
            app.setStartTime(rs.getString("start_time"));
            app.setEndTime(rs.getString("end_time"));
            app.setStatus(rs.getString("status"));
            app.setCreateTime(rs.getString("create_time"));
            return app;
        }
    }
}
