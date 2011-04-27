package pam.widget;

public class Entity {

	protected String name;
	protected int type;
	protected int status;
	
	public Entity(String name, int type, int status) {
		this.name = name;
		this.type = type;
		this.status = status;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getType() {
		return this.type;
	}
	
	public int getStatus() {
		return this.status;
	}	
	
}
