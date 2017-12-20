package hazi.vmarci94.mobweb.aut.bme.hu.parking;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

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
import java.util.List;

import hazi.vmarci94.mobweb.aut.bme.hu.parking.data.ParkingHistory;
import hazi.vmarci94.mobweb.aut.bme.hu.parking.data.ParkingHistoryDataManager;
import hazi.vmarci94.mobweb.aut.bme.hu.parking.data.Remind;
import hazi.vmarci94.mobweb.aut.bme.hu.parking.fragments.SigninFragment;
import hazi.vmarci94.mobweb.aut.bme.hu.parking.fragments.StartNewParkingDialog;

/**
 * Created by vmarci94 on 2017. 11. 16..
 */

public class MapsMainActivity extends AppCompatActivity
        implements OnMapReadyCallback, StartNewParkingDialog.INewParkingDialogListener{

    private final LatLng Zuglo = new LatLng(47.508322, 19.094957);

    private GoogleMap mMap;
    private KmlLayer kmlLayer;
    private ParkingHistoryDataManager parkingHistoryDataManager;
    AlarmManager alarmManager;
    AlarmManager.OnAlarmListener alarmListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        showMapFragment();
        loadParkingHistorysInBackground();
    }

    private void showMapFragment(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        FloatingActionButton fab = mapFragment.getView().findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_account_balance_wallet_black_48dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsMainActivity.this, ParkingHistoryActivity.class));
            }
        });
        mapFragment.getMapAsync(this);
        parkingHistoryDataManager = ParkingHistoryDataManager.getInstance();
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
                try {
                    for (KmlContainer container : kmlLayer.getContainers()) {
                        for (KmlPlacemark placemark : container.getPlacemarks()) {
                            final String name = placemark.getProperty("name");
                            String[] descriptions = placemark.getProperty("description").split("-");
                            final String phoneNumber = descriptions[0];
                            final String price = descriptions[1];
                            KmlPolygon polygon = (KmlPolygon) placemark.getGeometry();
                            View view = getSupportFragmentManager().findFragmentById(R.id.map).getView();
                            Snackbar snackbar;
                            if (PolyUtil.containsLocation(
                                    new LatLng(location.getLatitude(), location.getLongitude()),
                                    polygon.getGeometryObject().get(0), false)) {
                                snackbar = Snackbar
                                        .make(view, getString(R.string.pushMe), Snackbar.LENGTH_LONG)
                                        .setAction(R.string.send, new View.OnClickListener() {
                                            @TargetApi(Build.VERSION_CODES.N)
                                            @RequiresApi(api = Build.VERSION_CODES.M)
                                            @Override
                                            public void onClick(View view) {
                                                if(!Remind.getInstance(getApplicationContext()).getParkingStatus()){
                                                    showZonaInfoDialogFragment(name, phoneNumber, price);
                                                }else {
                                                    Snackbar snackbar= Snackbar
                                                            .make(view, getString(R.string.ParkingIsActive), Snackbar.LENGTH_LONG)
                                                            .setAction(R.string.shutDownParking, new View.OnClickListener() {
                                                                @RequiresApi(api = Build.VERSION_CODES.N)
                                                                @Override
                                                                public void onClick(View view) {
                                                                    //TODO unregister alarm
                                                                    if(alarmManager != null && alarmListener != null){
                                                                        alarmManager.cancel(alarmListener);
                                                                    }
                                                                    Remind.getInstance(getApplicationContext()).deleteParkingNumber();
                                                                }
                                                            });
                                                    snackbar.setActionTextColor(Color.RED);
                                                    View sbView = snackbar.getView();
                                                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                                    textView.setTextColor(Color.YELLOW);
                                                    snackbar.show();
                                                }
                                            }
                                        });
                                snackbar.setActionTextColor(Color.RED);
                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.YELLOW);
                                snackbar.show();
                                return;
                            }
                        }
                    }
                    Snackbar snackbar = Snackbar
                            .make(getSupportFragmentManager().findFragmentById(R.id.map).getView()
                                    , getString(R.string.pushMeButNoZone)
                                    , Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED);
                    ((TextView) (snackbar.getView().findViewById(android.support.design.R.id.snackbar_text))).setTextColor(Color.YELLOW);
                    snackbar.show();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void showZonaInfoDialogFragment(String name, final String phoneNumb, String price){
        //TODO show StartNewParkingDialog
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("price", price);
        bundle.putString("phoneNumb", phoneNumb);

        StartNewParkingDialog startNewParkingDialog = new StartNewParkingDialog();
        startNewParkingDialog.setArguments(bundle);
        startNewParkingDialog.show(getSupportFragmentManager(), StartNewParkingDialog.TAG);
    }

    private void setOnFeatureClickListener(){
        kmlLayer.setOnFeatureClickListener(new KmlLayer.OnFeatureClickListener() {
            @Override
            public void onFeatureClick(Feature feature) {
                if(!Remind.getInstance(getApplicationContext()).getParkingStatus()){
                    try {
                        String[] args = feature.getProperty("description").split("-");
                        showZonaInfoDialogFragment(feature.getProperty("name"), args[0], args[1]);
                    }catch (NullPointerException e){
                        e.printStackTrace();
                        Log.e("ERROR", getString(R.string.onFeratureClickParamsIsNull));
                    }
                }else {
                    View view = getSupportFragmentManager().findFragmentById(R.id.map).getView();
                    Snackbar snackbar= Snackbar
                                .make(view, getString(R.string.ParkingIsActive), Snackbar.LENGTH_LONG)
                                .setAction(R.string.shutDownParking, new View.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void onClick(View view) {
                                        //TODO unregister alarm
                                        if(alarmManager != null && alarmListener != null){
                                            alarmManager.cancel(alarmListener);
                                        }
                                        Remind.getInstance(getApplicationContext()).deleteParkingNumber();
                                    }
                                });
                        snackbar.setActionTextColor(Color.RED);
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        snackbar.show();
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


    @SuppressLint("StaticFieldLeak")
    private void loadParkingHistorysInBackground() {
        new AsyncTask<Void, Void, List<ParkingHistory>>() {

            @Override
            protected List<ParkingHistory> doInBackground(Void... voids) {
                return ParkingHistory.listAll(ParkingHistory.class);
            }

            @Override
            protected void onPostExecute(List<ParkingHistory> parkingHistories) {
                super.onPostExecute(parkingHistories);
                parkingHistoryDataManager.update(parkingHistories);
            }
        }.execute();
    }

    @Override
    public void onNewParkingCreated(ParkingHistory newParkingHistoryItem) {
        parkingHistoryDataManager.addItem(newParkingHistoryItem);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDetach(long time) {

        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmListener = new AlarmManager.OnAlarmListener() {
            @Override
            public void onAlarm() {
                Remind remind = Remind.getInstance(getApplicationContext());
                String number = remind.getParkingNumber();
                sendStopSms(number);
                remind.deleteParkingNumber();
            }
        };

        if(alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, time + System.currentTimeMillis(),
                    "stopSms", alarmListener, null);
        }else{
            Log.e("HIBA", "alarmManager is null");
        }
    }

    public void sendStopSms(String number){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, getString(R.string.stop), null, null);
    }
}
