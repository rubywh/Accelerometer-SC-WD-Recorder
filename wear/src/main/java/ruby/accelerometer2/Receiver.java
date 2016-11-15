package ruby.accelerometer2;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

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
                Log.d(TAG, "DataItem changed: " + dataEvent.getDataItem().getUri());

                //todo
            }
        }
    }

    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals("/start")) {
            Intent startIntent = new Intent(this, startSenseActivity.class);
            startService(startIntent);
        } else if (messageEvent.getPath().equals("/stop")) {
            Intent stopIntent = new Intent(this, startSenseActivity.class);
            stopService(stopIntent);
        }
    }
}
