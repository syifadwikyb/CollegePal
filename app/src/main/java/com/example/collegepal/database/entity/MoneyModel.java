package com.example.collegepal.database.entity;

public class MoneyModel {
    private static int staticId;
    private final int id;
    private final String title;
    private final String date;
    private final int amount;
    private final String type;

    public MoneyModel(int id, String title, String date, int amount, String type) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.amount = amount;
        this.type = type;
    }

    public static int getStaticId() {
        return staticId;
    }

    public static void setStaticId(int staticId) {
        MoneyModel.staticId = staticId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public int getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }
}
