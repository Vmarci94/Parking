package hazi.vmarci94.mobweb.aut.bme.hu.parking;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import hazi.vmarci94.mobweb.aut.bme.hu.parking.fragments.SigninFragment;
import hazi.vmarci94.mobweb.aut.bme.hu.parking.fragments.SignupFragment;
import hazi.vmarci94.mobweb.aut.bme.hu.parking.interfaces.ConnectionHandlerToMyFragment;
import hazi.vmarci94.mobweb.aut.bme.hu.parking.interfaces.ConnectionHandlerToMyFragmentActivity;

public class SignMainActivity extends FragmentActivity implements ConnectionHandlerToMyFragmentActivity {

    public static final String TAG = "SignMainActivity";

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    ConnectionHandlerToMyFragment mConnectionHandlerToSigninFragment;
    ConnectionHandlerToMyFragment mConnectionHandlerToSignupFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        showFragment(SigninFragment.TAG);
    }

    private void sendEmailVerification() {
        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]

                        if (task.isSuccessful()) {
                            Toast.makeText(SignMainActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(SignMainActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }

    private boolean validateForm(String email, String password) {
        boolean valid = true;
        if (TextUtils.isEmpty(email)) {
            valid = false;
        } else {
            //FIXME ?!?!
        }

        if (TextUtils.isEmpty(password)) {
            valid = false;
        } else {
            //FIXME ?!?!
        }

        return valid;
    }


    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm(email, password)) {
            return;
        }

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            sendEmailVerification();
                            mConnectionHandlerToSignupFragment.updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignMainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            mConnectionHandlerToSignupFragment.updateUI(null);
                        }

                    }
                });
        // [END create_user_with_email]
    }


    public void showFragment(String tag) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            if (SigninFragment.TAG.equals(tag)) {
                fragment = new SigninFragment();
                mConnectionHandlerToSigninFragment = (ConnectionHandlerToMyFragment) fragment;
            } else {
                fragment = new SignupFragment();
                mConnectionHandlerToSignupFragment = (ConnectionHandlerToMyFragment) fragment;
            }
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentLayoutContainer, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (getFragmentManager().getBackStackEntryCount() > 1) { // ActionBarActivity-nél kell
            getFragmentManager().popBackStackImmediate();
            getFragmentManager().beginTransaction().commit();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void singUpWithSignUpFragment(String name) {
        showFragment(name);
    }

    @Override
    public void singInMe(String email, String passwd) {
        signIn(email, passwd);
    }

    @Override
    public void singUpMe(String email, String passwd) {
        createAccount(email, passwd);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mConnectionHandlerToSigninFragment.updateUI(currentUser);
    }

    private void signOut() {
        mAuth.signOut();
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm(email, password)) {
            return;
        }

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            mConnectionHandlerToSigninFragment.updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignMainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            mConnectionHandlerToSignupFragment.updateUI(null); //FIXME crash
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            //mStatusTextView.setText(R.string.auth_failed);
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

}