package fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.abhar.android.studentmanagementsqlite.database.model.Student;
import com.abhar.sms.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.Inflater;

import activity.AddStudentActivity;
import activity.MainActivity;
import adapter.CommunicationFragments;
import adapter.StudentAdapter;
import async.BackProcess;
import comparator.sortByIdComparator;
import comparator.sortByNameComparator;
import constant.Constant;
import database.DatabaseHelper;

public class StudentListFragment extends Fragment {

    private final int VIEW = 0;
    private final int EDIT = 1;
    private final int DELETE = 2;
    private ArrayList<Student> list = new ArrayList<Student>();
    final GridLayoutManager grid = new GridLayoutManager(getActivity(), 1);
    private StudentAdapter mAdapter;
    private int mTempPosition;
    private LinearLayout mStudentView;
    private Context mContext;
    private CommunicationFragments communicationFragments;
    private Menu menu;
    Button btnAddStudent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_student_list, container, false);
        setHasOptionsMenu(true);

        btnAddStudent = rootView.findViewById(R.id.btn_add_student);
        mStudentView = rootView.findViewById(R.id.ll_image_text);

        DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
        createRecyclerView(rootView);
        list.addAll(db.getAllStudents());
        displayNoStudentMsg();
        onAdapterItemClick();

        onAddButtonClick();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext =context;
        try {
            communicationFragments=(CommunicationFragments)mContext;
        }catch (ClassCastException e) {
            throw new ClassCastException(getString(R.string.errorRetrieve));
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
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
     * This function sets the position of the item to be edited
     *
     * @param position sets the position
     */
    private void setPosition(int position) {
        mTempPosition = position;
    }

    /**
     * Method to view the clicked student details
     *
     * @param position specifies which position of the list is clicked
     */
    private void viewStudent(final int position) {
        Intent intent=new Intent(mContext,AddStudentActivity.class);
        intent.putExtra(Constant.Name,list.get(position).getName());
        intent.putExtra(Constant.RollNo,Integer.toString(list.get(position).getRollNo()));
        mContext.startActivity(intent);
    }

    /**
     * Method to Edit Student which is clicked.
     *
     * @param position specifies the position
     */
    private void editStudent(final int position) {

        Bundle bundleData = new Bundle();
        bundleData.putString(Constant.rollNum,String.valueOf(list.get(position).getRollNo()));
        bundleData.putString(Constant.Name,list.get(position).getName());

        communicationFragments.communicateForUpdate(bundleData);

        BackProcess backProcess = new BackProcess(mContext);
        backProcess.execute(Constant.deleteStudent,
                String.valueOf(list.get(position).getRollNo()),
                list.get(position).getName());

        list.remove(position);
    }

    /**
     * Method to delete clicked student.
     *
     * @param position specifies the clicked student
     */
    private void deleteStudent(final int position) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_entry)
                .setMessage(R.string.delete_warning)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
                        db.deleteNote(list.get(position));

                        BackProcess backprocess = new BackProcess(getActivity());
                        backprocess.execute(Constant.deleteStudent,
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
     * Method to create RecyclerView
     */
    private void createRecyclerView(View rootView) {
        mAdapter = new StudentAdapter(list);
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(grid);

        recyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), GridLayoutManager.VERTICAL));

        recyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), GridLayoutManager.HORIZONTAL));

        recyclerView.setAdapter(mAdapter);
    }

    /**
     * Method to Add Student
     * @param bundleData bundle stores the Student Data
     */

    public void addStudent(Bundle bundleData){

        Student student = new Student(Integer.parseInt(bundleData.getString(Constant.roll)),
                bundleData.getString(Constant.Name));
        list.add(student);
        mAdapter.notifyDataSetChanged();
        displayNoStudentMsg();
    }

    /**
     * Method to convert List from Linear to Grid and from Grid to Linear
     */
    private void LinearToGridToLinear() {

        if (grid.getSpanCount() == 2) {
            menu.findItem(R.id.menu_grid_layout).setIcon(ContextCompat.getDrawable(mContext,
                    R.drawable.grid_image));
            grid.setSpanCount(1);
        } else {
            menu.findItem(R.id.menu_grid_layout).setIcon(ContextCompat.getDrawable(mContext,
                    R.drawable.list_view_image));
            grid.setSpanCount(2);
        }
    }

    /**
     * Method to display No Student Added when there are No Students in the List
     */
    public void displayNoStudentMsg()
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
     * Method to perform various Actions on clicking Item of List
     */
    public void onAdapterItemClick()
    {
        mAdapter.setOnClickListener(new StudentAdapter.RecyclerViewClickListener() {

            @Override
            public void onClick(final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder
                        (getContext());
                builder.setTitle(R.string.chooseOptionText);
                String[] choice = {getString(R.string.viewText),
                        getString(R.string.editText),
                        getString(R.string.deleteText)};
                builder.setItems(choice, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) { switch (which) {
                        case VIEW:
                            viewStudent(position);
                            break;

                        case EDIT:
                            setPosition(position);
                            editStudent(position);
                            ((MainActivity)mContext).changeTab();
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
        });

    }

    /**
     * Method to perform action on Add Student Button Press
     */
    public void onAddButtonClick()
    {
        btnAddStudent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ((MainActivity)mContext).changeTab();

            }
        });
    }
}



