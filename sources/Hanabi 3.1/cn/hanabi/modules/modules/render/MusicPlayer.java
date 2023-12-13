package cn.hanabi.modules.modules.render;

import cn.hanabi.Hanabi;
import cn.hanabi.gui.cloudmusic.ui.MusicPlayerUI;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;

public class MusicPlayer extends Mod {
   public MusicPlayer() {
      super("MusicPlayer", Category.RENDER);
   }

   public void onEnable() {
      if (mc.currentScreen instanceof MusicPlayerUI) {
         this.setState(false);
      } else {
         mc.displayGuiScreen(Hanabi.INSTANCE.mpui);
         this.setState(false);
      }
   }
}
