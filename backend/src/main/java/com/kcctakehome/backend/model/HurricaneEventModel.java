package com.kcctakehome.backend.model;


import java.util.ArrayList;
import java.util.List;

public class HurricaneEventModel {
    private final int year;
    private final String hurricaneName;
    private final List<HurricaneRecordModel> hurricaneRecords;

    public HurricaneEventModel(String hurricaneName, int year) {
        this.year = year;
        this.hurricaneName = hurricaneName;
        this.hurricaneRecords = new ArrayList<>();
    }

    public int getYear() {
        return year;
    }

    public String getHurricaneName() {
        return hurricaneName;
    }

    public List<HurricaneRecordModel> getHurricaneRecords() {
        return hurricaneRecords;
    }

    public void addRecord(HurricaneRecordModel hurricaneRecord) {
        hurricaneRecords.add(hurricaneRecord);
    }



}