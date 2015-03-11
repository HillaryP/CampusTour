package edu.washington.prathh.campustour;

/**
 * Created by hillaryprather on 1/29/15.
 */
public class ListItem {
    private String buildingName;
    private String icon;
    private double lat;
    private double lon;

    public ListItem(String item) {
        this(item, "", 47.655335, -122.30352); // default is to middle of UW
    }

    public ListItem(String item, String icon, double lat, double lon) {
        this.buildingName = item;
        this.icon = icon;
        this.lat = lat;
        this.lon = lon;
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
}