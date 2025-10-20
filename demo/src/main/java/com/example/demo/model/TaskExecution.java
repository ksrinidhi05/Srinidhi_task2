package com.example.demo.model;

import java.util.Date;

public class TaskExecution {

    private Date startTime; // [cite: 40]
    private Date endTime; // [cite: 40]
    private String output; // [cite: 41]

    // --- Getters and Setters ---

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}