package ruby.accelerometer2;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by ruby__000 on 17/11/2016.
 */

public class Manager {
    private Context context;
    private static Manager instance;
    private GoogleApiClient apiClient;
    public List<Node> nodes;
    private static final String TAG = "Manager";

    public static synchronized Manager getInstance(Context context) {
        Log.d(TAG, "create Manager");
        if (instance == null) {
            instance = new Manager(context.getApplicationContext());
        }

        return instance;
    }

    private Manager(Context context) {
        Log.i(TAG, "connect API");
        this.context = context;
        this.apiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
    }



    public void startOrStopMeasurement(final String path) {
        Log.d(TAG, "start or stop measurement");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (validateConnection())
                    nodes = Wearable.NodeApi.getConnectedNodes(apiClient).await().getNodes();
                for (Node node : nodes) {
                    Log.i(TAG, "add node " + node.getDisplayName());
                    Wearable.MessageApi.sendMessage(apiClient, node.getId(), path, null).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                            if (!sendMessageResult.getStatus().isSuccess()) {
                                Log.v(TAG, path);
                            } else {
                                Log.v(TAG, path);
                            }
                        }
                    });
                }
            }
        }).start();
    }


    private boolean validateConnection() {
        if (apiClient.isConnected()) {
            return true;
        }
        ConnectionResult result = apiClient.blockingConnect(15000, TimeUnit.MILLISECONDS);

        return result.isSuccess();
    }
}
