package com.example.sharad.new_stormy.weather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by sharad on 11-08-2015.
 */
public class Current {


    private String mIcon;
    private long mTime;
    private double mTemperature, mHumidity, mPrecipChance;
    private String msummary, mTimeZone;

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }



    public String getIcon() {
        return mIcon;
    }

    public int getIconId(){

        return Forecast.getIconId(mIcon);
    }
    public void setIcon(String icon) {
        mIcon = icon;
    }

    public long getTime() {
        return mTime;
    }


    public String getFormattedTime(){

        SimpleDateFormat formatter= new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date dateTime=new Date(getTime()*1000);
        String timeString= formatter.format(dateTime);

        return timeString;
    }
    public void setTime(long time) {
        mTime = time;
    }

    public int getTemperature() {
        return (int)Math.round((mTemperature-32)/1.8);
    }

    public void setTemperature(double temperature) {

        mTemperature = temperature;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public int getPrecipChance() {
        double precipPercentage= mPrecipChance*1000;
        return (int)Math.round(precipPercentage);
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance;
    }

    public String getSummary() {
        return msummary;
    }

    public void setSummary(String summary) {
        msummary = summary;
    }
}
