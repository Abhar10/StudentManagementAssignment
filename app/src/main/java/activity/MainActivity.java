package activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.abhar.sms.R;
import adapter.CommunicationFragments;
import adapter.ViewPagerAdapter;
import fragment.AddStudentFragment;
import fragment.StudentListFragment;

/**
 * The MainActivity class implements an application that initially has no students
 * but new students information like Name and Roll Number of Student is displayed here.
 */
public class MainActivity extends AppCompatActivity implements CommunicationFragments {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

    }

    /**
     * Method to change Tab of ViewPager
     */
    public void changeTab() {

        if (viewPager.getCurrentItem() == 0) {
            viewPager.setCurrentItem(1);
        }
        else if (viewPager.getCurrentItem() == 1) {
            viewPager.setCurrentItem(0);
        }
    }

    @Override
    public void communicateForAdd(Bundle bundleData) {
        String tag = "android:switcher:" + R.id.view_pager + ":" + 0;
        StudentListFragment f = (StudentListFragment) getSupportFragmentManager().findFragmentByTag(tag);
        f.addStudent(bundleData);
    }

    @Override
    public void communicateForUpdate(Bundle bundleData)
    {
        String tag = "android:switcher:" + R.id.view_pager + ":" + 1;
        AddStudentFragment f = (AddStudentFragment)getSupportFragmentManager().findFragmentByTag(tag);
        f.update(bundleData);
    }
}