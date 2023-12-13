package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.gui.custom.clickgui.utils.GuiUtils;
import net.minecraft.client.Minecraft;

public class WindowButton extends ComponentButton {
	
	private ComponentWindow window;
	public float time = 0f;
	public float preTime = 0f;
	public boolean increasingTime = true;
	public float waitForTime = 0f;
	public boolean preModToggled;

	public WindowButton(ComponentWindow window) {
		this.window = window;
		this.title = window.title;
		preModToggled = window.isEnabled();
	}
	
	@Override
	public void update() {
		if(preModToggled != window.isEnabled()) {
			preModToggled = window.isEnabled();
			if(window.isEnabled()) {
				resetTime();
				increasingTime = true;
			}
			else {
				increasingTime = false;
			}
		}
		if(waitForTime > 0f) {
			waitForTime -= 0.1f;
			time = 0f;
			preTime = 0f;
		}
		else {
			preTime = time;
			if(increasingTime) {
				time += (4f / ((time * 0.03f) + 1f));
			}
			else {
				time -= 1f;
			}
			if(time > 10f) {
				time = 10f;
			}
			if(time < 0f) {
				time = 0f;
			}
		}
		this.setTitle(window.getTitle());
	}
	
	@Override
	protected void drawBody() {
		GuiUtils.enableDefaults();
		GuiUtils.translate(this, false);

		float time = (this.preTime + (this.time - this.preTime) * Minecraft.getMinecraft().timer.renderPartialTicks);
		
		GL11.glLineWidth(1f);
		
		GuiUtils.setColor(this.getForeground());
		
		glBegin(GL11.GL_QUADS);
		{
			glVertex2d(0, 0);
			glVertex2d(0, getHeight());
			glVertex2d(getWidth() * time / 10f, getHeight());
			glVertex2d(getWidth() * time / 10f, 0);
		}
		glEnd();
		
		GuiUtils.setColor(this.getBackground());
		
		glBegin(GL11.GL_QUADS);
		{
			glVertex2d(time * getWidth() / 10f, 0);
			glVertex2d(time * getWidth() / 10f, getHeight());
			glVertex2d(getWidth(), getHeight());
			glVertex2d(getWidth(), 0);
		}
		glEnd();
		if(hovered) {
			GL11.glColor4f(1f, 1f, 1f, 0.05f);
			glBegin(GL11.GL_QUADS);
			{
				glVertex2d(0, 0);
				glVertex2d(0, getHeight());
				glVertex2d(getWidth(), getHeight());
				glVertex2d(getWidth(), 0);
			}
			glEnd();
		}
		if(!window.isEnabled()) {
			GL11.glColor4f(0.3f, 0.3f, 0.3f, 0.8f);
			GL11.glColor4f(0.1f, 0.1f, 0.1f, 0.8f);
		}
		else {
			GL11.glColor4f(0.3f, 0.3f, 0.3f, 0.8f);
			GL11.glColor4f(0.1f, 0.1f, 0.1f, 0.8f);
		}

		GuiUtils.translate(this, true);
		GuiUtils.disableDefaults();
	}
	
	public ComponentWindow getWindow() {
		return window;
	}
	
	public void setWindow(ComponentWindow wind) {
		this.window = wind;
		this.preModToggled = wind.isExtended();
	}
	
	@Override
	public void onClicked(double x, double y, int button) {
		super.onClicked(x, y, button);
		if(button == 0) {
			window.setEnabled(!window.enabled);
		}
	}

	@Override
	public double getPreferedWidth() {
		return fontRenderer.getStringWidth(title) + 8;
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
	
	public void resetTime() {
		time = 1.3f;
		preTime = 1.3f;
	}
	
}
