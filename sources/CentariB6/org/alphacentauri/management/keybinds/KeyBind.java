package org.alphacentauri.management.keybinds;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import org.alphacentauri.AC;
import org.lwjgl.input.Keyboard;

public class KeyBind {
   public int keyCode;
   public String command;
   public boolean keyState = false;
   public boolean oldKeyState = false;

   public KeyBind(int key, String command) {
      this.keyCode = key;
      this.command = command;
   }

   public void update() {
      if(this.keyCode > 0) {
         this.oldKeyState = this.keyState;
         this.keyState = Keyboard.isKeyDown(this.keyCode);
      }

   }

   public boolean isPressed() {
      return !(AC.getMC().currentScreen instanceof GuiInventory) && !(AC.getMC().currentScreen instanceof GuiChest) && AC.getMC().currentScreen != null?false:!this.oldKeyState && this.keyState;
   }

   public boolean isPressedIgnoreGUI() {
      return !this.oldKeyState && this.keyState;
   }

   public boolean isDown() {
      return this.keyState;
   }
}
