package edu.washington.prathh.campustour;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

public class EventsActivity extends FragmentActivity {


    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.events_main);

		if (savedInstanceState == null) {
			//addRssFragment();
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            listView = (ListView) findViewById(R.id.listView);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i("TAG", "Set listener");
                    RssAdapter adapter = (RssAdapter) parent.getAdapter();
                    RssItem item = (RssItem) adapter.getItem(position);
                    Uri uri = Uri.parse(item.getLink());
                    Log.i("TAG", item.getLink());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
            startService();
		}
	}

	private void addRssFragment() {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		RssFragment fragment = new RssFragment();
		transaction.add(R.id.fragment_container, fragment);
		transaction.commit();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("fragment_added", true);
	}


    private ProgressBar progressBar;
    private ListView listView;
    private View view;

    private void startService() {
        Intent intent = new Intent(this, RssService.class);
        intent.putExtra(RssService.RECEIVER, resultReceiver);
        startService(intent);
    }

    /**
     * Once the {@link RssService} finishes its task, the result is sent to this
     * ResultReceiver.
     */
    private final ResultReceiver resultReceiver = new ResultReceiver(new Handler()) {
        @SuppressWarnings("unchecked")
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            progressBar.setVisibility(View.GONE);
            List<RssItem> items = (List<RssItem>) resultData.getSerializable(RssService.ITEMS);
            if (items != null) {
                RssAdapter adapter = new RssAdapter(EventsActivity.this, items);
                listView.setAdapter(adapter);
            } else {
                Toast.makeText(EventsActivity.this, "An error occured while downloading the rss feed.",
                        Toast.LENGTH_LONG).show();
            }
        };
    };

}