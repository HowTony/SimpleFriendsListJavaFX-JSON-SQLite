
/**
 * Created by Tony Howarth on 2/24/2017.
 */
public class Friend {

    private String mName;
    private String mLocation;
    private String mBio;

//    public Friend(String name){
//        mName = name;
//    }
    public Friend(String name, String location){
        mName = name;
        mLocation = location;
    }

//    public Friend(String name, String location, String bio){
//        mName = name;
//        mLocation = location;
//        mBio = bio;
//    }

    public String getName() {
        return mName;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getmBio() {
        return mBio;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public void setmBio(String mBio) {
        this.mBio = mBio;
    }

    @Override
    public String toString() {
        return mName + ", " + mLocation;
    }
}
