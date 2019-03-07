package Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextWatcher;
import android.text.Editable;

import com.abhar.sms.R;

import javax.xml.validation.Validator;

import Validate.validateId;
import Validate.validateName;

/**
 * This class helps in performing several actions such as adding a new student,
 * viewing a student details and for editing student details.
 */

public class AddStudentActivity extends AppCompatActivity {
    private EditText mEtName;
    private EditText mEtRollNumber;
    private Button mBtnSaveChange;

    /**
     * Overriden method to create a new Activity AddStudentActivity
     * @param savedInstanceState The previous state in which the activity was.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_add);
        setTitle(R.string.second_activity_title);

        mEtName = (EditText)findViewById(R.id.et_user_name);

        mEtRollNumber = (EditText)findViewById(R.id.et_user_roll_no);

        String Mode = (String)getIntent().getStringExtra("Mode");
        mBtnSaveChange = (Button)findViewById(R.id.btn_save_changes);

        //For checking validation while user types the name
        mEtName.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            /**
             * This method checks the validation in name while user is typing the name
             * @param s The EditText text currently typed
             */
            @Override
            public void afterTextChanged(Editable s)
            {
                if (! validateName.isvalidUserName(mEtName.getText().toString())) {
                    mEtName.setError("Enter a Valid Name");
                    mBtnSaveChange.setEnabled(false);
                }
                else
                    {
                    mEtName.setError(null);
                    mBtnSaveChange.setEnabled(true);
                }
            }
        });

        //Checks if mode is equal to View
        if(Mode != null && Mode.equals("View"))
        {
            onViewStudent();
        }

        //Checks if mode is equal to Edit
        else if(Mode != null && Mode.equals("Edit"))
        {
            onEditStudent();
        }

        //When default mode add student selected
        else
        {
        mBtnSaveChange.setOnClickListener(new View.OnClickListener() {

            /**
             * Method to validate name and Roll Number when SaveChange button clicked
             * @param v instance of View
             */
            @Override
            public void onClick(View v)
            {
                validateNameAndId();
        }}
    );}
    }

    /**
     * Method to check whether entered name and ID are not empty and
     * do not consists of only white spaces.
     */
    public void validateNameAndId()
    {
        //If User has entered a blank name or has entered only white spaces.
        if(validateName.isEmptyName(mEtName.getText().toString()))
        {
            mEtName.setError("Enter a Valid Name");
        }
        //If User has entered a blank ID
        if(validateId.isEmptyId(mEtRollNumber.getText().toString())) {
            mEtRollNumber.setError("Enter an ID");
        }
        //If both Name and ID consists of Valid characters
        else if(! validateName.isEmptyName(mEtRollNumber.getText().toString()) &&
                ! validateId.isEmptyId(mEtRollNumber.getText().toString()))
        {
            Intent intent = new Intent();
            intent.putExtra("key_name", mEtName.getText().toString());
            intent.putExtra("key_id", mEtRollNumber.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    /**
     * Method when main activity requests for Viewing a student
     */
    public void onViewStudent()
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
    public void onEditStudent()
    {
        setTitle(R.string.edit_string);
        mEtName.setText(getIntent().getStringExtra("Name"));
        mEtRollNumber.setText(getIntent().getStringExtra("ID"));
        mBtnSaveChange.setText(getString(R.string.save_changes_button));

        mBtnSaveChange.setOnClickListener(new View.OnClickListener() {

            /**Method to check whether user has not entered a empty name or ID or
             * has entered only white spaces.
             * @param v Istance of View
             */
            @Override
            public void onClick(View v)
            {
                validateNameAndId();
            }});
    }

}

