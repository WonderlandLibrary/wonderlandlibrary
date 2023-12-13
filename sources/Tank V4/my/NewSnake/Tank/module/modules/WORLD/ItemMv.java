package my.NewSnake.Tank.module.modules.WORLD;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;

@Module.Mod(
   displayName = "Item Move Screen"
)
public class ItemMv extends Module {
   @Option.Op(
      min = -0.5D,
      max = 2.0D,
      increment = 0.1D,
      name = "Move2"
   )
   public static double Move2;
   @Option.Op(
      min = -0.5D,
      max = 2.0D,
      increment = 0.1D,
      name = "Move1"
   )
   public static double Move1;
   @Option.Op(
      min = -0.5D,
      max = 1.0D,
      increment = 0.1D,
      name = "Move3"
   )
   public static double Move3;
}
