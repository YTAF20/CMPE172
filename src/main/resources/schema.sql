CREATE TABLE IF NOT EXISTS time_slots (
    slot_id      INTEGER PRIMARY KEY AUTOINCREMENT,
    advisor_name VARCHAR(100) NOT NULL,
    start_time   VARCHAR(20)  NOT NULL,
    end_time     VARCHAR(20)  NOT NULL,
    open         INTEGER      NOT NULL DEFAULT 1,
    version      INTEGER      NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS appointments (
    app_id       INTEGER PRIMARY KEY AUTOINCREMENT,
    slot_id      INTEGER NOT NULL,
    student_name VARCHAR(100) NOT NULL,
    advisor_name VARCHAR(100) NOT NULL,
    start_time   VARCHAR(20)  NOT NULL,
    end_time     VARCHAR(20)  NOT NULL,
    status       VARCHAR(20)  NOT NULL DEFAULT 'SCHEDULED',
    create_time  VARCHAR(30)  NOT NULL
);
