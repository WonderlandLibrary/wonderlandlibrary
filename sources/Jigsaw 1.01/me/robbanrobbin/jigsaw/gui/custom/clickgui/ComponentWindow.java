package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;

import org.darkstorm.minecraft.gui.font.UnicodeFontRenderer;
import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.font.Fonts;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.layout.Layout;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.utils.GuiUtils;
import net.minecraft.client.gui.ScaledResolution;

public class ComponentWindow extends Container {

	private static UnicodeFontRenderer titleRenderer = new UnicodeFontRenderer(Fonts.fontFromFile.deriveFont(Font.PLAIN, 17));
	protected String title;
	
	protected boolean dragging = false;
	protected double preDraggingTime = 0;
	protected double draggingTime = 0;
	
	protected double preX;
	protected double preY;
	protected boolean pinned;
	
	public int headHeight = getTitleRenderer().FONT_HEIGHT + 5;
	
	public ArrayList<ClickEffect> clickEffects = new ArrayList<ClickEffect>();
	
	public ComponentWindow(Layout layout) {
		super(layout);
	}
	
	@Override
	public void updateColors() {
		super.updateColors();

		setForeground(new java.awt.Color(
				Math.min(255, 
						this.foreground.getRed() + (9)),
				Math.min(255, 
						this.foreground.getGreen() + (9)), 
				Math.min(255, 
						this.foreground.getBlue() + (9)),
				this.foreground.getAlpha()));
	}

	@Override
	public void update() {
		
		this.preScroll = scroll;
		
		this.scrollMotion *= 0.6;
		
		if(Math.abs(scrollMotion) < 0.1) {
			scrollMotion = 0;
		}
		
		this.scroll += scrollMotion;
		
		if(this.scroll < 0) {
			this.scroll = 0;
			this.scrollMotion = 0;
		}
		
		if(getScrollBarHeight() > getHeight()) {
			this.scroll -= (int) (getScrollBarHeight() - getHeight());
			this.scrollMotion = 0;
		}
		
		if(scrollMotion != 0) {
			this.layoutChildren();
		}
		if(dragging) {
			this.layoutChildren();
		}
		
		preDraggingTime = draggingTime;
		
		double draggingTimeSpeed = 0.3;
		draggingTime += dragging ? draggingTimeSpeed : -draggingTimeSpeed;
		
		if(draggingTime < 0) {
			draggingTime = 0;
		}
		else if(draggingTime > 1) {
			draggingTime = 1;
		}
		
		super.update();
		
		for(int i = 0; i < clickEffects.size(); i++) {
			ClickEffect eff = clickEffects.get(i);
			
			if(eff.alpha <= 0.0f) {
				clickEffects.remove(i);
			}
		}
		
		for(ClickEffect effect : clickEffects) {
			effect.update();
		}
	}
	
	@Override
	public void postDrawChildren() {
		
		if(isFullyClosed()) {
			return;
		}
		for(ClickEffect effect : clickEffects) {
			effect.render();
		}
		
		GuiUtils.translate(this, false);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL_CULL_FACE);
		
		GuiUtils.renderShadowHorizontal(0.1d, 40, headHeight, 0, getWidth(), false, false);
		
		if(this.getParent() == null) {
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		}
		
		if(showScrollBar) {
			
			if(preScroll != scroll) {
				this.layoutChildren();
			}
			
			double scrollY = getScrollModifiedWithHeight() + headHeight;
			double scrollHeight = this.getScrollBarHeight();
			
			double shadowAlpha = 0.1;
			
			GuiUtils.renderShadowVertical(shadowAlpha, 12, 2, scrollY, scrollHeight, true, true);
			
			GuiUtils.renderShadowHorizontal(shadowAlpha, 12, scrollY, 0, 2, true, true);
			
			GuiUtils.renderShadowHorizontal(shadowAlpha, 12, scrollHeight, 0, 2, false, true);
			
			GuiUtils.setColor(foreground, 1f);
			
			glBegin(GL_QUADS);
			{
				glVertex2d(0, scrollY);
				glVertex2d(0, 0 + scrollHeight);
				glVertex2d(0 + 2, 0 + scrollHeight);
				glVertex2d(0 + 2, scrollY);
			}
			glEnd();
			
		}
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		glEnable(GL_CULL_FACE);
		
