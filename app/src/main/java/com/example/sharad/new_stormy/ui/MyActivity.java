package com.example.sharad.new_stormy.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharad.new_stormy.R;
import com.example.sharad.new_stormy.weather.Current;
import com.example.sharad.new_stormy.weather.Day;
import com.example.sharad.new_stormy.weather.Forecast;
import com.example.sharad.new_stormy.weather.Hour;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MyActivity extends Activity {

    public static String TAG= MyActivity.class.getSimpleName();
    public static String DAILY_FORECAST= "DAILY_FORECAST";
    public static String HOURLY_FORECAST= "HOURLY_FORECAST";
    private Forecast mForecast;
    double lattitude, longitude;
    String address;


    @Bind(R.id.timeLabel) TextView mTimeLabel;
    @Bind(R.id.locationLabel) TextView mLocationLabel;
    @Bind(R.id.temperatureLabel) TextView mTemperatureLabel;
    @Bind(R.id.humidityValue) TextView mHumidityValue;
    @Bind(R.id.precipValue) TextView mPrecipValue;
    @Bind(R.id.summaryLabel) TextView mSummaryLabel;
    @Bind(R.id.iconImageView) ImageView mIconImageView;
    @Bind(R.id.refreshImageView) ImageView mRefreshImageView;
    @Bind(R.id.progressBar) ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.INVISIBLE);

        Intent intent=getIntent();
        String place=intent.getExtras().getString("key");

        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(lattitude, longitude);
            }
        });


        //getForecast(lattitude, longitude);
        getData(place);

        Log.d(TAG, "main code is working");

    }

    private Forecast parseForecastDetails(String jsonData) throws JSONException{

        Forecast forecast= new Forecast();
        forecast.setCurrent(getCurrentDetails(jsonData));
        forecast.setHourlyForecast(getHourlyForecast(jsonData));
        forecast.setDailyForecast(getDailyForecast(jsonData));
        return forecast;
    }

    private Day[] getDailyForecast(String jsonData)throws JSONException {

        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject daily= forecast.getJSONObject("daily");
        JSONArray data= daily.getJSONArray("data");

        Day[] days= new Day[data.length()];
        for(int i=0; i< data.length(); i++)
        {
            JSONObject jsonDay= data.getJSONObject(i);
            Day day= new Day();

            day.setSummary(jsonDay.getString("summary"));
            day.setIcon(jsonDay.getString("icon"));
            day.setTime(jsonDay.getLong("time"));
            day.setTemperatureMax(jsonDay.getDouble("temperatureMax"));
            day.setTimezone(timezone);

            days[i]=day;
        }
        return days;
    }

    private Hour[] getHourlyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject hourly= forecast.getJSONObject("hourly");
        JSONArray data= hourly.getJSONArray("data");

        Hour[] hours= new Hour[data.length()];

        for(int i=0; i<data.length(); i++){
            JSONObject jsonHour= data.getJSONObject(i);
            Hour hour= new Hour();

            hour.setSummary(jsonHour.getString("summary"));
            hour.setTemperature(jsonHour.getDouble("temperature"));
            hour.setIcon(jsonHour.getString("icon"));
            hour.setTime(jsonHour.getLong("time"));
            hour.setTimezone(timezone);


            hours[i]= hour;
        }
        return hours;
    }

    private void getForecast(double latitude, double longitude) {
        String apiKey= "93b18e6667b432dad83ecf4c6b3e35ce";
        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey +
                "/" + latitude + "," + longitude;

        if (isNetworkAvailable()) {
            toggleRefresh();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });

                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mForecast= parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUserAboutError();
                        }
                    }
                    catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                    catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        }
        else {
            Toast.makeText(this, getString(R.string.network_unavailable_message),
                    Toast.LENGTH_LONG).show();
        }
    }


    private void toggleRefresh() {
        if(mProgressBar.getVisibility()==View.INVISIBLE){
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        }else{
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }

    }



    private void updateDisplay() {
        Current current= mForecast.getCurrent();
        mTemperatureLabel.setText(current.getTemperature() + "");
        mTimeLabel.setText("At " + current.getFormattedTime() + " it will be");
        mHumidityValue.setText(current.getHumidity() + "");
        mPrecipValue.setText(current.getPrecipChance() + "%");
        mSummaryLabel.setText(current.getSummary());

        Drawable drawable = getResources().getDrawable(current.getIconId());
        mIconImageView.setImageDrawable(drawable);
    }




    private Current getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG, "From JSON: " + timezone);

        JSONObject currently = forecast.getJSONObject("currently");

        Current current = new Current();
        current.setHumidity(currently.getDouble("humidity"));
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setTimeZone(timezone);

        Log.d(TAG, current.getFormattedTime());

        return current;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    @OnClick(R.id.dailyButton)
    public void startDailyActivity(View view){
        Intent intent= new Intent(MyActivity.this, DailyForecastActivity.class);
        intent.putExtra(DAILY_FORECAST, mForecast.getDailyForecast());
        startActivity(intent);
    }

    @OnClick(R.id.hourlyButton)
    public void startHourlyActivity(View view){
        Intent intent = new Intent(this, HourlyForecastActivity.class);
        intent.putExtra(HOURLY_FORECAST, mForecast.getHourlyForecast());
        startActivity(intent);
    }

    private void getData(String location) {


        if(isNetworkAvailable()){

            String locationurl = "https://maps.googleapis.com/maps/api/geocode/json?address="+ location+"&sensor=true";

            OkHttpClient newclient= new OkHttpClient();
            Request request= new Request.Builder()
                    .url(locationurl)
                    .build();

            Call call= newclient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.v(TAG,"failure");
                }

                @Override
                public void onResponse(Response response) throws IOException {

                    if(response.isSuccessful()){

                        String data =response.body().string();
                        checking(data);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(MyActivity.this, lattitude + ":"+longitude, Toast.LENGTH_LONG).show();
                                mLocationLabel.setText(address);
                                getForecast(lattitude , longitude);
                            }
                        });

                        Log.v(TAG, data);
                    }
                }
            });
        }else{
            Toast.makeText(MyActivity.this, "No network Connection", Toast.LENGTH_LONG).show();
        }

        Log.v(TAG, "main thread is running");

    }


    void checking(String data){
        try {
            JSONObject returndata = new JSONObject(data);
            String status= returndata.getString("status");
            JSONArray result=returndata.getJSONArray("results");

            JSONObject geometry= result.getJSONObject(0);

            address= geometry.getString("formatted_address");
            JSONObject maingeometry=geometry.getJSONObject("geometry");
            JSONObject mylocation= maingeometry.getJSONObject("location");
            lattitude=mylocation.getDouble("lat");
            longitude=mylocation.getDouble("lng");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
