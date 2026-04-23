package com.advising.scheduler.model;

public class NotificationRequest {
    private String studentName;
    private String advisorName;
    private String startTime;
    private String endTime;
    private Long appointmentId;

    public NotificationRequest() {}

    public NotificationRequest(String studentName, String advisorName,
                                String startTime, String endTime, Long appointmentId) {
        this.studentName = studentName;
        this.advisorName = advisorName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.appointmentId = appointmentId;
    }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getAdvisorName() { return advisorName; }
    public void setAdvisorName(String advisorName) { this.advisorName = advisorName; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
}
