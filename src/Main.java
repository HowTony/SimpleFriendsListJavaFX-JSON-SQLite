import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;


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

    @Override
    public void start(Stage primaryStage){

        mTable.setEditable(true);
        mData.loadFriendsFromFile();
        mObsList = mData.getFriends();

        Scene scene = new Scene(new Group());
        primaryStage.setWidth(445);
        primaryStage.setHeight(500);

        TableColumn<Friend, String> firstNameCol = new TableColumn("Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        firstNameCol.setMinWidth(200);

        TableColumn<Friend, String> locationCol = new TableColumn("Location");
        locationCol.setCellValueFactory(new PropertyValueFactory("location"));
        locationCol.setMinWidth(200);

        mTable.setItems(mObsList);
        mTable.getColumns().addAll(firstNameCol, locationCol);

        //inputs
        mNameInput = new TextField();
        mNameInput.setPromptText("Name");
        mNameInput.setMinSize(155, 30);

        mLocationInput = new TextField();
        mLocationInput.setPromptText("Location");
        mLocationInput.setMinSize(155, 30);

        Button addButton = new Button("Add");
        addButton.setOnAction(event -> addClicked());

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> saveClicked());

        mBox.getChildren().addAll( mNameInput, mLocationInput, addButton, saveButton);
        mBox.setSpacing(3);

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(mTable, mBox);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void addClicked(){
        mData.addToList(new Friend(mNameInput.getText(), mLocationInput.getText()));
        mLocationInput.clear();
        mNameInput.clear();
        mTable.setItems(mData.getFriends());
    }

    public void saveClicked(){
        try {
            mData.saveFriendsToDisk();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
