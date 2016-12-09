package ruby.accelerometer2;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedWriter;
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
    private Realm realm;

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
            final File path = context.getFilesDir();
            final File file = new File(path, filename + ".txt");
            System.out.println(path);
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            Log.d(TAG, "fw bw created");

            //Write each realm row as a row in a file (comma separated)
            for (int currentRealmRow = 1; currentRealmRow < totalRows; currentRealmRow++) {

                //Construct the string in a buffer before writing that entire row at once.
                StringBuffer sb = new StringBuffer(String.valueOf(database.get(currentRealmRow).getTimestamp()));
                sb.append(",");
                sb.append(String.valueOf(database.get(currentRealmRow).getX()));
                sb.append(",");
                sb.append(String.valueOf(database.get(currentRealmRow).getY()));
                sb.append(",");
                sb.append(String.valueOf(database.get(currentRealmRow).getZ()));
                sb.append("\n");

                System.out.println(sb);
                bw.write(sb.toString());
            }

            bw.flush();
            bw.close();
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