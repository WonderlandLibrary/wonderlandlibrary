package artemis.mods.Render;

import artemis.Category;
import artemis.module;

public class Fullbright extends module {
  public Fullbright() {
    super("Fullbright", "Infinite night vision", 46, 7, Category.RENDER);
  }
  
  public void onUpdate() {
    if (isToggled()) {
      this.mc.gameSettings.gammaSetting = 100.0F;
    } else {
      this.mc.gameSettings.gammaSetting = 0.0F;
    } 
  }
}
