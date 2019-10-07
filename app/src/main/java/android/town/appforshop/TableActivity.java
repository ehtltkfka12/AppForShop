package android.town.appforshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TableActivity extends AppCompatActivity {
    String[] spinner_items={"==선택==","치킨(18,000원)","피자(23,000원)"};

    class MenuAdaper extends BaseAdapter{
        ArrayList<MenuItem> items=new ArrayList<MenuItem>();

        public void addItem(MenuItem item){
            items.add(item);
        }
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public MenuItem getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MenuItemView view=new MenuItemView(getApplicationContext());
            MenuItem item=items.get(position);
            view.setImageView(item.getResId());
            view.setTextView(item.getMenuName());
            view.setTextView2(item.getMenuPrice());
            return view;
        }
    }

    MenuAdaper menuAdapter;
    ListView listView;
    Button btn1,btn2;
    TextView textTableNo;
    int tableNo;
    int noChicken;
    int noPizza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);


        textTableNo=(TextView)findViewById(R.id.textTableNo);
        listView=(ListView)findViewById(R.id.listView); // 스피너에서 선택된 메뉴 리스트뷰
        btn1=(Button)findViewById(R.id.btn1); // 주문하기
        btn2=(Button)findViewById(R.id.btn2); // 결제하기


        Intent intent=getIntent();
        tableNo=intent.getIntExtra("tableNo",0)+1;
        textTableNo.setText("테이블 번호 : "+tableNo);
        noChicken=intent.getIntExtra("noChicken",0);
        noPizza=intent.getIntExtra("noPizza",0);

        menuAdapter=new MenuAdaper();  // 기존에 테이블에 있던 물품들 리스트뷰에 추가
        /*for(int i=0;i<noChicken;i++){
            menuAdapter.addItem(new MenuItem("치킨", "18,000원", R.drawable.chiken));
        }
        for(int i=0;i<noPizza;i++){
            menuAdapter.addItem(new MenuItem("피자", "23,000원", R.drawable.pizza));
        }*/
        listView.setAdapter(menuAdapter);

        //스피너
        final Spinner spinner=(Spinner)findViewById(R.id.spinner);
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spinner_items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               if(position==0) {

               }
               else if(position==1){
                   menuAdapter.addItem(new MenuItem("치킨", "18,000원", R.drawable.chiken));
                   listView.setAdapter(menuAdapter);
                   spinner.setAdapter(adapter);
               }
                else  {
                   menuAdapter.addItem(new MenuItem("피자", "23,000원", R.drawable.pizza));
                   listView.setAdapter(menuAdapter);
                   spinner.setAdapter(adapter);
               }
                Toast.makeText(getApplicationContext(),"선택"+position,Toast.LENGTH_LONG).show();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();


                for(int i=0;i<menuAdapter.getCount();i++){
                    if(menuAdapter.getItem(i).menuName=="치킨"){
                        noChicken++;
                    }
                    else {
                        noPizza++;
                    }
                }
                Toast.makeText(getApplicationContext(), noChicken+","+noPizza, Toast.LENGTH_LONG).show();
                intent.putExtra("noChicken",noChicken);
                intent.putExtra("noPizza",noPizza);
                intent.putExtra("noTable",tableNo);
                setResult(RESULT_OK,intent);

                finish();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("noTable",tableNo);
                intent.putExtra("noChicken",0);
                intent.putExtra("noPizza",0);
                setResult(RESULT_OK,intent);
                finish();
            }
        });



    }
}
