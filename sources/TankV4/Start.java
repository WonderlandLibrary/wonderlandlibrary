import java.util.Arrays;
import net.minecraft.client.main.Main;

public class Start {
   public static Object[] concat(Object[] var0, Object[] var1) {
      Object[] var2 = Arrays.copyOf(var0, var0.length + var1.length);
      System.arraycopy(var1, 0, var2, var0.length, var1.length);
      return var2;
   }

   public static void main(String[] var0) {
      Main.main((String[])concat(new String[]{"--version", "mcp", "--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}"}, var0));
   }
}
