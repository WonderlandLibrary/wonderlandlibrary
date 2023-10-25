package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;

public class LayerVillagerArmor extends LayerBipedArmor {
  public LayerVillagerArmor(RendererLivingEntity<?> rendererIn) {
    super(rendererIn);
  }
  
  protected void initArmor() {
    this.field_177189_c = (ModelBase)new ModelZombieVillager(0.5F, 0.0F, true);
    this.field_177186_d = (ModelBase)new ModelZombieVillager(1.0F, 0.0F, true);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\renderer\entity\layers\LayerVillagerArmor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */