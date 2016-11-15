package ruby.accelerometer2;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.Arrays;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by ruby__000 on 14/11/2016.
 */

public class Receiver extends WearableListenerService {


    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent dataEvent : dataEvents) {
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                DataMap dataMap = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();
                DataItem item = dataEvent.getDataItem();
                Uri uri = item.getUri();
                String path = uri.getPath();
                Log.d(TAG, "DataItem changed: " + uri);

                if (path.startsWith("todo")) {
                    int accuracy = dataMap.getInt("accuracy");
                    long timestamp = dataMap.getLong("timestamp");
                    float[] values = dataMap.getFloatArray("values");
                    Log.d(TAG, "Accelerometer data received: " + Arrays.toString(values));
                    //todo
                }
            }
        }
    }
}
