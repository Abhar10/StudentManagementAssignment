package activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.Toast;

import background.BackgroundService;
import com.abhar.sms.R;
import constant.Constant;

import async.BackProcess;


import background.BackgroundIntent;
import database.DatabaseHelper;
import validate.ValidateId;
import validate.ValidateName;

/**
 * This class helps in performing several actions such as adding a new student,
 * viewing a student details and for editing student details.
 */

public class AddStudentActivity extends AppCompatActivity implements BackProcess.CallbackFor {
    private EditText mEtName;
    private EditText mEtRollNumber;
    private Button mBtnSaveChange;
    private  Long id;
    private StudentBroadcastReciever mStudentBroadcastReciever = new StudentBroadcastReciever();

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(getString(R.string.broadcast));
        LocalBroadcastManager.getInstance(this).registerReceiver(mStudentBroadcastReciever,intentFilter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_add);
        setTitle(R.string.second_activity_title);
        initialize();
        String Mode = getIntent().getStringExtra(Constant.Mode);
        createTextWatcher(mEtName,Mode);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mStudentBroadcastReciever);
    }

    /**
     * Method to check whether entered name and ID are not empty and
     * do not consists of only white spaces.
     */
    private void addStudent()
    {
        mBtnSaveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                boolean flag = isNameIdValid();
                if(flag)
                {
                    int roll = Integer.parseInt(mEtRollNumber.getText().toString());
                    String name = mEtName.getText().toString();
                    createIntent((long)roll);
                    generateServiceDialog(Constant.addStudent,String.valueOf(roll),name," ");

                }}}
        );

    }

    /**
     * Method when main activity requests for Viewing a student
     */
    private void onViewStudent()
    {
        setTitle(R.string.view_string);
        mEtName.setText(getIntent().getStringExtra(Constant.Name));
        mEtRollNumber.setText(getIntent().getStringExtra(Constant.Id));
        mEtRollNumber.setEnabled(false);
        mEtName.setEnabled(false);
        mBtnSaveChange.setVisibility(View.GONE);
    }

    /**
     * Method to Edit a student when user requests for it
     */
    private void onEditStudent()
    {
        setTitle(R.string.edit_string);
        mEtName.setText(getIntent().getStringExtra(Constant.Name));
        mEtRollNumber.setText(String.valueOf(getIntent().getIntExtra(Constant.Id,0)));
        mBtnSaveChange.setText(getString(R.string.save_changes_button));
        final int oldRollNum = getIntent().getIntExtra(Constant.Id,0);

        mBtnSaveChange.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if((Integer.parseInt(mEtRollNumber.getText().toString()) == oldRollNum)
                            && ! ValidateName.isEmptyName(mEtName.getText().toString()))
                {

                    long roll = Long.parseLong(mEtRollNumber.getText().toString());
                    String name = mEtName.getText().toString();
                    createIntent(roll);
                    generateServiceDialog(Constant.updateStudent,
                            String.valueOf(roll), name, String.valueOf(oldRollNum));
                }
                else
                {
                    boolean flag = isNameIdValid();
                    if(flag)
                    {
                    long roll = Long.parseLong(mEtRollNumber.getText().toString());
                    String name = mEtName.getText().toString();
                    createIntent(roll);
                    generateServiceDialog(Constant.updateStudent,
                            String.valueOf(roll), name, String.valueOf(oldRollNum));
                    }}
            }
        });}

    /**
     * Method to check whether Entered Name and ID are valid
     */
    private void afterTextChangedValidation()
    {
        if (! ValidateName.isvalidUserName(mEtName.getText().toString())) {
            mEtName.setError(getString(R.string.error_msg));
            mBtnSaveChange.setEnabled(false);
        }
        else
        {
            mEtName.setError(null);
            mBtnSaveChange.setEnabled(true);
        }
    }

    /**
     * Method to create Intent
     */
    private void createIntent(long id)
    {
        Intent intent = new Intent();
        intent.putExtra(Constant.keyId, id);
        setResult(RESULT_OK, intent);

    }

    /**
     * Initialize variables
     */
    private void initialize()
    {
        mEtName = findViewById(R.id.et_user_name);
        mEtRollNumber = findViewById(R.id.et_user_roll_no);
        mBtnSaveChange = findViewById(R.id.btn_save_changes);
    }

    /**
     * Method to check whether user is typing valid text
     * @param mEtName EditText for Name
     * @param Mode String for Mode(Edit,View,Delete,Default)
     */
    private void createTextWatcher(EditText mEtName, String Mode)
    {
        mEtName.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                afterTextChangedValidation();
            }
        });

        if(Mode != null && Mode.equals(Constant.view))
        {
            onViewStudent();
        }

        else if(Mode != null && Mode.equals(Constant.edit))
        {
            onEditStudent();
        }

        else if(Mode == null)
        {
            addStudent();
        }
    }

    /**
     * Method to generate dialog to choose one of three options Async Task, Services
     * and Intent Services
     * @param operation Add or Edit
     * @param roll Roll Number of Student
     * @param name Name of Student
     * @param oldRoll The Old Roll Number of the Student
     */
    public void generateServiceDialog(final String operation,
                                      final String roll ,final String name,final String oldRoll)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder
                (AddStudentActivity.this);
        builder.setTitle(R.string.chooseOptionText);
        String[] choice = {getString(R.string.async_task),
                getString(R.string.service),
                getString(R.string.intent_service)};
        builder.setItems(choice, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which) {
                    case 0:
                        (new BackProcess(AddStudentActivity.this))
                                .execute(operation, roll, name,oldRoll);
                        finish();
                        break;

                    case 1:
                        Intent intent = new Intent(AddStudentActivity.this,
                                BackgroundService.class);
                        intent.putExtra(Constant.Mode,operation);
                        intent.putExtra(Constant.Name,name);
                        intent.putExtra(Constant.RollNo,roll);
                        intent.putExtra(Constant.oldRollNo,oldRoll);
                        startService(intent);
                        break;

                    case 2:
                        Intent intentStudent = new Intent(AddStudentActivity.this,
                                BackgroundIntent.class);
                        intentStudent.putExtra(Constant.Mode,operation);
                        intentStudent.putExtra(Constant.Name,name);
                        intentStudent.putExtra(Constant.RollNo,roll);
                        intentStudent.putExtra(Constant.oldRollNo,oldRoll);
                        startService(intentStudent);
                        break;

                    default:
                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Method to check whether Name and Roll Number Valid or Not
     * @return true or false
     */
    public boolean isNameIdValid()
    {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(AddStudentActivity.this);
        boolean flag = true;
        if(ValidateName.isEmptyName(mEtName.getText().toString()))
        {
            flag = false;
            mEtName.setError(getString(R.string.valid_name_error));
        }
        if(ValidateId.isEmptyId(mEtRollNumber.getText().toString())) {

            flag = false;
            mEtRollNumber.setError(getString(R.string.valid_id_error));
        }
        else if(databaseHelper.isExisting(Integer.parseInt(mEtRollNumber.getText().toString())))
        {
            flag = false;
            mEtRollNumber.setError(getString(R.string.unique_roll_msg));
        }
        return flag;
    }

    @Override
    public void getCall(Long x) {
        id=x;
    }
    /**
     * Broadcast Reciever
     */
    public class StudentBroadcastReciever extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
            Toast.makeText(AddStudentActivity.this,getString(R.string.broadcast_msg),
                    Toast.LENGTH_LONG).show();
        }
    }
}

