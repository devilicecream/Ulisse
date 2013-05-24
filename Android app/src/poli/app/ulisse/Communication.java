package poli.app.ulisse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

public class Communication 
{
	
	public Communication()
	{
		StrictMode.ThreadPolicy policy = ( new StrictMode.ThreadPolicy.Builder() ).permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}
	
	public void login(String id)
	{
		String[] values={id};
		String[] params={"gp_id"};
		sendData("login",params,values);
	}
	

	
	public Bundle[] getPlaces(double lat,double lon)
	{
		Bundle[] b=null;
		String[] values={String.valueOf(lat),String.valueOf(lon)};
		String[] params={"lat","lon"};
		JSONArray jsa=sendData("get_places",params,values);
		b=new Bundle[jsa.length()];
		for(int i=0;i<jsa.length();i++)
		{
			try 
			{	
				JSONObject job=jsa.getJSONObject(i);
				b[i]=new Bundle();
				b[i].putInt("uid",job.getInt("uid"));
				b[i].putString("name",job.getString("name"));
				b[i].putString("address",job.getString("address"));
				b[i].putDouble("pos_lat",job.getDouble("pos_lat"));
				b[i].putDouble("pos_lon",job.getDouble("pos_lon"));
				b[i].putInt("up",job.getInt("up"));
				b[i].putInt("down",job.getInt("down"));
				b[i].putInt("user_id",job.getInt("user_id"));
				b[i].putInt("category_id",job.getInt("category_id"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;
	}
	
	public Bundle getPlace(int id)
	{
		Bundle b=new Bundle();
		String[] values={String.valueOf(id)};
		String[] params={"id"};
		JSONArray jsa=sendData("get_place",params,values);
		return b;
	}
	
	
	private JSONArray sendData(String page, String[] params, String[] values) 
	{	
	    HttpClient httpclient = new DefaultHttpClient();
	    URI server = null;
	    JSONArray jsa = null;
		try {
			server = new URI("http", null, "192.168.88.78", 5000, "/"+page,null,null);

		} catch (URISyntaxException e1) 
		{
			e1.printStackTrace();
		}
	    HttpPost httppost = new HttpPost(server);
	    if(values!=null)
	    {
	    	Log.d("Comunication","adding paramenters");
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        for(int i=0;i<values.length;i++)
	        	nameValuePairs.add(new BasicNameValuePair(params[i],values[i]));
	        try {
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			} catch (UnsupportedEncodingException e) {
			
				e.printStackTrace();
			}
	    }
	    try { 
	        HttpResponse response = httpclient.execute(httppost);
	        jsa=decodeJSON(response);
	        Log.d("response",response.toString());
	    } catch (ClientProtocolException e) 
	    {
	        Log.d("Exception","Client Protocol Exception");
	    } catch (IOException e) 
	    {
	        Log.d("Exception","IO Exception");
	    }
	    return jsa;
	}
	
	private JSONArray decodeJSON(HttpResponse response)
	{
		Log.d("response",response.toString());
	    Reader in = null;
	    JSONArray finalResult=null;
		try {
			in = new BufferedReader(
			    new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    StringBuilder builder= new StringBuilder();
	    char[] buf = new char[1000];
	    int l = 0;
	    while (l >= 0) {
	        builder.append(buf, 0, l);
	        try {l = in.read(buf);} catch (IOException e) {e.printStackTrace();}
	    }
	    JSONTokener tokener = new JSONTokener( builder.toString() );
		try {finalResult = new JSONArray( tokener );} catch (JSONException e){e.printStackTrace();}
	    return finalResult;
	}
}