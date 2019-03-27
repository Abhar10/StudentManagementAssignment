package activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import adapter.CommunicationFragments;
import fragment.AddStudentFragment;

import com.abhar.sms.R;

/**
 * This class helps in performing several actions such as adding a new student,
 * viewing a student details and for editing student details.
 */

public class AddStudentActivity extends AppCompatActivity implements CommunicationFragments {
    private Bundle bundle;
    private AddStudentFragment addStudentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_add);


        bundle=getIntent().getExtras();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        addStudentFragment=new AddStudentFragment();
        fragmentTransaction.add(R.id.frag_container,addStudentFragment);
        fragmentTransaction.commit();

    }

    @Override
    public void communicateForAdd(Bundle bundleData) {

    }

    @Override
    public void communicateForUpdate(Bundle bundleData) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        addStudentFragment.viewPrint(bundle);
    }
}

