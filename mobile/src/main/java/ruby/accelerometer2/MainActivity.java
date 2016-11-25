package ruby.accelerometer2;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import static com.google.android.gms.wearable.DataMap.TAG;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final String START_ACTIVITY_PATH = "/start";
    private static final String STOP_ACTIVITY_PATH = "/stop";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private Receiver receiver;
    private Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Declare UI
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // For the main activity, make sure the app icon in the action bar
            // does not behave as a button
            //`  ActionBar actionBar = getActionBar();
            //  actionBar.setHomeButtonEnabled(false);
        }


        //receiver = Receiver.getInstance(this);
        manager = Manager.getInstance(this);

    }

    /* When start button clicked, send start message to manager which will then
    begin communication with the wearable.
     */
    public void onStartClick(View view) {
        Log.d(TAG, "onStartClick");
        manager.start();
    }
    /* When stop button clicked, send stop message to manager which will then
        end communication with the wearable.
         */
    public void onStopClick(View view) {
        Log.d(TAG, "onStopClick");
        manager.stop();
    }


/*

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        apiClient.connect();
    }

    */
/*
    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();

        apiClient.disconnect();
    }
    */


    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        manager.start();
    }


    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        manager.stop();
    }
}