// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import net.minecraft.util.BlockRenderLayer;

public class RegionRenderCacheBuilder
{
    private final BufferBuilder[] worldRenderers;
    
    public RegionRenderCacheBuilder() {
        (this.worldRenderers = new BufferBuilder[BlockRenderLayer.values().length])[BlockRenderLayer.SOLID.ordinal()] = new BufferBuilder(2097152);
        this.worldRenderers[BlockRenderLayer.CUTOUT.ordinal()] = new BufferBuilder(131072);
        this.worldRenderers[BlockRenderLayer.CUTOUT_MIPPED.ordinal()] = new BufferBuilder(131072);
        this.worldRenderers[BlockRenderLayer.TRANSLUCENT.ordinal()] = new BufferBuilder(262144);
    }
    
    public BufferBuilder getWorldRendererByLayer(final BlockRenderLayer layer) {
        return this.worldRenderers[layer.ordinal()];
    }
    
    public BufferBuilder getWorldRendererByLayerId(final int id) {
        return this.worldRenderers[id];
    }
}
