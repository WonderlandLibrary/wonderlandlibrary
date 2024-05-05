package space.lunaclient.luna.impl.elements.combat.criticals.structure;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C02PacketUseEntity;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventPacketSend;

public class Motion
{
  private static Minecraft mc = ;
  
  public Motion() {}
  
  public void onCriticalHit()
  {
    if (Minecraft.thePlayer.onGround) {
      Minecraft.thePlayer.motionY = 0.0622D;
    }
  }
  
  @EventRegister
  public void onAttack(EventPacketSend e)
  {
    if ((e.getPacket() instanceof C02PacketUseEntity & Minecraft.thePlayer.isSwingInProgress)) {
      onCriticalHit();
    }
  }
}
