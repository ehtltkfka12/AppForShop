package android.town.appforshop;

import androidx.appcompat.app.AppCompatActivity;

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

public class CustomerMainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_MENU = 101;

    private static String IP_ADDRESS = "34.64.190.134";
    private static String TAG = "queryData";

    private static final String TAG_JSON="ehtltkfka12";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMPTY = "empty";
    private static final String TAG_TOTAL ="total";

    String mJsonString;

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

        //서버에서 데이터 가져와 갱신
        QueryData task=new QueryData();
        task.execute("http://" + IP_ADDRESS + "/queryData.php");



        /*adapter.addItem(new ShopItem("a",2,10));
        adapter.addItem(new ShopItem("b",3,20));
        adapter.addItem(new ShopItem("c",10,15));*/

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
    class QueryData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString="이름과 비밀번호가 일치하지 않습니다.";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(CustomerMainActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //Toast.makeText(getApplicationContext(),result+"!!!",Toast.LENGTH_LONG);
                mJsonString = result;
                ArrayList<String> mResult = new ArrayList<>();
                mResult=showResult();
            //Toast.makeText(getApplicationContext(),mResult.get(1),Toast.LENGTH_LONG);
                adapter.addItem(new ShopItem(mResult.get(1),Integer.parseInt(mResult.get(2)),Integer.parseInt(mResult.get(3))));
            adapter.addItem(new ShopItem(mResult.get(5),Integer.parseInt(mResult.get(6)),Integer.parseInt(mResult.get(7))));
            adapter.addItem(new ShopItem(mResult.get(9),Integer.parseInt(mResult.get(10)),Integer.parseInt(mResult.get(11))));
            gridView.setAdapter(adapter);

            Log.d(TAG, "POST response  - " + result);
            Log.d(TAG, "POST response  - " + mResult.get(1)+mResult.get(5)+mResult.get(9));
        }


        @Override
        protected String doInBackground(String... params) {


            String serverURL = (String)params[0];
            String postParameters = "";


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
                String name = item.getString(TAG_NAME);
                String empty=item.getString(TAG_EMPTY);
                String total = item.getString(TAG_TOTAL);


                tmp.add(id);
                tmp.add(name);
                tmp.add(empty);
                tmp.add(total);



            }



        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }
        return tmp;
    }
    }
