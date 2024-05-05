package me.aidanmees.trivia.client.gui.tab;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLSync;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.tools.timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class TabGuiTitle extends TabGuiItem {
	
	public String title = null;
	static int  r;
	static int g;
	static int b;
	static int state = 0;
	static int a;
	int red, green, blue;
	static timer timer = new timer();
	
	public TabGuiContainer nextContainer;
	
	public boolean hasnextContainer() {
		return nextContainer != null;
	}

	@Override
	public int getWidth() {
		return TabGuiItem.fontRenderer.getStringWidth(title) + 10;
	}

	@Override
	public int getHeigth() {
		return TabGuiItem.fontRenderer.FONT_HEIGHT - 1;
	}
	
	@Override
	public void update() {
		super.update();
		if(nextContainer == null) {
			return;
		}
		nextContainer.x = this.x + this.parent.width + 2;
		nextContainer.y = this.y;
		nextContainer.update();
	}
	
	@Override
	public void setValues() {
		super.setValues();
		if(nextContainer != null) {
			nextContainer.setValues();
			return;
		}
	}
	
	@Override
	public void render() {
		
		  
        
		super.render();
		int drawWidth = parent.width - 1;
		if(TabGui.novitex) {
			drawWidth = width - 10;
			
		}
		GlStateManager.enableAlpha();
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableCull();
		
		
		
	
		
		GL11.glColor4f(0.2f, 0.2f, 0.2f, 0.1f);
		if(selected) {
			GL11.glColor4f(0, 1, 0, 0.96f);
			
		}
		if(TabGui.novitex) {
			GL11.glColor4f(0.2f, 0.9f, 0.2f, 0.5f);
			if(selected) {
				GL11.glColor4f(0, 1, 0, 1);
				
			}
		}
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex2d(x - 1, y-1);
			GL11.glVertex2d(x - 1, y + height);
			GL11.glVertex2d(x + drawWidth, y + height);
			GL11.glVertex2d(x + drawWidth, y-1);
		}
		GL11.glEnd();
		GL11.glColor4f(0f, 0f, 0f, 0f);
		GL11.glLineWidth(0.1f);
		if(!TabGui.novitex) {
			if(selected) {
				GL11.glTranslated(0, 0, 10);
				GL11.glBegin(GL11.GL_LINE_LOOP);
				{
					GL11.glVertex2d(x, y);
					GL11.glVertex2d(x, y + height);
					GL11.glVertex2d(x + drawWidth, y + height);
					GL11.glVertex2d(x + drawWidth, y );
				}
				GL11.glEnd();
				GL11.glTranslated(0, 0, -10);
			}
		}
		GlStateManager.enableBlend();
		GlStateManager.enableTexture2D();
		GL11.glColor4f(1f, 1f, 1f, 1f);
		boolean toggled = true;
		if(trivia.isModuleName(title) && !trivia.getModuleByName(title).isToggled()) {
			toggled = false;
		}
		if(TabGui.novitex) {
			
			TabGuiItem.fontRenderer.drawString(title, x + 5, y - 1, toggled ? 0xffffff : 0xbfbfbf);
		}
		else {
			TabGuiItem.fontRenderer.drawString(title, x + 1, y - 1, toggled ? 0xffffff : 0xbfbfbf);
		}
		
		if(maximised && hasnextContainer()) {
			nextContainer.render();
		}
		if(maximised && !hasnextContainer()) {
			trivia.getModuleByName(this.title).toggle();
			maximised = false;
		}
	}
	
}
