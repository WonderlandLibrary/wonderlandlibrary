package net.minecraft.util;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class MovementInputFromOptions
  extends MovementInput
{
  private final GameSettings gameSettings;
  private static final String __OBFID = "CL_00000937";
  
  public MovementInputFromOptions(GameSettings p_i1237_1_)
  {
    this.gameSettings = p_i1237_1_;
  }
  
  public void updatePlayerMoveState()
  {
    moveStrafe = 0.0F;
    moveForward = 0.0F;
    if (this.gameSettings.keyBindForward.getIsKeyPressed()) {
      moveForward += 1.0F;
    }
    if (this.gameSettings.keyBindBack.getIsKeyPressed()) {
      moveForward -= 1.0F;
    }
    if (this.gameSettings.keyBindLeft.getIsKeyPressed()) {
      moveStrafe += 1.0F;
    }
    if (this.gameSettings.keyBindRight.getIsKeyPressed()) {
      moveStrafe -= 1.0F;
    }
    this.jump = this.gameSettings.keyBindJump.getIsKeyPressed();
    this.sneak = this.gameSettings.keyBindSneak.getIsKeyPressed();
    if (this.sneak)
    {
      moveStrafe = (float)(moveStrafe * 0.3D);
      moveForward = (float)(moveForward * 0.3D);
    }
  }
}
