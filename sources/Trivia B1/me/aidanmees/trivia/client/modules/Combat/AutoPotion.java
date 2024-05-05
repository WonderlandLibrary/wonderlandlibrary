package me.aidanmees.trivia.client.modules.Combat;

import java.util.concurrent.TimeUnit;

import org.lwjgl.input.Keyboard;

import com.google.common.base.Stopwatch;

import me.aidanmees.trivia.client.events.UpdateEvent;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.settings.ClientSettings;
import me.aidanmees.trivia.gui.custom.clickgui.CheckBtnSetting;
import me.aidanmees.trivia.gui.custom.clickgui.ModSetting;
import me.aidanmees.trivia.gui.custom.clickgui.SliderSetting;
import me.aidanmees.trivia.gui.custom.clickgui.ValueFormat;
import me.aidanmees.trivia.module.Module;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class AutoPotion extends Module {
	
    private int slot;
    private boolean healing;
    private Stopwatch timer;
    private int packetSlot;
    private boolean packetSoup;
    private ItemStack itemStack;

	    
	  

	public AutoPotion() {
		super("AutoPotion", Keyboard.KEY_NONE, Category.COMBAT, "Throws potions automatically.");
		
        this.slot = -1;
        this.healing = false;
        this.timer = Stopwatch.createUnstarted();
        this.packetSlot = -1;
        this.packetSoup = false;
        this.itemStack = null;

	}
	
	
	 @Override
	    public void onDisable() {
		 
	        super.onDisable();
	    }

	    @Override
	    public void onEnable() {
	    	 this.timer.reset();
	    	this.timer.start();


	    	 
	        super.onEnable();
	    }
	    
	    
	    @Override
		public void onPacketRecieved(AbstractPacket packetIn) {
			if (packetIn instanceof S2FPacketSetSlot  && this.packetSlot != -1 && this.timer.elapsed(TimeUnit.MILLISECONDS) < 1000L) {


                final S2FPacketSetSlot packet = (S2FPacketSetSlot)packetIn;
                if (packet.func_149173_d() == 44) {
                    packet.setItem(this.itemStack);
                }
                this.packetSlot = -1;
            
       
			}
			super.onPacketRecieved(packetIn);
		}

	  
	    @Override
	    public void onUpdate(UpdateEvent event) {
	    	if (!this.shouldHeal()) {
                return;
            }
            if (this.slot == -1) {
                return;
            }
           
            final boolean up = ClientSettings.upwards &&mc.thePlayer.onGround;
            if (mc.thePlayer.inventory.mainInventory[this.slot].getItem() instanceof ItemPotion) {
                if (up) {
                   mc.thePlayer.jump();
                }
                event.pitch = (up ? -90.0f : 90.0f);
            }
           this.healing = true;
        
      
            if (!this.healing) {
                return;
            }
            if (!this.shouldHeal()) {
                return;
            }
            if (this.slot == -1) {
                return;
            }
            if (mc.thePlayer.inventory.mainInventory[8] != null) {
               this.itemStack =mc.thePlayer.inventory.mainInventory[8].copy();
            }
           this.packetSoup = (mc.thePlayer.inventory.mainInventory[this.slot].getItem() == Items.mushroom_stew);
           this.packetSlot =this.slot;
            if (this.slot < 9) {
               mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.slot));
               mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                if (ClientSettings.drop &&this.packetSoup) {
                   mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ALL_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
                }
               mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            }
            else {
               mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(8));
                mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId,this.slot, 8, 2, mc.thePlayer);
                mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                if (ClientSettings.drop &&this.packetSoup) {
                   mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ALL_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
                }
                mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId,this.packetSlot, 8, 2, mc.thePlayer);
               mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            }
           this.timer.reset();
           this.timer.start();
           
	        int count = 0;
	        this.slot = -1;
	        for (int i = 0; i <= 35; ++i) {
	            if (this.mc.thePlayer.inventory.mainInventory[i] != null) {
	                if (mc.thePlayer.inventory.mainInventory[i].getItem() != null) {
	                    final ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
	                    final Item item = stack.getItem();
	                    if (item instanceof ItemPotion && ClientSettings.potions) {
	                        final ItemPotion potion = (ItemPotion)item;
	                        if (stack.getMetadata() == 16421) {
	                            this.slot = i;
	                            ++count;
	                        }
	                    }
	                    else if (item instanceof ItemSoup && ClientSettings.soup) {
	                        this.slot = i;
	                        ++count;
	                    }
	                }
	            }
	        }
	       super.onUpdate(event);
	    }
	    
	    private boolean shouldHeal() {
	        return mc.thePlayer.getHealth() <= ClientSettings.healthAutoPot * 2.0 && this.timer.elapsed(TimeUnit.MILLISECONDS) >= ClientSettings.delayAutoPot;
	    }


	    @Override
	    public void onLateUpdate() {
	    	
	        super.onLateUpdate();
	    }

	  
	    @Override
		public ModSetting[] getModSettings() {

	    	SliderSetting<Number> slider1 = new SliderSetting<Number>("Health", ClientSettings.healthAutoPot, 0.1, 10, 0.0, ValueFormat.DECIMAL);
	    	SliderSetting<Number> slider2 = new SliderSetting<Number>("Delay", ClientSettings.delayAutoPot, 50, 2000, 0.0, ValueFormat.DECIMAL);
	    	
	    	
	    	
	    	CheckBtnSetting box1 = new CheckBtnSetting("Potions", "potions");
	    	
	    	CheckBtnSetting box3 = new CheckBtnSetting("Soup", "soup");
			
			
	    	CheckBtnSetting box2 = new CheckBtnSetting("Upwards", "upwards");
			
	    	
	    	CheckBtnSetting box4 = new CheckBtnSetting("Drop soup", "drop");
			
	    	
	    	
	    	
			
			
			
			return new ModSetting[] { slider1 , slider2,box1,box3,box2,box4};
		}
}