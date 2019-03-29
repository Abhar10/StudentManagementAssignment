package async;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import android.util.Log;

import constant.Constant;
import database.DatabaseHelper;
import com.abhar.android.studentmanagementsqlite.database.model.Student;

public class BackProcess extends AsyncTask<String,Void, String>
{

    Context ctx;
    CallbackFor callbackFor;
    public BackProcess(Context ctx,CallbackFor callbackFor)
    {
        this.callbackFor=callbackFor;
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        String method = params[0];
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(ctx);
        //If Insertion Taking Place
        if(isCancelled())
        {
            return null;
        }
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

        return method;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String aLong) {
        callbackFor.getCall(aLong);
       // Toast.makeText(ctx,aLong,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCancelled(String aVoid) {
        if(isCancelled())
        {
            cancel(true);
        }
        super.onCancelled(aVoid);
    }

    public interface CallbackFor{

       void getCall(String x);

    }
}
