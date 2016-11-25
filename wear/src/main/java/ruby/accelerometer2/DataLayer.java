package ruby.accelerometer2;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Created by ruby__000 on 14/11/2016.
 */

public class DataLayer {
    private static final String TAG = "DataLayer";
    public static DataLayer instance;
    private ArrayList<Long> lastSensorData;
    private ExecutorService e;


    private Context context;
    private GoogleApiClient apiClient;

    private DataLayer(Context context) {
        Log.i(TAG, "Created");
        e = Executors.newCachedThreadPool();
        this.context = context;
        lastSensorData = new ArrayList<Long>();
        apiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
    }

    public static DataLayer getInstance(Context context) {
        if (instance == null) {
            instance = new DataLayer(context.getApplicationContext());
        }

        return instance;
    }

    private boolean checkConnection() {
        if (apiClient.isConnected()) {
            return true;
        }    ConnectionResult result = apiClient.blockingConnect(15000, TimeUnit.MILLISECONDS);

        return result.isSuccess();
    }

    public void sendForSync(final int accuracy, final long timestamp, final float[] values) {
        Log.i(TAG, "Sensor " + Arrays.toString(values));
        long time = System.currentTimeMillis();
        if(lastSensorData.isEmpty()){
            lastSensorData.add(0L);
        }
        long lastUpdate = lastSensorData.get(lastSensorData.size()-1);
        long timeAgo = time - lastUpdate;
        if (lastUpdate != 0) {
            if (timeAgo < 100) {
                return;
            }
        }
        lastSensorData.add(timestamp);


        e.submit(new Runnable() {
            @Override
            public void run() {
                send(accuracy, timestamp, values);
            }
        });
    }

        public void send(int accuracy, long timestamp, float[] values){
       // Thread t = new Thread(new Runnable() {
            //@Override
            //public void run() {
                Log.i(TAG, "run");
                //send the data we wish to sync once the client is connected.
                if (checkConnection()) {
                    //Create a PutDataMapRequest object and set the path of the data item
                    PutDataMapRequest pdmr = PutDataMapRequest.create("/accelerometer");
                    pdmr.setUrgent();
                    //Obtain a data map that you can set values on.
                    pdmr.getDataMap().putInt("Accuracy", accuracy);
                    pdmr.getDataMap().putLong("Timestamp", timestamp);
                    pdmr.getDataMap().putFloatArray("Values", values);
                    // Obtain PutDataRequest object
                    PutDataRequest request = pdmr.asPutDataRequest();
                    //Request system to create the data
                    Log.i(TAG, "pdmr request");
                    Wearable.DataApi.putDataItem(apiClient, request).setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                        @Override
                        public void onResult(DataApi.DataItemResult dataItemResult) {
                            Log.v(TAG, "Sending sensor data: " + dataItemResult.getStatus().isSuccess());
                        }
                    });
                } else {
                    Log.i(TAG, "No connection");
                }
            }
        //});
       // t.start();
    }

