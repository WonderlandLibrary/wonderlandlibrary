// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.ResourceLocation;
import net.optifine.entity.model.IEntityRenderer;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntitySpecialRenderer<T extends TileEntity> implements IEntityRenderer
{
    protected static final ResourceLocation[] DESTROY_STAGES;
    protected TileEntityRendererDispatcher rendererDispatcher;
    private Class tileEntityClass;
    private ResourceLocation locationTextureCustom;
    
    public TileEntitySpecialRenderer() {
        this.tileEntityClass = null;
        this.locationTextureCustom = null;
    }
    
    public void render(final T te, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final float alpha) {
        final ITextComponent itextcomponent = te.getDisplayName();
        if (itextcomponent != null && this.rendererDispatcher.cameraHitResult != null && te.getPos().equals(this.rendererDispatcher.cameraHitResult.getBlockPos())) {
            this.setLightmapDisabled(true);
            this.drawNameplate(te, itextcomponent.getFormattedText(), x, y, z, 12);
            this.setLightmapDisabled(false);
        }
    }
    
    protected void setLightmapDisabled(final boolean disabled) {
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        if (disabled) {
            GlStateManager.disableTexture2D();
        }
        else {
            GlStateManager.enableTexture2D();
        }
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
    
    protected void bindTexture(final ResourceLocation location) {
        final TextureManager texturemanager = this.rendererDispatcher.renderEngine;
        if (texturemanager != null) {
            texturemanager.bindTexture(location);
        }
    }
    
    protected World getWorld() {
        return this.rendererDispatcher.world;
    }
    
    public void setRendererDispatcher(final TileEntityRendererDispatcher rendererDispatcherIn) {
        this.rendererDispatcher = rendererDispatcherIn;
    }
    
    public FontRenderer getFontRenderer() {
        return this.rendererDispatcher.getFontRenderer();
    }
    
    public boolean isGlobalRenderer(final T te) {
        return false;
    }
    
    protected void drawNameplate(final T te, final String str, final double x, final double y, final double z, final int maxDistance) {
        final Entity entity = this.rendererDispatcher.entity;
        final double d0 = te.getDistanceSq(entity.posX, entity.posY, entity.posZ);
        if (d0 <= maxDistance * maxDistance) {
            final float f = this.rendererDispatcher.entityYaw;
            final float f2 = this.rendererDispatcher.entityPitch;
            final boolean flag = false;
            EntityRenderer.drawNameplate(this.getFontRenderer(), str, (float)x + 0.5f, (float)y + 1.5f, (float)z + 0.5f, 0, f, f2, false, false);
        }
    }
    
    public void renderTileEntityFast(final T p_renderTileEntityFast_1_, final double p_renderTileEntityFast_2_, final double p_renderTileEntityFast_4_, final double p_renderTileEntityFast_6_, final float p_renderTileEntityFast_8_, final int p_renderTileEntityFast_9_, final float p_renderTileEntityFast_10_, final BufferBuilder p_renderTileEntityFast_11_) {
    }
    
    @Override
    public Class getEntityClass() {
        return this.tileEntityClass;
    }
    
    @Override
    public void setEntityClass(final Class p_setEntityClass_1_) {
        this.tileEntityClass = p_setEntityClass_1_;
    }
    
    @Override
    public ResourceLocation getLocationTextureCustom() {
        return this.locationTextureCustom;
    }
    
    @Override
    public void setLocationTextureCustom(final ResourceLocation p_setLocationTextureCustom_1_) {
        this.locationTextureCustom = p_setLocationTextureCustom_1_;
    }
    
    static {
        DESTROY_STAGES = new ResourceLocation[] { new ResourceLocation("textures/blocks/destroy_stage_0.png"), new ResourceLocation("textures/blocks/destroy_stage_1.png"), new ResourceLocation("textures/blocks/destroy_stage_2.png"), new ResourceLocation("textures/blocks/destroy_stage_3.png"), new ResourceLocation("textures/blocks/destroy_stage_4.png"), new ResourceLocation("textures/blocks/destroy_stage_5.png"), new ResourceLocation("textures/blocks/destroy_stage_6.png"), new ResourceLocation("textures/blocks/destroy_stage_7.png"), new ResourceLocation("textures/blocks/destroy_stage_8.png"), new ResourceLocation("textures/blocks/destroy_stage_9.png") };
    }
}
