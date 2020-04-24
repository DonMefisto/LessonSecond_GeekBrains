package com.mefistophel.lessonsecond_geekbrains;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public final static int SELECTED_CITY = 0;

    private TextView txtTime;
    private TextView txtTemp;
    private TextView txtFeelsLike;
    private TextView txtCity;
    private ListView listTemp;

    private ConstraintLayout constraintLayout;
    private static Singleton savedData;

    private RequestDataAsync requestDataAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        setDefaultValue();

        if (savedInstanceState == null)
            requestDataFromAPI(getCityCoordinates());

    }

    private String getCityCoordinates() {
        Geocoder coder = new Geocoder(getApplicationContext());
        try {
            List<Address> addresses = coder.getFromLocationName(txtCity.getText().toString(), 1);
            Address location = addresses.get(0);
            Toast.makeText(getApplicationContext(), "lat" + location.getLatitude() + "&" + "lon" + location.getLongitude(), Toast.LENGTH_LONG).show();

            return "lat" + location.getLatitude() + "&" + "lon" + location.getLongitude();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        return "NoCity";
    }

    private void requestDataFromAPI(String coordinates) {
        if (coordinates.contentEquals("NoCity"))
            Toast.makeText(getApplicationContext(), "Sorry, but we could not find your city coordinates.", Toast.LENGTH_LONG).show();
        else {
            requestDataAsync = new RequestDataAsync();
            requestDataAsync.execute(coordinates);
        }
    }

    class RequestDataAsync extends AsyncTask < String, Void ,JSONObject> {
        @Override
        protected JSONObject doInBackground(String... coordinates) {
            final JSONObject json = RequestWeather.getJSON(coordinates[0]);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            try {
                JSONObject factJson = json.getJSONObject("fact");
                if (savedData == null) {
                    savedData = Singleton.getInstance(factJson.getInt("temp") + "°", factJson.getInt("feels_like") + "°", txtCity.getText().toString());
                    txtTemp.setText(savedData.temp);
                    txtFeelsLike.setText(savedData.tempFeelsLike);
                }
                else
                {
                    savedData.setTemp(factJson.getInt("temp") + "°");
                    savedData.setTempFeelsLike(factJson.getInt("feels_like") + "°");
                    txtTemp.setText(savedData.temp);
                    txtFeelsLike.setText(savedData.tempFeelsLike);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

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
        if (savedData == null) {
            txtTemp.setText("0°");
            txtFeelsLike.setText("0°");
        }
        else{
            txtTemp.setText(savedData.temp);
            txtFeelsLike.setText(savedData.tempFeelsLike);
        }

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
        txtFeelsLike = findViewById(R.id.txtTempLike);
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
            if (resultCode == RESULT_OK) {
                txtCity.setText(data.getStringExtra(SettingsActivity.CITY));
                if (!savedData.city.contentEquals(txtCity.getText())) {
                    savedData.setCity(txtCity.getText().toString());
                    requestDataFromAPI(getCityCoordinates());
                }
            }
    }

    public void clickTxtTemp(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/pogoda/" + txtCity.getText() + "?" + getCityCoordinates()));
        startActivity(intent);
    }
}
