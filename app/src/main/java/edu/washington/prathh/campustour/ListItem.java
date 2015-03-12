package edu.washington.prathh.campustour;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hillaryprather on 1/29/15.
 */
public class ListItem {
    private String buildingName;
    private String icon;
    private double lat;
    private double lon;
    private ArrayList<String> factoids;

    public ListItem(String item) {
        this(item, "", 47.655335, -122.30352, new ArrayList<String>()); // default is to middle of UW
    }

    public ListItem(String item, String icon, double lat, double lon, ArrayList<String> factoids) {
        this.buildingName = item;
        this.icon = icon;
        this.lat = lat;
        this.lon = lon;
        this.factoids = factoids;
    }

    public void setBuildingName(String itemName) {
        this.buildingName = itemName;
    }

    public String getBuildingName() {
        return this.buildingName;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }

    public double getLat() {
        return this.lat;
    }

    public double getLon() {
        return this.lon;
    }

    public ArrayList<String> getFactoids() {
        return this.factoids;
    }
}