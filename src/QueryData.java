import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tony Howarth on 3/1/2017.
 */
public class QueryData {

    private List<Friend>mFriends;
    private final String SAVE_FILE = "d:/friends.json";

    public QueryData(){
        mFriends = new ArrayList<>();
        loadFriendsFromFile();

    }

    public List<Friend> getLoadedList(){
        return mFriends;
    }

    /**
     * Saves copy of friends list in JSON format with pretty printing enabled to save file location
     * @param friends
     * @throws IOException
     */
    public void saveFriendsToDisk(List<Friend> friends) throws IOException {
            try (Writer writer = new FileWriter(SAVE_FILE)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(friends, writer);
            }catch (IOException e){
                e.printStackTrace();
            }
    }

    public void loadFriendsFromFile() {
        try {
            JsonReader reader = new JsonReader(new FileReader(SAVE_FILE));
            GsonBuilder gbuilder = new GsonBuilder();
            Gson gson = gbuilder
                    .create();
            reader.beginArray();
            while (reader.hasNext()) {
                Friend friend = gson.fromJson(reader, Friend.class);
                    if(!friendInList(friend.getName())) {
                        mFriends.add(friend);
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
        for (Friend friend: mFriends) {
            if(friendName.equalsIgnoreCase(friend.getName()) || friendName.length() < 2){
                bool = true;
            }
        }
        return bool;
    }
}
