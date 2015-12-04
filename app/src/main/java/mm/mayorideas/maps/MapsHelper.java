package mm.mayorideas.maps;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class MapsHelper implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private ClusterManager<ClusterMarkerItem> mClusterManager;
    private boolean animateCameraOnLocationChange;
    private boolean moveCameraOnInitialLocationChange;

    public MapsHelper(Context context, int mapId) {
        buildGoogleApiClient(context);
        setUpMapIfNeeded(context, mapId);
    }

    public MapsHelper(Context context, int mapId, @Nullable Fragment fragment) {
        buildGoogleApiClient(context);
        setUpMapIfNeeded(context, mapId, fragment);
    }

    protected synchronized void buildGoogleApiClient(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void setUpMapIfNeeded(Context context, int mapId) {
        setUpMapIfNeeded(context, mapId, null);
    }

    public void setUpMapIfNeeded(Context context, int mapId, @Nullable Fragment fragment) {
        if (context instanceof FragmentActivity) {
            if (mMap == null) {
                SupportMapFragment supportMapFragment;
                if (fragment == null) {
                    FragmentActivity fragmentActivity = (FragmentActivity) context;
                    supportMapFragment = (SupportMapFragment) fragmentActivity
                            .getSupportFragmentManager()
                            .findFragmentById(mapId);
                } else {
                    supportMapFragment = ((SupportMapFragment) fragment
                            .getChildFragmentManager()
                            .findFragmentById(mapId));
                }
                mMap = supportMapFragment.getMap();
                animateCameraOnLocationChange = true;
                moveCameraOnInitialLocationChange = true;
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    public void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, createLocationRequest(), this);
    }

    public boolean canStartLocationUpdates() {
        return mGoogleApiClient != null && mGoogleApiClient.isConnected();
    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private void getLocation() {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mMap != null && lastLocation != null) {
            LatLng location = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(location).title("Ahoj"));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (mMap != null && location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            if (animateCameraOnLocationChange) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Ahoj"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
            } else if (moveCameraOnInitialLocationChange) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                moveCameraOnInitialLocationChange = false;
            }

        }
    }

    public void setAnimateCameraOnLocationChange(boolean animateCameraOnLocationChange) {
        this.animateCameraOnLocationChange = animateCameraOnLocationChange;
    }

    public void setMoveCameraOnInitialLocationChange(boolean moveCameraOnInitialLocationChange) {
        this.moveCameraOnInitialLocationChange = moveCameraOnInitialLocationChange;
    }

    public void connectMap() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    public void setupClusterManager(Context context) {
        mClusterManager = new ClusterManager<>(context, mMap);
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setRenderer(new OwnIconRendered(context, mMap, mClusterManager));
    }

    public void addMarker(double lat, double lang, String title) {
        if (mClusterManager != null) {
            mClusterManager.addItem(new ClusterMarkerItem(lat, lang, title));
        }
    }

    private final class ClusterMarkerItem implements ClusterItem {
        private final LatLng position;
        private final String title;

        public ClusterMarkerItem(double lat, double lng, String title) {
            this.position = new LatLng(lat, lng);
            this.title = title;
        }

        @Override
        public LatLng getPosition() {
            return this.position;
        }

        public String getTitle() {
            return title;
        }
    }

    private final class OwnIconRendered extends DefaultClusterRenderer<ClusterMarkerItem> {

        public OwnIconRendered(Context context,
                               GoogleMap map,
                               ClusterManager<ClusterMarkerItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(ClusterMarkerItem item, MarkerOptions markerOptions) {
            //markerOptions.icon(item.getIcon());
            //markerOptions.snippet(item.getSnippet());
            markerOptions.title(item.getTitle());
            super.onBeforeClusterItemRendered(item, markerOptions);
        }
    }
}
