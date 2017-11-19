package hazi.vmarci94.mobweb.aut.bme.hu.parking;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.MultiGeometry;
import com.google.maps.android.data.kml.KmlContainer;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.maps.android.data.kml.KmlPlacemark;
import com.google.maps.android.data.kml.KmlPolygon;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import hazi.vmarci94.mobweb.aut.bme.hu.parking.fragments.SigninFragment;

/**
 * Created by vmarci94 on 2017. 11. 16..
 */

public class MapsMainActivity extends FragmentActivity
        implements OnMapReadyCallback {

    private GoogleMap mMap;
    private KmlLayer kmlLayer;
    private ArrayList<KmlPolygon> kmlPolygons = new ArrayList<>();
    private final LatLng Budapest = new LatLng(47.49801, 19.03991);
    private final LatLng Zuglo = new LatLng(47.508322, 19.094957);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_main);

        //Szolgáltatáshoz engedélykérés


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
            //setOnClickListenerAllPlacemark();
            listAllPlacemarksStyleID();

            mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
                @Override
                public void onPolygonClick(Polygon polygon) {
                    Log.i("MTAG_Polygon_id", polygon.getId());
                }
            });

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } catch (Exception e) {
            Log.e("Exception caught", e.toString());
        }
    }

    private void listAllPlacemarksStyleID(){
        for(KmlContainer container : kmlLayer.getContainers()){
            for(KmlPlacemark placemark : container.getPlacemarks()){
                //Log.i("MTAG", placemark.getProperty("name") +   " is a " + placemark.getGeometry().getGeometryType());
                if(placemark.getGeometry().getGeometryType().equals(MultiGeometry.class.getSimpleName())){
                    //if placmark is a MultiGeometry
                    MultiGeometry multiGeometry = (MultiGeometry) placemark.getGeometry();
                    for(int i = 0; i<multiGeometry.getGeometryObject().size(); i++){
                        kmlPolygons.add( (KmlPolygon) multiGeometry.getGeometryObject().get(i));
                    }

                    //...
                } else if(placemark.getGeometry().getGeometryType().equals(Polygon.class.getSimpleName())){ // I use just MultiGeometry and KmlPolygon type, just in case ... :)
                    KmlPolygon kmlPolygon = (KmlPolygon) placemark.getGeometry();
                    kmlPolygons.add(kmlPolygon);
                }

                PolygonOptions polygonOptions = new PolygonOptions();
                for(KmlPolygon kmlPolygon : kmlPolygons){
                    Log.i("MTAG_test", Integer.valueOf(kmlPolygon.getGeometryObject().size() ).toString());
                    polygonOptions.addAll(kmlPolygon.getGeometryObject().get(0)); //btw just one elem ... why list<list>?!
                    Polygon polygon = mMap.addPolygon(polygonOptions.strokeColor(Color.RED));
                    polygon.setClickable(true);
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
            kmlLayer = new KmlLayer(mMap, R.raw.zona5, getApplicationContext
                    ());
            kmlLayer.addLayerToMap();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

}
