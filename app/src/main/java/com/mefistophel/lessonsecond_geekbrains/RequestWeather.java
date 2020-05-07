package com.mefistophel.lessonsecond_geekbrains;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestWeather {
//    private static final String YANDEX_API = "https://api.weather.yandex.ru/v1/forecast?48.469084&lon=135.078262&limit=1&hours=5&extra=false";

    public static JSONObject getJSON(String coordinates){
        try {
            URL url = new URL("https://api.weather.yandex.ru/v1/forecast?" + coordinates + "&limit=1&hours=5&extra=false");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("X-Yandex-API-Key", "b985dff9-b536-46fc-8677-f06c5f457dda");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            return data;
        }catch(Exception e){
            return null;
        }
    }
}
