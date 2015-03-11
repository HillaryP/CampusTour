package edu.washington.prathh.campustour;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListItemAdapter extends ArrayAdapter {
    private int resource;
    private LayoutInflater inflater;
    private Context context;

    public ListItemAdapter(Context ctx, int resourceId, List objects) {

        super( ctx, resourceId, objects );
        resource = resourceId;
        inflater = LayoutInflater.from( ctx );
        context=ctx;
    }

    @Override
    public View getView ( int position, View convertView, ViewGroup parent ) {
        convertView = (LinearLayout) inflater.inflate( resource, null );
        ListItem item = (ListItem) getItem( position );

        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        imageView.setImageResource(R.drawable.logo);
        TextView itemString = (TextView) convertView.findViewById(R.id.building_name);
        itemString.setText(item.getBuildingName());
        return convertView;
    }
}