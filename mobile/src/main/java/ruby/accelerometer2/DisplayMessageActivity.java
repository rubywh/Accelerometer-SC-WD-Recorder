package ruby.accelerometer2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;
/* Not needed for now but here as a reminder of how to display something based on a button click!*/
public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        //get the intent that started the activity
        Intent intent = getIntent();
        //retrieve data
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        //create TextView and set size/layout
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);
        //Add view to layout
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_message);
        layout.addView(textView);
    }
}
