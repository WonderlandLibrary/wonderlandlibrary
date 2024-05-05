
package Reality.Realii.mods.modules.movement;

import Reality.Realii.event.EventHandler;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.entity.player.InventoryPlayer;
import Reality.Realii.event.events.world.EventPacketSend;
import Reality.Realii.event.events.world.EventPostUpdate;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.event.value.Option;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.combat.Killaura;
import Reality.Realii.mods.modules.render.ArrayList2;
import Reality.Realii.utils.cheats.player.PlayerUtils;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;


import net.minecraft.util.MathHelper;
import Reality.Realii.utils.cheats.player.Helper;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.entity.player.InventoryPlayer;

public class HighJump
        extends Module {
	

	public static Mode mode = new Mode("Mode", "Mode", new String[]{"Vannila"}, "Vannila");
 

    public HighJump() {
        super("HighJump", ModuleType.Movement);
        addValues(mode);
    }
    
 
    @EventHandler
    private void onUpdate(EventPreUpdate e) {
   	 if (mode.getValue().equals("Vannila")) {
   		if(mc.thePlayer.onGround) {
   			mc.thePlayer.motionY = 4;
   		}
   		 
   	 }
   	 
   	 
    	
    	if (ArrayList2.Sufix.getValue().equals("On")) {
        	
            
    		this.setSuffix(mode.getModeAsString());
    	}
    		
    	 
    	 if (ArrayList2.Sufix.getValue().equals("Off")) {
         	
    	        
     		this.setSuffix("");
     	}
    	
    	
    }
    
    
    
}

