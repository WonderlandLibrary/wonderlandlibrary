package my.NewSnake.Tank.module.modules.WORLD;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;

@Module.Mod
public class Ceu extends Module {
   @Option.Op(
      min = 0.0D,
      max = 10.0D,
      increment = 0.1D
   )
   public static double Vermelho;
   @Option.Op
   public static boolean Ativo;
   @Option.Op(
      min = 0.0D,
      max = 10.0D,
      increment = 0.1D
   )
   public static double Azul;
   @Option.Op(
      min = 0.0D,
      max = 10.0D,
      increment = 0.1D
   )
   public static double Verde;
}
