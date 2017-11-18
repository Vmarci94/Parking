package hazi.vmarci94.mobweb.aut.bme.hu.parking;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.MultiGeometry;
import com.google.maps.android.data.kml.KmlContainer;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.maps.android.data.kml.KmlPlacemark;
import com.google.maps.android.data.kml.KmlPolygon;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import hazi.vmarci94.mobweb.aut.bme.hu.parking.fragments.SigninFragment;

/**
 * Created by vmarci94 on 2017. 11. 16..
 */

public class MapsMainActivity extends FragmentActivity
        implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener {

    private GoogleMap mMap;
    private KmlLayer kmlLayer;
    private final LatLng Budapest = new LatLng(47.49801, 19.03991);
    private final LatLng Zuglo = new LatLng(47.508322, 19.094957);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Zuglo, 13));
            retrieveFileFromResource();
            setOnClickListenerAllPlacemark();
            listAllPlacemarksStyleID();
        } catch (Exception e) {
            Log.e("Exception caught", e.toString());
        }
    }

    private void listAllPlacemarksStyleID(){
        for(KmlContainer container : kmlLayer.getContainers()){
            for(KmlPlacemark placemark : container.getPlacemarks()){
                Log.i("MTAG", placemark.getProperty("name") +   " is a " + placemark.getGeometry().getGeometryType());

                if(placemark.getGeometry().getGeometryType().equals(MultiGeometry.class.getSimpleName())){
                    //if placmark is a MultiGeometry
                    MultiGeometry multiGeometry = (MultiGeometry) placemark.getGeometry();
                    //...
                } else if(placemark.getGeometry().getGeometryType().equals(KmlPolygon.class.getSimpleName())){ // I use just MultiGeometry and KmlPolygon type, just in case ... :)
                    KmlPolygon kmlPolygon = (KmlPolygon) placemark.getGeometry();
                    // ...
                }
            }
        }
    }

    private void setOnClickListenerAllPlacemark(){
        kmlLayer.setOnFeatureClickListener(new KmlLayer.OnFeatureClickListener() {
            @Override
            public void onFeatureClick(Feature feature) {
                if(feature != null) {
                    Toast.makeText(MapsMainActivity.this,
                            "Feature clicked: " + feature.getProperty("name"),
                            Toast.LENGTH_SHORT).show();
                }else{
                    Log.e("MTAG", "feature is null :("); //FIXME
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(SigninFragment.SIGNOUT_RESAULT);
        finish();
        super.onBackPressed();
    }

    private void retrieveFileFromResource() {
        try {
            kmlLayer = new KmlLayer(mMap, R.raw.zona, getApplicationContext
                    ());
            kmlLayer.addLayerToMap();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}
