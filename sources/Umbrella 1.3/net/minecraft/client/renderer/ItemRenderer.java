/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import optifine.Config;
import optifine.DynamicLights;
import optifine.Reflector;
import org.lwjgl.opengl.GL11;
import shadersmod.client.Shaders;

public class ItemRenderer {
    private static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
    private static final ResourceLocation RES_UNDERWATER_OVERLAY = new ResourceLocation("textures/misc/underwater.png");
    private final Minecraft mc;
    private ItemStack itemToRender;
    private float equippedProgress;
    private float prevEquippedProgress;
    private final RenderManager field_178111_g;
    private final RenderItem itemRenderer;
    private int equippedItemSlot = -1;

    public ItemRenderer(Minecraft mcIn) {
        this.mc = mcIn;
        this.field_178111_g = mcIn.getRenderManager();
        this.itemRenderer = mcIn.getRenderItem();
    }

    public void renderItem(EntityLivingBase p_178099_1_, ItemStack p_178099_2_, ItemCameraTransforms.TransformType p_178099_3_) {
        if (p_178099_2_ != null) {
            Item var4 = p_178099_2_.getItem();
            Block var5 = Block.getBlockFromItem(var4);
            GlStateManager.pushMatrix();
            if (this.itemRenderer.func_175050_a(p_178099_2_)) {
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                if (this.func_178107_a(var5)) {
                    GlStateManager.depthMask(false);
                }
            }
            this.itemRenderer.func_175049_a(p_178099_2_, p_178099_1_, p_178099_3_);
            if (this.func_178107_a(var5)) {
                GlStateManager.depthMask(true);
            }
            GlStateManager.popMatrix();
        }
    }

    private boolean func_178107_a(Block p_178107_1_) {
        return p_178107_1_ != null && p_178107_1_.getBlockLayer() == EnumWorldBlockLayer.TRANSLUCENT;
    }

