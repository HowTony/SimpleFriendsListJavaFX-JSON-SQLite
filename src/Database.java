import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Tony Howarth on 2/24/2017.
 */
public class Database {

    private List<Person> mPeople;
    private QueryData mQueriedData;

    public Database() throws SQLException, ClassNotFoundException {
        mQueriedData = new QueryData();
        mPeople = mQueriedData.loadFriendsFromDataBase();
    }

    public void addToList(Person person) {
        if (!friendInList(person)) {
            mPeople.add(person);
        }
    }

    public void removeFromList(Person person) throws SQLException, ClassNotFoundException {
        if (friendInList(person)) {
            mPeople.remove(person);
            mQueriedData.removeFromDB(person.getID());
        }
    }

    public ObservableList<Person> getFriendsInObservableList() {
        ObservableList<Person> people = FXCollections.observableArrayList();
        people.addAll(mPeople);
        return people;
    }

    private boolean friendInList(Person personName){
        boolean bool = false;
        for (Person person : mPeople) {
            if(personName.getLocation().equalsIgnoreCase((person.getLocation()))){
                bool = true;
            }
        }
        return bool;
    }

    public QueryData getQueriedData(){
        return mQueriedData;
    }

    public void saveFriendsToDisk() throws SQLException, ClassNotFoundException {
        mQueriedData.saveFriendsToDataBase(mPeople);
    }

    public void editFriend(Person editedPerson, Person personToEdit) throws SQLException, ClassNotFoundException {
        if(!friendInList(editedPerson)) {
            mPeople.set(mPeople.indexOf(personToEdit), editedPerson);
            mQueriedData.editUserInDB(editedPerson);
        }else{
            Person somePerson = new Person( editedPerson.getFirstName(), editedPerson.getLastName(), editedPerson.getLocation());
            mPeople.set(mPeople.indexOf(personToEdit), somePerson);
            mQueriedData.editUserInDB(somePerson);
        }
    }

    public Person getPerson(int userID){
        for(Person eachPerson: mPeople){
            if(eachPerson.getID() == userID){
                return eachPerson;
            }
        }
        return null;
    }
}
