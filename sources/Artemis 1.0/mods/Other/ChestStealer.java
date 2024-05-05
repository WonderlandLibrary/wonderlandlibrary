package artemis.mods.Other;

import org.lwjgl.input.Keyboard;

import artemis.Category;
import artemis.module;
import artemis.util.TimeUtils;
import net.minecraft.inventory.ContainerChest;

public class ChestStealer extends module{
	  public ChestStealer() {
		    super("ChsestStealer", "Takes things from a chest", Keyboard.KEY_R, 4, Category.OTHER);
	  }
	  TimeUtils time;
	  
	  public void onUpdate() {
		  if(this.isToggled()) {
			  if((mc.thePlayer.openContainer != null) && ((mc.thePlayer.openContainer instanceof ContainerChest))) {
				  ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
				  for(int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); i++) {
					  if((chest.getLowerChestInventory().getStackInSlot(i) != null) && time.hasTimerReached(1000)) {
						  mc.playerController.windowClick(chest.windowId, i, 0, 1, mc.thePlayer);
						  this.time.reset();
					  }
				  }
				  
				  if(chest.getInventory().isEmpty()) {
					  mc.displayGuiScreen(null);
				  }
				  
				
			  }
			 
			
		  }
	  }
}
