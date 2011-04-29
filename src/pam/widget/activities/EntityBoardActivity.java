/*
	Copyright 2011 by Jeroen De Dauw

    This file is part of Pamela widget for Android.

    Pamela for Android is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    It is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this code.  If not, see <http://www.gnu.org/licenses/>.
*/

package pam.widget.activities;

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

import pam.widget.classes.Entity;
import pam.widget.classes.EntityAdapter;
import pam.widget.classes.EntityBoard;
import pam.widget.classes.Entity.Status;
import pam.widget.classes.Entity.Type;
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

public class EntityBoardActivity extends ListActivity {
	/** Called when the activity is first created. */
	TextView statusTv ;
	TextView titleTv;
	RelativeLayout emptyRl;
	SimpleDateFormat hourFormatter = new SimpleDateFormat("HH:mm");
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		statusTv = (TextView) findViewById(R.id.status);
		titleTv = (TextView) findViewById(R.id.title);
		emptyRl = (RelativeLayout) findViewById(R.id.empty);

		displayEntityBoard();
	}
	
	private class DownloadEntityBoardTask extends AsyncTask<String, Void, EntityBoard> {
		@Override
		protected EntityBoard doInBackground(String... urls) {
			final Bundle extras = getIntent().getExtras();
			
			runOnUiThread(new Runnable() {
			    public void run() {
			    	statusTv.setText(R.string.loading);
			    	titleTv.setText(extras.getString("Name"));
			    }
			});
			
			return getEntityBoardFromApi(extras);
		}

		@Override
		protected void onPostExecute(EntityBoard result) {
			Date currentDate = result.getCurrentTime();
			String space = result.getSpace();
			
			if (currentDate != null) {
				statusTv.setText(hourFormatter.format(currentDate));
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
	
	private EntityBoard getEntityBoardFromApi (Bundle extras){
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
		
		String url = extras.getString("URL") + "?lang=" + getString(R.string.lang);
		// Prepare a request object
		HttpGet httpget = new HttpGet(url.replace(" ", "%20"));
		Log.i("", url);

		// Execute the request
		HttpResponse response;

		JSONArray json = new JSONArray();
		
		Date currentDate = new Date();
		String space = extras.getString("Name");
		ArrayList<Entity> entities = new ArrayList<Entity>();

		try {
			response = httpclient.execute(httpget);

			HttpEntity httpEntity = response.getEntity();

			if (httpEntity != null) {

				// A Simple JSON Response Read
				InputStream instream = httpEntity.getContent();
				String result = convertStreamToString(instream);

				json = new JSONArray(result);

				instream.close();
			}
			
			for (int i = 0; i < json.length(); ++i) {
				String rawName = json.getString(i);
				String displayName = rawName;
				Type type = Type.UNKNOWN;
				String customType = "";
				
				if ( rawName.split( ":" ).length != 6 )  {
					
					
					if ( rawName.indexOf("(") != -1 && rawName.indexOf(")") > rawName.indexOf("(") ) {
						type =  Type.DEVICE;
						customType = rawName.substring( rawName.indexOf("(") + 1, rawName.indexOf(")") );
						customType = customType.substring( 0, 1 ).toUpperCase() + customType.substring( 1 );
						displayName = rawName.substring( 0, rawName.indexOf("(") );
					}
					else {
						type =  Type.PERSON;
					}
				}
				
				Entity entity = new Entity( displayName, type, Status.ACTIVE );
				
				if ( customType != "" ) {
					entity.setCustomType(customType);
				}
				
				entities.add(entity);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<Entity> orderedEntities = new ArrayList<Entity>();
		
		int peopleAmount = this.addEntitiesOfType(entities, orderedEntities, Type.PERSON);
		this.addEntitiesOfType(entities, orderedEntities, Type.DEVICE);
		int unknownAmount = this.addEntitiesOfType(entities, orderedEntities, Type.UNKNOWN);
		
		Boolean spaceIsOpen = peopleAmount > 0 || unknownAmount > 0;
		space = space + " (" + getString( spaceIsOpen ? R.string.open : R.string.closed ) + ")"; 
		
		return new EntityBoard(currentDate, space, orderedEntities);
	}
	
	protected int addEntitiesOfType( ArrayList<Entity> source, ArrayList<Entity> destination, Type type ) {
		int amount = 0;
		
		for (int i = 0; i < source.size(); ++i) {
			if ( source.get(i).getType() == type ) {
				destination.add(source.get(i));
				amount++;
			}
		}
		
		return amount;
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