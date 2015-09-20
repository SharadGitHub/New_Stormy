package com.example.sharad.new_stormy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sharad.new_stormy.R;
import com.example.sharad.new_stormy.weather.Hour;

/**
 * Created by sharad on 01-08-2015.
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {

    private Hour[] mHours;

    public HourAdapter(Hour[] hours){
        mHours=hours;
    }
    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.hourly_list_item, viewGroup, false);
        HourViewHolder viewHolder= new HourViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HourViewHolder hourViewHolder, int position) {
        //this is a bridge between adapter and the Bind method we created in the ViewHolder class
        hourViewHolder.bindHour(mHours[position]);

    }

    @Override
    public int getItemCount() {
        return mHours.length;
    }

    public class HourViewHolder extends RecyclerView.ViewHolder{

        public TextView mTimeLabel;
        public TextView mSummaryLabel;
        public TextView mTemperatureLabel;
        public ImageView mIconImageView;

        public HourViewHolder(View itemView) {
            super(itemView);

            mTimeLabel= (TextView) itemView.findViewById(R.id.timeLabel);
            mTemperatureLabel= (TextView) itemView.findViewById(R.id.temperatureLabel);
            mSummaryLabel= (TextView) itemView.findViewById(R.id.summaryLabel);
            mIconImageView= (ImageView) itemView.findViewById(R.id.iconImageView);

        }

        public void bindHour(Hour hour){

            mTimeLabel.setText(hour.getHour());
            mTemperatureLabel.setText(hour.getTemperature()+"");
            mSummaryLabel.setText(hour.getSummary());
            mIconImageView.setImageResource(hour.getIconId());
        }
    }
}
