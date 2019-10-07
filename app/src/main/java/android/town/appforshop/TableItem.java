package android.town.appforshop;

import java.util.ArrayList;

public class TableItem {
    Boolean isEmpty;
    String tableNo;
    ArrayList<MenuItem> menuItems;
    String totalPrice;
    int noChicken;
    int noPizza;

    public TableItem(Boolean isEmpty,String tableNo){
        this.isEmpty=isEmpty;
        this.tableNo=tableNo;
        this.noChicken=0;
        this.noPizza=0;
        this.totalPrice="0";
    }
    public TableItem(String tableNo,String totalPrice,ArrayList<MenuItem> menuItems){
        this.tableNo=tableNo;
        this.totalPrice=totalPrice;
        this.menuItems=menuItems;
    }

    public int getNoChicken() {
        return noChicken;
    }

    public void setNoChicken(int noChicken) {
        this.noChicken = noChicken;
    }

    public int getNoPizza() {
        return noPizza;
    }

    public void setNoPizza(int noPizza) {
        this.noPizza = noPizza;
    }

    public TableItem(Boolean isEmpty, String tableNo, ArrayList<MenuItem> menuItems){
        this.tableNo=tableNo;
        this.menuItems=menuItems;
        this.isEmpty=isEmpty;
        this.totalPrice="0";
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public Boolean getEmpty() {
        return isEmpty;
    }

    public void setEmpty(Boolean empty) {
        isEmpty = empty;
    }

    public void setMenuItems(ArrayList<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTableNo() {
        return tableNo;
    }

    public ArrayList<MenuItem> getMenuItems(){
        return menuItems;
    }

    public String getTotalPrice() {
        return totalPrice;
    }
    public Boolean getIsEmpty(){return isEmpty;}
}
