package cn.hanabi.modules.modules.render;

import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.value.Value;

public class HudWindow extends Mod {
   public Value sessionInfo = new Value("HudWindow", "Session Info", false);
   public Value plyInventory = new Value("HudWindow", "Inventory", false);
   public Value scoreboard = new Value("HudWindow", "Scoreboard", false);
   public Value radar = new Value("HudWindow", "Radar", false);

   public HudWindow() {
      super("HudWindow", Category.RENDER, false, false, -1);
   }
}
