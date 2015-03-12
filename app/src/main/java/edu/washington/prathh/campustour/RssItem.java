package edu.washington.prathh.campustour;

import android.text.Html;

/**
 * A representation of an rss item from the list.
 * 
 * @author Veaceslav Grec
 * 
 */
public class RssItem {

	private final String title;
	private final String link;
    private final String description;
    private final String date;

	public RssItem(String title, String link, String description) {


		this.title = title;
		this.link = link;
        String desc = description;
        String dt = "";
        if (desc != null && desc.length() > 0) {
            desc = Html.fromHtml(desc.replaceAll("<img.+?>", "")).toString();
            int startInfo = desc.indexOf("Campus location:");
            String extraInfo = "";
            if (startInfo != -1) {
                extraInfo = desc.substring(startInfo);
                desc = desc.substring(0, startInfo);
            }
            int toDate = desc.indexOf("\n");
            if (toDate != -1) {
                dt = desc.substring(0, toDate);
                desc = desc.substring(toDate).trim();
            }

        }
        if (desc.length() > 200) {
            desc = desc.substring(0, 200) + "...";
        }
        this.description = desc;
        this.date = dt;
	}

	public String getTitle() {
		return title;
	}

	public String getLink() {
		return link;
	}

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

}
