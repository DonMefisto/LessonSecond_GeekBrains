package com.mefistophel.lessonsecond_geekbrains.data_weather;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.mefistophel.lessonsecond_geekbrains.RequestWeather;
import com.mefistophel.lessonsecond_geekbrains.observer_city_fragment.Observer;
import com.mefistophel.lessonsecond_geekbrains.observer_city_fragment.Publisher;
import com.mefistophel.lessonsecond_geekbrains.observer_city_fragment.PublisherGetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;

public class DataWeather extends AppCompatActivity implements Observer {

    private DateFormat dateCheckTemp;

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

    private AsyncTask requestDataAsync;

    private Publisher publisher;

    public DataWeather(String city, Context context) {
        publisher = ((PublisherGetter) context).getPublisher();

        if (setLatLonCity(getCityCoordinates(city, context))) {
            this.city = city;
            requestDataFromAPI("lat" + latCity + "&" + "lon" + lonCity);
        }
        else
            Toast.makeText(context, "Unfortunately, the city was not found. Check the spelling of the city and try again.", Toast.LENGTH_LONG);
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

    private void requestDataFromAPI(String coordinates) {
        requestDataAsync = new RequestDataAsync();
        requestDataAsync.execute(coordinates);
    }

    @Override
    public void updateCity(String text) {

    }

    class RequestDataAsync extends AsyncTask< Object, Void , JSONObject> {
        @Override
        protected JSONObject doInBackground(Object... coordinates) {
            final JSONObject json = RequestWeather.getJSON(String.valueOf(coordinates[0]));
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);

            try {
                JSONObject factJson = json.getJSONObject("fact");
                actualTemp      = factJson.getInt("temp") + "°";
                tempFeelsLike   = factJson.getInt("feels_like") + "°";
                condition       = factJson.getString("condition");

                windSpeed       = factJson.getInt("wind_speed") + "m/s";
                windGust        = factJson.getInt("wind_gust") + "m/s";
                windDirection   = factJson.getString("wind_dir");

                pressureMM      = factJson.getInt("pressure_mm") + "mm";

                JSONArray forecasts = json.getJSONArray("forecasts");
                JSONArray hours = forecasts.getJSONObject(0).getJSONArray("hours");
                for (int i = 0; i < hours.length(); i++)
                    tempForHours[i] = new TempForHour(hours.getJSONObject(i).getString("hour") + ":00", hours.getJSONObject(i).getInt("temp") + "°", hours.getJSONObject(i).getString("condition"));

                publisher.notify(city);
            } catch (JSONException e) {
                e.printStackTrace();
            }
         }
    }

}
