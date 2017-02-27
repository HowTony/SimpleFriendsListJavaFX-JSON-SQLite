import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tony Howarth on 2/24/2017.
 */
public class Database {

    private List<Friend> mFriends;

    public Database() {
        mFriends = new ArrayList<>();
    }

    public void addToList(Friend friend) {
        if (!friendInList(friend.getName())) {
            mFriends.add(friend);
        }
    }

    public void removeFromList(Friend friend) {
        if (!friendInList(friend.getName())) {
            mFriends.remove(friend);
        }
    }

    public void saveFriendsToDisk() throws IOException {
        printFriends();
        System.out.println("----------------------------------------------");
        try {
            FileWriter fileOut = new FileWriter(new File("d:/friend.txt"));
            for (Friend friend: mFriends) {
                fileOut.write(friend.getName() + ", " + friend.getLocation());
                fileOut.write(System.getProperty("line.separator"));
            }
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
        //printFriends();
    }

    public void loadFriendsFromFile() {
        try {
            FileReader fileIn = new FileReader("d:/friend.txt");
            BufferedReader bf = new BufferedReader(fileIn);
            while(bf.ready()) {
                String friend = bf.readLine();
                String[] friendToSplit = friend.split(", ", 2);
                String name = friendToSplit[0];
                String location = friendToSplit[1];
                Friend currentFriend = new Friend(name, location);
                addToList(currentFriend);
            }
            bf.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        }
        printFriends();
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

    private boolean friendInList(String friendName){
        boolean bool = false;
        for (Friend friend: mFriends) {
            if(friendName == friend.getName()){
                bool = true;
            }
        }
        return bool;
    }
}
