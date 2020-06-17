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

import com.example.felindalubis.wisatabelanjagdss.Adapter.ListAdapter;
import com.example.felindalubis.wisatabelanjagdss.Model.WisbelModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ListByCat extends AppCompatActivity implements ListAdapter.onItemListener {
    String JSON_STRING;
    String json_data;
    JSONObject jsonObject;
    JSONArray jsonArray;
    RecyclerView rv_list;
    TextView tv_namarv;
    String jenis2;
    ArrayList<WisbelModel> wsb = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_belanja);
        Intent i = getIntent();
        jenis2 = i.getExtras().getString("jenis");
        Log.d("JENIS", "Jenis: "+jenis2);
        rv_list = (RecyclerView)findViewById(R.id.rv_list);
        tv_namarv = (TextView) findViewById(R.id.tv_namarv);
        ListAdapter adapter = new ListAdapter(ListByCat.this, wsb, ListByCat.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ListByCat.this);
        rv_list.setLayoutManager(linearLayoutManager);
        rv_list.setAdapter(adapter);
        new BackgroundTask().execute();
    }

    @Override
    public void onItemClick(ArrayList<WisbelModel> data, int position) {
        Intent i = new Intent(this, DeskripsiWisbel.class);
        Log.d("CLICK", "clicked on:" + position);
        Log.d("CLICK", "size:" + data.size());
        Log.d("CLICK", "isi : "+data.get(position));
        Log.d("CLICK", "isi : "+data.get(position).nama_tempat);
        i.putExtra("posisi", position);
        i.putExtra("Wisbel", data);
        i.putExtra("nama", data.get(position).nama_tempat);
        i.putExtra("alamat", data.get(position).alamat);
        i.putExtra("jam_operasional", data.get(position).jam_buka+" - "+data.get(position).jam_tutup);
        i.putExtra("foto", data.get(position).foto);
        i.putExtra("lat", data.get(position).lat);
        i.putExtra("lng", data.get(position).lng);
        i.putExtra("rate", data.get(position).rating);
        i.putExtra("fasilitas", data.get(position).fasilitas);
        i.putExtra("jenis", data.get(position).jenis);
        startActivity(i);
    }

    class BackgroundTask extends AsyncTask<Void,Void,String> {
        String json_url;
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ListByCat.this, null, "Please wait...");
            dialog.show();
                json_url = "https://wisatabelanja.000webhostapp.com/get_data.php";
                if(jenis2.equalsIgnoreCase("toko")){
                    jenis2 = "Others";
                }
                tv_namarv.setText(jenis2);

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((JSON_STRING = bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(JSON_STRING+"\n");

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return stringBuilder.toString().trim();

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
                Toast.makeText(getApplicationContext(), "Check your internet connection & restart app", Toast.LENGTH_LONG).show();
            }
            else
            {
                try {
                    jsonObject = new JSONObject(json_data);
                    jsonArray = jsonObject.getJSONArray("server_response");
                    int count = 0;
                    String nama_tempat, jenis, alamat, jam_buka, jam_tutup, foto, fasilitas;
                    double rating, jam_operasional, lat, lng;
                    int id, jml_fasilitas;
                    ArrayList<WisbelModel> wsb = new ArrayList<>();

                    while(count<jsonArray.length()){
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
                        fasilitas = JO.getString("fasilitas");
                        if(jenis2.equalsIgnoreCase("others")){
                            jenis2 = "Toko";
                        }
                        if(jenis.equalsIgnoreCase(jenis2)) {
                            wsb.add(new WisbelModel(id, nama_tempat, rating, jam_buka, jam_tutup, jml_fasilitas, lat, lng, jenis, alamat, jam_operasional, foto, fasilitas));
                        } else if(jenis2.equalsIgnoreCase("All category")){
                            wsb.add(new WisbelModel(id, nama_tempat, rating, jam_buka, jam_tutup, jml_fasilitas, lat, lng, jenis, alamat, jam_operasional, foto, fasilitas));
                        }
                        count++;
                    }
                    Log.d("Hasil", "hasil: "+result);
                    ListAdapter adapter = new ListAdapter(ListByCat.this, wsb, ListByCat.this);
                    rv_list.setLayoutManager(new LinearLayoutManager(ListByCat.this));
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

