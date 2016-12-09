package ruby.accelerometer2;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * Handles sending of start reading/stop reading messages to the connected watch
 */


public class Manager {
    private static final String TAG = "Manager";
    private static Manager instance;
    private List<Node> nodes;
    private Context context;
    private GoogleApiClient apiClient;
    private ExecutorService e;

    private Manager(Context context) {
        this.context = context;
        Log.i(TAG, "connect API");
        this.context = context;
        this.apiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
        this.e = Executors.newCachedThreadPool();
    }

    public static synchronized Manager getInstance(Context context) {
        if (instance == null) {
            instance = new Manager(context.getApplicationContext());
        }
        return instance;
    }

    public void stop() {
        e.submit(new Runnable() {
            @Override
            public void run() {
                measurementS("/stop");
            }
        });
    }

    public void start() {
        e.submit(new Runnable() {
            @Override
            public void run() {
                measurementS("/start");
            }
        });
    }


    public void measurementS(final String path) {
        Log.d(TAG, "measurement");

        if (checkConnected()) {
            nodes = Wearable.NodeApi.getConnectedNodes(apiClient).await().getNodes();
            for (Node node : nodes) {
                Log.i(TAG, "add node " + node.getDisplayName());
                Wearable.MessageApi.sendMessage(apiClient, node.getId(), path, null)
                        .setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                            @Override
                            public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                                /* Return success or fail based on whether the message has been
                                successfully sent to the wearable */
                                if (!sendMessageResult.getStatus().isSuccess()) {
                                    Log.v(TAG, path);
                                } else {
                                    Log.v(TAG, path);
                                }
                            }
                        });
            }
        }
    }

    /* Called from startOrStopMeasurement to check if there is a connection to the google API client*/
    private boolean checkConnected() {
        if (apiClient.isConnected()) {
            return true;
        }
        /* Else, attempt to make connection */
        ConnectionResult result = apiClient.blockingConnect(15000, TimeUnit.SECONDS);
        return result.isSuccess();
    }

    public synchronized void newData(int accuracy, long timestamp, float[] values) {
        Log.d(TAG, "newData");
        AccelerometerData data = new AccelerometerData(accuracy, timestamp, values);
        //Post the event of an accelerometer data update to the event bus.
        MainThreadBus.myPost(new Update(data));
    }

}
