package info.sigmaclient.module.impl.player;

import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.module.data.Setting;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import org.lwjgl.input.Keyboard;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import net.minecraft.client.gui.GuiChat;

public class InventoryWalk extends Module {

	private String CARRY = "CARRY";

	public InventoryWalk(ModuleData data) {
		super(data);
		settings.put(CARRY, new Setting(CARRY, false, "Carry items in crafting slots."));
	}

	@Override
	@RegisterEvent(events = { EventPacket.class, EventTick.class })
	public void onEvent(Event event) {
		if (mc.currentScreen instanceof GuiChat) {
			return;
		}
		if(event instanceof EventTick) {
			if (mc.currentScreen != null) {
				if (Keyboard.isKeyDown(200)) {
					mc.thePlayer.rotationPitch -= 1;
				}
				if (Keyboard.isKeyDown(208)) {
					mc.thePlayer.rotationPitch += 1;
				}
				if (Keyboard.isKeyDown(203)) {
					mc.thePlayer.rotationYaw -= 3;
				}
				if (Keyboard.isKeyDown(205)) {
					mc.thePlayer.rotationYaw += 3;
				}
			}
		}
		if(event instanceof EventPacket && ((Boolean)settings.get(CARRY).getValue())) {
			EventPacket ep = (EventPacket)event;
			if(ep.isOutgoing() && ep.getPacket() instanceof C0DPacketCloseWindow) {
				ep.setCancelled(true);
			}
		}
	}
}
