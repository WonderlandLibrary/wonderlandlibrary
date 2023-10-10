package optifine;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;

public class PlayerConfigurations {
   private static Map mapConfigurations = null;

   public static void renderPlayerItems(ModelBiped var0, AbstractClientPlayer var1, float var2, float var3) {
      PlayerConfiguration var4 = getPlayerConfiguration(var1);
      if (var4 != null) {
         var4.renderPlayerItems(var0, var1, var2, var3);
      }

   }

   public static synchronized PlayerConfiguration getPlayerConfiguration(AbstractClientPlayer var0) {
      String var1 = var0.getNameClear();
      if (var1 == null) {
         return null;
      } else {
         PlayerConfiguration var2 = (PlayerConfiguration)getMapConfigurations().get(var1);
         if (var2 == null) {
            var2 = new PlayerConfiguration();
            getMapConfigurations().put(var1, var2);
            PlayerConfigurationReceiver var3 = new PlayerConfigurationReceiver(var1);
            String var4 = "http://s.optifine.net/users/" + var1 + ".cfg";
            FileDownloadThread var5 = new FileDownloadThread(var4, var3);
            var5.start();
         }

         return var2;
      }
   }

   public static synchronized void setPlayerConfiguration(String var0, PlayerConfiguration var1) {
      getMapConfigurations().put(var0, var1);
   }

   private static Map getMapConfigurations() {
      if (mapConfigurations == null) {
         mapConfigurations = new HashMap();
      }

      return mapConfigurations;
   }
}
