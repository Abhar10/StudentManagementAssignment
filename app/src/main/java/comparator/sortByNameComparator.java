package comparator;

import java.util.Comparator;

import com.abhar.android.studentmanagementsqlite.database.model.Student;

/**class sortByNameComparator implements the interface Comparator
 * and helps in sorting the student list by name.
 */
public class sortByNameComparator implements Comparator<Student> {
    public int compare(final Student student1, final Student student2)
    {
        if(student1.getName().compareTo(student2.getName()) == 0)
        {
            return 0;
        }
        else if(student1.getName().compareTo(student2.getName()) < 0)
        {
            return -1;
        }
        else
        {
            return 1;
        }
    }

}
