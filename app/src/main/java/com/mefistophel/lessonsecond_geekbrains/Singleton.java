package com.mefistophel.lessonsecond_geekbrains;

public final class Singleton {
    private static Singleton instance;
    public String temp;
    public String tempFeelsLike;

    public Singleton(String temp, String tempFeelsLike)
    {
        this.temp          = temp;
        this.tempFeelsLike = tempFeelsLike;
    }

    public static Singleton getInstance(String temp, String tempFeelsLike)
    {
        if (instance == null)
            instance = new Singleton(temp, tempFeelsLike);
        return instance;
    }
}
