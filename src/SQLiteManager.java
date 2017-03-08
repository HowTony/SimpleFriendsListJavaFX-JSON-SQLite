import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Tony Howarth on 3/3/2017.
 */
public class SQLiteManager {

    private static Connection mCon;

    public SQLiteManager() throws SQLException, ClassNotFoundException {
       getConnection();
       initialize();
    }

    private void initialize() throws SQLException, ClassNotFoundException {
        if(mCon == null){
            getConnection();
        }
        Statement stmt = mCon.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS users " +
                    "(userID INTEGER PRIMARY KEY AUTOINCREMENT        NOT NULL," +
                    " firstName           TEXT    NOT NULL, " +
                    " lastName           TEXT    NOT NULL, " +
                    " address        CHAR(50))";

            String sql2 = "CREATE TABLE IF NOT EXISTS friends " +
                    "(userID INT        NOT NULL," +
                    "friendID INT      NOT NULL)";

            stmt.executeUpdate(sql);
            stmt.executeUpdate(sql2);
            stmt.close();
    }

    private void getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        mCon = DriverManager.getConnection("jdbc:sqlite:users.db");
    }

    public List<Person> loadUsersToMemory() throws SQLException, ClassNotFoundException {
        List<Person> peopleInMemory = new ArrayList<>();
        if (mCon == null) {
            getConnection();
        }
        Statement state = mCon.createStatement();
        ResultSet res = state.executeQuery("SELECT * FROM users");
        while(res.next()){
            peopleInMemory.add(new Person(res.getString("firstName"),
                    res.getString("lastName"),
                    res.getString("address"),
                    res.getInt("userID")));


        }

        return peopleInMemory;
    }

    public void displayUsers() throws SQLException, ClassNotFoundException {
        if (mCon == null) {
            getConnection();
        }
        Statement state = mCon.createStatement();
        ResultSet res = state.executeQuery("SELECT * FROM users");
        while(res.next()){

            System.out.println(res.getInt("userid") +  "\t" +
                    res.getString("firstname") + "\t" +
                    res.getString("lastname"));

        }
    }

    public void addUser(String firstName, String lastName) throws SQLException, ClassNotFoundException {
        if(mCon == null){
            getConnection();
        }
        PreparedStatement prep = mCon.prepareStatement("INSERT INTO users VALUES(?, ?, ?);");
        prep.setString(2, firstName);
        prep.setString(3, lastName);
        prep.execute();
    }

    public void addUser(String firstName, String lastName, String address) throws SQLException, ClassNotFoundException {
        if(mCon == null){
            getConnection();
        }
        PreparedStatement prep = mCon.prepareStatement("INSERT INTO users VALUES(?, ?, ?, ?);");
        prep.setString(2, firstName);
        prep.setString(3, lastName);
        prep.setString(4, address);
        prep.execute();
    }

    public void addFriends(int userID, int friendID) throws SQLException, ClassNotFoundException {
        if(mCon == null){
            getConnection();
        }
        PreparedStatement prep = mCon.prepareStatement("INSERT INTO friends VALUES(?, ?)");
        prep.setInt(1, userID);
        prep.setInt(2, friendID);
        PreparedStatement prep2 = mCon.prepareStatement("INSERT INTO friends VALUES(?, ?)");
        prep2.setInt(1, friendID);
        prep2.setInt(2, userID);
        prep.execute();
        prep2.execute();
    }

    public void getFriends(int userID) throws SQLException, ClassNotFoundException {
        if(mCon == null){
            getConnection();
        }
        Statement stmt = mCon.createStatement();
        String sql = "SELECT * FROM FRIENDS WHERE " + "userID == " + userID;
        ResultSet res = stmt.executeQuery(sql);
        while(res.next()) {
            System.out.println(res.getString(2));
        }
    }

    public void getUserData(int userID) throws SQLException, ClassNotFoundException {
        if (mCon == null) {
            getConnection();
        }
        Statement state = mCon.createStatement();
        ResultSet res = state.executeQuery("SELECT * FROM users WHERE userID == " + userID);
        while(res.next()){
            System.out.println(res.getInt("userid") +  "\t" +
                    res.getString("firstname") + "\t" +
                    res.getString("lastname"));
        }
    }

    public boolean userIDInDB(int userID) throws SQLException, ClassNotFoundException {
        if (mCon == null) {
            getConnection();
        }
        Statement state = mCon.createStatement();
        ResultSet res = state.executeQuery("SELECT * FROM users WHERE userID == " + userID);
        while(res.next()) {
            if (res.getInt("userID") == userID) {
                return true;
            }
        }
        return false;
    }

    public void removeUser(int userID) throws SQLException, ClassNotFoundException {
        if (mCon == null) {
            getConnection();
        }
        Statement state = mCon.createStatement();
        state.executeUpdate("DELETE FROM users WHERE userID = " + userID);

    }

    public void editUser(Person person) throws SQLException, ClassNotFoundException{
        Person copy = person;
        int num = person.getID();
        String sql = "UPDATE users SET firstName = ? , " +
                "lastName = ? , " +
                "address = ? " +
                "WHERE userID = " + person.getID();
        if (mCon == null) {
            getConnection();
        }
        PreparedStatement prep = mCon.prepareStatement(sql);


        prep.setString(1, person.getFirstName());
        prep.setString(2, person.getLastName());
        prep.setString(3, person.getLocation());

        prep.executeUpdate();
    }

    public void removeFriend(){

    }
}
