package dailyselfie.mateialexandru.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends Activity {

    static final String TAG = "Daily-Selfie";
    static final String TAG_PHOTO_LIST_FRAGMENT = "photo_list_fragment";
    static final String TAG_SINGLE_PHOTO_FRAGMENT = "single_photo_fragment";
    static final String TAG_PHOTO_FRAGMENT = "photo_fragment";
    static final int SPLASH_TIME_OUT = 100;

    private LoginButton mLoginButton;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.login);
        mLoginButton = (LoginButton) findViewById(R.id.login_button);

        mLoginButton.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (AccessToken.getCurrentAccessToken() != null) {

                    Toast.makeText(MainActivity.this, "Facebook already logged in", Toast.LENGTH_SHORT).show();
                    startApplication();

                } else {

                    Toast.makeText(MainActivity.this, "Facebook not logged in", Toast.LENGTH_SHORT).show();
                    LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            startApplication();
                        }

                        @Override
                        public void onCancel() {
                            Toast.makeText(MainActivity.this, "Login canceled", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(FacebookException error) {
                            Toast.makeText(MainActivity.this, "Error on login.", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "" + error);

                        }
                    });
                }

            }

        }, SPLASH_TIME_OUT);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void startApplication() {
        Intent intent = new Intent(MainActivity.this, DailySelfieActivity.class);
        startActivity(intent);
        finish();
    }


}
