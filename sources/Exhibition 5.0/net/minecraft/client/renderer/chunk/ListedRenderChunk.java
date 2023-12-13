// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.BlockPos;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.world.World;

public class ListedRenderChunk extends RenderChunk
{
    private final int field_178601_d;
    private static final String __OBFID = "CL_00002453";
    
    public ListedRenderChunk(final World worldIn, final RenderGlobal p_i46198_2_, final BlockPos p_i46198_3_, final int p_i46198_4_) {
        super(worldIn, p_i46198_2_, p_i46198_3_, p_i46198_4_);
        this.field_178601_d = GLAllocation.generateDisplayLists(EnumWorldBlockLayer.values().length);
    }
    
    public int func_178600_a(final EnumWorldBlockLayer p_178600_1_, final CompiledChunk p_178600_2_) {
        return p_178600_2_.func_178491_b(p_178600_1_) ? -1 : (this.field_178601_d + p_178600_1_.ordinal());
    }
    
    @Override
    public void func_178566_a() {
        super.func_178566_a();
        GLAllocation.func_178874_a(this.field_178601_d, EnumWorldBlockLayer.values().length);
    }
}
