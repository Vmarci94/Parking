package hazi.vmarci94.mobweb.aut.bme.hu.parking.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import hazi.vmarci94.mobweb.aut.bme.hu.parking.R;
import hazi.vmarci94.mobweb.aut.bme.hu.parking.interfaces.ConnectionHandlerToMyFragment;
import hazi.vmarci94.mobweb.aut.bme.hu.parking.interfaces.ConnectionHandlerToMyFragmentActivity;

/**
 * Created by vmarci94 on 2017. 10. 21..
 */

public class SignupFragment extends Fragment implements ConnectionHandlerToMyFragment {
    public static String TAG = "SignupFragment";

    private EditText email;
    private EditText password;
    private Button btn;
    private TextView tv;

    ConnectionHandlerToMyFragmentActivity connectionHandlerToMyFragmentActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_signup, container, false);
        initUI(rootView);
        return rootView;
    }

    private void initUI(View rootView) {
        email = (EditText) rootView.findViewById(R.id.input_email);
        password = (EditText) rootView.findViewById(R.id.input_password);
        btn = (Button) rootView.findViewById(R.id.btn_signup);
        tv = (TextView) rootView.findViewById(R.id.link_login);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectionHandlerToMyFragmentActivity.singUpMe(email.getText().toString(), password.getText().toString());
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
        if(firebaseUser != null){
            Log.i(TAG, "SIKERES REGISZTRÁCIÓ");
        }
    }
}
