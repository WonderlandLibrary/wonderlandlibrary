package xyz.cucumber.base.interf.config.ext;

import xyz.cucumber.base.utils.position.PositionUtils;

public abstract class ConfigButton {
	
	PositionUtils position;
	int id;
	
	public abstract void draw(int mouseX, int mouseY);
	public abstract void onClick(int mouseX, int mouseY, int b);
	
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
