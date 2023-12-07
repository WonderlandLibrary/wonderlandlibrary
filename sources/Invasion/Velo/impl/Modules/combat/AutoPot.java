package Velo.impl.Modules.combat;

import Velo.api.Module.Module;
import Velo.api.Util.Other.Timer;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;

public class AutoPot extends Module {
  
	public AutoPot() {
    super("AutoPot", "AutoPot", 0, Category.COMBAT);
    loadSettings(delay);
  }

  public NumberSetting delay = new NumberSetting("Throw Delay", 1000, 0, 5000, 1);
 // public Setting speed;
 // public Setting health;
 // public Setting amount;
  private int timer;
  private int slot;
  
  private float oldPitch;
  


  
  public Timer time = new Timer();
 
  @Override
public void onUpdate(EventUpdate event) {
      final int prevSlot = mc.thePlayer.inventory.currentItem;
      
		 if (!(this.mc.currentScreen instanceof GuiChest || this.mc.currentScreen instanceof GuiChat|| this.mc.currentScreen instanceof GuiInventory)) {

	         for(int i = 0; i < 9; i++) {
	             final ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
	             if(itemStack != null && itemStack.getItem() instanceof ItemPotion) {
	            	  final ItemPotion potion = (ItemPotion) itemStack.getItem();
	                  if (ItemPotion.isSplash(itemStack.getItemDamage())) {
	                      for (final Object o : potion.getEffects(itemStack)) {
	                          final PotionEffect effect = (PotionEffect) o;
	                          if (effect.getPotionID() == Potion.heal.id || effect.getPotionID() == Potion.regeneration.id) {
	            	 // if (this.mc.thePlayer.inventory.getStackInSlot(i).getItem().getItemStackDisplayName(this.mc.thePlayer.inventory.getStackInSlot(i)).equalsIgnoreCase("Splash Potion of Regeneration") || this.mc.thePlayer.inventory.getStackInSlot(i).getItem().getItemStackDisplayName(this.mc.thePlayer.inventory.getStackInSlot(i)).equalsIgnoreCase("Splash Potion of Healing")) {
	            	           if (mc.thePlayer.getHealth() <= 13.0F) {
	            	        	   if(!mc.thePlayer.isPotionActive(Potion.regeneration)) {
	            	        		   if(time.hasTimePassed((long) delay.getValue())) {
	            	        			   if(mc.thePlayer.onGround) {
	            	        			   oldPitch = mc.thePlayer.rotationPitch;
	            	        			   mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, 89, mc.thePlayer.onGround));
	            	        			   mc.getNetHandler().addToSendQueueSilent(new C09PacketHeldItemChange(i));
	            	        			   mc.getNetHandler().addToSendQueueSilent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
	            	        			   mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(prevSlot));
	            	        			   mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, oldPitch, mc.thePlayer.onGround));
	            	        			   time.reset();
	            	        		   }
	            	        		   }
	                      // }
	            	        	   }
	            	           }
	                          }else if (effect.getPotionID() == Potion.moveSpeed.id && effect.getPotionID() != Potion.jump.id) {
	                        		if(!mc.thePlayer.isPotionActive(Potion.moveSpeed))  {   
	       		            		 if(time.hasTimePassed((long) delay.getValue())) {
	       		            		  if(mc.thePlayer.onGround) {
	       			                   	   oldPitch = mc.thePlayer.rotationPitch;
	                      	        	      mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, 89, mc.thePlayer.onGround));
	                                          mc.getNetHandler().addToSendQueueSilent(new C09PacketHeldItemChange(i));
	                                          mc.getNetHandler().addToSendQueueSilent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
	                                          mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(prevSlot));
	                                          mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, oldPitch, mc.thePlayer.onGround));
	       			                       	  time.reset();
	       			                  }
	       		            		 }
	       		            		} 
	                          }
	                    }
	            	 }
	             }else {
	       
                   this.movePotsToHotbar();
	         
	             }
	         }
	        }
	super.onUpdate(event);
}
  
 

  

private void movePotsToHotbar() {
    boolean added = false;
    if (!isHotbarFull()) {
        for (int k = 0; k < mc.thePlayer.inventory.mainInventory.length; ++k) {
            if (k > 8 && !added) {
                final ItemStack itemStack = mc.thePlayer.inventory.mainInventory[k];
                if (itemStack != null && this.isValid(itemStack)) {
                    shiftClick(k);
                    added = true;
                }
            }
        }
    }
}
private boolean isValid(ItemStack itemStack) {
    if (itemStack.getItem() instanceof ItemPotion) {
    	//if(ItemPotion.isSplash(itemStack.getItemDamage())) {
    	   return true;
    	// }
    }
    return false;
}
public static void shiftClick(int slot) {
    Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, slot, 0, 1, Minecraft.getMinecraft().thePlayer);
}
public boolean isHotbarFull() {
    int count = 0;
    for (int k = 0; k < 9; ++k) {
        final ItemStack itemStack = mc.thePlayer.inventory.mainInventory[k];
        if (itemStack != null) {
            count++;
        }
    }
    return count == 8;
}
}

  
  
 
