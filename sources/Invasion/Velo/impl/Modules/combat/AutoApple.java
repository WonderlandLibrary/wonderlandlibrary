package Velo.impl.Modules.combat;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.api.Util.Other.Timer;
import Velo.impl.Event.EventSendPacket;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.BlockPos;

public class AutoApple extends Module {
	
	public AutoApple() {
		super("AutoApple", "AutoApple", Keyboard.KEY_NONE, Category.COMBAT);
	}
	
	 public NumberSetting delay = new NumberSetting("Delay", 1000, 0, 5000, 1);
	  private int slot;
	  
	  
	  public Timer time = new Timer();
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	

	@Override
	public void onUpdate(EventUpdate event) {
		final int prevSlot = mc.thePlayer.inventory.currentItem;
		 if (!(this.mc.currentScreen instanceof GuiChest || this.mc.currentScreen instanceof GuiChat|| this.mc.currentScreen instanceof GuiInventory)) {
	         for(int i = 0; i < 9; i++) {
	             final ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
	             if(itemStack != null && itemStack.getItem() instanceof ItemAppleGold) {
	            	   if (mc.thePlayer.getHealth() <= 13.0F) {
	       	    //    if(time.hasTimePassed((long) delay.getValDouble())) {
	            		
	                     mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem = i));
	      
	                     mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(BlockPos.ORIGIN, 255, mc.thePlayer.inventory.getCurrentItem(), 0, 0, 0));
	                     // mc.getNetHandler().addToSendQueueSilent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
	                    if(time.delay(100)) {
	                     mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem = prevSlot));
	                    time.reset2();
	                    }
	                     //  time.reset();
	       	        }
	           // }
	         }
	        }
		 }

		super.onUpdate(event);
	}
}
