package epsilon.modules.skyblock;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventUpdate;
import epsilon.modules.Module;
import epsilon.settings.setting.ModeSetting;
import epsilon.util.MoveUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class SkyblockTest extends Module {

	public ModeSetting mode = new ModeSetting ("DisablerType", "AoTE", "AoTE","Grapple");
	
	private int realSlot, spoofedSlot;
	
	public SkyblockTest() {
		super("SkyblockDisabler", Keyboard.KEY_NONE, Category.EXPLOIT, "");
		this.addSettings(mode);
	}
	
	public void onEvent(Event e) {
		
		MoveUtil move = new MoveUtil();
		if(e instanceof EventUpdate) {
			
			
			/*
			 
			realSlot = mc.thePlayer.inventory.currentItem;
			
			if(mc.getNetHandler().doneLoadingTerrain && mc.thePlayer.ticksExisted>20) {
				switch (mode.getMode()) {
				case "AoTE":
					
					if(getAspect() == -1)
						return;
					
					spoofedSlot = getAspect();
					
					if(spoofedSlot!=getAspect()) {
						switchSlot(getAspect());
						spoofedSlot = getAspect();
					}	
					
					mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
					
					Epsilon.addChatMessage("AoTE Found");
					
					if(spoofedSlot!=realSlot) {
						switchSlot(realSlot);
						spoofedSlot = getAspect();
					}	
					
					break;
					
				case "Grapple":
					
					if(getGrapple() == -1)
						return;
					

					spoofedSlot = getGrapple();
					
					move.place(new BlockPos(0.5f, 0.5f, 0.5f), 0, mc.thePlayer.getHeldItem(), 0.5f, 0.5f, 0.5f);
					mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
					
					//mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0.5f, 0.5f, 0.5f), EnumFacing.DOWN));
					
					
					
					Epsilon.addChatMessage("Hook Found");
						
					
					break;
					
				} 
		
			}	*/
			
			
		}
	}
	
	private int getAspect() {
		for(int i = 36; i < 45; ++i) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && stack.getDisplayName().contains("Aspect") && stack.getItem() instanceof ItemSword) {
                return i - 36;
            }
        }
        return -1;
		
	}
	
	private int getGrapple() {
		for(int i = 36; i < 45; ++i) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && stack.getDisplayName().contains("Grappling") && stack.getItem() instanceof ItemFishingRod) {
                return i - 36;
            }
        }
        return -1;
		
	}
	
	private void switchSlot(int i) {
		mc.getNetHandler().addToSendQueueWithoutEvent(new C09PacketHeldItemChange(i));
	}

}
