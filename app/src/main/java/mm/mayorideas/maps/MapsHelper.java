package mm.mayorideas.maps;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsHelper {

    private GoogleMap mMap;

    public MapsHelper(Context context, int mapId) {
        setUpMapIfNeeded(context, mapId);
    }

    public void setUpMapIfNeeded(Context context, int mapId) {
        if (context instanceof FragmentActivity) {
            if (mMap == null) {
                FragmentActivity fragmentActivity = (FragmentActivity) context;
                SupportMapFragment supportMapFragment = (SupportMapFragment) fragmentActivity
                        .getSupportFragmentManager()
                        .findFragmentById(mapId);
                mMap = supportMapFragment.getMap();
                if (mMap != null) {
                    setUpMap();
                }
            }
        }
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
}
