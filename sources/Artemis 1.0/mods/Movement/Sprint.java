package artemis.mods.Movement;

import artemis.Category;
import artemis.module;

public class Sprint extends module {
  
  
  public Sprint() {
    super("Sprint", "Automatically sprints", 34, 15, Category.MOVEMENT);
  }
  
  public void onDisable() {
    this.mc.thePlayer.setSprinting(false);
    super.onDisable();
  }
  
  public void onUpdate() {
    if (isToggled()) {
      this.mc.thePlayer.setSprinting(true);
      super.onUpdate();
    } 
  }
}
