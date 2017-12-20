package hazi.vmarci94.mobweb.aut.bme.hu.parking.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import hazi.vmarci94.mobweb.aut.bme.hu.parking.MapsMainActivity;
import hazi.vmarci94.mobweb.aut.bme.hu.parking.R;
import hazi.vmarci94.mobweb.aut.bme.hu.parking.interfaces.ConnectionHandlerToMyFragment;
import hazi.vmarci94.mobweb.aut.bme.hu.parking.interfaces.ConnectionHandlerToMyFragmentActivity;

/**
 * Created by vmarci94 on 2017. 10. 21..
 */

public class SigninFragment extends Fragment implements ConnectionHandlerToMyFragment{
    public static final String TAG = "SigninFragment";

    public static final int ENTER_REQUEST = 500;
    public static final int SIGNOUT_RESAULT = 501;


    //FIXME be kell még őket kötni
    private EditText emailText;
    private EditText passwordText;

    public SigninFragment(){}

    private ConnectionHandlerToMyFragmentActivity connectionHandlerToMyFragmentActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_signin, container, false);
        initUI(rootView);
        return rootView;
    }

    private void initUI(View rootView) {
        emailText =  (EditText) rootView.findViewById(R.id.input_email);
        passwordText = (EditText) rootView.findViewById(R.id.input_password);
        TextView signupLink = (TextView) rootView.findViewById(R.id.link_signup);
        Button btn = (Button) rootView.findViewById(R.id.btn_login);

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectionHandlerToMyFragmentActivity.showFragment(SignupFragment.TAG);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectionHandlerToMyFragmentActivity.signInMe(emailText.getText().toString(), passwordText.getText().toString());
            }
        });



    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        try{
            connectionHandlerToMyFragmentActivity = (ConnectionHandlerToMyFragmentActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ISignupFragmentListener");
        }
    }


    @Override
    public void updateUI(FirebaseUser firebaseUser) {
        if(firebaseUser != null && !firebaseUser.isAnonymous()) {
            startActivityForResult(new Intent(getActivity(), MapsMainActivity.class), ENTER_REQUEST);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ENTER_REQUEST) {
            // Make sure the request was successful
            if (resultCode == SIGNOUT_RESAULT) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                connectionHandlerToMyFragmentActivity.signOutMe();
                // Do something with the contact here (bigger example below)
            }
        }
    }


}
