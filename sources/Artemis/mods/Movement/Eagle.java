package artemis.mods.Movement;

import artemis.Category;
import artemis.module;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

public class Eagle extends module{

	public Eagle() {
		super("Eagle", "Shifts", 0, 3453, Category.MOVEMENT);
	}

	
	

	  public void onDisable() {
		  mc.gameSettings.keyBindSneak.pressed = false;
	    super.onDisable();
	  }
	  
	  
	  @Override
		public void onUpdate() {
		  mc.gameSettings.keyBindSneak.pressed = false;
		  mc.gameSettings.keyBindJump.pressed = false;
			if(this.isToggled()){
			
				if(mc.thePlayer != null && mc.theWorld != null) {
					ItemStack i = mc.thePlayer.getCurrentEquippedItem();
					BlockPos Bp = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1D, mc.thePlayer.posZ);
					if(i != null) {
						if(i.getItem() instanceof ItemBlock) {
							mc.gameSettings.keyBindSneak.pressed = false;
							if(mc.theWorld.getBlockState(Bp).getBlock() == Blocks.air) {
								mc.gameSettings.keyBindSneak.pressed = true;
							}
							
						}
			        }
		       }			
	       }
	   }
   }
