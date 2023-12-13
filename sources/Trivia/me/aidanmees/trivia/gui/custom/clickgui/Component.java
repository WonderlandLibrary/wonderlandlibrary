package me.aidanmees.trivia.gui.custom.clickgui;

import java.awt.Font;

import org.darkstorm.minecraft.gui.font.UnicodeFontRenderer;

public abstract class Component {
	
	private Container parent = null;
	
	protected static UnicodeFontRenderer fontRenderer = new UnicodeFontRenderer(
			new Font("Segue UI", Font.PLAIN, 15));
	
	private double width;
	private double height;
	protected double x;
	protected double y;

	public abstract void update();
	
	public abstract void draw();
	
	public double getHeight() {
		
		return height;
	}
	
	public void setHeight(double height) {
		this.height = height;
	}
	
	public double getWidth() {
		return width;
	}
	
	public void setWidth(double width) {
		this.width = width;
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public Container getParent() {
		return parent;
	}
	
	public void setParent(Container parent) {
		this.parent = parent;
	}
	
	public void onClicked(double x, double y, int button) {
		
	}
	
	public void onReleased(double x, double y, int button) {
		
	}
	
	public void onReleased(int button) {
		
	}

	public void onDragged(int x, int y, double mx, double my, int button) {
		
	}
	
	public abstract double getPreferedWidth();
	
	public abstract double getPreferedHeight();


public void keyTyped(char typedChar, int key) {
		
	}
	
}
