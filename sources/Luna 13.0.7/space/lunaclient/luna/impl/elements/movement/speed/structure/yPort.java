package space.lunaclient.luna.impl.elements.movement.speed.structure;

import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.elements.movement.speed.Speed;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.util.PlayerUtils;
import space.lunaclient.luna.util.scaffold.BlockUtils;

public class yPort
{
  public static AtomicInteger state = new AtomicInteger();
  
  public yPort() {}
  
  @EventRegister
  public void onUpdate(EventUpdate e)
  {
    Minecraft.thePlayer.posY = Speed.yOffset;
    if (!(BlockUtils.getBlockUnderPlayer(Minecraft.thePlayer, 1.0D) instanceof BlockAir))
    {
      if ((((Minecraft.thePlayer.moveForward != 0.0F ? 1 : 0) | (Minecraft.thePlayer.moveStrafing != 0.0F ? 1 : 0)) & (!Minecraft.thePlayer.isCollidedHorizontally ? 1 : 0)) != 0)
      {
        if ((Minecraft.thePlayer.fallDistance > 3.994D | Minecraft.thePlayer.isInWater() | Minecraft.thePlayer.isOnLadder() | Element.mc.gameSettings.keyBindJump.pressed | Minecraft.thePlayer.fallDistance > 0.1D))
        {
          net.minecraft.util.Timer.timerSpeed = 1.0F;
          return;
        }
        Minecraft.thePlayer.cameraPitch = 0.0F;
        net.minecraft.util.Timer.timerSpeed = 1.0F;
        Minecraft.thePlayer.setSpeed(PlayerUtils.getBaseMoveSpeed() - 0.03D);
      }
      if ((Minecraft.thePlayer.isInWater() | Minecraft.thePlayer.isOnLadder())) {
        return;
      }
      if ((Minecraft.thePlayer.fallDistance < 0.0784D & Minecraft.thePlayer.isMoving() & !Minecraft.thePlayer.isCollidedHorizontally))
      {
        net.minecraft.util.Timer.timerSpeed = (float)Speed.vSpeed.getValDouble();
        Minecraft.thePlayer.motionY -= 1.0D;
        
        Minecraft.thePlayer.posY = Speed.yOffset;
      }
      else
      {
        net.minecraft.util.Timer.timerSpeed = 1.0F;
      }
      if ((Minecraft.thePlayer.onGround & ((Minecraft.thePlayer.moveForward != 0.0F ? 1 : 0) | (Minecraft.thePlayer.moveStrafing != 0.0F ? 1 : 0)) & !Minecraft.thePlayer.isCollidedHorizontally))
      {
        Minecraft.thePlayer.jump();
        if (Speed.vSpeed.getValDouble() > 2.0D)
        {
          Minecraft.thePlayer.setSpeed(PlayerUtils.getBaseMoveSpeed() + 0.17D);
          net.minecraft.util.Timer.timerSpeed = (float)(Speed.vSpeed.getValDouble() - 0.15D);
        }
        Minecraft.thePlayer.posY = Speed.yOffset;
        Minecraft.thePlayer.cameraPitch = 0.0F;
      }
    }
  }
}
