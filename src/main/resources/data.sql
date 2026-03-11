-- Seed users (students, advisors)
INSERT OR IGNORE INTO users (user_id, name, role) VALUES (1, 'Alice Johnson',   'STUDENT');
INSERT OR IGNORE INTO users (user_id, name, role) VALUES (2, 'Bob Smith',       'STUDENT');
INSERT OR IGNORE INTO users (user_id, name, role) VALUES (3, 'Carol Williams',  'STUDENT');
INSERT OR IGNORE INTO users (user_id, name, role) VALUES (4, 'Dr. Dana Rivera', 'ADVISOR');
INSERT OR IGNORE INTO users (user_id, name, role) VALUES (5, 'Dr. Eric Chen',   'ADVISOR');
INSERT OR IGNORE INTO users (user_id, name, role) VALUES (6, 'Admin User',      'ADMIN');

-- Seed advisors (link to user table)
INSERT OR IGNORE INTO advisors (advis_id, user_id) VALUES (1, 4);
INSERT OR IGNORE INTO advisors (advis_id, user_id) VALUES (2, 5);

-- Seed time slots
INSERT OR IGNORE INTO time_slots (slot_id, advis_id, start_time, end_time, is_open)
    VALUES (1, 1, '2026-03-20 09:00', '2026-03-20 09:30', 1);
INSERT OR IGNORE INTO time_slots (slot_id, advis_id, start_time, end_time, is_open)
    VALUES (2, 1, '2026-03-20 10:00', '2026-03-20 10:30', 1);
INSERT OR IGNORE INTO time_slots (slot_id, advis_id, start_time, end_time, is_open)
    VALUES (3, 1, '2026-03-21 13:00', '2026-03-21 13:30', 1);
INSERT OR IGNORE INTO time_slots (slot_id, advis_id, start_time, end_time, is_open)
    VALUES (4, 2, '2026-03-22 11:00', '2026-03-22 11:30', 1);
INSERT OR IGNORE INTO time_slots (slot_id, advis_id, start_time, end_time, is_open)
    VALUES (5, 2, '2026-03-23 14:00', '2026-03-23 14:30', 1);
