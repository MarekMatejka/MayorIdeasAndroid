package mm.mayorideas.maps;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsHelper implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    public MapsHelper(Context context, int mapId) {
        buildGoogleApiClient(context);
        setUpMapIfNeeded(context, mapId);
    }

    protected synchronized void buildGoogleApiClient(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void setUpMapIfNeeded(Context context, int mapId) {
        if (context instanceof FragmentActivity) {
            if (mMap == null) {
                FragmentActivity fragmentActivity = (FragmentActivity) context;
                SupportMapFragment supportMapFragment = (SupportMapFragment) fragmentActivity
                        .getSupportFragmentManager()
                        .findFragmentById(mapId);
                mMap = supportMapFragment.getMap();
//                if (mMap != null) {
//                    getLocation();
//                }
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        //getLocation();
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
            mMap.clear();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Ahoj"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
        }
    }

    public void connectMap() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }
}
