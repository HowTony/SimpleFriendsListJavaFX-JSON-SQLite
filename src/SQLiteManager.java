import java.io.File;
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
            String usersTable = "CREATE TABLE IF NOT EXISTS users " +
                    "(userID INTEGER PRIMARY KEY AUTOINCREMENT        NOT NULL," +
                    " firstName           TEXT    NOT NULL, " +
                    " lastName           TEXT    NOT NULL, " +
                    " address        CHAR(50))";

            String friendsTable = "CREATE TABLE IF NOT EXISTS friends " +
                    "(userID INT        NOT NULL, " +
                    "friendID INT      NOT NULL)";

            stmt.executeUpdate(usersTable);
            stmt.executeUpdate(friendsTable);
            stmt.close();
    }

    private void getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        File temp = new File("tmp/users.db");
        mCon = DriverManager.getConnection("jdbc:sqlite:" + temp.getAbsolutePath());
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

    public void addUser(Person person) throws SQLException, ClassNotFoundException {
        if(mCon == null){
            getConnection();
        }
        PreparedStatement prep = mCon.prepareStatement("INSERT INTO users VALUES(?, ?, ?, ?);");
        prep.setString(2, person.getFirstName());
        prep.setString(3, person.getLastName());
        prep.setString(4, person.getLocation());
        prep.execute();
        person.setID(getUserIDFromAddress(person.getLocation()));
    }

    public void getUserWithID(int userID) throws SQLException, ClassNotFoundException {
        if (mCon == null) {
            getConnection();
        }
        Statement state = mCon.createStatement();
        ResultSet res = state.executeQuery("SELECT FROM users WHERE userID == " + userID);
        while(res.next()){
            System.out.println(res.getInt("userid") +  "\t" +
                    res.getString("firstname") + "\t" +
                    res.getString("lastname"));
        }
    }

    public int getUserIDFromAddress(String address) throws SQLException, ClassNotFoundException {
        if (mCon == null) {
            getConnection();
        }
        Statement state = mCon.createStatement();
        ResultSet res = state.executeQuery("SELECT userID FROM users WHERE address = '" + address + "'");
        return res.getInt("userID");
    }

    public boolean userIDInDB(String address) throws SQLException, ClassNotFoundException {
        if (mCon == null) {
            getConnection();
        }
        Statement state = mCon.createStatement();
        ResultSet res = state.executeQuery("SELECT * FROM users WHERE address = '" + address + "'");
        while(res.next()) {
            if (res.getString("address").equalsIgnoreCase(address)) {
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

    public void addFriends(int userID, int friendID) throws SQLException, ClassNotFoundException {
        if(mCon == null){
            getConnection();
        }
        PreparedStatement prep = mCon.prepareStatement("INSERT INTO friends VALUES(?, ?)");
        prep.setInt(1, userID);
        prep.setInt(2, friendID);
        prep.execute();
        prep.setInt(1, friendID);
        prep.setInt(2, userID);
        prep.execute();
    }

    public void removeFriend(int mainUser, int userFriend) throws SQLException, ClassNotFoundException {
        if (mCon == null) {
            getConnection();
        }
        Statement state = mCon.createStatement();
        state.executeUpdate("DELETE FROM friends WHERE userID = '" + mainUser + "' AND friendID = '" + userFriend + "'");
        state.executeUpdate("DELETE FROM friends WHERE userID = '" + userFriend + "' AND friendID = '" + mainUser + "'");
    }

    public List<Integer> getFriends(int userID) throws SQLException, ClassNotFoundException {
        List<Integer> friendIDs = new ArrayList<>();
        if(mCon == null){
            getConnection();
        }
        Statement stmt = mCon.createStatement();
        String sql = "SELECT friendID FROM FRIENDS WHERE " + "userID == " + userID;
        ResultSet res = stmt.executeQuery(sql);
        while(res.next()) {
            friendIDs.add(res.getInt("friendID"));
        }

        return friendIDs;
    }
}
