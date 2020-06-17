package com.example.felindalubis.wisatabelanjagdss;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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


public class DeskripsiWisbel extends AppCompatActivity {
    ImageView wisbel_img;
    TextView nama_tv, alamat_tv, jamOp_tv, rate_tv, fasilitas_tv, jenis_tv;
    Button bt_direction;
    Uri g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_belanja);

        Intent i = getIntent();
        String nama, alamat, jam_operasional, foto, fasilitas, jenis;
        final Double lat, lng, rate;

        nama = i.getExtras().getString("nama");
        alamat = i.getExtras().getString("alamat");
        jam_operasional = i.getExtras().getString("jam_operasional");
        foto = i.getExtras().getString("foto");
        lat = i.getExtras().getDouble("lat");
        lng = i.getExtras().getDouble("lng");
        rate = i.getExtras().getDouble("rate");
        fasilitas = i.getExtras().getString("fasilitas");
        jenis = i.getExtras().getString("jenis");
        wisbel_img = (ImageView) findViewById(R.id.wisbel_img);
        nama_tv = (TextView) findViewById(R.id.nama_tv);
        alamat_tv = (TextView) findViewById(R.id.alamat_tv2);
        jamOp_tv = (TextView) findViewById(R.id.jamOp_tv2);
        rate_tv = (TextView) findViewById(R.id.rate_tv);
        fasilitas_tv = (TextView)findViewById(R.id.fasil_tv2);
        bt_direction = (Button) findViewById(R.id.direction_bt);
        jenis_tv = (TextView)findViewById(R.id.jenis_tv2);

        bt_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DeskripsiWisbel.this);
                builder.setMessage("Are you sure you want to open Maps?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                g = Uri.parse("google.navigation:q="+lat+","+lng);
                                Intent intent = new Intent(Intent.ACTION_VIEW, g);
                                intent.setPackage("com.google.android.apps.maps");
                                DeskripsiWisbel.this.startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        Glide.with(DeskripsiWisbel.this)
                .asBitmap()
                .load(foto)
                .into(wisbel_img);
        nama_tv.setText(nama);
        rate_tv.setText(rate.toString()+"/5.0 stars");
        alamat_tv.setText(alamat);
        jamOp_tv.setText(jam_operasional);
        fasilitas_tv.setText(fasilitas);
        jenis_tv.setText(jenis);
    }
}
