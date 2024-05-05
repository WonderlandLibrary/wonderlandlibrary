package Reality.Realii.mods.modules.player;

import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.misc.EventChat;
import Reality.Realii.event.events.world.EventPacketSend;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.managers.FriendManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.render.ArrayList2;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

import java.awt.Color;

public class AntiDesync
extends Module {
	private int slot;
    public AntiDesync(){
        super("AntiDesync", ModuleType.Player);
     
    }

  
    @EventHandler
    public void onupdate(EventPreUpdate e) {
        if (slot != mc.thePlayer.inventory.currentItem && slot != -1) {
        //(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        	 mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        }
    	
    }
    @EventHandler
    public void onsend(EventPacketSend e) {
    	  if (e.getPacket() instanceof C09PacketHeldItemChange) {
              slot = ((C09PacketHeldItemChange) e.getPacket()).getSlotId();
          }
    	
    }
    

}

