package ruby.accelerometer2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.Arrays;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by ruby__000 on 14/11/2016.
 */

public class Receiver extends WearableListenerService {

    public final static String EXTRA_MESSAGE = "example message";
    public static Receiver instance;
    public Context context;

    public Receiver(Context context) {
        this.context = context;

        GoogleApiClient apiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
        apiClient.connect();
    }
    public Receiver(){

    }

    public static Receiver getInstance(Context context) {
        if (instance == null) {
            instance = new Receiver(context.getApplicationContext());
        }

        return instance;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
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
    /*  */
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

                /* /accelerometer indicates that the movile receiver has received accelerometer data*/
                if (path.startsWith("/accelerometer")) {
                    /* Unpack the data received */
                    int accuracy = dataMap.getInt("Accuracy");
                    long timestamp = dataMap.getLong("Timestamp");
                    float[] values = dataMap.getFloatArray("Values");
                    /* Log the received data*/
                    Log.i(TAG, "Accelerometer data received: " + Arrays.toString(values));
                    AccelerometerData accelerometerData = new AccelerometerData(accuracy, timestamp, values);
                    /* Start the DisplayAccelerometerData class that will display the changing
                    accelerometer data to the mobile screen
                     */
                    Intent intent = new Intent(this, DisplayAccelerometerData.class);
                    intent.putExtra(EXTRA_MESSAGE, values);
                    startActivity(intent);
                }
            }
        }
    }
}