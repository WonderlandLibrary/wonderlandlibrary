package epsilon.modules.combat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventMotion;
import epsilon.events.listeners.EventUpdate;
import epsilon.modules.Module.Category;
import epsilon.modules.Module;
import epsilon.settings.setting.BooleanSetting;
import epsilon.settings.setting.ModeSetting;
import epsilon.settings.setting.NumberSetting;
import epsilon.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandom.Item;
public class AutoPot extends Module {

	public NumberSetting h = new NumberSetting ("HealthMin", 10, 1, 20, 1);
	public NumberSetting hw = new NumberSetting("HealthCritical", 10, 1, 20, 1);
	public ModeSetting mode = new ModeSetting("AutoGapMode", "Instant", "Instant", "Packetless", "NCP");
	public NumberSetting hcd  =new NumberSetting("CriticalHealthDelay", 250, 25, 1500, 50);
	public NumberSetting delay = new NumberSetting ("Delay", 500, 25, 1500, 50);
	public BooleanSetting jump = new BooleanSetting("Jump", false);
	int firstInv;
    boolean needSplash, switchSlot;
    
    public Timer timer = new Timer();
	private int serverSlot;

	
	public AutoPot(){
		super("AutoHeal", Keyboard.KEY_NONE, Category.COMBAT, "Low on health? NO!");
		this.addSettings(mode,h, delay, hw, hcd,jump);
	}
	
	
	public void onEnable(){
		firstInv = mc.thePlayer.inventory.currentItem;
	}
	
	public void onDisable() {
		needSplash = false;
		switchSlot = false;
	}
	
	
	public void onEvent(Event e){
		if(e instanceof EventUpdate){
			if(e.isPre() && !mc.thePlayer.isDead) {

				/*if(mc.thePlayer.getHealth()<hw.getValue()) {
				for(int i = 0; i < hw.getValue()-mc.thePlayer.getHealth(); i++) {
					mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer(false));
					mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer(false));
					if(mc.thePlayer.getHealth()<2)
					mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer(false));
					
				}
				}*/
				
				if(mc.thePlayer.getHealth()<= h.getValue() && getGap()==-1 && getSoup()!=-1) {
					firstInv = mc.thePlayer.inventory.currentItem;
					
					if (mc.thePlayer.inventory.currentItem != getSoup()) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(getSoup()));
                        serverSlot = getSoup();
					} 
					
                    mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(getSoup())));
                    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    if (mc.thePlayer.inventory.currentItem != getSoup()) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(firstInv));
                    serverSlot = firstInv;
                    }
				}
				switch(mode.getMode()) {
				
				
				case"Instant":
					if(mc.thePlayer.getHealth()<= h.getValue() && mc.thePlayer.getHealth()>0&&getGap()!=-1&&timer.hasTimeElapsed((long) delay.getValue(), true)) {
						firstInv = mc.thePlayer.inventory.currentItem;
						if (mc.thePlayer.inventory.currentItem != getGap()) {
	                        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(getGap()));
	                        serverSlot = getGap();
						} 
	                    mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(getGap())));
	                    for (int i = 0; i <= 31; i++) mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
	                    
	                    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
	                    if (mc.thePlayer.inventory.currentItem != getGap()) {
	                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(firstInv));
	                    serverSlot = firstInv;
	                    }
					}
					break;
					
				case"Packetless":
					
					if(mc.thePlayer.getHealth()<= h.getValue() && getGap()!= -1) {
						
						firstInv = mc.thePlayer.inventory.currentItem;

						if(serverSlot!=getGap()) {
							mc.getNetHandler().addToSendQueueWithoutEvent(new C09PacketHeldItemChange(getGap()));
							serverSlot = getGap();

							Epsilon.addChatMessage(serverSlot);
						}
						
						mc.getNetHandler().addToSendQueueWithoutEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(getGap())));


						
					}
					
					break;
					
				}	
				
			}
			if(e.isPost() && !mc.thePlayer.isDead) {
				if(mc.thePlayer.getHealth()<= h.getValue() && getGap()!= -1) {
					switch(mode.getMode()) {
					case "Packetless":
						Epsilon.addChatMessage(serverSlot);
						mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
						if(serverSlot!=firstInv) {
							mc.getNetHandler().addToSendQueueWithoutEvent(new C09PacketHeldItemChange(firstInv));
							serverSlot = firstInv;

							Epsilon.addChatMessage(serverSlot);
						}
						
						break;
					}
				}	
			}
		}
	}	
	
	private int getGap() {
        for(int i = 36; i < 45; ++i) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && stack.getDisplayName().contains("Golden") && stack.getItem() instanceof ItemFood) {
                return i - 36;
            }
        }
        return -1;
    }

	private int getSoup() {
        for(int i = 36; i < 45; ++i) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && stack.getItem() instanceof ItemSoup && stack.getDisplayName().contains("Mushroom")) {
                return i - 36;
            }
        }
        return -1;
    }
	
	
	private int getHead() {
        for(int i = 36; i < 45; ++i) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && stack.getDisplayName().contains("Golden") && stack.getItem() instanceof ItemSkull) {
                return i - 36;
            }
        }
        return -1;
    }
	
	
	
}
