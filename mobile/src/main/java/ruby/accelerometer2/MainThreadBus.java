package ruby.accelerometer2;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.squareup.otto.Bus;

/**
 * Otto event bus is used here as an efficient way to implement pub/sub such that when there is
 * an update in acceleromter readings, this is posted on the bus and subscribing methods will
 * be notified and act accordingly. It is an easy way to communicate between a service
 * (Receiver.class) and an Activity (MainActivity.class).
 */


public class MainThreadBus extends Bus {
    private static final String TAG = "Bus";
    private static final Bus BUS = new Bus();

    private MainThreadBus() {
    }

    public static Bus getInstance() {
        return BUS;
    }

    public static void myPost(final Object event) {
        Log.d(TAG, "Post");
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                BUS.post(event);
            }
        });
    }
}


