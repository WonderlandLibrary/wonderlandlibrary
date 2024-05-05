package space.lunaclient.luna.impl.elements.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.status.server.S01PacketPong;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventPacketReceive;
import space.lunaclient.luna.impl.events.EventPacketSend;
import space.lunaclient.luna.impl.events.EventUpdate;

@ElementInfo(name="Sync", category=Category.PLAYER)
public class Sync
  extends Element
{
  private int change;
  
  public Sync() {}
  
  @EventRegister
  public void onPacketSend(EventPacketSend e)
  {
    if ((e.getPacket() instanceof S2EPacketCloseWindow)) {
      e.setCancelled(true);
    }
  }
  
  @EventRegister
  public void onPacketReceive(EventPacketReceive e)
  {
    if ((e.getPacket() instanceof C03PacketPlayer | e.getPacket() instanceof S01PacketPong | e.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition)) {
      e.setCancelled(true);
    }
  }
  
  @EventRegister
  public void onUpdate(EventUpdate e)
  {
    int newSlot = 9;
    int oldSlot = Minecraft.thePlayer.inventory.currentItem;
    Minecraft.thePlayer.inventory.currentItem = newSlot;
    this.change += 1;
    Minecraft.thePlayer.inventory.currentItem = oldSlot;
    this.change = 0;
  }
}
