package edu.washington.prathh.campustour;

import android.sax.Element;
import android.text.Html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
    private final String imageUrl;
    public static final String uwIconUrl = "https://nbccollegebasketballtalk.files.wordpress.com/2014/06/uw-icon-20x20.gif";

	public RssItem(String title, String link, String description) {


		this.title = title;
		this.link = link;
        String desc = description;
        String html = "";
        String imgUrl = null;
        String dt = "";
        if (desc != null && desc.length() > 0) {
            // desc = Html.fromHtml(desc.replaceAll("<img.+?>", "")).toString();
            html = desc;
            Document doc = Jsoup.parse(html);
            org.jsoup.nodes.Element image = doc.select("img").first();
            if (image != null) {
                imgUrl = image.attr("href");
            }

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
        this.imageUrl = imgUrl;
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

    public String getImageUrl() {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return uwIconUrl;
        } else {
            return imageUrl;
        }
    }

}
