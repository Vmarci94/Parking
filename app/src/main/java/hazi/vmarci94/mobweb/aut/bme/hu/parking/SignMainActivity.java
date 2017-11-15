package hazi.vmarci94.mobweb.aut.bme.hu.parking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;

import hazi.vmarci94.mobweb.aut.bme.hu.parking.fragments.SigninFragment;
import hazi.vmarci94.mobweb.aut.bme.hu.parking.fragments.SignupFragment;

public class SignMainActivity extends FragmentActivity implements SigninFragment.LoginNextHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        showFragment(SigninFragment.TAG);
    }

    public void showFragment(String tag){
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if(fragment == null){
            if(SigninFragment.TAG.equals(tag)){
                fragment = new SigninFragment();
            }else {
                fragment = new SignupFragment();
            }
        }
        if(fragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentLayoutContainer, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (getFragmentManager().getBackStackEntryCount() > 1){ // ActionBarActivity-nél kell
            getFragmentManager().popBackStackImmediate();
            getFragmentManager().beginTransaction().commit();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void loginNextPressed(String name) {
        showFragment(name);
    }
}
