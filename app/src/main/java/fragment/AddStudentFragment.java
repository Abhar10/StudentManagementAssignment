package fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.abhar.sms.R;
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
    private StudentBroadcastReceiver studentBroadcastReceiver = new StudentBroadcastReceiver();

    @Override
    public void onStart() {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter(Constant.FILTER_ACTION_KEY);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(studentBroadcastReceiver,intentFilter);

    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(studentBroadcastReceiver);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        try {
            communicationFragments=(CommunicationFragments)mContext;
        }catch (ClassCastException e) {
            throw new ClassCastException(getString(R.string.error_retrieve));
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_add_student, container, false);
        initialize(rootView);
        createTextWatcher(mEtName);
        onSaveButtonPress();
        return rootView;
    }

    /**
     * Method to initialize variables
     * @param rootView the inflated fragment view
     */
    public void initialize(View rootView)
    {
        mEtName = rootView.findViewById(R.id.et_user_name);
        mEtRollNumber = rootView.findViewById(R.id.et_user_roll_no);
        mBtnSaveChange = rootView.findViewById(R.id.btn_save_changes);
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

    /**
     * Method to transfer data from one fragment to other
     * @param bundleData bundle stores the EditTexts data
     */
    public void update(Bundle bundleData){

        mEtName.setText(bundleData.getString(Constant.Name));
        mEtRollNumber.setText(bundleData.getString(Constant.rollNum));

    }

    /**
     * Method to View the selected Item of List
     * @param bundleData bundle stores the data to be viewed
     */
    public void viewPrint(Bundle bundleData){
        mEtName.setText(bundleData.getString(Constant.Name));
        mEtRollNumber.setText(bundleData.getString(Constant.RollNo));
        mBtnSaveChange.setVisibility(View.GONE);
        mEtRollNumber.setEnabled(false);
        mEtName.setEnabled(false);
    }

    /**
     * Generate Dialog for AsyncTask, Service and Intent Service
     * @param operation Mode
     * @param roll Roll Number
     * @param name Name
     * @param oldRoll Roll Number to be updated
     */
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

    /**
     * Method to perform actions on Save Button Pressed
     */
    public void onSaveButtonPress()
    {
        mBtnSaveChange.setOnClickListener(new View.OnClickListener() {

            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getActivity());

            @Override
            public void onClick(View v)
            {
                boolean error=false;

                if(validateName.isEmptyName(mEtName.getText().toString()))
                {
                    mEtName.setError(getString(R.string.valid_name_error));
                    error=true;
                }
                if(validateId.isEmptyId(mEtRollNumber.getText().toString())) {
                    mEtRollNumber.setError(getString(R.string.valid_id_error));
                    error=true;
                }
                else if(databaseHelper.isExisting(Integer.parseInt(mEtRollNumber.getText().toString())))
                {
                    mEtRollNumber.setError(getString(R.string.unique_id_error));
                    error=true;
                }

                if(! error)
                {
                    String name = mEtName.getText().toString();
                    String rollNum = mEtRollNumber.getText().toString();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.Name, name);
                    bundle.putString(Constant.roll,rollNum);
                    communicationFragments.communicateForAdd(bundle);
                    generateDialog(Constant.addStudent,rollNum,name,"");

                }}}
        );
    }

    /**Inner broadcast receiver that receives the broadcast if the services have indeed added the elements
     * in the database.
     */
    public class StudentBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            Toast.makeText(context,getString(R.string.broadcastRecieved),Toast.LENGTH_SHORT).show();
        }
    }
}


