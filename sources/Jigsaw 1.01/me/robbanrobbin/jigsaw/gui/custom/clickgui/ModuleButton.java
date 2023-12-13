package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.utils.GuiUtils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.Minecraft;

public class ModuleButton extends ComponentButton {
	
	private Module mod;
	private SettingContainer settingContainer;
	public float time = 0f;
	public float preTime = 0f;
	public boolean increasingTime = false;
	public float waitForTime = 0f;
	public boolean preModToggled;

	@Override
	public void update() {
		super.update();
		if(preModToggled != mod.isToggled()) {
			preModToggled = mod.isToggled();
			if(mod.isToggled()) {
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
		if (mod.getKeyboardKey() == Keyboard.KEY_NONE) {
			this.setTitle(mod.getName());
		} else {
			this.setTitle(mod.getName() + "[" + Keyboard.getKeyName(mod.getKeyboardKey()) + "]");
		}
		settingContainer.modeMatchedSearch = false;
		if(!Jigsaw.getSearch().isEmpty() && mod.getModes().length > 1) {
			boolean found = false;
			for(String s : mod.getModes()) {
				if(s.toLowerCase().indexOf(Jigsaw.getSearch().toLowerCase()) != -1) {
					found = true;
				}
			}
			if(found) {
				settingContainer.modeMatchedSearch = true;
			}
		}
	}
	
	@Override
	protected void drawBody() {
		GuiUtils.enableDefaults();
		GuiUtils.translate(this, false);

		float time = (this.preTime + (this.time - this.preTime) * Minecraft.getMinecraft().timer.renderPartialTicks);
		boolean drawSearchColor = false;
		
		if(Jigsaw.getSearch() != null && !Jigsaw.getSearch().isEmpty() && this.title.toLowerCase().indexOf(Jigsaw.getSearch().toLowerCase()) != -1) {
			drawSearchColor = true;
		}
		GL11.glLineWidth(1f);
		
		GuiUtils.setColor(this.getForeground());
		
		if(drawSearchColor) {
			GL11.glColor4f(0.3f, 0.7f, 0.2f, 0.8f);
		}
		glBegin(GL11.GL_QUADS);
		{
			glVertex2d(0, 0);
			glVertex2d(0, getHeight());
			glVertex2d(getWidth() * time / 10f, getHeight());
			glVertex2d(getWidth() * time / 10f, 0);
		}
		glEnd();
		
		GuiUtils.setColor(this.getBackground());
		
		if(drawSearchColor) {
			GL11.glColor4f(0.3f, 0.7f, 0.2f, 0.8f);
		}
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
		if(mod.hasCustomSettings()) {
			boolean isTimeValid = time * getWidth() / 10f == getWidth();
			GuiUtils.setColor(isTimeValid ? this.getBackground() : this.getForeground());
			
			GL11.glPointSize(3f);
			glBegin(GL11.GL_POINTS);
			{
				glVertex2d(getWidth() - 2, getHeight() / 2d);
				glVertex2d(getWidth() - 5, getHeight() / 2d);
				glVertex2d(getWidth() - 8, getHeight() / 2d);
			}
			glEnd();
		}
		if(!mod.isToggled()) {
			GL11.glColor4f(0.3f, 0.3f, 0.3f, 0.8f);
			GL11.glColor4f(0.1f, 0.1f, 0.1f, 0.8f);
			if(settingContainer.modeMatchedSearch) {
				GL11.glColor4f(0.3f, 0.7f, 0.2f, 0.8f);
				glBegin(GL11.GL_LINES);
				{
					glVertex2d(0, getHeight() - 0.5);
					glVertex2d(getWidth() - (mod.hasCustomSettings() ? 2 : 0), getHeight() - 0.5);
				}
				glEnd();
			}
		}
		else {
			GL11.glColor4f(0.3f, 0.3f, 0.3f, 0.8f);
			if(settingContainer.modeMatchedSearch) {
				GL11.glColor4f(0.3f, 0.7f, 0.2f, 0.8f);
				glBegin(GL11.GL_LINES);
				{
					glVertex2d(0, getHeight() - 0.5);
					glVertex2d(getWidth(), getHeight() - 0.5);
				}
				glEnd();
			}
			GL11.glColor4f(0.1f, 0.1f, 0.1f, 0.8f);
		}

		GuiUtils.translate(this, true);
		GuiUtils.disableDefaults();
	}
	
	public void setSettingContainer(SettingContainer settingContainer) {
		this.settingContainer = settingContainer;
	}
	
	public SettingContainer getSettingContainer() {
		return settingContainer;
	}
	
	public Module getMod() {
		return mod;
	}
	
	public void setMod(Module mod) {
		this.mod = mod;
		this.preModToggled = mod.isToggled();
	}
	
	@Override
	public void onClicked(double x, double y, int button) {
		super.onClicked(x, y, button);
		if(button == 0) {
			mod.toggle();
			
		}
		if(button == 1) {
			settingContainer.setExtended(!settingContainer.extended);
			this.getParent().layoutChildren();
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
