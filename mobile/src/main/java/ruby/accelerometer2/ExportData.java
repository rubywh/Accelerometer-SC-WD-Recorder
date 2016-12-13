package ruby.accelerometer2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * An Activity to handle exporting stored data in Realm to appropriately named files.
 * This is currently called when the user presses the export button.
 */

public class ExportData extends AppCompatActivity {

    private static final String TAG = "ExportActivity";
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private Realm realm;

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Created");
        super.onCreate(savedInstanceState);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run");
                exportThisFile(getIntent().getStringExtra("filename"));
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void exportThisFile(String filename) {
        Log.d(TAG, "ExportThisFile");
        System.out.println(filename);
        //Get the database instance and then find everything within it
        realm = Realm.getDefaultInstance();
        System.out.println(realm);
        RealmResults<RealmData> database = realm.where(RealmData.class).findAll();
        System.out.println(database);
        //Get the total number of sets of accelerometer readings currently in the database
        final int totalRows = database.size();
        System.out.println(totalRows);

        try {
            Context context = this.getApplicationContext();

            File exportDir = new File(Environment.getExternalStorageDirectory(), "");
            System.out.println(exportDir);

            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            File file = new File(exportDir, filename + ".csv");

            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

            //FileWriter fw = new FileWriter(file);
            //BufferedWriter bw = new BufferedWriter(fw);



            //Write each realm row as a row in a file (comma separated)
            for (int currentRealmRow = 1; currentRealmRow < totalRows; currentRealmRow++) {

                //Construct the string in a buffer before writing that entire row at once.
                String[] dataArray = new String[4];
                dataArray[0] = String.valueOf(database.get(currentRealmRow).getTimestamp());
                dataArray[1] = String.valueOf(database.get(currentRealmRow).getX());
                dataArray[2] = String.valueOf(database.get(currentRealmRow).getY());
                dataArray[3] = String.valueOf(database.get(currentRealmRow).getZ());


                /* StringBuffer sb = new StringBuffer(String.valueOf(database.get(currentRealmRow).getTimestamp()));
                sb.append(",");
                sb.append(String.valueOf(database.get(currentRealmRow).getX()));
                sb.append(",");
                sb.append(String.valueOf(database.get(currentRealmRow).getY()));
                sb.append(",");
                sb.append(String.valueOf(database.get(currentRealmRow).getZ()));
                sb.append("\n"); */

                System.out.println(dataArray);
                //bw.write(sb.toString());
                csvWrite.writeNext(dataArray);
            }
            csvWrite.close();
            // bw.flush();
            //  bw.close();
            Log.d(TAG, "Successful write");
        } catch (IOException e) {
            Log.e(TAG, "IOException, can't write file");
            e.printStackTrace();
        }
        //Once file has been written, clear the database
        realm.beginTransaction();
        RealmResults<RealmData> realmResults = realm.where(RealmData.class).findAll();
        realmResults.deleteAllFromRealm();
        realm.commitTransaction();
        Log.d(TAG, "Realm cleared");
    }
}