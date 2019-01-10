package com.lambdaschool.swapi;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class SwApiDao {
    public interface ObjectCallback<T> {
        void returnPlanets(T object);
    }

    public static void getAllPlanets(final AtomicBoolean canceled, final ObjectCallback<ArrayList<Planet>> objectCallback) {
        final ArrayList<Planet> planets = new ArrayList<>();
        final Semaphore lock = new Semaphore(1);
        final NetworkAdapter.NetworkCallback callback = new NetworkAdapter.NetworkCallback() {
            @Override
            public void returnResult(Boolean success, String page) {
                // process page of data
                if(canceled.get()) {
                    Log.i("GetRequestCanceled", page);
                    return;
                }

                String nextUrl = null;
                try {
                    nextUrl = new JSONObject(page).getString("next");
                } catch (JSONException e) {
                    e.printStackTrace();
                    nextUrl = null;
                }
                // yay recursion!
                if (nextUrl != null) {
                    NetworkAdapter.httpGetRequest(nextUrl, canceled, this);
                }

                try {
                    JSONObject pageJson     = new JSONObject(page);
                    JSONArray  resultsArray = pageJson.getJSONArray("results");

                    if(canceled.get()) {
                        Log.i("GetRequestCanceled", page);
                        return;
                    }

                    for (int i = 0; i < resultsArray.length(); ++i) {
                        try {
                            lock.acquire();
                            planets.add(new Planet(resultsArray.getJSONObject(i)));
                            lock.release();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
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
        if(canceled.get()) {
            Log.i("GetRequestCanceled", "First");
            return;
        }
        NetworkAdapter.httpGetRequest("https://swapi.co/api/planets", canceled, callback);
    }

    public static void getAllTransports(final ObjectCallback<Transport> objectCallback) {
        final ArrayList<Transport> transports = new ArrayList<>();
        final Semaphore lock = new Semaphore(1);
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
                            lock.acquire();
                            transports.add(new Vehicle(resultsArray.getJSONObject(i)));
                            objectCallback.returnPlanets(transports.get(transports.size() - 1));
                            lock.release();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
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
                            lock.acquire();
                            transports.add(new Starship(resultsArray.getJSONObject(i)));
                            objectCallback.returnPlanets(transports.get(transports.size() - 1));
                            lock.release();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
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
