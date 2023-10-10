package org.newdawn.slick.command;

public class MouseButtonControl implements Control {
   private int button;

   public MouseButtonControl(int var1) {
      this.button = var1;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof MouseButtonControl) {
         return ((MouseButtonControl)var1).button == this.button;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.button;
   }
}
