package baseline;

import javafx.beans.property.SimpleStringProperty;

public class InventoryItem {
    private SimpleStringProperty name;
    private SimpleStringProperty serial;
    private SimpleStringProperty value;

    public InventoryItem(String name, String serial, String value){
        this.name = new SimpleStringProperty(name);
        this.serial = new SimpleStringProperty(serial);
        this.value = new SimpleStringProperty(value);
    }

    public String getName(){
        return name.get();
    }

    public String getSerial(){
        return serial.get();
    }

    public String getValue(){
        return value.get();
    }

    public void setName(String name){
        this.name.set(name);
    }

    public void setSerial(String serial){
        this.serial.set(serial);
    }

    public void setValue(String value){
        this.value.set(value);
    }
}
