package epsilon.modules.movement;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventMotion;
import epsilon.events.listeners.EventUpdate;
import epsilon.modules.Module;
import epsilon.settings.setting.BooleanSetting;
import epsilon.settings.setting.ModeSetting;
import epsilon.util.MoveUtil;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;

public class InvMove extends Module{

	private final KeyBinding[] affectedBindings = new KeyBinding[]{
            mc.gameSettings.keyBindForward,
            mc.gameSettings.keyBindBack,
            mc.gameSettings.keyBindRight,
            mc.gameSettings.keyBindLeft,
            mc.gameSettings.keyBindJump,
    };
	
	public ModeSetting packet = new ModeSetting("Bypass", "Vanilla", "Vanilla", "Vulcan","PacketSpam", "Combination");
	public BooleanSetting sprint = new BooleanSetting("Sprint", true);
	
	public InvMove() {
		super("InvMove", Keyboard.KEY_NONE, Category.MOVEMENT, "Allows you to move in GUI's");
		this.addSettings(packet, sprint);
	}

	
	public void onEvent(Event e) {
		if(e instanceof EventMotion) {
			if(e.isPre()) {
				MoveUtil move = new MoveUtil();
				
				if(mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) {
					
					if(move.canSprint() && packet.getMode()!="Vulcan") {
						mc.thePlayer.setSprinting(true);
					}
					
					for (final KeyBinding a : affectedBindings) {
		                a.setKeyPressed(GameSettings.isKeyDown(a));
		            }
					if(packet.getMode()=="Vulcan") {
						mc.thePlayer.motionX *= 0.6;
	                    mc.thePlayer.motionZ *= 0.6;
					}
					
				}
			}
		}
		if(e instanceof EventMotion) {
			if(e.isPre()) {
				if(mc.currentScreen != null) {
					switch(packet.getMode()) {
					
					case "Vanilla":
						
						break;
					
					case "PacketSpam":
						
						mc.getNetHandler().addToSendQueueWithoutEvent(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
						
						break;
						
					case "Combination":
						
						mc.getNetHandler().addToSendQueueWithoutEvent(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
						
						break;
					}
				}
			}
			if(e.isPost()) {
				if(mc.currentScreen != null) {
					switch(packet.getMode()) {
						
					case "Combination":
						//I dont know why I made this a mode its not gunna do anything
						mc.getNetHandler().addToSendQueueWithoutEvent(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
						
						break;
					}
				}
			}
		}
	}
	
	
}
