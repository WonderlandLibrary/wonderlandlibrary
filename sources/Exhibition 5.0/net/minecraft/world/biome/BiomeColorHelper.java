// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.biome;

import java.util.Iterator;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BiomeColorHelper
{
    private static final ColorResolver field_180291_a;
    private static final ColorResolver field_180289_b;
    private static final ColorResolver field_180290_c;
    private static final String __OBFID = "CL_00002149";
    
    private static int func_180285_a(final IBlockAccess p_180285_0_, final BlockPos p_180285_1_, final ColorResolver p_180285_2_) {
        int var3 = 0;
        int var4 = 0;
        int var5 = 0;
        for (final BlockPos.MutableBlockPos var7 : BlockPos.getAllInBoxMutable(p_180285_1_.add(-1, 0, -1), p_180285_1_.add(1, 0, 1))) {
            final int var8 = p_180285_2_.func_180283_a(p_180285_0_.getBiomeGenForCoords(var7), var7);
            var3 += (var8 & 0xFF0000) >> 16;
            var4 += (var8 & 0xFF00) >> 8;
            var5 += (var8 & 0xFF);
        }
        return (var3 / 9 & 0xFF) << 16 | (var4 / 9 & 0xFF) << 8 | (var5 / 9 & 0xFF);
    }
    
    public static int func_180286_a(final IBlockAccess p_180286_0_, final BlockPos p_180286_1_) {
        return func_180285_a(p_180286_0_, p_180286_1_, BiomeColorHelper.field_180291_a);
    }
    
    public static int func_180287_b(final IBlockAccess p_180287_0_, final BlockPos p_180287_1_) {
        return func_180285_a(p_180287_0_, p_180287_1_, BiomeColorHelper.field_180289_b);
    }
    
    public static int func_180288_c(final IBlockAccess p_180288_0_, final BlockPos p_180288_1_) {
        return func_180285_a(p_180288_0_, p_180288_1_, BiomeColorHelper.field_180290_c);
    }
    
    static {
        field_180291_a = new ColorResolver() {
            private static final String __OBFID = "CL_00002148";
            
            @Override
            public int func_180283_a(final BiomeGenBase p_180283_1_, final BlockPos p_180283_2_) {
                return p_180283_1_.func_180627_b(p_180283_2_);
            }
        };
        field_180289_b = new ColorResolver() {
            private static final String __OBFID = "CL_00002147";
            
            @Override
            public int func_180283_a(final BiomeGenBase p_180283_1_, final BlockPos p_180283_2_) {
                return p_180283_1_.func_180625_c(p_180283_2_);
            }
        };
        field_180290_c = new ColorResolver() {
            private static final String __OBFID = "CL_00002146";
            
            @Override
            public int func_180283_a(final BiomeGenBase p_180283_1_, final BlockPos p_180283_2_) {
                return p_180283_1_.waterColorMultiplier;
            }
        };
    }
    
    interface ColorResolver
    {
        int func_180283_a(final BiomeGenBase p0, final BlockPos p1);
    }
}
