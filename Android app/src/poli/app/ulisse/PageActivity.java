package poli.app.ulisse;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class PageActivity extends Activity implements OnClickListener
{
	Bundle b;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
	 super.onCreate(savedInstanceState);
     setContentView(R.layout.page_activity);
     findViewById(R.id.btStreetview).setOnClickListener(this);
     String place_id = getIntent().getExtras().getString("id");
     Communication com=new Communication();
     
     b=com.getPlace(place_id);
     try {
		this.createUI();
     } catch (JSONException e) {
		e.printStackTrace();
     }
 
    }

    public void createUI() throws JSONException {
    	((TextView)findViewById(R.id.tvTitle)).setText(b.getString("name"));
    	((TextView)findViewById(R.id.tvAddr)).setText(b.getString("address"));
    	ArrayList<Object> array = (ArrayList<Object>)b.get("documents");
		((TextView)findViewById(R.id.TextView0)).setText(((JSONObject)array.get(0)).getString("name"));
		((TextView)findViewById(R.id.TextView1)).setText(((JSONObject)array.get(1)).getString("name"));
		((TextView)findViewById(R.id.TextView2)).setText(((JSONObject)array.get(2)).getString("name"));
		((TextView)findViewById(R.id.TextView3)).setText(((JSONObject)array.get(3)).getString("name"));
		((TextView)findViewById(R.id.TextView4)).setText(((JSONObject)array.get(4)).getString("name"));
		((TextView)findViewById(R.id.TextView5)).setText(((JSONObject)array.get(5)).getString("name"));

    }
    
	@Override
	public void onClick(View arg0) 
	{
        Uri streetViewUri = Uri.parse(
                "google.streetview:cbll="+b.getDouble("lat")+","+b.getDouble("lon")+"&cbp=1,90,,0,1.0&mz=20");
        Intent streetViewIntent = new Intent(Intent.ACTION_VIEW, streetViewUri);
        startActivity(streetViewIntent);
		
	}
}
