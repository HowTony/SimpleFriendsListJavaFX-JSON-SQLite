import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;

/**
 * Created by Tony Howarth on 2/24/2017.
 */
public class Database {

    private List<Friend> mFriends;
    private QueryData mQueriedData;

    public Database() {
        mQueriedData = new QueryData();
        mFriends = mQueriedData.getLoadedList();
    }

    public void addToList(Friend friend) {
        if (!friendInList(friend)) {
            mFriends.add(friend);
        }
    }

    public void removeFromList(Friend friend) {
        if (friendInList(friend)) {
            mFriends.remove(friend);
        }
    }

    private void printFriends() {
        for (Friend friend: mFriends) {
            System.out.println(friend);
        }
    }

    public ObservableList<Friend> getFriends() {
        ObservableList<Friend> friends = FXCollections.observableArrayList();
        friends.addAll(mFriends);
        return friends;
    }

    private boolean friendInList(Friend friendName){
        boolean bool = false;
        for (Friend friend: mFriends) {
            if(friendName.getName().equalsIgnoreCase((friend.getName()))){
                bool = true;
            }
        }
        return bool;
    }

    public QueryData getQueriedData(){
        return mQueriedData;
    }

    public void saveFriendsToDisk() {
        try {
            mQueriedData.saveFriendsToDisk(mFriends);
        }catch (IOException i){
            i.printStackTrace();
        }
    }

    public void editFriend(Friend editedFriend, Friend friendToEdit){
        if(!friendInList(editedFriend)) {
            mFriends.set(mFriends.indexOf(friendToEdit), editedFriend);
        }else{
            mFriends.set(mFriends.indexOf(friendToEdit), new Friend( friendToEdit.getName(), editedFriend.getLocation()));
        }
    }



}
