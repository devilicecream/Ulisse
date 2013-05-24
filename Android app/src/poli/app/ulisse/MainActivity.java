package poli.app.ulisse;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.android.gms.common.*;
import com.google.android.gms.common.GooglePlayServicesClient.*;
import com.google.android.gms.plus.PlusClient;

public class MainActivity extends Activity implements OnClickListener,
        ConnectionCallbacks, OnConnectionFailedListener {
    private static final String TAG = "ExampleActivity";
    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

    private ProgressDialog mConnectionProgressDialog;
    private PlusClient mPlusClient;
    private ConnectionResult mConnectionResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        mPlusClient = new PlusClient.Builder(this, this, this)
                .setVisibleActivities("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
                .build();
        // Progress bar to be displayed if the connection failure is not resolved.
        mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage("Signing in...");
        // if(mPlusClient.isConnected())
        //    Toast.makeText(this, " is connected.", Toast.LENGTH_LONG).show();
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
        String accountName = mPlusClient.getAccountName();
        Toast.makeText(this, accountName + " is connected.", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, AppActivity.class));
	}
	
    @Override
    public void onDisconnected() 
    {
        Log.d(TAG, "disconnected");
    }
    
    @Override
    public void onConnectionFailed(ConnectionResult result) 
    {
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
}
