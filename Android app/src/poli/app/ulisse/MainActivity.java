package poli.app.ulisse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.*;
import com.google.android.gms.common.GooglePlayServicesClient.*;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusClient.OnPersonLoadedListener;
import com.google.android.gms.plus.model.people.Person;

public class MainActivity extends Activity implements OnClickListener,
        ConnectionCallbacks, OnConnectionFailedListener, OnPersonLoadedListener {
    private static final String TAG = "ExampleActivity";
    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
    private ProgressDialog mConnectionProgressDialog;
    private PlusClient mPlusClient;
    private ConnectionResult mConnectionResult;

    Communication com;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        mPlusClient = new PlusClient.Builder(this, this, this).setScopes(Scopes.PLUS_LOGIN)
                .setVisibleActivities("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
                .build();

        com=new Communication();
        mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage("Signing in...");
//		startActivity(new Intent(this,AppActivity.class));	    

    }
    
    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onStop() 
    {
        super.onStop();
        mPlusClient.disconnect();
    }
    
    
	@Override
	public void onConnected(Bundle connectionHint) 
	{
		mPlusClient.loadPerson(this,"me");
		mConnectionProgressDialog.dismiss();    
	}
	
    @Override
    public void onDisconnected() 
    {
        Log.d(TAG, "disconnected");
    }
    
    @Override
    public void onConnectionFailed(ConnectionResult result) 
    {
		Toast.makeText(this,"Error: "+String.valueOf(result.getErrorCode()), Toast.LENGTH_LONG).show(); 

        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
            } catch (SendIntentException e) {
                mPlusClient.connect();
            }
        }
        // Save the result and resolve the connection failure upon a user click.
        mConnectionResult = result;
    }
    
	@Override
	public void onClick(View view) 
	{
		if (view.getId() == R.id.sign_in_button && !mPlusClient.isConnected()) 
		{
			 mConnectionProgressDialog.show();
	        mPlusClient.connect();
	    }
	}

	
	@Override
	public void onPersonLoaded(ConnectionResult status, Person person) 
	{
		if(person==null)
			Toast.makeText(this,"Error: "+String.valueOf(status.getErrorCode()), Toast.LENGTH_LONG).show(); 
		else
		{
			Toast.makeText(this,"ID: "+String.valueOf(person.getId()), Toast.LENGTH_LONG).show(); 
			//m.login(person.getId());
		//om.getPlaces(100.0,100.0);
			startActivity(new Intent(this,AppActivity.class));	    
		}
	}
}
