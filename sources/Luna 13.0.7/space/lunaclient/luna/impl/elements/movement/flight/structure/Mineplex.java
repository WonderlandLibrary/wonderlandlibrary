package space.lunaclient.luna.impl.elements.movement.flight.structure;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C03PacketPlayer;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventUpdate;

public class Mineplex
{
  public Mineplex() {}
  
  @EventRegister
  public void onUpdate(EventUpdate e)
  {
    if (Minecraft.thePlayer.isMoving())
    {
      net.minecraft.util.Timer.timerSpeed = 0.26F;
      Minecraft.thePlayer.speedInAir = 0.26F;
    }
    else
    {
      Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
    }
  }
}
