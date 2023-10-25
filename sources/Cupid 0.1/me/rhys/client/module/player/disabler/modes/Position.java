package me.rhys.client.module.player.disabler.modes;

import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.client.module.player.disabler.Disabler;

public class Position extends ModuleMode<Disabler> {
  public Position(String name, Disabler parent) {
    super(name, (Module)parent);
  }
  
  public void onEnable() {
    player().setLocationAndAngles(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1000.0D, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch);
    this.mc.thePlayer.sendMessage("[Debug] Done.");
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\disabler\modes\Position.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */