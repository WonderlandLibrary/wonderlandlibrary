package me.rhys.client.module.player.crasher.impl;

import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.player.crasher.Crasher;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class MultiVerse extends ModuleMode<Crasher> {
  @Name("/mvhelp")
  private boolean mvhelp;
  
  public MultiVerse(String name, Crasher parent) {
    super(name, (Module)parent);
    this.mvhelp = false;
  }
  
  public void onEnable() {
    String cmd = "/mv";
    if (this.mvhelp)
      cmd = "/mvhelp"; 
    this.mc.getNetHandler().addToSendQueue((Packet)new C01PacketChatMessage(cmd + " ^(.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*)$^"));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\crasher\impl\MultiVerse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */