package hazi.vmarci94.mobweb.aut.bme.hu.parking;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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

import hazi.vmarci94.mobweb.aut.bme.hu.parking.data.Zona;
import hazi.vmarci94.mobweb.aut.bme.hu.parking.fragments.SigninFragment;

/**
 * Created by vmarci94 on 2017. 11. 16..
 */

public class MapsMainActivity extends FragmentActivity
        implements OnMapReadyCallback{

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    private final LatLng Budapest = new LatLng(47.49801, 19.03991);
    private final LatLng Zuglo = new LatLng(47.508322, 19.094957);

    private GoogleMap mMap;
    private KmlLayer kmlLayer;
    private ArrayList<KmlPolygon> kmlPolygons = new ArrayList<>();
    private ArrayList<Zona> zonak = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_main);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Szolgáltatáshoz engedélykérés
        handlerPermission();

    }

    private void handlerPermission(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(R.string.dialogTitle);
                alertDialogBuilder
                        .setMessage(R.string.explanation)
                        .setCancelable(false)
                        .setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MapsMainActivity.this.finish();
                            }
                        })
                        .setPositiveButton(R.string.forward, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                ActivityCompat.requestPermissions(MapsMainActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(this);

                } else {
                    // permission denied! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @SuppressLint("MissingPermission") //handler call in onCreate
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Zuglo, 13));
            retrieveFileFromResource();
            createSimplePolygonsFromKmlLayout();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
                @Override
                public void onPolygonClick(Polygon polygon) {
                    Toast.makeText(MapsMainActivity.this, "click id=" + polygon.getId() + " polygon", Toast.LENGTH_LONG).show();
                }
            });

            mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {
                    Toast.makeText(MapsMainActivity.this, "megnyomtál", Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            Log.e("Exception caught", e.toString());
        }
    }


    private void createSimplePolygonsFromKmlLayout(){
        for(KmlContainer container : kmlLayer.getContainers()){
            for(KmlPlacemark placemark : container.getPlacemarks()){
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
                for(KmlPolygon kmlPolygon : kmlPolygons){
                    PolygonOptions polygonOptions = new PolygonOptions();
                    polygonOptions.addAll(kmlPolygon.getGeometryObject().get(0)); //btw just one elem ... why list<list>?!
                    Polygon polygon = mMap.addPolygon(polygonOptions.strokeColor(Color.RED));
                    polygon.setClickable(true);
                }
            }
        }
        Log.i("MTAG_ALLsize", Integer.valueOf(kmlPolygons.size()).toString());

    }

    private void setOnFeatureClickListener(){
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


}
