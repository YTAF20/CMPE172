CREATE TABLE IF NOT EXISTS users (
    user_id   INTEGER PRIMARY KEY AUTOINCREMENT,
    name      TEXT    NOT NULL,
    role      TEXT    NOT NULL CHECK(role IN ('STUDENT', 'ADVISOR', 'ADMIN'))
);

CREATE TABLE IF NOT EXISTS advisors (
    advis_id  INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id   INTEGER NOT NULL UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS time_slots (
    slot_id    INTEGER PRIMARY KEY AUTOINCREMENT,
    advis_id   INTEGER NOT NULL,
    start_time TEXT    NOT NULL,
    end_time   TEXT    NOT NULL,
    is_open    INTEGER NOT NULL DEFAULT 1,
    FOREIGN KEY (advis_id) REFERENCES advisors(advis_id)
);

CREATE TABLE IF NOT EXISTS appointments (
    app_id      INTEGER PRIMARY KEY AUTOINCREMENT,
    slot_id     INTEGER NOT NULL UNIQUE,
    stud_id     INTEGER NOT NULL,
    status      TEXT    NOT NULL DEFAULT 'SCHEDULED',
    create_time TEXT    NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (slot_id) REFERENCES time_slots(slot_id),
    FOREIGN KEY (stud_id) REFERENCES users(user_id)
);
