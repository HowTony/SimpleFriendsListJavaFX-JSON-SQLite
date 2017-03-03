import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by Tony Howarth on 2/24/2017.
 */
public class Main extends Application{

    public static void main(String[] args){
        launch(args);
    }

    private TableView<Friend> mTable = new TableView<>();
    private ObservableList<Friend> mObsList;
    private Database mData = new Database();
    private HBox mBox = new HBox();
    private TextField mNameInput, mLocationInput;
    private Group mRoot;
    private Friend mSelectedFriend;

    @Override
    public void start(Stage primaryStage){
        mRoot = new Group();
        mTable.setEditable(true);
        mData.getQueriedData().loadFriendsFromFile();
        mObsList = mData.getFriends();

        Scene scene = new Scene(mRoot);
        primaryStage.setWidth(475);
        primaryStage.setHeight(500);

        TableColumn<Friend, String> firstNameCol = new TableColumn("Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        firstNameCol.setMinWidth(primaryStage.getWidth() / 2.2);

        TableColumn<Friend, String> locationCol = new TableColumn("Location");
        locationCol.setCellValueFactory(new PropertyValueFactory("location"));
        locationCol.setMinWidth(primaryStage.getWidth() / 2.2);

        mTable.setItems(mObsList);
        mTable.getColumns().addAll(firstNameCol, locationCol);

        mTable.setRowFactory(tv -> {
                    TableRow<Friend> row = new TableRow<>();
        row.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && (!row.isEmpty())) {
                mSelectedFriend = row.getItem();
            }
        });
        return row;
    });

        //inputs
        mNameInput = new TextField();
        mNameInput.setPromptText("Name");
        mNameInput.setMinSize(50, 30);

        mLocationInput = new TextField();
        mLocationInput.setPromptText("Location");
        mLocationInput.setMinSize(50, 30);

        Button addButton = new Button("Add");
        addButton.setOnAction(event -> addClicked());

        Button editButton = new Button("Edit");
        editButton.setOnAction(event -> editClicked());

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> deleteClicked());

        mBox.getChildren().addAll( mNameInput, mLocationInput, addButton, editButton, deleteButton);
        mBox.setSpacing(3);

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(mTable, mBox);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void deleteClicked() {
        if(mSelectedFriend != null) {
            mData.removeFromList(mSelectedFriend);
        }
        saveClicked();
    }

    private void editClicked() {
        if(mSelectedFriend != null) {
            String name = "";
            String location = "";
            if (mNameInput.getText().length() < 1) {
                name = mSelectedFriend.getName();
            } else {
                name = mNameInput.getText();
            }
            if (mLocationInput.getText().length() < 1) {
                location = mSelectedFriend.getLocation();
            } else {
                location = mLocationInput.getText();
            }
            mData.editFriend(new Friend(name, location), mSelectedFriend);
            mLocationInput.clear();
            mNameInput.clear();
            saveClicked();
        }
        mSelectedFriend = null;
    }

    public void addClicked(){
        if(mNameInput.getText().length() > 0 && mLocationInput.getText().length() > 0 ) {
            mData.addToList(new Friend(mNameInput.getText(), mLocationInput.getText()));
            mLocationInput.clear();
            mNameInput.clear();
            saveClicked();
        }
    }

    public void saveClicked(){
        mData.saveFriendsToDisk();
        mTable.setItems(mData.getFriends());
    }
}
