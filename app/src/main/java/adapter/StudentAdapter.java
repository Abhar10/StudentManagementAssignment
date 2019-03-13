package adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abhar.sms.R;

import java.util.ArrayList;
import java.util.List;

import model.Student;

/**
 * This class extends the abstract class Adapter. This class helps in coverting a listview
 * to a recycler view.
 */
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.studentViewHolder>
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
    public class studentViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvId;

        /**
         * Constructor to initialize view of view holder
         * @param view instance of View
         */
        public studentViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_name);
            tvId = view.findViewById(R.id.tv_roll_no);

            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if(mListener!=null){
                        int position=getAdapterPosition();

                        if(position!=RecyclerView.NO_POSITION){
                            mListener.onClick(position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public studentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout, parent, false);

        return new studentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(studentViewHolder myViewHolder, int i) {
        Student text = StudentList.get(i);

        myViewHolder.tvName.setText(text.getStudentName());
        myViewHolder.tvId.setText(text.getStudentId());
    }

    @Override
    public int getItemCount() {
        int size = StudentList.size();
        return size;
    }


}


