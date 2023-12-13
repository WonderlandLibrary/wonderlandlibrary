package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelArmorStand;
import net.minecraft.client.model.ModelArmorStandArmor;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.ResourceLocation;

public class ArmorStandRenderer extends RendererLivingEntity {
   public static final ResourceLocation TEXTURE_ARMOR_STAND = new ResourceLocation("textures/entity/armorstand/wood.png");

   protected boolean canRenderName(EntityLivingBase var1) {
      return this.canRenderName((EntityArmorStand)var1);
   }

   protected void rotateCorpse(EntityArmorStand var1, float var2, float var3, float var4) {
      GlStateManager.rotate(180.0F - var3, 0.0F, 1.0F, 0.0F);
   }

   public ModelBase getMainModel() {
      return this.getMainModel();
   }

   public ArmorStandRenderer(RenderManager var1) {
      super(var1, new ModelArmorStand(), 0.0F);
      LayerBipedArmor var2 = new LayerBipedArmor(this, this) {
         final ArmorStandRenderer this$0;

         protected void initArmor() {
            this.field_177189_c = new ModelArmorStandArmor(0.5F);
            this.field_177186_d = new ModelArmorStandArmor(1.0F);
         }

         {
            this.this$0 = var1;
         }
      };
      this.addLayer(var2);
      this.addLayer(new LayerHeldItem(this));
      this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
   }

   protected ResourceLocation getEntityTexture(Entity var1) {
      return this.getEntityTexture((EntityArmorStand)var1);
   }

   protected ResourceLocation getEntityTexture(EntityArmorStand var1) {
      return TEXTURE_ARMOR_STAND;
   }

   public ModelArmorStand getMainModel() {
      return (ModelArmorStand)super.getMainModel();
   }

   protected boolean canRenderName(EntityArmorStand var1) {
      return var1.getAlwaysRenderNameTag();
   }

   protected void rotateCorpse(EntityLivingBase var1, float var2, float var3, float var4) {
      this.rotateCorpse((EntityArmorStand)var1, var2, var3, var4);
   }
}
