package Velo.impl.Modules.combat;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.impl.Event.EventSendPacket;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class WTap extends Module {
	
	public WTap() {
		super("WTap", "WTap", Keyboard.KEY_NONE, Category.COMBAT);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	public void onEventSendPacket(EventSendPacket event) {
		if(event.getPacket() instanceof C02PacketUseEntity) {
            C02PacketUseEntity packet = (C02PacketUseEntity) event.getPacket();
            if(packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
            	boolean isthiskidsprinting = mc.thePlayer.isSprinting();
            	mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
	            mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
	            mc.thePlayer.setSprinting(isthiskidsprinting);
            }
		}
	}
}
