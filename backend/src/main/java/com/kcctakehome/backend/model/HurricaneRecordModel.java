package com.kcctakehome.backend.model;


public class HurricaneRecordModel {
    private final String hurricaneRecordDate;
    private final String hurricaneRecordLatitude;
    private final String hurricaneRecordLongitude;
    private final int hurricaneRecordWindSpeed;
    private final int hurricaneRecordPressure;
    private final String landFallIndicator;

    public HurricaneRecordModel(String hurricaneRecordDate, String hurricaneRecordTime, String landFallIndicator, String hurricaneRecordLatitude, String hurricaneRecordLongitude, int hurricaneRecordWindSpeed, int hurricaneRecordPressure) {
        this.hurricaneRecordDate = hurricaneRecordDate;
        this.hurricaneRecordLatitude = hurricaneRecordLatitude;
        this.hurricaneRecordLongitude = hurricaneRecordLongitude;
        this.hurricaneRecordWindSpeed = hurricaneRecordWindSpeed;
        this.hurricaneRecordPressure = hurricaneRecordPressure;
        this.landFallIndicator = landFallIndicator;
    }


    public String getLandFallIndicator() {
        return landFallIndicator;
    }

    public int getHurricaneRecordPressure() {
        return hurricaneRecordPressure;
    }

    public int getHurricaneRecordWindSpeed() {
        return hurricaneRecordWindSpeed;
    }

    public String getHurricaneRecordLongitude() {
        return hurricaneRecordLongitude;
    }

    public String getHurricaneRecordLatitude() {
        return hurricaneRecordLatitude;
    }


    public String getHurricaneRecordDate() {
        return hurricaneRecordDate;
    }

    public boolean checkIsLandFall() {
        return landFallIndicator.equals("L");
    }


}

