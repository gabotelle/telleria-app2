package baseline;

import com.google.gson.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Scanner;

public class FXMLController {

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
    private Text errorText;

    private final ObservableList<InventoryItem> data = FXCollections.observableArrayList();
    private final ObservableList<InventoryItem> search = FXCollections.observableArrayList();


    //make cells editable
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


    //validate input
    //add valid item to list
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

    //validate name, serial, and value input
    public boolean validInput(String name, String serial, String value){
        return nameValid(name) && valueValid(value) && serialValid(serial);
    }

    //make sure serial is unique
    private boolean serialInList(String serial){
        for (InventoryItem item: data) {
            if(item.getSerial().equalsIgnoreCase(serial)){
                errorText.setText("Serial number already exists.");
                return true;
            }
        }
        return false;
    }

    //make value input into monetary value
    public String getMonetaryValue(String input){
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        return nf.format(new BigDecimal(input));
    }

    //make sure name is between 2-256 characters
    private boolean nameValid(String input){
        if(input.length() < 2 || input.length() > 256){
            errorText.setText("Name has to be between 2-256 characters in length.");
            return false;
        }
        return true;
    }

    //make sure serial is in proper format
    private boolean serialValid(String input) {
        if (!input.matches("[A-Za-z][-][0-9a-zA-Z]{3}[-][0-9a-zA-Z]{3}[-][0-9a-zA-Z]{3}")) {
            errorText.setText("Serial format: A-XXX-XXX-XXX \n <A is a letter> <X is a letter/number>");
            return false;
        }
        return !serialInList(input);
    }

    //make sure value input is valid number
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

    //add item to observable list
    public void addToObservableList(String name, String serial, String value){
        data.add(new InventoryItem(name, serial, value));
    }

    //remove all items
    @FXML
    void clearAll(ActionEvent event) {
        data.clear();
    }

    //load an HTML file into inventory list
    @FXML
    void loadHTMLList(ActionEvent event) {
        File file = getFile();
        loadHTMLFile(file);
    }

    //load an HTML file into inventory list
    private void loadHTMLFile(File file) {
        if(file != null){
            try {
                FileInputStream inputFile = new FileInputStream(file);
                Scanner sc = new Scanner(inputFile);
                data.clear();
                search.clear();
                String input = sc.nextLine();
                while(!input.equalsIgnoreCase("</table>")){
                    sc.nextLine();
                    input = sc.nextLine();
                    String[] item = new String[3];
                    for (int i = 0; i < 3; i++) {
                        int end = input.indexOf("</td>");
                        item[i] = input.substring(12, end);
                        input = sc.nextLine();
                    }
                    addToObservableList(item[0],item[1], item[2]);
                }
                sc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //load a TSV file into inventory list
    @FXML
    void loadTSVList(ActionEvent event) {
        File file = getFile();
        loadTSVFile(file);
    }

    //load a JSON file into inventory list
    @FXML
    void loadJsonList(ActionEvent event) {
        File file = getFile();
        loadJsonFile(file);
    }

    //load a JSON file into inventory list
    public void loadJsonFile(File file){
        JsonArray items = new JsonArray();
        try {
            JsonObject json = (JsonObject) JsonParser.parseReader(new FileReader(file));
            data.clear();
            items =  (JsonArray)json.get("items");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (JsonElement item: items){
                addToObservableList(item.getAsJsonObject().get("name").getAsString(),
                        item.getAsJsonObject().get("serial").getAsString(),
                        item.getAsJsonObject().get("value").getAsString());
        }
    }

    //load a TSV file into inventory list
    public void loadTSVFile(File file) {
        if (file != null) {
            try {
                FileInputStream inputFile = new FileInputStream(file);
                Scanner sc = new Scanner(inputFile);
                data.clear();
                search.clear();
                String[] input = sc.nextLine().split(" {4}");
                for(int i = 0; i < input.length; i += 3){
                    addToObservableList(input[i], input[i+1], input[i+2]);
                }
                sc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            errorText.setText("File does not exist");
        }
    }

    //remove item by serial number
    @FXML
    void removeList(ActionEvent event) {
        data.removeIf(item -> serialField.getText().equals(item.getSerial()));
        search.removeIf(item -> serialField.getText().equals(item.getSerial()));
    }

    //save list into HTML
    @FXML
    void saveListHTML(ActionEvent event) {
        File file = getFile();
        writeSaveHTMLFile(file);
    }

    //save list into HTML
    private void writeSaveHTMLFile(File file) {
        if (file != null) {
            try {
                PrintWriter writer;
                writer = new PrintWriter(file);
                StringBuilder saveText = new StringBuilder();
                saveText.append("<table>\n");
                for (InventoryItem item: data) {

                    saveText.append("    <tr>\n        <td>").append(item.getName()).append("</td>\n        <td>").append(item.getSerial())
                            .append("</td>\n        <td>").append(item.getValue()).append("</td>\n    </tr>\n");
                }
                saveText.append("</table>");
                writer.println(saveText);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            errorText.setText("File does not exist");
        }
    }

    //save list into JSON
    @FXML
    void saveListJSON(ActionEvent event) {
        File file = getFile();
        writeSaveJsonFile(file);
    }

    //save list into JSON
    public void writeSaveJsonFile(File file){
        if (file != null) {
            try {
                PrintWriter writer;
                writer = new PrintWriter(file);
                StringBuilder saveText = new StringBuilder();
                saveText.append("{\n  \"items\" : [\n");
                boolean first = true;
                for (InventoryItem item: data) {
                    if(!first){
                        saveText.append(",\n");
                    }
                    first = false;
                    saveText.append("    {\"name\" : \"").append(item.getName()).append("\", \"serial\" : \"").append(item.getSerial())
                            .append("\", \"value\" : \"").append(item.getValue()).append("\" }");
                }
                saveText.append("\n  ]\n}");
                writer.println(saveText);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            errorText.setText("File does not exist");
        }
    }

    //save list into TSV
    @FXML
    void saveListTSV(ActionEvent event) {
        File file  = getFile();
        writeSaveTSVFile(file);
    }

    //save list into TSV
    public void writeSaveTSVFile(File file) {
        if (file != null) {
            try {
                PrintWriter writer;
                writer = new PrintWriter(file);
                StringBuilder saveText = new StringBuilder();
                for (InventoryItem item: data) {
                    saveText.append(item.getName()).append("    ").append(item.getSerial())
                            .append("    ").append(item.getValue()).append("    ");
                }
                writer.println(saveText);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            errorText.setText("File does not exist");
        }
    }

    //search list by name or serial
    @FXML
    void searchList(ActionEvent event) {
        isInList(nameField.getText(), serialField.getText());
    }

    //search list by name or serial
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

    //show all items on list
    @FXML
    void showAll(ActionEvent event) {
        tableView.setItems(data);
    }

    //show user guide
    @FXML
    void userGuide(ActionEvent event) {
        Image picture = new Image("userGuide.png");
        ImageView iv = new ImageView();
        iv.setImage(picture);

    }

    public ObservableList<InventoryItem> getObservableList() {
        return data;
    }

    //get a file from the user
    private File getFile(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        return chooser.showOpenDialog(new Stage());
    }
}

