package activity;

import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
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

import async.BackProcess;
import async.BackProcessForList;
import comparator.sortByIdComparator;
import comparator.sortByNameComparator;

import java.util.ArrayList;
import java.util.Collections;

import adapter.StudentAdapter;
import constant.Constant;
import database.DatabaseHelper;


import com.abhar.android.studentmanagementsqlite.database.model.Student;

/**
 * The MainActivity class implements an application that initially has no students
 * but new students information like Name and Roll Number of Student is displayed here.
 */
public class StudentActivity extends AppCompatActivity implements BackProcessForList.Callback {

    private ArrayList<Student> list = new ArrayList<Student>();
    final GridLayoutManager grid = new GridLayoutManager(this, 1);
    private StudentAdapter mAdapter;
    private int mTempPosition;
    private Menu menu;
    private LinearLayout mStudentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseHelper db = DatabaseHelper.getInstance(StudentActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnAddStudent = findViewById(R.id.btn_add_student);
        mStudentView = findViewById(R.id.ll_image_text);

        createRecyclerView();
        list.addAll(db.getAllStudents());
        displayNoStudentAdded();
        onItemClickRecyclerView();

        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentActivity.this, AddStudentActivity.class);
                startActivityForResult(intent, Constant.REQUEST_CODE_ADD);
            }
        });
    }

    /**
     * Method to create RecyclerView
     */
    private void createRecyclerView() {
        mAdapter = new StudentAdapter(list);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(grid);

        recyclerView.addItemDecoration(new DividerItemDecoration(
                StudentActivity.this, GridLayoutManager.VERTICAL));

        recyclerView.addItemDecoration(new DividerItemDecoration(
                StudentActivity.this, GridLayoutManager.HORIZONTAL));

        recyclerView.setAdapter(mAdapter);
    }

    /**
     * Method to perform various action such as view, edit or delete on selecting an
     * item of RecyclerView
     */
    public void onItemClickRecyclerView()
    {
        mAdapter.setOnClickListener(new StudentAdapter.RecyclerViewClickListener() {

            @Override
            public void onClick(final int position) {

                AlertDialog.Builder builder = new AlertDialog.Builder
                        (StudentActivity.this);
                builder.setTitle(R.string.chooseOptionText);
                String[] choice = {getString(R.string.viewText),
                        getString(R.string.editText),
                        getString(R.string.deleteText)};
                builder.setItems(choice, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {

                            case Constant.VIEW:
                                createViewIntent(position);
                                break;

                            case Constant.EDIT:
                                createEditIntent(position);
                                break;

                            case Constant.DELETE:
                                deleteStudent(position);
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
        );
    }

    /**
     * Method to create Intent for viewing
     * @param position Position clicked
     */
    private void createViewIntent(int position) {
        Intent intentView = new Intent(
                StudentActivity.this, AddStudentActivity.class);

        intentView.putExtra(Constant.Mode, Constant.view);
        intentView.putExtra
                (Constant.Name, list.get(position).getName());

        intentView.putExtra(Constant.Id, list.get(position).getRollNo());

        startActivity(intentView);
    }

    /**
     * Method to create Intent for Edit
     * @param position Position clicked
     */
    private void createEditIntent(int position) {
        setPosition(position);
        Intent intentEdit = new Intent(
                StudentActivity.this, AddStudentActivity.class);

        intentEdit.putExtra(Constant.Mode, Constant.edit);
        intentEdit.putExtra
                (Constant.Name, list.get(position).getName());

        intentEdit.putExtra(Constant.Id, list.get(position).getRollNo());
        startActivityForResult(intentEdit, Constant.REQUEST_CODE_EDIT);
    }

    /**
     * Method to delete clicked student.
     *
     * @param position specifies the clicked student
     */
    private void deleteStudent(final int position) {
        new AlertDialog.Builder(StudentActivity.this)
                .setTitle(R.string.delete_entry)
                .setMessage(R.string.delete_warning)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper db = DatabaseHelper.getInstance(StudentActivity.this);
                        db.deleteNote(list.get(position));

                        BackProcess backprocess = new BackProcess(StudentActivity.this);
                        backprocess.execute(getString(R.string.deleteStudent),
                                String.valueOf(list.get(position).getRollNo()),
                                list.get(position).getName());
                        list.remove(position);

                        mAdapter.notifyDataSetChanged();
                        displayNoStudentAdded();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.REQUEST_CODE_ADD) {
            {
                if (resultCode == RESULT_OK) {
                    mStudentView.setVisibility(View.GONE);
                    long id = data.getLongExtra(Constant.keyId,0);
                    DatabaseHelper db = DatabaseHelper.getInstance(StudentActivity.this);
                    Student student = db.getStudent(id);
                    list.add(student);
                    mStudentView.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
                }
            }

        } else if (requestCode == Constant.REQUEST_CODE_EDIT) {
            if (resultCode == RESULT_OK) {
                DatabaseHelper db = DatabaseHelper.getInstance(StudentActivity.this);
                long id = data.getLongExtra(Constant.keyId,0);
                list.set(getPosition(), db.getStudent(id));
                mAdapter.notifyDataSetChanged();
            }
        }
    }

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

    /**
     * Method to convert List from Linear to Grid and from Grid to Linear
     */
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

    /**
     * Method to display No student Added when List Empty
     */
    public void displayNoStudentAdded()
    {
        if(list.size() == 0)
        {
            mStudentView.setVisibility(View.VISIBLE);
        }
        else
        {
            mStudentView.setVisibility(View.GONE);
        }
    }

    /**
     * This function sets the position of the item to be edited
     *
     * @param position sets the position
     */
    private void setPosition(int position) {
        mTempPosition = position;
    }

    /**
     * This function returns the position of item to be edited
     *
     * @return mTempPosition returns the position which is to be edited
     */
    private int getPosition() {
        return mTempPosition;
    }

    @Override
    public void getOutput(ArrayList<Student> out) {
        list=out;
        mAdapter.notifyDataSetChanged();
    }
}


