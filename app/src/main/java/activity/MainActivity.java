package activity;

import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.LinearLayout;
import android.util.Log;
import com.abhar.sms.R;

import adapter.CommunicationFragments;
import adapter.ViewPagerAdapter;
import async.BackProcess;
import async.BackProcessForList;
import comparator.sortByIdComparator;
import comparator.sortByNameComparator;

import java.util.ArrayList;
import java.util.Collections;

import adapter.StudentAdapter;
import database.DatabaseHelper;
import fragment.AddStudentFragment;
import fragment.StudentListFragment;


import com.abhar.android.studentmanagementsqlite.database.model.Student;

/**
 * The MainActivity class implements an application that initially has no students
 * but new students information like Name and Roll Number of Student is displayed here.
 */
public class MainActivity extends AppCompatActivity implements CommunicationFragments {

   // public static final int REQUEST_CODE_ADD = 1;
    //public static final int REQUEST_CODE_EDIT = 2;
    //private final int VIEW = 0;
    //private final int EDIT = 1;
    //private final int DELETE = 2;
    private ArrayList<Student> list = new ArrayList<Student>();
    final GridLayoutManager grid = new GridLayoutManager(this, 1);
    private StudentAdapter mAdapter;
    //private int mTempPosition;
    private Menu menu;
    //private LinearLayout mStudentView;
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

    public void changeTab() {

        if (viewPager.getCurrentItem() == 0) {
            viewPager.setCurrentItem(1);
        }
        else if (viewPager.getCurrentItem() == 1) {
            viewPager.setCurrentItem(0);
        }


    }

    public void setToolbarTitle()
    {
        if(viewPager.getCurrentItem() == 0)
        {
            setTitle("Student List");
        }
        else if(viewPager.getCurrentItem() == 1)
        {
            setTitle("Add/Edit Student");
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


/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_grid_layout:
                LinearToGridToLinear();
                return true;

            case R.id.menu_submenu_sort_by_name:
                Collections.sort(list, new sortByNameComparator());
                mAdapter.notifyDataSetChanged();
                return true;

            case R.id.menu_submenu_sort_by_id:
                Collections.sort(list, new sortByIdComparator());
                mAdapter.notifyDataSetChanged();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

*/

    /**
     * Method to convert List from Linear to Grid and from Grid to Linear
     */
/*
    private void LinearToGridToLinear() {

        if (grid.getSpanCount() == 2) {
            menu.findItem(R.id.menu_grid_layout).setIcon(ContextCompat.getDrawable(this,
                    R.drawable.grid_image));
            grid.setSpanCount(1);
        } else {
            menu.findItem(R.id.menu_grid_layout).setIcon(ContextCompat.getDrawable(this,
                    R.drawable.list_view_image));
            grid.setSpanCount(2);
        }
    }

*/

}






