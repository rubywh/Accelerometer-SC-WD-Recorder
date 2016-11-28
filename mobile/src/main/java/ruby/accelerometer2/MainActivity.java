package ruby.accelerometer2;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final String START_ACTIVITY_PATH = "/start";
    private static final String STOP_ACTIVITY_PATH = "/stop";
    private static final String TAG = "Main";
    private TextView currentX, currentY, currentZ;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private Receiver receiver;
    private Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Declare UI
        setContentView(R.layout.activity_main);
        initialiseViews();


        //receiver = Receiver.getInstance(this);
        manager = Manager.getInstance(this);

    }

    /* When start button clicked, send start message to manager which will then
  begin communication with the wearable.
   */
    public void onStartClick(View view) {
        Log.d(TAG, "onStartClick");
        manager.start();
    }
    /* When stop button clicked, send stop message to manager which will then
        end communication with the wearable.
         */
    public void onStopClick(View view) {
        Log.d(TAG, "onStopClick");
        manager.stop();
    }



    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        // manager.start();
        MainThreadBus.getInstance().register(this);
    }


    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        manager.stop();
        MainThreadBus.getInstance().unregister(this);
    }


    public void initialiseViews() {
        currentX = (TextView) findViewById(R.id.currentX);
        currentY = (TextView) findViewById(R.id.currentY);
        currentZ = (TextView) findViewById(R.id.currentZ);
    }

    public void updateUI(float[] s) {
        Log.d(TAG, "UpdateUI");

        //Update Your UI here..
        currentX.setText(Float.toString(s[0]));
        currentY.setText(Float.toString(s[1]));
        currentZ.setText(Float.toString(s[2]));
    }

    /*@Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }*/


    @Subscribe
    public void onSampleEvent(Update event) {
        updateUI(event.getData().getValues());
    }

}