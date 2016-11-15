package ruby.accelerometer2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by ruby__000 on 14/11/2016.
 */

public class DataLayer{

    private GoogleApiClient apiClient;
    private Context context;
    public static DataLayer instance;

    private DataLayer(Context context) {
        this.context = context;

        apiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
        apiClient.connect();
    }

    public void sendForSync(final int accuracy, final long timestamp, final float[] values){
            //send the data we wish to sync once the client is connected.
            if(apiClient.isConnected()){
                //Create a PutDataMapRequest object and set the path of the data item
                PutDataMapRequest pdmr = PutDataMapRequest.create("/accelerometer");
                //Obtain a data map that you can set values on.
                pdmr.getDataMap().putInt("Accuracy", accuracy);
                pdmr.getDataMap().putLong("Timestamp", timestamp);
                pdmr.getDataMap().putFloatArray("Values", values);
                // Obtain PutDataRequest object
                PutDataRequest request = pdmr.asPutDataRequest();
                //Request system to create the data
                Wearable.DataApi.putDataItem(apiClient, request).setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(DataApi.DataItemResult dataItemResult) {
                        if (!dataItemResult.getStatus().isSuccess()) {
                            Log.v(TAG, "Data Sync Failed");
                        }else{
                            Log.v(TAG, "Sending");
                        }
                    }
                });
            }
        }

    }


