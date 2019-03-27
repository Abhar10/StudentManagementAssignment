package background;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.abhar.sms.R;

import constant.Constant;
import database.DatabaseHelper;

import static constant.Constant.FILTER_ACTION_KEY;

public class BackgroundService extends Service {
    public BackgroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(BackgroundService.this);


        if(intent.getStringExtra(Constant.Mode).equals(Constant.addStudent)){
            databaseHelper.insertStudent(Integer.parseInt(intent.getStringExtra(Constant.RollNo)),
                    intent.getStringExtra(Constant.Name));
            Log.i("Hello","Hello");
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
        Toast.makeText(this,getString(R.string.msg_service),Toast.LENGTH_LONG).show();
        stopSelf();
        return START_NOT_STICKY;
    }
}
