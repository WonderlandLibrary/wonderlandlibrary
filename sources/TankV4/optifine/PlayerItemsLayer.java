package optifine;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;

public class PlayerItemsLayer implements LayerRenderer {
   private RenderPlayer renderPlayer = null;

   public PlayerItemsLayer(RenderPlayer var1) {
      this.renderPlayer = var1;
   }

   public void doRenderLayer(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.renderEquippedItems(var1, var8, var4);
   }

   protected void renderEquippedItems(EntityLivingBase var1, float var2, float var3) {
      if (Config.isShowCapes() && var1 instanceof AbstractClientPlayer) {
         AbstractClientPlayer var4 = (AbstractClientPlayer)var1;
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.disableRescaleNormal();
         GlStateManager.enableCull();
         ModelPlayer var5 = this.renderPlayer.getMainModel();
         PlayerConfigurations.renderPlayerItems(var5, var4, var2, var3);
         GlStateManager.disableCull();
      }

   }

   public boolean shouldCombineTextures() {
      return false;
   }

   public static void register(Map var0) {
      Set var1 = var0.keySet();
      boolean var2 = false;
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         Object var3 = var4.next();
         Object var5 = var0.get(var3);
         if (var5 instanceof RenderPlayer) {
            RenderPlayer var6 = (RenderPlayer)var5;
            var6.addLayer(new PlayerItemsLayer(var6));
            var2 = true;
         }
      }

      if (!var2) {
         Config.warn("PlayerItemsLayer not registered");
      }

   }
}
