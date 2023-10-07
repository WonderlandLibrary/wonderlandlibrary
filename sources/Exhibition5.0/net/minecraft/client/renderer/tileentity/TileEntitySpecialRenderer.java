// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public abstract class TileEntitySpecialRenderer
{
    protected static final ResourceLocation[] DESTROY_STAGES;
    protected TileEntityRendererDispatcher rendererDispatcher;
    private static final String __OBFID = "CL_00000964";
    
    public abstract void renderTileEntityAt(final TileEntity p0, final double p1, final double p2, final double p3, final float p4, final int p5);
    
    protected void bindTexture(final ResourceLocation p_147499_1_) {
        final TextureManager var2 = this.rendererDispatcher.renderEngine;
        if (var2 != null) {
            var2.bindTexture(p_147499_1_);
        }
    }
    
    protected World getWorld() {
        return this.rendererDispatcher.worldObj;
    }
    
    public void setRendererDispatcher(final TileEntityRendererDispatcher p_147497_1_) {
        this.rendererDispatcher = p_147497_1_;
    }
    
    public FontRenderer getFontRenderer() {
        return this.rendererDispatcher.getFontRenderer();
    }
    
    static {
        DESTROY_STAGES = new ResourceLocation[] { new ResourceLocation("textures/blocks/destroy_stage_0.png"), new ResourceLocation("textures/blocks/destroy_stage_1.png"), new ResourceLocation("textures/blocks/destroy_stage_2.png"), new ResourceLocation("textures/blocks/destroy_stage_3.png"), new ResourceLocation("textures/blocks/destroy_stage_4.png"), new ResourceLocation("textures/blocks/destroy_stage_5.png"), new ResourceLocation("textures/blocks/destroy_stage_6.png"), new ResourceLocation("textures/blocks/destroy_stage_7.png"), new ResourceLocation("textures/blocks/destroy_stage_8.png"), new ResourceLocation("textures/blocks/destroy_stage_9.png") };
    }
}
