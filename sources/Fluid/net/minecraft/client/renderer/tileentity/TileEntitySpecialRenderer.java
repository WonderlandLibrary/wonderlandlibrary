// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.optifine.entity.model.IEntityRenderer;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntitySpecialRenderer<T extends TileEntity> implements IEntityRenderer
{
    protected static final ResourceLocation[] DESTROY_STAGES;
    protected TileEntityRendererDispatcher rendererDispatcher;
    private Class tileEntityClass;
    private ResourceLocation locationTextureCustom;
    
    static {
        DESTROY_STAGES = new ResourceLocation[] { new ResourceLocation("textures/blocks/destroy_stage_0.png"), new ResourceLocation("textures/blocks/destroy_stage_1.png"), new ResourceLocation("textures/blocks/destroy_stage_2.png"), new ResourceLocation("textures/blocks/destroy_stage_3.png"), new ResourceLocation("textures/blocks/destroy_stage_4.png"), new ResourceLocation("textures/blocks/destroy_stage_5.png"), new ResourceLocation("textures/blocks/destroy_stage_6.png"), new ResourceLocation("textures/blocks/destroy_stage_7.png"), new ResourceLocation("textures/blocks/destroy_stage_8.png"), new ResourceLocation("textures/blocks/destroy_stage_9.png") };
    }
    
    public TileEntitySpecialRenderer() {
        this.tileEntityClass = null;
        this.locationTextureCustom = null;
    }
    
    public abstract void renderTileEntityAt(final T p0, final double p1, final double p2, final double p3, final float p4, final int p5);
    
    protected void bindTexture(final ResourceLocation location) {
        final TextureManager texturemanager = this.rendererDispatcher.renderEngine;
        if (texturemanager != null) {
            texturemanager.bindTexture(location);
        }
    }
    
    protected World getWorld() {
        return this.rendererDispatcher.worldObj;
    }
    
    public void setRendererDispatcher(final TileEntityRendererDispatcher rendererDispatcherIn) {
        this.rendererDispatcher = rendererDispatcherIn;
    }
    
    public FontRenderer getFontRenderer() {
        return this.rendererDispatcher.getFontRenderer();
    }
    
    public boolean forceTileEntityRender() {
        return false;
    }
    
    public void renderTileEntityFast(final T p_renderTileEntityFast_1_, final double p_renderTileEntityFast_2_, final double p_renderTileEntityFast_4_, final double p_renderTileEntityFast_6_, final float p_renderTileEntityFast_8_, final int p_renderTileEntityFast_9_, final WorldRenderer p_renderTileEntityFast_10_) {
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
}
