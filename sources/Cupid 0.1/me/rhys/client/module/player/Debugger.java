package me.rhys.client.module.player;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Name;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

public class Debugger extends Module {
  @Name("C0FPacketConfirmTransaction")
  private boolean C0FPacketConfirmTransaction;
  
  @Name("C00PacketKeepAlive")
  private boolean C00PacketKeepAlive;
  
  @Name("C00Handshake")
  private boolean C00Handshake;
  
  public Debugger(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
  }
  
  @EventTarget
  void onPacket(PacketEvent event) {
    if (event.getPacket() instanceof C0FPacketConfirmTransaction && 
      this.C0FPacketConfirmTransaction)
      this.mc.thePlayer.sendMessage("[Packet] C0FPacketConfirmTransaction | " + ((C0FPacketConfirmTransaction)event.getPacket()).getUid()); 
    if (event.getPacket() instanceof C00PacketKeepAlive && 
      this.C00PacketKeepAlive)
      this.mc.thePlayer.sendMessage("[Packet] C00PacketKeepAlive | " + ((C00PacketKeepAlive)event.getPacket()).getKey()); 
    if (event.getPacket() instanceof C00Handshake && 
      this.C00Handshake)
      this.mc.thePlayer.sendMessage("[Packet] C00Handshake | " + ((C00Handshake)event.getPacket()).getRequestedState()); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\Debugger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */