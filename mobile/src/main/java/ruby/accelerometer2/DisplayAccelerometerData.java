package ruby.accelerometer2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by ruby__000 on 17/11/2016.
 */

public class DisplayAccelerometerData extends AppCompatActivity {
    private TextView currentX, currentY, currentZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);
        initialiseViews();
        Intent intent = getIntent();
        float[] data = intent.getFloatArrayExtra(Receiver.EXTRA_MESSAGE);
        currentX.setText(Float.toString(data[0]));
        currentY.setText(Float.toString(data[1]));
        currentZ.setText(Float.toString(data[2]));
    }

    public void initialiseViews(){
        currentX = (TextView) findViewById(R.id.currentX);
        currentY = (TextView) findViewById(R.id.currentY);
        currentZ = (TextView) findViewById(R.id.currentZ);
    }


}