		GuiUtils.translate(this, true);
	}
	
	@Override
	public void preDrawChildren() {
		
		if(isFullyClosed()) {
			return;
		}
		
		if(getScrollBarHeight() > getHeight()) {
			this.scroll -= getScrollBarHeight() - getHeight();
		}
		
		if(this.getParent() == null) {
			
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			GL11.glScissor(
					(int) (this.getX() * 2), //x
					(int) (mc.displayHeight - this.getY() * 2 - getHeightAdjustedForExtendedTicks() * 2), //y
					(int) (this.getWidth() * 2), //width
					(int) (getHeightAdjustedForExtendedTicks() - headHeight) * 2); //height
			
			GuiUtils.drawBlurBufferNoSetupMatrix(false);
		}
	}
	
	@Override
	public void draw() {
		
		if(dragging) {
			this.setX(getX() - (preX + (getX() - RenderUtil.calculateMouseLocation().x)));
			this.setY(getY() - (preY + (getY() - RenderUtil.calculateMouseLocation().y - 1)));
		}
		
		GuiUtils.translate(this, false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		double shadowAlpha = 0.2;

//		GuiUtils.blurSpot(0, 0, (int)getWidth(), getHeightAdjustedForExtendedTicks(), getShadowSize() - 9, 1);
		GuiUtils.drawBlurredShadow(0, 0, (int)getWidth(), getHeightAdjustedForExtendedTicks(), getInterpolatedDraggedTime() + 9);
		
		GuiUtils.setColor(foreground);
		
		glBegin(GL_QUADS);
		{
			glVertex2d(0 - 1, 0);
			glVertex2d(0 - 1, 0 + headHeight);
			glVertex2d(getWidth() + 1, 0 + headHeight);
			glVertex2d(getWidth() + 1, 0);
		}
		glEnd();
		
		Point mouse = RenderUtil.calculateMouseLocation();
		
		boolean inPinArea = mouse.getX() > this.getX() + getWidth() - headHeight && mouse.getY() < this.getY() + headHeight && mouse.getX() < this.getX() + this.getWidth() && mouse.getY() > this.getY();
		
		GuiUtils.renderShadowVertical(inPinArea ? 0.2 : 0.1, 12, getWidth() - headHeight, 0, headHeight, false, false);
		
		GuiUtils.setColor(inPinArea ? foreground.brighter() : foreground);
		
		glBegin(GL_QUADS);
		{
			glVertex2d(getWidth() - headHeight, 0);
			glVertex2d(getWidth() - headHeight, 0 + headHeight);
			glVertex2d(getWidth() + 1, 0 + headHeight);
			glVertex2d(getWidth() + 1, 0);
		}
		glEnd();
		
		GL11.glColor4f(1f, 1f, 1f, (isPinned() ? 1f : 0.5f));
		
		glBegin(GL_QUADS);
		{
			glVertex2d(getWidth() - headHeight / 2 - 2, 3);
			glVertex2d(getWidth() - headHeight / 2 - 2, -7 + headHeight);
			glVertex2d(getWidth() - headHeight / 2 + 2, -7 + headHeight);
			glVertex2d(getWidth() - headHeight / 2 + 2, 3);
		}
		glEnd();
		
		GL11.glLineWidth(2f);
		
		glBegin(GL11.GL_LINES);
		{
			glVertex2d(getWidth() - headHeight / 2, -7 + headHeight);
			glVertex2d(getWidth() - headHeight / 2, -4 + headHeight);
		}
		glEnd();
		
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		
		if(title != null) {
			int stringWidth = getTitleRenderer().getStringWidth(title);
			Color color = new Color(1f, 1f, 1f, 1f);
			
			if(this.foreground.getRed() > 180 && this.foreground.getGreen() > 180 && this.foreground.getBlue() > 180) {
				color = new Color(0f, 0f, 0f, 1f);
			}
			
			if(stringWidth > ((int)getWidth() / 2 - headHeight)) {
				getTitleRenderer().drawString(title, ((int)getWidth() - headHeight) / 2 - stringWidth / 2, 2, color);
			}
			else {
				getTitleRenderer().drawString(title, ((int)getWidth()) / 2 - stringWidth / 2, 2, color);
			}
		}
		
		glEnable(GL_CULL_FACE);
		GuiUtils.translate(this, true);
		super.draw();
		
		
	}

//	@Override
//	public double getPreferedHeight() {
//		double y = headHeight;
//		if(!extended) {
//			return y;
//		}
//		for(Component comp : children) {
//			y += getBorderY() / 2.0;
//			comp.setY(y);
//			if(comp instanceof Container) {
//				//comp.setHeight(comp.getPreferedHeight());
//			}
//			y += comp.getHeight();
//			y += getBorderY() / 2.0;
//		}
//		return y;
//	}
//
//	@Override
//	public double getPreferedWidth() {
//		double maxWidth = getTitleRenderer().getStringWidth(title) + 10 + headHeight;
//		for(Component child : children) {
//			if(child instanceof Container) {
//				//child.setWidth(child.getPreferedWidth());
//			}
//			maxWidth = Math.max(child.getPreferedWidth() + getBorderX() * 2, maxWidth);
//		}
//		return maxWidth;
//	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public void onClicked(double x, double y, int button) {
		
		if(button == 0) {
			clickEffects.add(new ClickEffect((int)(x + this.getX()), (int)(y + this.getY())));
		}
		
		ArrayList<ComponentWindow> tempWindows = new ArrayList<ComponentWindow>();
		for(ComponentWindow wind : Jigsaw.getClickGuiManager().windows) {
			if(!wind.equals(this)) {
				tempWindows.add(wind);
			}
		}
		tempWindows.add(this);
		
		Jigsaw.getClickGuiManager().windows = tempWindows;
		
		if(button == 0 && y <= headHeight) {
			preX = x;
			preY = y;
			this.dragging = true;
		}
		else {
			super.onClicked(x, y, button);
		}
		if(button == 0 && x > getWidth() - headHeight && y < headHeight && x < this.getWidth() && y > 0) {
			this.setPinned(!this.isPinned());
		}
	}
	
	@Override
	public void onDragged(int x, int y, double mx, double my, int button) {
		
		super.onDragged(x, y, mx, my, button);
	}
	
	@Override
	public void onHover(double x, double y) {
		if(x == this.getWidth()) {
			
		}
		super.onHover(x, y);
	}
	
	@Override
	public void onReleased(int button) {
		super.onReleased(button);
		if(button == 0) {
			this.dragging = false;
		}
	}
	
	@Override
	public void onReleased(double x, double y, int button) {
		super.onReleased(x, y, button);
		if(button == 1 && y < headHeight) {
			this.setExtended(!isExtended());
		}
	}
	
	public void setPinned(boolean pinned) {
		this.pinned = pinned;
	}
	
	public boolean isPinned() {
		return pinned;
	}

	public static UnicodeFontRenderer getTitleRenderer() {
		return titleRenderer;
	}

	public static void setTitleRenderer(UnicodeFontRenderer titleRenderer) {
		ComponentWindow.titleRenderer = titleRenderer;
	}

	public void onScroll(int scroll) {
		if(!showScrollBar) {
			return;
		}
		
		this.scrollMotion -= scroll * 10;
	}
	
	public double getScrollBarHeight() {
		return getScrollModifiedWithHeight() + (getHeight() * (getHeight() / this.getRealHeight()));
	}
	
	public double getScrollModifiedWithHeight() {
		return (double)(preScroll + (this.scroll - this.preScroll) * mc.timer.renderPartialTicks) * (getHeight() / getRealHeight());
	}
	
	@Override
	public int getHeightAdjustedForExtendedTicks() {
		if(isFullyClosed()) {
			return (int)getHeight();
		}
		double multiplier = (preExtendedTime + (extendedTime - preExtendedTime) * mc.timer.renderPartialTicks);
		multiplier = Math.sin((multiplier * 1.6d));
		return (int) Math.max(this.getRealHeight() * multiplier, headHeight);
	}
	
	@Override
	public boolean useSmoothExtension() {
		return true;
	}
	
	private int getInterpolatedDraggedTime() {
		double multiplier = (preDraggingTime + (draggingTime - preDraggingTime) * mc.timer.renderPartialTicks);
		return (int) (Math.sin(multiplier) * 10);
	}

}
