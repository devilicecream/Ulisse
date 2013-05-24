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
     b = getIntent().getExtras();
     this.createUI();
    }

    public void createUI() 
    {
    	((TextView)findViewById(R.id.tvTitle)).setText(b.getString("name"));
    	((TextView)findViewById(R.id.tvAddr)).setText(b.getString("address"));
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
