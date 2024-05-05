package epsilon.modules.combat;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventUpdate;
import epsilon.modules.Module;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;

public class ArmorBreaker extends Module {

	private int ogSlot;
	private int currentSlotSpoofed;

	
	public ArmorBreaker() {
		super("ArmorBreaker", Keyboard.KEY_NONE, Category.COMBAT, "Breaks an opponents armor faster");
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(KillAura.target!=null) {
					ogSlot = mc.thePlayer.inventory.currentItem;
					mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
					mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(KillAura.target, Action.ATTACK));
					for(int i = ogSlot; i<9; i++) {
						mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(i));
						mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
						mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(KillAura.target, Action.ATTACK));
						currentSlotSpoofed = i;
					
					}	
					if(currentSlotSpoofed != ogSlot)
						mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(ogSlot));
			}
			
		
		}
	}

}
