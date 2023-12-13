package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.events.PacketEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.gui.Level;
import me.robbanrobbin.jigsaw.gui.Notification;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.server.SPacketResourcePackSend;

public class SpoofResourcepack extends Module {

	public SpoofResourcepack() {
		super("SpoofResourcepack", Keyboard.KEY_NONE, Category.EXPLOITS, "Accepts all resourcepacks server-side while not accepting client-side");
	}
	
	@Override
	public void onPacketRecieved(PacketEvent event) {
		Packet packetIn = event.getPacket();
		if(packetIn instanceof SPacketResourcePackSend) {
			event.cancel();
			Jigsaw.getNotificationManager().addNotification(new Notification(Level.INFO, "SoofResourcepack intercepted a resourcepack request!"));
			sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.ACCEPTED));
		}
		super.onPacketRecieved(event);
	}

}
