package com.example.collegepal.database.entity;

public class ScheduleModel {
    private static int staticId;
    private final int id;
    private final String day;
    private final String subject;
    private final String startTime;
    private final String finishTime;

    public ScheduleModel (int id, String day, String subject, String startTime, String finishTime) {
        this.id = id;
        this.day = day;
        this.subject = subject;
        this.startTime = startTime;
        this.finishTime = finishTime;
    }

    public static int getStaticId() {
        return staticId;
    }

    public static void setStaticId(int staticId) {
        ScheduleModel.staticId = staticId;
    }

    public int getId() {
        return id;
    }

    public String getDay() {
        return day;
    }

    public String getSubject() {
        return subject;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }
}
