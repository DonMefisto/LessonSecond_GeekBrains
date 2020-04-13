package com.mefistophel.lessonsecond_geekbrains;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSettings = new Intent(MainActivity.this, SettingsActivity.class);

                TextView txtCity = findViewById(R.id.txtCity);
                intentSettings.putExtra("City", txtCity.getText().toString());
                startActivity(intentSettings);
            }
        });
    }
}
