package com.lambdaschool.swapi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SwApiDao {
    public static ArrayList<Planet> getAllPlanets() {
        final ArrayList<Planet> planets = new ArrayList<>();
        final NetworkAdapter.NetworkCallback callback = new NetworkAdapter.NetworkCallback() {
            @Override
            public void returnResult(Boolean success, String page) {
                // process page of data
                String nextUrl = null;
                try {
                    nextUrl = new JSONObject(page).getString("next");
                } catch (JSONException e) {
                    e.printStackTrace();
                    nextUrl = null;
                }
                // yay recursion!
                if (nextUrl != null) {
                    NetworkAdapter.httpGetRequest(nextUrl, this);
                } else {
                    synchronized (planets) {
                        planets.notify();
                    }
                }

                try {
                    JSONObject pageJson     = new JSONObject(page);
                    JSONArray  resultsArray = pageJson.getJSONArray("results");
                    for (int i = 0; i < resultsArray.length(); ++i) {
                        try {
                            planets.add(new Planet(resultsArray.getJSONObject(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        NetworkAdapter.httpGetRequest("https://swapi.co/api/planets", callback);
        return planets;
    }

    /*public static ArrayList<Transport> getAllTransports() {
        ArrayList<Transport> transports = new ArrayList<>();
        String               nextUrl    = "https://swapi.co/api/vehicles";
        while (nextUrl != null) {
            String page = NetworkAdapter.httpGetRequest(nextUrl);

            // process page of data
            try {
                JSONObject pageJson     = new JSONObject(page);
                JSONArray  resultsArray = pageJson.getJSONArray("results");
                for (int i = 0; i < resultsArray.length(); ++i) {
                    try {
                        transports.add(new Vehicle(resultsArray.getJSONObject(i)));
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
        nextUrl = "https://swapi.co/api/starships";
        while (nextUrl != null) {
            String page = NetworkAdapter.httpGetRequest(nextUrl);

            // process page of data
            try {
                JSONObject pageJson     = new JSONObject(page);
                JSONArray  resultsArray = pageJson.getJSONArray("results");
                for (int i = 0; i < resultsArray.length(); ++i) {
                    try {
                        transports.add(new Vehicle(resultsArray.getJSONObject(i)));
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
        return transports;
    }*/
}
