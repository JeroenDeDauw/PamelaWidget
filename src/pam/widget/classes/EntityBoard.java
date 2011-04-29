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

import java.util.ArrayList;
import java.util.Date;

public class EntityBoard {
	private Date currentDate;
	private String space;
	private ArrayList<Entity> entities;


	public EntityBoard(Date currentDate, String space, ArrayList<Entity> entities) {
		this.currentDate = currentDate;
		this.space = space;
		this.entities = entities;

	}

	public Date getCurrentTime() {
		return this.currentDate;
	}

	public String getSpace() {
		return this.space;
	}
	
	public ArrayList<Entity> getEntities() {
		return this.entities;
	}

}

