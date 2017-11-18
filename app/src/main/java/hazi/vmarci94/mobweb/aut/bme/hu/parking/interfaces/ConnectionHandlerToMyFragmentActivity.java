package hazi.vmarci94.mobweb.aut.bme.hu.parking.interfaces;

/**
 * Created by vmarci94 on 2017. 11. 15..
 */

public interface ConnectionHandlerToMyFragmentActivity {
    /**
     * @param name = SignupFragment
     */
    void signUpWithSignUpFragment(String name);

    void signInMe(String email, String passwd);

    void signUpMe(String email, String passwd);

    void signOutMe();
}

