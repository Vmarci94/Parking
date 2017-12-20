package hazi.vmarci94.mobweb.aut.bme.hu.parking.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.IllegalFormatException;

import hazi.vmarci94.mobweb.aut.bme.hu.parking.R;
import hazi.vmarci94.mobweb.aut.bme.hu.parking.data.ParkingHistory;
import hazi.vmarci94.mobweb.aut.bme.hu.parking.data.Remind;

/**
 * Created by vmarci94 on 2017. 12. 15..
 */

public class StartNewParkingDialog extends AppCompatDialogFragment{

    public static final String TAG = "StartNewParkingDialog";
    private EditText rendszamET;
    private TimePicker timePicker;
    private AlertDialog.Builder builder;

    public interface INewParkingDialogListener{
        void onNewParkingCreated(ParkingHistory newParkingHistoryItem);
        void onDetach(long time);
    }

    INewParkingDialogListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contextView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog_fragmen_start_parking, null);
        rendszamET = (EditText) contextView.findViewById(R.id.rendszamET);
        timePicker = (TimePicker) contextView.findViewById(R.id.timePicker1);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                Date currentDate = Calendar.getInstance().getTime();
                int currentHour = currentDate.getHours();
                int currentMin = currentDate.getMinutes();

                if(currentHour > i ||
                        (currentHour == i &&  currentMin  > i1) ){
                    timePicker.setHour(currentHour);
                    timePicker.setMinute(currentMin);
                }
            }
        });

        FragmentActivity activity = getActivity();
        if(activity instanceof INewParkingDialogListener){
            listener = (INewParkingDialogListener) activity;
        } else {
            throw new RuntimeException(getString(R.string.interfaceMustImplementErrorMsg));
        }

        Bundle bundle = this.getArguments();
        final String name = bundle.getString("name", "name");
        final String price = bundle.getString("price", "price");
        final String phoneNumb = bundle.getString("phoneNumb", "36301111111");

        builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AppTheme_Purple));
        builder.setTitle(getString(R.string.prakingIn) + name + getString(R.string.inZona));
        builder.setView(contextView);
        builder.setMessage(getString(R.string.price) + price);
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //TODO send sms and save history
                sendSMS(phoneNumb, rendszamET.getText().toString(), timePicker);
                try {
                    ParkingHistory parkingHistory = new ParkingHistory(name, Integer.valueOf(price));

                    parkingHistory.save();
                    listener.onNewParkingCreated(parkingHistory);
                }catch (IllegalFormatException e){
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N) //AlarmManager.OnAlarmListener() must min api24
    public void sendSMS(String number, String rendszam, TimePicker timePicker){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, rendszam, null, null);
        Remind.getInstance(getContext()).setParkingNumber(number);
        long time = getAlarmTimeIntervalAtMillisec(Calendar.getInstance().getTime(), timePicker);
        listener.onDetach(time);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public long getAlarmTimeIntervalAtMillisec(Date date, TimePicker timePicker){
        int setHour = timePicker.getHour();
        int setMin = timePicker.getMinute();
        int differentHour = setHour - date.getHours();
        int differentMinute = setMin - date.getMinutes();
        int timeAtMinute = (differentHour*60) + differentMinute;
        return (long) timeAtMinute*60*1000;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

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
                if(!TextUtils.isEmpty(editable) && editable.toString().length() == 6){
                    ((AlertDialog)dialog).getButton(android.app.AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }else{
                    ((AlertDialog)dialog).getButton(android.app.AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }
        });
        return dialog;
    }
}
