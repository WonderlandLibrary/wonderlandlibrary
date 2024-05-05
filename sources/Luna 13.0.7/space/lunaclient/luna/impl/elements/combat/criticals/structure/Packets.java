package space.lunaclient.luna.impl.elements.combat.criticals.structure;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventPacketSend;
import space.lunaclient.luna.util.Timer;

public class Packets
{
  private Minecraft mc = Minecraft.getMinecraft();
  private Timer timer = new Timer();
  
  public Packets() {}
  
  private void onCriticalHit()
  {
    if ((Minecraft.thePlayer.onGround) && (Minecraft.thePlayer.isCollidedVertically) && 
      (this.timer.hasReached(500L)))
    {
      Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.0622D, Minecraft.thePlayer.posZ, false));
      Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.0622D, Minecraft.thePlayer.posZ, false));
      Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, false));
      Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, false));
      this.timer.reset();
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
