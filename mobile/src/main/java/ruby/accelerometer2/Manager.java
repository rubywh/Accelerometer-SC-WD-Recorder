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
 * A class that directs the wearable as to starting or stopping their 'Receiver' service
 * This is done by finding the conencted node and sending a message to it containing a path
 * The path is either "start" or "stop".
 */



public class Manager {
    private static final String TAG = "Manager";
    private static Manager instance;
    private List<Node> nodes;
    private Context context;
   // private Context context;
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

    /* Called from MainActivity: onStartClick or onStopClick
             */

    public void stop(){
        e.submit(new Runnable() {
            @Override
            public void run() {
                measurementS("/stop");
            }
        });
    }
    public void start(){
        e.submit(new Runnable() {
            @Override
            public void run() {
                measurementS("/start");
            }
        });
    }


    public void measurementS(final String path) {
        Log.d(TAG, "measurement");
        /* Start a new thread
             */
       // new Thread(new Runnable() {
           // @Override
            //public void run() {
                /* Check for connection to the google API client, attempt connection
                 if not one already */
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
       //  }).start();
    }
    /* Called from startOrStopMeasurement to check if there is a connection to the google API client*/
    private boolean checkConnected() {
        if (apiClient.isConnected()) {
            return true;
        }
        /* Else, attempt to make conenction */
        ConnectionResult result = apiClient.blockingConnect(15000, TimeUnit.SECONDS);
        return result.isSuccess();
    }

    public synchronized void newData(int accuracy, long timestamp, float[] values) {
        Log.d(TAG, "newData");
        AccelerometerData data = new AccelerometerData(accuracy, timestamp, values);
        MainThreadBus.myPost(new Update(data));
    }

}
