package com.example.myfirstapp;

import android.location.Location;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener{
    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mView;
    private Location lastLocation;
    private Marker currentUserLocationMarker;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView= inflater.inflate(R.layout.fragment_map, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView=(MapView)mView.findViewById(R.id.map);
        if(mMapView!=null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    mGoogleMap=googleMap;
    mGoogleMap.setMyLocationEnabled(true);

    }



    @Override
    public void onLocationChanged(Location location) {
        lastLocation=location;
        if(currentUserLocationMarker !=null){
            currentUserLocationMarker.remove();
        }
        MapsInitializer.initialize(getContext());
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("User Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        currentUserLocationMarker=mGoogleMap.addMarker(markerOptions);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomBy(16));

    }

}
