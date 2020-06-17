package com.example.felindalubis.wisatabelanjagdss;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.felindalubis.wisatabelanjagdss.Adapter.WisbelAdapter;
import com.example.felindalubis.wisatabelanjagdss.Model.WisbelModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

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

public class MainActivity extends AppCompatActivity implements WisbelAdapter.onItemListener{
    String JSON_STRING;
    String json_data;
    JSONObject jsonObject;
    JSONArray jsonArray;
    RecyclerView rv_toprated;
    Button bt_mall, bt_supermarket, bt_toko, bt_pasar, bt_rekomendasi, bt_list;
    TextView tv_kategori, tv_toprated;
    ArrayList<WisbelModel> wsb = new ArrayList<>();
    ConnectivityManager conMgr;
    private static final int LOCATION_REQUEST = 1000;
    private FusedLocationProviderClient myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv_toprated = (RecyclerView) findViewById(R.id.rv_toprated);
        bt_mall = (Button) findViewById(R.id.bt_Mall);
        bt_supermarket = (Button) findViewById(R.id.bt_Supermarket);
        bt_toko = (Button) findViewById(R.id.bt_Toko);
        bt_pasar = (Button) findViewById(R.id.bt_Pasar);
        bt_list = (Button) findViewById(R.id.bt_list);
        bt_rekomendasi = (Button) findViewById(R.id.bt_rekomendasi);
        tv_kategori = (TextView) findViewById(R.id.kategori_tv);
        tv_toprated = (TextView) findViewById(R.id.toprated_tv);

        // alert dialog aktifin GPS
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }

        //Cek permission lokasi
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST);
        }
        myLocation = LocationServices.getFusedLocationProviderClient(this);
        Log.d("Location", "location : "+myLocation);

        bt_rekomendasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AturKelompok.class);
                startActivity(i);
            }
        });
        bt_mall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ListByCat.class);
                i.putExtra("jenis", "Mall");
                startActivity(i);
            }
        });
        bt_supermarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ListByCat.class);
                i.putExtra("jenis", "Supermarket");
                startActivity(i);
            }
        });
        bt_toko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ListByCat.class);
                i.putExtra("jenis", "Toko");
                startActivity(i);
            }
        });
        bt_pasar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ListByCat.class);
                i.putExtra("jenis", "Pasar");
                startActivity(i);
            }
        });

        bt_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ListByCat.class);
                i.putExtra("jenis", "All category");
                startActivity(i);
            }
        });

        if(!isOnline()){
            Toast.makeText(getApplicationContext(), "Check your internet and restart app", Toast.LENGTH_LONG).show();
        }

        WisbelAdapter adapter = new WisbelAdapter(MainActivity.this, wsb, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rv_toprated.setLayoutManager(linearLayoutManager);
        rv_toprated.setAdapter(adapter);
        new BackgroundTask().execute();
    }

    public boolean isOnline() {
        conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            return false;
        }
        return true;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("GPS is not activated, do you want to activate it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
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

    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent();
                        i.addCategory(Intent.CATEGORY_HOME);
                        startActivity(i);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }


    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String json_url;

        @Override
        protected void onPreExecute() {
            json_url = "https://wisatabelanja.000webhostapp.com/get_data_sort.php";

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((JSON_STRING = bufferedReader.readLine()) != null) {
                    stringBuilder.append(JSON_STRING + "\n");

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
            if (json_data == null) {
                Toast.makeText(getApplicationContext(), "Check your internet connection & restart app", Toast.LENGTH_LONG).show();
            } else {
                try {
                    jsonObject = new JSONObject(json_data);
                    jsonArray = jsonObject.getJSONArray("server_response");
                    int count = 0;
                    String nama_tempat, jenis, alamat, jam_buka, jam_tutup, foto, fasilitas;
                    double rating, jam_operasional, lat, lng;
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
                        fasilitas = JO.getString("fasilitas");
                        wsb.add(new WisbelModel(id, nama_tempat, rating, jam_buka, jam_tutup, jml_fasilitas,lat, lng, jenis, alamat, jam_operasional, foto, fasilitas));
                        count++;
                    }
                    WisbelAdapter adapter = new WisbelAdapter(MainActivity.this, wsb, MainActivity.this);
                    rv_toprated.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    rv_toprated.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

