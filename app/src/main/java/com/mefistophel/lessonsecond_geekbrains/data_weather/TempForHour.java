package com.mefistophel.lessonsecond_geekbrains.data_weather;

public class TempForHour {
    private String time;
    private String temp;
    private String condition;

    public TempForHour(String time, String temp, String condition) {
        this.time      = time;
        this.temp      = temp;
        this.condition = condition;
    }

    public String getTime() {
        return time;
    }

    public String getTemp() {
        return temp;
    }

    public String getCondition() {
        return condition;
    }
}
