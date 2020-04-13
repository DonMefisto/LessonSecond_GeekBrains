package com.mefistophel.lessonsecond_geekbrains;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class SettingsActivity extends Activity {

    public final static String CITY = "com.mefistophel.lessonsecond_geekbrains.CITY";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        String[] cities = new String[] {getIntent().getStringExtra("City"), "Moscow", "Saint Petersburg", "Vladivostok", "Novosibirsk", "Yakutsk"};
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cities);

        ListView listView = findViewById(R.id.listCities);
        listView.setAdapter(stringArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent settingsIntent = new Intent();
                settingsIntent.putExtra(CITY, ((TextView) view).getText());
                setResult(RESULT_OK, settingsIntent);
                finish();
            }
        });
    }

}
