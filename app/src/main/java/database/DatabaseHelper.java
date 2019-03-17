
package database;

import android.util.Log;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.abhar.android.studentmanagementsqlite.database.model.Student;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper sInstance = null;

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "student_db";

    public static synchronized DatabaseHelper getInstance(Context context)
    {
        if(sInstance == null)
        {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(Student.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Student.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertStudent(int rollNo, String studentName) {
        Log.i("completed","yes");
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Student.COLUMN_ROLL_NUMBER, rollNo);
        values.put(Student.COLUMN_NAME, studentName);


        // insert row
        long id = db.insert(Student.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Student getStudent(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Student.TABLE_NAME,
                new String[]{Student.COLUMN_ROLL_NUMBER, Student.COLUMN_NAME},
                Student.COLUMN_ROLL_NUMBER + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        // prepare note object
        Student student = new Student(
                cursor.getInt(cursor.getColumnIndex(Student.COLUMN_ROLL_NUMBER)),
                cursor.getString(cursor.getColumnIndex(Student.COLUMN_NAME)));

        // close the db connection
        cursor.close();

        return student;
    }

    public int getStudentCount() {
        String countQuery = "SELECT  * FROM " + Student.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public long updateStudent(int oldroll, long rollNum, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Student.COLUMN_ROLL_NUMBER, rollNum);
        values.put(Student.COLUMN_NAME, name);


        // updating row
        db.update(Student.TABLE_NAME, values, Student.COLUMN_ROLL_NUMBER + " = ?",
                new String[]{String.valueOf(oldroll)});
        return rollNum;
    }

    public void deleteNote(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Student.TABLE_NAME, Student.COLUMN_ROLL_NUMBER + " = ?",
                new String[]{String.valueOf(student.getRollNo())});
        db.close();
    }

    public ArrayList<Student> getAllStudents()
    {
        ArrayList<Student> studentList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Student.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst())
        {
            do
            {
                Student student = new Student();
                student.setName(cursor.getString(cursor.getColumnIndex(Student.COLUMN_NAME)));
                student.setRollNum(cursor.getInt(cursor.getColumnIndex(Student.COLUMN_ROLL_NUMBER)));
                studentList.add(student);
            }
            while(cursor.moveToNext());
        }
        db.close();
        return studentList;
    }
    public boolean isExisting(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Student.TABLE_NAME,
                new String[]{Student.COLUMN_ROLL_NUMBER, Student.COLUMN_NAME},
                Student.COLUMN_ROLL_NUMBER + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);


        if(cursor.getCount() <= 0)
        {
            return false;
        }
        return true;
    }


}

