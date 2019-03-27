package background;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;
import com.abhar.sms.R;
import constant.Constant;
import database.DatabaseHelper;

import static constant.Constant.FILTER_ACTION_KEY;

public class BackgroundIntent extends IntentService {
    public BackgroundIntent(){
        super("BackgroundIntent");

    }



    @Override
    public void onHandleIntent(Intent intent)
    {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(BackgroundIntent.this);


        if(intent.getStringExtra(Constant.Mode).equals(Constant.addStudent)){
            databaseHelper.insertStudent(Integer.parseInt(intent.getStringExtra(Constant.RollNo)),
                    intent.getStringExtra(Constant.Name));

        }
        else if(intent.getStringExtra(Constant.Mode).equals(Constant.updateStudent))
        {
            databaseHelper.updateStudent(Integer.parseInt(intent.getStringExtra(Constant.oldRollNo))
                    ,Long.parseLong(intent.getStringExtra(Constant.RollNo)),
                    intent.getStringExtra(Constant.Name));
        }
        intent.setAction(FILTER_ACTION_KEY);
        String echoMessage = getString(R.string.broadcastReciever) ;
        LocalBroadcastManager.getInstance(getApplicationContext()).
                sendBroadcast(intent.putExtra(Constant.broadcastMsg, echoMessage));
        Toast.makeText(this,getString(R.string.service_msg),Toast.LENGTH_LONG).show();
    }

}
