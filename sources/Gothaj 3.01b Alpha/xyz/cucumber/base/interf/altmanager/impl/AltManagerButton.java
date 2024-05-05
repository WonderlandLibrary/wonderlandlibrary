package xyz.cucumber.base.interf.altmanager.impl;

import xyz.cucumber.base.utils.position.PositionUtils;

public abstract class AltManagerButton {
	
	PositionUtils position;
	int id;
	
	public abstract void draw(int mouseX, int mouseY);

	public PositionUtils getPosition() {
		return position;
	}

	public void setPosition(PositionUtils position) {
		this.position = position;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	

}
