package net.minecraft.client.renderer.entity;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerVillagerArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;

public class RenderZombie extends RenderBiped<EntityZombie> {
  private static final ResourceLocation zombieTextures = new ResourceLocation("textures/entity/zombie/zombie.png");
  
  private static final ResourceLocation zombieVillagerTextures = new ResourceLocation("textures/entity/zombie/zombie_villager.png");
  
  private final ModelBiped field_82434_o;
  
  private final ModelZombieVillager zombieVillagerModel;
  
  private final List<LayerRenderer<EntityZombie>> field_177121_n;
  
  private final List<LayerRenderer<EntityZombie>> field_177122_o;
  
  public RenderZombie(RenderManager renderManagerIn) {
    super(renderManagerIn, (ModelBiped)new ModelZombie(), 0.5F, 1.0F);
    LayerRenderer layerrenderer = this.layerRenderers.get(0);
    this.field_82434_o = this.modelBipedMain;
    this.zombieVillagerModel = new ModelZombieVillager();
    addLayer(new LayerHeldItem(this));
    LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this) {
        protected void initArmor() {
          this.field_177189_c = (ModelBase)new ModelZombie(0.5F, true);
          this.field_177186_d = (ModelBase)new ModelZombie(1.0F, true);
        }
      };
    addLayer(layerbipedarmor);
    this.field_177122_o = Lists.newArrayList(this.layerRenderers);
    if (layerrenderer instanceof LayerCustomHead) {
      removeLayer(layerrenderer);
      addLayer(new LayerCustomHead(this.zombieVillagerModel.bipedHead));
    } 
    removeLayer(layerbipedarmor);
    addLayer(new LayerVillagerArmor(this));
    this.field_177121_n = Lists.newArrayList(this.layerRenderers);
  }
  
  public void doRender(EntityZombie entity, double x, double y, double z, float entityYaw, float partialTicks) {
    func_82427_a(entity);
    super.doRender(entity, x, y, z, entityYaw, partialTicks);
  }
  
  protected ResourceLocation getEntityTexture(EntityZombie entity) {
    return entity.isVillager() ? zombieVillagerTextures : zombieTextures;
  }
  
  private void func_82427_a(EntityZombie zombie) {
    if (zombie.isVillager()) {
      this.mainModel = (ModelBase)this.zombieVillagerModel;
      this.layerRenderers = this.field_177121_n;
    } else {
      this.mainModel = (ModelBase)this.field_82434_o;
      this.layerRenderers = this.field_177122_o;
    } 
    this.modelBipedMain = (ModelBiped)this.mainModel;
  }
  
  protected void rotateCorpse(EntityZombie bat, float p_77043_2_, float p_77043_3_, float partialTicks) {
    if (bat.isConverting())
      p_77043_3_ += (float)(Math.cos(bat.ticksExisted * 3.25D) * Math.PI * 0.25D); 
    super.rotateCorpse(bat, p_77043_2_, p_77043_3_, partialTicks);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\renderer\entity\RenderZombie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */