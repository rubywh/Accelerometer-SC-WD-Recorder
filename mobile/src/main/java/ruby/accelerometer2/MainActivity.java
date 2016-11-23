package ruby.accelerometer2;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import static com.google.android.gms.wearable.DataMap.TAG;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final String START_ACTIVITY_PATH = "/start";
    private static final String STOP_ACTIVITY_PATH = "/stop";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient apiClient;
    private Receiver receiver;
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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        apiClient = new GoogleApiClient.Builder(this)
                .addApi(AppIndex.API)
                .build();

        receiver = Receiver.getInstance(this);
        manager = Manager.getInstance(this);

    }

    /* When start button clicked, send start message to manager which will then
    begin communication with the wearable.
     */
    public void onStartClick(View view) {
        Log.d(TAG, "onStartClick");
        manager.startOrStopMeasurement(START_ACTIVITY_PATH);
    }
    /* When stop button clicked, send stop message to manager which will then
        end communication with the wearable.
         */
    public void onStopClick(View view) {
        Log.d(TAG, "onStartClick");
        manager.startOrStopMeasurement(STOP_ACTIVITY_PATH);
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        apiClient.connect();
        AppIndex.AppIndexApi.start(apiClient, getIndexApiAction());
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(apiClient, getIndexApiAction());
        apiClient.disconnect();
    }
    /* Tell manager to resume communication with wearable.
         */
    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        manager.startOrStopMeasurement(START_ACTIVITY_PATH);
    }
    /* Tell manager to stop communication with wearable.
       */
    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        manager.startOrStopMeasurement(STOP_ACTIVITY_PATH);
    }
}