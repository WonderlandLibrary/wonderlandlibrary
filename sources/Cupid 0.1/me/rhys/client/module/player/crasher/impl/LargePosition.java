package me.rhys.client.module.player.crasher.impl;

import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.client.module.player.crasher.Crasher;

public class LargePosition extends ModuleMode<Crasher> {
  public LargePosition(String name, Crasher parent) {
    super(name, (Module)parent);
  }
  
  public void onEnable() {
    player().setLocationAndAngles(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 11444.0D, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\crasher\impl\LargePosition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */