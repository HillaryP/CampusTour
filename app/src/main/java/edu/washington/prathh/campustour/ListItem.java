package edu.washington.prathh.campustour;

/**
 * Created by hillaryprather on 1/29/15.
 */
public class ListItem {
    private String buildingName;
    private String icon;

    public ListItem(String item) {
        this(item, "");
    }

    public ListItem(String item, String icon) {
        this.buildingName = item;
        this.icon = icon;
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
}