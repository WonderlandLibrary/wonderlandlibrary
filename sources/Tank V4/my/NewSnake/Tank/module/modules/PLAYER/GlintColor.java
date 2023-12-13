package my.NewSnake.Tank.module.modules.PLAYER;

import java.awt.Color;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;

@Module.Mod(
   displayName = "Rainbow Enchant"
)
public class GlintColor extends Module {
   @Option.Op
   public static boolean Rainbow;

   public static int getRainbow(int var0, int var1) {
      float var2 = (float)((System.currentTimeMillis() * 2L + (long)(var1 / 2)) % (long)var0 * 2L);
      var2 /= (float)var0;
      return Color.getHSBColor(var2, 1.0F, 1.0F).getRGB();
   }
}
