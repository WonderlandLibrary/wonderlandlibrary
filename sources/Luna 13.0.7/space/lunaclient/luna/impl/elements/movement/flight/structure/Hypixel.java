package space.lunaclient.luna.impl.elements.movement.flight.structure;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventMove;
import space.lunaclient.luna.util.PlayerUtils;

public class Hypixel
{
  public int counter = 0;
  private int destr;
  
  public Hypixel() {}
  
  @EventRegister
  public void onUpdate(EventMove e)
  {
    if (Minecraft.thePlayer.isMoving()) {
      Minecraft.thePlayer.setSpeed(PlayerUtils.getBaseMoveSpeed() - 0.05D);
    } else {
      Minecraft.thePlayer.setSpeed(0.0D);
    }
    this.counter += 1;
    Minecraft.thePlayer.onGround = false;
    if (Minecraft.thePlayer.isMoving())
    {
      Minecraft.thePlayer.setSpeed(PlayerUtils.getBaseMoveSpeed());
    }
    else
    {
      Minecraft.thePlayer.motionX *= 0.0D;
      Minecraft.thePlayer.motionZ *= 0.0D;
      net.minecraft.util.Timer.timerSpeed = 1.0F;
    }
    if (Element.mc.gameSettings.keyBindJump.pressed) {
      PlayerUtils.tpRel(0.0D, 0.12D, 0.0D);
    } else if (Element.mc.gameSettings.keyBindSneak.pressed) {
      PlayerUtils.tpRel(0.0D, -0.12D, 0.0D);
    }
    PlayerUtils.updatePosition(Minecraft.thePlayer);
    switch (this.counter)
    {
    case 1: 
      break;
    case 2: 
      Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 1.0E-6D, Minecraft.thePlayer.posZ);
      this.counter = 0;
      break;
    case 3: 
      Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 1.0E-6D, Minecraft.thePlayer.posZ);
      this.counter = 0;
    }
    e.y = (Minecraft.thePlayer.motionY = 0.0D);
  }
}
