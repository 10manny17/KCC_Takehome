package com.kcctakehome.backend.service;

import com.kcctakehome.backend.model.HurricaneEventModel;
import com.kcctakehome.backend.model.HurricaneRecordModel;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * HurricaneService.java
 * <p>
 * This class provides business logic for managing Hurricane operations, including
 * fetching, computing, and parsing data.
 *
 * @author Emmanuel Chalumeau
 * @version 1.0
 * @since 2025-02-05
 */
@Service
public class HurricaneService {
    private static final String HURRICANE_DATA_URL = "https://www.nhc.noaa.gov/data/hurdat/hurdat2-1851-2023-051124.txt";

    /**
     * Pulls hurricane data from site, and returns Hurricane Events since 1990 only.
     *
     * @return List of Hurricane Events since 1990.
     */
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
            throw new RuntimeException("Error fetching data: " + e.getMessage(), e);
        }

        return hurricaneData;
    }

    /**
     * Gets Hurricane events that are only: 1990-Now, Landfall, Florida
     *
     * @return List<HurricaneEventModel>
     */
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
                        if (!filteredHurricaneEvents.contains(hurricaneEvent)) {
                            filteredHurricaneEvents.add(hurricaneEvent);
                        }
                    }
                }
            }

        }
        return filteredHurricaneEvents;
    }

    /**
     * Calculates the maximum wind speed of a hurricane event by identifying
     * the landfall record. If multiple records have the same top speed,
     * the final selection is based on the lowest pressure.
     *
     * @param hurricaneEvents A list of hurricane event records.
     * @return The hurricane record with the highest wind speed,
     * with pressure used as a tiebreaker.
     */
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

    /**
     * Parses the input record data and maps it to the respective constructor fields
     * to create a HurricaneRecordModel associated with a hurricane event.
     *
     * @param hurricaneRecordInput The raw input data for the hurricane record.
     * @return A HurricaneRecordModel instance containing the parsed data.
     */
    private HurricaneRecordModel parseHurricaneRecord(String[] hurricaneRecordInput) {
        return new HurricaneRecordModel(hurricaneRecordInput[0].trim(), hurricaneRecordInput[1].trim(), hurricaneRecordInput[2].trim(), hurricaneRecordInput[4].trim(), hurricaneRecordInput[5].trim(), Integer.parseInt(hurricaneRecordInput[6].trim()), Integer.parseInt(hurricaneRecordInput[7].trim()));
    }

    /**
     * Converts the given date format into the format "Month: Day: Year".
     *
     * @param date The date to be formatted.
     * @return A string representation of the date in "Month: Day: Year" format.
     */
    public String parseHurricaneRecordDate(String date) {
        return "Month: " + date.substring(4, 6) + " Day: " + date.substring(6) + " Year: " + date.substring(0, 4);
    }

    /**
     * Converts the given latitude coordinates into a numerical value,
     * determining the sign based on the hemisphere (N for positive, S for negative).
     *
     * @param lat The latitude coordinate as a string.
     * @return The numerical latitude as a Double, where North is positive and South is negative.
     */
    double getLat(String lat) {
        if (lat.endsWith("S")) {
            return -Double.parseDouble(lat.substring(0, lat.length() - 1).trim());
        } else {
            return Double.parseDouble(lat.substring(0, lat.length() - 1).trim());
        }
    }

    /**
     * Converts the given longitude coordinates into a numerical value,
     * determining the sign based on the hemisphere (E for positive, W for negative).
     *
     * @param lon The longitude coordinate as a string.
     * @return The numerical longitude as a Double, where East is positive and West is negative.
     */
    double getLong(String lon) {
        if (lon.endsWith("W")) {
            return -Double.parseDouble(lon.substring(0, lon.length() - 1).trim());
        } else {
            return Double.parseDouble(lon.substring(0, lon.length() - 1).trim());
        }
    }


}