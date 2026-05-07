# Academic Advising Appointment Scheduler

A Spring Boot web app for booking advising appointments. Students can view available time slots, book appointments, and cancel them. Advisors can manage their availability through the admin panel.

## Prerequisites

- Java 17+
- Maven

If you don't have Maven, install it with Homebrew:
```
brew install maven
```

## How to Run

Clone the repo and navigate to the project folder, then run:
```
mvn spring-boot:run
```

Open your browser and go to: http://localhost:8080

## Pages

| URL | Description |
|-----|-------------|
| `/` | Home page |
| `/slots` | View available time slots and book one |
| `/appointments` | View all appointments and cancel |
| `/admin/slots` | Add or delete time slots |
| `/health` | JSON status and booking stats |

## Notes

- The database is SQLite (`advising.db`) and gets created automatically on first run
- Sample time slots are loaded from `src/main/resources/data.sql` on startup
- The notification service is built into the same app at `/notification-service/send`
- App runs on port 8080 by default
