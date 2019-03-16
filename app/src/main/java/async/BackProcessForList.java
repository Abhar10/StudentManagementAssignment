package async;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import com.abhar.android.studentmanagementsqlite.database.model.Student;

import database.DatabaseHelper;

public class BackProcessForList extends AsyncTask<String,Void, ArrayList<Student>> {

    Context ctx;
    Callback callback;
    public BackProcessForList(Context ctx,Callback callback)
    {
        this.callback=callback;
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Student> doInBackground(String... params) {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(ctx);
        return databaseHelper.getAllStudents();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(ArrayList<Student> out) {
        super.onPostExecute(out);
        callback.getOutput(out);

    }
    public interface Callback
    {
        void getOutput(ArrayList<Student>out);
    }
}

