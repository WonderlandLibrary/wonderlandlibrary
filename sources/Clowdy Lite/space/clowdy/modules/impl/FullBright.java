package space.clowdy.modules.impl;

import space.clowdy.modules.Category;
import space.clowdy.modules.Module;

public class FullBright extends Module {
     public FullBright() {
          super("FullBright", "\u001f>2KH05B O@:>ABL", 0, Category.VISUAL);
     }

     public void ぜ() {
          this.mc.gameSettings.gamma = 100.0D;
     }

     public void ず() {
          this.mc.gameSettings.gamma = 15.0D;
     }
}
