package ie.tcd.mcardleg.cyclqe.frontend.ui;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import ie.tcd.mcardleg.cyclqe.frontend.Constants;
import ie.tcd.mcardleg.cyclqe.frontend.Globals;
import ie.tcd.mcardleg.cyclqe.frontend.R;
import ie.tcd.mcardleg.cyclqe.frontend.databinding.ActivityMapsBinding;
import ie.tcd.mcardleg.cyclqe.frontend.gather.BikeLocation;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        List<BikeLocation> bikeLocations = Globals.getInstance().bikeLocations;
        for(BikeLocation bike : bikeLocations) {
            LatLng location = new LatLng(bike.getLatitude(), bike.getLongitude());
            map.addMarker(new MarkerOptions()
                    .position(location)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bleeper)));
        }

        List<Document> sensorData = Globals.getInstance().sensorData;
        for(Document doc : sensorData) {
            LatLng location = new LatLng(
                    doc.get(Constants.LATITUDE).asDouble(), doc.get(Constants.LONGITUDE).asDouble());
            Float accel = doc.get(Constants.ACCELERATION).asFloat();
            if(accel < Constants.ORANGE_MARKER_THRESHOLD) {
                map.addMarker(new MarkerOptions()
                        .position(location)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            }
            else if(accel < Constants.RED_MARKER_THRESHOLD) {
                map.addMarker(new MarkerOptions()
                        .position(location)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            }
            else {
                map.addMarker(new MarkerOptions()
                        .position(location)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        }

        LatLngBounds bounds = new LatLngBounds(
                new LatLng(Constants.DUBLIN_SOUTHEAST_LAT, Constants.DUBLIN_NORTHWEST_LON),
                new LatLng(Constants.DUBLIN_NORTHWEST_LAT, Constants.DUBLIN_SOUTHEAST_LON)
        );
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), Constants.MAP_ZOOM));
    }
}