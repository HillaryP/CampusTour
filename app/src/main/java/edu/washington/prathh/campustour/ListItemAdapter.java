package edu.washington.prathh.campustour;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    public View getView ( final int position, View convertView, ViewGroup parent ) {
        convertView = (LinearLayout) inflater.inflate( resource, null );

        ListItem item = (ListItem) getItem( position );
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToInfo((ListItem) getItem(position));
            }
        });
        String category = item.getIcon();

        // Need to credit <div>Icons made by <a href="http://www.flaticon.com/authors/icons8" title="Icons8">Icons8</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a>
        // is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>
        switch (category) {
            case "library":
                imageView.setImageResource(R.drawable.library);
                break;
            case "recreation":
                imageView.setImageResource(R.drawable.recreation);
                break;
            case "hall":
                imageView.setImageResource(R.drawable.hall);
                break;
            case "landmark":
                imageView.setImageResource(R.drawable.landmark);
                break;
            case "food":
                imageView.setImageResource(R.drawable.food);
                break;
            default:
                imageView.setImageResource(R.drawable.logo);
                break;
        }

        TextView itemString = (TextView) convertView.findViewById(R.id.building_name);
        itemString.setText(item.getBuildingName());
        itemString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToInfo((ListItem) getItem(position));
            }
        });
        return convertView;
    }

    public void goToInfo(ListItem info) {
        Intent intent = new Intent(context, SiteInfo.class);
        Bundle bundle = new Bundle();
        bundle.putString("building_name", info.getBuildingName());
        bundle.putStringArrayList("factoids", info.getFactoids());
        intent.putExtras(bundle);
        Log.i("ListItemAdapter", info.getBuildingName() + " list element clicked");
        context.startActivity(intent);
    }
}