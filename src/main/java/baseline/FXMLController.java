package baseline;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class FXMLController {

    @FXML
    private Button showButton;

    @FXML
    private Button clearButton;

    @FXML
    private TableView<?> tableView;

    @FXML
    private TableColumn<?, ?> nameCol;

    @FXML
    private TableColumn<?, ?> serialCol;

    @FXML
    private TableColumn<?, ?> valueCol;

    @FXML
    private TextField nameField;

    @FXML
    private TextField serialField;

    @FXML
    private TextField valueField;

    @FXML
    private Button addButton;

    @FXML
    private Button removeButton;

    @FXML
    private Button searchButton;

    @FXML
    private Text errorText;

    @FXML
    private Button saveTSVButton;

    @FXML
    private Button saveHTMLButton;

    @FXML
    private Button saveJSONButton;

    @FXML
    private Button loadButton;

    @FXML
    void addToList(ActionEvent event) {

    }

    @FXML
    void clearAll(ActionEvent event) {

    }

    @FXML
    void loadList(ActionEvent event) {

    }

    @FXML
    void removeList(ActionEvent event) {

    }

    @FXML
    void saveListHTML(ActionEvent event) {

    }

    @FXML
    void saveListJSON(ActionEvent event) {

    }

    @FXML
    void saveListTSV(ActionEvent event) {

    }

    @FXML
    void searchList(ActionEvent event) {

    }

    @FXML
    void showAll(ActionEvent event) {

    }

    @FXML
    void userGuide(ActionEvent event) {

    }

}

