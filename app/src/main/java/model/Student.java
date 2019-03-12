package model;

/**
 * The class Student is the model and helps in setting Roll number and name of student
 * and also in getting name and roll number of student.
 */
public class Student {

    private String mStudentName;
    private String mStudentId;

    /**
     * Constructor to set the name and roll number of the created student
     * @param name Name of the student
     * @param id Roll Number of the student
     */
    public Student(String name,String id)
    {
        mStudentName = name;
        mStudentId = id;
    }

    /**
     * Function used to get the name of the student
     * @return string that is the name of the student
     */
    public String getStudentName()
    {

        return mStudentName;
    }

    /**
     * Function used to get ID of the student
     * @return string that is the roll number of the sudent.
     */
    public String getStudentId()
    {

        return mStudentId;
    }

}
