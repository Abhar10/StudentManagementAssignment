package com.abhar.android.studentmanagementsqlite.database.model;

public class Student {

    public static final String TABLE_NAME = "Student";
    public static final String COLUMN_ROLL_NUMBER = "ROLL_NO";
    public static final String COLUMN_NAME = "name";

    private int roll_no;
    private String name;
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ROLL_NUMBER + " INTEGER PRIMARY KEY,"
            + COLUMN_NAME + " TEXT)";




    public Student()
    {
    }

    public Student(int roll_no, String name)
    {
        this.roll_no = roll_no;
        this.name = name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    public void setRollNum(int roll_no)
    {
        this.roll_no = roll_no;
    }

    public int getRollNo()
    {
        return roll_no;
    }
    public String getName()
    {
        return name;
    }
}

