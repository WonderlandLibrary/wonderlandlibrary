package space.lunaclient.luna.impl.elements.movement.speed.structure;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.util.PlayerUtils;

public class Strafe
{
  public Strafe() {}
  
  @EventRegister
  public void onUpdate(EventUpdate event)
  {
    Minecraft mc = Minecraft.getMinecraft();
    if ((Minecraft.thePlayer.onGround & Minecraft.thePlayer.isMoving())) {
      Minecraft.thePlayer.jump();
    } else if ((!Minecraft.thePlayer.onGround & Minecraft.thePlayer.isMoving())) {
      Minecraft.thePlayer.setSpeed(PlayerUtils.getBaseMoveSpeed() - 0.05D);
    }
  }
}
