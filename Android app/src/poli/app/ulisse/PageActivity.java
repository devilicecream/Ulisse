package poli.app.ulisse;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

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
     TextView tvTitle=(TextView)findViewById(R.id.tvTitle);
     TextView tvAddr=(TextView)findViewById(R.id.tvAddr);

     tvTitle.setText(b.getString("title"));
     tvAddr.setText(b.getString("addr"));

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
