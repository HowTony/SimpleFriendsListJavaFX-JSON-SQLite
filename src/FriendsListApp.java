import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Tony Howarth on 3/14/2017.
 * Removed all logic from main.
 */
public class FriendsListApp extends Application {

    private TableView<Person> mUsersTable, mFriendsTable;
    private TextField mFirstNameField, mLastNameField, mAddressField;
    private ObservableList<Person> mObsList, mPossibleFriends;
    private Database mData;
    private HBox mHorizontalButtonBox;
    private HBox mHorizontalTopBox;
    private VBox mMainVertBox;
    private VBox mFriendsListVertBox;
    private Group mRoot;
    private Person mPrimarySelectedPerson, mSecondarySelectedPerson;
    private Stage mPrimaryStage;
    private Button mAddOrRemoveFriendsButton, mViewFriendsButton, mExtendView;
    private Boolean mPossibleFriendsShows = true;
    private TableColumn<Person, String> mFriendsListColumn;

    private final String WINDOW_TITLE = "Friends Lists";
    private double mStageMinWidth = 490;
    private double mStageMaxWidth = 850;
    private final double ADD_REMOVE_BUTTTON_WIDTH = 105;
    private final double STAGE_PREF_HEIGHT = 490;
    private final double EXTEND_BUTTON_SCALE = 0.8;
    private final double PREF_PADDING = 8;
    private double USERS_COLUMN_PREF_WIDTH = 200;
    private final double FRIENDS_COLUMN_PREF_WIDTH = 250;
    private final double BUTTON_PREF_HEIGHT = 30;
    private final double NAME_TEXTFIELD_MIN_WIDTH = 75;
    private final double ADDRESS_TEXTFIELD_MIN_WIDTH = 115;
    private final int STRING_NAME_MIN_LENGTH = 1;
    private final int STRING_ADDRESS_MIN_LENGTH = 10;
    private final int MINIMUM_MOUSE_CLICKS = 1;


