package com.example.sharad.new_stormy.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.sharad.new_stormy.R;
import com.example.sharad.new_stormy.adapter.HourAdapter;
import com.example.sharad.new_stormy.weather.Hour;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sharad on 11-08-2015.
 */
public class HourlyForecastActivity extends Activity {

    private Hour[] mHours;

    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecast);
        ButterKnife.bind(this);

        Intent intent= getIntent();
        Parcelable[] parcelables= intent.getParcelableArrayExtra(MyActivity.HOURLY_FORECAST);
        mHours= Arrays.copyOf(parcelables, parcelables.length, Hour[].class);

        HourAdapter adapter= new HourAdapter(mHours);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);
    }

}

