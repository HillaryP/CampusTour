package edu.washington.prathh.campustour;

/**
 * Created by hillaryprather on 1/29/15.
 */
public class ListItem {
    private String itemName;
    private String subItem;

    public ListItem(String item) {
        this(item, "");
    }

    public ListItem(String item, String subItem) {
        this.itemName = item;
        this.subItem = subItem;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setSubItem(String subItem) {
        this.subItem = subItem;
    }

    public String getSubItem() {
        return this.subItem;
    }
}