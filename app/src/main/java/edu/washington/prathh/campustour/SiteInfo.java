package edu.washington.prathh.campustour;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SiteInfo extends Activity {
    private Map<String, Integer> getImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_info);
        getImg = setUpMap();

        ImageView close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SiteInfo.this, Content.class);
                startActivity(intent);
            }
        });

        ImageView imageView = (ImageView) findViewById(R.id.image);
        Integer resource = getImg.get(this.getIntent().getExtras().getString("building_name"));
        if (resource != null) {
            imageView.setImageResource(resource);
        }

        TextView textView = (TextView) findViewById(R.id.info_title);
        textView.setText(this.getIntent().getExtras().getString("building_name"));

        String facts = "";
        ArrayList<String> factoids = this.getIntent().getExtras().getStringArrayList("factoids");
        for (String singleFact : factoids) {
            facts += singleFact + "\n\n";
        }
        TextView content = (TextView) findViewById(R.id.facts);
        content.setText(facts);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_site_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Map<String, Integer> setUpMap() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Mary Gates Hall (MGH)", R.drawable.mgh);
        mapping.put("Drumheller Fountain", R.drawable.drumheller);
        mapping.put("Husky Union Building (HUB)", R.drawable.hub);
        mapping.put("Burke Museum", R.drawable.burke);
        mapping.put("Paccar Hall", R.drawable.paccar);
        mapping.put("The Liberal Arts Quadrangle (The Quad)", R.drawable.quad);
        mapping.put("Suzzallo Library", R.drawable.suzzallo);
        mapping.put("Intramural Activities Building (IMA)", R.drawable.ima);
        mapping.put("Sylvan Grove", R.drawable.sylvan);
        mapping.put("Husky Stadium", R.drawable.stadium);
        return mapping;
    }
}
