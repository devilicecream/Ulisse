package poli.app.ulisse;

import java.io.File;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;


public class NewPlace extends Activity implements android.widget.PopupMenu.OnMenuItemClickListener
{
	private Camera mCamera;
	final int TAKE_PICTURE = 0;
	private Uri imageUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_place);
	}

/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_menu, menu);
		return true;
	}*/
	
	public void showPopup(View v) 
	{
	    PopupMenu popup = new PopupMenu(this, v);
	    MenuInflater inflater = popup.getMenuInflater();
	    inflater.inflate(R.menu.new_menu, popup.getMenu()); //Menù da fare nel .R
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
				//		imageView.setImageBitmap(bitmap);
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
