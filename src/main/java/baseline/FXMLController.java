package baseline;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;

import javax.swing.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class FXMLController {

    @FXML
    private Button showButton;

    @FXML
    private Button clearButton;

    @FXML
    private TableView<InventoryItem> tableView;

    @FXML
    private TableColumn<InventoryItem, String> nameCol;

    @FXML
    private TableColumn<InventoryItem, String> serialCol;

    @FXML
    private TableColumn<InventoryItem, String> valueCol;

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

    private final ObservableList<InventoryItem> data = FXCollections.observableArrayList();
    private final ObservableList<InventoryItem> search = FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(
                t -> (t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setName(t.getNewValue())
        );
        serialCol.setCellValueFactory(
                new PropertyValueFactory<>("serial")
        );
        serialCol.setCellFactory(TextFieldTableCell.forTableColumn());
        serialCol.setOnEditCommit(
                t -> (t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setSerial(t.getNewValue())
        );
        valueCol.setCellValueFactory(
                new PropertyValueFactory<>("value")
        );
        valueCol.setCellFactory(TextFieldTableCell.forTableColumn());
        valueCol.setOnEditCommit(
                t -> (t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setValue(t.getNewValue())
        );
        tableView.setItems(data);
    }


    @FXML
    void addToList(ActionEvent event) {
        if(validInput(nameField.getText(), serialField.getText(), valueField.getText())){
            addToObservableList(nameField.getText(), serialField.getText(), getMonetaryValue(valueField.getText()));
        } else{
            return;
        }
        errorText.setText("");
        nameField.clear();
        serialField.clear();
        valueField.clear();
    }

    public boolean validInput(String name, String serial, String value){
        return nameValid(name) && valueValid(value) && serialValid(serial);
    }

    private boolean serialInList(String serial){
        for (InventoryItem item: data) {
            if(item.getSerial().equalsIgnoreCase(serial)){
                errorText.setText("Serial number already exists.");
                return true;
            }
        }
        return false;
    }

    public String getMonetaryValue(String input){
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        return nf.format(new BigDecimal(input));
    }

    private boolean nameValid(String input){
        if(input.length() < 2 || input.length() > 256){
            errorText.setText("Name has to be between 2-256 characters in length.");
            return false;
        }
        return true;
    }

    private boolean serialValid(String input) {
        if (!input.matches("[A-Za-z]{1}[-][0-9a-zA-Z]{3}[-][0-9a-zA-Z]{3}[-][0-9a-zA-Z]{3}")) {
            errorText.setText("Serial format: A-XXX-XXX-XXX \n <A is a letter> <X is a letter/number>");
            return false;
        }
        return !serialInList(input);
    }

    private boolean valueValid(String input) {
        double money;
        try
        {
            money = Double.parseDouble(input);
        }
        catch(NumberFormatException e)
        {
            errorText.setText("Value has to be a number.");
            return false;
        }
        if(money >= 0){
            return true;
        }
        else{
            errorText.setText("Value cannot be negative.");
            return false;
        }
    }

    public void addToObservableList(String name, String serial, String value){
        data.add(new InventoryItem(name, serial, value));
    }

    @FXML
    void clearAll(ActionEvent event) {
        data.clear();
    }

    @FXML
    void loadList(ActionEvent event) {

    }

    @FXML
    void removeList(ActionEvent event) {
        data.removeIf(item -> serialField.getText().equals(item.getSerial()));
        search.removeIf(item -> serialField.getText().equals(item.getSerial()));
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
        isInList(nameField.getText(), serialField.getText());
    }

    public boolean isInList(String name, String serial) {
        search.clear();
        boolean flag = false;
        for (InventoryItem item : data) {
            if(item.getSerial().equalsIgnoreCase(serial) || item.getName().equalsIgnoreCase(name)){
                search.add(item);
                flag = true;
            }
        }
        tableView.setItems(search);
        return flag;
    }

    @FXML
    void showAll(ActionEvent event) {
        tableView.setItems(data);
    }

    @FXML
    void userGuide(ActionEvent event) {

    }

}

