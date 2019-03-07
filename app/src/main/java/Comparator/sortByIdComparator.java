package Comparator;

import java.util.Comparator;

import Model.Student;

/**
 * class sortByIdComparator implements the interface Comparator and helps in sorting the
 * student list by ID
 */

public class sortByIdComparator implements Comparator<Student> {
    public int compare(final Student student1, final Student student2)
    {
        if(Integer.parseInt(student1.getStudentId()) == Integer.parseInt(student2.getStudentId()))
        {
            return 0;
        }
        else if(Integer.parseInt(student1.getStudentId()) < Integer.parseInt(student2.getStudentId()))
        {
            return -1;
        }
        else
        {
            return 1;
        }
    }
}
