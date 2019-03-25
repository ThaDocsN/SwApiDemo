package com.lambdaschool.swapi

import android.util.Log

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.concurrent.Semaphore
import java.util.concurrent.atomic.AtomicBoolean

object SwApiDao {
    interface ObjectCallback<T> {
        fun returnPlanets(`object`: T)
    }

    fun getAllPlanets(canceled: AtomicBoolean, objectCallback: ObjectCallback<ArrayList<Planet>>) {
        val planets = ArrayList<Planet>()
        val lock = Semaphore(1)
        val callback = object : NetworkAdapter.NetworkCallback {
            override fun returnResult(success: Boolean?, page: String) {
                // process page of data
                if (canceled.get()) {
                    Log.i("GetRequestCanceled", page)
                    return
                }

                var nextUrl: String? = null
                try {
                    nextUrl = JSONObject(page).getString("next")
                } catch (e: JSONException) {
                    e.printStackTrace()
                    nextUrl = null
                }

                // yay recursion!
                if (nextUrl != null) {
                    NetworkAdapter.httpGetRequest(nextUrl, canceled, this)
                }

                try {
                    val pageJson = JSONObject(page)
                    val resultsArray = pageJson.getJSONArray("results")

                    if (canceled.get()) {
                        Log.i("GetRequestCanceled", page)
                        return
                    }

                    for (i in 0 until resultsArray.length()) {
                        try {
                            lock.acquire()
                            planets.add(Planet(resultsArray.getJSONObject(i)))
                            lock.release()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                if (nextUrl == null) {
                    /*synchronized (planets) {
                        planets.notify();
                    }*/
                    objectCallback.returnPlanets(planets)
                }
            }
        }
        if (canceled.get()) {
            Log.i("GetRequestCanceled", "First")
            return
        }
        NetworkAdapter.httpGetRequest("https://swapi.co/api/planets", canceled, callback)
    }

    fun getAllTransports(objectCallback: ObjectCallback<Transport>) {
        val transports = ArrayList<Transport>()
        val lock = Semaphore(1)
        NetworkAdapter.httpGetRequest("https://swapi.co/api/vehicles", object : NetworkAdapter.NetworkCallback {
            override fun returnResult(success: Boolean?, page: String) {
                // process page of data
                var nextUrl: String? = "https://swapi.co/api/vehicles"
                try {
                    val pageJson = JSONObject(page)
                    val resultsArray = pageJson.getJSONArray("results")
                    for (i in 0 until resultsArray.length()) {
                        try {
                            lock.acquire()
                            transports.add(Vehicle(resultsArray.getJSONObject(i)))
                            objectCallback.returnPlanets(transports[transports.size - 1])
                            lock.release()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                try {
                    nextUrl = JSONObject(page).getString("next")
                    NetworkAdapter.httpGetRequest(nextUrl, this)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    nextUrl = null
                }

            }
        })

        NetworkAdapter.httpGetRequest("https://swapi.co/api/starships") { success, page ->
            // process page of data
            var nextUrl: String?
            try {
                val pageJson = JSONObject(page)
                val resultsArray = pageJson.getJSONArray("results")
                for (i in 0 until resultsArray.length()) {
                    try {
                        lock.acquire()
                        transports.add(Starship(resultsArray.getJSONObject(i)))
                        objectCallback.returnPlanets(transports[transports.size - 1])
                        lock.release()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            try {
                nextUrl = JSONObject(page).getString("next")
            } catch (e: JSONException) {
                e.printStackTrace()
                nextUrl = null
            }
        }
    }
}
