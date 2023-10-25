/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class TileEntitySpecialRenderer<T extends TileEntity> {
    protected TileEntityRendererDispatcher rendererDispatcher;
    protected static final ResourceLocation[] DESTROY_STAGES = new ResourceLocation[]{new ResourceLocation("textures/blocks/destroy_stage_0.png"), new ResourceLocation("textures/blocks/destroy_stage_1.png"), new ResourceLocation("textures/blocks/destroy_stage_2.png"), new ResourceLocation("textures/blocks/destroy_stage_3.png"), new ResourceLocation("textures/blocks/destroy_stage_4.png"), new ResourceLocation("textures/blocks/destroy_stage_5.png"), new ResourceLocation("textures/blocks/destroy_stage_6.png"), new ResourceLocation("textures/blocks/destroy_stage_7.png"), new ResourceLocation("textures/blocks/destroy_stage_8.png"), new ResourceLocation("textures/blocks/destroy_stage_9.png")};

    protected void bindTexture(ResourceLocation resourceLocation) {
        TextureManager textureManager = this.rendererDispatcher.renderEngine;
        if (textureManager != null) {
            textureManager.bindTexture(resourceLocation);
        }
    }

    public boolean func_181055_a() {
        return false;
    }

    public abstract void renderTileEntityAt(T var1, double var2, double var4, double var6, float var8, int var9);

    public void setRendererDispatcher(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        this.rendererDispatcher = tileEntityRendererDispatcher;
    }

    protected World getWorld() {
        return this.rendererDispatcher.worldObj;
    }

    public FontRenderer getFontRenderer() {
        return this.rendererDispatcher.getFontRenderer();
    }
}

