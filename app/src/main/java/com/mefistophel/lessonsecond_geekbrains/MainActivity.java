package com.mefistophel.lessonsecond_geekbrains;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    public final static int SELECTED_CITY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
