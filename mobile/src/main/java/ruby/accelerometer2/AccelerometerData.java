package ruby.accelerometer2;

/**
 * Accelerometer data objects include a timestamp, a set of values and an accuracy value
 * A new accelerometer data object is created each time the mobile  Receiver.class
 * detects a change in data received from the wear side. Each time a new object is created,
 * this event is posted on the event bus from within Manager.class.
 */

public class AccelerometerData {

    private long timestamp;
    private float[] values;
    private int accuracy;

    public AccelerometerData(int accuracy, long timestamp, float[] values) {

        this.accuracy = accuracy;
        this.timestamp = timestamp;
        this.values = values;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public float[] getValues() {
        return values;
    }

}
