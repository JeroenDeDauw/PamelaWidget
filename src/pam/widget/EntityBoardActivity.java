package pam.widget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import pam.widget.EntityBoard;
import pam.widget.Entity;
import pam.widget.EntityAdapter;

public class EntityBoardActivity extends ListActivity {
	/** Called when the activity is first created. */
	TextView currentTimeTv ;
	TextView titleTv;
	RelativeLayout emptyRl;
	SimpleDateFormat hourFormatter = new SimpleDateFormat("HH:mm");
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		currentTimeTv = (TextView) findViewById(R.id.currentTime);
		titleTv = (TextView) findViewById(R.id.title);
		emptyRl = (RelativeLayout) findViewById(R.id.empty);

		displayEntityBoard();
	}
	
	private class DownloadEntityBoardTask extends AsyncTask<String, Void, EntityBoard> {
		@Override
		protected EntityBoard doInBackground(String... urls) {
			final String mIntentString = getStationNameFromIntent();
			runOnUiThread(new Runnable() {
			    public void run() {
			    	titleTv.setText(mIntentString);
			    }
			});
			
			return getEntityBoardFromApi(mIntentString);
		}

		@Override
		protected void onPostExecute(EntityBoard result) {
			Date currentDate = result.getCurrentTime();
			String space = result.getSpace();
			
			if (currentDate != null) {
				currentTimeTv.setText(hourFormatter.format(currentDate));
			}
				
			if (space != null) {
				titleTv.setText(space);
			}
				
			setListAdapter(new EntityAdapter(getBaseContext(),
					R.layout.liveboardrow, result.getEntities()));
			emptyRl.setVisibility(View.GONE);
		}
	}

	public void displayEntityBoard() {
		DownloadEntityBoardTask task = new DownloadEntityBoardTask();
		task.execute();

	}

	private String getStationNameFromIntent (){
			Bundle extras = getIntent().getExtras();
			return extras != null ? extras.getString("Name")
					: "nothing passed in";		
		
	}
	
	private EntityBoard getEntityBoardFromApi (String mIntentString){
		HttpClient httpclient = new DefaultHttpClient();
		
		String myVersion = "0.0";
		PackageManager manager = getBaseContext().getPackageManager();
		try {
			myVersion = (manager.getPackageInfo(getBaseContext().getPackageName(), 0).versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		httpclient.getParams().setParameter("http.useragent",  "PamelaWidget "+myVersion+ " for Android - "
				+ System.getProperty("http.agent"));
		String url = "https://0x20.be/pam/data/?lang=" + getString(R.string.lang);
		// Prepare a request object
		HttpGet httpget = new HttpGet(url.replace(" ", "%20"));
		Log.i("", url);

		// Execute the request
		HttpResponse response;

		JSONObject json = new JSONObject();
		// SimpleDateFormat dateFormatter = new
		// SimpleDateFormat("dd/MM/yy HH:mm");
		
		Date currentDate = null;
		String space = null;
		ArrayList<Entity> entities = new ArrayList<Entity>();

		try {
			response = httpclient.execute(httpget);

			HttpEntity entity = response.getEntity();

			if (entity != null) {

				// A Simple JSON Response Read
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);

				json = new JSONObject(result);

				instream.close();
			}

			double attributeTimestamp = json.getDouble("timestamp") * 1000;
			currentDate = new Date((long) attributeTimestamp);
/*
			space = "0x20"; //json.getString("space");
			JSONArray entityList = json.getJSONArray( "" );
			
			for (int i = 0; i < Integer.valueOf(attributeNumber); i++) {

				long attributeTime = departureArray.getJSONObject(i).getLong( "time") * 1000;
				Date time = new Date(attributeTime);

				entities.add(new Entity(
						
				));
			}
*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		entities.add(new Entity(
			"foo",
			1,
			1
		));
		
		entities.add(new Entity(
				"bar",
				1,
				1
			));		
		
		return new EntityBoard(currentDate, space, entities);
	
}

	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}