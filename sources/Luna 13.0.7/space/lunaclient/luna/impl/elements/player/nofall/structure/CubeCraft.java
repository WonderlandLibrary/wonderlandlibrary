package space.lunaclient.luna.impl.elements.player.nofall.structure;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C03PacketPlayer;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventUpdate;

public class CubeCraft
{
  public CubeCraft() {}
  
  @EventRegister
  public void onUpdate(EventUpdate event)
  {
    if (Minecraft.thePlayer.fallDistance > 2.656F) {
      Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
    }
  }
}
