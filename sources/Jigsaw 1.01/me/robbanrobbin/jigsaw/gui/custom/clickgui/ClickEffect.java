package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class ClickEffect {
	
	public int posX;
	
	public int posY;
	
	public double time;
	
	public double preTime;
	
	float alpha;
	
	public ClickEffect(int posX, int posY) {
		
		this.posX = posX;
		this.posY = posY;
		
		alpha = 0.25f;
		
		time = 0.2;
		
	}
	
	public void update() {
		
		preTime = time;
		
		time += 1.3;
		
		if(time > 2) {
			alpha -= 0.03;
			if(alpha < 0f) {
				alpha = 0;
			}
		}
		
	}
	
	public void render() {
		
		GL11.glPushMatrix();
		
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0f);
		
		float alpha = this.alpha;
		
		RenderTools.drawFilledCircle(posX, posY, (time + (time - preTime) * Minecraft.getMinecraft().timer.renderPartialTicks) * 10d, new Color(0.0f, 0.0f, 0.0f, alpha));
		
		GL11.glPopMatrix();
		
	}
	
}
