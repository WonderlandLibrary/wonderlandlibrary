package me.rhys.client.module.render;

import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Name;

public class KeyStrokes extends Module {
  public static boolean isEnabled;
  
  public KeyStrokes(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
  }
  
  @Name("Rounded")
  public static boolean Rounded = true;
  
  @Name("Rainbow")
  public static boolean Rainbow = true;
  
  @Name("Custom Font")
  public static boolean Font = true;
  
  public void onEnable() {
    isEnabled = true;
  }
  
  public void onDisable() {
    isEnabled = false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\render\KeyStrokes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */