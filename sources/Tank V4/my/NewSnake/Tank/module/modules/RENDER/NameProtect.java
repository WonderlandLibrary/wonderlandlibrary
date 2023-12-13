package my.NewSnake.Tank.module.modules.RENDER;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;

@Module.Mod(
   displayName = "Name Protect",
   shown = false
)
public class NameProtect extends Module {
   @Option.Op
   private boolean colored = true;

   public boolean getColored() {
      return this.colored;
   }
}
