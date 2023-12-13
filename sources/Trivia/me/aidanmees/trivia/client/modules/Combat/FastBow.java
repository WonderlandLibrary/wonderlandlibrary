package me.aidanmees.trivia.client.modules.Combat;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.WaitTimer;
import me.aidanmees.trivia.client.events.UpdateEvent;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class FastBow extends Module {

	  public boolean isShooting;
	  WaitTimer t = new WaitTimer();
	public FastBow() {
		super("FastBow", Keyboard.KEY_NONE, Category.COMBAT, "Enables you to shoot without charging your bow first.");
	}

	@Override
	public void onDisable() {
		 mc.timer.timerSpeed = 1.0F;
		 mc.rightClickDelayTimer = 4;
		super.onDisable();
	}
	public static void sendPacketNoEvents(Packet packet)
	  {
	    mc.getNetHandler().getNetworkManager().sendPacketNoEvent(packet);
	  }
	private int GetBowSlot() {
		for (int i = 0; i < 36; i++) {
			if (this.mc.thePlayer.inventory.mainInventory[i] != null) {
				ItemStack is = this.mc.thePlayer.inventory.mainInventory[i];
				Item item = is.getItem();
				if (Item.getIdFromItem(item) == 261) {
					return i;
				}
			}
		}
		return -1;
	}
	
	
	
	
	
	

	@Override
	public void onUpdate(UpdateEvent event) {
		
if(currentMode.equalsIgnoreCase("Guardian")) {
	
	if (mc.thePlayer.inventory.getCurrentItem()!=null && mc.thePlayer.onGround&& !(mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemFood)) {
			if (mc.gameSettings.keyBindUseItem.pressed) {
				
			int PrevSlot = mc.thePlayer.inventory.currentItem;
            mc.thePlayer.inventory.currentItem = GetBowSlot();
            
            mc.rightClickDelayTimer = 3;
            
            mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 1);
            
            try {
	            if (mc.thePlayer.isUsingItem()
	                    && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow) {
	            	EntityLivingBase en;
	            
	    			
	                mc.timer.timerSpeed = 0.1f;
	                
	                mc.playerController.sendUseItem(mc.thePlayer,
	                        mc.theWorld,
	                        mc.thePlayer.getCurrentEquippedItem());
	                for (int i = 0; i < 20; ++i) {
	                    Thread.sleep(1l);
	                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(
	                            mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround));
	                }
	                mc.playerController
	                        .onStoppedUsingItem(mc.thePlayer);
	                mc.timer.timerSpeed = 1f;
	               
	            }
            
	        } catch (Exception e3) {
	            e3.printStackTrace();
	        
            }
            mc.thePlayer.inventory.currentItem = PrevSlot;


		
			}
}
}
		
		
		if (!currentMode.equalsIgnoreCase("Fast")) {
			return;
		}
		if (mc.thePlayer.inventory.getCurrentItem() == null) {
			return;
		}
		if (!mc.thePlayer.onGround) {
			// return;
		}
		if (mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow
				&& mc.gameSettings.keyBindUseItem.pressed) {
			mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());

			mc.thePlayer.inventory.getCurrentItem().getItem().onItemRightClick(mc.thePlayer.inventory.getCurrentItem(),
					mc.theWorld, mc.thePlayer);

			for (int i = 0; i < 20; i++) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
				
			}

			mc.getNetHandler().addToSendQueue(
					new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));

			mc.thePlayer.inventory.getCurrentItem().getItem()
					.onPlayerStoppedUsing(mc.thePlayer.inventory.getCurrentItem(), mc.theWorld, mc.thePlayer, 100000);
		}

		super.onUpdate();
	}
	private float[] getAngles(Entity target) {
        double xDiff = target.posX - mc.thePlayer.posX;
        double yDiff = target.posY - mc.thePlayer.posY;
        double zDiff = target.posZ - mc.thePlayer.posZ;
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)((- Math.atan2(target.posY + (double)target.getEyeHeight() / 0.0 - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
       
            if (yDiff > -0.2 && yDiff < 0.2) {
                pitch = (float)((- Math.atan2(target.posY + (double)target.getEyeHeight() / HitLocation.CHEST.getOffset() - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
            } else if (yDiff > -0.2) {
                pitch = (float)((- Math.atan2(target.posY + (double)target.getEyeHeight() / HitLocation.FEET.getOffset() - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
            } else if (yDiff < 0.3) {
                pitch = (float)((- Math.atan2(target.posY + (double)target.getEyeHeight() / HitLocation.HEAD.getOffset() - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
            }
        
        return new float[]{yaw, pitch};
    }

	@Override
	public void onRightClick() {
		
		if (!currentMode.equalsIgnoreCase("Single")) {
			return;
		}
		if (mc.thePlayer.inventory.getCurrentItem() == null) {
			return;
		}
		if (!mc.thePlayer.onGround) {
			return;
		}
		if (mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow
				&& mc.gameSettings.keyBindUseItem.pressed) {
			mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());

			mc.thePlayer.inventory.getCurrentItem().getItem().onItemRightClick(mc.thePlayer.inventory.getCurrentItem(),
					mc.theWorld, mc.thePlayer);

			for (int i = 0; i < 20; i++) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
			}

			mc.getNetHandler().addToSendQueue(
					new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));

			mc.thePlayer.inventory.getCurrentItem().getItem()
					.onPlayerStoppedUsing(mc.thePlayer.inventory.getCurrentItem(), mc.theWorld, mc.thePlayer, 10);
		}

		super.onRightClick();
	}

	private static enum HitLocation {
        AUTO(0.0),
        HEAD(1.0),
        CHEST(1.5),
        FEET(3.5);
        
        private double offset;

        private HitLocation(double offset) {
            this.offset = offset;
        }

        public double getOffset() {
            return this.offset;
        }
        
    }
	@Override
	public String getAddonText() {
		return this.currentMode;
	}

	@Override
	public String[] getModes() {
		return new String[] { "Single", "Fast" , "Guardian"};
	}

}
