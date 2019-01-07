package com.lambdaschool.swapi;

import org.json.JSONException;
import org.json.JSONObject;

public class Vehicle extends Transport {
    public Vehicle(JSONObject json) {
        super(json);
        try {
            super.maxSpeed = json.getLong("max_atmosphering_speed");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
