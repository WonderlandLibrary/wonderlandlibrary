package artemis.mods.World;

import artemis.Category;
import artemis.module;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class Tower extends module{
	int d = 0;
	public Tower() {
		super("Tower", "Places blocks underneh you", 0, 0, Category.PLAYER);
	}
	
	
	public void onDisable() {
		mc.gameSettings.keyBindUseItem.pressed = false;
		mc.thePlayer.cameraPitch = 1;
	}
	
	public void onUpdate() {
		if(this.isToggled()) {
			if(mc.gameSettings.keyBindJump.isPressed()) {
				d = 0;
			}
			ItemStack i = mc.thePlayer.getCurrentEquippedItem();
			if(mc.thePlayer.onGround) {
				mc.timer.timerSpeed = 0.8F;
				
				
				
				if(d == 0) {
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.6, mc.thePlayer.posZ);
					d++;
					
				}else {
					
					
					if(i.getItem() instanceof ItemBlock) {
						mc.thePlayer.cameraPitch = 1;
						mc.gameSettings.keyBindUseItem.pressed = true;
						mc.thePlayer.rotationPitch = 455;
						
					}
					mc.thePlayer.cameraPitch = 1;
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.6, mc.thePlayer.posZ);
				}
				
				
				
			}else {
				mc.timer.timerSpeed = 1.0F;
				mc.gameSettings.keyBindUseItem.pressed = false;
			}
			
			
			
		}
		
	}

}
