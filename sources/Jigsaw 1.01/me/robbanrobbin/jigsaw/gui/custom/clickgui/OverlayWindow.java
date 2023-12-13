package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import org.darkstorm.minecraft.gui.util.RenderUtil;

import me.robbanrobbin.jigsaw.gui.custom.clickgui.layout.Layout;

public class OverlayWindow extends ComponentWindow {
	
	public OverlayWindow(Layout layout) {
		super(layout);
		this.setX(getX() - ((getX() - RenderUtil.calculateMouseLocation().x)));
		this.setY(getY() - ((getY() - RenderUtil.calculateMouseLocation().y - 1)));
		
		if(this.getX() > mc.displayWidth) {
			this.setX(mc.displayWidth - (this.getX() - mc.displayWidth));
		}
		this.headHeight = 0;
	}
	
	@Override
	public void addChild(Component comp) {
		super.addChild(comp);
		this.layoutChildren();
	}

	@Override
	public double getPreferedHeight() {
		double y = 0;
		if(isFullyClosed()) {
			return y;
		}
		for(Component comp : children) {
			y += getBorderY() / 2.0;
			comp.setY(y);
			if(comp instanceof Container) {
				//comp.setHeight(comp.getPreferedHeight());
			}
			y += comp.getHeight();
			y += getBorderY() / 2.0;
		}
		return y;
	}

	@Override
	public double getPreferedWidth() {
		double maxWidth = 40;
		for(Component child : children) {
			if(child instanceof Container) {
				//child.setWidth(child.getPreferedWidth());
			}
			maxWidth = Math.max(child.getPreferedWidth() + getBorderX() * 2, maxWidth);
		}
		return maxWidth;
	}
	
	@Override
	public boolean useSmoothExtension() {
		return false;
	}

}
