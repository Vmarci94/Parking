package hazi.vmarci94.mobweb.aut.bme.hu.parking.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by vmarci94 on 2017. 12. 07..
 */

public class Remind {

    private static final String PARKING_STATUS = "ParkingStatus";
    private static final String PARKING_PHONE_NUMBER = "StartedParkingPhoneNumber";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPrefEditor;
    private static Remind instance;

    public static Remind getInstance(Context context){
        if(instance == null){
            instance = new Remind(context);
        }
        return instance;
    }

    @SuppressLint("CommitPrefEdits")
    private Remind(Context context){
        this.sharedPreferences = context.getSharedPreferences(PARKING_STATUS, Context.MODE_PRIVATE);
        this.sharedPrefEditor = sharedPreferences.edit();
    }

    public final boolean getParkingStatus(){
        return sharedPreferences.getBoolean(PARKING_STATUS, false);
    }

    private void setParkingStatus(boolean flag){
        sharedPrefEditor.putBoolean(PARKING_STATUS, flag);
        sharedPrefEditor.commit();
    }

    public String getParkingNumber(){
        if(getParkingStatus()){
            return sharedPreferences.getString(PARKING_PHONE_NUMBER, null);
        }
        return null;
    }

    public void setParkingNumber(String number){
        sharedPrefEditor.putString(PARKING_PHONE_NUMBER, number);
        setParkingStatus(true);
        sharedPrefEditor.commit();
    }

    public void deleteParkingNumber(){
        sharedPrefEditor.putString(PARKING_PHONE_NUMBER, null);
        setParkingStatus(false);
        sharedPrefEditor.commit();
    }

}
