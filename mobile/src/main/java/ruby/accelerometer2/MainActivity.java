package ruby.accelerometer2;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.squareup.otto.Subscribe;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final String START_ACTIVITY_PATH = "/start";
    private static final String STOP_ACTIVITY_PATH = "/stop";
    private static final String TAG = "Main";
    private TextView currentX, currentY, currentZ;
    private Realm realm;
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
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
        // Create the Realm instancejbb33

        //receiver = Receiver.getInstance(this);
        manager = Manager.getInstance(this);


    }

    /* When start button clicked, send start message to manager which will then
  begin communication with the wearable.
   */
    public void onStartClick(View view) {
        Log.d(TAG, "onStartClick");
        manager.start();
        Context context = getApplicationContext();
        CharSequence text = "Reading accelerometer data...";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /* When stop button clicked, send stop message to manager which will then
        end communication with the wearable.
         */
    public void onStopClick(View view) {
        Log.d(TAG, "onStopClick");
        manager.stop();
        Context context = getApplicationContext();
        CharSequence text = "Stopped reading accelerometer data...";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        // manager.start();
        MainThreadBus.getInstance().register(this);
        realm = Realm.getDefaultInstance();
    }


    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        manager.stop();
        // realm.close();
        MainThreadBus.getInstance().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //realm.close();
    }

    public void initialiseViews() {
        currentX = (TextView) findViewById(R.id.currentX);
        currentY = (TextView) findViewById(R.id.currentY);
        currentZ = (TextView) findViewById(R.id.currentZ);
    }

    public void updateUI(float[] s) {
        Log.d(TAG, "UpdateUI");


        //Update UI here..
        currentX.setText(Float.toString(s[0]));
        currentY.setText(Float.toString(s[1]));
        currentZ.setText(Float.toString(s[2]));
    }


    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
        realm = Realm.getDefaultInstance();
    }


    @Subscribe
    public void onSampleEvent(Update event) {
        updateUI(event.getData().getValues());

        realm.beginTransaction();
        RealmData realmData = realm.createObject(RealmData.class);
        realmData.setTimestamp(event.getData().getTimestamp());
        if (event.getData().getValues().length > 0) {
            realmData.setX(event.getData().getValues()[0]);
        } else {
            realmData.setX(0.0f);
        }

        if (event.getData().getValues().length > 0) {
            realmData.setY(event.getData().getValues()[1]);
        } else {
            realmData.setY(0.0f);
        }

        if (event.getData().getValues().length > 0) {
            realmData.setZ(event.getData().getValues()[2]);
        } else {
            realmData.setZ(0.0f);
        }

        realm.commitTransaction();
    }
    }
