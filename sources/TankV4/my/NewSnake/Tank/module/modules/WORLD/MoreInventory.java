package my.NewSnake.Tank.module.modules.WORLD;

import my.NewSnake.Tank.module.Module;

@Module.Mod(
   displayName = "More Inventory"
)
public class MoreInventory extends Module {
   public static boolean cancelClose() {
      return (new MoreInventory()).getInstance().isEnabled();
   }
}
