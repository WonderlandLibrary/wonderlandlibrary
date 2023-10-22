package cn.hanabi.utils;

import org.lwjgl.input.Mouse;

public class MouseInputHandler {
   private final int button;
   public boolean clicked;

   public MouseInputHandler(int key) {
      this.button = key;
   }

   public boolean canExcecute() {
      if (Mouse.isButtonDown(this.button)) {
         if (!this.clicked) {
            this.clicked = true;
            return true;
         }
      } else {
         this.clicked = false;
      }

      return false;
   }
}
