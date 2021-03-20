package android.town.appforshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class ShopItemView extends LinearLayout {
    TextView textView1;
    TextView textView2;
    TextView textView3;
    ConstraintLayout layout;

    public ShopItemView(Context context){
        super(context);
        init(context);
    }
    public void init(Context context){

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.shop_item,this,true);

    layout=(ConstraintLayout)findViewById(R.id.cons_layout2);
    textView1=(TextView)findViewById(R.id.textShop1);
    textView2=(TextView)findViewById(R.id.textShop2);
    textView3=(TextView)findViewById(R.id.textShop3);

}
    public void setName(String name){
        textView1.setText("가게명: "+name);
    }
    public void setAllTableNo(int allTableNo){
        textView3.setText("총좌석 수: "+Integer.toString(allTableNo));
    }
    public void setEmptyNo(int emptyNo){
        textView2.setText("빈좌석 수: "+Integer.toString(emptyNo));
    }


}
