package net.minecraft.client.renderer;

import my.NewSnake.Tank.module.ModuleManager;
import my.NewSnake.Tank.module.modules.PLAYER.SwordAnimations;
import my.NewSnake.Tank.module.modules.WORLD.ItemMv;
import my.NewSnake.utils.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import optifine.Config;
import optifine.DynamicLights;
import optifine.Reflector;
import org.lwjgl.opengl.GL11;
import shadersmod.client.Shaders;

public class ItemRenderer {
   public final RenderManager renderManager;
   private final Minecraft mc;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$item$EnumAction;
   private static final ResourceLocation RES_UNDERWATER_OVERLAY = new ResourceLocation("textures/misc/underwater.png");
   private static final String __OBFID = "CL_00000953";
   private float prevEquippedProgress;
   private float equippedProgress;
   private float delay = 0.0F;
   private final RenderItem itemRenderer;
   private float rotate;
   private Timer rotateTimer = new Timer();
   private int equippedItemSlot = -1;
   public ItemStack itemToRender;
   private static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");

   private void renderFireInFirstPerson(float var1) {
      Tessellator var2 = Tessellator.getInstance();
      WorldRenderer var3 = var2.getWorldRenderer();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 0.9F);
      GlStateManager.depthFunc(519);
      GlStateManager.depthMask(false);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      float var4 = 1.0F;

