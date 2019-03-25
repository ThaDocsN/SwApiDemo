package com.lambdaschool.swapi

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Planet(json: JSONObject) {
    var edited: String
    var terrain: String
    var diameter: String
    var films: Array<String>
    var url: String
    var surface_water: String
    var orbital_period: String
    var created: String
    var name: String
    var rotation_period: String
    var climate: String
    var gravity: String
    var population: String
    var residents: Array<String>

    init {
        try {
            this.edited = json.getString("edited")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        try {
            this.terrain = json.getString("terrain")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        try {
            this.diameter = json.getString("diameter")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        try {
            val jsonFilms = json.getJSONArray("films")
            this.films = arrayOfNulls(jsonFilms.length())
            for (i in 0 until jsonFilms.length()) {
                this.films[i] = jsonFilms.getString(i)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        try {
            this.url = json.getString("url")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        try {
            this.surface_water = json.getString("surface_water")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        try {
            this.orbital_period = json.getString("orbital_period")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        try {
            this.created = json.getString("created")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        try {
            this.name = json.getString("name")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        try {
            this.rotation_period = json.getString("rotation_period")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        try {
            this.climate = json.getString("climate")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        try {
            this.gravity = json.getString("gravity")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        try {
            this.population = json.getString("population")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        try {
            val residentsArray = json.getJSONArray("residents")
            this.residents = arrayOfNulls(residentsArray.length())
            for (i in 0 until residentsArray.length()) {
                this.residents[i] = residentsArray.getString(i)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    override fun toString(): String {
        return "ClassPojo [edited = $edited, terrain = $terrain, diameter = $diameter, films = $films, url = $url, surface_water = $surface_water, orbital_period = $orbital_period, created = $created, name = $name, rotation_period = $rotation_period, climate = $climate, gravity = $gravity, population = $population, residents = $residents]"
    }
}
