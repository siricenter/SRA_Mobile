package functionalTests;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;
import android.widget.EditText;

import com.example.chad.sra_mobile.DashBoard;
import com.example.chad.sra_mobile.R;
import com.example.chad.sra_mobile.login;

/**
 * Created by jakobhartman on 11/13/14.
 */
public class loginTest extends ActivityInstrumentationTestCase2<login> {
    private static final int TIMEOUT_IN_MS = 10000;
    private static final String username = "testemail@hotmail.com";
    private static final String password = "rbdc";
    private login mlogin;

    public loginTest(){super(login.class);}

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        setActivityInitialTouchMode(true);
        mlogin = getActivity();
    }


    @MediumTest
    public void testloginSuccess(){

        final EditText enterUsername = (EditText) mlogin.findViewById(R.id.usernameET);
        final EditText enterPassword = (EditText) mlogin.findViewById(R.id.passwordET);
        final Button login = (Button) mlogin.findViewById(R.id.loginButton);

        //Select the username text field
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                enterUsername.requestFocus();
            }
        });

        Instrumentation.ActivityMonitor dashboardMonitor = getInstrumentation()
                .addMonitor(DashBoard.class.getName(), null, false);

        getInstrumentation().waitForIdleSync();
        //Enter username
        getInstrumentation().sendStringSync(username);
        getInstrumentation().waitForIdleSync();

        //Select the password field
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                enterPassword.requestFocus();
            }
        });

        getInstrumentation().waitForIdleSync();
        //Enter password
        getInstrumentation().sendStringSync(password);
        getInstrumentation().waitForIdleSync();

        //Press Login Button
        TouchUtils.clickView(this,login);

        getInstrumentation().waitForIdleSync();


        getInstrumentation().waitForIdleSync();

        //onAuthenticated was fired
        assertTrue("Failed to login",mlogin.getStatus());
    }


    @MediumTest
    public void testLoginFail(){
        final EditText enterUsername = (EditText) mlogin.findViewById(R.id.usernameET);
        final EditText enterPassword = (EditText) mlogin.findViewById(R.id.passwordET);
        final Button login = (Button) mlogin.findViewById(R.id.loginButton);

        //Select the username text field
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                enterUsername.requestFocus();
            }
        });

        Instrumentation.ActivityMonitor dashboardMonitor = getInstrumentation()
                .addMonitor(DashBoard.class.getName(), null, false);

        getInstrumentation().waitForIdleSync();
        //Enter username
        getInstrumentation().sendStringSync("bobdillian@hotmail.com");
        getInstrumentation().waitForIdleSync();

        //Select the password field
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                enterPassword.requestFocus();
            }
        });

        getInstrumentation().waitForIdleSync();
        //Enter password
        getInstrumentation().sendStringSync("Frank");
        getInstrumentation().waitForIdleSync();

        //Press Login Button
        TouchUtils.clickView(this,login);

        getInstrumentation().waitForIdleSync();

        //User has successfully logged in

        getInstrumentation().waitForIdleSync();

        //Ran Next Activity
        assertFalse("Was not able to login with Firebase",mlogin.getStatus());
    }
}