    @Override
    public void start(Stage primaryStage) {
        mPrimaryStage = primaryStage;
        mRoot = new Group();
        try {
            mData = new Database();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        mObsList = mData.getFriendsInObservableList();
        Scene scene = new Scene(mRoot);
        mPrimaryStage.setWidth(mStageMinWidth);
        mPrimaryStage.setHeight(STAGE_PREF_HEIGHT);
        mPrimaryStage.setTitle(WINDOW_TITLE);

        /**
         * Tables
         */
        mUsersTable = new TableView<>();
        mFriendsTable = new TableView<>();
        mFriendsTable.setPlaceholder(new Label("No Friends Available :("));

        TableColumn<Person, String> firstNameCol = new TableColumn("Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        firstNameCol.setPrefWidth(USERS_COLUMN_PREF_WIDTH);

        TableColumn<Person, String> locationCol = new TableColumn("Location");
        locationCol.setCellValueFactory(new PropertyValueFactory("location"));
        locationCol.setPrefWidth(USERS_COLUMN_PREF_WIDTH);

        //made field variable so you can switch text and objects in the list easily
        mFriendsListColumn = new TableColumn<>("Possible Friends");
        mFriendsListColumn.setCellValueFactory(new PropertyValueFactory("name"));
        mFriendsListColumn.setPrefWidth(FRIENDS_COLUMN_PREF_WIDTH);

        /**
         * input boxes
         */
        mFirstNameField = new TextField();
        mFirstNameField.setPromptText("First Name");
        mFirstNameField.setPrefSize(NAME_TEXTFIELD_MIN_WIDTH, BUTTON_PREF_HEIGHT);

        mLastNameField = new TextField();
        mLastNameField.setPromptText("Last Name");
        mLastNameField.setPrefSize(NAME_TEXTFIELD_MIN_WIDTH, BUTTON_PREF_HEIGHT);

        mAddressField = new TextField();
        mAddressField.setPromptText("Address");
        mAddressField.setPrefSize(ADDRESS_TEXTFIELD_MIN_WIDTH, BUTTON_PREF_HEIGHT);

        /**
         * buttons
         */
        Button addPerson = new Button("Add");
        addPerson.setPrefHeight(BUTTON_PREF_HEIGHT);
        addPerson.setOnAction(event -> {
            try {
                addUserClicked();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        Button editButton = new Button("Edit");
        editButton.setPrefHeight(BUTTON_PREF_HEIGHT);
        editButton.setOnAction(event -> {
            try {
                editUserClicked();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setPrefHeight(BUTTON_PREF_HEIGHT);
        deleteButton.setOnAction(event -> {
            try {
                deleteUserClicked();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        /**
         * made buttons field variables to easily adjust button text
         */
        mAddOrRemoveFriendsButton = new Button("Add Friend");
        mAddOrRemoveFriendsButton.setPrefHeight(BUTTON_PREF_HEIGHT);
        mAddOrRemoveFriendsButton.setPrefWidth(ADD_REMOVE_BUTTTON_WIDTH);
        mAddOrRemoveFriendsButton.setOnAction(event -> {
                try {
                    friendModifierClicked();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
        });

        mViewFriendsButton = new Button("View Friends");
        mViewFriendsButton.setPrefHeight(BUTTON_PREF_HEIGHT);
        mViewFriendsButton.setPrefWidth(ADD_REMOVE_BUTTTON_WIDTH);
        mViewFriendsButton.setOnAction(event -> {
            try {
                viewFriendClicked();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        mExtendView = new Button(">>");
        mExtendView.setOnAction(event -> adjustMainView());
        mExtendView.setScaleX(EXTEND_BUTTON_SCALE);
        mExtendView.setScaleY(EXTEND_BUTTON_SCALE);
        mHorizontalButtonBox = new HBox();
        mHorizontalButtonBox.getChildren().addAll(mFirstNameField, mLastNameField, mAddressField, addPerson, editButton, deleteButton);
        mHorizontalButtonBox.setSpacing(PREF_PADDING);
        mHorizontalTopBox = new HBox();
        mHorizontalTopBox.setSpacing(PREF_PADDING);
        mFriendsListVertBox = new VBox();
        mFriendsListVertBox.setSpacing(PREF_PADDING);
        mFriendsListVertBox.getChildren().addAll(mAddOrRemoveFriendsButton, mViewFriendsButton);
        mHorizontalTopBox.getChildren().addAll(mUsersTable, mExtendView);
        mMainVertBox = new VBox();
        mMainVertBox.setSpacing(PREF_PADDING);
        mMainVertBox.setPadding(new Insets(PREF_PADDING, PREF_PADDING, PREF_PADDING, PREF_PADDING));
        mMainVertBox.getChildren().addAll(mHorizontalTopBox, mHorizontalButtonBox);
        mUsersTable.setItems(mObsList);
        mUsersTable.getColumns().addAll(firstNameCol, locationCol);
        mFriendsTable.getColumns().addAll(mFriendsListColumn);


        mUsersTable.setRowFactory(tv -> {
            TableRow<Person> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == MINIMUM_MOUSE_CLICKS && (!row.isEmpty())) {
                    mPrimarySelectedPerson = row.getItem();
                    try {
                        loadSideList();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });

        mFriendsTable.setRowFactory(tv -> {
            TableRow<Person> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == MINIMUM_MOUSE_CLICKS && (!row.isEmpty())) {
                    mSecondarySelectedPerson = row.getItem();
                }
            });
            return row;
        });

        ((Group) scene.getRoot()).getChildren().addAll(mMainVertBox);
        mPrimaryStage.setScene(scene);
        mPrimaryStage.show();
    }

    private void adjustMainView() {
        if (!isViewLargest()) {
            mPrimaryStage.setWidth(mStageMaxWidth);
            mExtendView.setText("<<");
            mHorizontalTopBox.getChildren().addAll(mFriendsTable, mFriendsListVertBox);
        } else {
            mPrimaryStage.setWidth(mStageMinWidth);
            mExtendView.setText(">>");
            mHorizontalTopBox.getChildren().removeAll(mFriendsTable, mFriendsListVertBox);
        }
    }

    private boolean isViewLargest() {
        if (mPrimaryStage.getWidth() >= mStageMaxWidth) {
            return true;
        }
        return false;
    }

    private void deleteUserClicked() throws SQLException, ClassNotFoundException {
        if (mPrimarySelectedPerson != null) {
            mData.removeFromList(mPrimarySelectedPerson);
        }
        saveStateOfLists();
    }

    /**
     * edits the selected entry as long as the length of the edit is greated than our string minimum length.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void editUserClicked() throws SQLException, ClassNotFoundException {
        if (mPrimarySelectedPerson != null) {
            String firstName;
            String lastName;
            String location;
            if (mFirstNameField.getText().length() < STRING_NAME_MIN_LENGTH) {
                firstName = mPrimarySelectedPerson.getFirstName();
            } else {
                firstName = mFirstNameField.getText().trim();
            }
            if( mLastNameField.getText().length() < STRING_NAME_MIN_LENGTH){
                lastName = mPrimarySelectedPerson.getLastName();
            }else{
                lastName = mLastNameField.getText().trim();
            }
            if (mAddressField.getText().length() < STRING_ADDRESS_MIN_LENGTH) {
                location = mPrimarySelectedPerson.getLocation();
            } else {
                location = mAddressField.getText().trim();
            }

            mData.editFriend(new Person(firstName, lastName, location, mPrimarySelectedPerson.getID()), mPrimarySelectedPerson);
            mAddressField.clear();
            mFirstNameField.clear();
            mLastNameField.clear();
            saveStateOfLists();
        }
    }

    public void addUserClicked() throws SQLException, ClassNotFoundException {
        if (mFirstNameField.getText().length() > STRING_NAME_MIN_LENGTH && mAddressField.getText().length() > STRING_ADDRESS_MIN_LENGTH) {
            Person createdPerson = new Person(mFirstNameField.getText().trim(), mLastNameField.getText().trim(), mAddressField.getText().trim());
            mAddressField.clear();
            mFirstNameField.clear();
            mLastNameField.clear();
            mData.addToList(createdPerson);
            saveStateOfLists();
            loadSideList();
        }
    }

    public void saveStateOfLists() throws SQLException, ClassNotFoundException {
        mData.saveFriendsToDisk();
        mUsersTable.setItems(mData.getFriendsInObservableList());
    }

    public void loadSideList() throws SQLException, ClassNotFoundException {
        if(mPrimarySelectedPerson != null) {
            if (!mPossibleFriendsShows) {
                mPossibleFriends = FXCollections.observableArrayList(getFriendsList());
            } else {
                mPossibleFriends = FXCollections.observableArrayList(mObsList);
                mPossibleFriends.remove(mPrimarySelectedPerson);
                ObservableList<Person> testCurrentFriends = getFriendsList();
                mPossibleFriends.removeAll(testCurrentFriends);
            }
            mFriendsTable.setItems(mPossibleFriends);
            mFriendsTable.refresh();
        }
    }

    private void viewFriendClicked() throws SQLException, ClassNotFoundException {
        if(mPossibleFriendsShows) {
            mViewFriendsButton.setText("Possible Friends");
            mFriendsListColumn.setText("Friends");
            mAddOrRemoveFriendsButton.setText("Remove Friend");
            mPossibleFriendsShows = false;
        }else {
            mViewFriendsButton.setText("View Friends");
            mFriendsListColumn.setText("Possible Friends");
            mAddOrRemoveFriendsButton.setText("Add Friend");
            mPossibleFriendsShows = true;
        }
        mPossibleFriends = null;
        loadSideList();
    }

    private void friendModifierClicked() throws SQLException, ClassNotFoundException {
        if(!mPossibleFriendsShows) {
           removeFriendClicked();
        }else {
          addFriendClicked();
        }
        loadSideList();
    }

    private void addFriendClicked() throws SQLException, ClassNotFoundException {
        if(mSecondarySelectedPerson != null) {
            mData.getQueriedData().getSQLManager().addFriends(mPrimarySelectedPerson.getID(), mSecondarySelectedPerson.getID());
        }
    }

    private void removeFriendClicked() throws SQLException, ClassNotFoundException {
        if(mSecondarySelectedPerson != null) {
            mData.getQueriedData().getSQLManager().removeFriend(mPrimarySelectedPerson.getID(), mSecondarySelectedPerson.getID());
        }
    }

    public ObservableList<Person> getFriendsList() throws SQLException, ClassNotFoundException {
        List<Integer> friendIDs = mData.getQueriedData().getSQLManager().getFriends(mPrimarySelectedPerson.getID());
        ObservableList<Person> peopleList = FXCollections.observableArrayList();
        for (Integer eachInt: friendIDs) {
            Person eachPerson = mData.getPerson(eachInt);
            if(eachPerson != null) {
                peopleList.add(eachPerson);
            }
        }
        return  peopleList;
    }
}