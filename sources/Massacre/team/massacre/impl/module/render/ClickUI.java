package team.massacre.impl.module.render;

import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.ui.csgo.SkeetUI;

public class ClickUI extends Module {
   public ClickUI() {
      super("ClickUI", 54, Category.RENDER);
   }

   public void onEnable() {
      super.onEnable();
      SkeetUI.init();
      this.toggle();
   }
}
