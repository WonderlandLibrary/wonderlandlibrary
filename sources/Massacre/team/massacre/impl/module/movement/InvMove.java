package team.massacre.impl.module.movement;

import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.impl.event.EventUpdate;

public class InvMove extends Module {
   public InvMove() {
      super("Inventory Move", 0, Category.MOVEMENT);
   }

   @Handler
   public void onUpdate(EventUpdate eventUpdate) {
   }
}
