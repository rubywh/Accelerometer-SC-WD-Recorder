package ruby.accelerometer2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.Arrays;


/**
 * A wearable listener service that receives incoming watch accelerometer data, unpacks it and
 * passes on accordingly such that other classes handle its usage and storage.
 */

public class Receiver extends WearableListenerService {
    private static final String TAG = "Receiver";
    public static Receiver instance;
    public Context context;
    LocalBroadcastManager broadcaster;
    private Manager manager;
    private Intent intent;

    /*public static Receiver getInstance(Context context) {
        if (instance == null) {
            instance = new Receiver(context.getApplicationContext());
        }

        return instance;
    } */

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        manager = Manager.getInstance(this);

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

                /* /accelerometer indicates that the mobile receiver has received accelerometer data*/
                if (path.startsWith("/accelerometer")) {
                    /* Unpack the data received */
                    int accuracy = dataMap.getInt("Accuracy");
                    long timestamp = dataMap.getLong("Timestamp");
                    float[] values = dataMap.getFloatArray("Values");
                    /* Log the received data*/
                    Log.i(TAG, "Accelerometer data received: " + Arrays.toString(values));

                    manager.newData(accuracy, timestamp, values);
                }
            }
        }
    }

}