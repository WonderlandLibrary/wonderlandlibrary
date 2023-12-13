package org.newdawn.slick.command;

public class KeyControl implements Control {
   private int keycode;

   public KeyControl(int var1) {
      this.keycode = var1;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof KeyControl) {
         return ((KeyControl)var1).keycode == this.keycode;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.keycode;
   }
}
