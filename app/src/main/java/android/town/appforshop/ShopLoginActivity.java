package android.town.appforshop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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
import java.util.HashMap;


public class ShopLoginActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "34.64.190.134";
    private static String TAG = "login";

    private static final String TAG_JSON="ehtltkfka12";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMPTY = "empty";
    private static final String TAG_TOTAL ="total";

    private EditText editTextName;
    private EditText editTextPW;
    private TextView mTextViewLogin;
    String mJsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_login);

        editTextName = (EditText)findViewById(R.id.editText_name);
        editTextPW = (EditText)findViewById(R.id.editText_pw);
        mTextViewLogin=(TextView)findViewById(R.id.mTextViewResult);

        Button buttonLogin = (Button)findViewById(R.id.btn_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editTextName.getText().toString();
                String password = editTextPW.getText().toString();

                Login task = new Login();
                task.execute("http://" + IP_ADDRESS + "/loginQuery.php", name,password);


                editTextName.setText("");
                editTextPW.setText("");

            }
        });

    }



    class Login extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        String errorString="이름과 비밀번호가 일치하지 않습니다.";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ShopLoginActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            if (errorString.equals(result)){

                mTextViewLogin.setText(errorString);
            }
            else {

                mJsonString = result;
                ArrayList<String> mResult = new ArrayList<>();
                mResult=showResult();
                //mTextViewLogin.setText(result);
                Intent intent=new Intent(getApplicationContext(),ShopMainActivity.class);
                if(mResult.size()==0){
                    mTextViewLogin.setText(result);
                }
                else{
                    intent.putExtra("name",mResult.get(1));
                    int emptyTable=Integer.parseInt(mResult.get(2));
                    intent.putExtra("empty",emptyTable);
                    int totalTable=Integer.parseInt(mResult.get(3));
                    intent.putExtra("total",totalTable);
                    startActivity(intent);
                }

            }
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String name = (String)params[1];
            String password = (String)params[2];

            String serverURL = (String)params[0];
            String postParameters = "name=" + name + "&password=" + password;


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