package com.lambdaschool.swapi

import org.json.JSONException
import org.json.JSONObject

class Vehicle(json: JSONObject) : Transport(json) {
    init {
        try {
            super.maxSpeed = json.getLong("max_atmosphering_speed")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }
}
