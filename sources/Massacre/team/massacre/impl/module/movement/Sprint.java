package team.massacre.impl.module.movement;

import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.impl.event.EventUpdate;

public class Sprint extends Module {
   public Sprint() {
      super("Sprint", 0, Category.MOVEMENT);
   }

   @Handler
   public void onUpdate(EventUpdate eventUpdate) {
      if (this.movementKeybindsPressed() && this.mc.thePlayer.getFoodStats().getFoodLevel() > 3 && !this.mc.thePlayer.isCollidedHorizontally && !this.mc.thePlayer.isSneaking()) {
         this.mc.thePlayer.setSprinting(true);
      }

   }

   public boolean movementKeybindsPressed() {
      return this.mc.gameSettings.keyBindForward.isKeyDown() || this.mc.gameSettings.keyBindBack.isKeyDown() || this.mc.gameSettings.keyBindLeft.isKeyDown() || this.mc.gameSettings.keyBindRight.isKeyDown();
   }
}
