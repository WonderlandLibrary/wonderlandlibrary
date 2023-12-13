package dev.monsoon.module.implementation.render;

import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventPacket;
import dev.monsoon.module.base.Module;
import dev.monsoon.notification.Notification;
import dev.monsoon.notification.NotificationManager;
import dev.monsoon.notification.NotificationType;
import net.minecraft.network.play.server.S45PacketTitle;
import org.lwjgl.input.Keyboard;
import dev.monsoon.module.enums.Category;

public class NoTitle extends Module {

	public NoTitle() {
		super("NoTitle", Keyboard.KEY_NONE, Category.RENDER);
	}

	public void onEvent(Event e) {
		if(e instanceof EventPacket) {
			if (((EventPacket<?>) e).getPacket() instanceof S45PacketTitle) {
				if(((EventPacket<?>) e).getPacket() != null) {
					String message = ((S45PacketTitle) ((EventPacket<?>) e).getPacket()).func_179805_b().getUnformattedText();

					if (message != null) {
						NotificationManager.show(new Notification(NotificationType.INFO, "Title", message, 3));
					}
					e.setCancelled(true);
				}
			}
		}
	}
	
}
