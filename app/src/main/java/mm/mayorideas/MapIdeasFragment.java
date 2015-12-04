package mm.mayorideas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mm.mayorideas.maps.MapsHelper;

public class MapIdeasFragment extends Fragment {

    private MapsHelper mapsHelper;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_map_ideas, container, false);
        mapsHelper = new MapsHelper(this.getContext(), R.id.ideas_map, this);
        mapsHelper.setupClusterManager(this.getContext());
        mapsHelper.setAnimateCameraOnLocationChange(false);

        addMarkers();

        return layout;
    }

    private void addMarkers() {
        double lat = 51.5247610;
        double lang = -0.0982650;
        for (int i = 0; i < 25; i++) {
            mapsHelper.addMarker(lat+(i*0.001), lang+(i*0.001), "a"+i);
        }
        for (int i = 0; i < 25; i++) {
            mapsHelper.addMarker(lat+(i*0.001), lang-(i*0.001), "b"+i);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapsHelper == null) {
            mapsHelper = new MapsHelper(this.getContext(), R.id.ideas_map, this);
        } else {
            mapsHelper.setUpMapIfNeeded(this.getContext(), R.id.ideas_map, this);
        }
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

}
