package activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextWatcher;
import android.text.Editable;
import com.abhar.sms.R;
import validate.validateId;
import validate.validateName;

/**
 * This class helps in performing several actions such as adding a new student,
 * viewing a student details and for editing student details.
 */

public class AddStudentActivity extends AppCompatActivity {
    private EditText mEtName;
    private EditText mEtRollNumber;
    private Button mBtnSaveChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_add);
        setTitle(R.string.second_activity_title);

        initialize();
        String Mode = getIntent().getStringExtra("Mode");
        createTextWatcher(mEtName,Mode);
    }

    /**
     * Method to check whether entered name and ID are not empty and
     * do not consists of only white spaces.
     */
    private void validateNameAndId()
    {
        mBtnSaveChange.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                if(validateName.isEmptyName(mEtName.getText().toString()))
                {
                    mEtName.setError(getString(R.string.valid_name_error));
                }
                if(validateId.isEmptyId(mEtRollNumber.getText().toString())) {
                    mEtRollNumber.setError(getString(R.string.valid_id_error));
                }
                else if(! validateName.isEmptyName(mEtRollNumber.getText().toString()) &&
                        ! validateId.isEmptyId(mEtRollNumber.getText().toString()))
                {
                    createIntent();
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
        mEtRollNumber.setText(getIntent().getStringExtra("ID"));
        mBtnSaveChange.setText(getString(R.string.save_changes_button));

        validateNameAndId();
    }

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
    private void createIntent()
    {
        Intent intent = new Intent();
        intent.putExtra("key_name", mEtName.getText().toString());
        intent.putExtra("key_id", mEtRollNumber.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
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

    }

