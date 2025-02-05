package com.kcctakehome.backend.service;

import com.kcctakehome.backend.model.HurricaneEventModel;
import com.kcctakehome.backend.model.HurricaneRecordModel;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class HurricaneService {
    private static final String HURRICANE_DATA_URL = "https://www.nhc.noaa.gov/data/hurdat/hurdat2-1851-2023-051124.txt";

    public List<HurricaneEventModel> fetchData() {
        List<HurricaneEventModel> hurricaneData = new ArrayList<>();
        try {

            URL url = new URL(HURRICANE_DATA_URL);

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String hurricaneInput;

            while ((hurricaneInput = in.readLine()) != null) {
                hurricaneInput = hurricaneInput.trim();
                String[] hurricaneContent = hurricaneInput.split("\\s*,\\s*");

                if (hurricaneContent[0].startsWith("AL")) {
                    int hurricaneYear = Integer.parseInt(hurricaneContent[0].substring(4).trim());
                    String hurricaneName = hurricaneContent[1].trim();
                    int eventRecords = Integer.parseInt(hurricaneContent[2].trim());
                    HurricaneEventModel newHurricane = new HurricaneEventModel(hurricaneName, hurricaneYear);
                    for (int i = 0; i < eventRecords; i++) {
                        String newRecordLine = in.readLine().trim();
                        String[] newRecord = newRecordLine.split("\\s*,\\s*");
                        newHurricane.addRecord(parseHurricaneRecord(newRecord));
                    }
                    hurricaneData.add(newHurricane);
                }

            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hurricaneData;
    }

    public List<HurricaneEventModel> fetchFilteredByYearByLandfallByLocation() {
        List<HurricaneEventModel> filteredHurricaneEvents = new ArrayList<>();
        List<HurricaneEventModel> hurricaneEvents = fetchData();

        double FL_MIN_LAT = 24.396308; // Southernmost (Key West)
        double FL_MAX_LAT = 31.000968; // Northernmost (Georgia border)
        double FL_MIN_LON = -87.634896; // Westernmost (Perdido River)
        double FL_MAX_LON = -79.974307; // Easternmost (Atlantic boundary)

        for (HurricaneEventModel hurricaneEvent : hurricaneEvents) {
            if (hurricaneEvent.getYear() >= 1990) {
                for (HurricaneRecordModel hurricaneCurrentRecord : hurricaneEvent.getHurricaneRecords()) {

                    if (hurricaneCurrentRecord.checkIsLandFall() && ((getLat(hurricaneCurrentRecord.getHurricaneRecordLatitude()) >= FL_MIN_LAT && getLat(hurricaneCurrentRecord.getHurricaneRecordLatitude()) <= FL_MAX_LAT && getLong(hurricaneCurrentRecord.getHurricaneRecordLongitude()) >= FL_MIN_LON && getLong(hurricaneCurrentRecord.getHurricaneRecordLongitude()) <= FL_MAX_LON))) {
                        if(!filteredHurricaneEvents.contains(hurricaneEvent))
                        {
                            filteredHurricaneEvents.add(hurricaneEvent);
                        }
                    }
                }
            }

        }
        return filteredHurricaneEvents;
    }

    public HurricaneRecordModel getMaxSpeed(List<HurricaneRecordModel> hurricaneEvents) {
        HurricaneRecordModel finalMaxSpeedEvent = null;
        ArrayList<HurricaneRecordModel> maxSpeedEvents = new ArrayList<>();
        int maxSpeed = 0;
        for (HurricaneRecordModel currentEvent : hurricaneEvents) {
            if (currentEvent.checkIsLandFall()) {
                int currentSpeed = currentEvent.getHurricaneRecordWindSpeed();
                if (finalMaxSpeedEvent == null || currentSpeed > maxSpeed) {
                    finalMaxSpeedEvent = currentEvent;
                    maxSpeed = currentSpeed;
                    maxSpeedEvents.clear();
                    maxSpeedEvents.add(currentEvent);
                } else if (currentSpeed == maxSpeed) {
                    if (!maxSpeedEvents.contains(finalMaxSpeedEvent)) {
                        maxSpeedEvents.add(finalMaxSpeedEvent);
                    }
                    maxSpeedEvents.add(currentEvent);

                }
            }
        }

            return maxSpeedEvents.stream()
                    .min((a, b) -> Integer.compare(a.getHurricaneRecordPressure(), b.getHurricaneRecordPressure()))
                    .orElse(finalMaxSpeedEvent);
    }

    private HurricaneRecordModel parseHurricaneRecord(String[] hurricaneRecordInput) {
        return new HurricaneRecordModel(hurricaneRecordInput[0].trim(), hurricaneRecordInput[1].trim(), hurricaneRecordInput[2].trim(), hurricaneRecordInput[4].trim(), hurricaneRecordInput[5].trim(), Integer.parseInt(hurricaneRecordInput[6].trim()), Integer.parseInt(hurricaneRecordInput[7].trim()));
    }

    public String parseHurricaneRecordDate(String date){
        return  "Month: " + date.substring(4,6) + " Day: " + date.substring(6) + " Year: " + date.substring(0,4);
    }

    double getLat(String lat) {
        if (lat.endsWith("S")) {
            return -Double.parseDouble(lat.substring(0, lat.length() - 1).trim());
        } else {
            return Double.parseDouble(lat.substring(0, lat.length() - 1).trim());
        }
    }

    double getLong(String lon) {
        if (lon.endsWith("W")) {
            return -Double.parseDouble(lon.substring(0, lon.length() - 1).trim());
        } else {
            return Double.parseDouble(lon.substring(0, lon.length() - 1).trim());
        }
    }


}