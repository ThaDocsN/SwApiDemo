package com.lambdaschool.swapi;

import org.json.JSONException;
import org.json.JSONObject;

public class Starship extends Transport {
    protected double hyperdriveRating;

    public Starship(JSONObject json) {
        super(json);
        try {
            this.hyperdriveRating = json.getDouble("hyperdrive_rating");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.maxSpeed = (long)calculateMaxSpeed();
    }

    private double calculateMaxSpeed() {
        return 1.079e+9 * this.hyperdriveRating;
    }
}
