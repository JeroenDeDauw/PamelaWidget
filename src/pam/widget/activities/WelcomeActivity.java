package pam.widget.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WelcomeActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_activity);
	}

	public void PamelaWidget(View view) {
		Intent myIntent = new Intent(WelcomeActivity.this, SpacePickerActivity.class);
		WelcomeActivity.this.startActivityForResult(myIntent,0);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			finish();
		}
	}

}