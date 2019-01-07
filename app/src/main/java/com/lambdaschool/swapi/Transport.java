package com.lambdaschool.swapi;

import org.json.JSONException;
import org.json.JSONObject;

public class Transport {
    protected String name, model, manufacturer, category;
    protected long cost, maxSpeed;

    public Transport(String name, String model, String manufacturer, String category, int cost) {
        this.name = name;
        this.model = model;
        this.manufacturer = manufacturer;
        this.category = category;
        this.cost = cost;
    }

    public Transport(JSONObject json) {
        try {
            this.name = json.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.model = json.getString("model");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.manufacturer = json.getString("manufacturer");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.cost = json.getLong("cost_in_credits");
        } catch (JSONException e) {
            e.printStackTrace();
            this.cost = -1;
        }
//        this.category will be added by the child class
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return String.format("%s - %d kph", this.name, this.maxSpeed);
    }
}
