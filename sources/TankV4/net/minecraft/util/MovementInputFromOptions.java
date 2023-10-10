package net.minecraft.util;

import my.NewSnake.Tank.module.modules.WORLD.InventoryMove;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import org.lwjgl.input.Keyboard;

public class MovementInputFromOptions extends MovementInput {
   private static final String __OBFID = "CL_00000937";
   private final GameSettings gameSettings;

   public MovementInputFromOptions(GameSettings var1) {
      this.gameSettings = var1;
   }

   public void updatePlayerMoveState() {
      if ((new InventoryMove()).getInstance().isEnabled() && !(ClientUtils.mc().currentScreen instanceof GuiChat)) {
         this.moveStrafe = 0.0F;
         this.moveForward = 0.0F;
         if (Keyboard.isKeyDown(this.gameSettings.keyBindForward.getKeyCode())) {
            ++this.moveForward;
         }

         if (Keyboard.isKeyDown(this.gameSettings.keyBindBack.getKeyCode())) {
            --this.moveForward;
         }

         if (Keyboard.isKeyDown(this.gameSettings.keyBindLeft.getKeyCode())) {
            ++this.moveStrafe;
         }

         if (Keyboard.isKeyDown(this.gameSettings.keyBindRight.getKeyCode())) {
            --this.moveStrafe;
         }

         this.jump = Keyboard.isKeyDown(this.gameSettings.keyBindJump.getKeyCode());
         this.sneak = this.gameSettings.keyBindSneak.isKeyDown();
         if (this.sneak) {
            this.moveStrafe = (float)((double)this.moveStrafe * 0.3D);
            this.moveForward = (float)((double)this.moveForward * 0.3D);
         }
      } else {
         this.moveStrafe = 0.0F;
         this.moveForward = 0.0F;
         if (this.gameSettings.keyBindForward.isKeyDown()) {
            ++this.moveForward;
         }

         if (this.gameSettings.keyBindBack.isKeyDown()) {
            --this.moveForward;
         }

         if (this.gameSettings.keyBindLeft.isKeyDown()) {
            ++this.moveStrafe;
         }

         if (this.gameSettings.keyBindRight.isKeyDown()) {
            --this.moveStrafe;
         }

         this.jump = this.gameSettings.keyBindJump.isKeyDown();
         this.sneak = this.gameSettings.keyBindSneak.isKeyDown();
         if (this.sneak) {
            this.moveStrafe = (float)((double)this.moveStrafe * 0.3D);
            this.moveForward = (float)((double)this.moveForward * 0.3D);
         }
      }

   }
}
