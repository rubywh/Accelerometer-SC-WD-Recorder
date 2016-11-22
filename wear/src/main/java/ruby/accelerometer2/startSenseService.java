package ruby.accelerometer2;

import android.app.Activity;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import static com.google.android.gms.wearable.DataMap.TAG;
import static ruby.accelerometer2.DataLayer.instance;

/**
 * Created by ruby__000 on 14/11/2016.
 */

public class startSenseService extends Service implements SensorEventListener {
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private DataLayer d;
    private long lastUpdate;


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
        lastUpdate = System.currentTimeMillis();
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            int accuracy = event.accuracy;
            long timestamp = event.timestamp;
            float[] values = event.values;
            // Movement
            float x = values[0];
            float y = values[1];
            float z = values[2];

            long timeAgo = timestamp - lastUpdate;

            if (timeAgo< 200) {
                return;
            }
            lastUpdate = timestamp;
            d.sendForSync(accuracy, timestamp, values);
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
