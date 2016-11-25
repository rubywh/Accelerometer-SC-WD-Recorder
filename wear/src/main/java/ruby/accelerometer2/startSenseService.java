package ruby.accelerometer2;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.util.SparseLongArray;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * Created by ruby__000 on 14/11/2016.
 */

public class startSenseService extends Service implements SensorEventListener {
    private SensorManager senSensorManager;
    private DataLayer d;
    Sensor senAccelerometer;
    private static final String TAG = "SenseService";


    @Override
    public void onCreate() {
        Log.d(TAG, "startSensorService");
        super.onCreate();


        d = DataLayer.getInstance(this);



        int notificationId = 001;

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Sensor Dashboard");
        builder.setContentText("Collecting sensor data..");

        startForeground(1, builder.build());

        //fetch the system's SensorManager instance. get a reference to a service of the system by passing the name of the service
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //get accelerometer
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //register the sensor, use context, name and rate at which sensor events are delivered to us.
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

    }

    // invoked every time the built-in sensor detects a change
    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.i(TAG, "onSensorChanged");
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
             d.sendForSync(event.accuracy, event.timestamp, event.values);
            }
        }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        senSensorManager.unregisterListener(this);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
