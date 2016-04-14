package com.alisher.schedule;

/**
 * Created by Adilet on 13.04.2016.
 */
public class Subject {
    private int id;
    private int userId;
    private int dayWeekId;
    private String nameSubject;
    private String startTime;
    private String endTime;
    private int cabinet;

    public Subject(int userId, int dayWeekId, String nameSubject, String startTime, String endTime, int cabinet) {
        this.userId = userId;
        this.dayWeekId = dayWeekId;
        this.nameSubject = nameSubject;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cabinet = cabinet;
    }

    public Subject(int id, int userId, int dayWeekId, String nameSubject, String startTime, String endTime, int cabinet) {
        this.id = id;
        this.userId = userId;
        this.dayWeekId = dayWeekId;
        this.nameSubject = nameSubject;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cabinet = cabinet;
    }

    public Subject() {
    }

    public Subject(int id, String name, String startTime, String endTime, Integer cab) {
        this.id = id;
        this.nameSubject = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cabinet = cab;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDayWeekId() {
        return dayWeekId;
    }

    public void setDayWeekId(int dayWeekId) {
        this.dayWeekId = dayWeekId;
    }

    public String getNameSubject() {
        return nameSubject;
    }

    public void setNameSubject(String nameSubject) {
        this.nameSubject = nameSubject;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getCabinet() {
        return cabinet;
    }

    public void setCabinet(int cabinet) {
        this.cabinet = cabinet;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", userId=" + userId +
                ", dayWeekId=" + dayWeekId +
                ", nameSubject='" + nameSubject + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", cabinet=" + cabinet +
                '}';
    }
}
