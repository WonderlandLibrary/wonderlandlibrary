package xyz.cucumber.base.utils.render;

import xyz.cucumber.base.utils.position.PositionUtils;

public class ButtonUtils {
	
	private String name, animation;
	
	private PositionUtils position;
	
	private long time,animationDelay;
	
	private boolean hover;
	
	public ButtonUtils(String name, PositionUtils position, String animation, long animationDelay) {
		this.name = name;
		this.position = position;
		this.animation = animation;
		this.animationDelay = animationDelay;
	}
	
	public void draw() {
		
	}

}
