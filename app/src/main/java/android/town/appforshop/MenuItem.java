package android.town.appforshop;

public class MenuItem{
    String menuName;
    String menuPrice;
    int resId;
    int no;

    public MenuItem(String menuName,String menuPrice){
        this.menuName=menuName;
        this.menuPrice=menuPrice;
        this.no=0;

    }
    public MenuItem(String menuName,String menuPrice,int resId){
        this.menuName=menuName;
        this.menuPrice=menuPrice;
        this.resId=resId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(String menuPrice) {
        this.menuPrice = menuPrice;
    }

    public int getResId() {
        return resId;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}