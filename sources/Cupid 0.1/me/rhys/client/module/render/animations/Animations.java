package me.rhys.client.module.render.animations;

import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.render.animations.modes.Allah;
import me.rhys.client.module.render.animations.modes.Chop;
import me.rhys.client.module.render.animations.modes.Exhibition;
import me.rhys.client.module.render.animations.modes.Jax;
import me.rhys.client.module.render.animations.modes.Slice;
import me.rhys.client.module.render.animations.modes.Slide;
import me.rhys.client.module.render.animations.modes.Up;
import me.rhys.client.module.render.animations.modes.UpperCut;

public class Animations extends Module {
  public static boolean oldAnimations = false;
  
  public static boolean delayFix = false;
  
  public boolean equipReset = true;
  
  public boolean smoothSwing = false;
  
  @Name("Swing Type")
  public SwingType swingType = SwingType.SMOOTH;
  
  public Animations(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    add((Object[])new ModuleMode[] { (ModuleMode)new Exhibition("Exhibition", this), (ModuleMode)new Slide("Slide", this), (ModuleMode)new Up("Up", this), (ModuleMode)new UpperCut("UpperCut", this), (ModuleMode)new Chop("Chop", this), (ModuleMode)new Allah("Allah", this), (ModuleMode)new Slice("Slice", this), (ModuleMode)new Jax("Jax", this) });
  }
  
  public enum SwingType {
    GERMAN, SMOOTH;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\render\animations\Animations.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */