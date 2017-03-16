/**
 * Created by Tony Howarth on 2/24/2017.
 */
public class Person {
    private String mFirstName;
    private String mLastName;
    private String mLocation;
    private String mBio;
    private int mID;

    public Person(String firstName, String lastName, String location){
        this.mFirstName = firstName;
        this.mLastName = lastName;
        this.mLocation = location;
        mBio = "";
    }

    public Person(String firstName, String lastName, String location, int id){
        this.mFirstName = firstName;
        this.mLastName = lastName;
        this.mLocation = location;
        mID = id;
        mBio = "";
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setID(int id){
        mID = id;
    }

    public String getName(){
        return mFirstName + " " + mLastName;
    }

    public String getLastName(){
        return mLastName;
    }

    public int getID(){
        return mID;
    }

    @Override
    public String toString() {
        return mID + " " + mFirstName + " " + mLastName + ", " + mLocation + ",";
    }
}
