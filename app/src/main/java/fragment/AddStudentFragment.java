package fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.abhar.sms.R;

import activity.AddStudentActivity;
import activity.MainActivity;
import adapter.CommunicationFragments;
import async.BackProcess;
import background.BackgroundIntent;
import background.BackgroundService;
import constant.Constant;
import database.DatabaseHelper;
import validate.validateId;
import validate.validateName;


public class AddStudentFragment extends Fragment {
    private EditText mEtName;
    private EditText mEtRollNumber;
    private Button mBtnSaveChange;
    private CommunicationFragments communicationFragments;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        try {
            communicationFragments=(CommunicationFragments)mContext;
        }catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       // (getActivity()).setToolbarTitle();
        View rootView = inflater.inflate(R.layout.fragment_add_student, container, false);
        mEtName = rootView.findViewById(R.id.et_user_name);
        mEtRollNumber = rootView.findViewById(R.id.et_user_roll_no);
        mBtnSaveChange = rootView.findViewById(R.id.btn_save_changes);
        createTextWatcher(mEtName);

        mBtnSaveChange.setOnClickListener(new View.OnClickListener() {

            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getActivity());

            @Override
            public void onClick(View v)
            {
                boolean error=true;

                if(validateName.isEmptyName(mEtName.getText().toString()))
                {
                    mEtName.setError(getString(R.string.valid_name_error));
                    error=false;
                    //mBtnSaveChange.setEnabled(false);
                }
                if(validateId.isEmptyId(mEtRollNumber.getText().toString())) {
                    mEtRollNumber.setError(getString(R.string.valid_id_error));
                    error=false;
                   // mBtnSaveChange.setEnabled(false);
                }
                else if(databaseHelper.isExisting(Integer.parseInt(mEtRollNumber.getText().toString())))
                {
                    mEtRollNumber.setError("Enter A Unique Roll Number");
                    error=false;
                    //mBtnSaveChange.setEnabled(false);
                }

                if(error)
                {
                    int roll = Integer.parseInt(mEtRollNumber.getText().toString());
                    String name = mEtName.getText().toString();
                    String rollNum = mEtRollNumber.getText().toString();
                    Bundle bundle = new Bundle();
                    bundle.putString("Name", name);
                    bundle.putString("Roll",rollNum);
                    Log.i("abc",String.valueOf(roll));
                    communicationFragments.communicateForAdd(bundle);
                    generateDialog(Constant.addStudent,rollNum,name,"");

                }}}
        );
        return rootView;
    }
    /**
     * Method to check whether user is typing valid text
     * @param mEtName EditText for Name
     *
     */
    private void createTextWatcher(EditText mEtName)
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

    public void update(Bundle bundleData){

        mEtName.setText(bundleData.getString("Name"));
        mEtRollNumber.setText(bundleData.getString("RollNum"));

    }
    public void viewPrint(Bundle bundleData){
        mEtName.setText(bundleData.getString(Constant.Name));
        mEtRollNumber.setText(bundleData.getString(Constant.RollNo));
        mBtnSaveChange.setVisibility(View.GONE);
        mEtRollNumber.setEnabled(false);
        mEtName.setEnabled(false);
    }
    public  void onViewStudent(String name,String id)
    {
        //mContext.setTitle(R.string.view_string);
        mEtName.setText(name);
        mEtRollNumber.setText(id);
        mEtRollNumber.setEnabled(false);
        mEtName.setEnabled(false);
        mBtnSaveChange.setVisibility(View.GONE);
    }

    public void generateDialog(final String operation,
                               final String roll ,final String name,final String oldRoll)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder
                (mContext);
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
                        (new BackProcess(getActivity()))
                                .execute(operation, roll, name,oldRoll);
                        ((MainActivity)getActivity()).changeTab();
                        mEtName.getText().clear();
                        mEtRollNumber.getText().clear();
                        break;

                    case 1:
                        Intent intent = new Intent(getActivity(),
                                BackgroundService.class);
                        intent.putExtra(Constant.Mode,operation);
                        intent.putExtra(Constant.Name,name);
                        intent.putExtra(Constant.RollNo,roll);
                        intent.putExtra(Constant.oldRollNo,oldRoll);
                        mContext.startService(intent);
                        ((MainActivity)getActivity()).changeTab();
                        mEtName.getText().clear();
                        mEtRollNumber.getText().clear();
                        break;

                    case 2:
                        Intent intentStudent = new Intent(getActivity(),
                                BackgroundIntent.class);
                        intentStudent.putExtra(Constant.Mode,operation);
                        intentStudent.putExtra(Constant.Name,name);
                        intentStudent.putExtra(Constant.RollNo,roll);
                        intentStudent.putExtra(Constant.oldRollNo,oldRoll);
                        mContext.startService(intentStudent);
                        ((MainActivity)getActivity()).changeTab();
                        mEtName.getText().clear();
                        mEtRollNumber.getText().clear();
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

