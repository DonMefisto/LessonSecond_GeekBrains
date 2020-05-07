package com.mefistophel.lessonsecond_geekbrains;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.mefistophel.lessonsecond_geekbrains.observer_city_fragment.Observer;
import com.mefistophel.lessonsecond_geekbrains.observer_city_fragment.Publisher;
import com.mefistophel.lessonsecond_geekbrains.observer_city_fragment.PublisherGetter;


public class CityList extends Fragment implements Observer {

    private Publisher publisher;

    public CityList() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListCities((LinearLayout) view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        publisher = ((PublisherGetter) context).getPublisher();
    }

    private void initListCities(final LinearLayout view) {
        LinearLayout linearLayout = view;
        String[] cities = getResources().getStringArray(R.array.cities);

        for (int i = 0; i < cities.length; i++)
        {
            final TextView txtNameCity = new TextView(getContext());
            txtNameCity.setText(cities[i]);
            txtNameCity.setTextSize(26);
            linearLayout.addView(txtNameCity);

            txtNameCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar snackbar = Snackbar
                            .make(view, "Do you want to change city to " + txtNameCity.getText().toString() + "?", Snackbar.LENGTH_LONG)
                            .setAction("Yes", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    publisher.notify(txtNameCity.getText().toString());
                                }
                            });
                    snackbar.show();
                }
            });
        }
    }

    @Override
    public void updateCity(String text) {
        publisher.notify(text);
    }
}
