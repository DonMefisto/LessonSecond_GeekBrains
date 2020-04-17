package com.mefistophel.lessonsecond_geekbrains;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public final static int SELECTED_CITY = 0;
    private TextView txtTime;
    private TextView txtTemp;
    private TextView txtFeelsLike;
    private TextView txtCity;
    private ListView listTemp;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        updateWeather();

        setDefaultValue();
    }

    private void updateWeather() {
        new Thread(){
            public void run() {
                final JSONObject json = RequestWeather.getJSON();
                Message msg = handler.obtainMessage();
                Bundle bundle = new Bundle();
                if (json == null) {
                    bundle.putString("temp", "Sorry, we are trying to pull data, but we can't.");
                } else {
                    try {
                        JSONObject factJson = json.getJSONObject("fact");
                        bundle.putString("temp", factJson.getInt("temp") + "°");
                        bundle.putString("feelsLike", factJson.getInt("feels_like") + "°");
                    } catch (JSONException e) {
                        bundle.putString("Key", "Sorry, we are trying to pull data, but we can't.");
                    }
                }
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        }.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            txtTemp = findViewById(R.id.txtTemp);
            txtTemp.setText(bundle.getString("temp"));
            txtFeelsLike = findViewById(R.id.txtTempLike);
            txtFeelsLike.setText(bundle.getString("feelsLike"));
        }
    };


    private void setDefaultValue() {
        //current time for user
        Calendar cal = Calendar.getInstance();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        txtTime.setText(timeFormat.format(cal.getTime()));

        int hour = cal.get(Calendar.HOUR);
        if ( hour > 19 || hour < 7)
            constraintLayout.setBackgroundResource(R.drawable.cloud_night_sky);
        else
            constraintLayout.setBackgroundResource(R.drawable.cloud_day_sky);

        //current temperature
        //txtTemp.setText("18°");

        //planned temperature
        timeFormat = new SimpleDateFormat("HH:00", Locale.getDefault());

        String[] temps = new String[23];
        for (int i = 1; i < 24; i++) {
            cal.add(Calendar.HOUR, 1);
            temps[i - 1] = timeFormat.format(cal.getTime()) + "    " + (i+18) + "°";
        }
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, temps);
        listTemp.setAdapter(stringArrayAdapter);

        //default city - Khabarovsk
        txtCity.setText("Khabarovsk");
    }

    private void initView() {
        txtTime = findViewById(R.id.txtTime);
        txtTemp = findViewById(R.id.txtTemp);
        txtCity = findViewById(R.id.txtCity);
        listTemp= findViewById(R.id.listTemp);
        constraintLayout = findViewById(R.id.constLayoutMain);
   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.itemSettings: {
                    Intent intentSettings = new Intent(MainActivity.this, SettingsActivity.class);

                    TextView txtCity = findViewById(R.id.txtCity);
                    intentSettings.putExtra("City", txtCity.getText().toString());
                    startActivityForResult(intentSettings, SELECTED_CITY);
                    break;
                }
            default: {
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TextView txtCity = findViewById(R.id.txtCity);
        if (requestCode == SELECTED_CITY)
            if (resultCode == RESULT_OK)
                txtCity.setText(data.getStringExtra(SettingsActivity.CITY));
    }
}
