package ruby.accelerometer2;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;


import static com.google.android.gms.wearable.DataMap.TAG;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final String START_ACTIVITY = "/start_activity";


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private GoogleApiClient apiClient;
    private Receiver receiver;
    private Manager manager;
    private static final String START_ACTIVITY_PATH = "/start";
    private static final String STOP_ACTIVITY_PATH = "/stop";

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
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        receiver = Receiver.getInstance(this);
        manager = Manager.getInstance(this);

    }


    public void sendMessage(View view) {
        // this is the context, class to which to deliver intent
        Log.i(TAG, "sendMessage");
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        //Add edit text's value to the intent
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        //EXTRA_MESSAGE is public key, intents carry key value pairs.
        intent.putExtra(EXTRA_MESSAGE, message);
        //start instance of DisplayMessageActivity
        startActivity(intent);
    }



    public void onStartClick(View view) {
        Log.d(TAG, "onStartClick");
        manager.startOrStopMeasurement(START_ACTIVITY_PATH);

    }

    public void onStopClick(View view) {

        Log.i(TAG, "onStartClick");
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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    protected void onResume() {
        super.onResume();

        //bus provider here?
        manager.startOrStopMeasurement("/start");

    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.startOrStopMeasurement("/stop");
    }
}