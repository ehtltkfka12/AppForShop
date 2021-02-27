package android.town.appforshop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
//testtest2
public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_MENU = 101;

    class TableAdapter extends BaseAdapter {
        ArrayList<TableItem> items = new ArrayList<>();

        public void addItem(TableItem item) {
            items.add(item);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public TableItem getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TableItemView view = new TableItemView(getApplicationContext());
            TableItem item = items.get(position);
            view.setTableNo(item.getTableNo());
            view.setTotalPrice(item.getTotalPrice());

            if(item.getMenuItems()!=null) {
                view.setMenu(item.getMenuItems(), item);
            }
            view.setIsEmpty(item.getIsEmpty());
            return view;
        }
    }


    int noTable;
    GridView gridView;
    TableAdapter adapter;
    ArrayList<MenuItem> menuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridView);

        adapter = new TableAdapter();
        menuItems = new ArrayList<MenuItem>();
        menuItems.add(new MenuItem("치킨", "18,000원"));
        menuItems.add(new MenuItem("피자", "23,000원"));

        adapter.addItem(new TableItem(true, "1",menuItems));
        adapter.addItem(new TableItem(true, "2",menuItems));
        adapter.addItem(new TableItem(true, "3",menuItems));
        adapter.addItem(new TableItem(true, "4",menuItems));
        adapter.addItem(new TableItem(true, "5",menuItems));
        adapter.addItem(new TableItem(true, "6",menuItems));
        adapter.addItem(new TableItem(true, "7",menuItems));
        adapter.addItem(new TableItem(true, "8",menuItems));
        adapter.addItem(new TableItem(true, "9",menuItems));
        adapter.addItem(new TableItem(true, "10",menuItems));

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TableItem item = (TableItem) adapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), TableActivity.class);
                intent.putExtra("tableNo", position);
                intent.putExtra("noChicken",item.getNoChicken());
                intent.putExtra("noPizza",item.getNoPizza());
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            //테이블 번호, 치킨개수, 피자개수 불러와서 해당 테이블에 저장
            noTable = data.getExtras().getInt("noTable")-1;
            int noChicken=data.getExtras().getInt("noChicken");
            int noPizza=data.getExtras().getInt("noPizza");
            adapter.getItem(noTable).setNoChicken(noChicken);
            adapter.getItem(noTable).setNoPizza(noPizza);


            Toast.makeText(getApplicationContext(),
                    "치킨test"+noChicken+
                            "피자"+noPizza+
                            "테이블"+noTable,Toast.LENGTH_LONG).show();
            if(noChicken==0&&noPizza==0){
                adapter.getItem(noTable).setEmpty(true);
            }else {
                adapter.getItem(noTable).setEmpty(false);

            }
            int totalPrice=18000*noChicken+23000*noPizza;
            adapter.getItem(noTable).setTotalPrice(Integer.toString(totalPrice));
            //adapter.getItem(noTable).setTotalPrice(Integer.toString(totalPrice));
            gridView.setAdapter(adapter);
        }

    }
}

