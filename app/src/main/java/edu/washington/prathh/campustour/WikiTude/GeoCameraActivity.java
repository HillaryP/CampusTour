package edu.washington.prathh.campustour.WikiTude;

import android.hardware.SensorManager;
import android.location.LocationListener;
import android.widget.Toast;

import com.wikitude.architect.ArchitectView.ArchitectUrlListener;
import com.wikitude.architect.ArchitectView.SensorAccuracyChangeListener;

import edu.washington.prathh.campustour.R;

public class GeoCameraActivity extends AbstractArchitectCamActivity {

    /**
     * last time the calibration toast was shown, this avoids too many toast shown when compass needs calibration
     */
    private long lastCalibrationToastShownTimeMillis = System.currentTimeMillis();

    @Override
    public String getARchitectWorldPath() {
        return "assets/index.html";
    }

    @Override
    public String getActivityTitle() {
        return "GeoCamera";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_geo_camera;
    }

    @Override
    public int getArchitectViewId() {
        return R.id.architectView;
    }

    @Override
    public String getWikitudeSDKLicenseKey() {
        return "XcZk4IBkd5sD6CYfdtXjmum1pQPDB9JZ4w47uEDEPM4Lm+D2Ljn96qndDAATnKRsAqQr9De0/6v0Nm385cAjCGMvk71cg0of4FO9OamGtjwnCiYG0Q2sPT0cYq/y2rXmgETeHcXmsHb63k05QRvS3Xt62QsaknmjwANLC/oQshNTYWx0ZWRfX+9PmWk5kTsqStwQ3lKgmaVMo1phjuzjaAItHfmHdDaNVr6IdEqrqLsRVxgpuUlkPX0bohm0fh89mpB+5tYZ+ycykAf4S9k7wL7Io5r7lX5o8glXj5vn64KT3W+I9f2NpvjRvjQ5MYBqJkiN00oZMBCfA0ExVUDbOqiCwjeWkoYGKH2UftYF/CJgKzFoOUpBdOuR3WLaYvATUuyowxtSKs/BoDDh9p7TxHvNgNILEayhzGtgC6Ux9uKZqocStgQSn82mhHTR6fgSpSbSfoAj3gbweCn9Vn70lzXsuo7zeZOTXxgALTAoKiXimG4iVJiXJwm0N7O5pMPwI/FMfEnot+wIBdcXJ3Tt7eV3d4mpar2exfxLG5AxZlVDWt7hiio4cY5kVS+ZcU/psClbgQgHEJPbC7TYmXvFVos3J9RzZ8xhlRYxutQDZtXziqfiILTsoxRhHJUavuj8sY1uKxsqu7XKMne8dfD/2g==";
    }

    @Override
    public SensorAccuracyChangeListener getSensorAccuracyListener() {
        return new SensorAccuracyChangeListener() {
            @Override
            public void onCompassAccuracyChanged( int accuracy ) {
				/* UNRELIABLE = 0, LOW = 1, MEDIUM = 2, HIGH = 3 */
                if ( accuracy < SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM && GeoCameraActivity.this != null && !GeoCameraActivity.this.isFinishing() && System.currentTimeMillis() - GeoCameraActivity.this.lastCalibrationToastShownTimeMillis > 5 * 1000) {
                    Toast.makeText( GeoCameraActivity.this, "Please re-calibrate compass by waving your device in a figure 8 motion.", Toast.LENGTH_LONG ).show();
                    GeoCameraActivity.this.lastCalibrationToastShownTimeMillis = System.currentTimeMillis();
                }
            }
        };
    }

    @Override
    public ArchitectUrlListener getUrlListener() {
        return new ArchitectUrlListener() {

            @Override
            public boolean urlWasInvoked(String uriString) {
                // by default: no action applied when url was invoked
                return false;
            }
        };
    }

    @Override
    public ILocationProvider getLocationProvider(final LocationListener locationListener) {
        return new LocationProvider(this, locationListener);
    }

    @Override
    public float getInitialCullingDistanceMeters() {
        // you need to adjust this in case your POIs are more than 50km away from user here while loading or in JS code (compare 'AR.context.scene.cullingDistance')
        return CULLING_DISTANCE_DEFAULT_METERS;
    }

    @Override
    protected boolean hasGeo() {
        return true;
    }

    @Override
    protected boolean hasIR() {
        return false;
    }
}
