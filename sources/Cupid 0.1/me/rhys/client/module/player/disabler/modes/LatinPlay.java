package me.rhys.client.module.player.disabler.modes;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.util.Timer;
import me.rhys.client.module.player.disabler.Disabler;
import net.minecraft.network.Packet;

public class LatinPlay extends ModuleMode<Disabler> {
  private final Timer timer;
  
  private final List<Packet> packetList;
  
  public LatinPlay(String name, Disabler parent) {
    super(name, (Module)parent);
    this.timer = new Timer();
    this.packetList = new CopyOnWriteArrayList<>();
  }
  
  public void onEnable() {
    this.packetList.clear();
    this.timer.reset();
  }
  
  public void onDisable() {
    sendAll();
  }
  
  @EventTarget
  void onMotion(PlayerMotionEvent event) {
    if (this.packetList.size() > 0 && this.timer.hasReached(1071.0D) && event.getType() == Event.Type.PRE)
      sendAll(); 
  }
  
  @EventTarget
  void onPacket(PacketEvent event) {
    boolean isTransaction = false;
    if (event.getPacket() instanceof net.minecraft.network.play.client.C00PacketKeepAlive || (
      isTransaction = event.getPacket() instanceof net.minecraft.network.play.client.C0FPacketConfirmTransaction)) {
      event.setCancelled(true);
      this.packetList.add(event.getPacket());
    } 
  }
  
  void sendAll() {
    this.packetList.forEach(packet -> {
          this.mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(packet);
          this.packetList.remove(packet);
        });
    this.timer.reset();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\disabler\modes\LatinPlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */