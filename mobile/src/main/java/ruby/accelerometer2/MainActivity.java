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
    private TextView currentX, currentY, currentZ;
/*
    currentX.setText(Float.toString(values[0]));
    currentY.setText(Float.toString(values[1]));
    currentZ.setText(Float.toString(values[2]));
    */


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


        apiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        apiClient.connect();

        receiver = Receiver.getInstance(this);
        initialiseViews();

    }

    /**
     * Called when the user clicks the Send button
     */
    public void sendMessage(View view) {
        // this is the context, class to which to deliver intent

        Intent intent = new Intent(this, DisplayMessageActivity.class);
        //Add edit text's value to the intent
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        //EXTRA_MESSAGE is public key, intents carry key value pairs.
        intent.putExtra(EXTRA_MESSAGE, message);
        //start instance of DisplayMessageActivity
        startActivity(intent);
    }

    public void initialiseViews(){
        currentX = (TextView) findViewById(R.id.currentX);
        currentY = (TextView) findViewById(R.id.currentY);
        currentZ = (TextView) findViewById(R.id.currentZ);
    }


    public void onStartClick(View view) {
        sendNodes("/start");
    }

    public void sendForSync(final int accuracy, final long timestamp, final float[] values) {
        //send the data we wish to sync once the client is connected.
        if (client.isConnected()) {
            //Create a PutDataMapRequest object and set the path of the data item
            PutDataMapRequest pdmr = PutDataMapRequest.create("/accelerometer");
            //Obtain a data map that you can set values on.
            pdmr.getDataMap().putInt("Accuracy", accuracy);
            pdmr.getDataMap().putLong("Timestamp", timestamp);
            pdmr.getDataMap().putFloatArray("Values", values);
            // Obtain PutDataRequest object
            PutDataRequest request = pdmr.asPutDataRequest();
            //Request system to create the data
            Wearable.DataApi.putDataItem(client, request).setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                @Override
                public void onResult(DataApi.DataItemResult dataItemResult) {
                    if (!dataItemResult.getStatus().isSuccess()) {
                        Log.v(TAG, "Data Sync Failed");
                    } else {
                        Log.v(TAG, "Sending");
                    }
                }
            });
        }
    }

    public void onStopClick(View view) {
        sendNodes("/stop");
    }


    public void sendNodes(final String path) {
        Thread s = new Thread(new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(apiClient).await();
                for (Node node : nodes.getNodes()) {
                    Wearable.MessageApi.sendMessage(
                            apiClient, node.getId(), path, null
                    ).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                            Log.d(TAG, "controlMeasurementInBackground(" + path + "): " + sendMessageResult.getStatus().isSuccess());
                        }
                    });
                }
            }
        });
        s.start();
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
        receiver.startOrStopMeasurement("/start/");

    }

    @Override
    protected void onPause() {
        super.onPause();
        receiver.startOrStopMeasurement("/stop/");
    }
}