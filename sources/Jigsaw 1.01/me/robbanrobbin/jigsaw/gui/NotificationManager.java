package me.robbanrobbin.jigsaw.gui;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class NotificationManager {

	private ArrayList<NotificationWindow> notes = new ArrayList<NotificationWindow>();

	private boolean adding = false;

	private int addingCount = 0;

	public NotificationManager() {

	}

	public void addNotification(Notification note) {
		if (Jigsaw.ghostMode) {
			return;
		}
		if (Jigsaw.getUIManager() == null) {
			return;
		}
		try {
			NotificationWindow window = new NotificationWindow(note);
			window.setPosition(Jigsaw.getUIManager().getWidth() - window.getWidth() - 3,
					Jigsaw.getUIManager().getHeight());
			notes.add(window);
			adding = true;
		} catch (Exception e) {
			try {
				if (Jigsaw.devVersion) {
					NotificationWindow window = new NotificationWindow(new Notification(Level.ERROR,
							"Error displaying note, please report to the creator of the client along with this message: "
									+ e.getMessage()));
					window.setPosition(Jigsaw.getUIManager().getWidth() - window.getWidth() - 3,
							Jigsaw.getUIManager().getHeight());
					notes.add(window);
					adding = true;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

	}

	public void update() {
		if (Jigsaw.ghostMode) {
			return;
		}
		if (Minecraft.getMinecraft().player == null) {
			return;
		}
		for (NotificationWindow window : notes) {
			window.update();
		}
		Iterator<NotificationWindow> iter = notes.iterator();
		while (iter.hasNext()) {
			NotificationWindow window = iter.next();
			if (window.getLifeTime() > window.getNotification().getText().length() * 3) {
				iter.remove();
			}
		}
	}

	public void draw() {
		if (Jigsaw.ghostMode) {
			return;
		}
		if (Minecraft.getMinecraft().player == null) {
			return;
		}
		int space = -23;
		if (adding) {
			adding = false;
			addingCount = -space;
		}
		if (addingCount > 0) {
			addingCount--;
		}

		GlStateManager.pushMatrix();
		
		for (int i = 0; i < this.notes.size(); i++) {
			NotificationWindow window = notes.get(this.notes.size() - 1 - i);
			window.setPosition(Jigsaw.getUIManager().getWidth() - window.getWidth() - Jigsaw.getUIManager().getModuleList().getTargetWindowWidth(),
					Jigsaw.getUIManager().getHeight() + space + addingCount);
			window.draw(i);
		}
		GlStateManager.popMatrix();
		GlStateManager.enableTexture2D();
		GL11.glColor4f(1f, 1f, 1f, 1f);
	}

}
