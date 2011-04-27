package pam.widget;

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

