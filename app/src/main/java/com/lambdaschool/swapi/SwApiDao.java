package com.lambdaschool.swapi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SwApiDao {
    public interface ObjectCallback<T> {
        void returnPlanets(T object);
    }

    public static void getAllPlanets(final ObjectCallback<ArrayList<Planet>> objectCallback) {
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

                if (nextUrl == null) {
                    /*synchronized (planets) {
                        planets.notify();
                    }*/
                    objectCallback.returnPlanets(planets);
                }
            }
        };
        NetworkAdapter.httpGetRequest("https://swapi.co/api/planets", callback);
    }

    public static void getAllTransports(final ObjectCallback<Transport> objectCallback) {
        final ArrayList<Transport> transports = new ArrayList<>();
        NetworkAdapter.httpGetRequest("https://swapi.co/api/vehicles", new NetworkAdapter.NetworkCallback() {
            @Override
            public void returnResult(Boolean success, String page) {
                // process page of data
                String nextUrl = "https://swapi.co/api/vehicles";
                try {
                    JSONObject pageJson     = new JSONObject(page);
                    JSONArray  resultsArray = pageJson.getJSONArray("results");
                    for (int i = 0; i < resultsArray.length(); ++i) {
                        try {
                            transports.add(new Vehicle(resultsArray.getJSONObject(i)));
                            objectCallback.returnPlanets(transports.get(transports.size() - 1));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    nextUrl = new JSONObject(page).getString("next");
                    NetworkAdapter.httpGetRequest(nextUrl, this);
                } catch (JSONException e) {
                    e.printStackTrace();
                    nextUrl = null;
                }
            }
        });

        NetworkAdapter.httpGetRequest("https://swapi.co/api/starships", new NetworkAdapter.NetworkCallback() {
            @Override
            public void returnResult(Boolean success, String page) {
                // process page of data
                String nextUrl;
                try {
                    JSONObject pageJson     = new JSONObject(page);
                    JSONArray  resultsArray = pageJson.getJSONArray("results");
                    for (int i = 0; i < resultsArray.length(); ++i) {
                        try {
                            transports.add(new Starship(resultsArray.getJSONObject(i)));
                            objectCallback.returnPlanets(transports.get(transports.size() - 1));
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
        });
    }
}
