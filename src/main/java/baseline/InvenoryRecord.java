package baseline;

record  InventoryRecord(String name, String serial, String value){
    public InventoryItem toInventoryItem(){
        return new InventoryItem(name, serial, value);
    }
}
