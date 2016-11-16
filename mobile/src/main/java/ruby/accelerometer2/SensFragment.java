package ruby.accelerometer2;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ruby__000 on 16/11/2016.
 */

public class SensFragment extends Fragment {

    private View myView;
    private TextView myTitle;
    private TextView myValues;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.sensor, container, false);

        myTitle = (TextView) myView.findViewById(R.id.text_title);
        ((TextView) myView.findViewById(R.id.text_title)).setText("Accelerometer");
        myValues = (TextView) myView.findViewById(R.id.text_values);

        return myView;
    }



        public void displayValues(float[] event){
        myValues.setText(
                "x = " + Float.toString(event[0]) + "\n" +
                        "y = " + Float.toString(event[1]) + "\n" +
                        "z = " + Float.toString(event[2]) + "\n"
        );

    }
}
