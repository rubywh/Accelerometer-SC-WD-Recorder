package ruby.accelerometer2;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.squareup.otto.Subscribe;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main";
    private TextView currentX, currentY, currentZ;
    private Realm realm;
    private String filename;

    private Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Declare UI
        setContentView(R.layout.activity_main);
        //Set each accelerometer value displayed as 0
        initialiseViews();

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        Realm.init(this);
        //Set up a new realm configuration, initialise realm
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();

        manager = Manager.getInstance(this);


    }

    /* When start button clicked, send start message to manager which will then
  begin communication with the wearable.
   */
    public void onStartClick(View view) {
        Log.d(TAG, "onStartClick");
        manager.start();
        Context context = getApplicationContext();

        //Briefly notify the user that accelerometer data is being read
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

    /* Start the exportData activity to save data in database to files and then clear the database
     */
    public void onExportClick(View view) {
        Log.d(TAG, "onExportClick");
        Intent intent = new Intent(this, ExportData.class);
        intent.putExtra("filename", filename);
        startActivity(intent);
    }

    /* Construct filename based on user selected age, gender, height and generated UUID
    * This is passed to the exportData activity so to store each file with the appropriate filename.
    * */
    private void makeFilename(String uid, String gender, String age, String pHeight) {
        Log.d(TAG, "made filename");
        filename = (uid + "_" + gender + "_" + age + "_" + pHeight);
    }

    /*Store user entered details that are then used to generate filename*/
    public void onSaveClick(View view) {
        Log.d(TAG, "onSaveClick");
        String uid = UUID.randomUUID().toString();
        String age = ((EditText) findViewById(R.id.age)).getText().toString();
        String gender = ((EditText) findViewById(R.id.gender)).getText().toString();
        String height = ((EditText) findViewById(R.id.height)).getText().toString();
        makeFilename(uid, gender, age, height);
    }


    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
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


    /* A method that acts based on when a new event has been posted on the event bus
    * i.e. when there is a new reading, update the ui and write to the database
     */
    @Subscribe
    public void onSampleEvent(Update event) {
        //Set the X, Y and Z values on the tablet to those received from the watch
        updateUI(event.getData().getValues());

        realm.beginTransaction();
        RealmData realmData = realm.createObject(RealmData.class);
        realmData.setTimestamp(event.getData().getTimestamp());
        //If there are values received
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
