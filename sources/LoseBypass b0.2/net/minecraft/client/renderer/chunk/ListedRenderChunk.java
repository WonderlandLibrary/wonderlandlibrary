/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;

public class ListedRenderChunk
extends RenderChunk {
    private final int baseDisplayList = GLAllocation.generateDisplayLists(EnumWorldBlockLayer.values().length);

    public ListedRenderChunk(World worldIn, RenderGlobal renderGlobalIn, BlockPos pos, int indexIn) {
        super(worldIn, renderGlobalIn, pos, indexIn);
    }

    public int getDisplayList(EnumWorldBlockLayer layer, CompiledChunk p_178600_2_) {
        if (p_178600_2_.isLayerEmpty(layer)) return -1;
        int n = this.baseDisplayList + layer.ordinal();
        return n;
    }

    @Override
    public void deleteGlResources() {
        super.deleteGlResources();
        GLAllocation.deleteDisplayLists(this.baseDisplayList, EnumWorldBlockLayer.values().length);
    }
}

