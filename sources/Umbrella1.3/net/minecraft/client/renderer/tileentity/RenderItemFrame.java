/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureCompass;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import optifine.Config;
import optifine.Reflector;
import org.lwjgl.opengl.GL11;
import shadersmod.client.ShadersTex;

public class RenderItemFrame
extends Render {
    private static final ResourceLocation mapBackgroundTextures = new ResourceLocation("textures/map/map_background.png");
    private final Minecraft field_147917_g = Minecraft.getMinecraft();
    private final ModelResourceLocation field_177072_f = new ModelResourceLocation("item_frame", "normal");
    private final ModelResourceLocation field_177073_g = new ModelResourceLocation("item_frame", "map");
    private RenderItem field_177074_h;
    private static final String __OBFID = "CL_00001002";

    public RenderItemFrame(RenderManager p_i46166_1_, RenderItem p_i46166_2_) {
        super(p_i46166_1_);
        this.field_177074_h = p_i46166_2_;
    }

    public void doRender(EntityItemFrame p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        GlStateManager.pushMatrix();
        BlockPos var10 = p_76986_1_.func_174857_n();
        double var11 = (double)var10.getX() - p_76986_1_.posX + p_76986_2_;
        double var13 = (double)var10.getY() - p_76986_1_.posY + p_76986_4_;
        double var15 = (double)var10.getZ() - p_76986_1_.posZ + p_76986_6_;
        GlStateManager.translate(var11 + 0.5, var13 + 0.5, var15 + 0.5);
        GlStateManager.rotate(180.0f - p_76986_1_.rotationYaw, 0.0f, 1.0f, 0.0f);
        this.renderManager.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        BlockRendererDispatcher var17 = this.field_147917_g.getBlockRendererDispatcher();
        ModelManager var18 = var17.func_175023_a().func_178126_b();
        IBakedModel var19 = p_76986_1_.getDisplayedItem() != null && p_76986_1_.getDisplayedItem().getItem() == Items.filled_map ? var18.getModel(this.field_177073_g) : var18.getModel(this.field_177072_f);
        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.5f, -0.5f, -0.5f);
        var17.func_175019_b().func_178262_a(var19, 1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
        GlStateManager.translate(0.0f, 0.0f, 0.4375f);
        this.func_82402_b(p_76986_1_);
        GlStateManager.popMatrix();
        this.func_147914_a(p_76986_1_, p_76986_2_ + (double)((float)p_76986_1_.field_174860_b.getFrontOffsetX() * 0.3f), p_76986_4_ - 0.25, p_76986_6_ + (double)((float)p_76986_1_.field_174860_b.getFrontOffsetZ() * 0.3f));
    }

    protected ResourceLocation getEntityTexture(EntityItemFrame p_110775_1_) {
        return null;
    }

    private void func_82402_b(EntityItemFrame p_82402_1_) {
        ItemStack var2 = p_82402_1_.getDisplayedItem();
        if (var2 != null) {
            EntityItem var3 = new EntityItem(p_82402_1_.worldObj, 0.0, 0.0, 0.0, var2);
            Item var4 = var3.getEntityItem().getItem();
            var3.getEntityItem().stackSize = 1;
            var3.hoverStart = 0.0f;
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            int var5 = p_82402_1_.getRotation();
            if (var4 instanceof ItemMap) {
                var5 = var5 % 4 * 2;
            }
            GlStateManager.rotate((float)var5 * 360.0f / 8.0f, 0.0f, 0.0f, 1.0f);
            if (!Reflector.postForgeBusEvent(Reflector.RenderItemInFrameEvent_Constructor, p_82402_1_, this)) {
                if (var4 instanceof ItemMap) {
                    this.renderManager.renderEngine.bindTexture(mapBackgroundTextures);
                    GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
                    float var12 = 0.0078125f;
                    GlStateManager.scale(var12, var12, var12);
                    GlStateManager.translate(-64.0f, -64.0f, 0.0f);
                    MapData var13 = Items.filled_map.getMapData(var3.getEntityItem(), p_82402_1_.worldObj);
                    GlStateManager.translate(0.0f, 0.0f, -1.0f);
                    if (var13 != null) {
                        this.field_147917_g.entityRenderer.getMapItemRenderer().func_148250_a(var13, true);
                    }
                } else {
                    TextureAtlasSprite var121 = null;
                    if (var4 == Items.compass) {
                        var121 = this.field_147917_g.getTextureMapBlocks().getAtlasSprite(TextureCompass.field_176608_l);
                        if (Config.isShaders()) {
                            ShadersTex.bindTextureMapForUpdateAndRender(this.field_147917_g.getTextureManager(), TextureMap.locationBlocksTexture);
                        } else {
                            this.field_147917_g.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
                        }
                        if (var121 instanceof TextureCompass) {
                            TextureCompass var131 = (TextureCompass)var121;
                            double var8 = var131.currentAngle;
                            double var10 = var131.angleDelta;
                            var131.currentAngle = 0.0;
                            var131.angleDelta = 0.0;
                            var131.updateCompass(p_82402_1_.worldObj, p_82402_1_.posX, p_82402_1_.posZ, MathHelper.wrapAngleTo180_float(180 + p_82402_1_.field_174860_b.getHorizontalIndex() * 90), false, true);
                            var131.currentAngle = var8;
                            var131.angleDelta = var10;
                        } else {
                            var121 = null;
                        }
                    }
                    GlStateManager.scale(0.5f, 0.5f, 0.5f);
                    if (!this.field_177074_h.func_175050_a(var3.getEntityItem()) || var4 instanceof ItemSkull) {
                        GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                    }
                    GlStateManager.pushAttrib();
                    RenderHelper.enableStandardItemLighting();
                    this.field_177074_h.func_175043_b(var3.getEntityItem());
                    RenderHelper.disableStandardItemLighting();
                    GlStateManager.popAttrib();
                    if (var121 != null && var121.getFrameCount() > 0) {
                        var121.updateAnimation();
                    }
                }
            }
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
        if (Config.isShaders()) {
            ShadersTex.updatingTex = null;
        }
    }

    protected void func_147914_a(EntityItemFrame p_147914_1_, double p_147914_2_, double p_147914_4_, double p_147914_6_) {
        if (Minecraft.isGuiEnabled() && p_147914_1_.getDisplayedItem() != null && p_147914_1_.getDisplayedItem().hasDisplayName() && this.renderManager.field_147941_i == p_147914_1_) {
            float var12;
            float var8 = 1.6f;
            float var9 = 0.016666668f * var8;
            double var10 = p_147914_1_.getDistanceSqToEntity(this.renderManager.livingPlayer);
            float f = var12 = p_147914_1_.isSneaking() ? 32.0f : 64.0f;
            if (var10 < (double)(var12 * var12)) {
                String var13 = p_147914_1_.getDisplayedItem().getDisplayName();
                if (p_147914_1_.isSneaking()) {
                    FontRenderer var14 = this.getFontRendererFromRenderManager();
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((float)p_147914_2_ + 0.0f, (float)p_147914_4_ + p_147914_1_.height + 0.5f, (float)p_147914_6_);
                    GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
                    GlStateManager.rotate(-this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate(this.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
                    GlStateManager.scale(-var9, -var9, var9);
                    GlStateManager.disableLighting();
                    GlStateManager.translate(0.0f, 0.25f / var9, 0.0f);
                    GlStateManager.depthMask(false);
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(770, 771);
                    Tessellator var15 = Tessellator.getInstance();
                    WorldRenderer var16 = var15.getWorldRenderer();
                    GlStateManager.func_179090_x();
                    var16.startDrawingQuads();
                    int var17 = var14.getStringWidth(var13) / 2;
                    var16.func_178960_a(0.0f, 0.0f, 0.0f, 0.25f);
                    var16.addVertex(-var17 - 1, -1.0, 0.0);
                    var16.addVertex(-var17 - 1, 8.0, 0.0);
                    var16.addVertex(var17 + 1, 8.0, 0.0);
                    var16.addVertex(var17 + 1, -1.0, 0.0);
                    var15.draw();
                    GlStateManager.func_179098_w();
                    GlStateManager.depthMask(true);
                    var14.drawString(var13, -var14.getStringWidth(var13) / 2, 0.0, 0x20FFFFFF);
                    GlStateManager.enableLighting();
                    GlStateManager.disableBlend();
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    GlStateManager.popMatrix();
                } else {
                    this.renderLivingLabel(p_147914_1_, var13, p_147914_2_, p_147914_4_, p_147914_6_, 64);
                }
            }
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.getEntityTexture((EntityItemFrame)p_110775_1_);
    }

    @Override
    protected void func_177067_a(Entity p_177067_1_, double p_177067_2_, double p_177067_4_, double p_177067_6_) {
        this.func_147914_a((EntityItemFrame)p_177067_1_, p_177067_2_, p_177067_4_, p_177067_6_);
    }

    @Override
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.doRender((EntityItemFrame)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}

