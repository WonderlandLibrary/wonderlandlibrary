package optifine;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;

public class PlayerConfiguration {
   private PlayerItemModel[] playerItemModels = new PlayerItemModel[0];
   private boolean initialized = false;

   public void renderPlayerItems(ModelBiped var1, AbstractClientPlayer var2, float var3, float var4) {
      if (this.initialized) {
         for(int var5 = 0; var5 < this.playerItemModels.length; ++var5) {
            PlayerItemModel var6 = this.playerItemModels[var5];
            var6.render(var1, var2, var3, var4);
         }
      }

   }

   public boolean isInitialized() {
      return this.initialized;
   }

   public void setInitialized(boolean var1) {
      this.initialized = var1;
   }

   public PlayerItemModel[] getPlayerItemModels() {
      return this.playerItemModels;
   }

   public void addPlayerItemModel(PlayerItemModel var1) {
      this.playerItemModels = (PlayerItemModel[])Config.addObjectToArray(this.playerItemModels, var1);
   }
}
