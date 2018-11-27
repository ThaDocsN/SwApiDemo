package com.lambdaschool.swapi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SwApiDao {
    public static ArrayList<Planet> getAllPlanets() {
        ArrayList<Planet> planets = new ArrayList<>();
        String nextUrl = "https://swapi.co/api/planets";
        while(nextUrl != null) {
            String page = NetworkAdapter.httpGetRequest(nextUrl);

            // process page of data
            try {
                JSONObject pageJson = new JSONObject(page);
                JSONArray resultsArray = pageJson.getJSONArray("results");
                for(int i = 0; i < resultsArray.length(); ++i) {
                    try {
                        planets.add(new Planet(resultsArray.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                nextUrl = new JSONObject(page).getString("next");
            } catch (JSONException e) {
                e.printStackTrace();
                nextUrl = null;
            }
        }
        return planets;
    }
}
