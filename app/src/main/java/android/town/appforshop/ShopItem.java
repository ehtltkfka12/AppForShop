package android.town.appforshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ShopItem{
    String name; // 영업점 이름
    int noEmpty; // 빈자리 숫자
    int noAllTable; //총 좌석수

    public ShopItem(String name,int noEmpty,int noAllTable){
        this.name=name;
        this.noEmpty=noEmpty;
        this.noAllTable=noAllTable;
    }

    public int getNoAllTable() {
        return noAllTable;
    }

    public void setNoAllTable(int noAllTable) {
        this.noAllTable = noAllTable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNoEmpty() {
        return noEmpty;
    }

    public void setNoEmpty(int noEmpty) {
        this.noEmpty = noEmpty;
    }
}