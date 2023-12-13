package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import java.awt.Color;
import java.awt.Font;

import org.darkstorm.minecraft.gui.font.UnicodeFontRenderer;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.font.Fonts;
import net.minecraft.client.Minecraft;

public abstract class Component {
	
	private Container parent = null;
	
	protected static Minecraft mc = Minecraft.getMinecraft();

	protected boolean hovered;
	protected int hoverTime;
	
	protected boolean enabled = true;
	
	protected static UnicodeFontRenderer fontRenderer = new UnicodeFontRenderer(Fonts.fontGuiFromFile.deriveFont(Font.PLAIN, 16));
	
	protected Color foreground = ClientSettings.getForeGroundGuiColor();
	protected Color background = ClientSettings.getBackGroundGuiColor();
	
	private double width;
	private double height;
	private double x;
	private double y;
	
	private double preX = -999;
	private double preY = -999;
	
	public boolean firstChild;
	public boolean lastChild;
	
	public boolean firstUpdate;
	
	public void updateColors() {
		
		foreground = ClientSettings.getForeGroundGuiColor();
		background = ClientSettings.getBackGroundGuiColor();
		
	}

	public void update() {
		
		if(hovered) {
			hoverTime++;
		}
		else {
			hoverTime = 0;
		}
	}
	
	public void preUpdate() {
		hovered = false;
	}
	
	public void draw() {
		
	}
	
	public void postDraw() {
		
	}
	
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
		this.setWidth(this.getPreferedWidth());
		this.setHeight(this.getPreferedHeight());
	}
	
	public void onClicked(double x, double y, int button) {
		this.setFocused(true);
	}
	
	public void onReleased(double x, double y, int button) {
		
	}
	
	public void onReleased(int button) {
		
	}

	public void onDragged(int x, int y, double mx, double my, int button) {
		
	}
	
	public abstract double getPreferedWidth();
	
	public abstract double getPreferedHeight();
	
	public int getIndexInContainer() {
		if(this.getParent() == null) {
			return -1;
		}
		int i = 0;
		for(Component child : getParent().getChildren()) {
			if(this.equals(child)) {
				return i;
			}
			i++;
		}
		return -1;
	}
	
	public int getIndexInContainerBackwards() {
		return -(getIndexInContainer() - getParent().getChildren().size());
	}
	
	public void setForeground(Color foreground) {
		this.foreground = foreground;
	}
	
	public Color getForeground() {
		return foreground;
	}
	
	public void setBackground(Color background) {
		this.background = background;
	}
	
	public Color getBackground() {
		return background;
	}
	
	public void setHovered(boolean hovered) {
		this.hovered = hovered;
	}
	
	public boolean isHovered() {
		return hoverTime > 0;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void onHover(double x, double y) {
		this.hovered = true;
	}
	
	public void onLostHover(double x, double y) {
		
	}
	
	public void setFocused(boolean focused) {
		if(focused) {
			Jigsaw.getClickGuiManager().focusedComponent = this;
			onGainedFocus();
		}
		else {
			Jigsaw.getClickGuiManager().focusedComponent = null;
			onLostFocus();
		}
	}
	
	public boolean isFocused() {
		return Jigsaw.getClickGuiManager().focusedComponent != null && Jigsaw.getClickGuiManager().focusedComponent.equals(this);
	}

	public void keyTyped(char typedChar, int keyCode) {
		
	}
	
	public void onGainedFocus() {
		
	}
	
	public void onLostFocus() {
		
	}
	
	public double getPreX() {
		return preX;
	}
	
	public double getPreY() {
		return preY;
	}
	
	public void setPreX(double preX) {
		this.preX = preX;
	}
	
	public void setPreY(double preY) {
		this.preY = preY;
	}
	
}
