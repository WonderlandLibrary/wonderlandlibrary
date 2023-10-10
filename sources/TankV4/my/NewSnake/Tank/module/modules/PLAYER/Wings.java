package my.NewSnake.Tank.module.modules.PLAYER;

import java.awt.Color;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;

@Module.Mod
public class Wings extends Module {
   @Option.Op(
      min = 0.0D,
      max = 255.0D,
      increment = 1.0D
   )
   public static double colorGreen;
   @Option.Op
   public static boolean Wing;
   @Option.Op
   public static boolean rainbowWings;
   @Option.Op(
      min = 5.0D,
      max = 400.0D,
      increment = 1.0D
   )
   public static double wingsSize;
   @Option.Op(
      min = 0.0D,
      max = 255.0D,
      increment = 1.0D
   )
   public static double colorBlue;
   @Option.Op(
      min = 0.0D,
      max = 255.0D,
      increment = 1.0D
   )
   public static double colorRed;

   public static GameSettings getGameSettings() {
      return getMinecraft().gameSettings;
   }

   public static Color fade(long var0, float var2) {
      float var3 = (float)(System.nanoTime() + var0) / 1.0E10F % 1.0F;
      long var4 = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(var3, 1.0F, 1.0F)), 16);
      Color var6 = new Color((int)var4);
      return new Color((float)var6.getRed() / 255.0F * var2, (float)var6.getGreen() / 255.0F * var2, (float)var6.getBlue() / 255.0F * var2, (float)var6.getAlpha() / 155.0F);
   }

   public static EntityPlayerSP getPlayer() {
      getMinecraft();
      Minecraft.getMinecraft();
      return Minecraft.thePlayer;
   }

   public static Minecraft getMinecraft() {
      return Minecraft.getMinecraft();
   }

   public static int getRainbow(int var0, int var1) {
      float var2 = (float)((System.currentTimeMillis() * 1L + (long)(var1 / 2)) % (long)var0 * 2L);
      var2 /= (float)var0;
      return Color.getHSBColor(var2, 1.0F, 1.0F).getRGB();
   }
}
