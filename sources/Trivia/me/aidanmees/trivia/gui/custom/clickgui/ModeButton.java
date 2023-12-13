package me.aidanmees.trivia.gui.custom.clickgui;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.gui.custom.clickgui.utils.GuiUtils;
import me.aidanmees.trivia.module.Module;

public class ModeButton extends Component {
	
	private Module mod;
	
	public ModeButton(Module mod) {
		this.mod = mod;
	}

	@Override
	public void update() {
		
	}

	@Override
	public void draw() {
		GuiUtils.translate(this, false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		glDisable(GL_CULL_FACE);
		GL11.glColor4f(0.05f, 0.05f, 0.05f, 0.8f - trivia.getClickGuiManager().getAlpha());
		if(((SettingContainer)getParent()).modeMatchedSearch) {
			
		}
		glBegin(GL11.GL_QUADS);
		{
			glVertex2d(0, 0);
			glVertex2d(0, getHeight());
			glVertex2d(getWidth(), getHeight());
			glVertex2d(getWidth(), 0);
		}
		
		glEnd();
	
		GL11.glLineWidth(1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		if(mod != null && trivia.getClickGuiManager().getAlpha() <= 0.5f) {
			fontRenderer.drawString(getDrawString(),  (int) (getWidth() / 2 - fontRenderer.getStringWidth(getDrawString()) / 2), 1, 0xffffffff);
		}
		GL11.glDisable(GL11.GL_BLEND);
		glEnable(GL_CULL_FACE);
		GuiUtils.translate(this, true);
	}
	
	private String getDrawString() {
		return "Mode: " + mod.getCurrentMode();
	}
	
	@Override
	public void onClicked(double x, double y, int button) {
		if(button == 0) {
			mod.setMode(mod.getModes()[getModeIndexForward()]);
		}
		if(button == 1) {
			mod.setMode(mod.getModes()[getModeIndexBackward()]);
		}
		super.onClicked(x, y, button);
	}
	
	public int getModeIndexForward() {
		for(int i = 0; i < mod.getModes().length; i++) {
			if(mod.getModes()[i].equals(mod.getCurrentMode())) {
				if(i + 1 >= mod.getModes().length) {
					return 0;
				}
				return i + 1;
			}
		}
		return -1;
	}
	
	public int getModeIndexBackward() {
		for(int i = 0; i < mod.getModes().length; i++) {
			if(mod.getModes()[i].equals(mod.getCurrentMode())) {
				if(i - 1 < 0) {
					return mod.getModes().length - 1;
				}
				return i - 1;
			}
		}
		return -1;
	}

	@Override
	public double getPreferedWidth() {
		return 25;
	}

	@Override
	public double getPreferedHeight() {
		return fontRenderer.FONT_HEIGHT + 2;
	}
	
}
