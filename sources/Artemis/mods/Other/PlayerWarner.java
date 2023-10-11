package artemis.mods.Other;

import org.lwjgl.input.Keyboard;

import artemis.Category;
import artemis.module;

public class PlayerWarner extends module {
  
  public PlayerWarner() {
    super("PlayerWarner", "Warns you when a entity is near", Keyboard.KEY_NONE, 11, Category.OTHER);
  }
  
  public void onDisable() {
    super.onDisable();
  }
  
  public void onUpdate() {
    if (isToggled()) {
      super.onUpdate();
    } 
  }
}