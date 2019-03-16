package comparator;

import java.util.Comparator;
import com.abhar.android.studentmanagementsqlite.database.model.Student;


/**
 * class sortByIdComparator implements the interface Comparator and helps in sorting the
 * student list by ID
 */

public class sortByIdComparator implements Comparator<Student> {
    public int compare(final Student student1, final Student student2)
    {
        if(student1.getRollNo() == student2.getRollNo())
        {
            return 0;
        }
        else if(student1.getRollNo() < student2.getRollNo())
        {
            return -1;
        }
        else
        {
            return 1;
        }
    }
}
