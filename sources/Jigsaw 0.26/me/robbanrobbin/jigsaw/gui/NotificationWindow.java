package me.robbanrobbin.jigsaw.gui;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.font.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class NotificationWindow {
	
	public static FontRenderer fontRenderer = Fonts.font18;
	
	public static FontRenderer fontRenderer2 = Fonts.font18bold;
	
	private int x;
	private int y;
	
	private int width;
	private int height;
	
	private float time = 0;
	private float preTime = 0;
	
	private Notification notification;
	
	private int stringWidth = 0;
	
	public NotificationWindow(Notification notification) {
		this.notification = notification;
		stringWidth = fontRenderer.getStringWidth(this.notification.getText());
		if(this.getNotification().getLevel().getHeader().isEmpty()) {
			this.width = stringWidth + 11;
		}
		else {
			this.width = stringWidth + 15 + fontRenderer2.getStringWidth(this.getNotification().getLevel().getHeader());
		}
		
		this.height = 16;
	}
	
	public void update() {
		preTime = time;
		time += 1f;
		if(time < 0f) {
			time = 0f;
		}
	}
	
	public Notification getNotification() {
		return notification;
	}
	
	public void draw() {
		float time = (this.preTime + (this.time - this.preTime) * Minecraft.getMinecraft().timer.renderPartialTicks);
		float maxTime = getNotification().getText().length() * 3;
		float timePercent = (time / maxTime);
		glEnable(GL_BLEND);
		glDisable(GL_CULL_FACE);
		glDisable(GL_TEXTURE_2D);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		float alpha = 1;
		
		if(timePercent < 0.1f) {
			alpha = timePercent * 10f;
		}
		
		if(timePercent > 0.9f) {
			alpha = -(timePercent - 1f) * 10f;
		}
		
		GL11.glColor4f(0.1f, 0.1f, 0.1f, alpha);
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex2d(x, y);
			GL11.glVertex2d(x + width, y);
			GL11.glVertex2d(x + width, y + height);
			GL11.glVertex2d(x, y + height);
		}
		GL11.glEnd();
		
		GL11.glLineWidth(6f);
		GL11.glColor4f(0.8f, 0.1f, 0.1f, alpha);
		GL11.glBegin(GL11.GL_LINES);
		{
			float width = timePercent * this.width;
			
			GL11.glVertex2d(x, y + height - 2);
			GL11.glVertex2d(x + width, y + height - 2);
		}
		GL11.glEnd();
		
		if(alpha > 1) {
			alpha = 1;
		}
		if(alpha < 0) {
			alpha = 0;
		}
		Level l = getNotification().getLevel();
		glEnable(GL_CULL_FACE);
		if(alpha > 0.5f) {
			if(l == Level.ERROR) {
				fontRenderer2.drawStringWithShadow(this.getNotification().getLevel().getHeader(), x + 5, y + 3f, 0xffffffff);
			}
			if(l == Level.INFO) {
				fontRenderer2.drawStringWithShadow(this.getNotification().getLevel().getHeader(), x + 5, y + 3f, 0xffffffff);
			}
			if(l == Level.WARNING) {
				fontRenderer2.drawStringWithShadow(this.getNotification().getLevel().getHeader(), x + 5, y + 3f, 0xffffffff);
			}
			
			fontRenderer.drawStringWithShadow(notification.getText(), x + -(stringWidth - width) - 5, y + 3f, 0xffffffff);
		}
		
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public float getLifeTime() {
		return time;
	}
	
}
