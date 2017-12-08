package hazi.vmarci94.mobweb.aut.bme.hu.parking.interfaces;

/**
 * Created by vmarci94 on 2017. 11. 15..
 */

public interface ConnectionHandlerToMyFragmentActivity {
    /**
     * @param fragmentTAG = find fragment by tag
     */
    void showFragment(String fragmentTAG);

    void signInMe(String email, String passwd);

    void signUpMe(String email, String passwd);

    void signOutMe();
}

