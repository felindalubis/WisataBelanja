package com.example.felindalubis.wisatabelanjagdss;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.felindalubis.wisatabelanjagdss.Adapter.BobotAdapter;
import com.example.felindalubis.wisatabelanjagdss.Adapter.ListAdapter;
import com.example.felindalubis.wisatabelanjagdss.Model.BobotModel;
import com.example.felindalubis.wisatabelanjagdss.Model.WisbelModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class BobotUser extends AppCompatActivity {
    String json_data;
    JSONObject jsonObject;
    JSONArray jsonArray;
    RecyclerView rv_list;
    TextView tv_namarv;
    int jmlUser;
    String nama_grup;
    ArrayList<BobotModel> bobot = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_belanja);
        Intent i = getIntent();
        jmlUser = i.getExtras().getInt("Jml_user");
        nama_grup = i.getExtras().getString("nama_grup");
        Log.d("User", "User: "+jmlUser);
        rv_list = (RecyclerView)findViewById(R.id.rv_list);
        tv_namarv = (TextView) findViewById(R.id.tv_namarv);
        BobotAdapter adapter = new BobotAdapter(BobotUser.this, bobot);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BobotUser.this);
        rv_list.setLayoutManager(linearLayoutManager);
        rv_list.setAdapter(adapter);
        new BackgroundTask().execute(String.valueOf(jmlUser), String.valueOf(nama_grup));
    }

    class BackgroundTask extends AsyncTask<String,Void,String> {
        String json_url;
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(BobotUser.this, null, "Please wait...");
            dialog.show();
                json_url = "https://wisatabelanja.000webhostapp.com/get_dataBobot.php";
                tv_namarv.setText("Preferences");

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String user = params[0];
                String nama_grup = params[1];
                URL url = new URL("https://wisatabelanja.000webhostapp.com/get_dataBobot.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8") + "&"
                        + URLEncoder.encode("nama_grup", "UTF-8") + "=" + URLEncoder.encode(nama_grup, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            json_data = result;
            if(json_data==null)
            {
                Toast.makeText(getApplicationContext(), "Please restart app", Toast.LENGTH_LONG).show();
            }
            else
            {
                try {
                    jsonObject = new JSONObject(json_data);
                    jsonArray = jsonObject.getJSONArray("server_response");
                    int count = 0;
                    int rating, jam_operasional, jarak, fasilitas;
                    int id;
                    ArrayList<BobotModel> bobot = new ArrayList<>();

                    while(count<jsonArray.length()){
                        JSONObject JO = jsonArray.getJSONObject(count);
                        id = Integer.valueOf(JO.getString("user"));
                        rating = Integer.valueOf(JO.getString("rating"));
                        fasilitas = Integer.valueOf(JO.getString("fasilitas"));
                        jarak = Integer.valueOf(JO.getString("jarak"));
                        jam_operasional = Integer.valueOf(JO.getString("operasional"));
                        if(rating==1){
                            rating=0;
                        }
                        if(fasilitas==1){
                            fasilitas=0;
                        }
                        if(jarak==1){
                            jarak=0;
                        }
                        if(jam_operasional==1){
                            jam_operasional=0;
                        }
                        bobot.add(new BobotModel(id, rating, fasilitas, jarak, jam_operasional));
                        count++;
                    }
                    Log.d("Hasil", "hasil: "+result);
                    BobotAdapter adapter = new BobotAdapter(BobotUser.this, bobot);
                    rv_list.setLayoutManager(new LinearLayoutManager(BobotUser.this));
                    rv_list.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}

