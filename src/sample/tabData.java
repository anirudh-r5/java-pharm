package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

class tabData{
    SimpleIntegerProperty sl=new SimpleIntegerProperty();
    SimpleIntegerProperty price=new SimpleIntegerProperty();
    SimpleIntegerProperty qty=new SimpleIntegerProperty();
    SimpleIntegerProperty amt=new SimpleIntegerProperty();
    SimpleIntegerProperty itemID = new SimpleIntegerProperty();
    SimpleStringProperty name= new SimpleStringProperty();

    public int getSl() {
        return sl.get();
    }

    public int getPrice() {
        return price.get();
    }

    public int getQty() {
        return qty.get();
    }

    public int getAmt() {
        return amt.get();
    }

    public int getItemID() {
        return itemID.get();
    }

    public String getName() {
        return name.get();
    }

    public void setSl(int sl) {
        this.sl.set(sl);
    }

    public void setPrice(int price) {
        this.price.set(price);
    }

    public void setQty(int qty) {
        this.qty.set(qty);
    }

    public void setAmt(int amt) {
        this.amt.set(amt);
    }

    public void setItemID(int itemID) {
        this.itemID.set(itemID);
    }

    public void setName(String name) {
        this.name.set(name);
    }
}

