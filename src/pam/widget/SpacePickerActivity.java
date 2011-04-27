package pam.widget;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import pam.widget.FilterTextWatcher;

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
		shortcutIntent.putExtra("URL", "https://www.0x20.be/pam/data/");
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

	public final static String[] LIST_OF_SPACES = new String[] {
		"0x20",
		"HSBXL"
	};
}