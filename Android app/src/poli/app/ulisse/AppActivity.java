package poli.app.ulisse;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;

import com.google.android.gms.maps.MapFragment;


public class AppActivity extends FragmentActivity implements android.widget.PopupMenu.OnMenuItemClickListener {

    LinearLayout near;
    LinearLayout segnalation;
	private Camera mCamera;
	final int TAKE_PICTURE = 0;
	private Uri imageUri;


    //lista contenente i fragment per istanziare il viewPager
    List<Fragment> fragments = new ArrayList<Fragment>();

    private android.support.v4.view.PagerAdapter myAdapter;

    private ViewPager myPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        //crea i fragment e li aggiunge alla lista

        fragments.add(Fragment.instantiate(this, homePage.class.getName()));
        fragments.add(Fragment.instantiate(this, rankActivity.class.getName()));

        // creating adapter and linking to view pager
        this.myAdapter = new PagerAdapter(super.getSupportFragmentManager(), fragments);
        myPager = (ViewPager) super.findViewById(R.id.pager);
        myPager.setAdapter(this.myAdapter);
        myPager.setCurrentItem(0);

        // upper bar button listener, allows direct page access
        Button button = (Button)findViewById(R.id.newBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showPopup(v);   // go to central page
            }
        });
        button = (Button)findViewById(R.id.homeBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myPager.setCurrentItem(0);   // go to left page
            }
        });
        button = (Button)findViewById(R.id.rankBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myPager.setCurrentItem(1);   // go to right page
            }
        });

        Button mapBtn = (Button)findViewById(R.id.mapsBtn);
        mapBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(v.getContext(), MapsActivity.class));//salto del login
            }
        });
        //
    }


    public void onPage1() {

        // page 2 fragment update
        mapsPage f2 = (mapsPage) fragments.get(0);

        // if page 2 view is already created, update
        View v2 = f2.getView();
        if (v2!=null) {

        }

        // page 3 fragment update
        rankingPage f3 = (rankingPage) fragments.get(2);

        // if page 3 view is already created, update
        View v3 = f3.getView();

    }
    
    public void showPopup(View v) 
	{
	    PopupMenu popup = new PopupMenu(this, v);
	    MenuInflater inflater = popup.getMenuInflater();
	    inflater.inflate(R.menu.new_menu, popup.getMenu()); //Men� da fare nel .R
	    popup.setOnMenuItemClickListener(this);
	    popup.show();
	}
	
	@Override
	public boolean onMenuItemClick(MenuItem item) 
	{
	    switch (item.getItemId()) 
	    {
	        case R.id.gallery:

	            return true;
	        case R.id.TakePhoto:
	        	Log.d("camera ok", "takephotocalled");
	        	Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
	        	File photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
	        	intent.putExtra(MediaStore.EXTRA_OUTPUT,
	        	Uri.fromFile(photo));
	        	imageUri = Uri.fromFile(photo);
	        	startActivityForResult(intent, TAKE_PICTURE); //result: intent data
	            return true;
	        default:
	            return false;
	    }
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode)
		{
		case TAKE_PICTURE:
			if (resultCode == Activity.RESULT_OK)
			{
				Uri selectedImage = imageUri;
				getContentResolver().notifyChange(selectedImage, null);
				//ImageView imageView = (ImageView) findViewById(R.id.ImageView);
				ContentResolver cr = getContentResolver();
				Bitmap bitmap;
				try
				{
					bitmap = android.provider.MediaStore.Images.Media
								.getBitmap(cr, selectedImage);
					Communication com = new Communication();
					com.uploadPhoto("1", "Prova", "1", "prova", selectedImage);
					Toast.makeText(this, selectedImage.toString(),
											Toast.LENGTH_LONG).show();
				}

				catch (Exception e)
				{
					Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
					.show();
					Log.e("Camera", e.toString());

				}
			}
		}
	}


}

