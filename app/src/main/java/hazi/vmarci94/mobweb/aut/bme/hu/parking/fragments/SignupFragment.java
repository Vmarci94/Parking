package hazi.vmarci94.mobweb.aut.bme.hu.parking.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hazi.vmarci94.mobweb.aut.bme.hu.parking.R;

/**
 * Created by vmarci94 on 2017. 10. 21..
 */

public class SignupFragment extends Fragment {
    public static String TAG = "SignupFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_signup, container, false);
        initUI(rootView);
        return rootView;
    }

    private void initUI(View rootView) {
        //FIXME
    }
}
