package com.example.felindalubis.wisatabelanjagdss;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.felindalubis.wisatabelanjagdss.Adapter.ListAdapter;
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

//implements CompoundButton.OnCheckedChangeListener
public class AturKelompok extends AppCompatActivity {
    Button bt_next;
    EditText et_grup;
    String nama_grup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nama_kelompok);

        bt_next = (Button)findViewById(R.id.bt_next);
        et_grup = (EditText) findViewById(R.id.et_grup);
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nama_grup = et_grup.getText().toString();
                if (nama_grup.equalsIgnoreCase("")){
                    Toast.makeText(AturKelompok.this, "Please input group name", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("Hasil", "nama grup: " + nama_grup);
                    new BackgroundTask().execute(et_grup.getText().toString());
                }
            }
    class BackgroundTask extends AsyncTask<String,Void,String> {
        String json_url;
        String hasil;
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(AturKelompok.this, null, "Checking group name...");
            dialog.show();
            json_url = "https://wisatabelanja.000webhostapp.com/insertGroupName.php";
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String grup = params[0];
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("nama_grup", "UTF-8") + "=" + URLEncoder.encode(grup, "UTF-8");
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
            hasil = result;
            Log.d("Hasil", "hasil: " + hasil);
            if(hasil.equalsIgnoreCase("1")){
                Intent i = new Intent(AturKelompok.this, AturAnggota.class);
                i.putExtra("nama_grup", nama_grup);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                Toast.makeText(AturKelompok.this.getApplicationContext(), "nama grup : "+nama_grup, Toast.LENGTH_SHORT).show();
            } else {
                dialog.dismiss();
                Toast.makeText(AturKelompok.this, result, Toast.LENGTH_SHORT).show();
            }

        }
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
    }
