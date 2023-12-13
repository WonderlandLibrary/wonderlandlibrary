package me.robbanrobbin.jigsaw.gui.custom.clickgui.layout;

import me.robbanrobbin.jigsaw.gui.custom.clickgui.Container;

public abstract class Layout {
	
	public double REAL_HEIGHT;
	
	protected Container container;
	
	public abstract void layoutChildren(double scroll);
	
	public void setContainer(Container container) {
		this.container = container;
	}
	
	public Container getContainer() {
		return container;
	}
	
	public abstract double getPreferedWidth();
	
	public abstract double getPreferedHeight();
	
}
