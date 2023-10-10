package net.minecraft.util;

import net.minecraft.util.MovementInput;

public class MovementInputOverridden extends MovementInput {
   public MovementInputOverridden(float fwd, float strafe, boolean jump, boolean sneak) {
      this.moveForward = fwd;
      this.moveStrafe = strafe;
      this.jump = jump;
      this.sneak = sneak;
   }

   public void updatePlayerMoveState() {
   }
}
