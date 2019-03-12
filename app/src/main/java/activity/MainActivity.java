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

import com.abhar.sms.R;
import comparator.sortByIdComparator;
import comparator.sortByNameComparator;

import java.util.ArrayList;
import java.util.Collections;

import adapter.StudentAdapter;
import model.Student;

/**
 * The MainActivity class implements an application that initially has no students
 * but new students information like Name and Roll Number of Student is displayed here.
 */
public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ADD = 1;
    public static final int REQUEST_CODE_EDIT = 2;
    ArrayList<Student> list = new ArrayList<Student>();
    final GridLayoutManager grid = new GridLayoutManager(this,1);
    private StudentAdapter mAdapter;
    private int mTempPosition;
    private Menu menu;
    private LinearLayout mStudentView;
    private final int VIEW=0;
    private final int EDIT=1;
    private final int DELETE=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnAddStudent = (Button) findViewById(R.id.btn_add_student);
        mStudentView = (LinearLayout) findViewById(R.id.ll_image_text);

        createRecyclerView();

        mAdapter.setOnClickListener(new StudentAdapter.RecyclerViewClickListener() {

            @Override
            public void onClick(final int position) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle(R.string.chooseOptionText);
                            String[] choice = {getString(R.string.viewText), getString(R.string.editText),
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        this.menu = menu;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
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
                    String name = data.getStringExtra("key_name");
                    String id = data.getStringExtra("key_id");
                    list.add(new Student(name, id));
                    mAdapter.notifyDataSetChanged();

                }
                else if (resultCode == RESULT_CANCELED) {
                    //whatever
                }
            }
        }

        else if(requestCode == REQUEST_CODE_EDIT)
        {
            if(resultCode == RESULT_OK)
            {
                String name = data.getStringExtra("key_name");
                String id = data.getStringExtra("key_id");
                list.set(getPosition(),new Student(name, id));
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * This function sets the position of the item to be edited
     * @param position sets the position
     */
    public void setPosition(int position)
    {
        mTempPosition = position;
    }

    /**
     * This function returns the position of item to be edited
     * @return mTempPosition returns the position which is to be edited
     */
    public int getPosition()
    {
        return mTempPosition;
    }

    /**
     * Method to view the clicked student details
     * @param position specifies which position of the list is clicked
     */
    public void viewStudent(final int position)
    {
        Intent intentView = new Intent(
                MainActivity.this, AddStudentActivity.class);

        intentView.putExtra("Mode","View");
        intentView.putExtra
                ("Name", list.get(position).getStudentName());

        intentView.putExtra("ID", list.get(position).getStudentId());

        startActivity(intentView);
    }

    /**
     * Method to Edit Student which is clicked.
     * @param position specifies the position
     */
    public void editStudent(final int position)
    {
        setPosition(position);
        Intent intentEdit = new Intent(
                MainActivity.this, AddStudentActivity.class);

        intentEdit.putExtra("Mode","Edit");
        intentEdit.putExtra
                ("Name", list.get(position).getStudentName());

        intentEdit.putExtra("ID", list.get(position).getStudentId());
        startActivityForResult(intentEdit, REQUEST_CODE_EDIT);
    }

    /**
     * Method to delete clicked student.
     * @param position specifies the clicked student
     */
    public void deleteStudent(final int position)
    {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.delete_entry)
                .setMessage(R.string.delete_warning)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(position);
                        mAdapter.notifyDataSetChanged();
                        if(list.isEmpty())
                        {
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
    public void LinearToGridToLinear()
    {
        //If spancount is equal to 2 change it to linear
        if(grid.getSpanCount() == 2)
        {
            menu.findItem(R.id.menu_grid_layout).setIcon(ContextCompat.getDrawable(this, R.drawable.grid_image));
            grid.setSpanCount(1);
        }
        //If spancount is equal to 1 change it to grid
        else
        {
            menu.findItem(R.id.menu_grid_layout).setIcon(ContextCompat.getDrawable(this, R.drawable.list_view_image));
            grid.setSpanCount(2);
        }
    }

    /**
     * Method to create RecyclerView
     */
    public void createRecyclerView()
    {
        mAdapter = new StudentAdapter(list);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(grid);

        recyclerView.addItemDecoration(new DividerItemDecoration(
                MainActivity.this, GridLayoutManager.VERTICAL));

        recyclerView.addItemDecoration(new DividerItemDecoration(
                MainActivity.this, GridLayoutManager.HORIZONTAL));

        recyclerView.setAdapter(mAdapter);
    }

}
