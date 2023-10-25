package me.rhys.client.module.player.pingspoof.mode;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.Timer;
import me.rhys.client.module.player.pingspoof.PingSpoof;
import net.minecraft.network.Packet;

public class Delay extends ModuleMode<PingSpoof> {
  @Name("Delay")
  @Clamp(min = 1000.0D, max = 99000.0D)
  public int delay;
  
  @Name("Delay C0F")
  public boolean delayC0F;
  
  private final Timer timer;
  
  private final List<Packet> packetList;
  
  public Delay(String name, PingSpoof parent) {
    super(name, (Module)parent);
    this.delay = 1500;
    this.delayC0F = true;
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
    if (this.packetList.size() > 0 && this.timer.hasReached(this.delay) && event.getType() == Event.Type.PRE)
      sendAll(); 
  }
  
  @EventTarget
  void onPacket(PacketEvent event) {
    boolean isTransaction = false;
    if (event.getPacket() instanceof net.minecraft.network.play.client.C00PacketKeepAlive || (
      isTransaction = event.getPacket() instanceof net.minecraft.network.play.client.C0FPacketConfirmTransaction)) {
      if (this.delayC0F && isTransaction)
        return; 
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


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\pingspoof\mode\Delay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */