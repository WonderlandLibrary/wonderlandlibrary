package my.NewSnake.event.events;

import my.NewSnake.event.Event;

public class SprintEvent extends Event {
   private boolean sprinting;

   public SprintEvent(boolean var1) {
      this.setSprinting(var1);
   }

   public boolean isSprinting() {
      return this.sprinting;
   }

   public void setSprinting(boolean var1) {
      this.sprinting = var1;
   }
}
