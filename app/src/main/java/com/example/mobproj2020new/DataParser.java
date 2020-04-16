package com.example.mobproj2020new;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {
    public List<List<HashMap<String, String>>> parse(JSONObject jObject) {
        List<List<HashMap<String, String>>> routes = new ArrayList<>();
        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;
        JSONObject jDistance;
        JSONObject jDuration;
        long totalDistance = 0;
        int totalSeconds = 0;
        int myIndex = 0;
        List<HashMap<String,String>> myList = new ArrayList<>();
        try {
            jRoutes = jObject.getJSONArray("routes");
            /** Traversing all routes */
            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<>();
                /** Traversing all legs */
                for (int j = 0; j < jLegs.length(); j++) {
                    jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                    //Kokonaismatkan haku jsonista
                    jDistance = ((JSONObject) jLegs.get(j)).getJSONObject("distance");
                    totalDistance = totalDistance + Long.parseLong(jDistance.getString("value"));

                    //Kokonaisajan haku jsonista
                    jDuration = ((JSONObject) jLegs.get(j)).getJSONObject("duration");
                    totalSeconds = totalSeconds + Integer.parseInt(jDuration.getString("value"));

                    /** Traversing all steps */
                    for (int k = 0; k < jSteps.length(); k++) {
                        String polyline = "";
                        polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        /** Traversing all points */
                        for (int l = 0; l < list.size(); l++) {
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("lat", Double.toString((list.get(l)).latitude));
                            hm.put("lng", Double.toString((list.get(l)).longitude));
                            path.add(hm);
                            if(myIndex % 100 == 0)
                            {
                                myList.add(hm);
                            }
                            myIndex++;
                        }
                    }
                    routes.add(path);

                    //matkan pituuden määritys
                    double dist = totalDistance / 1000.0;
                    Constant.DISTANCE = String.valueOf(dist);

                    //matka ajan määritys
                    int hours = (totalSeconds / 3600);
                    int minutes = ((totalSeconds - hours * 3600) / 60);
                    Constant.DURATION = String.valueOf(hours + "h " + minutes + "min");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
        Constant.pointsList = myList;
        return routes;
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}