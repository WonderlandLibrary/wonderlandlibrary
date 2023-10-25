package me.rhys.client.module.render;

import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;

public class FullBright extends Module {
  @Name("Brightness")
  @Clamp(min = 1000.0D, max = 10000.0D)
  public float bright;
  
  float pre;
  
  public FullBright(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    this.bright = 1000.0F;
    this.pre = 1.0F;
  }
  
  public void onEnable() {
    this.pre = this.mc.gameSettings.gammaSetting;
    this.mc.gameSettings.gammaSetting = this.bright;
  }
  
  public void onDisable() {
    this.mc.gameSettings.gammaSetting = this.pre;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\render\FullBright.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */