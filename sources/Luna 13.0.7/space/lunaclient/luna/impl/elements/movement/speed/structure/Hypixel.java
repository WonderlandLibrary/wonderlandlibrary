package space.lunaclient.luna.impl.elements.movement.speed.structure;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MathHelper;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventMove;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.util.PlayerUtils;

public class Hypixel
{
  public static int stage;
  
  public Hypixel() {}
  
  @EventRegister
  public void onUpdate(EventUpdate e) {}
  
  @EventRegister
  public void onMove(EventMove em)
  {
    if (Minecraft.thePlayer.isMoving())
    {
      net.minecraft.util.Timer.timerSpeed = 1.085F;
      if (Minecraft.thePlayer.onGround) {
        em.setY(Minecraft.thePlayer.motionY = 0.39912D);
      }
      if (Minecraft.thePlayer.ticksExisted % 2 == 0)
      {
        em.setX(-MathHelper.sin(PlayerUtils.getDirection()) * 0.3275D);
        em.setZ(MathHelper.cos(PlayerUtils.getDirection()) * 0.3275D);
      }
      else
      {
        em.setX(-MathHelper.sin(PlayerUtils.getDirection()) * PlayerUtils.getBaseMoveSpeed());
        em.setZ(MathHelper.cos(PlayerUtils.getDirection()) * PlayerUtils.getBaseMoveSpeed());
      }
    }
    else
    {
      em.setX(0.0D);
      em.setZ(0.0D);
    }
  }
}
