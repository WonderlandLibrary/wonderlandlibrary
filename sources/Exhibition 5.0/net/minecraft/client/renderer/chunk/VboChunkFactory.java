// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.chunk;

import net.minecraft.util.BlockPos;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.world.World;

public class VboChunkFactory implements IRenderChunkFactory
{
    private static final String __OBFID = "CL_00002451";
    
    @Override
    public RenderChunk func_178602_a(final World worldIn, final RenderGlobal p_178602_2_, final BlockPos p_178602_3_, final int p_178602_4_) {
        return new RenderChunk(worldIn, p_178602_2_, p_178602_3_, p_178602_4_);
    }
}
