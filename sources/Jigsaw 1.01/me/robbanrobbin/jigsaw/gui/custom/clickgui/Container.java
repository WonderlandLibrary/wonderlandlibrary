package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.gui.custom.clickgui.layout.Layout;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.utils.GuiUtils;

public abstract class Container extends Component {
	
	public static final double MIN_HEIGHT = 100d;
	public static final double EXTENDING_SPEED = 0.1d;
	
	public double preScroll = 0;
	public double scroll = 0;
	public double scrollMotion = 0;
	
	public boolean showScrollBar = false;
	
	protected boolean extended = true;
	protected double extendedTime = 1d;
	protected double preExtendedTime = 1d;
	
	protected ArrayList<Component> children = new ArrayList<Component>();
	protected Layout layout;
	
	public Component focus = null;
	
	public Container(Layout layout) {
		super();
		this.setAndInitLayout(layout);
	}
	
	public ArrayList<Component> getChildren() {
		return children;
	}
	
	public void addChild(Component comp) {
		
		comp.setParent(this);
		comp.setX(comp.getX() + getBorderX());
		children.add(comp);
	}
	
	public void removeChild(Component child) {
		child.setParent(null);
		children.remove(child);
	}
	
	public boolean isExtended() {
		return extended;
	}
	
	@Override
	public void update() {
		
		if(!enabled) {
			return;
		}
		
		if(useSmoothExtension()) {
			preExtendedTime = extendedTime;
			
			if(extended) {
				extendedTime+=EXTENDING_SPEED;
			}
			else {
				extendedTime-=EXTENDING_SPEED;
			}
			
			if(extendedTime > 1.0) {
				extendedTime = 1.0;
			}
			if(extendedTime < 0.0) {
				extendedTime = 0.0;
			}
		}
		else {
			preExtendedTime = extended ? 1.0 : 0.0;
			extendedTime = extended ? 1.0 : 0.0;
		}
		
		if(!isFullyClosed()) {
			for(Component child : children) {
				child.update();
				child.preUpdate();
			}
		}
		
	}
	
	public void postDrawChildren() {
		GuiUtils.translate(this, false);
		
		if(this.getParent() == null) {
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		}
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL_CULL_FACE);
		
		GuiUtils.renderShadowHorizontal(0.2, 20, 0, 0, getWidth(), false, false);
		
		if(getParent() != null) {
			GuiUtils.renderShadowHorizontal(0.2, 20, getHeight(), 0, getWidth(), true, false);
		}
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		glEnable(GL_CULL_FACE);
		
