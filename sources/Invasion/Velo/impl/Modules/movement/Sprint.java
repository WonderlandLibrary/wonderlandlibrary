package Velo.impl.Modules.movement;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.ModeSetting;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class Sprint extends Module {
	
	public static ModeSetting mode = new ModeSetting("Mode", "Legit", "Legit", "Omni");
	
	public Sprint() {
		super("Sprint", "Sprint", Keyboard.KEY_NONE, Category.MOVEMENT);
		this.loadSettings(mode);
	}
	
	public void onUpdate(EventUpdate event) {
		if(mode.equalsIgnorecase("Legit")) {
			if(mc.gameSettings.keyBindForward.pressed && !mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isUsingItem()) {
				mc.thePlayer.setSprinting(true);
			}
		}
		if(mode.equalsIgnorecase("Omni")) {
			if(mc.thePlayer.isMoving()) {
				mc.thePlayer.setSprinting(true);
				mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
			}
		}
	}
}
