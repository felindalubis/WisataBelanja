package com.example.felindalubis.wisatabelanjagdss;

import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

//implements CompoundButton.OnCheckedChangeListener
public class AturAnggota extends AppCompatActivity implements View.OnClickListener {
    Button bt_next, bt_plus, bt_min;
    TextView tv_jml;
    String nama_grup;
    int user  = 2;
//    ArrayList<String> list_user = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atur_anggota2);

        bt_next = (Button)findViewById(R.id.bt_next);
        bt_plus = (Button)findViewById(R.id.bt_plus);
        bt_min = (Button)findViewById(R.id.bt_min);
        tv_jml = (TextView)findViewById(R.id.tv_jml);
        bt_plus.setOnClickListener(this);
        bt_min.setOnClickListener(this);
        Intent i = getIntent();
        nama_grup = i.getExtras().getString("nama_grup");
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(AturAnggota.this, AturBobot.class);
                    i.putExtra("Jml_user", user);
                    i.putExtra("nama_grup", nama_grup);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    Toast.makeText(AturAnggota.this.getApplicationContext(), "User : "+user, Toast.LENGTH_SHORT).show();
                }
            }
        );
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bt_plus){
            if(user < 9) {
                user++;
                tv_jml.setText(String.valueOf(user));
            }
        }
        else if (v.getId() == R.id.bt_min) {
            if (user > 2) {
                user--;
                tv_jml.setText(String.valueOf(user));
            }
        }
    }

    @Override
    public void onBackPressed() {
      Intent i = new Intent(this, AturKelompok.class);
     i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      startActivity(i);
}
}
