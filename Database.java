
import javax.management.Query;
import javax.xml.crypto.Data;
import java.sql.*;


public class Database {

    private static Connection connection = null;
    private static boolean isConnected = false;
    private static boolean connectToLocalHost = false;


    /**
     * Function used to close the database connection when finished.
     */
    public static void disconnect(){
        try {
            if (connection != null) { //If there is an open connection, close it.
                connection.close();
                isConnected = false;
            }
        }
        catch(SQLException e){
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    /**e
     * Conects to the UoS Group 34 database.
     * @return Whether it as successfull or not.
     */
    public static boolean connectToDB(){
        if(isConnected){
            System.out.println("You are connected to the database, disconnecting...");
            disconnect();
        }

        try {
            //Connect to the dentistdb on the localhost server (port 3306)
            connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team034", "team034", "869fb20b");
            isConnected = true;
        }
        catch(SQLException e){
            System.out.println("ER  ROR: " + e.getMessage());
            isConnected = false;
        }

        return isConnected;
    }

    /**
     * Conects to the database.
     * @param user The user name.
     * @param pass The password.
     * @return Whether it as successfull or not.
     */
    public static boolean connectToDB(String user, String pass){
        if(!connectToLocalHost) {
            return connectToDB();
        }

        if(isConnected){
            System.out.println("You are connected to the database, disconnecting...");
            disconnect();
        }

        try {
            //Connect to the dentistdb on the localhost server (port 3306)
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dentistdb", user, pass);
            isConnected = true;
        }
        catch(SQLException e){
            System.out.println("ERROR: " + e.getMessage());
            isConnected = false;
        }

        return isConnected;
    }

    /**
     * Executes an SQL query, and returns a Query Result.
     * @param sql The SQl query to execute.
     * @return a Query Result (a table of rows)
     */
    public static QueryResult performSearch(String sql){
        if(!isConnected){
            Database.connectToDB("dentist", "dentistpass");
            //System.out.println("You are not connected to the database, please connect first.");
            //return new QueryResult();
        }

        try {

            System.out.println(sql);

            //Execute the query and convert it to a QueryResult
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery(sql);
            QueryResult qRes = ResultSetToQueryRes(res);
            stmt.close();
            Database.disconnect();
            return qRes;
        }
        catch(SQLException e){
            System.out.println("ERROR: " + e.getMessage());
        }
        finally{
            Database.disconnect();
        }
        return new QueryResult();
    }

    /**
     * Executes a SELECT function on the connected database;
     * @param table The table to select from.
     * @param condition The condition to search from, to get all results leave as "".
     * @return A QueryResult with the results, empty if failed.
     */
    public static QueryResult performSearch(String table, String condition){
        String query = "SELECT * FROM " + table;
        String whereSect = "";
        if(condition != ""){ whereSect = " WHERE " + condition; }
        return performSearch(query + whereSect);
    }

    /**
     * Executes a SELECT function on the connected database, this form can be used for joined results.
     * @param tables The tables to select from, make sure that the main table is at index 0.
     * @param joinMethod The join Method to Use INNER, LEFT OUTER, RIGHT OUTER
     * @param columns1 The first tables columns to join with
     * @param columns2 The second tables columns to join with
     * @param condition The condition to search from, to get all results leave as "".
     * @return A QueryResult with the results, empty if failed.
     */
    public static QueryResult performSearch(String[] tables, String joinMethod, String[] columns1, String[] columns2, String condition){
        String query = "SELECT * FROM " + tables[0];
        String whereSect = "";
        if(condition != ""){ whereSect = " WHERE " + condition; }

        String joinSect = " " + joinMethod + " JOIN " + tables[1] + " ON "; //Add on the joins to the statement.
        for(int c = 0; c< columns1.length; c++){
            if(c > 0){
                joinSect += " AND ";
            }
            joinSect += tables[0] + "." + columns1[c] + "=";
            joinSect += tables[1] + "." + columns2[c];
        }
        return performSearch(query + joinSect + whereSect);
    }

    /**
     * Executes a SELECT function on the connected database, this form can be used for joined results.
     * @param tables The tables to select from, make sure that the main table is at index 0.
     * @param joinMethod The join Method to Use INNER, LEFT OUTER, RIGHT OUTER
     * @param columns The first tables columns to join with
     * @param condition The condition to search from, to get all results leave as "".
     * @return A QueryResult with the results, empty if failed.
     */
    public static QueryResult performSearch(String[] tables, String joinMethod, String[] columns, String condition){
        return performSearch(tables, joinMethod, columns, columns, condition);
    }

    /**
     * @param res The resultSet to convert.
     * @return ResultSet as a Query Result.
     */
    private static QueryResult ResultSetToQueryRes(ResultSet res){
        QueryResult table = new QueryResult();
        try {
            ResultSetMetaData meta = res.getMetaData();
            while (res.next()) {
                Row row = new Row();
                for (int c = 1; c <= meta.getColumnCount(); c++) {
                    row.put(meta.getColumnLabel(c), res.getObject(c));
                }
                table.add(row);
            }
        }
        catch(SQLException e){
            System.out.println("ERROR: " + e.getMessage());
        }

        return table;
    }


    /**
     * Performs an insert or update sql command
     * @param sql The sql statement, with %s instead of values
     * @param args The values in order of appearance.
     * @return The number of affected rows.
     */
    public static int performUpdate(String sql, Object[] args){
        sql = String.format(sql, args);
        return performUpdate(sql);
    }

    /**
     * Performs an insert or update sql command
     * @param sql The sql statement, with values
     * @return The number of affected rows.
     */
    public static int performUpdate(String sql){
        if(!isConnected) {
            Database.connectToDB("dentist", "dentistpass");
            //System.out.println("You are not connected to the database, please connect first.");
            //return 0;
        }

        try {
            System.out.println(sql);
            Statement stmt = connection.createStatement();
            int rowsAff = stmt.executeUpdate(sql);
            stmt.close();
            Database.disconnect();
            return rowsAff;
        }
        catch(SQLException e){
            System.out.println("ERROR: " + e.getMessage());
        }
        finally{
            Database.disconnect();
        }
        return 0;
    }

    /**
     * Inserts a patient into the database..
     * @param data The patient.
     * @return rows affected.
     */
    public static int performInsert(Patient data){
        Address addr = data.getAddress();
        String sql = "INSERT INTO patients(title, forename, surname, dateOfBirth, houseNumber, postCode, checkUpsThisYear, hygieneVisitsThisYear, repairsThisYear) VALUES('%s', '%s', '%s', '%s', %s, '%s', 0, 0, 0)";
        String[] args;
        args = new String[]{data.getTitle(), data.getForename(), data.getSurname(), data.getDOB().toString(), Integer.toString(addr.getHouseNumber()), addr.getPostCode()};
        return performUpdate(sql, args);
    }

    /**
     * Inserts an Address into the database.
     * @param data The address.
     * @return Rows affected.
     */
    public static int performInsert(Address data){
        QueryResult res = performSearch("addresses", "houseNumber=" + data.getHouseNumber() + " AND postCode='" + data.getPostCode()+"'");
        if(res.size() >0){
            return 0;
        }

        String sql = "INSERT INTO addresses(houseNumber, streetName, districtName, city, postCode) VALUES(%s, '%s', '%s', '%s', '%s')";
        String[] args = {Integer.toString(data.getHouseNumber()), data.getStreetName(), data.getDistrictName(), data.getCity(), data.getPostCode()};
        return performUpdate(sql, args);
    }

    /**
     * Insertsa partner into the database.
     * @param data The partner.
     * @return Rows affected.
     */
    public static int performInsert(Partner data){
        String sql = "INSERT INTO partners(role, fullName) VALUES('%s', '%s')";
        String[] args = {data.getRole(), data.getFullName()};
        return performUpdate(sql, args);
    }

    /**
     * Inserts a treatment into the database.
     * @param data The treatment.
     * @return Rows affected.
     */
    public static int performInsert(Treatment data){
        String sql = "INSERT INTO treatments(name, cost) VALUES('%s', %s)";
        String[] args = {data.getName(), Float.toString(data.getCost())};
        return performUpdate(sql, args);
    }

    /**
     * Inserts an appointment into the database.
     * @param data The appointment.
     * @return Rows affected.
     */
    public static int performInsert(Appointment data){
        if(data.getPatient() == -1){
            String sql = "INSERT INTO appointments(startTime, endTime, isComplete, date, partnerID) VALUES('%s', '%s', %s, '%s', %s)";
            String[] args = {data.getStartTime().toString(), data.getEndTime().toString(), Boolean.toString(data.getIsComplete()), data.getDate().toString(),
                    Integer.toString(data.getPartner())};
            return performUpdate(sql, args);
        }else{
            String sql = "INSERT INTO appointments(startTime, endTime, isComplete, date, partnerID, patientID, treatment) VALUES('%s', '%s', %s, '%s', %s, %s, '%s')";
            String[] args = {data.getStartTime().toString(), data.getEndTime().toString(), Boolean.toString(data.getIsComplete()), data.getDate().toString(),
                    Integer.toString(data.getPartner()), Integer.toString(data.getPatient()), data.getTreatment()};
            return performUpdate(sql, args);
        }


    }

    /**
     * Inserts an appointment into the database.
     * @param data The appointment.
     * @return Rows affected.
     */
    public static int performInsert(HealthcarePlan data){
        String sql = "INSERT INTO healthcareplans(planName, checkups, hygieneVisits, repairs, monthlyCost) VALUES('%s', %s' %s, %s, %s)";
        String[] args = {data.getName(), Integer.toString(data.getCheckups()), Integer.toString(data.getHygieneVisits()), Integer.toString(data.getRepairs()), Float.toString(data.getMonthlyCost())};
        return performUpdate(sql, args);
    }

    /**
     * Updates a table in a database
     * @param data the patient to update
     * @return
     */
    public static int performUpdate(Patient data){
        //Creating the SQL statement.
        String sql = "UPDATE patients SET ";
        sql += "title='" + data.getTitle();
        sql += "', forename='" + data.getForename();
        sql += "', surname='" + data.getSurname();
        sql += "', dateOfBirth='" + data.getDOB();
        sql += "', totalOwed=" + data.getTotalOwed();
        sql += ", checkUpsThisYear=" + data.getCheckUpsThisYear();
        sql += ", hygieneVisitsThisYear=" + data.getHygeineVisitsThisYear();
        sql += ", repairsThisYear=" + data.getRepairsThisYear();
        sql += ", houseNumber=" + data.getAddress().getHouseNumber();
        sql += ", postCode='" + data.getAddress().getPostCode();
        if(data.getHealthcarePlan() != null) {
            sql += "', healthCarePlan='" + data.getHealthcarePlan() + "'";
        }else{
            sql += "'";
        }

        sql += " WHERE patientID=" + data.getPatientID();

        return performUpdate(sql);
    }

    /**
     * Updates a table in a database
     * @param data the Appointment
     * @return
     */
    public static int performUpdate(Appointment data){
        String sql = "UPDATE appointments SET ";
        sql += "startTime='" + data.getStartTime();
        sql += "', endTime='" + data.getEndTime();
        sql += "', isComplete='" + Boolean.toString(data.getIsComplete());
        sql += "', date='" + data.getDate();
        sql += "' partnerID=" + data.getPartner();
        sql += "' patientID=" + data.getPatient();
        sql += "' treatment=" + data.getTreatment();


        return performUpdate(sql);
    }

    /**
     * Test harness, examples on how to use this class/
     * @param args
     */
    public static void main(String args[]){

        System.out.println(toSQLDate("02/01/1997"));
        System.out.println(toSQLDate("03-04-1989"));
        System.out.println(toSQLDate("31.06.1980"));
        System.out.println(toSQLDate("28121980"));
        /*//connect with username and password
        Database.connectToDB("dentist", "dentistpass");
        //Gets the first patient in the db
        QueryResult res = Database.performSearch("patients", "patientID =1");

        //get Value from the first row
        System.out.println(res);

        Address a = new Address(0, "", "", "", "a1aa1");
        Database.performInsert(a);

        Patient p = new Patient(0, "Mr", "Jane", "Stevenson", "19900102", 0.0f, 0, 0, 0, a, null);
        Database.performInsert(p);

        //More complex Join search.
        QueryResult PandA = Database.performSearch(new String[] {"patients", "addresses"}, "LEFT OUTER", new String[]{"houseNumber", "postCode"}, "");
        System.out.println(PandA);

        //View the patient table.
        res = Database.performSearch("patients", "");
        System.out.println(res);


        //Update the last inserted row from the patients database.
        int id = res.getRow(res.lastRow()).getValueAsInt("patientID");
        p = new Patient(id, "Mr", "John", "Johnson", "19900201", 1.0f, 4, 3, 2, p.getAddress(), null);
        Database.performUpdate(p);

        //View the patient table.
        res = Database.performSearch("patients", "");
        System.out.println(res.get(res.lastRow()));*/
    }


    public static String toSQLDate(String date){
        date = date.replace("-", "");
        date = date.replace("/", "");
        date = date.replace(".", "");
		String yr = date.substring(4);
        String m = date.substring(2, 4);
        String d = date.substring(0, 2);
		if(date.length() < 8){
				d = "0" + date.substring(0,1);
				m = "0" + date.substring(1,2);
				yr = date.substring(2);
		}
        return yr+m+d;
    }
}
