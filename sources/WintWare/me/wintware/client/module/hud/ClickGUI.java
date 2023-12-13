package me.wintware.client.module.hud;

import me.wintware.client.Main;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;

public class ClickGUI extends Module {
   public ClickGUI() {
      super("ClickGui", Category.Hud);
      this.setKey(54);
   }

   public void onEnable() {
      super.onEnable();
      mc.displayGuiScreen(Main.instance.clickGui1);
      mc.gameSettings.guiScale = 2;
      this.toggle();
   }
}
