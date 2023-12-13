package net.minecraft.client.renderer.entity;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import team.massacre.Massacre;

public class RenderEntityItem extends Render<EntityItem> {
   private final RenderItem itemRenderer;
   private Random field_177079_e = new Random();

   public RenderEntityItem(RenderManager renderManagerIn, RenderItem p_i46167_2_) {
      super(renderManagerIn);
      this.itemRenderer = p_i46167_2_;
      this.shadowSize = 0.15F;
      this.shadowOpaque = 0.75F;
   }

   private int func_177077_a(EntityItem itemIn, double p_177077_2_, double p_177077_4_, double p_177077_6_, float p_177077_8_, IBakedModel p_177077_9_) {
      ItemStack itemstack;
      Item item;
      boolean flag;
      int i;
      float f;
      float f1;
      float f2;
      float f6;
      float f4;
      float f5;
      if (Massacre.INSTANCE.getModuleManager().getModule("Item Physics").getState()) {
         itemstack = itemIn.getEntityItem();
         item = itemstack.getItem();
         if (item == null) {
            return 0;
         } else {
            flag = p_177077_9_.isGui3d();
            i = this.func_177078_a(itemstack);
            f = 0.25F;
            f1 = -0.13F;
            f2 = p_177077_9_.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;
            GlStateManager.translate((float)p_177077_2_, (float)p_177077_4_ + f1 + 0.25F * f2, (float)p_177077_6_);
            if (!(item instanceof ItemBlock) || ((ItemBlock)item).getBlock().isPassable(Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getPosition())) {
               f1 = (float)((double)f1 - 0.1D);
            }

            if (flag || this.renderManager.options != null) {
               f6 = (((float)itemIn.getAge() + p_177077_8_) / 20.0F + itemIn.hoverStart) * 57.295776F;
               double var;
               if (itemIn.onGround) {
                  var = (itemIn.posX + itemIn.motionX * (double)Minecraft.getMinecraft().timer.renderPartialTicks) * 200.0D + (itemIn.posZ + itemIn.motionZ * (double)Minecraft.getMinecraft().timer.renderPartialTicks) * 200.0D;
                  GlStateManager.rotate((float)var, 0.0F, 1.0F, 0.0F);
                  if (!(item instanceof ItemBlock) || ((ItemBlock)item).getBlock().isPassable(Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getPosition())) {
                     GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                  }
               } else {
                  var = (itemIn.posX + itemIn.motionX * (double)Minecraft.getMinecraft().timer.renderPartialTicks) * 200.0D;
                  double y = (itemIn.posY + itemIn.motionY * (double)Minecraft.getMinecraft().timer.renderPartialTicks) * 200.0D;
                  double z = (itemIn.posZ + itemIn.motionZ * (double)Minecraft.getMinecraft().timer.renderPartialTicks) * 200.0D;
                  GlStateManager.rotate((float)var, 1.0F, 0.0F, 0.0F);
                  GlStateManager.rotate((float)y, 0.0F, 1.0F, 0.0F);
                  GlStateManager.rotate((float)z, 0.0F, 0.0F, 1.0F);
               }
            }

            if (!flag) {
               f6 = -0.0F * (float)(i - 1) * 0.5F;
               f4 = -0.0F * (float)(i - 1) * 0.5F;
               f5 = -0.046875F * (float)(i - 1) * 0.5F;
               GlStateManager.translate(f6, f4, f5);
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            return i;
         }
      } else {
         itemstack = itemIn.getEntityItem();
         item = itemstack.getItem();
         if (item == null) {
            return 0;
         } else {
            flag = p_177077_9_.isGui3d();
            i = this.func_177078_a(itemstack);
            f = 0.25F;
            f1 = MathHelper.sin(((float)itemIn.getAge() + p_177077_8_) / 10.0F + itemIn.hoverStart) * 0.1F + 0.1F;
            f2 = p_177077_9_.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;
            GlStateManager.translate((float)p_177077_2_, (float)p_177077_4_ + f1 + 0.25F * f2, (float)p_177077_6_);
            if (flag || this.renderManager.options != null) {
               f6 = (((float)itemIn.getAge() + p_177077_8_) / 20.0F + itemIn.hoverStart) * 57.295776F;
               GlStateManager.rotate(f6, 0.0F, 1.0F, 0.0F);
            }

            if (!flag) {
               f6 = -0.0F * (float)(i - 1) * 0.5F;
               f4 = -0.0F * (float)(i - 1) * 0.5F;
               f5 = -0.046875F * (float)(i - 1) * 0.5F;
               GlStateManager.translate(f6, f4, f5);
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            return i;
         }
      }
   }

   private int func_177078_a(ItemStack stack) {
      int i = 1;
      if (stack.stackSize > 48) {
         i = 5;
      } else if (stack.stackSize > 32) {
         i = 4;
      } else if (stack.stackSize > 16) {
         i = 3;
      } else if (stack.stackSize > 1) {
         i = 2;
      }

      return i;
   }

   public void doRender(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks) {
      ItemStack itemstack = entity.getEntityItem();
      this.field_177079_e.setSeed(187L);
      boolean flag = false;
      if (this.bindEntityTexture(entity)) {
         this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).setBlurMipmap(false, false);
         flag = true;
      }

      GlStateManager.enableRescaleNormal();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.pushMatrix();
      IBakedModel ibakedmodel = this.itemRenderer.getItemModelMesher().getItemModel(itemstack);
      int i = this.func_177077_a(entity, x, y, z, partialTicks, ibakedmodel);

      for(int j = 0; j < i; ++j) {
         float f;
         float f1;
         float f2;
         if (ibakedmodel.isGui3d()) {
            GlStateManager.pushMatrix();
            if (j > 0) {
               f = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
               f1 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
               f2 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
               GlStateManager.translate(f, f1, f2);
            }

            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            ibakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GROUND);
            this.itemRenderer.renderItem(itemstack, ibakedmodel);
            GlStateManager.popMatrix();
         } else {
            GlStateManager.pushMatrix();
            ibakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GROUND);
            this.itemRenderer.renderItem(itemstack, ibakedmodel);
            GlStateManager.popMatrix();
            f = ibakedmodel.getItemCameraTransforms().ground.scale.x;
            f1 = ibakedmodel.getItemCameraTransforms().ground.scale.y;
            f2 = ibakedmodel.getItemCameraTransforms().ground.scale.z;
            GlStateManager.translate(0.0F * f, 0.0F * f1, 0.046875F * f2);
         }
      }

      GlStateManager.popMatrix();
      GlStateManager.disableRescaleNormal();
      GlStateManager.disableBlend();
      this.bindEntityTexture(entity);
      if (flag) {
         this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).restoreLastBlurMipmap();
      }

      super.doRender(entity, x, y, z, entityYaw, partialTicks);
   }

   protected ResourceLocation getEntityTexture(EntityItem entity) {
      return TextureMap.locationBlocksTexture;
   }
}
