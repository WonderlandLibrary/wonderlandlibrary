package me.aidanmees.trivia.gui.custom.clickgui;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import org.lwjgl.opengl.GL11;

import me.aidanmees.trivia.client.gui.tab.TabGuiContainer;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.gui.custom.clickgui.utils.GuiUtils;

public class SettingContainer extends Container {

	public boolean modeMatchedSearch;

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void draw() {
		GuiUtils.translate(this, false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		glDisable(GL_CULL_FACE);
		
		
		
		if(extended) {
			GL11.glColor4f(0.05f, 0.05f, 0.05f, 1f - trivia.getClickGuiManager().getAlpha());
			glBegin(GL_QUADS);
			{
				glVertex2d(0, 0);
				glVertex2d(0, getHeight());
				glVertex2d(getWidth(), getHeight());
				glVertex2d(getWidth(), 0);
			}
			glEnd();
			
			GL11.glColor4f(TabGuiContainer.out, TabGuiContainer.out1, TabGuiContainer.out2, 1f - trivia.getClickGuiManager().getAlpha());
			glBegin(GL_QUADS);
			{
				glVertex2d(getWidth(), 0);
				glVertex2d(getWidth(), getHeight());
				glVertex2d(getWidth() + 1, getHeight());
				glVertex2d(getWidth() + 1, 0);
			}
			glEnd();
			glBegin(GL_QUADS);
			{
				glVertex2d(0, getHeight() -1);
				glVertex2d(0, getHeight());
				glVertex2d(getWidth() + 1, getHeight());
				glVertex2d(getWidth() + 1, getHeight() -1);
			}
			glEnd();
			glBegin(GL_QUADS);
			{
				glVertex2d(0, -1);
				glVertex2d(0, 0);
				glVertex2d(getWidth() + 1, 0);
				glVertex2d(getWidth() + 1,  -1);
			}
			glEnd();
		}
		
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		glEnable(GL_CULL_FACE);
		GuiUtils.translate(this, true);
		super.draw();
	}

	@Override
	public double getPreferedHeight() {
		double y = 0;
		if(!extended) {
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
		double maxWidth = 0;
		for(Component child : children) {
			if(child instanceof Container) {
				//child.setWidth(child.getPreferedWidth());
			}
			maxWidth = Math.max(child.getPreferedWidth() + getBorderX() * 2, maxWidth);
		}
		return maxWidth;
	}
	
	@Override
	public void onReleased(double x, double y, int button) {
		super.onReleased(x, y, button);
	}
	
	@Override
	public double getBorderX() {
		return 3;
	}
	
	@Override
	public double getBorderY() {
		return 3;
	}

}
