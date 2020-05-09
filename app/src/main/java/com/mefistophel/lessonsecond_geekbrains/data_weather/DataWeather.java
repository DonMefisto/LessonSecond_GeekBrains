package com.mefistophel.lessonsecond_geekbrains.data_weather;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.mefistophel.lessonsecond_geekbrains.RequestWeather;
import com.mefistophel.lessonsecond_geekbrains.observer.Publisher;
import com.mefistophel.lessonsecond_geekbrains.observer.PublisherGetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public final class DataWeather extends AppCompatActivity {

    private String city;

    private double latCity;
    private double lonCity;

    private String actualTemp;
    private String tempFeelsLike;
    private String condition;

    private String windSpeed;
    private String windGust;
    private String windDirection;

    private String pressureMM;

    private TempForHour[] tempForHours = new TempForHour[24];

    private Publisher publisher;

    public DataWeather(String city, Context context) {
        publisher = ((PublisherGetter) context).getPublisher();

        if (setLatLonCity(getCityCoordinates(city, context))) {
            this.city = city;
            requestDataFromAPI("lat" + latCity + "&" + "lon" + lonCity);
        }
        else{
            this.city = "";
            Toast.makeText(context, "Unfortunately, the city was not found. Check the spelling of the city and try again.", Toast.LENGTH_LONG).show();
        }
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city, Context context) {
        if (!this.city.equals(city))
            if (setLatLonCity(getCityCoordinates(city, context)))
            {
                this.city = city;
                requestDataFromAPI("lat" + latCity + "&" + "lon" + lonCity);
            }
    }

    private boolean setLatLonCity(Address coordinates) {
        if (coordinates != null) {
            this.latCity = coordinates.getLatitude();
            this.lonCity = coordinates.getLongitude();
            return true;
        }
        return false;
    }

    public double getLatCity() {
        return latCity;
    }

    public double getLonCity() {
        return lonCity;
    }

    public String getActualTemp() {
        return actualTemp;
    }

    public String getTempFeelsLike() {
        return tempFeelsLike;
    }

    public String getCondition() {
        return condition;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getWindGust() {
        return windGust;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public String getPressureMM() {
        return pressureMM;
    }

    public String getTempForHour(int index) {
        return tempForHours[index].getTime() + "    " + tempForHours[index].getTemp();
    }

    private Address getCityCoordinates(String city, Context context) {
        try {
            return new Geocoder(context).getFromLocationName(city, 1).get(0);
        } catch (IOException e) {
            return null;
        }
    }

    private void requestDataFromAPI(final String coordinates) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final String forecast = RequestWeather.getJSON(coordinates);
                Message message = handler.obtainMessage();
                Bundle bundle   = new Bundle();
                bundle.putString("Forecast", forecast);
                message.setData(bundle);
                handler.sendMessage(message);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void fillValues(String forecast) {
        try {
            synchronized (DataWeather.this) {
                JSONObject json = new JSONObject(forecast);
                JSONObject factJson = json.getJSONObject("fact");
                actualTemp = factJson.getInt("temp") + "°";
                tempFeelsLike = factJson.getInt("feels_like") + "°";
                condition = factJson.getString("condition");

                windSpeed = factJson.getInt("wind_speed") + "m/s";
                windGust = factJson.getInt("wind_gust") + "m/s";
                windDirection = factJson.getString("wind_dir");

                pressureMM = factJson.getInt("pressure_mm") + "mm";

                JSONArray forecasts = json.getJSONArray("forecasts");
                JSONArray hours = forecasts.getJSONObject(0).getJSONArray("hours");
                for (int i = 0; i < hours.length(); i++)
                    tempForHours[i] = new TempForHour(hours.getJSONObject(i).getString("hour") + ":00", hours.getJSONObject(i).getInt("temp") + "°", hours.getJSONObject(i).getString("condition"));
            }
            publisher.notify(city);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            String forecast = message.getData().getString("Forecast");
            fillValues(forecast);
        }
    };
}
