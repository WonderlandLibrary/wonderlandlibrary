package me.rhys.client.module.ghost.beanernet;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.Timer;
import net.minecraft.network.Packet;

public class BeanerNet extends Module {
  @Name("How Beaner?")
  @Clamp(min = 100.0D, max = 500.0D)
  public int delay = 250;
  
  private final Timer timer = new Timer();
  
  private final List<Packet> packetList = new CopyOnWriteArrayList<>();
  
  public BeanerNet(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
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
      if (isTransaction)
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


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\ghost\beanernet\BeanerNet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */