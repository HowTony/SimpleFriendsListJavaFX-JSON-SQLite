import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;


import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tony Howarth on 3/1/2017.
 */
public class QueryData {

    private List<Person> mPeople;
    private final String SAVE_FILE = "d:/friends.json";
    private SQLiteManager mSQLManager;

    public QueryData() throws SQLException, ClassNotFoundException {
        mPeople = new ArrayList<>();
        mSQLManager = new SQLiteManager();
        //loadFriendsFromFile();
        loadFriendsFromDataBase();
    }

    public List<Person> getLoadedList(){
        return mPeople;
    }

    /**
     * Saves copy of people list in JSON format with pretty printing enabled to save file location
     * @param people
     * @throws IOException
     */
    public void saveFriendsToDisk(List<Person> people) throws IOException {
            try (Writer writer = new FileWriter(SAVE_FILE)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(people, writer);
            }catch (IOException e){
                e.printStackTrace();
            }
    }

    public void saveFriendsToDataBase(List<Person>people) throws SQLException, ClassNotFoundException {
        for(Person eachPerson: people) {
            if (!mSQLManager.userIDInDB(eachPerson.getID())){
                mSQLManager.addUser(eachPerson.getFirstName(), eachPerson.getLastName(), eachPerson.getLocation());
            }
        }
    }

    public void loadFriendsFromDataBase() throws SQLException, ClassNotFoundException {
       mPeople =  mSQLManager.loadUsersToMemory();
    }


    public void loadFriendsFromFile() {
        try {
            JsonReader reader = new JsonReader(new FileReader(SAVE_FILE));
            GsonBuilder gbuilder = new GsonBuilder();
            Gson gson = gbuilder
                    .create();
            reader.beginArray();
            while (reader.hasNext()) {
                Person person = gson.fromJson(reader, Person.class);
                    if(!friendInList(person.getName())) {
                        mPeople.add(person);
                    }
            }
            reader.close();

        }catch (UnsupportedEncodingException ex){
            ex.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        }
    }

    private boolean friendInList(String friendName){
        boolean bool = false;
        System.out.println(friendName.length());
        for (Person person : mPeople) {
            if(friendName.equalsIgnoreCase(person.getName()) || friendName.length() < 2){
                bool = true;
            }
        }
        return bool;
    }

    public void removeFromDB(int userID) throws SQLException, ClassNotFoundException {
        mSQLManager.removeUser(userID);
    }

    public void editUserInDB(Person person) throws SQLException, ClassNotFoundException {
        mSQLManager.editUser(person);
    }
}
