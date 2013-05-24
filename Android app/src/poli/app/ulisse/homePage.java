package poli.app.ulisse;
import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import com.google.android.gms.maps.model.LatLng;


public class homePage extends Fragment implements android.location.LocationListener{
    LinearLayout near;
    LinearLayout segnalation;
    ArrayList<ImageView> nearImages = new ArrayList<ImageView>();
    ArrayList<TextView> nearDescriptions = new ArrayList<TextView>();

    ArrayList<LinearLayout> nearContainer = new ArrayList<LinearLayout>();
    ArrayList<LinearLayout> segnalatedContainer = new ArrayList<LinearLayout>();

    ArrayList<ImageView> segnalatedImages = new ArrayList<ImageView>();
    ArrayList<TextView> segnalatedDescriptions = new ArrayList<TextView>();
    
    class ButtonListener implements OnClickListener {
		public String uid;
		public String name;
		public String address;

		
		@Override
		public void onClick(View v) {
			// Porcata assurda.
			createPageActivity(this.uid, this.name, this.address);
		}
	};

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        // fragment not when container null
        if (container == null) {

            return null;
        }
        // inflate view from layout
        View view = (LinearLayout)inflater.inflate(R.layout.center,container,false);
        near = (LinearLayout) view.findViewById(R.id.near);
        createImageArray(view);
        
        Communication com = new Communication();
        Bundle[] bundles = com.getPlaces(45.06952, 7.67862);
        int i = 0;
        for(Bundle bundle : bundles) {
        	Button bt = new Button(getActivity());
        	bt.setText(bundle.getString("name"));
        	ButtonListener listener = new ButtonListener();
			listener.uid = String.format("%d",bundle.getInt("uid"));
			listener.name = bundle.getString("name");
			listener.address = bundle.getString("address");
			bt.setOnClickListener(listener);
			
			
			
			LinearLayout l = new LinearLayout(getActivity());
			
		    l.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		
		    Random rand = new Random();
		    int x = rand.nextInt(4);
		    
		    ImageView img = new ImageView(getActivity());
		    switch(x) {
		    case 0 :
		    	img.setImageResource(R.drawable.colosseo);
		    	break;
		    case 1 :
		    	img.setImageResource(R.drawable.catacombe_di_siracusa);
		    	break;
		    case 2 :
		    	img.setImageResource(R.drawable.mole_antonelliana);
		    	break;
		    case 3 :
		    	img.setImageResource(R.drawable.torre_di_pisa);
		    	break;
		    case 4 :
		    	img.setImageResource(R.drawable.duomo_di_milano);
		    	break;
		    
		    }
		    
		    
		
		    img.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		    
		    l.addView(img);
		    LayoutParams btnParams =new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		    bt.setBackgroundColor(0);
		    l.addView(bt);
		    l.setPadding(0, 5, 0, 5);
		    near.addView(l);
		    
			
            i++;
			}
        return view;
    }
    
    

    void createPageActivity(String id, String name, String address) {
		Intent intent = new Intent(getActivity(), PageActivity.class);
		
		Bundle bundle = new Bundle();
		bundle.putString("id", id);
		bundle.putString("name", name);
		bundle.putString("address", address);

		intent.putExtras(bundle);
		startActivity(intent);
    }

    private void createImageArray(View v){
        for(int x = 0; x < 10; x++){
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
        i.setBackgroundColor(Color.WHITE);
    }


    // activity listener interface
    private OnPageListener pageListener;
    public interface OnPageListener {
        public void onPage1();
    }

/*    // onAttach : set activity listener
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
    }*/


	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}

