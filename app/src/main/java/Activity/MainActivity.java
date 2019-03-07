package Activity;

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
import Comparator.sortByIdComparator;
import Comparator.sortByNameComparator;

import java.util.ArrayList;
import java.util.Collections;

import Adapter.StudentAdapter;
import Model.Student;

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

    /**
     * Method to create a new activity
     * @param savedInstanceState creates the activity in the previous left state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnAddStudent = (Button) findViewById(R.id.btn_add_student);
        mStudentView = (LinearLayout) findViewById(R.id.ll_image_text);

        mAdapter = new StudentAdapter(list);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(grid);

        recyclerView.addItemDecoration(new DividerItemDecoration(
                MainActivity.this, GridLayoutManager.VERTICAL));

        recyclerView.addItemDecoration(new DividerItemDecoration(
                MainActivity.this, GridLayoutManager.HORIZONTAL));

        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(new StudentAdapter.RecyclerViewClickListener() {
            /**
             * Method to open a dialog box on click for edit,view,delete
             * @param position the position of the list which is clicked
             */
            @Override
            public void onClick(final int position) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Choose One Option");
                            String[] choice = {"View", "Edit", "Delete"};
                            builder.setItems(choice, new DialogInterface.OnClickListener() {
                                /**
                                 * Function to check which option selected among edit,view,delete
                                 * @param dialog To create dialog interface
                                 * @param which To know which option of the dialog box selected
                                 */

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        //Case for View
                                        case VIEW:
                                            viewStudent(position);
                                            break;

                                        //Case for Edit
                                        case EDIT:
                                            editStudent(position);
                                            break;

                                        //Case for Delete
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
            /**
             * Function to ask for result from AddStudentActivity
             * @param v instance of View
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddStudentActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD);
            }
        });
}

    /**
     * To create menu
     * @param menu Instace of Menu
     * @return true when menu created
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        this.menu = menu;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return true;
    }

    /**
     * Function to change List View to Grid View and vice versa
     * @param item Menu Item which is selected
     * @return true when any of the option of the menu option clicked
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            //Case when user clicks the grid layout icon
            case R.id.menu_grid_layout:
                LinearToGridToLinear();
                return true;

                //If user selects sort by name submenu
            case R.id.menu_submenu_sort_by_name:


                Collections.sort(list, new sortByNameComparator());
                mAdapter.notifyDataSetChanged();
                return true;

                //If user selects Sort by ID submenu
            case R.id.menu_submenu_sort_by_id:

                Collections.sort(list, new sortByIdComparator());
                mAdapter.notifyDataSetChanged();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Method to recieve the results from AddStudentActivity
     * @param requestCode To know for which intent request we are getting the result
     * @param resultCode To know if successfully got a result from AddStudentActivity
     * @param data The data we get from the AddStudentActivity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        //If results recieved for add intent
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
        //If results recieved for Edit intent
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

        intentView.putExtra("Mode", "View");
        intentView.putExtra(
                "Name", list.get(position).getStudentName());

        intentView.putExtra(
                "ID", list.get(position).getStudentId());

        startActivity(intentView);
    }

    /**
     * Method to Edit Student which is clicked.
     * @param position
     */
    public void editStudent(final int position)
    {
        setPosition(position);
        Intent intentEdit = new Intent(
                MainActivity.this, AddStudentActivity.class);

        intentEdit.putExtra("Mode", "Edit");
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
}
