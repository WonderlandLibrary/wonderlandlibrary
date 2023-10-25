package me.rhys.client.module.player;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Name;
import net.minecraft.client.entity.EntityPlayerSP;

public class Clipper extends Module {
  @Name("UP")
  public boolean up;
  
  public Clipper(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    this.up = false;
  }
  
  @EventTarget
  public void onMotion(PlayerMotionEvent event) {
    EntityPlayerSP player = event.getPlayer();
    if (this.mc.thePlayer.isSneaking())
      if (!this.up) {
        player.setPositionAndUpdate(player.posX, player.posY + -4.0D, player.posZ);
      } else {
        player.setPositionAndUpdate(player.posX, player.posY + 4.0D, player.posZ);
      }  
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\Clipper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */