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

package pam.widget.classes;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;

public class FilterTextWatcher implements TextWatcher {
	
	private ArrayAdapter<String> adapter;	
	
	public FilterTextWatcher(ArrayAdapter<String> adapter) {		
		this.adapter = adapter;
	}

	public void afterTextChanged(Editable s) {
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	public void onTextChanged(CharSequence s, int start, int before,
			int count) {
		adapter.getFilter().filter(s);
	}

}
