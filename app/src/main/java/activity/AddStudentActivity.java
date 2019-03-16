package activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextWatcher;
import android.text.Editable;

import background.BackgroundService;
import com.abhar.sms.R;
import constant.Constant;

import async.BackProcess;


import background.BackgroundIntent;
import database.DatabaseHelper;
import validate.validateId;
import validate.validateName;

/**
 * This class helps in performing several actions such as adding a new student,
 * viewing a student details and for editing student details.
 */

public class AddStudentActivity extends AppCompatActivity implements BackProcess.CallbackFor {
    private EditText mEtName;
    private EditText mEtRollNumber;
    private Button mBtnSaveChange;
    private  Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_add);
        //  BackGroundTask backgroundTask=new
        setTitle(R.string.second_activity_title);
        initialize();
        String Mode = getIntent().getStringExtra(Constant.Mode);
        createTextWatcher(mEtName,Mode);
    }

    /**
     * Method to check whether entered name and ID are not empty and
     * do not consists of only white spaces.
     */
    private void validateNameAndId()
    {
        mBtnSaveChange.setOnClickListener(new View.OnClickListener() {
            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(AddStudentActivity.this);
            @Override
            public void onClick(View v)
            {
                if(databaseHelper.isExisting(Integer.parseInt(mEtRollNumber.getText().toString())) == true)
                {
                    mEtRollNumber.setError("Enter A Unique Roll Number");
                }
                if(validateName.isEmptyName(mEtName.getText().toString()))
                {
                    mEtName.setError(getString(R.string.valid_name_error));
                }
                if(validateId.isEmptyId(mEtRollNumber.getText().toString())) {
                    mEtRollNumber.setError(getString(R.string.valid_id_error));
                }
                else if(! validateName.isEmptyName(mEtRollNumber.getText().toString()) &&
                        ! validateId.isEmptyId(mEtRollNumber.getText().toString()) &&
                ! databaseHelper.isExisting(Integer.parseInt(mEtRollNumber.getText().toString())))
                {
                    int roll = Integer.parseInt(mEtRollNumber.getText().toString());
                    String name = mEtName.getText().toString();

                    Log.i("abc",String.valueOf(roll));
                    createIntent((long)roll);
                    generateDialog(Constant.addStudent,String.valueOf(roll),name," ");



                }}}
        );

    }

    /**
     * Method when main activity requests for Viewing a student
     */
    private void onViewStudent()
    {
        setTitle(R.string.view_string);
        mEtName.setText(getIntent().getStringExtra("Name"));
        mEtRollNumber.setText(getIntent().getStringExtra("ID"));
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
        mEtName.setText(getIntent().getStringExtra("Name"));
        mEtRollNumber.setText(String.valueOf(getIntent().getIntExtra("ID",0)));
        mBtnSaveChange.setText(getString(R.string.save_changes_button));
       // final String oldName = getIntent().getStringExtra("Name");
        final int oldRollNum = getIntent().getIntExtra("ID",0);
        //validateNameAndId();

        mBtnSaveChange.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(AddStudentActivity.this);
                if(databaseHelper.isExisting(Integer.parseInt(mEtRollNumber.getText().toString())))
                {
                    mEtRollNumber.setError("Enter A Unique Roll Number");
                }
                if(validateName.isEmptyName(mEtName.getText().toString()))
                {
                    mEtName.setError(getString(R.string.valid_name_error));
                }
                if(validateId.isEmptyId(mEtRollNumber.getText().toString())) {
                    mEtRollNumber.setError(getString(R.string.valid_id_error));
                }
                else if(! validateName.isEmptyName(mEtRollNumber.getText().toString()) &&
                        ! validateId.isEmptyId(mEtRollNumber.getText().toString()) &&
                ! databaseHelper.isExisting(Integer.parseInt(mEtRollNumber.getText().toString()))) {

                    long roll = Long.parseLong(mEtRollNumber.getText().toString());
                    String name = mEtName.getText().toString();

                    createIntent(roll);
                    Log.i("abc", String.valueOf(id));
                    generateDialog(Constant.updateStudent,
                            String.valueOf(roll), name, String.valueOf(oldRollNum));
                }
            }
        });}

    /**
     * Method to check whether Entered Name and ID are valid
     */
    private void afterTextChangedValidation()
    {
        if (! validateName.isvalidUserName(mEtName.getText().toString())) {
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
        intent.putExtra("key_id", id);
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

        if(Mode != null && Mode.equals("View"))
        {
            onViewStudent();
        }

        else if(Mode != null && Mode.equals("Edit"))
        {
            onEditStudent();
        }

        else if(Mode == null)
        {
            validateNameAndId();
        }
    }

    @Override
    public void getCall(Long x) {
        id=x;
        Log.i("awesrtyui",String.valueOf(id));
    }
    public void generateDialog(final String operation,
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
                        finish();
                        finish();
                        break;

                    case 2:
                         Intent intentStudent = new Intent(AddStudentActivity.this,
                                 BackgroundIntent.class);
                         intentStudent.putExtra(Constant.Mode,operation);
                         intentStudent.putExtra(Constant.Name,name);
                         intentStudent.putExtra(Constant.RollNo,roll);
                         intentStudent.putExtra(Constant.oldRollNo,oldRoll);
                         startService(intentStudent);
                         finish();
                        break;

                    default:
                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

