package edu.washington.prathh.campustour;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private double latitude;
    private double longitude;
    private List<ListItem> buildingList;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Log.i("MapsActivity", "Lat: " + this.latitude + " Long: " + this.longitude);
        Log.i("MapsActivity", "latitude: " + this.latitude + " longitude: " + this.longitude);
        setUpMapIfNeeded();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap().setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            public void onInfoWindowClick(Marker marker) {
                ArrayList<String> factoids = findFactoids(marker.getTitle());
                if (factoids != null) {
                    Intent intent = new Intent(MapsActivity.this, SiteInfo.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("building_name", marker.getTitle());
                    bundle.putStringArrayList("factoids", factoids);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    public ArrayList<String> findFactoids(String title) {
        for (ListItem i : buildingList) {
            if (i.getBuildingName().equals(title)) {
                return i.getFactoids();
            }
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    public void populateList() {
        ListView listView = (ListView) findViewById(R.id.listView);
        getNearbySites();
        listView.setAdapter(new ListItemAdapter(this, R.layout.list_item, buildingList));
    }

    @TargetApi(19)
    public void getNearbySites() {
        this.buildingList = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(loadJSONFromAsset());
            JSONArray buildings = json.getJSONArray("buildings");
            for (int i = 0; i < buildings.length(); i++) {
                JSONObject building = buildings.getJSONObject(i);
                double lat = building.getDouble("latitude");
                double lon = building.getDouble("longitude");
                double diff = Math.abs(lat - this.latitude) / Math.abs(lon - this.longitude);
                Log.i("MapsActivity", building.getString("building_name") + " diff from current location: " + diff);
                if (diff < 25) {
                    JSONArray factJSON = building.getJSONArray("facts");
                    ArrayList<String> factoids = new ArrayList<>();
                    for (int j = 0; j < factJSON.length(); j++) {
                        factoids.add(factJSON.getJSONObject(j).getString("fact"));
                    }
                    Log.i("MapsActivity", building.getString("building_name") + " " + factoids.toString());
                    buildingList.add(new ListItem(building.getString("building_name"),
                            building.getString("category"),
                            lat, lon,
                            factoids));
                }
            }
        } catch (Exception e) {
            Log.e("MapsActivity", e.toString() + " " + e.getLocalizedMessage());
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("info.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(this.latitude, this.longitude))
                .title("Your current location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        if (buildingList != null) {
            for (ListItem point : buildingList) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(point.getLat(), point.getLon()))
                        .title(point.getBuildingName()));
            }
        }
        LatLng coords = new LatLng(this.latitude, this.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 16.0f));
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            handleNewLocation(location);
        }
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        populateList();
        setUpMap();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }
}