		GuiUtils.translate(this, true);
	}
	
	public void preDrawChildren() {
		
		if(isFullyClosed()) {
			return;
		}
		if(this.getParent() == null) {
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			GL11.glScissor((int)this.getX() * 2, (int)((mc.displayHeight) - (this.getY() * 2) - (this.getHeight() * 2)), (int)this.getWidth() * 2, (int)this.getHeight() * 2);
		}
	}
	
	public void preDraw() {
		
	}
	
	@Override
	public void draw() {
		
		if(!enabled) {
			return;
		}
		if(isFullyClosed()) {
			return;
		}
		
		this.preDrawChildren();
		
		this.focus = null;
		for(Component child : children) {
			if(child.isHovered()) {
				this.focus = child;
				continue;
			}
			child.draw();
		}
		if(focus != null) {
			focus.draw();
		}
		
		this.postDrawChildren();
		
		this.focus = null;
		for(Component child : children) {
			if(child.isHovered()) {
				this.focus = child;
				continue;
			}
			child.postDraw();
		}
		if(focus != null) {
			focus.postDraw();
		}
	}
	
	public void setExtended(boolean extended) {
		resetTimeForChildren(false);
		this.extended = extended;
		this.setHeight(this.getPreferedHeight());
		this.layoutChildren();
	}
	
	public void setExtendedSkipHeightCheck(boolean extended) {
		resetTimeForChildren(true);
		this.extended = extended;
		this.setHeight(this.getPreferedHeight());
		this.layoutChildren();
	}
	
	@Override
	public double getPreferedHeight() {
		return layout.getPreferedHeight();
	}
	
	@Override
	public double getPreferedWidth() {
		return layout.getPreferedWidth();
	}

	public void resetTimeForChildren(boolean skipHeightCheck) {
		for(Component child : children) {
			if(!skipHeightCheck && child.getY() < getHeightAdjustedForExtendedTicks()) {
				continue;
			}
			if(child instanceof ModuleButton) {
				resetTimeForExtension((ModuleButton) child);
			}
			if(child instanceof WindowButton) {
				resetTimeForExtension((WindowButton) child);
				
			}
		}
	}
	
	public void resetTimeForExtension(ModuleButton child) {
		if(!child.getMod().isToggled()) {
			child.increasingTime = false;
			return;
		}
		child.time = 0f;
		child.preTime = 0f;
		child.waitForTime = 0.02f * (float)child.getIndexInContainer() + 0.1f;
	}
	
	public void resetTimeForExtension(WindowButton child) {
		if(!child.getWindow().isEnabled()) {
			child.increasingTime = false;
			return;
		}
		child.time = 0f;
		child.preTime = 0f;
		child.waitForTime = 0.02f * ((float)child.getIndexInContainer()) + 0.1f;
	}
	
	public void layoutChildren() {
		setWidth(getPreferedWidth());
		setHeight(getPreferedHeight());
		justLayoutChildren();
	}
	
	public void justLayoutChildren() {
		
		this.layout.layoutChildren(scroll);
		int i = 0;
		for(Component child : children) {
			child.setWidth(getWidth() - getBorderX() * 2.0);
			child.setHeight(child.getPreferedHeight());
			child.setX(getBorderX());
			if(child instanceof Container) {
				((Container) child).justLayoutChildren();
			}
			child.firstChild = i == 0;
			i++;
		}
		
	}
	
	@Override
	public void onClicked(double x, double y, int button) {
		super.onClicked(x, y, button);
		if(!extended) {
			return;
		}
		for(Component comp : children) {
			if(x > comp.getX() && x <= comp.getX() + comp.getWidth()
				&& y > comp.getY() && y <= comp.getY() + comp.getHeight()) {
				comp.onClicked(x - comp.getX(), y - comp.getY(), button);
			}
		}
	}
	
	@Override
	public void onReleased(double x, double y, int button) {
		super.onReleased(x, y, button);
		if(!extended) {
			return;
		}
		for(Component comp : children) {
			if(x > comp.getX() && x < comp.getX() + comp.getWidth()
				&& y > comp.getY() && y < comp.getY() + comp.getHeight()) {
				comp.onReleased(x - comp.getX(), y - comp.getY(), button);
			}
		}
	}
	
	@Override
	public void onDragged(int x, int y, double mx, double my, int button) {
		super.onDragged(x, y, mx, my, button);
		if(isFullyClosed()) {
			return;
		}
		for(Component child : children) {
			if(mx >= child.getX() && mx < child.getWidth() + child.getX() + 1
				&& my > child.getY() && my < child.getHeight() + child.getY() + 1) {
				child.onDragged(x, y, mx - child.getX(), my - child.getY(), button);
			}
		}
	}
	
	@Override
	public void setWidth(double width) {
		if(width <= getPreferedWidth()) {
			width = getPreferedWidth();
		}
		super.setWidth(width);
	}
	
	@Override
	public void onReleased(int button) {
		super.onReleased(button);
		for(Component comp : children) {
			comp.onReleased(button);
		}
	}
	
	@Override
	public void setHeight(double height) {
		super.setHeight(height);
	}
	
	public double getBorderX() {
		return 0.0;
	}
	
	public double getBorderY() {
		return 0.0;
	}

	@Override
	public void onHover(double x, double y) {
		super.onHover(x, y);
		for (Component child : children) {
			if(x > child.getX() && x <= child.getX() + child.getWidth()
				&& y > child.getY() && y <= child.getY() + child.getHeight()) {
				child.onHover(x - child.getX(), y - child.getY());
			}
		}
	}
	
	@Override
	public void onLostHover(double x, double y) {
		super.onLostHover(x, y);
		for (Component child : children) {
			if(child.hovered) {
				child.onLostHover(x - child.getX(), y - child.getY());
			}
		}
	}
	
	public void setAndInitLayout(Layout layout) {
		layout.setContainer(this);
		this.layout = layout;
	}
	
	@Override
	public void updateColors() {
		super.updateColors();
		for (Component child : children) {
			child.updateColors();
		}
	}
	
	public int getHeightAdjustedForExtendedTicks() {
		if(isFullyClosed()) {
			return (int)getHeight();
		}
		return (int) (this.getHeight() * (extendedTime + (extendedTime - preExtendedTime) * mc.timer.renderPartialTicks));
	}
	
	public double getRealHeight() {
		return this.layout.REAL_HEIGHT;
	}
	
	public boolean isFullyClosed() {
		return extendedTime == 0d;
	}
	
	public boolean isFullyExtended() {
		return extendedTime == 1d;
	}
	
	public boolean useSmoothExtension() {
		return false;
	}
	
}
