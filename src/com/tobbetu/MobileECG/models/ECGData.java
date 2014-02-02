package com.tobbetu.MobileECG.models;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 01.02.2014
 * Time: 17:56
 */
public class ECGData {

    private Date date;
    private double latitude;
    private double longitude;
    private double ra_ll;
    private double la_ll;


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getRa_ll() {
        return ra_ll;
    }

    public void setRa_ll(double ra_ll) {
        this.ra_ll = ra_ll;
    }

    public double getLa_ll() {
        return la_ll;
    }

    public void setLa_ll(double la_ll) {
        this.la_ll = la_ll;
    }
}
