package me.rhys.client.module.player;

import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;

public class FPSBooster extends Module {
  public FPSBooster(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
  }
  
  public void onEnable() {
    this.mc.gameSettings.ofFastRender = true;
    this.mc.gameSettings.ofFastMath = true;
    this.mc.gameSettings.renderDistanceChunks = 6;
    this.mc.gameSettings.enableVsync = false;
    this.mc.gameSettings.viewBobbing = true;
    this.mc.gameSettings.ofClearWater = true;
    this.mc.renderGlobal.loadRenderers();
  }
  
  public void onDisable() {
    this.mc.gameSettings.ofFastRender = false;
    this.mc.gameSettings.renderDistanceChunks = 12;
    this.mc.gameSettings.ofFastMath = false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\FPSBooster.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */