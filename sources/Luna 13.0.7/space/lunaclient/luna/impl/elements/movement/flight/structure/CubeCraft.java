package space.lunaclient.luna.impl.elements.movement.flight.structure;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.Vec3;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.util.PlayerUtils;
import space.lunaclient.luna.util.Vector3DUtils;

public class CubeCraft
{
  public CubeCraft() {}
  
  @EventRegister
  public void onUpdate(EventUpdate e)
  {
    net.minecraft.util.Timer.timerSpeed = 0.250592F;
    Minecraft mc = Minecraft.getMinecraft();
    if (Minecraft.thePlayer.isMoving())
    {
      Minecraft.thePlayer.motionY += 0.002D;
      Minecraft.thePlayer.noClip = true;
      PlayerUtils.updatePosition(Minecraft.thePlayer);
      Vec3 pos = new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ);
      Vector3DUtils vec = new Vector3DUtils(pos, -Minecraft.thePlayer.rotationYaw, 0.0F, Minecraft.thePlayer.ticksExisted % 2 == 0 ? 0.1D : 1.5D);
      Minecraft.thePlayer.onGround = (Minecraft.thePlayer.isDead & Minecraft.thePlayer.isMoving());
      Minecraft.thePlayer.setPosition(vec.getEndVector().xCoord, vec.getEndVector().yCoord, vec.getEndVector().zCoord);
      if (mc.gameSettings.keyBindJump.getIsKeyPressed()) {
        Minecraft.thePlayer.motionY = 0.45D;
      } else if (mc.gameSettings.keyBindSneak.getIsKeyPressed()) {
        Minecraft.thePlayer.motionY = -0.45D;
      }
    }
    else
    {
      Minecraft.thePlayer.setSpeed(0.0D);
    }
  }
}