    private void func_178101_a(float p_178101_1_, float p_178101_2_) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(p_178101_1_, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(p_178101_2_, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    private void func_178109_a(AbstractClientPlayer p_178109_1_) {
        int var2 = this.mc.theWorld.getCombinedLight(new BlockPos(p_178109_1_.posX, p_178109_1_.posY + (double)p_178109_1_.getEyeHeight(), p_178109_1_.posZ), 0);
        if (Config.isDynamicLights()) {
            var2 = DynamicLights.getCombinedLight(this.mc.func_175606_aa(), var2);
        }
        float var3 = var2 & 0xFFFF;
        float var4 = var2 >> 16;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var3, var4);
    }

    private void func_178110_a(EntityPlayerSP p_178110_1_, float p_178110_2_) {
        float var3 = p_178110_1_.prevRenderArmPitch + (p_178110_1_.renderArmPitch - p_178110_1_.prevRenderArmPitch) * p_178110_2_;
        float var4 = p_178110_1_.prevRenderArmYaw + (p_178110_1_.renderArmYaw - p_178110_1_.prevRenderArmYaw) * p_178110_2_;
        GlStateManager.rotate((p_178110_1_.rotationPitch - var3) * 0.1f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate((p_178110_1_.rotationYaw - var4) * 0.1f, 0.0f, 1.0f, 0.0f);
    }

    private float func_178100_c(float p_178100_1_) {
        float var2 = 1.0f - p_178100_1_ / 45.0f + 0.1f;
        var2 = MathHelper.clamp_float(var2, 0.0f, 1.0f);
        var2 = -MathHelper.cos(var2 * (float)Math.PI) * 0.5f + 0.5f;
        return var2;
    }

    private void func_180534_a(RenderPlayer p_180534_1_) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(54.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(64.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(-62.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(0.25f, -0.85f, 0.75f);
        p_180534_1_.func_177138_b(this.mc.thePlayer);
        GlStateManager.popMatrix();
    }

    private void func_178106_b(RenderPlayer p_178106_1_) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(92.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(45.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(41.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(-0.3f, -1.1f, 0.45f);
        p_178106_1_.func_177139_c(this.mc.thePlayer);
        GlStateManager.popMatrix();
    }

    private void func_178102_b(AbstractClientPlayer p_178102_1_) {
        this.mc.getTextureManager().bindTexture(p_178102_1_.getLocationSkin());
        Render var2 = this.field_178111_g.getEntityRenderObject(this.mc.thePlayer);
        RenderPlayer var3 = (RenderPlayer)var2;
        if (!p_178102_1_.isInvisible()) {
            this.func_180534_a(var3);
            this.func_178106_b(var3);
        }
    }

    private void func_178097_a(AbstractClientPlayer p_178097_1_, float p_178097_2_, float p_178097_3_, float p_178097_4_) {
        float var5 = -0.4f * MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * (float)Math.PI);
        float var6 = 0.2f * MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * (float)Math.PI * 2.0f);
        float var7 = -0.2f * MathHelper.sin(p_178097_4_ * (float)Math.PI);
        GlStateManager.translate(var5, var6, var7);
        float var8 = this.func_178100_c(p_178097_2_);
        GlStateManager.translate(0.0f, 0.04f, -0.72f);
        GlStateManager.translate(0.0f, p_178097_3_ * -1.2f, 0.0f);
        GlStateManager.translate(0.0f, var8 * -0.5f, 0.0f);
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(var8 * -85.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(0.0f, 1.0f, 0.0f, 0.0f);
        this.func_178102_b(p_178097_1_);
        float var9 = MathHelper.sin(p_178097_4_ * p_178097_4_ * (float)Math.PI);
        float var10 = MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * (float)Math.PI);
        GlStateManager.rotate(var9 * -20.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(var10 * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(var10 * -80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(0.38f, 0.38f, 0.38f);
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(0.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(-1.0f, -1.0f, 0.0f);
        GlStateManager.scale(0.015625f, 0.015625f, 0.015625f);
        this.mc.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
        Tessellator var11 = Tessellator.getInstance();
        WorldRenderer var12 = var11.getWorldRenderer();
        GL11.glNormal3f((float)0.0f, (float)0.0f, (float)-1.0f);
        var12.startDrawingQuads();
        var12.addVertexWithUV(-7.0, 135.0, 0.0, 0.0, 1.0);
        var12.addVertexWithUV(135.0, 135.0, 0.0, 1.0, 1.0);
        var12.addVertexWithUV(135.0, -7.0, 0.0, 1.0, 0.0);
        var12.addVertexWithUV(-7.0, -7.0, 0.0, 0.0, 0.0);
        var11.draw();
        MapData var13 = Items.filled_map.getMapData(this.itemToRender, this.mc.theWorld);
        if (var13 != null) {
            this.mc.entityRenderer.getMapItemRenderer().func_148250_a(var13, false);
        }
    }

    private void func_178095_a(AbstractClientPlayer p_178095_1_, float p_178095_2_, float p_178095_3_) {
        float var4 = -0.3f * MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * (float)Math.PI);
        float var5 = 0.4f * MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * (float)Math.PI * 2.0f);
        float var6 = -0.4f * MathHelper.sin(p_178095_3_ * (float)Math.PI);
        GlStateManager.translate(var4, var5, var6);
        GlStateManager.translate(0.64000005f, -0.6f, -0.71999997f);
        GlStateManager.translate(0.0f, p_178095_2_ * -0.6f, 0.0f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        float var7 = MathHelper.sin(p_178095_3_ * p_178095_3_ * (float)Math.PI);
        float var8 = MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * (float)Math.PI);
        GlStateManager.rotate(var8 * 70.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(var7 * -20.0f, 0.0f, 0.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(p_178095_1_.getLocationSkin());
        GlStateManager.translate(-1.0f, 3.6f, 3.5f);
        GlStateManager.rotate(120.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(200.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        GlStateManager.translate(5.6f, 0.0f, 0.0f);
        Render var9 = this.field_178111_g.getEntityRenderObject(this.mc.thePlayer);
        RenderPlayer var10 = (RenderPlayer)var9;
        var10.func_177138_b(this.mc.thePlayer);
    }

    private void func_178105_d(float p_178105_1_) {
        float var2 = -0.4f * MathHelper.sin(MathHelper.sqrt_float(p_178105_1_) * (float)Math.PI);
        float var3 = 0.2f * MathHelper.sin(MathHelper.sqrt_float(p_178105_1_) * (float)Math.PI * 2.0f);
        float var4 = -0.2f * MathHelper.sin(p_178105_1_ * (float)Math.PI);
        GlStateManager.translate(var2, var3, var4);
    }

    private void func_178104_a(AbstractClientPlayer p_178104_1_, float p_178104_2_) {
        float var3 = (float)p_178104_1_.getItemInUseCount() - p_178104_2_ + 1.0f;
        float var4 = var3 / (float)this.itemToRender.getMaxItemUseDuration();
        float var5 = MathHelper.abs(MathHelper.cos(var3 / 4.0f * (float)Math.PI) * 0.1f);
        if (var4 >= 0.8f) {
            var5 = 0.0f;
        }
        GlStateManager.translate(0.0f, var5, 0.0f);
        float var6 = 1.0f - (float)Math.pow(var4, 27.0);
        GlStateManager.translate(var6 * 0.6f, var6 * -0.5f, var6 * 0.0f);
        GlStateManager.rotate(var6 * 90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(var6 * 10.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(var6 * 30.0f, 0.0f, 0.0f, 1.0f);
    }

    private void func_178096_b(float p_178096_1_, float p_178096_2_) {
        GlStateManager.translate(0.56f, -0.52f, -0.71999997f);
        GlStateManager.translate(0.0f, p_178096_1_ * -0.6f, 0.0f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        float var3 = MathHelper.sin(p_178096_2_ * p_178096_2_ * (float)Math.PI);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(p_178096_2_) * (float)Math.PI);
        GlStateManager.rotate(var3 * -20.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(var4 * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(var4 * -80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(0.4f, 0.4f, 0.4f);
    }

    private void func_178098_a(float p_178098_1_, AbstractClientPlayer p_178098_2_) {
        GlStateManager.rotate(-18.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(-12.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-8.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(-0.9f, 0.2f, 0.0f);
        float var3 = (float)this.itemToRender.getMaxItemUseDuration() - ((float)p_178098_2_.getItemInUseCount() - p_178098_1_ + 1.0f);
        float var4 = var3 / 20.0f;
        var4 = (var4 * var4 + var4 * 2.0f) / 3.0f;
        if (var4 > 1.0f) {
            var4 = 1.0f;
        }
        if (var4 > 0.1f) {
            float var5 = MathHelper.sin((var3 - 0.1f) * 1.3f);
            float var6 = var4 - 0.1f;
            float var7 = var5 * var6;
            GlStateManager.translate(var7 * 0.0f, var7 * 0.01f, var7 * 0.0f);
        }
        GlStateManager.translate(var4 * 0.0f, var4 * 0.0f, var4 * 0.1f);
        GlStateManager.scale(1.0f, 1.0f, 1.0f + var4 * 0.2f);
    }

    private void func_178103_d() {
        GlStateManager.translate(-0.5f, 0.2f, 0.0f);
        GlStateManager.rotate(30.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(60.0f, 0.0f, 1.0f, 0.0f);
    }

    public void renderItemInFirstPerson(float p_78440_1_) {
        float var2 = 1.0f - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * p_78440_1_);
        EntityPlayerSP var3 = this.mc.thePlayer;
        float var4 = var3.getSwingProgress(p_78440_1_);
        float var5 = var3.prevRotationPitch + (var3.rotationPitch - var3.prevRotationPitch) * p_78440_1_;
        float var6 = var3.prevRotationYaw + (var3.rotationYaw - var3.prevRotationYaw) * p_78440_1_;
        this.func_178101_a(var5, var6);
        this.func_178109_a(var3);
        this.func_178110_a(var3, p_78440_1_);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        if (this.itemToRender != null) {
            if (this.itemToRender.getItem() instanceof ItemMap) {
                this.func_178097_a(var3, var5, var2, var4);
            } else if (var3.getItemInUseCount() > 0) {
                EnumAction var7 = this.itemToRender.getItemUseAction();
                switch (SwitchEnumAction.field_178094_a[var7.ordinal()]) {
                    case 1: {
                        this.func_178096_b(var2, 0.0f);
                        break;
                    }
                    case 2: 
                    case 3: {
                        this.func_178104_a(var3, p_78440_1_);
                        this.func_178096_b(var2, 0.0f);
                        break;
                    }
                    case 4: {
                        this.func_178096_b(var2, 0.0f);
                        this.func_178103_d();
                        break;
                    }
                    case 5: {
                        this.func_178096_b(var2, 0.0f);
                        this.func_178098_a(p_78440_1_, var3);
                    }
                }
            } else {
                this.func_178105_d(var4);
                this.func_178096_b(var2, var4);
            }
            this.renderItem(var3, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
        } else if (!var3.isInvisible()) {
            this.func_178095_a(var3, var2, var4);
        }
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }

    public void renderOverlays(float p_78447_1_) {
        GlStateManager.disableAlpha();
        if (this.mc.thePlayer.isEntityInsideOpaqueBlock()) {
            BlockPos blockPos = new BlockPos(this.mc.thePlayer);
            IBlockState var2 = this.mc.theWorld.getBlockState(blockPos);
            EntityPlayerSP var3 = this.mc.thePlayer;
            for (int overlayType = 0; overlayType < 8; ++overlayType) {
                double var5 = var3.posX + (double)(((float)((overlayType >> 0) % 2) - 0.5f) * var3.width * 0.8f);
                double var7 = var3.posY + (double)(((float)((overlayType >> 1) % 2) - 0.5f) * 0.1f);
                double var9 = var3.posZ + (double)(((float)((overlayType >> 2) % 2) - 0.5f) * var3.width * 0.8f);
                blockPos = new BlockPos(var5, var7 + (double)var3.getEyeHeight(), var9);
                IBlockState var12 = this.mc.theWorld.getBlockState(blockPos);
                if (!var12.getBlock().isVisuallyOpaque()) continue;
                var2 = var12;
            }
            if (var2.getBlock().getRenderType() != -1) {
                Object var13 = Reflector.getFieldValue(Reflector.RenderBlockOverlayEvent_OverlayType_BLOCK);
                if (!Reflector.callBoolean(Reflector.ForgeEventFactory_renderBlockOverlay, this.mc.thePlayer, Float.valueOf(p_78447_1_), var13, var2, blockPos)) {
                    this.func_178108_a(p_78447_1_, this.mc.getBlockRendererDispatcher().func_175023_a().func_178122_a(var2));
                }
            }
        }
        if (!this.mc.thePlayer.func_175149_v()) {
            if (this.mc.thePlayer.isInsideOfMaterial(Material.water)) {
                if (!Reflector.callBoolean(Reflector.ForgeEventFactory_renderWaterOverlay, this.mc.thePlayer, Float.valueOf(p_78447_1_))) {
                    this.renderWaterOverlayTexture(p_78447_1_);
                }
            }
            if (this.mc.thePlayer.isBurning()) {
                if (!Reflector.callBoolean(Reflector.ForgeEventFactory_renderFireOverlay, this.mc.thePlayer, Float.valueOf(p_78447_1_))) {
                    this.renderFireInFirstPerson(p_78447_1_);
                }
            }
        }
        GlStateManager.enableAlpha();
    }

    private void func_178108_a(float p_178108_1_, TextureAtlasSprite p_178108_2_) {
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        Tessellator var3 = Tessellator.getInstance();
        WorldRenderer var4 = var3.getWorldRenderer();
        float var5 = 0.1f;
        GlStateManager.color(var5, var5, var5, 0.5f);
        GlStateManager.pushMatrix();
        float var6 = -1.0f;
        float var7 = 1.0f;
        float var8 = -1.0f;
        float var9 = 1.0f;
        float var10 = -0.5f;
        float var11 = p_178108_2_.getMinU();
        float var12 = p_178108_2_.getMaxU();
        float var13 = p_178108_2_.getMinV();
        float var14 = p_178108_2_.getMaxV();
        var4.startDrawingQuads();
        var4.addVertexWithUV(var6, var8, var10, var12, var14);
        var4.addVertexWithUV(var7, var8, var10, var11, var14);
        var4.addVertexWithUV(var7, var9, var10, var11, var13);
        var4.addVertexWithUV(var6, var9, var10, var12, var13);
        var3.draw();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void renderWaterOverlayTexture(float p_78448_1_) {
        this.mc.getTextureManager().bindTexture(RES_UNDERWATER_OVERLAY);
        Tessellator var2 = Tessellator.getInstance();
        WorldRenderer var3 = var2.getWorldRenderer();
        float var4 = this.mc.thePlayer.getBrightness(p_78448_1_);
        GlStateManager.color(var4, var4, var4, 0.5f);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();
        float var5 = 4.0f;
        float var6 = -1.0f;
        float var7 = 1.0f;
        float var8 = -1.0f;
        float var9 = 1.0f;
        float var10 = -0.5f;
        float var11 = -this.mc.thePlayer.rotationYaw / 64.0f;
        float var12 = this.mc.thePlayer.rotationPitch / 64.0f;
        var3.startDrawingQuads();
        var3.addVertexWithUV(var6, var8, var10, var5 + var11, var5 + var12);
        var3.addVertexWithUV(var7, var8, var10, 0.0f + var11, var5 + var12);
        var3.addVertexWithUV(var7, var9, var10, 0.0f + var11, 0.0f + var12);
        var3.addVertexWithUV(var6, var9, var10, var5 + var11, 0.0f + var12);
        var2.draw();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
    }

    private void renderFireInFirstPerson(float p_78442_1_) {
        Tessellator var2 = Tessellator.getInstance();
        WorldRenderer var3 = var2.getWorldRenderer();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.9f);
        GlStateManager.depthFunc(519);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        float var4 = 1.0f;
        for (int var5 = 0; var5 < 2; ++var5) {
            GlStateManager.pushMatrix();
            TextureAtlasSprite var6 = this.mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/fire_layer_1");
            this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            float var7 = var6.getMinU();
            float var8 = var6.getMaxU();
            float var9 = var6.getMinV();
            float var10 = var6.getMaxV();
            float var11 = (0.0f - var4) / 2.0f;
            float var12 = var11 + var4;
            float var13 = 0.0f - var4 / 2.0f;
            float var14 = var13 + var4;
            float var15 = -0.5f;
            GlStateManager.translate((float)(-(var5 * 2 - 1)) * 0.24f, -0.3f, 0.0f);
            GlStateManager.rotate((float)(var5 * 2 - 1) * 10.0f, 0.0f, 1.0f, 0.0f);
            var3.startDrawingQuads();
            var3.addVertexWithUV(var11, var13, var15, var8, var10);
            var3.addVertexWithUV(var12, var13, var15, var7, var10);
            var3.addVertexWithUV(var12, var14, var15, var7, var9);
            var3.addVertexWithUV(var11, var14, var15, var8, var9);
            var2.draw();
            GlStateManager.popMatrix();
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(515);
    }

    public void updateEquippedItem() {
        this.prevEquippedProgress = this.equippedProgress;
        EntityPlayerSP var1 = this.mc.thePlayer;
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
        } else {
            var3 = this.itemToRender != null || var2 != null;
        }
        float var41 = 0.4f;
        float var5 = var3 ? 0.0f : 1.0f;
        float var6 = MathHelper.clamp_float(var5 - this.equippedProgress, -var41, var41);
        this.equippedProgress += var6;
        if (this.equippedProgress < 0.1f) {
            if (Config.isShaders()) {
                Shaders.itemToRender = var2;
            }
            this.itemToRender = var2;
            this.equippedItemSlot = var1.inventory.currentItem;
        }
    }

    public void resetEquippedProgress() {
        this.equippedProgress = 0.0f;
    }

    public void resetEquippedProgress2() {
        this.equippedProgress = 0.0f;
    }

    static final class SwitchEnumAction {
        static final int[] field_178094_a = new int[EnumAction.values().length];

        static {
            try {
                SwitchEnumAction.field_178094_a[EnumAction.NONE.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumAction.field_178094_a[EnumAction.EAT.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumAction.field_178094_a[EnumAction.DRINK.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumAction.field_178094_a[EnumAction.BLOCK.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumAction.field_178094_a[EnumAction.BOW.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchEnumAction() {
        }
    }
}

