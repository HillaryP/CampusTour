package edu.washington.prathh.campustour;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
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
            //holder.moreInfo = (TextView) convertView.findViewById(R.id.itemLink);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
        RssItem currItem = items.get(position);
		holder.itemTitle.setText(currItem.getTitle());
        holder.itemDate.setText(currItem.getDate());
        holder.descTitle.setText(currItem.getDescription());

//        SpannableString ss = new SpannableString(
//                "More Info");
//
//        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, 9,
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(new URLSpan(currItem.getLink()), 0, 9,
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//
//
//        //holder.moreInfo.setText(Html.fromHtml("<a href=\"" + currItem.getLink() + "\">More Info</a> "));
//        holder.moreInfo.setText(ss);
//        holder.moreInfo.setMovementMethod(LinkMovementMethod.getInstance());
		return convertView;
	}

	static class ViewHolder {
		TextView itemTitle;
        TextView descTitle;
        TextView itemDate;
        //TextView moreInfo;
	}

}