      for(int var5 = 0; var5 < 2; ++var5) {
         GlStateManager.pushMatrix();
         TextureAtlasSprite var6 = this.mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/fire_layer_1");
         this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
         float var7 = var6.getMinU();
         float var8 = var6.getMaxU();
         float var9 = var6.getMinV();
         float var10 = var6.getMaxV();
         float var11 = (0.0F - var4) / 2.0F;
         float var12 = var11 + var4;
         float var13 = 0.0F - var4 / 2.0F;
         float var14 = var13 + var4;
         float var15 = -0.5F;
         GlStateManager.translate((float)(-(var5 * 2 - 1)) * 0.24F, -0.3F, 0.0F);
         GlStateManager.rotate((float)(var5 * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
         var3.begin(7, DefaultVertexFormats.POSITION_TEX);
         var3.pos((double)var11, (double)var13, (double)var15).tex((double)var8, (double)var10).endVertex();
         var3.pos((double)var12, (double)var13, (double)var15).tex((double)var7, (double)var10).endVertex();
         var3.pos((double)var12, (double)var14, (double)var15).tex((double)var7, (double)var9).endVertex();
         var3.pos((double)var11, (double)var14, (double)var15).tex((double)var8, (double)var9).endVertex();
         var2.draw();
         GlStateManager.popMatrix();
      }

      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.disableBlend();
      GlStateManager.depthMask(true);
      GlStateManager.depthFunc(515);
   }

   public void resetEquippedProgress2() {
      this.equippedProgress = 0.0F;
   }

   private void renderPlayerArms(AbstractClientPlayer var1) {
      this.mc.getTextureManager().bindTexture(var1.getLocationSkin());
      Render var2 = this.renderManager.getEntityRenderObject(Minecraft.thePlayer);
      RenderPlayer var3 = (RenderPlayer)var2;
      if (!var1.isInvisible()) {
         GlStateManager.disableCull();
         this.renderRightArm(var3);
         this.renderLeftArm(var3);
         GlStateManager.enableCull();
      }

   }

   private void func_178103_d() {
      GlStateManager.translate(-0.5F, 0.2F, 0.0F);
      GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
   }

   private void renderWaterOverlayTexture(float var1) {
      if (!Config.isShaders() || Shaders.isUnderwaterOverlay()) {
         this.mc.getTextureManager().bindTexture(RES_UNDERWATER_OVERLAY);
         Tessellator var2 = Tessellator.getInstance();
         WorldRenderer var3 = var2.getWorldRenderer();
         float var4 = Minecraft.thePlayer.getBrightness(var1);
         GlStateManager.color(var4, var4, var4, 0.5F);
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         GlStateManager.pushMatrix();
         float var5 = 4.0F;
         float var6 = -1.0F;
         float var7 = 1.0F;
         float var8 = -1.0F;
         float var9 = 1.0F;
         float var10 = -0.5F;
         float var11 = -Minecraft.thePlayer.rotationYaw / 64.0F;
         float var12 = Minecraft.thePlayer.rotationPitch / 64.0F;
         var3.begin(7, DefaultVertexFormats.POSITION_TEX);
         var3.pos(-1.0D, -1.0D, -0.5D).tex((double)(4.0F + var11), (double)(4.0F + var12)).endVertex();
         var3.pos(1.0D, -1.0D, -0.5D).tex((double)(0.0F + var11), (double)(4.0F + var12)).endVertex();
         var3.pos(1.0D, 1.0D, -0.5D).tex((double)(0.0F + var11), (double)(0.0F + var12)).endVertex();
         var3.pos(-1.0D, 1.0D, -0.5D).tex((double)(4.0F + var11), (double)(0.0F + var12)).endVertex();
         var2.draw();
         GlStateManager.popMatrix();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.disableBlend();
      }

   }

   private void renderLeftArm(RenderPlayer var1) {
      GlStateManager.pushMatrix();
      GlStateManager.rotate(92.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(41.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.translate(-0.3F, -1.1F, 0.45F);
      var1.renderLeftArm(Minecraft.thePlayer);
      GlStateManager.popMatrix();
   }

   private void func_178104_a(AbstractClientPlayer var1, float var2) {
      float var3 = (float)var1.getItemInUseCount() - var2 + 1.0F;
      float var4 = var3 / (float)this.itemToRender.getMaxItemUseDuration();
      float var5 = MathHelper.abs(MathHelper.cos(var3 / 4.0F * 3.1415927F) * 0.1F);
      if (var4 >= 0.8F) {
         var5 = 0.0F;
      }

      GlStateManager.translate(0.0F, var5, 0.0F);
      float var6 = 1.0F - (float)Math.pow((double)var4, 27.0D);
      GlStateManager.translate(var6 * 0.6F, var6 * -0.5F, var6 * 0.0F);
      GlStateManager.rotate(var6 * 90.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(var6 * 10.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(var6 * 30.0F, 0.0F, 0.0F, 1.0F);
   }

   private void func_178098_a(float var1, AbstractClientPlayer var2) {
      GlStateManager.rotate(-18.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate(-12.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-8.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.translate(-0.9F, 0.2F, 0.0F);
      float var3 = (float)this.itemToRender.getMaxItemUseDuration() - ((float)var2.getItemInUseCount() - var1 + 1.0F);
      float var4 = var3 / 20.0F;
      var4 = (var4 * var4 + var4 * 2.0F) / 3.0F;
      if (var4 > 1.0F) {
         var4 = 1.0F;
      }

      if (var4 > 0.1F) {
         float var5 = MathHelper.sin((var3 - 0.1F) * 1.3F);
         float var6 = var4 - 0.1F;
         float var7 = var5 * var6;
         GlStateManager.translate(var7 * 0.0F, var7 * 0.01F, var7 * 0.0F);
      }

      GlStateManager.translate(var4 * 0.0F, var4 * 0.0F, var4 * 0.1F);
      GlStateManager.scale(1.0F, 1.0F, 1.0F + var4 * 0.2F);
   }

   private void renderItemMap(AbstractClientPlayer var1, float var2, float var3, float var4) {
      float var5 = -0.4F * MathHelper.sin(MathHelper.sqrt_float(var4) * 3.1415927F);
      float var6 = 0.2F * MathHelper.sin(MathHelper.sqrt_float(var4) * 3.1415927F * 2.0F);
      float var7 = -0.2F * MathHelper.sin(var4 * 3.1415927F);
      GlStateManager.translate(var5, var6, var7);
      float var8 = this.func_178100_c(var2);
      GlStateManager.translate(0.0F, 0.04F, -0.72F);
      GlStateManager.translate(0.0F, var3 * -1.2F, 0.0F);
      GlStateManager.translate(0.0F, var8 * -0.5F, 0.0F);
      GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(var8 * -85.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
      this.renderPlayerArms(var1);
      float var9 = MathHelper.sin(var4 * var4 * 3.1415927F);
      float var10 = MathHelper.sin(MathHelper.sqrt_float(var4) * 3.1415927F);
      GlStateManager.rotate(var9 * -20.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(var10 * -20.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate(var10 * -80.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.scale(0.38F, 0.38F, 0.38F);
      GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.translate(-1.0F, -1.0F, 0.0F);
      GlStateManager.scale(0.015625F, 0.015625F, 0.015625F);
      this.mc.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
      Tessellator var11 = Tessellator.getInstance();
      WorldRenderer var12 = var11.getWorldRenderer();
      GL11.glNormal3f(0.0F, 0.0F, -1.0F);
      var12.begin(7, DefaultVertexFormats.POSITION_TEX);
      var12.pos(-7.0D, 135.0D, 0.0D).tex(0.0D, 1.0D).endVertex();
      var12.pos(135.0D, 135.0D, 0.0D).tex(1.0D, 1.0D).endVertex();
      var12.pos(135.0D, -7.0D, 0.0D).tex(1.0D, 0.0D).endVertex();
      var12.pos(-7.0D, -7.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
      var11.draw();
      MapData var13 = Items.filled_map.getMapData(this.itemToRender, Minecraft.theWorld);
      if (var13 != null) {
         this.mc.entityRenderer.getMapItemRenderer().renderMap(var13, false);
      }

   }

   private void transformFirstPersonItem(float var1, float var2) {
      GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
      GlStateManager.translate(0.0F, var1 * -0.6F, 0.0F);
      if (SwordAnimations.Item360) {
         GlStateManager.rotate(this.delay, 0.0F, 1.0F, 0.0F);
         if (this.delay > 360.0F) {
            this.delay = 0.0F;
         }

         for(int var3 = 0; (double)var3 < SwordAnimations.Velocidade; ++var3) {
            ++this.delay;
         }
      }

      if (!SwordAnimations.Item360) {
         GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
      }

      float var5 = MathHelper.sin(var2 * var2 * 3.1415927F);
      float var4 = MathHelper.sin(MathHelper.sqrt_float(var2) * 3.1415927F);
      GlStateManager.rotate(var5 * -20.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(var4 * -20.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate(var4 * -80.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.scale(0.4F, 0.4F, 0.4F);
   }

   public void renderItem(EntityLivingBase var1, ItemStack var2, ItemCameraTransforms.TransformType var3) {
      if (var2 != null) {
         Item var4 = var2.getItem();
         Block var5 = Block.getBlockFromItem(var4);
         GlStateManager.pushMatrix();
         if (this.itemRenderer.shouldRenderItemIn3D(var2)) {
            GlStateManager.scale(2.0F, 2.0F, 2.0F);
            if (var5 != null && (!Config.isShaders() || !Shaders.renderItemKeepDepthMask)) {
               GlStateManager.depthMask(false);
            }
         }

         this.itemRenderer.renderItemModelForEntity(var2, var1, var3);
         if (var5 != null) {
            GlStateManager.depthMask(true);
         }

         GlStateManager.popMatrix();
      }

   }

   private void func_178095_a(AbstractClientPlayer var1, float var2, float var3) {
      float var4 = -0.3F * MathHelper.sin(MathHelper.sqrt_float(var3) * 3.1415927F);
      float var5 = 0.4F * MathHelper.sin(MathHelper.sqrt_float(var3) * 3.1415927F * 2.0F);
      float var6 = -0.4F * MathHelper.sin(var3 * 3.1415927F);
      GlStateManager.translate(var4, var5, var6);
      GlStateManager.translate(0.64000005F, -0.6F, -0.71999997F);
      GlStateManager.translate(0.0F, var2 * -0.6F, 0.0F);
      GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
      float var7 = MathHelper.sin(var3 * var3 * 3.1415927F);
      float var8 = MathHelper.sin(MathHelper.sqrt_float(var3) * 3.1415927F);
      GlStateManager.rotate(var8 * 70.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(var7 * -20.0F, 0.0F, 0.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(var1.getLocationSkin());
      GlStateManager.translate(-1.0F, 3.6F, 3.5F);
      GlStateManager.rotate(120.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate(200.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.scale(1.0F, 1.0F, 1.0F);
      GlStateManager.translate(5.6F, 0.0F, 0.0F);
      Render var9 = this.renderManager.getEntityRenderObject(Minecraft.thePlayer);
      GlStateManager.disableCull();
      RenderPlayer var10 = (RenderPlayer)var9;
      var10.renderRightArm(Minecraft.thePlayer);
      GlStateManager.enableCull();
   }

   public void renderItemInFirstPerson(float var1) {
      if (((ItemMv)(new ItemMv()).getInstance()).isEnabled()) {
         GlStateManager.translate((double)((float)ItemMv.Move1) - 0.5D, ItemMv.Move2 - 0.5D, ItemMv.Move3 - 0.5D);
      }

      float var2 = 1.0F - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * var1);
      EntityPlayerSP var3 = Minecraft.thePlayer;
      float var4 = var3.getSwingProgress(var1);
      float var5 = var3.prevRotationPitch + (var3.rotationPitch - var3.prevRotationPitch) * var1;
      float var6 = var3.prevRotationYaw + (var3.rotationYaw - var3.prevRotationYaw) * var1;
      this.func_178101_a(var5, var6);
      this.func_178109_a(var3);
      this.func_178110_a((EntityPlayerSP)var3, var1);
      float var7 = MathHelper.sin(MathHelper.sqrt_float(var4) * 3.1415927F);
      GlStateManager.enableRescaleNormal();
      GlStateManager.pushMatrix();
      if (this.itemToRender != null) {
         if (this.itemToRender.getItem() == Items.filled_map) {
            this.renderItemMap(var3, var5, var2, var4);
         } else if (var3.getItemInUseCount() > 0) {
            EnumAction var8 = this.itemToRender.getItemUseAction();
            switch($SWITCH_TABLE$net$minecraft$item$EnumAction()[var8.ordinal()]) {
            case 1:
               this.transformFirstPersonItem(var2, 0.0F);
               break;
            case 2:
            case 3:
               this.func_178104_a(var3, var1);
               this.transformFirstPersonItem(var2, 0.0F);
               break;
            case 4:
               if (!ModuleManager.getModule("SwordAnimations").isEnabled()) {
                  this.transformFirstPersonItem(var2, 0.0F);
                  this.func_178103_d();
               } else {
                  float var9 = MathHelper.sin((float)((double)MathHelper.sqrt_float(var4) * 3.1D));
                  if (SwordAnimations.Exibido) {
                     this.transformFirstPersonItem(var2 * 0.5F, -2.0F);
                     GlStateManager.rotate(-var9 * 55.0F / 2.0F, -8.0F, -0.0F, 9.0F);
                     GlStateManager.rotate(-var9 * 45.0F, 1.0F, var9 / 2.0F, -0.0F);
                     this.func_178103_d();
                     GL11.glTranslated(1.2D, 0.3D, 0.5D);
                     GL11.glTranslatef(-1.0F, -0.1F, 0.2F);
                     GlStateManager.scale(1.5D, 1.5D, 1.5D);
                  } else {
                     if (SwordAnimations.Normal) {
                        this.transformFirstPersonItem(-0.3F, var4);
                        this.func_178103_d();
                     }

                     if (SwordAnimations.Sigma) {
                        this.transformFirstPersonItem(var2 * 0.5F, -2.0F);
                        GlStateManager.rotate(-var9 * 55.0F / 2.0F, -8.0F, -0.0F, 9.0F);
                        GlStateManager.rotate(-var9 * 45.0F, 1.0F, var9 / 2.0F, -0.0F);
                        this.func_178103_d();
                        GL11.glTranslated(1.2D, 0.3D, 0.5D);
                        GL11.glTranslatef(-1.0F, -0.1F, 0.2F);
                        GlStateManager.scale(1.5D, 1.5D, 1.5D);
                     }

                     if (SwordAnimations.Tapa) {
                        this.transformFirstPersonItem(-0.2F, var4 - 1.0F);
                        GL11.glTranslated(0.6D, -0.5D, 0.0D);
                        GlStateManager.translate(-0.5F, 0.2F, 0.0F);
                        GlStateManager.rotate(10.0F, 0.0F, 1.0F, 0.0F);
                        GlStateManager.rotate(-85.0F, 1.0F, 0.0F, 0.0F);
                        GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
                        GlStateManager.translate(-0.5F, 0.2F, 0.0F);
                     }

                     if (SwordAnimations.Remix) {
                        GL11.glTranslated(0.0D, 0.0D, 0.0D);
                        this.transformFirstPersonItem(-0.1F, 1.0F);
                        GlStateManager.rotate(-var9 * 50.0F / 2.0F, var9 / 2.0F, -0.0F, 4.0F);
                        GlStateManager.rotate(-var9 * 30.0F, 1.0F, var9 / 2.0F, -0.0F);
                        this.func_178103_d();
                     }

                     int var10;
                     if (SwordAnimations.Spinhola) {
                        this.transformFirstPersonItem(var2 - 0.2F, 0.0F);
                        GlStateManager.translate(0.0D, 0.1D, -0.1D);
                        GlStateManager.rotate(45.0F, -0.0F, 0.0F, -0.1F);
                        GlStateManager.rotate(this.delay, -0.1F, 0.1F, -0.1F);
                        if (this.delay > 360.0F) {
                           this.delay = 0.0F;
                        }

                        for(var10 = 0; (double)var10 < SwordAnimations.Velocidade; ++var10) {
                           ++this.delay;
                        }

                        this.func_178103_d();
                     }

                     if (SwordAnimations.Gira) {
                        this.transformFirstPersonItem(var2 - 0.2F, 0.0F);
                        GlStateManager.translate(0.0D, 0.1D, -0.1D);
                        GlStateManager.rotate(45.0F, -0.0F, 0.0F, -0.1F);
                        GlStateManager.rotate(this.delay, -3666.1F, 1000.1F, 1000.1F);
                        if (this.delay > 360.0F) {
                           this.delay = 0.0F;
                        }

                        for(var10 = 0; (double)var10 < SwordAnimations.Velocidade; ++var10) {
                           ++this.delay;
                        }

                        this.func_178103_d();
                     }

                     if (SwordAnimations.New) {
                        this.transformFirstPersonItem(var2 - 0.2F, 0.0F);
                        GlStateManager.translate(0.0D, 0.1D, -0.1D);
                        GlStateManager.rotate(45.0F, -1000.0F, 0.0F, -0.1F);
                        GlStateManager.rotate(this.delay, -3.1F, 6.1F, -12.1F);
                        if (this.delay > 360.0F) {
                           this.delay = 0.0F;
                        }

                        for(var10 = 0; (double)var10 < SwordAnimations.Velocidade; ++var10) {
                           ++this.delay;
                        }

                        this.func_178103_d();
                     }

                     if (SwordAnimations.Interia) {
                        this.transformFirstPersonItem(0.05F, var4);
                        GlStateManager.translate(-0.5F, 0.5F, 0.0F);
                        GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
                        GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
                        GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
                     }

                     if (SwordAnimations.Etherial) {
                        this.transformFirstPersonItem(var2, 0.0F);
                        this.func_178103_d();
                        GlStateManager.translate(-0.05F, 0.2F, 0.2F);
                        GlStateManager.rotate(-var7 * 70.0F / 2.0F, -8.0F, -0.0F, 9.0F);
                        GlStateManager.rotate(-var7 * 70.0F, 1.0F, -0.4F, -0.0F);
                     }

                     if (SwordAnimations.Stella) {
                        this.transformFirstPersonItem(-0.1F, var4);
                        GlStateManager.translate(-0.5F, 0.4F, -0.2F);
                        GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
                        GlStateManager.rotate(-70.0F, 1.0F, 0.0F, 0.0F);
                        GlStateManager.rotate(40.0F, 0.0F, 1.0F, 0.0F);
                     }

                     float var12;
                     if (SwordAnimations.Style) {
                        this.transformFirstPersonItem(var2, 0.0F);
                        this.func_178103_d();
                        var12 = MathHelper.sin((float)((double)MathHelper.sqrt_float(var4) * 3.141592653589793D));
                        GlStateManager.translate(-0.05F, 0.2F, 0.0F);
                        GlStateManager.rotate(-var12 * 70.0F / 2.0F, -8.0F, -0.0F, 9.0F);
                        GlStateManager.rotate(-var12 * 70.0F, 1.0F, -0.4F, -0.0F);
                     }

                     if (SwordAnimations.Punch) {
                        this.transformFirstPersonItem(var2, 0.0F);
                        this.func_178103_d();
                        GlStateManager.translate(0.1F, 0.2F, 0.3F);
                        GlStateManager.rotate(-var7 * 30.0F, -5.0F, 0.0F, 9.0F);
                        GlStateManager.rotate(-var7 * 10.0F, 1.0F, -0.4F, -0.5F);
                     }

                     if (SwordAnimations.Astolfo) {
                        GlStateManager.rotate(this.delay, 0.0F, 0.0F, -0.1F);
                        this.transformFirstPersonItem(var2 / 1.6F, 0.0F);
                        var12 = MathHelper.sin(MathHelper.sqrt_float(var4) * 3.0F);
                        if (Timer.hasReached(1.0D)) {
                           for(int var11 = 0; (double)var11 < SwordAnimations.Velocidade; ++var11) {
                              ++this.delay;
                           }

                           this.rotateTimer.reset();
                        }

                        if (this.delay > 360.0F) {
                           this.delay = 0.0F;
                        }

                        this.func_178103_d();
                     }

                     if (SwordAnimations.derp) {
                        this.transformFirstPersonItem(var2, var4);
                        this.func_178103_d();
                        var12 = MathHelper.sin(var4 * var4 * 5.1415925F);
                        GL11.glTranslatef(0.1F, -0.1F, 0.3F);
                        GlStateManager.translate(0.1F, -0.1F, 0.4F);
                        GlStateManager.rotate(var12 * 523.0F, 215.0F, 1.4F, 525.0F);
                     }

                     if (SwordAnimations.Nine) {
                        this.transformFirstPersonItem(var2, 0.0F);
                        this.func_178103_d();
                        var12 = MathHelper.sin((float)(Math.sqrt((double)var4) * 3.1415927410125732D));
                        GlStateManager.translate(0.0F, 0.0F, 0.5F);
                        GlStateManager.rotate(-var12 * 70.0F / 1.787898F, -8.0F, -0.0F, 9.0F);
                        GlStateManager.rotate(-var12 * 70.0F, 1.5F, -0.5F, -0.2F);
                     }

                     if (SwordAnimations.AnimationBang) {
                        this.transformFirstPersonItem(var2, 0.0F);
                        this.func_178103_d();
                        GlStateManager.translate(-0.5D, 0.0D, 0.5D);
                        GlStateManager.rotate(this.rotate, 0.0F, this.rotate, this.rotate);
                     }

                     if (SwordAnimations.VaieVem) {
                        this.transformFirstPersonItem(var2, 0.0F);
                        this.func_178103_d();
                        GlStateManager.translate(-0.5D, 1.0D, 0.5D);
                        GlStateManager.rotate(this.rotate, 0.0F, 0.0F, 0.0F);
                     }

                     if (SwordAnimations.xato) {
                        this.transformFirstPersonItem(var2, 1.0F);
                        this.func_178103_d();
                        GL11.glTranslatef(0.4F, 0.3F, 0.3F);
                        var12 = MathHelper.sin(var4 * var4 * 5.1415925F);
                        GlStateManager.translate(-0.4F, -0.1F, -0.2F);
                        GlStateManager.rotate(var12 * 17.0F, -65.0F, -0.4F, -5.0F);
                     }

                     if (SwordAnimations.Travadinha) {
                        this.transformFirstPersonItem(var2, var4 / 5.0F);
                        this.func_178103_d();
                     }

                     ++this.rotate;
                  }
               }
               break;
            case 5:
               this.transformFirstPersonItem(var2, 0.0F);
               this.func_178098_a(var1, var3);
            }
         } else {
            this.func_178105_d(0.0F);
            this.transformFirstPersonItem(var2, var4);
         }

         this.renderItem(var3, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
      } else if (!var3.isInvisible()) {
         this.func_178095_a(var3, var2, var4);
      }

      GlStateManager.popMatrix();
      GlStateManager.disableRescaleNormal();
      RenderHelper.disableStandardItemLighting();
   }

   private void func_178110_a(EntityPlayerSP var1, float var2) {
      float var3 = var1.prevRenderArmPitch + (var1.renderArmPitch - var1.prevRenderArmPitch) * var2;
      float var4 = var1.prevRenderArmYaw + (var1.renderArmYaw - var1.prevRenderArmYaw) * var2;
      GlStateManager.rotate((var1.rotationPitch - var3) * 0.1F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate((var1.rotationYaw - var4) * 0.1F, 0.0F, 1.0F, 0.0F);
   }

   private void func_178101_a(float var1, float var2) {
      GlStateManager.pushMatrix();
      GlStateManager.rotate(var1, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(var2, 0.0F, 1.0F, 0.0F);
      RenderHelper.enableStandardItemLighting();
      GlStateManager.popMatrix();
   }

   private float func_178100_c(float var1) {
      float var2 = 1.0F - var1 / 45.0F + 0.1F;
      var2 = MathHelper.clamp_float(var2, 0.0F, 1.0F);
      var2 = -MathHelper.cos(var2 * 3.1415927F) * 0.5F + 0.5F;
      return var2;
   }

   private void func_178108_a(float var1, TextureAtlasSprite var2) {
      this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
      Tessellator var3 = Tessellator.getInstance();
      WorldRenderer var4 = var3.getWorldRenderer();
      float var5 = 0.1F;
      GlStateManager.color(0.1F, 0.1F, 0.1F, 0.5F);
      GlStateManager.pushMatrix();
      float var6 = -1.0F;
      float var7 = 1.0F;
      float var8 = -1.0F;
      float var9 = 1.0F;
      float var10 = -0.5F;
      float var11 = var2.getMinU();
      float var12 = var2.getMaxU();
      float var13 = var2.getMinV();
      float var14 = var2.getMaxV();
      var4.begin(7, DefaultVertexFormats.POSITION_TEX);
      var4.pos(-1.0D, -1.0D, -0.5D).tex((double)var12, (double)var14).endVertex();
      var4.pos(1.0D, -1.0D, -0.5D).tex((double)var11, (double)var14).endVertex();
      var4.pos(1.0D, 1.0D, -0.5D).tex((double)var11, (double)var13).endVertex();
      var4.pos(-1.0D, 1.0D, -0.5D).tex((double)var12, (double)var13).endVertex();
      var3.draw();
      GlStateManager.popMatrix();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
   }

   private void func_178109_a(AbstractClientPlayer var1) {
      int var2 = Minecraft.theWorld.getCombinedLight(new BlockPos(var1.posX, var1.posY + (double)var1.getEyeHeight(), var1.posZ), 0);
      if (Config.isDynamicLights()) {
         var2 = DynamicLights.getCombinedLight(this.mc.getRenderViewEntity(), var2);
      }

      float var3 = (float)(var2 & '\uffff');
      float var4 = (float)(var2 >> 16);
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var3, var4);
   }

   public void updateEquippedItem() {
      this.prevEquippedProgress = this.equippedProgress;
      EntityPlayerSP var1 = Minecraft.thePlayer;
      ItemStack var2 = var1.inventory.getCurrentItem();
      boolean var3 = false;
      if (this.itemToRender != null && var2 != null) {
         if (!this.itemToRender.getIsItemStackEqual(var2)) {
            if (Reflector.ForgeItem_shouldCauseReequipAnimation.exists()) {
               boolean var4 = Reflector.callBoolean(this.itemToRender.getItem(), Reflector.ForgeItem_shouldCauseReequipAnimation, this.itemToRender, var2, this.equippedItemSlot != var1.inventory.currentItem);
               if (!var4) {
                  this.itemToRender = var2;
                  this.equippedItemSlot = var1.inventory.currentItem;
                  return;
               }
            }

            var3 = true;
         }
      } else if (this.itemToRender == null && var2 == null) {
         var3 = false;
      } else {
         var3 = true;
      }

      float var7 = 0.4F;
      float var5 = var3 ? 0.0F : 1.0F;
      float var6 = MathHelper.clamp_float(var5 - this.equippedProgress, -var7, var7);
      this.equippedProgress += var6;
      if (this.equippedProgress < 0.1F) {
         if (Config.isShaders()) {
            Shaders.setItemToRenderMain(var2);
         }

         this.itemToRender = var2;
         this.equippedItemSlot = var1.inventory.currentItem;
      }

   }

   public void renderOverlays(float var1) {
      GlStateManager.disableAlpha();
      if (Minecraft.thePlayer.isEntityInsideOpaqueBlock()) {
         IBlockState var2 = Minecraft.theWorld.getBlockState(new BlockPos(Minecraft.thePlayer));
         BlockPos var3 = new BlockPos(Minecraft.thePlayer);
         EntityPlayerSP var4 = Minecraft.thePlayer;

         for(int var5 = 0; var5 < 8; ++var5) {
            double var6 = var4.posX + (double)(((float)((var5 >> 0) % 2) - 0.5F) * var4.width * 0.8F);
            double var8 = var4.posY + (double)(((float)((var5 >> 1) % 2) - 0.5F) * 0.1F);
            double var10 = var4.posZ + (double)(((float)((var5 >> 2) % 2) - 0.5F) * var4.width * 0.8F);
            BlockPos var12 = new BlockPos(var6, var8 + (double)var4.getEyeHeight(), var10);
            IBlockState var13 = Minecraft.theWorld.getBlockState(var12);
            if (var13.getBlock().isVisuallyOpaque()) {
               var2 = var13;
               var3 = var12;
            }
         }

         if (var2.getBlock().getRenderType() != -1) {
            Object var14 = Reflector.getFieldValue(Reflector.RenderBlockOverlayEvent_OverlayType_BLOCK);
            if (!Reflector.callBoolean(Reflector.ForgeEventFactory_renderBlockOverlay, Minecraft.thePlayer, var1, var14, var2, var3)) {
               this.func_178108_a(var1, this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(var2));
            }
         }
      }

      if (!Minecraft.thePlayer.isSpectator()) {
         if (Minecraft.thePlayer.isInsideOfMaterial(Material.water) && !Reflector.callBoolean(Reflector.ForgeEventFactory_renderWaterOverlay, Minecraft.thePlayer, var1)) {
            this.renderWaterOverlayTexture(var1);
         }

         if (Minecraft.thePlayer.isBurning() && !Reflector.callBoolean(Reflector.ForgeEventFactory_renderFireOverlay, Minecraft.thePlayer, var1)) {
            this.renderFireInFirstPerson(var1);
         }
      }

      GlStateManager.enableAlpha();
   }

   public void resetEquippedProgress() {
      this.equippedProgress = 0.0F;
   }

   static int[] $SWITCH_TABLE$net$minecraft$item$EnumAction() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$item$EnumAction;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[EnumAction.values().length];

         try {
            var0[EnumAction.BLOCK.ordinal()] = 4;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[EnumAction.BOW.ordinal()] = 5;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[EnumAction.DRINK.ordinal()] = 3;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[EnumAction.EAT.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[EnumAction.NONE.ordinal()] = 1;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$item$EnumAction = var0;
         return var0;
      }
   }

   private void func_178105_d(float var1) {
      float var2 = -0.4F * MathHelper.sin(MathHelper.sqrt_float(var1) * 3.1415927F);
      float var3 = 0.2F * MathHelper.sin(MathHelper.sqrt_float(var1) * 3.1415927F * 2.0F);
      float var4 = -0.2F * MathHelper.sin(var1 * 3.1415927F);
      GlStateManager.translate(var2, var3, var4);
   }

   private void renderRightArm(RenderPlayer var1) {
      GlStateManager.pushMatrix();
      GlStateManager.rotate(54.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(64.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(-62.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.translate(0.25F, -0.85F, 0.75F);
      var1.renderRightArm(Minecraft.thePlayer);
      GlStateManager.popMatrix();
   }

   public ItemRenderer(Minecraft var1) {
      this.mc = var1;
      this.renderManager = var1.getRenderManager();
      this.itemRenderer = var1.getRenderItem();
   }

   static final class ItemRenderer$1 {
      private static final String __OBFID = "CL_00002537";
      static final int[] field_178094_a = new int[EnumAction.values().length];

      static {
         try {
            field_178094_a[EnumAction.NONE.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
         }

         try {
            field_178094_a[EnumAction.EAT.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
         }

         try {
            field_178094_a[EnumAction.DRINK.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
         }

         try {
            field_178094_a[EnumAction.BLOCK.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
         }

         try {
            field_178094_a[EnumAction.BOW.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
         }

      }
   }
}
