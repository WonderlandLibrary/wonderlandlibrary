package org.newdawn.slick.command;

abstract class ControllerControl implements Control {
   protected static final int BUTTON_EVENT = 0;
   protected static final int LEFT_EVENT = 1;
   protected static final int RIGHT_EVENT = 2;
   protected static final int UP_EVENT = 3;
   protected static final int DOWN_EVENT = 4;
   private int event;
   private int button;
   private int controllerNumber;

   protected ControllerControl(int var1, int var2, int var3) {
      this.event = var2;
      this.button = var3;
      this.controllerNumber = var1;
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (!(var1 instanceof ControllerControl)) {
         return false;
      } else {
         ControllerControl var2 = (ControllerControl)var1;
         return var2.controllerNumber == this.controllerNumber && var2.event == this.event && var2.button == this.button;
      }
   }

   public int hashCode() {
      return this.event + this.button + this.controllerNumber;
   }
}
