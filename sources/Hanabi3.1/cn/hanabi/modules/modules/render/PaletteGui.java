package cn.hanabi.modules.modules.render;

import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;

public class PaletteGui extends Mod {
   public me.yarukon.palette.PaletteGui pGui = null;

   public PaletteGui() {
      super("APaletteGui", Category.RENDER);
   }

   protected void onEnable() {
      super.onEnable();
      if (this.pGui == null) {
         this.pGui = new me.yarukon.palette.PaletteGui();
      }

      mc.displayGuiScreen(this.pGui);
      this.set(false);
   }
}
