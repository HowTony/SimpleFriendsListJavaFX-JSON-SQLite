import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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
    private boolean GSON_ACTIVE = false;

    @Override
    public void start(Stage primaryStage){
        mRoot = new Group();
        mTable.setEditable(true);

        if(GSON_ACTIVE) {
            mData.getQueriedData().loadFriendsFromFile();
        }
        mObsList = mData.getFriends();

        Scene scene = new Scene(mRoot);
        primaryStage.setWidth(475);
        primaryStage.setHeight(500);

        TableColumn<Person, String> firstNameCol = new TableColumn("Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        firstNameCol.setMinWidth(primaryStage.getWidth() / 2.2);

        TableColumn<Person, String> locationCol = new TableColumn("Location");
        locationCol.setCellValueFactory(new PropertyValueFactory("location"));
        locationCol.setMinWidth(primaryStage.getWidth() / 2.2);

        mTable.setItems(mObsList);
        mTable.getColumns().addAll(firstNameCol, locationCol);

        mTable.setRowFactory(tv -> {
                    TableRow<Person> row = new TableRow<>();
        row.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && (!row.isEmpty())) {
                mSelectedPerson = row.getItem();
            }else if(event.getClickCount() == 2 && (!row.isEmpty())){
                StackPane pane = new StackPane();
                Scene scene1 = new Scene(pane);
                Stage stage = new Stage();
                stage.setScene(scene1);
                pane.getChildren().add(
                        new TextField(mSelectedPerson.getFirstName()));
                stage.show();
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
