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
import java.util.Map;


public class Entity {

	public static enum Status {
		ACTIVE,
		AWAY
	};
	
	public static enum Type {
		MEMBER,
		PERSON,
		DEVICE,
		UNKNOWN
	};
	
	protected String name;
	protected Type type;
	protected Status status;
	protected String customType = "";
	
	public Entity(String name) {
		this(name, Type.UNKNOWN, Status.ACTIVE);
	}
	
	public Entity(String name, Type type, Status status) {
		this.name = name;
		this.type = type;
		this.status = status;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public Status getStatus() {
		return this.status;
	}
	
	public void setCustomType( String type ) {
		this.customType = type;
	}
	
	public String getTypeName() {
		if ( this.customType != "" ) {
			return this.customType;
		}
		
		Map<Type, String> names = new HashMap<Type, String>(){
			{
				put(Type.MEMBER, "Member");
				put(Type.PERSON, "Person");
				put(Type.DEVICE, "Device");
				put(Type.UNKNOWN, "Unknown");
			}
		};
		
		return names.get( this.getType() );
	}
	
}
