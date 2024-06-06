package com.example.collegepal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.collegepal.database.entity.AssignmentModel;
import com.example.collegepal.database.entity.MoneyModel;
import com.example.collegepal.database.entity.ScheduleModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DatabaseCP extends SQLiteOpenHelper {
    private final Context CONTEXT;
    private static final String DATABASE_NAME = "CollegePalDatabase";
    private static final int DATABASE_VERSION = 10;

    private static final String TABLE_USER = "User";
    private static final String USER_ID = "UserId";
    private static final String USER_NAME = "UserName";
    private static final String USER_EMAIL = "UserEmail";
    private static final String USER_PASSWORD = "UserPassword";
    private static final String USER_NUMBER = "UserNumber";
    private static final String USER_BIRTH = "UserDateBirth";
    private static final String USER_GENDER = "UserGender";
    private static final String USER_COUNTRY = "UserCountry";

    private static final String TABLE_ASSIGNMENT = "Assignment";
    private static final String ASSIGNMENT_USER_ID = "AssignmentUserId";
    private static final String ASSIGNMENT_ID = "AssignmentId";
    private static final String ASSIGNMENT_TITLE = "Title";
    private static final String ASSIGNMENT_DEADLINE = "Deadline";
    private static final String ASSIGNMENT_DESCRIPTION = "Description";
    private static final String ASSIGNMENT_STATUS = "Status";

    private static final String TABLE_MONEY = "MoneyManagement";
    private static final String MONEY_USER_ID = "MoneyUserId";
    private static final String MONEY_ID = "MoneyId";
    private static final String MONEY_TITLE = "Title";
    private static final String MONEY_TYPE = "Type";
    private static final String MONEY_VALUE = "Value";
    private static final String MONEY_DATE = "Date";

    private static final String TABLE_SCHEDULE = "Schedule";
    private static final String SCHEDULE_USER_ID = "ScheduleUserId";
    private static final String SCHEDULE_ID = "ScheduleId";
    private static final String SCHEDULE_DAY = "Day";
    private static final String SCHEDULE_DAY_ID = "DayId";
    private static final String SCHEDULE_SUBJECT = "Subject";
    private static final String SCHEDULE_START = "Start";
    private static final String SCHEDULE_FINISH = "Finish";

    public DatabaseCP(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        CONTEXT = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_USER +
                "(" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USER_NAME + " TEXT NOT NULL," +
                USER_EMAIL + " TEXT NOT NULL UNIQUE," +
                USER_PASSWORD + " TEXT NOT NULL," +
                USER_NUMBER + " TEXT," +
                USER_BIRTH + " TEXT," +
                USER_GENDER + " TEXT," +
                USER_COUNTRY + " TEXT)";
        db.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS " + TABLE_ASSIGNMENT +
                "(" +
                ASSIGNMENT_USER_ID + " INTEGER," +
                ASSIGNMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ASSIGNMENT_TITLE + " TEXT NOT NULL," +
                ASSIGNMENT_DEADLINE + " DATE," +
                ASSIGNMENT_DESCRIPTION + " TEXT," +
                ASSIGNMENT_STATUS + " TEXT NOT NULL," +
                "CONSTRAINT assignment_id_foreign_key " +
                "FOREIGN KEY (AssignmentUserId) REFERENCES User(UserId))";
        db.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS " + TABLE_MONEY +
                "(" +
                MONEY_USER_ID + " INTEGER," +
                MONEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MONEY_TITLE + " TEXT NOT NULL," +
                MONEY_TYPE + " TEXT NOT NULL," +
                MONEY_VALUE + " INTEGER NOT NULL," +
                MONEY_DATE + " DATE NOT NULL," +
                "CONSTRAINT money_id_foreign_key " +
                "FOREIGN KEY (MoneyUserId) REFERENCES User(UserId))";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            String query = "DROP TABLE IF EXISTS " + TABLE_SCHEDULE;
            db.execSQL(query);

            query = "CREATE TABLE IF NOT EXISTS " + TABLE_SCHEDULE +
                    "(" +
                    SCHEDULE_USER_ID + " INTEGER," +
                    SCHEDULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SCHEDULE_DAY + " TEXT NOT NULL," +
                    SCHEDULE_DAY_ID + " INTEGER NOT NULL," +
                    SCHEDULE_SUBJECT + " TEXT NOT NULL," +
                    SCHEDULE_START + " TEXT NOT NULL," +
                    SCHEDULE_FINISH + " TEXT NOT NULL," +
                    "CONSTRAINT schedule_id_foreign_key " +
                    "FOREIGN KEY (ScheduleUserId) REFERENCES User(UserId))";
            db.execSQL(query);
        }
    }

    public void signUpAccount(String userName, String userEmail, String userPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(USER_NAME, userName);
        cv.put(USER_EMAIL, userEmail);
        cv.put(USER_PASSWORD, userPassword);

        long result = db.insert(TABLE_USER, null, cv);
        if (result == -1) {
            Toast.makeText(CONTEXT, "Gagal membuat akun", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(CONTEXT, "Akun berhasil dibuat, silakan masuk dengan akun tersebut", Toast.LENGTH_LONG).show();
        }
    }

    public boolean signInAccount(String userEmail, String userPassword) {
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + USER_EMAIL + " = ? AND " + USER_PASSWORD + " = ?";
        String[] dataUser = new String[]{userEmail, userPassword};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, dataUser);
        boolean result = false;

        if (cursor.moveToNext()) {
            result = true;
        } else {
            Toast.makeText(CONTEXT, "Email atau password pengguna salah", Toast.LENGTH_LONG).show();
        }

        cursor.close();
        return result;
    }

    public int getUserId(String userEmail) {
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + USER_EMAIL + " = ?";
        String[] dataUser = new String[]{userEmail};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, dataUser);

        int userId = 0;
        if (cursor.moveToNext()) {
            userId = cursor.getInt(0);
        }

        cursor.close();
        return userId;
    }

    public String getUserFullName(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + USER_ID + " = " + userId, null);

        String result = null;
        if (cursor.moveToNext()) {
            result = cursor.getString(1);
        }

        cursor.close();
        return result;
    }

    public String getUserEmail(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + USER_ID + " = " + userId, null);

        String result = null;
        if (cursor.moveToNext()) {
            result = cursor.getString(2);
        }

        cursor.close();
        return result;
    }

    public String getUserPassword(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + USER_ID + " = " + userId, null);

        String result = null;
        if (cursor.moveToNext()) {
            result = cursor.getString(3);
        }

        cursor.close();
        return result;
    }

    public String getUserNumber(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + USER_ID + " = " + userId, null);

        String result = null;
        if (cursor.moveToNext()) {
            result = cursor.getString(4);
        }

        cursor.close();
        return result;
    }

    public String getUserBirth(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + USER_ID + " = " + userId, null);

        String result = null;
        if (cursor.moveToNext()) {
            result = cursor.getString(5);
        }

        cursor.close();
        return result;
    }

    public void updateUserFullName(int userId, String fullName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_NAME, fullName);
        String whereClause = "UserId = ?";
        String[] whereArgs = { String.valueOf(userId)};

        int rowsAffected = db.update(TABLE_USER, contentValues, whereClause, whereArgs);

        db.close();

        if (rowsAffected > 0) {
            Toast.makeText(CONTEXT, "Nama pengguna berhasil diperbarui", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateUserPassword(int userId, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_PASSWORD, password);
        String whereClause = "UserId = ?";
        String[] whereArgs = { String.valueOf(userId)};

        int rowsAffected = db.update(TABLE_USER, contentValues, whereClause, whereArgs);

        db.close();

        if (rowsAffected > 0) {
            Toast.makeText(CONTEXT, "Password pengguna berhasil diperbarui", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateUserNumber(int userId, String number) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_NUMBER, number);
        String whereClause = "UserId = ?";
        String[] whereArgs = { String.valueOf(userId)};

        int rowsAffected = db.update(TABLE_USER, contentValues, whereClause, whereArgs);

        db.close();

        if (rowsAffected > 0) {
            Toast.makeText(CONTEXT, "Nomor telepon pengguna berhasil diperbarui", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateUserBirth(int userId, String birth) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_BIRTH, birth);
        String whereClause = "UserId = ?";
        String[] whereArgs = { String.valueOf(userId)};

        int rowsAffected = db.update(TABLE_USER, contentValues, whereClause, whereArgs);

        db.close();

        if (rowsAffected > 0) {
            Toast.makeText(CONTEXT, "Tanggal lahir pengguna berhasil diperbarui", Toast.LENGTH_SHORT).show();
        }
    }

    public void insertDataAssignment(int userId, String subject, String deadline, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ASSIGNMENT_USER_ID, userId);
        cv.put(ASSIGNMENT_TITLE, subject);
        cv.put(ASSIGNMENT_DEADLINE, deadline);
        cv.put(ASSIGNMENT_DESCRIPTION, description);
        cv.put(ASSIGNMENT_STATUS, "ACTIVE");
        long result = db.insert(TABLE_ASSIGNMENT, null, cv);
        if (result == -1) {
            Toast.makeText(CONTEXT, "Data tugas gagal ditambahkan", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(CONTEXT, "Data tugas berhasil ditambahkan", Toast.LENGTH_LONG).show();
        }
    }

    public List<AssignmentModel> selectDataAssignmentActive(int userId) {
        List<AssignmentModel> listAssignment = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Assignment WHERE " + ASSIGNMENT_USER_ID + " = ? AND " + ASSIGNMENT_STATUS + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), "ACTIVE"});

        while (cursor.moveToNext()) {
            int id = cursor.getInt(1);
            String title = cursor.getString(2);
            String deadline = cursor.getString(3);
            String description = cursor.getString(4);
            String status = cursor.getString(5);

            listAssignment.add(new AssignmentModel(id, title, deadline, description, status));
        }

        cursor.close();
        db.close();
        return listAssignment;
    }

    public List<AssignmentModel> selectDataAssignmentInactive(int userId) {
        List<AssignmentModel> listAssignment = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Assignment WHERE " + ASSIGNMENT_USER_ID + " = ? AND " + ASSIGNMENT_STATUS + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), "INACTIVE"});

        while (cursor.moveToNext()) {
            int id = cursor.getInt(1);
            String title = cursor.getString(2);
            String deadline = cursor.getString(3);
            String description = cursor.getString(4);
            String status = cursor.getString(5);

            listAssignment.add(new AssignmentModel(id, title, deadline, description, status));
        }

        cursor.close();
        db.close();
        return listAssignment;
    }

    public String getSubjectAssignment(int assignmentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Assignment WHERE " + ASSIGNMENT_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(assignmentId)});

        String result = null;
        if (cursor.moveToNext()) {
            result = cursor.getString(2);
        }

        cursor.close();
        return result;
    }

    public String getDeadlineAssignment(int assignmentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Assignment WHERE " + ASSIGNMENT_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(assignmentId)});

        String result = null;
        if (cursor.moveToNext()) {
            result = cursor.getString(3);
        }

        cursor.close();
        return result;
    }

    public String getDescriptionAssignment(int assignmentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Assignment WHERE " + ASSIGNMENT_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(assignmentId)});

        String result = null;
        if (cursor.moveToNext()) {
            result = cursor.getString(4);
        }

        cursor.close();
        return result;
    }

    public void updateAssignment(int userId, int id, String subject, String deadline, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ASSIGNMENT_TITLE, subject);
        contentValues.put(ASSIGNMENT_DEADLINE, deadline);
        contentValues.put(ASSIGNMENT_DESCRIPTION, description);
        String whereClause = "AssignmentUserId = ? AND AssignmentId = ?";
        String[] whereArgs = { String.valueOf(userId), String.valueOf(id) };

        int rowsAffected = db.update(TABLE_ASSIGNMENT, contentValues, whereClause, whereArgs);

        db.close();

        if (rowsAffected > 0) {
            Toast.makeText(CONTEXT, "Tugas berhasil diperbarui", Toast.LENGTH_LONG).show();
        }
    }

    public void updateStatusAssignment(int userId, int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ASSIGNMENT_STATUS, status);
        String whereClause = "AssignmentUserId = ? AND AssignmentId = ?";
        String[] whereArgs = { String.valueOf(userId), String.valueOf(id) };

        db.update(TABLE_ASSIGNMENT, contentValues, whereClause, whereArgs);
        db.close();
    }

    public void deleteAssignment(int userId, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = "AssignmentUserId = ? AND AssignmentId = ?";
        String[] whereArgs = { String.valueOf(userId), String.valueOf(id) };

        int rowsAffected = db.delete(TABLE_ASSIGNMENT, whereClause, whereArgs);

        db.close();

        if (rowsAffected > 0) {
            Toast.makeText(CONTEXT, "Data tugas berhasil dihapus", Toast.LENGTH_LONG).show();
        }
    }

    public void insertDataMoney(int userId, String title, String date, int amount, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(MONEY_USER_ID, userId);
        cv.put(MONEY_TITLE, title);
        cv.put(MONEY_DATE, date);
        cv.put(MONEY_VALUE, amount);
        cv.put(MONEY_TYPE, type);
        long result = db.insert(TABLE_MONEY, null, cv);
        if (result == -1) {
            Toast.makeText(CONTEXT, "Data keuangan gagal ditambahkan", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(CONTEXT, "Data keuangan berhasil ditambahkan", Toast.LENGTH_LONG).show();
        }
    }

    public List<MoneyModel> selectDataIncomeOutcome(int userId) {
        List<MoneyModel> listMoney = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] column = {MONEY_ID, MONEY_TITLE, MONEY_TYPE, MONEY_VALUE, MONEY_DATE};
        String whereCondition = "MoneyUserId = ?";
        String[] whereClause = {String.valueOf(userId)};
        String sortOrder = "Date DESC";

        Cursor cursor = db.query(TABLE_MONEY, column, whereCondition, whereClause, null, null, sortOrder);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String type = cursor.getString(2);
            int amount = cursor.getInt(3);
            String date = cursor.getString(4);

            listMoney.add(new MoneyModel(id, title, date, amount, type));
        }

        cursor.close();
        db.close();
        return listMoney;
    }

    public List<MoneyModel> selectDataIncome(int userId) {
        List<MoneyModel> listMoney = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] column = {MONEY_ID, MONEY_TITLE, MONEY_TYPE, MONEY_VALUE, MONEY_DATE};
        String whereCondition = "MoneyUserId = ? AND Type = ?";
        String[] whereClause = {String.valueOf(userId), "Pemasukan"};
        String sortOrder = "Date DESC";

        Cursor cursor = db.query(TABLE_MONEY, column, whereCondition, whereClause, null, null, sortOrder);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String type = cursor.getString(2);
            int amount = cursor.getInt(3);
            String date = cursor.getString(4);

            listMoney.add(new MoneyModel(id, title, date, amount, type));
        }

        cursor.close();
        db.close();
        return listMoney;
    }

    public List<MoneyModel> selectDataOutcome(int userId) {
        List<MoneyModel> listMoney = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] column = {MONEY_ID, MONEY_TITLE, MONEY_TYPE, MONEY_VALUE, MONEY_DATE};
        String whereCondition = "MoneyUserId = ? AND Type = ?";
        String[] whereClause = {String.valueOf(userId), "Pengeluaran"};
        String sortOrder = "Date DESC";

        Cursor cursor = db.query(TABLE_MONEY, column, whereCondition, whereClause, null, null, sortOrder);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String type = cursor.getString(2);
            int amount = cursor.getInt(3);
            String date = cursor.getString(4);

            listMoney.add(new MoneyModel(id, title, date, amount, type));
        }

        cursor.close();
        db.close();
        return listMoney;
    }

    public int remainingMoney(int userId) {
        int result = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MONEY + " WHERE " + MONEY_USER_ID + " = ?", new String[] {String.valueOf(userId)});

        while (cursor.moveToNext()) {
            if (Objects.equals(cursor.getString(3), "Pemasukan")) {
                result += cursor.getInt(4);
            } else {
                result -= cursor.getInt(4);
            }
        }

        cursor.close();
        db.close();
        return result;
    }

    public int totalOutcomeMoney(int userId) {
        int result = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MONEY + " WHERE " + MONEY_USER_ID + " = ?", new String[] {String.valueOf(userId)});

        while (cursor.moveToNext()) {
            if (Objects.equals(cursor.getString(3), "Pengeluaran")) {
                result += cursor.getInt(4);
            } else {
                result += 0;
            }
        }

        cursor.close();
        db.close();
        return result;
    }

    public int totalIncomeMoney(int userId) {
        int result = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MONEY + " WHERE " + MONEY_USER_ID + " = ?", new String[] {String.valueOf(userId)});

        while (cursor.moveToNext()) {
            if (Objects.equals(cursor.getString(3), "Pemasukan")) {
                result += cursor.getInt(4);
            } else {
                result += 0;
            }
        }

        cursor.close();
        db.close();
        return result;
    }

    public String getTitleMoney(int moneyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM MoneyManagement WHERE " + MONEY_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(moneyId)});

        String result = null;
        if (cursor.moveToNext()) {
            result = cursor.getString(2);
        }

        cursor.close();
        return result;
    }

    public String getDateMoney(int moneyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM MoneyManagement WHERE " + MONEY_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(moneyId)});

        String result = null;
        if (cursor.moveToNext()) {
            result = cursor.getString(5);
        }

        cursor.close();
        return result;
    }

    public int getAmountMoney(int moneyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM MoneyManagement WHERE " + MONEY_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(moneyId)});

        int result = 0;
        if (cursor.moveToNext()) {
            result = cursor.getInt(4);
        }

        cursor.close();
        return result;
    }

    public String getTypeMoney(int moneyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM MoneyManagement WHERE " + MONEY_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(moneyId)});

        String result = null;
        if (cursor.moveToNext()) {
            result = cursor.getString(3);
        }

        cursor.close();
        return result;
    }

    public void updateMoney(int userId, int id, String title, String date, int amount, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(MONEY_TITLE, title);
        contentValues.put(MONEY_DATE, date);
        contentValues.put(MONEY_VALUE, amount);
        contentValues.put(MONEY_TYPE, type);
        String whereClause = "MoneyUserId = ? AND MoneyId = ?";
        String[] whereArgs = { String.valueOf(userId), String.valueOf(id) };

        int rowsAffected = db.update(TABLE_MONEY, contentValues, whereClause, whereArgs);

        db.close();

        if (rowsAffected > 0) {
            Toast.makeText(CONTEXT, "Data transaksi berhasil diperbarui", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteMoney(int userId, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = "MoneyUserId = ? AND MoneyId = ?";
        String[] whereArgs = { String.valueOf(userId), String.valueOf(id) };

        int rowsAffected = db.delete(TABLE_MONEY, whereClause, whereArgs);

        db.close();

        if (rowsAffected > 0) {
            Toast.makeText(CONTEXT, "Data transaksi berhasil dihapus", Toast.LENGTH_LONG).show();
        }
    }

    public int getDayId(String day) {
        int result = 0;

        switch (day) {
            case "Senin" :
                result = 1;
                break;
            case "Selasa" :
                result = 2;
                break;
            case "Rabu" :
                result = 3;
                break;
            case "Kamis" :
                result = 4;
                break;
            case "Jumat" :
                result = 5;
                break;
            case "Sabtu" :
                result = 6;
                break;
            case "Minggu" :
                result = 7;
                break;
        }

        return result;
    }

    public void insertDataSchedule(int userId, String day, int dayId, String subject, String startTime, String finishTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(SCHEDULE_USER_ID, userId);
        cv.put(SCHEDULE_DAY, day);
        cv.put(SCHEDULE_DAY_ID, dayId);
        cv.put(SCHEDULE_SUBJECT, subject);
        cv.put(SCHEDULE_START, startTime);
        cv.put(SCHEDULE_FINISH, finishTime);
        long result = db.insert(TABLE_SCHEDULE, null, cv);
        if (result == -1) {
            Toast.makeText(CONTEXT, "Data jadwal gagal ditambahkan", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(CONTEXT, "Data jadwal berhasil ditambahkan", Toast.LENGTH_LONG).show();
        }
    }

    public List<ScheduleModel> selectAllDataSchedule(int userId) {
        List<ScheduleModel> listSchedule = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] column = {SCHEDULE_ID, SCHEDULE_DAY, SCHEDULE_SUBJECT, SCHEDULE_START, SCHEDULE_FINISH};
        String whereCondition = "ScheduleUserId = ?";
        String[] whereClause = {String.valueOf(userId)};
        String sortOrder = "DayId ASC, Start ASC";

        Cursor cursor = db.query(TABLE_SCHEDULE, column, whereCondition, whereClause, null, null, sortOrder);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String day = cursor.getString(1);
            String subject = cursor.getString(2);
            String start = cursor.getString(3);
            String finish = cursor.getString(4);

            listSchedule.add(new ScheduleModel(id, day, subject, start, finish));
        }

        cursor.close();
        db.close();
        return listSchedule;
    }

    public List<ScheduleModel> selectDataScheduleByDay(int userId, int dayId) {
        List<ScheduleModel> listSchedule = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] column = {SCHEDULE_ID, SCHEDULE_DAY, SCHEDULE_SUBJECT, SCHEDULE_START, SCHEDULE_FINISH};
        String whereCondition = "ScheduleUserId = ? AND DayId = ?";
        String[] whereClause = {String.valueOf(userId), String.valueOf(dayId)};
        String sortOrder = "DayId ASC, Start ASC";

        Cursor cursor = db.query(TABLE_SCHEDULE, column, whereCondition, whereClause, null, null, sortOrder);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String day = cursor.getString(1);
            String subject = cursor.getString(2);
            String start = cursor.getString(3);
            String finish = cursor.getString(4);

            listSchedule.add(new ScheduleModel(id, day, subject, start, finish));
        }

        cursor.close();
        db.close();
        return listSchedule;
    }

    public String getDaySchedule(int scheduleId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Schedule WHERE " + SCHEDULE_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(scheduleId)});

        String result = null;
        if (cursor.moveToNext()) {
            result = cursor.getString(2);
        }

        cursor.close();
        return result;
    }

    public String getSubjectSchedule(int scheduleId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Schedule WHERE " + SCHEDULE_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(scheduleId)});

        String result = null;
        if (cursor.moveToNext()) {
            result = cursor.getString(4);
        }

        cursor.close();
        return result;
    }

    public String getStartTimeSchedule(int scheduleId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Schedule WHERE " + SCHEDULE_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(scheduleId)});

        String result = null;
        if (cursor.moveToNext()) {
            result = cursor.getString(5);
        }

        cursor.close();
        return result;
    }

    public String getFinishTimeSchedule(int scheduleId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Schedule WHERE " + SCHEDULE_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(scheduleId)});

        String result = null;
        if (cursor.moveToNext()) {
            result = cursor.getString(6);
        }

        cursor.close();
        return result;
    }

    public void updateSchedule(int userId, int id, String day, int dayId, String subject, String startTime, String finishTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SCHEDULE_DAY, day);
        contentValues.put(SCHEDULE_DAY_ID, dayId);
        contentValues.put(SCHEDULE_SUBJECT, subject);
        contentValues.put(SCHEDULE_START, startTime);
        contentValues.put(SCHEDULE_FINISH, finishTime);
        String whereClause = "ScheduleUserId = ? AND ScheduleId = ?";
        String[] whereArgs = { String.valueOf(userId), String.valueOf(id) };

        int rowsAffected = db.update(TABLE_SCHEDULE, contentValues, whereClause, whereArgs);

        db.close();

        if (rowsAffected > 0) {
            Toast.makeText(CONTEXT, "Jadwal berhasil diperbarui", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteSchedule(int userId, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = "ScheduleUserId = ? AND ScheduleId = ?";
        String[] whereArgs = { String.valueOf(userId), String.valueOf(id) };

        int rowsAffected = db.delete(TABLE_SCHEDULE, whereClause, whereArgs);

        db.close();

        if (rowsAffected > 0) {
            Toast.makeText(CONTEXT, "Jadwal berhasil dihapus", Toast.LENGTH_LONG).show();
        }
    }
}