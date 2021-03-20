package android.town.appforshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.ArrayList;

public class CustomerMainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_MENU = 101;

    class ShopAdapter extends BaseAdapter {
        ArrayList<ShopItem> items = new ArrayList<>();

        public void addItem(ShopItem item) {
            items.add(item);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public ShopItem getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ShopItemView view = new ShopItemView(getApplicationContext());
            ShopItem item = items.get(position);
            view.setName(item.getName());
            view.setEmptyNo(item.getNoEmpty());
            view.setAllTableNo(item.getNoAllTable());

            return view;
        }
    }


    GridView gridView;
    ShopAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        gridView = (GridView) findViewById(R.id.gridView2);

        adapter = new ShopAdapter();

        adapter.addItem(new ShopItem("a",2,10));
        adapter.addItem(new ShopItem("b",3,20));
        adapter.addItem(new ShopItem("c",10,15));


        gridView.setAdapter(adapter);
  /*
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShopItem item = (ShopItem) adapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), TableActivity.class);
                intent.putExtra("tableNo", position);
                intent.putExtra("noChicken",item.getNoChicken());
                intent.putExtra("noPizza",item.getNoPizza());
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });
*/

    }
    }
