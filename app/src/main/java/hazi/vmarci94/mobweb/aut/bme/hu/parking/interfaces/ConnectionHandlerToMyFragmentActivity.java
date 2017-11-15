package hazi.vmarci94.mobweb.aut.bme.hu.parking.interfaces;

/**
 * Created by vmarci94 on 2017. 11. 15..
 */

public interface ConnectionHandlerToMyFragmentActivity {
    /**
     * @param name = SignupFragment
     */
    void singUpWithSignUpFragment(String name);

    void singInMe(String email, String passwd);

    void singUpMe(String email, String passwd);
}

