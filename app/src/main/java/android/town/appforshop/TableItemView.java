package android.town.appforshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class TableItemView extends LinearLayout {
    TextView textView;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    TextView textView6;
    TextView textView7;
    TextView textView8;
    ConstraintLayout layout;

    public TableItemView(Context context){
        super(context);
        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.table_item,this,true);

        layout=(ConstraintLayout)findViewById(R.id.cons_layout);
        textView=(TextView)findViewById(R.id.textView);
        textView2=(TextView)findViewById(R.id.textView2);
        textView3=(TextView)findViewById(R.id.textView3);
        textView4=(TextView)findViewById(R.id.textView4);
        textView5=(TextView)findViewById(R.id.textView5);
        textView6=(TextView)findViewById(R.id.textView6);
        textView7=(TextView)findViewById(R.id.textView7);
        textView8=(TextView)findViewById(R.id.textView8);
    }

    public void setTableNo(String tableNo){
        textView.setText(tableNo);
    }

    public void setMenu(ArrayList<MenuItem> menuItems,TableItem item){
        if(item.getNoChicken()!=0){
        textView2.setText(menuItems.get(0).menuName);
        textView3.setText(menuItems.get(0).menuPrice);
        textView4.setText("수량: "+Integer.toString(item.getNoChicken()));
        }
        if(item.getNoPizza()!=0){
        textView5.setText(menuItems.get(1).menuName);
        textView6.setText(menuItems.get(1).menuPrice);
        textView7.setText("수량: "+Integer.toString(item.getNoPizza()));
        }
    }

    public void setIsEmpty(Boolean isEmpty) {
        if (isEmpty == true) {
            layout.setBackgroundResource(R.color.gray);
        }
    }
    public void setTotalPrice(String totalPrice){

        textView8.setText("총합: "+totalPrice+"원");
    }
}
