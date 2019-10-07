package android.town.appforshop;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuItemView extends LinearLayout {
    ImageView imageView;
    TextView textView;
    TextView textView2;

    public MenuItemView(Context context){
        super(context);
        init(context);
    }

    public MenuItemView(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.menu_item,this,true);

        textView=(TextView)findViewById(R.id.text);
        textView2=(TextView)findViewById(R.id.text2);
        imageView=(ImageView)findViewById(R.id.imageView);
    }
    public void setImageView(int resId){
        imageView.setImageResource(resId);

    }
    public void setTextView(String menuName){
        textView.setText(menuName);
    }
    public void setTextView2(String menuPrice){
        textView2.setText(menuPrice);
    }
}
