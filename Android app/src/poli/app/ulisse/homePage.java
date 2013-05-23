package poli.app.ulisse;
import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class homePage extends Fragment{
    LinearLayout near;
    LinearLayout segnalation;
    ArrayList<ImageView> nearImages = new ArrayList<ImageView>();
    ArrayList<TextView> nearDescriptions = new ArrayList<TextView>();

    ArrayList<LinearLayout> nearContainer = new ArrayList<LinearLayout>();
    ArrayList<LinearLayout> segnalatedContainer = new ArrayList<LinearLayout>();

    ArrayList<ImageView> segnalatedImages = new ArrayList<ImageView>();
    ArrayList<TextView> segnalatedDescriptions = new ArrayList<TextView>();


    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        // fragment not when container null
        if (container == null) {

            return null;
        }
        // inflate view from layout
        View view = (LinearLayout)inflater.inflate(R.layout.center,container,false);
        near = (LinearLayout) view.findViewById(R.id.near);
        segnalation = (LinearLayout) view.findViewById(R.id.segnal);
        createImageArray(view);
        return view;
    }


    private void createImageArray(View v){
        for(int x = 0; x < 10; x++){
            nearImages.add(new ImageView(v.getContext()));
            defineImage(nearImages.get(x));

            nearDescriptions.add(new TextView(v.getContext()));
            defineDescription(nearDescriptions.get(x));

            nearContainer.add(new LinearLayout(v.getContext()));
            defineContainer(nearContainer.get(x));

            nearContainer.get(x).addView(nearImages.get(x));
            nearContainer.get(x).addView(nearDescriptions.get(x));

            near.addView(nearContainer.get(x));

            segnalatedImages.add(new ImageView(v.getContext()));
            defineImage(segnalatedImages.get(x));

            segnalatedDescriptions.add(new TextView(v.getContext()));
            defineDescription(segnalatedDescriptions.get(x));

            segnalatedContainer.add(new LinearLayout(v.getContext()));
            defineContainer(segnalatedContainer.get(x));

            segnalatedContainer.get(x).addView(segnalatedImages.get(x));
            segnalatedContainer.get(x).addView(segnalatedDescriptions.get(x));

            segnalation.addView(segnalatedContainer.get(x));
        }

    }

    private void defineContainer(LinearLayout l) {
        l.setOrientation(LinearLayout.VERTICAL);
        l.setBackgroundColor(Color.argb(17, 255, 255, 255));
        l.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 250));
    }

    private void defineDescription(TextView t) {
        t.setText("Example");
        t.setTextColor(Color.RED);
        t.setGravity(Gravity.CENTER);
    }

    void defineImage(ImageView i){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
        params.setMargins(5, 5, 5, 5);
        i.setLayoutParams(params);
        i.setBackgroundColor(Color.BLUE);
    }


    // activity listener interface
    private OnPageListener pageListener;
    public interface OnPageListener {
        public void onPage1();
    }

    // onAttach : set activity listener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // if implemented by activity, set listener
        if(activity instanceof OnPageListener) {
            pageListener = (OnPageListener) activity;
        }
        // else create local listener (code never executed in this example)
        else pageListener = new OnPageListener() {
            @Override
            public void onPage1() {
                Log.d("PAG1", "Button event");
            }
        };
    }
}

