package com.hadar.finalproject;

import java.io.Serializable;

public class FlightDataItem implements Serializable {
    private String expectedLandingTime;
    private String city;
    private String airport;
    private int logoID;
    private String flightNumber;
    private String realLandingTime;
    private String landingStatus;

    public FlightDataItem() {
    }

    public FlightDataItem(String expectedLandingTime, String city,
                          String airport, String flightNumber, String realLandingTime) {
        this.expectedLandingTime = expectedLandingTime;
        this.city = city;
        this.airport = airport;
        this.flightNumber = flightNumber;
        this.realLandingTime = realLandingTime;
    }

    public String getExpectedLandingTime() {
        return expectedLandingTime;
    }

    public void setExpectedLandingTime(String expectedLandingTime) {
        this.expectedLandingTime = expectedLandingTime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public int getLogoID() {
        int logo = 0;

        switch (this.city) {
            case "ROME":
            case "STOCKHOLM":
            case "EDINBURGH":
            case "MADRID":
            case "AMSTERDAM":
                logo = R.drawable.british;
                break;
            case "WARSAW":
            case "LONDON":
            case "LOS ANGELES":
            case "CHICAGO":
                logo = R.drawable.lot;
                break;
            case "DOHA":
            case "AUSTRALIA":
            case "BRAZIL":
                logo = R.drawable.qatar;
                break;
            case "JERUSALEM":
            case "EILAT":
                logo = R.drawable.el_al;
                break;
            default:
                break;
        }
        return logo;
    }

    public void setLogoID(int logoID) {
        this.logoID = logoID;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getRealLandingTime() {
        return realLandingTime;
    }

    public void setRealLandingTime(String realLandingTime) {
        this.realLandingTime = realLandingTime;
    }

    public String getLandingStatus() {
        return landingStatus;
    }

    public void setLandingStatus(String landingStatus) {
        this.landingStatus = landingStatus;
    }

    @Override
    public String toString() {
        return "approximate time: " + this.expectedLandingTime +
                " city: " + this.city +
                " airport: " + this.airport +
                " flightNumber: " + this.flightNumber;
    }
}
