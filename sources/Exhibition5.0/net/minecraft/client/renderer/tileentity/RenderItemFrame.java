// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.storage.MapData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemSkull;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.texture.TextureCompass;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.util.BlockPos;
import net.minecraft.init.Items;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.entity.Render;

public class RenderItemFrame extends Render
{
    private static final ResourceLocation mapBackgroundTextures;
    private final Minecraft field_147917_g;
    private final ModelResourceLocation field_177072_f;
    private final ModelResourceLocation field_177073_g;
    private RenderItem field_177074_h;
    private static final String __OBFID = "CL_00001002";
    
    public RenderItemFrame(final RenderManager p_i46166_1_, final RenderItem p_i46166_2_) {
        super(p_i46166_1_);
        this.field_147917_g = Minecraft.getMinecraft();
        this.field_177072_f = new ModelResourceLocation("item_frame", "normal");
        this.field_177073_g = new ModelResourceLocation("item_frame", "map");
        this.field_177074_h = p_i46166_2_;
    }
    
    public void doRender(final EntityItemFrame p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        GlStateManager.pushMatrix();
        final BlockPos var10 = p_76986_1_.func_174857_n();
        final double var11 = var10.getX() - p_76986_1_.posX + p_76986_2_;
        final double var12 = var10.getY() - p_76986_1_.posY + p_76986_4_;
        final double var13 = var10.getZ() - p_76986_1_.posZ + p_76986_6_;
        GlStateManager.translate(var11 + 0.5, var12 + 0.5, var13 + 0.5);
        GlStateManager.rotate(180.0f - p_76986_1_.rotationYaw, 0.0f, 1.0f, 0.0f);
        this.renderManager.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        final BlockRendererDispatcher var14 = this.field_147917_g.getBlockRendererDispatcher();
        final ModelManager var15 = var14.func_175023_a().func_178126_b();
        IBakedModel var16;
        if (p_76986_1_.getDisplayedItem() != null && p_76986_1_.getDisplayedItem().getItem() == Items.filled_map) {
            var16 = var15.getModel(this.field_177073_g);
        }
        else {
            var16 = var15.getModel(this.field_177072_f);
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.5f, -0.5f, -0.5f);
        var14.func_175019_b().func_178262_a(var16, 1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
        GlStateManager.translate(0.0f, 0.0f, 0.4375f);
        this.func_82402_b(p_76986_1_);
        GlStateManager.popMatrix();
        this.func_147914_a(p_76986_1_, p_76986_2_ + p_76986_1_.field_174860_b.getFrontOffsetX() * 0.3f, p_76986_4_ - 0.25, p_76986_6_ + p_76986_1_.field_174860_b.getFrontOffsetZ() * 0.3f);
    }
    
    protected ResourceLocation getEntityTexture(final EntityItemFrame p_110775_1_) {
        return null;
    }
    
    private void func_82402_b(final EntityItemFrame p_82402_1_) {
        final ItemStack var2 = p_82402_1_.getDisplayedItem();
        if (var2 != null) {
            final EntityItem var3 = new EntityItem(p_82402_1_.worldObj, 0.0, 0.0, 0.0, var2);
            final Item var4 = var3.getEntityItem().getItem();
            var3.getEntityItem().stackSize = 1;
            var3.hoverStart = 0.0f;
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            int var5 = p_82402_1_.getRotation();
            if (var4 == Items.filled_map) {
                var5 = var5 % 4 * 2;
            }
            GlStateManager.rotate(var5 * 360.0f / 8.0f, 0.0f, 0.0f, 1.0f);
            if (var4 == Items.filled_map) {
                this.renderManager.renderEngine.bindTexture(RenderItemFrame.mapBackgroundTextures);
                GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
                final float var6 = 0.0078125f;
                GlStateManager.scale(var6, var6, var6);
                GlStateManager.translate(-64.0f, -64.0f, 0.0f);
                final MapData var7 = Items.filled_map.getMapData(var3.getEntityItem(), p_82402_1_.worldObj);
                GlStateManager.translate(0.0f, 0.0f, -1.0f);
                if (var7 != null) {
                    this.field_147917_g.entityRenderer.getMapItemRenderer().func_148250_a(var7, true);
                }
            }
            else {
                TextureAtlasSprite var8 = null;
                if (var4 == Items.compass) {
                    var8 = this.field_147917_g.getTextureMapBlocks().getAtlasSprite(TextureCompass.field_176608_l);
                    this.field_147917_g.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
                    if (var8 instanceof TextureCompass) {
                        final TextureCompass var9 = (TextureCompass)var8;
                        final double var10 = var9.currentAngle;
                        final double var11 = var9.angleDelta;
                        var9.currentAngle = 0.0;
                        var9.angleDelta = 0.0;
                        var9.updateCompass(p_82402_1_.worldObj, p_82402_1_.posX, p_82402_1_.posZ, MathHelper.wrapAngleTo180_float(180 + p_82402_1_.field_174860_b.getHorizontalIndex() * 90), false, true);
                        var9.currentAngle = var10;
                        var9.angleDelta = var11;
                    }
                    else {
                        var8 = null;
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
                if (var8 != null && var8.getFrameCount() > 0) {
                    var8.updateAnimation();
                }
            }
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }
    
    protected void func_147914_a(final EntityItemFrame p_147914_1_, final double p_147914_2_, final double p_147914_4_, final double p_147914_6_) {
        if (Minecraft.isGuiEnabled() && p_147914_1_.getDisplayedItem() != null && p_147914_1_.getDisplayedItem().hasDisplayName() && this.renderManager.field_147941_i == p_147914_1_) {
            final float var8 = 1.6f;
            final float var9 = 0.016666668f * var8;
            final double var10 = p_147914_1_.getDistanceSqToEntity(this.renderManager.livingPlayer);
            final float var11 = p_147914_1_.isSneaking() ? 32.0f : 64.0f;
            if (var10 < var11 * var11) {
                final String var12 = p_147914_1_.getDisplayedItem().getDisplayName();
                if (p_147914_1_.isSneaking()) {
                    final FontRenderer var13 = this.getFontRendererFromRenderManager();
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((float)p_147914_2_ + 0.0f, (float)p_147914_4_ + p_147914_1_.height + 0.5f, (float)p_147914_6_);
                    GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                    final RenderManager renderManager = this.renderManager;
                    GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
                    final RenderManager renderManager2 = this.renderManager;
                    GlStateManager.rotate(RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
                    GlStateManager.scale(-var9, -var9, var9);
                    GlStateManager.disableLighting();
                    GlStateManager.translate(0.0f, 0.25f / var9, 0.0f);
                    GlStateManager.depthMask(false);
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(770, 771);
                    final Tessellator var14 = Tessellator.getInstance();
                    final WorldRenderer var15 = var14.getWorldRenderer();
                    GlStateManager.disableTextures();
                    var15.startDrawingQuads();
                    final int var16 = var13.getStringWidth(var12) / 2;
                    var15.setColorRGBA(0.0f, 0.0f, 0.0f, 0.25f);
                    var15.addVertex(-var16 - 1, -1.0, 0.0);
                    var15.addVertex(-var16 - 1, 8.0, 0.0);
                    var15.addVertex(var16 + 1, 8.0, 0.0);
                    var15.addVertex(var16 + 1, -1.0, 0.0);
                    var14.draw();
                    GlStateManager.enableTextures();
                    GlStateManager.depthMask(true);
                    var13.drawString(var12, -var13.getStringWidth(var12) / 2, 0.0f, 553648127);
                    GlStateManager.enableLighting();
                    GlStateManager.disableBlend();
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    GlStateManager.popMatrix();
                }
                else {
                    this.renderLivingLabel(p_147914_1_, var12, p_147914_2_, p_147914_4_, p_147914_6_, 64);
                }
            }
        }
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.getEntityTexture((EntityItemFrame)p_110775_1_);
    }
    
    @Override
    protected void func_177067_a(final Entity p_177067_1_, final double p_177067_2_, final double p_177067_4_, final double p_177067_6_) {
        this.func_147914_a((EntityItemFrame)p_177067_1_, p_177067_2_, p_177067_4_, p_177067_6_);
    }
    
    @Override
    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.doRender((EntityItemFrame)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    static {
        mapBackgroundTextures = new ResourceLocation("textures/map/map_background.png");
    }
}
