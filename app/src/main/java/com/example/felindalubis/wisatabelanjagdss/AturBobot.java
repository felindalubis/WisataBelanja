package com.example.felindalubis.wisatabelanjagdss;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AturBobot extends AppCompatActivity {
    double rating, fasilitas, jarak, jam_operasional;
    String nama_grup;
    BubbleSeekBar sb_rating, sb_fasilitas, sb_jarak, sb_jamOp;
    Button bt_next, bt_help;
    int jml_user, user=1, jmlfinal;
    TextView tv_user;
    double myLatitude = 0.0, myLongitude = 0.0;
    private FusedLocationProviderClient myLocation;
    private static final int LOCATION_REQUEST = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atur_bobot);

        sb_rating = (BubbleSeekBar)findViewById(R.id.sb_rating);
        sb_fasilitas = (BubbleSeekBar)findViewById(R.id.sb_fasilitas);
        sb_jarak = (BubbleSeekBar)findViewById(R.id.sb_jarak);
        sb_jamOp = (BubbleSeekBar)findViewById(R.id.sb_operasional);

        sb_rating.setProgress(0);
        sb_fasilitas.setProgress(0);
        sb_jarak.setProgress(0);
        sb_jamOp.setProgress(0);

        bt_help = (Button)findViewById(R.id.bt_help);
        bt_next = (Button)findViewById(R.id.bt_next);
        tv_user = (TextView) findViewById(R.id.tv_user);
        Intent i = getIntent();
        jml_user = i.getExtras().getInt("Jml_user");
        nama_grup = i.getExtras().getString("nama_grup");
        tv_user.setText("User "+user);
        jmlfinal = i.getExtras().getInt("Jml_user");

        //alert dialog help
        bt_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(AturBobot.this).create();
                alertDialog.setTitle("How to set your preferences");
                alertDialog.setMessage("Slide the dot to the right to make a criteria more important than others." +
                        "\n\nRating : \nSlide right to get top rated places \nFacility: \nSlide right to get places with many facility (parking lot, toilet, mushola, etc.)"+
                        "\nDistance: \nSlide right to get nearest places \nOperational hour: \nSlide right to get places with longest operational hour"+
                        "\nIf you don't mind some or all of the criteria, just keep your preferences to 0" +
                        "\n\nFor example: " +
                        "\n\nSet rating to 60, \nfacility to 0, \ndistance to 40, \noperational hour at 0. \nThis will increase the possibility of getting the nearest top-rated places for recommendation."+
                        "\n\nRemember that results based not only your preferences but your group.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        //Cek permission lokasi
        if(ActivityCompat.checkSelfPermission(AturBobot.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(AturBobot.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST);
        }
        myLocation = LocationServices.getFusedLocationProviderClient(AturBobot.this);
        Log.d("Location", "location : "+myLocation);
        if (myLocation != null) {
            myLocation.getLastLocation().addOnSuccessListener(AturBobot.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    myLatitude = location.getLatitude();
                    myLongitude = location.getLongitude();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Please restart app", Toast.LENGTH_SHORT).show();
        }

        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jml_user>1) {
                    if(rating == 0){
                        rating = 0.01;
                    }
                    if (fasilitas==0){
                        fasilitas =0.01;
                    }
                    if(jarak == 0){
                        jarak = 0.01;
                    }
                    if(jam_operasional == 0){
                        jam_operasional = 0.01;
                    }
                        Intent i = new Intent();
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("Jml_user", jml_user);
                        Log.d("User", "user ke: " + user);
                        Log.d("Bobot", "Bobot user "+user+": "+ rating +" "+ fasilitas +" "+ jarak +" "+ jam_operasional);
                        RequestHandler rq = new RequestHandler(AturBobot.this);
                        rq.execute("Next", String.valueOf(user), String.valueOf(rating), String.valueOf(fasilitas), String.valueOf(jarak),
                                String.valueOf(jam_operasional), String.valueOf(myLatitude), String.valueOf(myLongitude), String.valueOf(nama_grup));
                        Log.d("Location", "lat: "+myLatitude+", lng: "+myLongitude);
                        jml_user = jml_user-1;
                        if(jml_user>=1) {
                            sb_rating.setProgress(0);
                            sb_fasilitas.setProgress(0);
                            sb_jarak.setProgress(0);
                            sb_jamOp.setProgress(0);
                            user = user + 1;
                            tv_user.setText("User " + user);
                        }
                }
                else {
                    if(rating == 0){
                        rating = 0.01;
                    }
                    if (fasilitas==0){
                        fasilitas =0.01;
                    }
                    if(jarak == 0){
                        jarak = 0.01;
                    }
                    if(jam_operasional == 0){
                        jam_operasional = 0.01;
                    }
                    Log.d("User", "user ke: " + user);
                    Log.d("Bobot", "Bobot user "+user+": "+ rating +" "+ fasilitas +" "+ jarak +" "+ jam_operasional);
                    RequestHandler rq = new RequestHandler(AturBobot.this);
                    rq.execute("Next", String.valueOf(user), String.valueOf(rating), String.valueOf(fasilitas), String.valueOf(jarak),
                            String.valueOf(jam_operasional), String.valueOf(myLatitude), String.valueOf(myLongitude), String.valueOf(nama_grup));
                    Log.d("Location", "lat: "+myLatitude+", lng: "+myLongitude);
                    jml_user = jml_user-1;
                    if(jml_user>=1) {
                        sb_rating.setProgress(0);
                        sb_fasilitas.setProgress(0);
                        sb_jarak.setProgress(0);
                        sb_jamOp.setProgress(0);
                        user = user + 1;
                        tv_user.setText("User " + user);
                    }
                    Intent i = new Intent(AturBobot.this, HasilTopsis.class);
                    i.putExtra("my_lng", myLongitude);
                    i.putExtra("my_lat", myLatitude);
                    i.putExtra("Jml_user", jmlfinal);
                    i.putExtra("nama_grup", nama_grup);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }
        });

        sb_rating.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                rating = (double) progress/100;
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        });
        sb_fasilitas.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                fasilitas = (double) progress/100;
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        });
        sb_jarak.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                jarak = (double) progress/100;
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        });
        sb_jamOp.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                jam_operasional = (double) progress/100;
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}

