/*
 * #%L
 * SlidingMenuDemo
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2012 Paul Grime
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package poli.app.ulisse;

import poli.app.ulisse.MyHorizontalScrollView.SizeCallback;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * This demo uses a custom HorizontalScrollView that ignores touch events, and therefore does NOT allow manual scrolling.
 * 
 * The only scrolling allowed is scrolling in code triggered by the menu button.
 * 
 * When the button is pressed, both the menu and the app will scroll. So the menu isn't revealed from beneath the app, it
 * adjoins the app and moves with the app.
 */
public class MapsActivity extends Activity implements OnMarkerClickListener,OnInfoWindowClickListener,android.location.LocationListener
{
    MyHorizontalScrollView scrollView;
    View menu;
    View app;
    ImageView btnSlide;
    boolean menuOut = false;
    Handler handler = new Handler();
    int btnWidth;
	private	GoogleMap mMap;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(this);
        scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
        setContentView(scrollView);
        
        int scrollToViewIdx = 1;

        menu = inflater.inflate(R.layout.horz_scroll_menu, null);
        app = inflater.inflate(R.layout.activity_map, null);
        
        final View[] children = new View[] { menu, app };

        ListView listView = (ListView) menu.findViewById(R.id.list);
        ViewUtils.initListView(this, listView, "Categoria ", 30, android.R.layout.simple_list_item_1);

        btnSlide = (ImageView) app.findViewById(R.id.BtnSlide);
        btnSlide.setOnClickListener(new ClickListenerForScrolling(scrollView, menu));

        scrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();	
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        Communication com=new Communication();
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        CameraUpdate camupd=CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
        mMap.animateCamera(camupd);
        Toast.makeText(this, location.getLatitude()+"x"+location.getLongitude(), Toast.LENGTH_LONG).show();
        Bundle[] b=com.getPlaces(location.getLatitude(), location.getLongitude());
        
        addMarkers(b);
    }
    
    void addMarkers(Bundle[] b)
    {
    	for(int i=0;i<b.length;i++)
    	{
		    mMap.addMarker(new MarkerOptions()
		    .position(new LatLng(b[i].getDouble("pos_lat"),b[i].getDouble("pos_lon")))
		    .title(b[i].getString("name"))
		    .snippet(b[i].getString("address"))
		/*.icon(BitmapDescriptorFactory
		    .fromResource(R.drawable.mole_antonelliana))*/);
    	}
	}
    
    
    //CLASSE PER LA GESTIONE DELLO SCROLLING
    static class ClickListenerForScrolling implements OnClickListener {
        HorizontalScrollView scrollView;
        View menu;
        /**
         * Menu must NOT be out/shown to start with.
         */
        boolean menuOut = false;

        public ClickListenerForScrolling(HorizontalScrollView scrollView, View menu) {
            super();
            this.scrollView = scrollView;
            this.menu = menu;
        }

        @Override
        public void onClick(View v) 
        {
        	if(v.getId()==R.id.BtnSlide)
        	{
            int menuWidth = menu.getMeasuredWidth();
            // Ensure menu is visible
            menu.setVisibility(View.VISIBLE);

            if (!menuOut) {
                // Scroll to 0 to reveal menu
                int left = 0;
                scrollView.smoothScrollTo(left, 0);
            } else {
                // Scroll to menuWidth so menu isn't on screen.
                int left = menuWidth;
                scrollView.smoothScrollTo(left, 0);
            }
            menuOut = !menuOut;
        	}
        }
    }


    
    static class SizeCallbackForMenu implements SizeCallback {
        int btnWidth;
        View btnSlide;

        public SizeCallbackForMenu(View btnSlide) {
            super();
            this.btnSlide = btnSlide;
        }

        @Override
        public void onGlobalLayout() 
        {
            btnWidth = btnSlide.getMeasuredWidth();
        }

        @Override
        public void getViewSize(int idx, int w, int h, int[] dims) 
        {
            dims[0] = w;
            dims[1] = h;
            final int menuIdx = 0;
            if (idx == menuIdx) {
                dims[0] = w - btnWidth;
            }
        }
    }



	@Override
	public boolean onMarkerClick(Marker marker) 
	{
        Toast.makeText(this, marker.getTitle() , Toast.LENGTH_LONG).show();       
		return false;
	}

	@Override
	public void onInfoWindowClick(Marker marker) 
	{
		Intent intent = new Intent(this,PageActivity.class);
		Bundle b = new Bundle();
		b.putString("title",marker.getTitle());
		b.putString("addr",marker.getSnippet());
		b.putDouble("lat",marker.getPosition().latitude);
		b.putDouble("lat",marker.getPosition().longitude);

		intent.putExtras(b);
        startActivity(intent);			
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_main, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.normal:
	            mMap.setMapType(com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL);
	            return true;
	        case R.id.satellite:
	            mMap.setMapType(com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE);
	            return true;
	        case R.id.terreno:
	            mMap.setMapType(com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public void onLocationChanged(Location location) 
	{
        Toast.makeText(this,"test", Toast.LENGTH_LONG).show();       

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
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) 
	{
		// TODO Auto-generated method stub
		
	}

}
