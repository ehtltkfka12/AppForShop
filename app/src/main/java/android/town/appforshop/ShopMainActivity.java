package android.town.appforshop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.IntegerRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
//testtest2222
public class ShopMainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_MENU = 101;

    private static String IP_ADDRESS = "34.64.190.134";
    private static String TAG = "update";
    private static final String TAG_JSON="ehtltkfka12";
    private static final String TAG_ID = "id";
    private static final String TAG_CHICKEN = "chicken";
    private static final String TAG_PIZZA = "pizza";


    String mJsonString;

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
    int totalTable;
    int emptyTable;
    String shopName;

    GridView gridView;
    TableAdapter adapter;
    ArrayList<MenuItem> menuItems;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shop);

        //로그인 페이지로 부터 데이터 받아오는 부분
        Intent intent=getIntent();
        shopName=intent.getStringExtra("name");
        totalTable=intent.getIntExtra("total",0);
        emptyTable=intent.getIntExtra("empty",0);

        gridView = (GridView) findViewById(R.id.gridView);

        adapter = new TableAdapter();
        menuItems = new ArrayList<MenuItem>();
        menuItems.add(new MenuItem("치킨", "18,000원"));
        menuItems.add(new MenuItem("피자", "23,000원"));

        for(int i=1;i<=totalTable;i++){
            adapter.addItem(new TableItem(true,Integer.toString(i),menuItems));
        }


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

        //서버로부터 데이터 받아오는 부분 (주문 존재하는 테이블 불러와서 갱신)
        UpdateDataShop2 task=new UpdateDataShop2();
        task.execute("http://" + IP_ADDRESS + "/queryshop.php",shopName);

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
            if(noChicken==0&&noPizza==0){//결제버튼 눌렀을때
                adapter.getItem(noTable).setEmpty(true);
                //emptyTable++;
            }else {//주문버튼 눌렀을때
                adapter.getItem(noTable).setEmpty(false);
                //emptyTable--;

            }
            boolean isEmpty=adapter.getItem(noTable).getEmpty();
            int totalPrice=18000*noChicken+23000*noPizza;
            adapter.getItem(noTable).setTotalPrice(Integer.toString(totalPrice));
            //adapter.getItem(noTable).setTotalPrice(Integer.toString(totalPrice));
            int count=0;
            for(int i=0;i<totalTable;i++){
                if(adapter.getItem(i).isEmpty){
                    count ++;
                }
            }
            emptyTable=count;
            UpdateDataShop task=new UpdateDataShop();
            Log.d(TAG,"postParameters:"+Integer.toString(noTable));
            task.execute("http://" + IP_ADDRESS + "/update.php",shopName,Integer.toString(emptyTable)
                    ,Integer.toString(noTable),Integer.toString(noChicken),Integer.toString(noPizza),Boolean.toString(isEmpty));
            Toast.makeText(getApplicationContext(), "빈좌석수:"+ Integer.toString(emptyTable), Toast.LENGTH_LONG).show();
            gridView.setAdapter(adapter);



        }
        

    }
    class UpdateDataShop2 extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        String errorString="이름과 비밀번호가 일치하지 않습니다.";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ShopMainActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();


                mJsonString = result;
                ArrayList<String> mResult = new ArrayList<>();
                mResult=showResult();

            for(int i=0;i<mResult.size();i++){
                if(i%3==0){
                    int id=Integer.parseInt(mResult.get(i))-1;
                    adapter.getItem(id).setEmpty(false);
                    adapter.getItem(id).setNoChicken(Integer.parseInt(mResult.get(i+1)));
                    adapter.getItem(id).setNoPizza(Integer.parseInt(mResult.get(i+2)));
                    int totalPrice=18000*Integer.parseInt(mResult.get(i+1))+23000*Integer.parseInt(mResult.get(i+2));
                    adapter.getItem(id).setTotalPrice(Integer.toString(totalPrice));
                    gridView.setAdapter(adapter);
                }
            }



            Log.d(TAG, "POST response  - " + result);
          //  Log.d(TAG, "POST response  - " + mResult.get(0)+mResult.get(1));
        }


        @Override
        protected String doInBackground(String... params) {

            String name = (String)params[1];

            String serverURL = (String)params[0];
            String postParameters = "name=" + name;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return null;
                //return new String("Error: " + e.getMessage());
            }

        }
    }
    private ArrayList<String> showResult(){
        ArrayList<String> tmp=new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String id = item.getString(TAG_ID);
                String chicken = item.getString(TAG_CHICKEN);
                String pizza=item.getString(TAG_PIZZA);



                tmp.add(id);
                tmp.add(chicken);
                tmp.add(pizza);



            }



        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }
        return tmp;
    }

    class UpdateDataShop extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ShopMainActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String shopname=(String)params[1];
            String emptytable = (String)params[2];
            int tmp1=Integer.parseInt((String)params[3])+1;
            String notable=Integer.toString(tmp1);
            String nochicken=(String)params[4];
            String nopizza=(String)params[5];
            String isempty=(String)params[6];

            String serverURL = (String)params[0];
            String postParameters = "shopname="+shopname+ "&emptytable=" + emptytable+"&notable="
                    +notable+"&nochicken="+nochicken+"&nopizza="+nopizza+"&isempty="+isempty;
            Log.d(TAG,"postParameters: "+postParameters);

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }



}

