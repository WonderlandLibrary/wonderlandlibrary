package me.aidanmees.trivia.gui;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import me.aidanmees.trivia.client.main.trivia;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class NotificationManager {
	
	private ArrayList<NotificationWindow> notes = new ArrayList<NotificationWindow>();
	
	private boolean adding = false;
	
	private int addingCount = 0;
	
	public NotificationManager() {
		
	}
	
	public void addNotification(Notification note) {
		
		if(trivia.getUIRenderer() == null) {
			return;
		}
		try {
			trivia.chatMessage(note.getText());
		}
		catch(Exception e) {
			try {
				if(trivia.devVersion) {
					trivia.chatMessage("rip");
				}
			}
			catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}

	public void update() {
		
		if(Minecraft.getMinecraft().thePlayer == null) {
			return;
		}
		for(NotificationWindow window : notes) {
			window.update();
		}
		Iterator<NotificationWindow> iter = notes.iterator();
		while(iter.hasNext()) {
			NotificationWindow window = iter.next();
			if(window.getLifeTime() > window.getNotification().getText().length() * 3) {
				iter.remove();
			}
		}
	}

	public void draw() {
		
		if(Minecraft.getMinecraft().thePlayer == null) {
			return;
		}
		int space = -20;
		if(adding) {
			adding = false;
			addingCount = -space;
		}
		if(addingCount > 0) {
			addingCount--;
		}
		
		GlStateManager.pushMatrix();
		//GlStateManager.scale(2d / Minecraft.getMinecraft().gameSettings.guiScale, 2d / Minecraft.getMinecraft().gameSettings.guiScale, 1);
		GlStateManager.translate(0, -21, 0);
		GlStateManager.translate(0, addingCount, 0);
		for(int i = notes.size() - 1; i > -1; i--) {
			NotificationWindow window = notes.get(i);
			GlStateManager.translate(0, space, 0);
			window.setPosition(trivia.getUIRenderer().getWidth() -  window.getWidth() - 3, trivia.getUIRenderer().getHeight());
			window.draw();
			
		}
		GlStateManager.popMatrix();
		GlStateManager.enableTexture2D();
		GL11.glColor4f(1f, 1f, 1f, 1f);
	}
	
}
