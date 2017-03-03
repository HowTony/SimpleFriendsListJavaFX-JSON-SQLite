/**
 * Created by Tony Howarth on 2/24/2017.
 */
public class Friend{
    private String mName;
    private String mLocation;
    private String mBio;

    public Friend(String name, String location){
        this.mName = name;
       this.mLocation = location;
       mBio = "";
    }
    public String getName() {
        return mName;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public void setBio(String mBio) {
        this.mBio = mBio;
    }

    @Override
    public String toString() {
        return mName + ", " + mLocation;
    }
}
