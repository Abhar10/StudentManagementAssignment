package Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abhar.sms.R;

import java.util.ArrayList;
import java.util.List;

import Model.Student;

import static android.content.ContentValues.TAG;

/**
 * This class extends the abstract class Adapter. This class helps in coverting a listview
 * to a recycler view.
 */
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder>
{
    List<Student> StudentList;
    private RecyclerViewClickListener mListener;

    /**
     * Interface to implement Click Listener on Recycler View
     */
    public interface RecyclerViewClickListener
    {
        void onClick(int position);
    }

    /**
     * Method to initialize listener
     * @param listener RecyclerViewClickListener
     */
    public void setOnClickListener(RecyclerViewClickListener listener){
        mListener=listener;
    }

    /**
     * Constructor to initialize Student List
     * @param StudentList The list of students created by the user
     */
    public StudentAdapter(ArrayList<Student> StudentList) {
        this.StudentList = StudentList;
    }

    /**
     * Inner class of RecyclerView for view holder
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, id;

        /**
         * Constructor to initialize view of view holder
         * @param view instance of View
         */
        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tv_name);
            id = (TextView) view.findViewById(R.id.tv_roll_no);

            view.setOnClickListener(new View.OnClickListener() {
                /**
                 * Method to get the position of the clicked view holder
                 * @param v instance of view
                 */
                @Override
                public void onClick(View v) {
                    //To know which position viewholder clicked
                    if(mListener!=null){
                        int position=getAdapterPosition();
                        //If none of the view holder clicked
                        if(position!=RecyclerView.NO_POSITION){
                            mListener.onClick(position);
                        }
                    }
                }
            });
        }
    }

    /**
     * Method to return the created view holder
     * @param parent To get the context of the parent view group
     * @param viewType To define the view type of the viewholder
     * @return created view holder
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    /**
     * Method to bind data to the View Holder
     * @param myViewHolder instance of ViewHolder
     * @param i position of the view holder to be binded
     * */
    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        Student text = StudentList.get(i);

        myViewHolder.name.setText(text.getStudentName());
        myViewHolder.id.setText(text.getStudentId());
    }

    /**
     * Method to return the number of items in the list
     */
    @Override
    public int getItemCount() {
        int size = StudentList.size();
        return size;
    }


}


