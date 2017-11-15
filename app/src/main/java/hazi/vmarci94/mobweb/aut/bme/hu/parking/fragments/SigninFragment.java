package hazi.vmarci94.mobweb.aut.bme.hu.parking.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import hazi.vmarci94.mobweb.aut.bme.hu.parking.R;

/**
 * Created by vmarci94 on 2017. 10. 21..
 */

public class SigninFragment extends Fragment {
    public static final String TAG = "SigninFragment";

    //FIXME be kell még őket kötni
    private EditText emailText;
    private EditText passwordText;
    private Button loginButton;
    private TextView signupLink;

    public SigninFragment(){}

    public interface LoginNextHandler{
        public void loginNextPressed(String name);
    }

    private LoginNextHandler loginNextHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_signin, container, false);
        initUI(rootView);
        return rootView;
    }

    private void initUI(View rootView) {
        signupLink = rootView.findViewById(R.id.link_signup);
        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginNextHandler.loginNextPressed(SignupFragment.TAG);
            }
        });
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        try{
            loginNextHandler = (LoginNextHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ISignupFragmentListener");
        }
    }

}
