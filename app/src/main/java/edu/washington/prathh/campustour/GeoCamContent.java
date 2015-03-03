package edu.washington.prathh.campustour;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

public class GeoCamContent extends GeoCameraActivity {

    protected JSONArray poiData;
    protected boolean isLoading = false;


    /** Called when the activity is first created. */
    @Override
    public void onCreate( final Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );


        this.locationListener = new LocationListener() {

            @Override
            public void onStatusChanged( String provider, int status, Bundle extras ) {
            }

            @Override
            public void onProviderEnabled( String provider ) {
            }

            @Override
            public void onProviderDisabled( String provider ) {
            }

            @Override
            public void onLocationChanged( final Location location ) {
                if (location!=null) {
                    GeoCamContent.this.lastKnownLocaton = location;
                    if ( GeoCamContent.this.architectView != null ) {
                        if ( location.hasAltitude() ) {
                            GeoCamContent.this.architectView.setLocation( location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getAccuracy() );
                        } else {
                            GeoCamContent.this.architectView.setLocation( location.getLatitude(), location.getLongitude(), location.getAccuracy() );
                        }
                    }
                }
            }
        };

        this.architectView.registerSensorAccuracyChangeListener( this.sensorAccuracyListener );
        this.locationProvider = new LocationProvider( this, this.locationListener );
    }


    @Override
    protected void onPostCreate( final Bundle savedInstanceState ) {
        super.onPostCreate( savedInstanceState );
        this.loadData();

    }

    final Runnable loadData = new Runnable() {



        @Override
        public void run() {

            GeoCamContent.this.isLoading = true;

            final int WAIT_FOR_LOCATION_STEP_MS = 2000;

            while (GeoCamContent.this.lastKnownLocaton==null && !GeoCamContent.this.isFinishing()) {

                GeoCamContent.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(GeoCamContent.this, "Fetching location", Toast.LENGTH_SHORT).show();
                    }
                });

                try {
                    Thread.sleep(WAIT_FOR_LOCATION_STEP_MS);
                } catch (InterruptedException e) {
                    break;
                }
            }

            if (GeoCamContent.this.lastKnownLocaton!=null && !GeoCamContent.this.isFinishing()) {
                // TODO: you may replace this dummy implementation and instead load POI information e.g. from your database
                GeoCamContent.this.poiData = GeoCamContent.getPoiInformation(GeoCamContent.this.lastKnownLocaton, 20);
                GeoCamContent.this.callJavaScript("World.loadPoisFromJsonData", new String[] { GeoCamContent.this.poiData.toString() });
            }

            GeoCamContent.this.isLoading = false;
        }
    };


    protected void loadData() {
        if (!isLoading) {
            final Thread t = new Thread(loadData);
            t.start();
        }
    }

    /**
     * call JacaScript in architectView
     * @param methodName
     * @param arguments
     */
    private void callJavaScript(final String methodName, final String[] arguments) {
        final StringBuilder argumentsString = new StringBuilder("");
        for (int i= 0; i<arguments.length; i++) {
            argumentsString.append(arguments[i]);
            if (i<arguments.length-1) {
                argumentsString.append(", ");
            }
        }

        if (this.architectView!=null) {
            final String js = ( methodName + "( " + argumentsString.toString() + " );" );
            this.architectView.callJavascript(js);
        }
    }


    /**
     * loads poiInformation and returns them as JSONArray. Ensure attributeNames of JSON POIs are well known in JavaScript, so you can parse them easily
     * @param userLocation the location of the user
     * @param numberOfPlaces number of places to load (at max)
     * @return POI information in JSONArray
     */
    public static JSONArray getPoiInformation(final Location userLocation, final int numberOfPlaces) {

        if (userLocation==null) {
            return null;
        }

        final JSONArray pois = new JSONArray();

        // ensure these attributes are also used in JavaScript when extracting POI data
        final String ATTR_ID = "id";
        final String ATTR_NAME = "name";
        final String ATTR_DESCRIPTION = "description";
        final String ATTR_LATITUDE = "latitude";
        final String ATTR_LONGITUDE = "longitude";
        final String ATTR_ALTITUDE = "altitude";

        for (int i=1;i <= numberOfPlaces; i++) {
            final HashMap<String, String> poiInformation = new HashMap<String, String>();
            poiInformation.put(ATTR_ID, String.valueOf(i));
            poiInformation.put(ATTR_NAME, "POI#" + i);
            poiInformation.put(ATTR_DESCRIPTION, "This is the description of POI#" + i);
            double[] poiLocationLatLon = getRandomLatLonNearby(userLocation.getLatitude(), userLocation.getLongitude());
            poiInformation.put(ATTR_LATITUDE, String.valueOf(poiLocationLatLon[0]));
            poiInformation.put(ATTR_LONGITUDE, String.valueOf(poiLocationLatLon[1]));
            final float UNKNOWN_ALTITUDE = -32768f;  // equals "AR.CONST.UNKNOWN_ALTITUDE" in JavaScript (compare AR.GeoLocation specification)
            // Use "AR.CONST.UNKNOWN_ALTITUDE" to tell ARchitect that altitude of places should be on user level. Be aware to handle altitude properly in locationManager in case you use valid POI altitude value (e.g. pass altitude only if GPS accuracy is <7m).
            poiInformation.put(ATTR_ALTITUDE, String.valueOf(UNKNOWN_ALTITUDE));
            pois.put(new JSONObject(poiInformation));
        }

        return pois;
    }

    /**
     * helper for creation of dummy places.
     * @param lat center latitude
     * @param lon center longitude
     * @return lat/lon values in given position's vicinity
     */
    private static double[] getRandomLatLonNearby(final double lat, final double lon) {
        return new double[] { lat + Math.random()/5-0.1 , lon + Math.random()/5-0.1};
    }
}