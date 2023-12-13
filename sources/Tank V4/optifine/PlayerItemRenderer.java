package optifine;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class PlayerItemRenderer {
   private int attachTo = 0;
   private float scaleFactor = 0.0F;
   private ModelRenderer modelRenderer = null;

   public PlayerItemRenderer(int var1, float var2, ModelRenderer var3) {
      this.attachTo = var1;
      this.scaleFactor = var2;
      this.modelRenderer = var3;
   }

   public ModelRenderer getModelRenderer() {
      return this.modelRenderer;
   }

   public void render(ModelBiped var1, float var2) {
      ModelRenderer var3 = PlayerItemModel.getAttachModel(var1, this.attachTo);
      if (var3 != null) {
         var3.postRender(var2);
      }

      this.modelRenderer.render(var2 * this.scaleFactor);
   }
}
