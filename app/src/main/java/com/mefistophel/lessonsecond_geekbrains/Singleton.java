package com.mefistophel.lessonsecond_geekbrains;

public final class Singleton {
    private static volatile Singleton instance;
    public String temp;
    public String tempFeelsLike;
    public String city;

    public Singleton(String temp, String tempFeelsLike, String city)
    {
        this.temp          = temp;
        this.tempFeelsLike = tempFeelsLike;
        this.city          = city;
    }

    public static Singleton getInstance(String temp, String tempFeelsLike, String city)
    {
        synchronized (Singleton.class){
            if (instance == null)
                instance = new Singleton(temp, tempFeelsLike, city);
            return instance;
        }
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setTempFeelsLike(String tempFeelsLike) {
        this.tempFeelsLike = tempFeelsLike;
    }
}
