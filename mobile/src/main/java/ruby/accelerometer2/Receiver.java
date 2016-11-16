package ruby.accelerometer2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.android.gms.wearable.DataMap.TAG;
import static ruby.accelerometer2.R.id.currentX;
import static ruby.accelerometer2.R.id.currentY;
import static ruby.accelerometer2.R.id.currentZ;

/**
 * Created by ruby__000 on 14/11/2016.
 */

public class Receiver extends WearableListenerService {

    private LinkedList<AccelerometerData> dataPoints = new LinkedList<AccelerometerData>();
    private GoogleApiClient apiClient;
    public Context context;
    public List<Node> nodes;
    public SensFragment sensFragment;
    public static Receiver instance;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Receiver(Context context) {
        this.context = context;

        apiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
        apiClient.connect();
    }

    public static Receiver getInstance(Context context) {
        if (instance == null) {
            instance = new Receiver(context.getApplicationContext());
        }

        return instance;
    }

    @Override
    public void onPeerConnected(Node peer) {
        super.onPeerConnected(peer);

        Log.i(TAG, "Connected: " + peer.getDisplayName() + " (" + peer.getId() + ")");
    }

    @Override
    public void onPeerDisconnected(Node peer) {
        super.onPeerDisconnected(peer);

        Log.i(TAG, "Disconnected: " + peer.getDisplayName() + " (" + peer.getId() + ")");
    }
@Override
    public void onDataChanged(DataEventBuffer dataEvents) {
    Log.d(TAG, "onDataChanged()");
        for (DataEvent dataEvent : dataEvents) {
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                DataMap dataMap = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();
                DataItem item = dataEvent.getDataItem();
                Uri uri = item.getUri();
                String path = uri.getPath();
                Log.d(TAG, "DataItem changed: " + uri);

                if (path.startsWith("/accelerometer")) {
                    int accuracy = dataMap.getInt("Accuracy");
                    long timestamp = dataMap.getLong("Timestamp");
                    float[] values = dataMap.getFloatArray("Values");
                    Log.d(TAG, "Accelerometer data received: " + Arrays.toString(values));
                    AccelerometerData accelerometerData = new AccelerometerData(accuracy, timestamp, values);


                }
            }
        }
    }



    public void addData(AccelerometerData accelerometerData) {
        dataPoints.addLast(accelerometerData);
        if (dataPoints.size() > 1000) {
            dataPoints.removeFirst();
        }
        //bus provider here?
    }

    public void startOrStopMeasurement(final String path) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                if (validateConnection())
                    nodes = Wearable.NodeApi.getConnectedNodes(apiClient).await().getNodes();
                for (Node node : nodes) {
                    Log.i(TAG, "add node " + node.getDisplayName());
                    Wearable.MessageApi.sendMessage(
                            apiClient, node.getId(), path, null
                    ).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                            if (!sendMessageResult.getStatus().isSuccess()) {
                                Log.v(TAG, path + " Measurement Failed");
                            } else {
                                Log.v(TAG, path + " Measurement success");
                            }
                        }
                    });
                }
            }
        });
        t.start();
    }


    private boolean validateConnection() {
        if (apiClient.isConnected()) {
            return true;
        }
        ConnectionResult result = apiClient.blockingConnect(15000, TimeUnit.MILLISECONDS);

        return result.isSuccess();
    }
}