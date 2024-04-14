package de.violence.module.modules.MOVEMENT;

import de.violence.module.Module;
import de.violence.module.ui.Category;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiChest;
import org.lwjgl.input.Keyboard;

public class InventoryWalk extends Module {
   public static InventoryWalk instance;
   public static boolean isInvWalking = false;
   public static boolean isForServerInInv = false;
   public boolean wasLastInv = false;

   public InventoryWalk() {
      super("InventoryWalk", Category.MOVEMENT);
      instance = this;
   }

   public void onUpdate() {
      if(!(this.mc.currentScreen instanceof GuiChest)) {
         isInvWalking = false;
         if(this.mc.currentScreen != null && (this.mc.currentScreen == null || !(this.mc.currentScreen instanceof GuiChat))) {
            this.wasLastInv = true;
            this.mc.gameSettings.keyBindForward.pressed = Keyboard.isKeyDown(this.mc.gameSettings.keyBindForward.getKeyCode());
            this.mc.gameSettings.keyBindBack.pressed = Keyboard.isKeyDown(this.mc.gameSettings.keyBindBack.getKeyCode());
            this.mc.gameSettings.keyBindRight.pressed = Keyboard.isKeyDown(this.mc.gameSettings.keyBindRight.getKeyCode());
            this.mc.gameSettings.keyBindLeft.pressed = Keyboard.isKeyDown(this.mc.gameSettings.keyBindLeft.getKeyCode());
            this.mc.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(this.mc.gameSettings.keyBindJump.getKeyCode());
            if(Math.abs(this.mc.thePlayer.motionX) > 0.05D || Math.abs(this.mc.thePlayer.motionZ) > 0.05D) {
               isInvWalking = true;
            }

            super.onUpdate();
         } else {
            if(this.wasLastInv) {
               this.mc.gameSettings.keyBindForward.pressed = Keyboard.isKeyDown(this.mc.gameSettings.keyBindForward.getKeyCode());
               this.mc.gameSettings.keyBindBack.pressed = Keyboard.isKeyDown(this.mc.gameSettings.keyBindBack.getKeyCode());
               this.mc.gameSettings.keyBindRight.pressed = Keyboard.isKeyDown(this.mc.gameSettings.keyBindRight.getKeyCode());
               this.mc.gameSettings.keyBindLeft.pressed = Keyboard.isKeyDown(this.mc.gameSettings.keyBindLeft.getKeyCode());
               this.mc.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(this.mc.gameSettings.keyBindJump.getKeyCode());
               this.mc.gameSettings.keyBindSneak.pressed = Keyboard.isKeyDown(this.mc.gameSettings.keyBindSneak.getKeyCode());
               this.mc.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(this.mc.gameSettings.keyBindJump.getKeyCode());
               this.wasLastInv = false;
            }

         }
      }
   }

   public void onDisable() {
      this.mc.gameSettings.keyBindForward.pressed = false;
      this.mc.gameSettings.keyBindBack.pressed = false;
      this.mc.gameSettings.keyBindRight.pressed = false;
      this.mc.gameSettings.keyBindLeft.pressed = false;
      this.mc.gameSettings.keyBindJump.pressed = false;
      this.mc.gameSettings.keyBindSneak.pressed = false;
      super.onDisable();
   }
}
