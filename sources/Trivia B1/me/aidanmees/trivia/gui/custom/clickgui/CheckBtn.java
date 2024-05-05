package me.aidanmees.trivia.gui.custom.clickgui;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.awt.Point;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.aidanmees.trivia.client.gui.tab.TabGuiContainer;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.gui.custom.clickgui.utils.GuiUtils;
import me.aidanmees.trivia.module.Module;

public abstract class CheckBtn extends Component {
	
	private String title;
	private boolean toggled = false;

	@Override
	public void update() {
//		if (mod.getKeyboardKey() == Keyboard.KEY_NONE) {
//			this.setTitle(mod.getName());
//		} else {
//			this.setTitle(mod.getName() + "[" + Keyboard.getKeyName(mod.getKeyboardKey()) + "]");
//		}
	}

	@Override
	public void draw() {
		GuiUtils.translate(this, false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(0.05f, 0.05f, 0.05f, 1.06f  - trivia.getClickGuiManager().getAlpha());
		glBegin(GL11.GL_QUADS);
		{
			glVertex2d(0, 0);
			glVertex2d(0, getHeight());
			glVertex2d(getWidth(), getHeight());
			glVertex2d(getWidth(), 0);
		}
		glEnd();
		glDisable(GL_CULL_FACE);
		GL11.glColor4f(0.2f, 0.2f, 0.2f, 0.8f - trivia.getClickGuiManager().getAlpha());
		if(isToggled()) {
			GL11.glColor4f(TabGuiContainer.out, TabGuiContainer.out1, TabGuiContainer.out2, TabGuiContainer.out3);
		}
		glBegin(GL11.GL_QUADS);
		{
			
			glVertex2d(2, 2);
			glVertex2d(2, getHeight() - 1);
			glVertex2d(1 + 10, getHeight() - 1);
			glVertex2d(1 + 10, 2);
		}
		glEnd();
		
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		if(trivia.getClickGuiManager().getAlpha() <= 0.5f) {
			fontRenderer.drawString(title, 13, 1, 0xffffffff);
		}
		
		GL11.glDisable(GL11.GL_BLEND);
		glEnable(GL_CULL_FACE);
		GuiUtils.translate(this, true);
	}
	
	public void setToggled(boolean toggled) {
		this.toggled = toggled;
	}
	
	public boolean isToggled() {
		return toggled;
	}

	@Override
	public double getPreferedWidth() {
		return fontRenderer.getStringWidth(title) + 12;
	}

	@Override
	public double getPreferedHeight() {
		return fontRenderer.FONT_HEIGHT + 2;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
}
