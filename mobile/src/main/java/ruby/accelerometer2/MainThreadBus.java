package ruby.accelerometer2;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.squareup.otto.Bus;

/**
 * Created by ruby__000 on 28/11/2016.
 */


//send event from Service to Activity with Otto event bus?
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


