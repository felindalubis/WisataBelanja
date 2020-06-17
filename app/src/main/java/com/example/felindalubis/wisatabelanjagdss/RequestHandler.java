package com.example.felindalubis.wisatabelanjagdss;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

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

public class RequestHandler extends AsyncTask<String,Void,String> {
    Context context;


    RequestHandler (Context ctx) {
        context = ctx;
    }

    @Override
    protected void onPreExecute() {
//        myUrl = "https://wisatabelanja.000webhostapp.com/insertBobot.php";
    }

    @Override
    protected String doInBackground(String... params) {
        String bt = params[0];
        if (bt.equalsIgnoreCase("Next")) {
            try {
                String user = params[1];
                String rating = params[2];
                String fasilitas = params[3];
                String jarak = params[4];
                String operasional = params[5];
                String my_lat = params[6];
                String my_lng = params[7];
                String nama_grup = params[8];
                URL url = new URL("https://wisatabelanja.000webhostapp.com/insertBobot.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8") + "&"
                        + URLEncoder.encode("rating", "UTF-8") + "=" + URLEncoder.encode(rating, "UTF-8") + "&"
                        + URLEncoder.encode("fasilitas", "UTF-8") + "=" + URLEncoder.encode(fasilitas, "UTF-8") + "&"
                        + URLEncoder.encode("jarak", "UTF-8") + "=" + URLEncoder.encode(jarak, "UTF-8") + "&"
                        + URLEncoder.encode("jam_operasional", "UTF-8") + "=" + URLEncoder.encode(operasional, "UTF-8") + "&"
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
        } else if(bt.equalsIgnoreCase("Hitung")){
            try {
                String user = params[1];
                URL url = new URL("https://wisatabelanja.000webhostapp.com/count.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8");
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
        }
        return null;
    }


    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}

