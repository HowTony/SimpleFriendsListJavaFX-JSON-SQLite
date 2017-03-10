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
public class Main extends Application {

    public Main() throws SQLException, ClassNotFoundException {
    }

    public static void main(String[] args) {
        launch(args);
    }

    private TableView<Person> mTable = new TableView<>();
    private ObservableList<Person> mObsList;
    private Database mData = new Database();
    private HBox mBox = new HBox();
    private TextField mFirstName, mLastName, mLocationInput;
    private Group mRoot;
    private Person mSelectedPerson = null;
    private Person mSecondarySelectedPerson;
    private Stage mPrimaryStage;
    private Button mExtendView;
    private boolean GSON_ACTIVE = false;
    private TableView<Person> mFriends = new TableView<>();
    private VBox mVBox;
    private Pane mPane;


    @Override
    public void start(Stage primaryStage) {
        mPrimaryStage = primaryStage;
        mRoot = new Group();


        if (GSON_ACTIVE) {
            mData.getQueriedData().loadFriendsFromFile();
        }
        mObsList = mData.getFriends();

        Scene scene = new Scene(mRoot);
        primaryStage.setWidth(500);
        primaryStage.setHeight(500);

        TableColumn<Person, String> firstNameCol = new TableColumn("Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        firstNameCol.setPrefWidth(primaryStage.getWidth() / 2.5);

        TableColumn<Person, String> locationCol = new TableColumn("Location");
        locationCol.setCellValueFactory(new PropertyValueFactory("location"));
        locationCol.setPrefWidth(primaryStage.getWidth() / 2.5);


        TableColumn<Person, String> friendNameCol = new TableColumn<>("Possible Friends");
        friendNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        friendNameCol.setPrefWidth(250);

        //inputs
        mFirstName = new TextField();
        mFirstName.setPromptText("First Name");
        mFirstName.setPrefSize(75, 30);

        mLastName = new TextField();
        mLastName.setPromptText("Last Name");
        mLastName.setPrefSize(75, 30);

        mLocationInput = new TextField();
        mLocationInput.setPromptText("Address");
        mLocationInput.setPrefSize(115, 30);

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
        mBox.getChildren().addAll(mFirstName, mLastName, mLocationInput, addButton, editButton, deleteButton);
        mBox.setSpacing(3);
        mVBox = new VBox();
        mVBox.setSpacing(10);
        mVBox.setPadding(new Insets(10, 10, 10, 10));
        mVBox.getChildren().addAll(mTable, mBox);

        mFriends.setLayoutY(mTable.getLayoutY() + 10);
        mFriends.setLayoutX(mTable.getLayoutX() + 475);

        Button addFriend = new Button("Add Friend");
        addFriend.setOnAction(event -> addFriendClicked());
        Button viewFriends = new Button("View Friends");
        addFriend.setOnAction(event -> viewFriendClicked());

        addFriend.setLayoutX(mFriends.getLayoutX() + (475 / 1.8));
        addFriend.setLayoutY(10);
        addFriend.setPrefWidth(85);
        viewFriends.setLayoutX(mFriends.getLayoutX() + (475 / 1.8));
        viewFriends.setLayoutY(40);
        viewFriends.setPrefWidth(85);
        mTable.setItems(mObsList);
        mTable.getColumns().addAll(firstNameCol, locationCol);

        mFriends.getColumns().addAll(friendNameCol);
        mPane.getChildren().addAll(addFriend, viewFriends);
//        System.out.println("width of view friends button" + viewFriends.getWidth());
//        System.out.println(mSelectedPerson);



        /**
         * Table Selection
         */

        mTable.setRowFactory(tv -> {
            TableRow<Person> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    mSelectedPerson = row.getItem();
                    ObservableList<Person> mPossibleFriends = FXCollections.observableArrayList(mObsList);
                    mPossibleFriends.remove(mSelectedPerson);
                    mFriends.setItems(mPossibleFriends);
                    System.out.println(mSelectedPerson);
                }
            });
            return row;
        });

        mTable.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem != null) {
                mFriends.getSelectionModel().clearSelection();
            }
        });

        mFriends.setRowFactory(tv -> {
            TableRow<Person> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    mSecondarySelectedPerson = row.getItem();
                    System.out.println(mSecondarySelectedPerson);
                }

            });
            return row;
        });

        mFriends.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem != null) {
                mTable.getSelectionModel().clearSelection();
            }
        });

        ((Group) scene.getRoot()).getChildren().addAll(mVBox, mPane);
        mPrimaryStage.setScene(scene);
        mPrimaryStage.show();
    }

    private void viewFriendClicked() {
    }

    private void addFriendClicked() {
    }

    private void adjustMainView() {
        if (!isViewLargest()) {
            mPrimaryStage.setWidth(900);
            mExtendView.setText("<<");
            mPane.getChildren().add(mFriends);

        } else {
            mPrimaryStage.setWidth(500);
            mExtendView.setText(">>");
            mPane.getChildren().remove(mFriends);
        }
    }

    private boolean isViewLargest() {
        if (mPrimaryStage.getWidth() >= 900) {
            return true;
        }
        return false;
    }

    private void deleteClicked() throws SQLException, ClassNotFoundException {
        if (mSelectedPerson != null) {
            mData.removeFromList(mSelectedPerson);
        }
        saveClicked();
    }

    private void editClicked() throws SQLException, ClassNotFoundException {
        if (mSelectedPerson != null) {
            String firstName = "";
            String lastName = "";
            String location = "";
            if (mFirstName.getText().length() < 1) {
                firstName = mSelectedPerson.getFirstName();
            } else {
                firstName = mFirstName.getText();
            }
            if( mLastName.getText().length() < 1){
                lastName = mSelectedPerson.getLastName();
            }else{
                lastName = mLastName.getText();
            }
            if (mLocationInput.getText().length() < 10) {
                location = mSelectedPerson.getLocation();
            } else {
                location = mLocationInput.getText();
            }

            mData.editFriend(new Person(firstName, lastName, location, mSelectedPerson.getID()), mSelectedPerson);
            mLocationInput.clear();
            mFirstName.clear();
            mLastName.clear();
            saveClicked();
        }

    }


    public void addClicked() throws SQLException, ClassNotFoundException {
        if (mFirstName.getText().length() > 1 && mLocationInput.getText().length() > 10) {
            Person createdPerson = new Person(mFirstName.getText(), mLastName.getText(), mLocationInput.getText());
            mLocationInput.clear();
            mFirstName.clear();
            mLastName.clear();
            mData.addToList(createdPerson);
            saveClicked(createdPerson);

        }
    }

//    private void updateCreatedID(Person createdPerson) {
//        try {
//            createdPerson.setID(mData.getQueriedData().getmSQLManager().getUserID(createdPerson.getLocation()));
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        System.out.println("new persons id"+createdPerson.getID());
//    }

    public void saveClicked() throws SQLException, ClassNotFoundException {
        mData.saveFriendsToDisk();
        mTable.setItems(mData.getFriends());
    }

    public void saveClicked(Person person) throws SQLException, ClassNotFoundException {
        mData.saveFriendsToDisk();
        //updateCreatedID(person);
        mTable.setItems(mData.getFriends());
    }

    public void refreshTableData(){

    }
}
