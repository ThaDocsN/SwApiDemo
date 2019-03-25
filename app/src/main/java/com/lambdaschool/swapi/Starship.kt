package com.lambdaschool.swapi

import org.json.JSONException
import org.json.JSONObject

class Starship(json: JSONObject) : Transport(json) {
    protected var hyperdriveRating: Double = 0.toDouble()

    init {
        try {
            this.hyperdriveRating = json.getDouble("hyperdrive_rating")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        super.maxSpeed = calculateMaxSpeed().toLong()
    }

    private fun calculateMaxSpeed(): Double {
        return 1.079e+9 * this.hyperdriveRating
    }
}
