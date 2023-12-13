package my.NewSnake.Tank.module.modules.RENDER;

import my.NewSnake.Tank.gui.click.ClickGui;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.utils.ClientUtils;

@Module.Mod(
   displayName = "Click Gui",
   keybind = 54,
   shown = false
)
public class Gui extends Module {
   @Option.Op
   private boolean darkTheme;

   public boolean isDarkTheme() {
      return this.darkTheme;
   }

   public void enable() {
      ClientUtils.mc().displayGuiScreen(ClickGui.getInstance());
   }
}
