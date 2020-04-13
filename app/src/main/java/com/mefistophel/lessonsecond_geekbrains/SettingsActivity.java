package com.mefistophel.lessonsecond_geekbrains;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;

import java.sql.Array;
import java.util.ArrayList;

public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        String[] cities = new String[] {getIntent().getStringExtra("City")};
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cities);

        ListView listView = findViewById(R.id.listCities);
        listView.setAdapter(stringArrayAdapter);
    }
}
