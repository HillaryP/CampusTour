package edu.washington.prathh.campustour;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class RssAdapter extends BaseAdapter {

	private final List<RssItem> items;
	private final Context context;

	public RssAdapter(Context context, List<RssItem> items) {
		this.items = items;
		this.context = context;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.rss_item, null);
			holder = new ViewHolder();
			holder.itemTitle = (TextView) convertView.findViewById(R.id.itemTitle);
            holder.descTitle = (TextView) convertView.findViewById(R.id.itemDesc);
            holder.itemDate = (TextView) convertView.findViewById(R.id.itemDate);
            holder.icon = (WebView) convertView.findViewById(R.id.event_icon);
            //holder.moreInfo = (TextView) convertView.findViewById(R.id.itemLink);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
        RssItem currItem = items.get(position);
		holder.itemTitle.setText(currItem.getTitle());
        holder.itemDate.setText(currItem.getDate());
        holder.descTitle.setText(currItem.getDescription());
        holder.icon.loadData("<html><body><img src=\""+ currItem.getImageUrl() +"\" width=\"50\"/></body></html>", "text/html", null);
		return convertView;
	}

	static class ViewHolder {
		TextView itemTitle;
        TextView descTitle;
        TextView itemDate;
        WebView icon;
        //TextView moreInfo;
	}

}
