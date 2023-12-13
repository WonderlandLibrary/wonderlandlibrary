package me.robbanrobbin.jigsaw.gui.custom.clickgui.layout;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.Component;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ComponentWindow;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.Container;

public class HorizontalLayout extends Layout {
	
	private int rows = 1;

	public HorizontalLayout(int rows) {
		this.rows = rows;
	}
	
	@Override
	public void layoutChildren(double scroll) {
		int i = 0;
		int row = 1;
		for(Component child : container.getChildren()) {
			child.setWidth(container.getWidth() - container.getBorderX() * 2.0);
			child.setHeight(child.getPreferedHeight());
			child.setX(container.getBorderX());
			if(child instanceof Container) {
				((Container) child).justLayoutChildren();
			}
			child.firstChild = i == 0;
			i++;
		}
		if(container instanceof ComponentWindow) {
			ComponentWindow wind = (ComponentWindow)container;
			
			double y = wind.headHeight - scroll;
			if(!wind.isExtended()) {
				return;
			}
			for(Component comp : wind.getChildren()) {
				y += wind.getBorderY() / 2.0;
				comp.setY(y);
				y += comp.getHeight();
				y += wind.getBorderY() / 2.0;
			}
		}
		else {
			double y = 0 - scroll;
			if(!container.isExtended()) {
				return;
			}
			y+= container.getBorderY();
			for(Component comp : container.getChildren()) {
				comp.setY(y);
				y += comp.getHeight();
			}
			y+= container.getBorderY();
		}
	}

	@Override
	public double getPreferedWidth() {
		if(container instanceof ComponentWindow) {
			ComponentWindow wind = (ComponentWindow)container;
			
			double maxWidth = wind.getTitleRenderer().getStringWidth(wind.getTitle()) + 10 + wind.headHeight;
			for(Component child : wind.getChildren()) {
				maxWidth = Math.max(child.getPreferedWidth() + wind.getBorderX() * 2, maxWidth);
			}
			return maxWidth;
		}
		else {
			double maxWidth = 0;
			for(Component child : container.getChildren()) {
				maxWidth = Math.max(child.getPreferedWidth() + container.getBorderX() * 2, maxWidth);
			}
			return maxWidth;
		}
	}

	@Override
	public double getPreferedHeight() {
		if(container instanceof ComponentWindow) {
			ComponentWindow wind = (ComponentWindow)container;
			
			double y = wind.headHeight;
			if(!wind.isExtended()) {
				return y;
			}
			for(Component comp : wind.getChildren()) {
				y += wind.getBorderY() / 2.0;
				y += comp.getHeight();
				y += wind.getBorderY() / 2.0;
			}
			this.REAL_HEIGHT = y;
			
			double maxYForWindow = ((y + container.getY()));
			
			double newHeight = Jigsaw.getClickGuiManager().MAX_CONTAINER_HEIGHT / 2 - wind.getY();
			
			if(this.REAL_HEIGHT >= Container.MIN_HEIGHT && maxYForWindow * 2 > Jigsaw.getClickGuiManager().MAX_CONTAINER_HEIGHT) {
				wind.showScrollBar = true;
				if(newHeight >= Container.MIN_HEIGHT) {
					return Jigsaw.getClickGuiManager().MAX_CONTAINER_HEIGHT / 2 - wind.getY();
				}
				else {
					return Container.MIN_HEIGHT;
				}
			}
			else {
				if(wind.scroll == 0) {
					wind.showScrollBar = false;
				}
			}
//			if(y <= Container.MIN_HEIGHT) {
//				return Container.MIN_HEIGHT;
//			}
			return y;
		}
		else {
			double y = 0;
			if(!container.isExtended()) {
				return y;
			}
			y+= container.getBorderY();
			for(Component comp : container.getChildren()) {
				y += comp.getHeight();
			}
			y+= container.getBorderY();
			return y;
		}
	}

}
