package app;

import java.sql.*;
import java.util.Scanner;


public class App {
    private static Scanner input = new Scanner(System.in);
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public void openConnection()
    {
        try {
            DriverManager.registerDriver(new com.ibm.db2.jcc.DB2Driver());
        }
        catch(Exception cnfex) {
            System.out.println("Problem in loading or registering IBM DB2 JDBC driver");
            cnfex.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:db2://62.44.108.24:50000/SAMPLE", "db2admin", "db2admin");
            statement = connection.createStatement();
        }
        catch(SQLException s){
            s.printStackTrace();
        }
    }

    public void closeConnection(){
        try {
            if(null != connection) {
                resultSet.close();
                statement.close();
                connection.close();
            }
        }
        catch (SQLException s) {
            s.printStackTrace();
        }
    }

    public void select(String stmnt, int column) {

        try{

            resultSet = statement.executeQuery(stmnt);

            String result = "";

            while(resultSet.next()) {

                for (int i = 1; i <= column; i++) {

                    result += resultSet.getString(i);

                    if (i == column) result += " \n";
                    else             result += ", ";
                }
            }

            System.out.println("Executing query: " + stmnt + "\n");
            System.out.println("Result output \n");
            System.out.println("---------------------------------- \n");
            System.out.println(result);

        }
        catch (SQLException s)
        {
            s.printStackTrace();
        }

    }

    public void insert(String stmnt) {

        try{

            statement.executeUpdate(stmnt);

        }

        catch (SQLException s){

            s.printStackTrace();

        }

        System.out.println("Successfully inserted!");

    }


    public void delete(String stmnt) {

        try{

            statement.executeUpdate(stmnt);

        }

        catch (SQLException s){

            s.printStackTrace();

        }

        System.out.println("Successfully deleted!");

    }

    //menu method
    public static void printInfo()
    {
        System.out.println("Menu:");
        System.out.println("1 - Find student by:");
        System.out.println("2 - Show attended courses");
        System.out.println("3 - Show students mentor");
        System.out.println("4 - Find teacher by:");
        System.out.println("5 - Show departments headteacher");
        System.out.println("6 - Enroll student");
        System.out.println("7 - Hire teacher");
        System.out.println("8 - Create course");
        System.out.println("9 - Exit");
    }

    //choice 1
    private static void choice1(App app)
    {
        System.out.print("Choose criteria (course/egn): ");
        String criteria = input.next();
        if(criteria.equals("course"))
        {
            int nCourse;
            System.out.print("Choose course: ");
            do {
                nCourse = input.nextInt();
            }while(1 > nCourse || nCourse > 6);
            String select = "SELECT * FROM FN71923.STUDENTS WHERE yearOfStudying = " + nCourse;
            System.out.println(select);
            app.select(select, 7);
        }
        else if(criteria.equals("egn"))
        {
            System.out.print("Enter egn: ");
            String egn = input.next();
            app.select("SELECT * FROM FN71923.STUDENTS WHERE EGN = " + egn, 7);
        }
        else
        {
            System.out.println("Selected all");
            app.select("SELECT * FROM FN71923.STUDENTS", 7);
        }
    }

    private static void choice2(App db2Obj) {
        System.out.print("Enter egn: ");
        String egn = input.next();
        db2Obj.select("SELECT S.NAME AS STUDENTNAME, C.NAME AS COURSENAME\n" +
                "FROM FN71923.STUDENTS S, FN71923.ATTENDS A, FN71923.COURSES C\n" +
                "WHERE S.EGN = A.STUDENTEGN AND A.COURSEYEAR = C.YEAR AND A.COURSENUMBER = C.COURSENUMBER AND EGN = " + egn, 2);
    }

    private static void choice3(App db2Obj) {
        System.out.print("Enter egn: ");
        String egn = input.next();
        db2Obj.select("SELECT S.NAME AS STUDENTNAME, S.MENTOR\n" +
                "FROM FN71923.STUDENTS S\n" +
                "WHERE S.EGN = " + egn, 2);
    }


