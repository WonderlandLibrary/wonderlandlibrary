package my.NewSnake.Tank.cosmetics;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;

public class CosmeticModelBase extends ModelBase {
   protected final ModelBiped playerModel;

   public CosmeticModelBase(RenderPlayer var1) {
      this.playerModel = var1.getMainModel();
   }
}
