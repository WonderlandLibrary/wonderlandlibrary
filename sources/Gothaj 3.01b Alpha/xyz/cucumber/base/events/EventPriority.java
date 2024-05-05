package xyz.cucumber.base.events;

public enum EventPriority {
	LOWEST(0),
	LOW(1),
	MEDIUM(2),
	HIGH(3),
	HIGHEST(4);
	
	private int value;

	EventPriority(int  value){
	    this.value = value;
	}

	public int getID(){
		return value;
	}

}
