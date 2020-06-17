package com.example.felindalubis.wisatabelanjagdss;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.felindalubis.wisatabelanjagdss.Adapter.ListAdapter;
import com.example.felindalubis.wisatabelanjagdss.Adapter.ListRekomendasi;
import com.example.felindalubis.wisatabelanjagdss.Adapter.WisbelAdapter;
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

public class HasilTopsis extends AppCompatActivity implements ListRekomendasi.onItemListener {
    Button bt_Bobot;
    String json_data;
    JSONObject jsonObject;
    JSONArray jsonArray;
    RecyclerView rv_list;
    TextView tv_namarv;
    int jmlUser;
    double lat, lng;
    String nama_grup;
    ArrayList<WisbelModel> wsb = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_belanja);
        Intent i = getIntent();
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        jmlUser = i.getExtras().getInt("Jml_user");
        lat = i.getExtras().getDouble("my_lat");
        lng = i.getExtras().getDouble("my_lng");
        nama_grup = i.getExtras().getString("nama_grup");
        rv_list = (RecyclerView) findViewById(R.id.rv_list);
        tv_namarv = (TextView) findViewById(R.id.tv_namarv);
        tv_namarv.setText("Recommendation");
        ListRekomendasi adapter = new ListRekomendasi(HasilTopsis.this, wsb, HasilTopsis.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HasilTopsis.this);
        rv_list.setLayoutManager(linearLayoutManager);
        rv_list.setAdapter(adapter);
        new BackgroundTask().execute(String.valueOf(jmlUser), String.valueOf(lat), String.valueOf(lng), String.valueOf(nama_grup));
        Log.d("User", "send:" + jmlUser +", "+lat+", "+lng+", "+nama_grup);

        // Liat preferences
        bt_Bobot = (Button)findViewById(R.id.bt_Bobot);
        bt_Bobot.setVisibility(View.VISIBLE);
        bt_Bobot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(HasilTopsis.this, BobotUser.class);
                in.putExtra("Jml_user", jmlUser);
                in.putExtra("nama_grup", nama_grup);
                startActivity(in);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void onItemClick(ArrayList<WisbelModel> data, int position) {
        Intent i = new Intent(this, DeskripsiWisbel.class);
        Log.d("CLICK", "clicked on:" + position);
        Log.d("CLICK", "size:" + data.size());
        Log.d("CLICK", "isi : " + data.get(position));
        Log.d("CLICK", "isi : " + data.get(position).nama_tempat);
        i.putExtra("posisi", position);
        i.putExtra("Wisbel", data);
        i.putExtra("nama", data.get(position).nama_tempat);
        i.putExtra("alamat", data.get(position).alamat);
        i.putExtra("jam_operasional", data.get(position).jam_buka + " - " + data.get(position).jam_tutup);
        i.putExtra("foto", data.get(position).foto);
        i.putExtra("lat", data.get(position).lat);
        i.putExtra("lng", data.get(position).lng);
        i.putExtra("rate", data.get(position).rating);
        i.putExtra("jarak", data.get(position).jarak);
        i.putExtra("fasilitas", data.get(position).fasilitas);
        i.putExtra("jenis", data.get(position).jenis);
        startActivity(i);
    }

    class BackgroundTask extends AsyncTask<String, Void, String> {
        String json_url;
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            json_url = "https://wisatabelanja.000webhostapp.com/count.php";
            dialog = ProgressDialog.show(HasilTopsis.this, null, "Please wait...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String user = params[0];
                String my_lat = params[1];
                String my_lng = params[2];
                String nama_grup = params[3];
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8") + "&"
                        + URLEncoder.encode("my_lat", "UTF-8") + "=" + URLEncoder.encode(my_lat, "UTF-8") + "&"
                        + URLEncoder.encode("my_lng", "UTF-8") + "=" + URLEncoder.encode(my_lng, "UTF-8") + "&"
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
            Log.d("Hasil", "hasil: " + result);
            if (json_data == null) {
                Toast.makeText(getApplicationContext(), "Please restart app", Toast.LENGTH_LONG).show();
            } else {
                try {
                    jsonObject = new JSONObject(json_data);
                    jsonArray = jsonObject.getJSONArray("server_response");
                    int count = 0;
                    String nama_tempat, jenis, alamat, jam_buka, jam_tutup, foto, fasilitas;
                    double rating, jam_operasional, lat, lng, jarak;
                    int id, jml_fasilitas;
                    ArrayList<WisbelModel> wsb = new ArrayList<>();

                    while (count < jsonArray.length()) {
                        JSONObject JO = jsonArray.getJSONObject(count);
                        id = Integer.valueOf(JO.getString("ID_tempat"));
                        nama_tempat = JO.getString("nama_tempat");
                        rating = Double.valueOf(JO.getString("rating"));
                        jam_buka = JO.getString("jam_buka");
                        jam_tutup = JO.getString("jam_tutup");
                        jml_fasilitas = Integer.valueOf(JO.getString("jml_fasilitas"));
                        lat = Double.valueOf(JO.getString("lat"));
                        lng = Double.valueOf(JO.getString("lng"));
                        jenis = JO.getString("jenis");
                        alamat = JO.getString("alamat");
                        jam_operasional = Double.valueOf(JO.getString("jam_operasional"));
                        foto = JO.getString("foto");
                        jarak = Double.valueOf(JO.getString("jarak"));
                        fasilitas = JO.getString("fasilitas");
                        if(count<=9) {
                            wsb.add(new WisbelModel(id, nama_tempat, rating, jam_buka, jam_tutup, jml_fasilitas, lat, lng, jenis, alamat, jam_operasional, foto, jarak, fasilitas));
                        }
                        count++;
                    }
                    ListRekomendasi adapter = new ListRekomendasi(HasilTopsis.this, wsb, HasilTopsis.this);
                    rv_list.setLayoutManager(new LinearLayoutManager(HasilTopsis.this));
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
