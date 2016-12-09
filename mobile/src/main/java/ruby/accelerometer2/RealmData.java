package ruby.accelerometer2;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * A class for creating RealmData objects that packages up accelerometer data into an entity
 * that is stored on the Realm database.
 */

@RealmClass
public class RealmData extends RealmObject {
    private long timestamp;
    private float X;
    private float Y;
    private float Z;
    private int accuracy;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float getX() {
        return X;
    }

    public void setX(float myX) {
        this.X = myX;
    }

    public float getY() {
        return Y;
    }

    public void setY(float myY) {
        this.Y = myY;
    }

    public float getZ() {
        return Z;
    }

    public void setZ(float myZ) {
        this.Z = myZ;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int myAccuracy) {
        accuracy = myAccuracy;
    }

}
