package edu.washington.prathh.campustour;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.List;

import edu.washington.prathh.campustour.WikiTude.Constants;

public class RssService extends IntentService {

	private static final String RSS_LINK = "https://www.trumba.com/calendars/sea_campus.rss?days=7&filterview=No+Ongoing+Events&filter1=_410446_410447_417393_410450_410452_410449_664739_410454_410453_410456_422909_422910_426704_&filter5=_409198_&filterfield1=30073&filterfield5=30051";
	public static final String ITEMS = "items";
	public static final String RECEIVER = "receiver";

	public RssService() {
		super("RssService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(Constants.TAG, "Service started");
		List<RssItem> rssItems = null;
		try {
			RssParser parser = new RssParser();
			rssItems = parser.parse(getInputStream(RSS_LINK));
		} catch (XmlPullParserException e) {
			Log.w(e.getMessage(), e);
		} catch (IOException e) {
			Log.w(e.getMessage(), e);
		}
		Bundle bundle = new Bundle();
		bundle.putSerializable(ITEMS, (Serializable) rssItems);
		ResultReceiver receiver = intent.getParcelableExtra(RECEIVER);
		receiver.send(0, bundle);
	}

	public InputStream getInputStream(String link) {
		try {
			URL url = new URL(link);
			return url.openConnection().getInputStream();
		} catch (IOException e) {
			Log.w(Constants.TAG, "Exception while retrieving the input stream", e);
			return null;
		}
	}
}
