package ruby.accelerometer2;

/**
 * A class to represent an event whereby accelerometer readings have changed.
 * Update events are published and subscribed to on the otto event bus.
 */

public class Update {


    private AccelerometerData data;

    public Update(AccelerometerData data) {
        this.data = data;
    }

    public AccelerometerData getData() {
        return data;
    }

}

