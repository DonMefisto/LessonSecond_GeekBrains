package com.mefistophel.lessonsecond_geekbrains;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mefistophel.lessonsecond_geekbrains.data_weather.DataWeather;
import com.mefistophel.lessonsecond_geekbrains.observer.Observer;
import com.mefistophel.lessonsecond_geekbrains.observer.Publisher;
import com.mefistophel.lessonsecond_geekbrains.observer.PublisherGetter;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PublisherGetter, Observer {

    private SharedPreferences shPreferences;

    private TextView txtTime;
    private TextView txtTemp;
    private TextView txtFeelsLike;
    private TextView txtCity;
    private RecyclerView recViewTemp;
    private WeatherAdapter tempAdapter;

    private ConstraintLayout constraintLayout;

    private DataWeather dataWeather;
    private Publisher publisher = new Publisher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shPreferences = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        initView();
        setDefaultValue();
    }

    private void setDefaultValue() {
        //current time for user
        Calendar cal = Calendar.getInstance();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        txtTime.setText(timeFormat.format(cal.getTime()));

        constraintLayout.setBackgroundResource(R.drawable.clear_night_sky);

        //current temperature
        txtTemp.setText("0°");
        txtFeelsLike.setText("0°");

        publisher.subscribe(MainActivity.this);

        if (shPreferences.contains(Constants.PREFERENCES_CITY))
            dataWeather = new DataWeather(shPreferences.getString(Constants.PREFERENCES_CITY, ""), MainActivity.this);
        else
            txtCity.setText("Choose a city to forecast.");
    }

    private void initView() {
        txtTime = findViewById(R.id.txtTime);
        txtTemp = findViewById(R.id.txtTemp);
        txtFeelsLike = findViewById(R.id.txtTempLike);
        txtCity      = findViewById(R.id.txtCity);
        constraintLayout = findViewById(R.id.constLayoutMain);
        recViewTemp  = findViewById(R.id.recViewTemp);
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
                    startActivityForResult(intentSettings, Constants.SELECTED_CITY);
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

        if (requestCode == Constants.SELECTED_CITY)
            if (resultCode == RESULT_OK)
                dataWeather.setCity(data.getStringExtra(Constants.CITY), MainActivity.this);
    }

    public void clickTxtTemp(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/pogoda/" + txtCity.getText() + "?" + "lat" + dataWeather.getLatCity() + "&" + "lon" + dataWeather.getLonCity()));
        startActivity(intent);
    }

    @Override
    public void updateCity(String text) {
        setViewDataFromDataWeather();
    }

    private void setViewDataFromDataWeather() {
        txtCity.setText(dataWeather.getCity());
        txtTemp.setText(dataWeather.getActualTemp());
        txtFeelsLike.setText(dataWeather.getTempFeelsLike());

        //planned temperature
        Calendar cal = Calendar.getInstance();
        String[] temps = new String[24];
        for (int i = 1; i < 24-cal.get(Calendar.HOUR_OF_DAY); i++)
            temps[i - 1] = dataWeather.getTempForHour(i+cal.get(Calendar.HOUR_OF_DAY));

        recViewTemp.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recViewTemp.setLayoutManager(layoutManager);
        tempAdapter = new WeatherAdapter(temps);
        recViewTemp.setAdapter(tempAdapter);

        //set separator
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getDrawable(R.drawable.separator));
        recViewTemp.addItemDecoration(itemDecoration);

        SharedPreferences.Editor editor = shPreferences.edit();
        editor.putString(Constants.PREFERENCES_CITY, dataWeather.getCity());
        editor.apply();
    }

    @Override
    public Publisher getPublisher() {
        return publisher;
    }

}
