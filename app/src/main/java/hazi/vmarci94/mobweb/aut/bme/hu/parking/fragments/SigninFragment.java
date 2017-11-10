package hazi.vmarci94.mobweb.aut.bme.hu.parking.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import hazi.vmarci94.mobweb.aut.bme.hu.parking.R;

/**
 * Created by vmarci94 on 2017. 10. 21..
 */

public class SigninFragment extends DialogFragment {
    private static final String TAG = "SigninFragment";

    //FIXME be kell még őket kötni
    private EditText emailText;
    private EditText passwordText;
    private Button loginButton;
    private TextView signupLink;

    public SigninFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_signin, container, false);
        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE); //TITLE letiltása
        return dialog;
    }

}
