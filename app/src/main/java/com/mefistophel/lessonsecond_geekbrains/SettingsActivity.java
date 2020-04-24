package com.mefistophel.lessonsecond_geekbrains;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity implements PublisherGetter, Observer{

    public final static String CITY = "com.mefistophel.lessonsecond_geekbrains.CITY";
    private CityList fragmentCities;
    private Publisher publisher = new Publisher();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        fragmentCities = new CityList();
        publisher.subscribe(SettingsActivity.this);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frameCities, fragmentCities).commit();
    }

    @Override
    public Publisher getPublisher() {
        return publisher;
    }

    @Override
    public void updateCity(String text) {
        Intent settingsIntent = new Intent();
        settingsIntent.putExtra(CITY, text);
        setResult(RESULT_OK, settingsIntent);
        finish();
    }
}
