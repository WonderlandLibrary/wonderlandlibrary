package space.lunaclient.luna.impl.elements.movement.highjump.structure;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.util.Vector3DUtils;

public class Border
{
  public Border() {}
  
  @EventRegister
  public void onUpdate(EventUpdate e)
  {
    if ((Minecraft.thePlayer.isCollidedHorizontally & Minecraft.thePlayer.isSneaking()))
    {
      if (Minecraft.thePlayer.isMoving())
      {
        Minecraft.thePlayer.setSpeed(EntityPlayerSP.getSpeed() + 0.05D);
        Vec3 pos = new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ);
        Vector3DUtils vec = new Vector3DUtils(pos, -Minecraft.thePlayer.rotationYaw, 0.0F, 0.35D);
        Minecraft.thePlayer.onGround = (Minecraft.thePlayer.isDead & Minecraft.thePlayer.isMoving());
        Minecraft.thePlayer.setPosition(vec.getEndVector().xCoord, vec.getEndVector().yCoord - 0.0195D, vec.getEndVector().zCoord);
        e.setCancelled(true);
        if (Element.mc.gameSettings.keyBindJump.getIsKeyPressed()) {
          Minecraft.thePlayer.motionY = 0.45D;
        } else if (Element.mc.gameSettings.keyBindSneak.getIsKeyPressed()) {
          Minecraft.thePlayer.motionY = -0.45D;
        }
      }
    }
    else {
      Element.mc.timer.resetTimer();
    }
  }
}
