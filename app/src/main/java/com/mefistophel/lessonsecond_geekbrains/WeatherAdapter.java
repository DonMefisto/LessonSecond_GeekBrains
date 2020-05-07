package com.mefistophel.lessonsecond_geekbrains;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private String [] dataTemp;

    public WeatherAdapter(String [] dataTemp){
        this.dataTemp = dataTemp;
    }

    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.temp_for_hour, parent,false);
        return new ViewHolder(view);
     }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder holder, int position) {
        holder.getTxtTemp().setText(dataTemp[position]);
    }

    @Override
    public int getItemCount() {
        return dataTemp.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTemp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTemp = (TextView) itemView;
        }

        public TextView getTxtTemp() {
            return txtTemp;
        }
    }
}
