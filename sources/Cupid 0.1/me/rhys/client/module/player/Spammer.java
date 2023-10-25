package me.rhys.client.module.player;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.TickEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;

public class Spammer extends Module {
  String message;
  
  @Name("AntiSpam Bypass")
  public boolean antiKick;
  
  @Name("Delay")
  @Clamp(min = 0.1D, max = 20.0D)
  public double delay;
  
  private long lastMessageTime;
  
  public Spammer(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    this.message = "Get Cupid, Get Good!";
    this.antiKick = true;
    this.delay = 5.0D;
    this.lastMessageTime = 0L;
  }
  
  @EventTarget
  public void onWorldTick(TickEvent event) {
    if ((System.currentTimeMillis() - this.lastMessageTime) > this.delay * 1000.0D) {
      String message = this.message;
      if (this.antiKick);
      this.mc.thePlayer.sendChatMessage(message);
      this.lastMessageTime = System.currentTimeMillis();
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\Spammer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */