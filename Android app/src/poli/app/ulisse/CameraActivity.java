package poli.app.ulisse;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
//import app.prova.R;

public class CameraActivity extends Activity implements SurfaceHolder.Callback

{       
/* VARIABILI PRIVATE */
private SurfaceView mSurfaceView;
private SurfaceHolder mSurfaceHolder;
private Camera mCamera;
private boolean mPreviewRunning;
       
/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) 
{
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT); //aggiungo il traslucido
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //no barra titolo
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   //full screen
        setContentView(R.layout.activity_main);
        ImageButton buttonPicture = (ImageButton) findViewById(R.id.camera_surface_button);
                buttonPicture.setOnClickListener(new OnClickListener() 
                {
                        public void onClick(View v) 
                        {
                                mCamera.takePicture(null, null, jpegCallback);
                        }
                } );
                
                
        mSurfaceView = (SurfaceView)findViewById(R.id.camera_surface);
        mSurfaceHolder = mSurfaceView.getHolder();      //recupero l'holder della surfaceview
        mSurfaceHolder.addCallback(this);       //faccio la bind alla nostra activity
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);        //tipo di surface, suggerito nei tutorial ufficiali
}
   
   	//android:src="@drawable/ic_launcher" in activiti main
	// in manifest sotto 1.0 <application android:icon="@drawable-hdpi/ic_Launcher" android:label="@string/app_name">
	//prima di 7 </application>
   
    PictureCallback jpegCallback = new PictureCallback() 
    {
                public void onPictureTaken(byte[] _data, Camera _camera)
                {
                        //riparte la preview della camera
                        mCamera.startPreview();       
                }
    };

        @Override
        public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) 
        {
                if (mPreviewRunning)
                        mCamera.stopPreview();
               
                //setto le preferenze
                Camera.Parameters p = mCamera.getParameters();  //prendo le preferenze della camera
                p.setPreviewSize(arg2, arg3);
                ArrayList<Size> list = (ArrayList<Size>) p.getSupportedPictureSizes();  //recuepro le risoluzioni supportate dalla camera
        int picture_width = list.get(list.size()-1).width;
        int picture_height = list.get(list.size()-1).height;
        p.setPictureSize(picture_width, picture_height);        //setto la camera alla risoluzione più bassa
        p.setJpegQuality(80);   // qualità compressione JPEG
       
        // salvo le pref
        mCamera.setParameters(p);
        try {
                //lancio la preview
                        mCamera.setPreviewDisplay(arg0);       
                        mCamera.startPreview();
                        mPreviewRunning = true;
            } 
        	catch (IOException e) 
        	{
                        //gestione errore
        	}
                
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) 
        {
                mCamera = Camera.open();       
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) 
        {
        	mCamera.stopPreview();
        	mPreviewRunning = false;
        	mCamera.release();   
        }
}