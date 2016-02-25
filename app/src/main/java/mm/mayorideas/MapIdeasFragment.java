package mm.mayorideas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import mm.mayorideas.api.IdeaAPI;
import mm.mayorideas.maps.MapsHelper;
import mm.mayorideas.objects.Idea;

public class MapIdeasFragment extends Fragment implements MapsHelper.LocationListener, IdeaAPI.GetIdeasListener {

    private MapsHelper mapsHelper;
    private LatLng lastLocation = null;

    public MapIdeasFragment() {
        // Required empty public constructor
    }

    public static MapIdeasFragment newInstance() {
        MapIdeasFragment fragment = new MapIdeasFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_map_ideas, container, false);
        mapsHelper = new MapsHelper(this.getContext(), R.id.ideas_map, this);
        mapsHelper.setupClusterManager(this.getContext());
        mapsHelper.setAnimateCameraOnLocationChange(false);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapsHelper == null) {
            mapsHelper = new MapsHelper(this.getContext(), R.id.ideas_map, this);
        } else {
            mapsHelper.setUpMapIfNeeded(this.getContext(), R.id.ideas_map, this);
        }
        mapsHelper.setLocationListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapsHelper.connectMap();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapsHelper.stopLocationUpdates();
    }

    @Override
    public void locationUpdate(LatLng newPosition) {
        if (hasMovedSignificantly(newPosition)) {
            IdeaAPI.get10ClosestIdeas(newPosition.latitude, newPosition.longitude, this);
            lastLocation = newPosition;
        }
    }

    private boolean hasMovedSignificantly(LatLng newPosition) {
        return lastLocation == null || distanceBetween2LocationsInKM(lastLocation, newPosition) >= 0.5;
    }

    private double distanceBetween2LocationsInKM(LatLng l, LatLng n) {
        double inRads = (Math.PI / 180D);
        double lng = (n.longitude - l.longitude) * inRads;
        double lat = (n.latitude - l.latitude) * inRads;
        double a = Math.pow(Math.sin(lat / 2D), 2D) + Math.cos(l.latitude * inRads) * Math.cos(n.latitude * inRads)
                   * Math.pow(Math.sin(lng / 2D), 2D);
        double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));

        return 6372 * c;
    }

    @Override
    public void onSuccess(List<Idea> ideas) {
        mapsHelper.clearAllMarkers();
        addAllMarkers(ideas);
    }

    private void addAllMarkers(List<Idea> ideas) {
        for (Idea idea : ideas) {
            mapsHelper.addMarker(idea);
        }
    }

    @Override
    public void onFailure() {

    }
}
