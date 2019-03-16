package async;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import android.util.Log;

import constant.Constant;
import database.DatabaseHelper;
import com.abhar.android.studentmanagementsqlite.database.model.Student;

public class BackProcess extends AsyncTask<String,Void, Void>
{

    Context ctx;
    CallbackFor callbackFor;
    public BackProcess(Context ctx,CallbackFor callbackFor)
    {
        this.callbackFor=callbackFor;
        this.ctx = ctx;
    }
    public BackProcess(Context ctx)
    {
        this.ctx = ctx;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... params) {

        String method = params[0];
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(ctx);
        //If Insertion Taking Place
        if(method.equals(Constant.addStudent))
        {
            int roll_no =Integer.parseInt(params[1]);
            String name = params[2];

            long id = databaseHelper.insertStudent(roll_no,name);


        }
        //If Update Taking Place
        else if(method.equals(Constant.updateStudent))
        {
            int oldRollNo = Integer.parseInt(params[3]);
            long roll_no =Long.parseLong(params[1]);
            String name = params[2];
            databaseHelper.updateStudent(oldRollNo,roll_no,name);

        }
        //If Delete Taking Place
        else if(method.equals(Constant.deleteStudent))
        {
            int rollNum = Integer.parseInt(params[1]);
            String name = params[2];
            Student student = new Student(rollNum,name);
            databaseHelper.deleteNote(student);
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aLong) {
        Toast.makeText(ctx,"Async Running",Toast.LENGTH_LONG).show();
    }

    public interface CallbackFor{

        void getCall(Long x);

    }
}
