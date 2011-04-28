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

package pam.widget;

import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class SpacePickerActivity extends ListActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_getspace);
		ArrayAdapter<String> stationAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, LIST_OF_SPACES);
		FilterTextWatcher filterTextWatcher = new FilterTextWatcher(
				stationAdapter);
		EditText filterText=(EditText) findViewById(R.id.search_box);
		filterText.addTextChangedListener(filterTextWatcher);
		setListAdapter(stationAdapter);
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setFastScrollEnabled(true);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		ArrayAdapter<String> adapter = (ArrayAdapter<String>) l.getAdapter();
	
		String name = adapter.getItem(position);

		Intent shortcutIntent = new Intent(this, EntityBoardActivity.class);
		shortcutIntent.putExtra("Name", name);
		shortcutIntent.putExtra("URL", getSpaces().get(name));
		shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		Intent intent = new Intent();
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(this, R.drawable.icon));
		String action = getIntent().getAction();
		if (Intent.ACTION_CREATE_SHORTCUT.equals(action)) {
	        setResult(RESULT_OK, intent);
	        finish(); 

		} else {
			intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
			sendBroadcast(intent);
			setResult(RESULT_OK, null);
			finish();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();	
	}

	// TODO: stupid to have the names twice, but can't get it to work with a hasMap; evil java is evil!
	public final static String[] LIST_OF_SPACES = new String[] {
		"0x20", "HSBXL"
	};
	
	public final static HashMap<String, String> getSpaces() {
		HashMap<String, String> spaces = new HashMap<String, String>();
		
		spaces.put( "0x20", "https://www.0x20.be/pam/data/" );
		spaces.put( "HSBXL", "https://www.hackerspace.be/pam/macs/" );
		
		return spaces;
	}
	
}