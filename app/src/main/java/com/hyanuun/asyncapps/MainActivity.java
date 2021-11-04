package com.hyanuun.asyncapps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Button btnGo, btnGetData;
    private ProgressBar progressBar;
    private TextView textInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGo = (Button) findViewById(R.id.button_go);
        btnGetData = (Button) findViewById(R.id.button_GetData);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textInfo = (TextView) findViewById(R.id.text_info);

//        melakukan proses untuk melakukan asynctask, akan berjalan ketika melakukan button
        btnGo.setOnClickListener(v -> {
            progressBar.setProgress(0);
            new MyAsync().execute(15);
        });

//        ketika di klik maka akan mengambil data dari internet
        btnGetData.setOnClickListener(v -> {
            String url = "https://random-data-api.com/api/cannabis/random_cannabis?size=5";
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Log.e("ERROR", "Kendala di server API");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final  String resp = response.body().string();
                    Log.i("RESPONSE", resp);
                }
            });
        });
    }

    private class MyAsync extends AsyncTask<Integer, Integer, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            textInfo.setText(getResources().getString(R.string.text_starting));
        }

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                int count = integers[0];
                for(int i = 0; i < count; i++){
                    Thread.sleep(1000);
                    int value = (int)(((i+1)/(float)count)*100);

//                    untuk mengupdate nilai di saat terjadi async saat berjalan
                    publishProgress(value);
                    Log.d("PROGRESS", value+"");
                }

            } catch (Exception e){}

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
            textInfo.setText(getResources().getString(R.string.text_running)+" "+values[0]+"%");
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
        }
    }


}