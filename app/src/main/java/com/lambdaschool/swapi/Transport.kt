package com.lambdaschool.swapi

import org.json.JSONException
import org.json.JSONObject

open class Transport {
    var name: String
    var model: String
    var manufacturer: String
    var category: String
    var cost: Long = 0
    protected var maxSpeed: Long = 0

    constructor(name: String, model: String, manufacturer: String, category: String, cost: Int) {
        this.name = name
        this.model = model
        this.manufacturer = manufacturer
        this.category = category
        this.cost = cost.toLong()
    }

    constructor(json: JSONObject) {
        try {
            this.name = json.getString("name")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        try {
            this.model = json.getString("model")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        try {
            this.manufacturer = json.getString("manufacturer")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        try {
            this.cost = json.getLong("cost_in_credits")
        } catch (e: JSONException) {
            e.printStackTrace()
            this.cost = -1
        }

        //        this.category will be added by the child class
    }

    override fun toString(): String {
        return String.format("%s - %d kph", this.name, this.maxSpeed)
    }
}
