package artemis.mods.Movement;

import artemis.Category;
import artemis.module;

public class Step extends module {
  
  public Step() {
    super("Step", "Steps over any amount of blocks", 0, 16, Category.MOVEMENT);
  }
  
  public void onDisable() {
	  mc.thePlayer.stepHeight = .5F;
    super.onDisable();
  }
  
  public void onUpdate() {
    if (isToggled()) {
    	mc.thePlayer.stepHeight = 1.5F;
      super.onUpdate();
    } 
  }
}