package pam.widget;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import pam.widget.R;

public class EntityAdapter extends ArrayAdapter<Entity> {
	protected ArrayList<Entity> items;

	public EntityAdapter(Context context, int textViewResourceId, ArrayList<Entity> items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}
	 
	public boolean isEnabled(int position) {
        return false;
	}

	private int[] colors = new int[] { 0x66302D3B, 0x6653769B };

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) super.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.liveboardrow, null);
		}
		Entity entity = items.get(position);

		if (entity != null) {
			TextView nameTv = (TextView) v.findViewById(R.id.name);

			nameTv.setText(entity.getName());

			int colorPos = position % colors.length;
			v.setBackgroundColor(colors[colorPos]);

		}
		return v;
	}

	public static String last(String[] array) {
		Log.i("", "size = " + array.length);
		if (array.length > 0)
			return array[array.length - 1];
		else
			return null;
	}



}
