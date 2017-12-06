package hazi.vmarci94.mobweb.aut.bme.hu.parking;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.data.Feature;
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

public class MapsMainActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    private final LatLng Budapest = new LatLng(47.49801, 19.03991);
    private final LatLng Zuglo = new LatLng(47.508322, 19.094957);

    private GoogleMap mMap;
    private KmlLayer kmlLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_main);
        //ablak keret nélküli inicializálása.
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Szolgáltatáshoz engedélykérés, ha szükséges és mapfragment felcsatolása
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
            showMapFragment();
        }
    }

    private void showMapFragment(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        FloatingActionButton fab = mapFragment.getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsMainActivity.this, ParkingHistoryActivity.class));
            }
        });

        mapFragment.getMapAsync(this);
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
    public void onMapReady(final GoogleMap googleMap) {
        try {
            mMap = googleMap;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Zuglo, 13));
            retrieveFileFromResource();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            setOnFeatureClickListener();

            setOnMyLocationClickListener();

        } catch (Exception e) {
            Log.e("Exception caught", e.toString());
        }
    }

    private void setOnMyLocationClickListener() {
        mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                for(KmlContainer container: kmlLayer.getContainers()) {
                    for (KmlPlacemark placemark : container.getPlacemarks()) {
                        String name = placemark.getProperty("name");
                        String[] descriptions = placemark.getProperty("description").split("-");
                        String phoneNumber = descriptions[0];
                        String price = descriptions[1];
                        KmlPolygon polygon = (KmlPolygon) placemark.getGeometry();
                        if( PolyUtil.containsLocation(
                                new LatLng(location.getLatitude(), location.getLongitude()),
                                polygon.getGeometryObject().get(0), false) ){

                            try{
                                View view = getSupportFragmentManager().findFragmentById(R.id.map).getView();
                                Snackbar snackbar = Snackbar
                                        .make(view, getString(R.string.pushMe), Snackbar.LENGTH_LONG)
                                        .setAction(R.string.send, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                //FIXME send sms
                                            }
                                        });
                                snackbar.setActionTextColor(Color.RED);
                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.YELLOW);
                                snackbar.show();

                            }catch (NullPointerException e ){
                                e.printStackTrace();
                                Toast.makeText(MapsMainActivity.this, R.string.error, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }
        });
    }

    public void showZonaInfoDialogFragment(String name, String price){
        View contextView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_zona_info, null);
        EditText rendszamET = (EditText) contextView.findViewById(R.id.rendszamET);
        TimePicker timePicker = (TimePicker) contextView.findViewById(R.id.timePicker1);

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle(getString(R.string.prakingIn) + name + getString(R.string.inZona));
        builder.setView(contextView);
        builder.setMessage(getString(R.string.price) + price);
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //TODO send sms
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        final AlertDialog dialog = builder.create();
        dialog.show();
        ((AlertDialog) dialog).getButton(android.app.AlertDialog.BUTTON_POSITIVE).setEnabled(false); //hint muszáj ez előtt show-t hívni

        rendszamET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(editable) && editable.toString().length() == 6){
                    ((AlertDialog)dialog).getButton(android.app.AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }else{
                    ((AlertDialog)dialog).getButton(android.app.AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }
        });

    }

    private void setOnFeatureClickListener(){
        kmlLayer.setOnFeatureClickListener(new KmlLayer.OnFeatureClickListener() {
            @Override
            public void onFeatureClick(Feature feature) {
                try {
                    Toast.makeText(MapsMainActivity.this,
                            "Feature clicked: " + feature.getProperty("name"),
                            Toast.LENGTH_SHORT).show();
                    //Todo start paking, show dialog fragment
                }catch (NullPointerException e){
                    e.printStackTrace();
                    Log.e("ERROR", "onFeatureClick parameter is null");
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
            kmlLayer = new KmlLayer(mMap, R.raw.draw, getApplicationContext
                    ());
            kmlLayer.addLayerToMap();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

}
