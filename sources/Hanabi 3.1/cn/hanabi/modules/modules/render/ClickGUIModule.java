package cn.hanabi.modules.modules.render;

import cn.hanabi.gui.newclickui.ClickUI;
import cn.hanabi.gui.superskidder.material.clickgui.themes.Classic;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.utils.SoundFxPlayer;
import cn.hanabi.value.Value;
import net.minecraft.client.gui.ScaledResolution;

public class ClickGUIModule extends Mod {
   public static ClickUI newgui = new ClickUI();
   public static Classic material = new Classic();
   public static Value theme = new Value("ClickGUI", "Theme", 0);
   public static Value blur = new Value("ClickGUI", "Blur", true);
   ScaledResolution sr;
   int lastWidth = 0;

   public ClickGUIModule() {
      super("ClickGUI", Category.RENDER, true, false, 54);
      theme.addValue("Dark");
      theme.addValue("Light");
      theme.addValue("Material");
      this.setState(false);
   }

   protected void onEnable() {
      if (mc.thePlayer != null) {
         if (!(mc.currentScreen instanceof ClickUI) && !(mc.currentScreen instanceof Classic)) {
            if (!((HUD)ModManager.getModule("HUD")).sound.isCurrentMode("Minecraft")) {
               (new SoundFxPlayer()).playSound(SoundFxPlayer.SoundType.ClickGuiOpen, -4.0F);
            }

            if (theme.isCurrentMode("Material")) {
               mc.displayGuiScreen(new Classic());
            } else {
               mc.displayGuiScreen(newgui);
            }

            this.setState(false);
         } else {
            this.setState(false);
         }
      }
   }
}
