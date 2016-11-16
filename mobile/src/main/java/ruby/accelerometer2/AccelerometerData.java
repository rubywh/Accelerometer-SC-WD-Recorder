package ruby.accelerometer2;

/**
 * Created by ruby__000 on 15/11/2016.
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
