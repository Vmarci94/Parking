package hazi.vmarci94.mobweb.aut.bme.hu.parking;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import hazi.vmarci94.mobweb.aut.bme.hu.parking.fragments.SigninFragment;

public class SignMainActivity extends FragmentActivity {

    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        fragmentManager = getSupportFragmentManager();
        showFragment();
    }

    public void showFragment(){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        SigninFragment signinFragment = new SigninFragment();
        signinFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_Dark);
        signinFragment.show(fragmentManager, null);
        /*
        fragmentTransaction.replace(R.id.layoutContainer, signinFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    */
    }
}
