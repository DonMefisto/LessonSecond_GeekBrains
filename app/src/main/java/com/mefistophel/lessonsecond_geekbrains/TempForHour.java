package com.mefistophel.lessonsecond_geekbrains;

import android.icu.text.SimpleDateFormat;

public class TempForHour {
    private SimpleDateFormat time;
    private String temp;

    public TempForHour(SimpleDateFormat time, String temp) {
        this.time = time;
        this.temp = temp;
    }

    public SimpleDateFormat getTime() {
        return time;
    }

    public String getTemp() {
        return temp;
    }
}
