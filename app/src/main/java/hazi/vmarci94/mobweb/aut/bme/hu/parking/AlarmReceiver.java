package hazi.vmarci94.mobweb.aut.bme.hu.parking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import hazi.vmarci94.mobweb.aut.bme.hu.parking.data.Remind;

/**
 * Created by vmarci94 on 2017. 12. 07..
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TAG", "Lejárt az idő");
        Remind remind = Remind.getInstance(context);
        String number = remind.getParkingNumber();
        //TODO send "STOP" sms ...
        remind.deleteParkingNumber();
    }
}
