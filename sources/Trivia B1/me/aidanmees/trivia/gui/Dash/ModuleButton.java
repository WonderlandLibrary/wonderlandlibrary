package me.aidanmees.trivia.gui.Dash;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.awt.Color;
import java.awt.Point;

import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.aidanmees.trivia.client.gui.tab.TabGuiContainer;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.gui.custom.clickgui.Component;
import me.aidanmees.trivia.gui.custom.clickgui.utils.GuiUtils;
import me.aidanmees.trivia.module.Module;
import net.minecraft.client.Minecraft;

public class ModuleButton extends Component {
	
	private String title;
	private float time = 0f;
private int TheColor;
	public boolean hovered;
	private boolean isToggled = false;

	@Override
	public void update() {
		
		
	}

	@Override
	public void draw() {
		time += GuiUtils.partialTicks * 0.04f;
		GuiUtils.translate(this, false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		glDisable(GL_CULL_FACE);
		GL11.glColor4f(0.05f, 0.05f, 0.05f, 1.0f  - trivia.getClickGuiManager().getAlpha());
		
		
			TheColor = -1;
		
		
		glBegin(GL11.GL_QUADS);
		{
			glVertex2d(0, 0);
			glVertex2d(0, getHeight());
			glVertex2d(getWidth(), getHeight());
			glVertex2d(getWidth(), 0);
		}
		glEnd();
		if(hovered) {
			GL11.glColor4f(0.05f, 0.05f, 0.05f, 1f - trivia.getClickGuiManager().getAlpha());
			glBegin(GL11.GL_QUADS);
			{
				glVertex2d(0, 0);
				glVertex2d(0, getHeight());
				glVertex2d(getWidth(), getHeight());
				glVertex2d(getWidth(), 0);
			}
			glEnd();
		}
		if(!isToggled) {
			GL11.glColor4f(0.3f, 0.3f, 0.3f, 0.0f - trivia.getClickGuiManager().getAlpha());
			glBegin(GL11.GL_LINES);
			{
				glVertex2d(10, 0);
				glVertex2d(getWidth(), 0);
			}
			glEnd();
			GL11.glColor4f(0.1f, 0.1f, 0.1f, 0.0f - trivia.getClickGuiManager().getAlpha());
			
			
		}
		else {
			GL11.glColor4f(0.3f, 0.3f, 0.3f, 0.8f - trivia.getClickGuiManager().getAlpha());
			
			
		}
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
			fontRenderer.drawStringWithShadow(title, (float) (getWidth() / 2 - fontRenderer.getStringWidth(title) / 2), 1, TheColor);
		
		GuiUtils.translate(this, true);
		if(hovered) {
			Point mouse = RenderUtil.calculateMouseLocation();
			mouse.translate(0, 1);
			glDisable(GL_TEXTURE_2D);
			// TODO trivia tooltips
			
			}
			glEnable(GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
			glEnable(GL_CULL_FACE);
			hovered = false;
		
	}
	
	
	
	
	@Override
	public void onClicked(double x, double y, int button) {
		super.onClicked(x, y, button);
		if(button == 0) {
			isToggled =! isToggled;
			trivia.chatMessage("CLICKED "+ getTitle());
		}
		if(button == 1) {
			
		}
	}

	@Override
	public double getPreferedWidth() {
		return fontRenderer.getStringWidth(title) ;
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
