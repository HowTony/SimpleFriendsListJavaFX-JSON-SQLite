import java.sql.SQLException;
import java.util.List;

/**
 * Created by Tony Howarth on 3/1/2017.
 */
public class QueryData {

    private SQLiteManager mSQLManager;

    public QueryData() throws SQLException, ClassNotFoundException {
        mSQLManager = new SQLiteManager();
        loadFriendsFromDataBase();
    }

    public void saveFriendsToDataBase(List<Person>people) throws SQLException, ClassNotFoundException {
        for(Person eachPerson: people) {
            if (!mSQLManager.userIDInDB(eachPerson.getLocation())){
                mSQLManager.addUser(eachPerson);
            }
        }
    }

    public List<Person> loadFriendsFromDataBase() throws SQLException, ClassNotFoundException {
       return mSQLManager.loadUsersToMemory();
    }

    public void removeFromDB(int userID) throws SQLException, ClassNotFoundException {
        mSQLManager.removeUser(userID);
    }

    public void editUserInDB(Person person) throws SQLException, ClassNotFoundException {
        mSQLManager.editUser(person);
    }

    public SQLiteManager getSQLManager() {
        return mSQLManager;
    }
}
