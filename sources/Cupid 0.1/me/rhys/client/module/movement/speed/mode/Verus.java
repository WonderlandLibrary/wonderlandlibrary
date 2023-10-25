package me.rhys.client.module.movement.speed.mode;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.util.StrafeUtil;
import me.rhys.client.module.movement.speed.Speed;
import net.minecraft.client.Minecraft;

public class Verus extends ModuleMode<Speed> {
  public Verus(String name, Speed parent) {
    super(name, (Module)parent);
  }
  
  public static int tick = 0;
  
  public static Minecraft mc = Minecraft.getMinecraft();
  
  private static double jumpY = 0.0D;
  
  private static int bypassTicks = 0;
  
  @EventTarget
  public void onMotion(PlayerMotionEvent event) {
    double speedBoost = 0.0D;
    bypassTicks--;
    tick++;
    if (mc.thePlayer.onGround && StrafeUtil.isMoving()) {
      StrafeUtil.strafe(Double.valueOf(0.35D));
      mc.thePlayer.jump();
      jumpY = mc.thePlayer.posY;
    } 
    if (StrafeUtil.isMoving()) {
      if (mc.thePlayer.fallDistance >= 1.5D) {
        if (bypassTicks > 0) {
          StrafeUtil.strafe(Double.valueOf(1.0D));
        } else {
          StrafeUtil.strafe(Double.valueOf(0.26D));
        } 
      } else if (bypassTicks > 0) {
        StrafeUtil.strafe(Double.valueOf(1.0D + speedBoost));
      } else {
        StrafeUtil.strafe(Double.valueOf(0.33D + speedBoost));
        if (mc.thePlayer.posY - jumpY < 0.35D)
          StrafeUtil.strafe(Double.valueOf(0.5D + speedBoost)); 
      } 
    } else {
      mc.thePlayer.motionX = 0.0D;
      mc.thePlayer.motionZ = 0.0D;
    } 
  }
  
  public static void onEnable(Speed module) {
    mc.timer.timerSpeed = 1.0F;
    bypassTicks = 0;
    tick = 0;
  }
  
  public static void onDisable(Speed module) {
    mc.timer.timerSpeed = 1.0F;
    tick = 0;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\speed\mode\Verus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */