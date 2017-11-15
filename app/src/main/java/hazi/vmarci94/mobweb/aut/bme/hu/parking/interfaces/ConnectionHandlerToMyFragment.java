package hazi.vmarci94.mobweb.aut.bme.hu.parking.interfaces;

import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by vmarci94 on 2017. 11. 15..
 */

public interface ConnectionHandlerToMyFragment {
    void updateUI(@Nullable FirebaseUser firebaseUser);
}
