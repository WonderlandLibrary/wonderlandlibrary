package my.NewSnake.Tank.module.modules.WORLD;

import my.NewSnake.Tank.friend.FriendManager;
import my.NewSnake.Tank.gui.click.ClickGui;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.module.ModuleManager;
import my.NewSnake.Tank.option.OptionManager;
import my.NewSnake.utils.ClientUtils;

@Module.Mod(
   displayName = "Load Config"
)
public class Reload extends Module {
   public void enable() {
      ClientUtils.mc().currentScreen = null;
      ModuleManager.load();
      OptionManager.load();
      FriendManager.load();
      ClickGui.getInstance().load();
   }
}
