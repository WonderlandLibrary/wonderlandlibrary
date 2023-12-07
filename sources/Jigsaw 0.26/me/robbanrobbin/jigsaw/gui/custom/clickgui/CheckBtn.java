package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.awt.Color;
import java.awt.Point;

import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.utils.GuiUtils;
import me.robbanrobbin.jigsaw.module.Module;

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
		glDisable(GL_CULL_FACE);
		GL11.glColor4f(0.1f, 0.1f, 0.1f, 0.8f - Jigsaw.getClickGuiManager().getAlpha());
		glBegin(GL11.GL_QUADS);
		{
			glVertex2d(0, 0);
			glVertex2d(0, getHeight());
			glVertex2d(getWidth(), getHeight());
			glVertex2d(getWidth(), 0);
		}
		glEnd();
		if(isToggled()) {
			GL11.glColor4f(0.8f, 0.1f, 0.1f, 0.8f - Jigsaw.getClickGuiManager().getAlpha());
		}
		glBegin(GL11.GL_QUADS);
		{
			glVertex2d(2, 1);
			glVertex2d(2, getHeight() - 2.5);
			glVertex2d(2 + 8, getHeight() - 2.5);
			glVertex2d(2 + 8, 1);
		}
		glEnd();
		GL11.glColor4f(0.3f, 0.3f, 0.3f, 0.8f - Jigsaw.getClickGuiManager().getAlpha());
		glBegin(GL11.GL_LINES);
		{
			glVertex2d(2, getHeight() - 2.5);
			glVertex2d(10, getHeight() - 2.5);
		}
		glEnd();
		glBegin(GL11.GL_LINES);
		{
			glVertex2d(2, 1);
			glVertex2d(10, 1);
		}
		glEnd();
		glBegin(GL11.GL_LINES);
		{
			glVertex2d(2, 1);
			glVertex2d(2, 10);
		}
		glEnd();
		glBegin(GL11.GL_LINES);
		{
			glVertex2d(10, 1);
			glVertex2d(10, 10);
		}
		glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		fontRenderer.drawStringWithShadow(title, 11, 1, RenderUtil.toRGBA(new Color(1f, 1f, 1f, 1f - Jigsaw.getClickGuiManager().getAlpha())));
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
		return fontRenderer.FONT_HEIGHT + 3;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
}
