package net.minecraft.util;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInput;
import org.alphacentauri.management.events.EventInput;
import org.alphacentauri.management.events.EventSlowdown;
import org.alphacentauri.management.events.EventSlowdown$Cause;

public class MovementInputFromOptions extends MovementInput {
   private final GameSettings gameSettings;

   public MovementInputFromOptions(GameSettings gameSettingsIn) {
      this.gameSettings = gameSettingsIn;
   }

   public void updatePlayerMoveState() {
      this.moveStrafe = 0.0F;
      this.moveForward = 0.0F;
      if(this.gameSettings.keyBindForward.isKeyDown()) {
         ++this.moveForward;
      }

      if(this.gameSettings.keyBindBack.isKeyDown()) {
         --this.moveForward;
      }

      if(this.gameSettings.keyBindLeft.isKeyDown()) {
         ++this.moveStrafe;
      }

      if(this.gameSettings.keyBindRight.isKeyDown()) {
         --this.moveStrafe;
      }

      this.jump = this.gameSettings.keyBindJump.isKeyDown();
      this.sneak = this.gameSettings.keyBindSneak.isKeyDown();
      if(this.sneak) {
         float noSlowDown = ((EventSlowdown)(new EventSlowdown(EventSlowdown$Cause.Sneaking)).fire()).getNoSlowDown();
         this.moveStrafe = (float)((double)this.moveStrafe * (0.3D + 0.7D * (double)noSlowDown));
         this.moveForward = (float)((double)this.moveForward * (0.3D + 0.7D * (double)noSlowDown));
      }

      EventInput input = (EventInput)(new EventInput(this.moveForward, this.moveStrafe, this.jump, this.sneak)).fire();
      this.moveForward = input.getMoveForward();
      this.moveStrafe = input.getMoveStrafe();
      this.jump = input.isJump();
      this.sneak = input.isSneak();
   }
}
