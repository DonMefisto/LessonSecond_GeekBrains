package com.mefistophel.lessonsecond_geekbrains;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestWeather {
//    private static final String YANDEX_API = "https://api.weather.yandex.ru/v1/forecast?48.469084&lon=135.078262&limit=1&hours=5&extra=false";

    public static String getJSON(String coordinates){
        try {
            URL url = new URL(Constants.ADDRESS_API_YANDEX + coordinates + "&limit=1&hours=5&extra=false");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.addRequestProperty(Constants.NAME_API_KEY_YANDEX, Constants.VALUE_API_KEY_YANDEX);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            return json.toString();

        }catch(Exception e){
            return null;
        }
    }
}
