
package Reality.Realii.mods.modules.movement;

import Reality.Realii.event.EventHandler;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.*;
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
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;


import net.minecraft.util.MathHelper;
import Reality.Realii.utils.cheats.player.Helper;
import net.minecraft.entity.player.InventoryPlayer;

public class NoSlow
        extends Module {
	public static int Stop;

	public static Mode mode = new Mode("Mode", "Mode", new String[]{"Vannila","Hypixel", "Ncp","Uncp","Mmc"}, "Vannila");
    public static Option noAttackSlow = new Option("NoAttackSlow", true);

    public NoSlow() {
        super("NoSlow", ModuleType.Movement);
        addValues(noAttackSlow,mode);
    }
    
    @Override
    public void onDisable() {
    	
    }


    
    

    public static int getItemIndex() {
        final InventoryPlayer inventoryPlayer = Helper.mc.thePlayer.inventory;
        return inventoryPlayer.currentItem;
    }
    @EventHandler
    private void onUpdate(EventPreUpdate e) {
   	 if (mode.getValue().equals("Uncp")) {
   		 if(mc.thePlayer.isBlocking()) {
   		mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem % 8 + 1));
   		mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
   		 }
   		 
   	 }
   	 
     	 if (mode.getValue().equals("Hypixel")) {
     		 if(mc.thePlayer.isUsingItem() && this.mc.thePlayer.getCurrentEquippedItem() != null&& this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemFood && PlayerUtils.isMoving()) {
     		// if(mc.thePlayer.isSwingInProgress) {
     			 //mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem % 8 + 1));
     		//	mc.thePlayer.sendQueue.addToSendQueue(new C08PacketBlockPlacemante(null));
     			// if (mc.thePlayer.ticksExisted % 3 == 0) {
     		  
     			// }
     		
     			 Helper.sendMessage("Eating Food");
          		// mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
     		 }

			 if(mc.thePlayer.isUsingItem() && this.mc.thePlayer.getCurrentEquippedItem() != null&& this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow && PlayerUtils.isMoving()) {
				 //Helper.sendMessage("Using Bow");
				// mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
				 //or start sneak
				mc.thePlayer.setSneaking(true);
			 }
			 if(mc.thePlayer.isBlocking() && PlayerUtils.isMoving()){
				 //Helper.sendMessage("Blocking Sword");
			 }
     		
     		// mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
     		// }
     	 }
    	
    	if (ArrayList2.Sufix.getValue().equals("On")) {
        	
            
    		this.setSuffix(mode.getModeAsString());
    	}
    		
    	 
    	 if (ArrayList2.Sufix.getValue().equals("Off")) {
         	
    	        
     		this.setSuffix("");
     	}
    	
    	
    		if(mode.getValue().equals("Mmc")) {
    			if(mc.thePlayer.isBlocking()) {
    				this.mc.thePlayer.setSprinting(false);
    			}
    			
    			if(!mc.thePlayer.isBlocking()) {
    				  if (mc.gameSettings.keyBindForward.isPressed()) {
    			        	
    		                this.mc.thePlayer.setSprinting(true);
    				  }
    				  
    				  if (mc.gameSettings.keyBindBack.isPressed()) {
  			        	
  		                this.mc.thePlayer.setSprinting(true);
  				  }
    				  
    				  if (mc.gameSettings.keyBindLeft.isPressed()) {
  			        	
  		                this.mc.thePlayer.setSprinting(true);
  				  }
    				  
    				  if (mc.gameSettings.keyBindRight.isPressed()) {
    			        	
    		                this.mc.thePlayer.setSprinting(true);
    				  }
    				
    			    
    			}
    	
    		}
    	
    	if(mode.getValue().equals("Ncp")) {
    		if(mc.thePlayer.isBlocking()) {
    	  mc.playerController.syncCurrentPlayItem();
    	 // mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, new BlockPos(mc.thePlayer), EnumFacing.UP));
    	  mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
    		}
    	}
    }
    
    
    
}

