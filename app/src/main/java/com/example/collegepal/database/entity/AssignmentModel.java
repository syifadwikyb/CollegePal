package com.example.collegepal.database.entity;

public class AssignmentModel {
    private static int staticId;
    private final int id;
    private final String subject;
    private final String deadline;
    private final String description;
    private String status;

    public AssignmentModel(int id, String subject, String deadline, String description, String status) {
        this.id = id;
        this.subject = subject;
        this.deadline = deadline;
        this.description = description;
        this.status = status;
    }

    public static int getStaticId() {
        return staticId;
    }

    public static void setStaticId(int staticId) {
        AssignmentModel.staticId = staticId;
    }

    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}