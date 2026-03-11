package com.advising.scheduler.model;

public class TimeSlot {
    private Long slotId;
    private String advisorName;
    private String startTime;
    private String endTime;
    private boolean open;

    public TimeSlot() {}

    public TimeSlot(Long slotId, String advisorName, String startTime, String endTime) {
        this.slotId = slotId;
        this.advisorName = advisorName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.open = true;
    }

    public Long getSlotId() { return slotId; }
    public void setSlotId(Long slotId) { this.slotId = slotId; }

    public String getAdvisorName() { return advisorName; }
    public void setAdvisorName(String advisorName) { this.advisorName = advisorName; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public boolean isOpen() { return open; }
    public void setOpen(boolean open) { this.open = open; }
}
