package space.lunaclient.luna.impl.elements.player.nofall.structure;

import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventPacketReceive;
import space.lunaclient.luna.util.PlayerUtils;
import space.lunaclient.luna.util.scaffold.BlockUtils;

public class AAC
{
  public AAC() {}
  
  @EventRegister
  public void onUpdate(EventPacketReceive event)
  {
    if (Minecraft.thePlayer.fallDistance > 3.0F)
    {
      PlayerUtils.tpRel(0.0D, -0.06D, 0.0D);
      if (((event.getPacket() instanceof S19PacketEntityHeadLook)) || ((event.getPacket() instanceof S14PacketEntity))) {
        event.setCancelled(true);
      }
      if (!(BlockUtils.getBlockUnderPlayer(Minecraft.thePlayer, 2.0D) instanceof BlockAir))
      {
        PlayerUtils.tpRel(0.0D, 0.0D, 0.0D);
        event.setCancelled(true);
        net.minecraft.util.Timer.timerSpeed = 0.8F;
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
      }
      else
      {
        net.minecraft.util.Timer.timerSpeed = 1.0F;
      }
      Minecraft.thePlayer.onGround = true;
      if ((Minecraft.thePlayer.isMoving()) || (Element.mc.gameSettings.keyBindJump.pressed))
      {
        Minecraft.thePlayer.setSpeed(0.0D);
        Minecraft.thePlayer.onGround = false;
      }
    }
    else
    {
      net.minecraft.util.Timer.timerSpeed = 1.0F;
    }
  }
}
