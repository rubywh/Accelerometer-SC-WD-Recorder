package ruby.accelerometer2;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import static ruby.accelerometer2.DataLayer.instance;

/**
 * Created by ruby__000 on 14/11/2016.
 */

public class startSenseActivity extends Activity implements SensorEventListener {
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private DataLayer d;
    private long lastUpdate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        d = DataLayer.getInstance(this);
        int notificationId = 001;
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
        .setContentTitle("Accelerometer").setContentText("Reading Accelerometer Data...");
        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        // Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());


        //fetch the system's SensorManager instance. get a reference to a service of the system by passing the name of the service
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //get accelerometer
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //register the sensor, use context, name and rate at which sensor events are delivered to us.
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        lastUpdate = System.currentTimeMillis();
    }

    // invoked every time the built-in sensor detects a change
    @Override
    public void onSensorChanged(SensorEvent event) {
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


    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

}
