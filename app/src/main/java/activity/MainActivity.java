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
import database.DatabaseHelper;


import com.abhar.android.studentmanagementsqlite.database.model.Student;

/**
 * The MainActivity class implements an application that initially has no students
 * but new students information like Name and Roll Number of Student is displayed here.
 */
public class MainActivity extends AppCompatActivity implements BackProcessForList.Callback {

    public static final int REQUEST_CODE_ADD = 1;
    public static final int REQUEST_CODE_EDIT = 2;
    private final int VIEW = 0;
    private final int EDIT = 1;
    private final int DELETE = 2;
    private ArrayList<Student> list = new ArrayList<Student>();
    final GridLayoutManager grid = new GridLayoutManager(this, 1);
    private StudentAdapter mAdapter;
    private int mTempPosition;
    private Menu menu;
    private LinearLayout mStudentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseHelper db = DatabaseHelper.getInstance(MainActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnAddStudent = findViewById(R.id.btn_add_student);
        mStudentView = findViewById(R.id.ll_image_text);


        createRecyclerView();
        list.addAll(db.getAllStudents());
        //new BackProcessForList(MainActivity.this,MainActivity.this).execute();
        mAdapter.setOnClickListener(new StudentAdapter.RecyclerViewClickListener() {

                                        @Override
                                        public void onClick(final int position) {

                                            AlertDialog.Builder builder = new AlertDialog.Builder
                                                    (MainActivity.this);
                                            builder.setTitle(R.string.chooseOptionText);
                                            String[] choice = {getString(R.string.viewText),
                                                    getString(R.string.editText),
                                                    getString(R.string.deleteText)};
                                            builder.setItems(choice, new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    switch (which) {
                                                        case VIEW:
                                                            viewStudent(position);
                                                            break;

                                                        case EDIT:
                                                            editStudent(position);
                                                            break;

                                                        case DELETE:
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


        btnAddStudent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddStudentActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD);
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD) {
            {
                if (resultCode == RESULT_OK) {

                    mStudentView.setVisibility(View.GONE);
                    //String name = data.getStringExtra("key_name");
                    long id = data.getLongExtra("key_id",0);
                    //list.add(new Student(id,name));
                    DatabaseHelper db = DatabaseHelper.getInstance(MainActivity.this);
                    Student student = db.getStudent(id);
                    Log.i("name",student.getName());
                    list.add(student);
                    mAdapter.notifyDataSetChanged();

                }
            }
        } else if (requestCode == REQUEST_CODE_EDIT) {
            if (resultCode == RESULT_OK) {
                DatabaseHelper db = DatabaseHelper.getInstance(MainActivity.this);
                //String name = data.getStringExtra("key_name");
                long id = data.getLongExtra("key_id",0);
                Log.i("cvgjhbnlkk",String.valueOf(data.getLongExtra("key_id",0)));
                //list.set(getPosition(), new Student(id,name));

                // Student student
                list.set(getPosition(), db.getStudent(id));
                mAdapter.notifyDataSetChanged();
            }
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

    /**
     * Method to view the clicked student details
     *
     * @param position specifies which position of the list is clicked
     */
    private void viewStudent(final int position) {
        createViewIntent(position);
    }

    /**
     * Method to Edit Student which is clicked.
     *
     * @param position specifies the position
     */
    private void editStudent(final int position) {
        createEditIntent(position);
    }

    /**
     * Method to delete clicked student.
     *
     * @param position specifies the clicked student
     */
    private void deleteStudent(final int position) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.delete_entry)
                .setMessage(R.string.delete_warning)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper db = DatabaseHelper.getInstance(MainActivity.this);
                        db.deleteNote(list.get(position));

                        BackProcess backprocess = new BackProcess(MainActivity.this);
                        backprocess.execute("delete_student",
                                String.valueOf(list.get(position).getRollNo()),
                                list.get(position).getName());
                        list.remove(position);

                        mAdapter.notifyDataSetChanged();
                        if (list.isEmpty()) {
                            mStudentView.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
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
     * Method to create RecyclerView
     */
    private void createRecyclerView() {
        mAdapter = new StudentAdapter(list);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(grid);

        recyclerView.addItemDecoration(new DividerItemDecoration(
                MainActivity.this, GridLayoutManager.VERTICAL));

        recyclerView.addItemDecoration(new DividerItemDecoration(
                MainActivity.this, GridLayoutManager.HORIZONTAL));

        recyclerView.setAdapter(mAdapter);
    }

    /**
     * Method to create Intent for viewing
     * @param position Position clicked
     */
    private void createViewIntent(int position) {
        Intent intentView = new Intent(
                MainActivity.this, AddStudentActivity.class);

        intentView.putExtra("Mode", "View");
        intentView.putExtra
                ("Name", list.get(position).getName());

        intentView.putExtra("ID", list.get(position).getRollNo());

        startActivity(intentView);
    }

    /**
     * Method to create Intent for Edit
     * @param position Position clicked
     */
    private void createEditIntent(int position) {
        setPosition(position);
        Intent intentEdit = new Intent(
                MainActivity.this, AddStudentActivity.class);

        intentEdit.putExtra("Mode", "Edit");
        intentEdit.putExtra
                ("Name", list.get(position).getName());
        Log.i("qwertyuio",list.get(position).getName());

        intentEdit.putExtra("ID", list.get(position).getRollNo());
        startActivityForResult(intentEdit, REQUEST_CODE_EDIT);
    }

    @Override
    public void getOutput(ArrayList<Student> out) {
        list=out;
        mAdapter.notifyDataSetChanged();
    }
}