    private static void choice4(App db2Obj) {
        System.out.print("Enter egn: ");
        String egn = input.next();
        db2Obj.select("SELECT *\n" +
                "FROM FN71923.TEACHER\n" +
                "WHERE teacherEGN = " + egn, 4);
    }

    private static void choice6(App db2Obj) {
        input.nextLine();
        String name;
        System.out.print("Enter name: ");
        name = input.nextLine();

        String egn;
        do {
            System.out.print("Enter egn (10 digits): ");
            egn = input.nextLine();
        } while(!egn.matches("[0-9]{10}"));

        String facultyNumber;
        do {
            System.out.print("Enter faculty number (5 digits): ");
            facultyNumber = input.nextLine();
        } while(!facultyNumber.matches("[0-9]{5}"));

        String major;
        System.out.print("Enter major: ");
        major = input.nextLine();

        int yearOfStudying;
        do {
            System.out.print("Enter course: ");
            yearOfStudying = input.nextInt();
        } while(yearOfStudying < 1 || yearOfStudying > 6);

        input.nextLine();
        String mentor;
        do {
            System.out.print("Enter mentor (10 digits): ");
            mentor = input.nextLine();
        } while(!mentor.matches("[0-9]{10}"));

        db2Obj.insert("INSERT INTO FN71923.STUDENTS VALUES ('" + name + "', '" + egn + "', '" + facultyNumber + "', '" + major + "', " + yearOfStudying + " , '01', '" + mentor + "')");
    }


    private static void choice7(App db2Obj) {
        input.nextLine();
        String name;
        System.out.print("Enter name: ");
        name = input.nextLine();

        String egn;
        do {
            System.out.print("Enter egn (10 digits): ");
            egn = input.nextLine();
        } while(!egn.matches("[0-9]{10}"));

        String scientificTitle;
        System.out.print("Enter scientific title: ");
        scientificTitle = input.nextLine();

        String departmentName;
        System.out.print("Enter department: ");
        departmentName = input.nextLine();

        db2Obj.insert("INSERT INTO FN71923.TEACHER VALUES ('" + name + "', '" + egn + "', '" + scientificTitle + "', '" + departmentName + "')");
    }


    private static void choice8(App db2Obj)
    {
        input.nextLine();
        String name;
        System.out.print("Enter name: ");
        name = input.nextLine();

        int courseNumber;
        System.out.print("Enter course number: ");
        courseNumber = input.nextInt();

        int year;
        System.out.print("Enter year: ");
        year = input.nextInt();

        String type;
        System.out.print("Enter type: ");
        type = input.next();

        String description;
        System.out.print("Enter type: ");
        description = input.nextLine();

        String egn;
        do {
            System.out.print("Enter egn (10 digits): ");
            egn = input.nextLine();
        } while(!egn.matches("[0-9]{10}"));

        db2Obj.insert("INSERT INTO FN71923.COURSES VALUES ('" + name + "', " + courseNumber + ", " + year + ", '" + type + "', " + description + ", '" + egn + "')");
    }

    public static void main(String[] args) {
        //initialise
        App db2Obj = new App();
        db2Obj.openConnection();

        //main program
        int currentChoise;
        do
        {
            //user choice
            printInfo();
            System.out.print("Your choice: ");
            currentChoise = input.nextInt();

            switch(currentChoise)
            {
                case 1:
                {   choice1(db2Obj);
                    break;
                }
                case 2: 
                {
                    choice2(db2Obj);
                    break;
                }
                case 3:
                {
                    choice3(db2Obj);
                    break;
                }
                case 4:
                {
                    choice4(db2Obj);
                    break;
                }
                case 5:
                {
                    db2Obj.select("SELECT * FROM FN71923.MENTOROFDEPARTMENTS", 2);
                    break;
                }
                case 6:
                {
                    choice6(db2Obj);
                    break;
                }
                case 7:
                {
                    choice7(db2Obj);
                    break;
                }
                case 8:
                {
                    choice8(db2Obj);
                    break;
                }
                case 9:
                {
                    System.out.println("Thank you for using our program");
                    break;
                }

            }
        } while(currentChoise != 9);

        //finalise
        db2Obj.closeConnection();
    }
}
