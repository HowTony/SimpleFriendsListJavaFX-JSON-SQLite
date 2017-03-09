import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

/**
 * Created by Tony Howarth on 2/24/2017.
 */
public class Main extends Application{

    public Main() throws SQLException, ClassNotFoundException {
    }

    public static void main(String[] args){
        launch(args);
    }

    private TableView<Person> mTable = new TableView<>();
    private ObservableList<Person> mObsList;
    private Database mData = new Database();
    private HBox mBox = new HBox();
    private TextField mNameInput, mLocationInput;
    private Group mRoot;
    private Person mSelectedPerson;
    private Stage mPrimaryStage;
    private Button mExtendView;
    private boolean GSON_ACTIVE = false;
    TableView<Person> mFriends;
    VBox mVBox;
    Pane mPane;

    @Override
    public void start(Stage primaryStage){
        mPrimaryStage = primaryStage;
        mRoot = new Group();
        mTable.setEditable(true);

        if(GSON_ACTIVE) {
            mData.getQueriedData().loadFriendsFromFile();
        }
        mObsList = mData.getFriends();

        Scene scene = new Scene(mRoot);
        primaryStage.setWidth(510);
        primaryStage.setHeight(500);

        TableColumn<Person, String> firstNameCol = new TableColumn("Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        firstNameCol.setPrefWidth(primaryStage.getWidth() / 2.5);

        TableColumn<Person, String> locationCol = new TableColumn("Location");
        locationCol.setCellValueFactory(new PropertyValueFactory("location"));
        locationCol.setPrefWidth(primaryStage.getWidth() / 2.5);

        mTable.setItems(mObsList);
        mTable.getColumns().addAll(firstNameCol, locationCol);

        mTable.setRowFactory(tv -> {
                    TableRow<Person> row = new TableRow<>();
        row.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && (!row.isEmpty())) {
                mSelectedPerson = row.getItem();


            }else if(event.getClickCount() == 2 && (!row.isEmpty())){
                mFriends = new TableView<>();
                TableColumn<Person, String> friendNameCol = new TableColumn<>("Possible Friends");
                friendNameCol.setCellValueFactory(new PropertyValueFactory("name"));
                friendNameCol.setPrefWidth(250);
                ObservableList<Person> possibleFriends = FXCollections.observableArrayList(mObsList);
                possibleFriends.remove(mSelectedPerson);
                Button addFriend = new Button("Add Friend");
                addFriend.setOnAction(eventAdd -> {

                        addFriendClicked();

                });
                Button viewFriends = new Button("View Friends");
                addFriend.setOnAction(eventAdd -> {

                    viewFriendClicked();

                });



                mFriends.setLayoutY(mTable.getLayoutY());
                mFriends.setLayoutX(mTable.getLayoutX() + 475);
                addFriend.setLayoutX(mFriends.getLayoutX() + (475 / 1.8));
                addFriend.setLayoutY(10);
                mFriends.setItems(possibleFriends);
                mFriends.getColumns().addAll(friendNameCol);
                mPane.getChildren().addAll(mFriends, addFriend);



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
        addButton.setOnAction(event -> {
            try {
                addClicked();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        Button editButton = new Button("Edit");
        editButton.setOnAction(event -> {
            try {
                editClicked();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            try {
                deleteClicked();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });


        mExtendView = new Button(">>");
        mExtendView.setOnAction(event -> adjustMainView());
        mPane = new Pane();
        mExtendView.setScaleX(.5);
        mExtendView.setScaleY(.8);
        mExtendView.setLayoutX(444);
        mExtendView.setLayoutY(8);
        mPane.getChildren().add(mExtendView);

        mBox.getChildren().addAll( mNameInput, mLocationInput, addButton, editButton, deleteButton);
        mBox.setSpacing(3);

        mVBox = new VBox();
        mVBox.setSpacing(10);
        mVBox.setPadding(new Insets(10, 10, 10, 10));
        mVBox.getChildren().addAll(mTable, mBox);

        ((Group) scene.getRoot()).getChildren().addAll(mVBox, mPane);

        mPrimaryStage.setScene(scene);
        mPrimaryStage.show();
    }

    private void viewFriendClicked() {
    }

    private void addFriendClicked() {
    }

    private void adjustMainView() {
        if(!isViewLargest()){
            mPrimaryStage.setWidth(900);
            mExtendView.setText("<<");
        }else{
            mPrimaryStage.setWidth(510);
            mExtendView.setText(">>");
            mSelectedPerson = null;
            mPane.getChildren().remove(mFriends);
        }
    }

    private boolean isViewLargest(){
        if(mPrimaryStage.getWidth() >= 900){
            return true;
        }
        return false;
    }

    private void deleteClicked() throws SQLException, ClassNotFoundException {
        if(mSelectedPerson != null) {
            mData.removeFromList(mSelectedPerson);
        }
        saveClicked();
    }

    private void editClicked() throws SQLException, ClassNotFoundException {
        if(mSelectedPerson != null) {
            String name = "";
            String location = "";
            if (mNameInput.getText().length() < 1) {
                name = mSelectedPerson.getName();
            } else {
                name = mNameInput.getText();
            }
            if (mLocationInput.getText().length() < 1) {
                location = mSelectedPerson.getLocation();
            } else {
                location = mLocationInput.getText();
            }
            String firstName = makeSubNames(name)[0];
            String lastName = makeSubNames(name)[1];
            mData.editFriend(new Person(firstName, lastName, location, mSelectedPerson.getID()), mSelectedPerson);
            mLocationInput.clear();
            mNameInput.clear();
            saveClicked();
        }
        mSelectedPerson = null;
    }

    private String[] makeSubNames(String inputString){
        String[] parts = inputString.split(" ", 2);
        return parts;
    }

    public void addClicked() throws SQLException, ClassNotFoundException {
        if(mNameInput.getText().length() > 0 && mLocationInput.getText().length() > 0 ) {
            String name = mNameInput.getText();
            String firstName = name.substring(0,name.lastIndexOf(' '));
            String lastName = name.substring(name.lastIndexOf(' ') + 1);
            mData.addToList(new Person(firstName, lastName, mLocationInput.getText()));
            mLocationInput.clear();
            mNameInput.clear();
            saveClicked();
        }
    }

    public void saveClicked() throws SQLException, ClassNotFoundException {
        mData.saveFriendsToDisk();
        mTable.setItems(mData.getFriends());
    }
}
