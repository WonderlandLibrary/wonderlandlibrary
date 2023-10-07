// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block.state.pattern;

import net.minecraft.block.state.BlockWorldState;
import net.minecraft.util.Vec3i;
import java.util.Iterator;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheBuilder;
import net.minecraft.world.World;
import com.google.common.cache.LoadingCache;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import com.google.common.base.Predicate;

public class BlockPattern
{
    private final Predicate[][][] field_177689_a;
    private final int field_177687_b;
    private final int field_177688_c;
    private final int field_177686_d;
    private static final String __OBFID = "CL_00002024";
    
    public BlockPattern(final Predicate[][][] p_i45657_1_) {
        this.field_177689_a = p_i45657_1_;
        this.field_177687_b = p_i45657_1_.length;
        if (this.field_177687_b > 0) {
            this.field_177688_c = p_i45657_1_[0].length;
            if (this.field_177688_c > 0) {
                this.field_177686_d = p_i45657_1_[0][0].length;
            }
            else {
                this.field_177686_d = 0;
            }
        }
        else {
            this.field_177688_c = 0;
            this.field_177686_d = 0;
        }
    }
    
    public int func_177685_b() {
        return this.field_177688_c;
    }
    
    public int func_177684_c() {
        return this.field_177686_d;
    }
    
    private PatternHelper func_177682_a(final BlockPos p_177682_1_, final EnumFacing p_177682_2_, final EnumFacing p_177682_3_, final LoadingCache p_177682_4_) {
        for (int var5 = 0; var5 < this.field_177686_d; ++var5) {
            for (int var6 = 0; var6 < this.field_177688_c; ++var6) {
                for (int var7 = 0; var7 < this.field_177687_b; ++var7) {
                    if (!this.field_177689_a[var7][var6][var5].apply(p_177682_4_.getUnchecked((Object)func_177683_a(p_177682_1_, p_177682_2_, p_177682_3_, var5, var6, var7)))) {
                        return null;
                    }
                }
            }
        }
        return new PatternHelper(p_177682_1_, p_177682_2_, p_177682_3_, p_177682_4_);
    }
    
    public PatternHelper func_177681_a(final World worldIn, final BlockPos p_177681_2_) {
        final LoadingCache var3 = CacheBuilder.newBuilder().build((com.google.common.cache.CacheLoader)new CacheLoader(worldIn));
        final int var4 = Math.max(Math.max(this.field_177686_d, this.field_177688_c), this.field_177687_b);
        for (final BlockPos var6 : BlockPos.getAllInBox(p_177681_2_, p_177681_2_.add(var4 - 1, var4 - 1, var4 - 1))) {
            for (final EnumFacing var10 : EnumFacing.values()) {
                for (final EnumFacing var14 : EnumFacing.values()) {
                    if (var14 != var10 && var14 != var10.getOpposite()) {
                        final PatternHelper var15 = this.func_177682_a(var6, var10, var14, var3);
                        if (var15 != null) {
                            return var15;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    protected static BlockPos func_177683_a(final BlockPos p_177683_0_, final EnumFacing p_177683_1_, final EnumFacing p_177683_2_, final int p_177683_3_, final int p_177683_4_, final int p_177683_5_) {
        if (p_177683_1_ != p_177683_2_ && p_177683_1_ != p_177683_2_.getOpposite()) {
            final Vec3i var6 = new Vec3i(p_177683_1_.getFrontOffsetX(), p_177683_1_.getFrontOffsetY(), p_177683_1_.getFrontOffsetZ());
            final Vec3i var7 = new Vec3i(p_177683_2_.getFrontOffsetX(), p_177683_2_.getFrontOffsetY(), p_177683_2_.getFrontOffsetZ());
            final Vec3i var8 = var6.crossProduct(var7);
            return p_177683_0_.add(var7.getX() * -p_177683_4_ + var8.getX() * p_177683_3_ + var6.getX() * p_177683_5_, var7.getY() * -p_177683_4_ + var8.getY() * p_177683_3_ + var6.getY() * p_177683_5_, var7.getZ() * -p_177683_4_ + var8.getZ() * p_177683_3_ + var6.getZ() * p_177683_5_);
        }
        throw new IllegalArgumentException("Invalid forwards & up combination");
    }
    
    static class CacheLoader extends com.google.common.cache.CacheLoader
    {
        private final World field_177680_a;
        private static final String __OBFID = "CL_00002023";
        
        public CacheLoader(final World worldIn) {
            this.field_177680_a = worldIn;
        }
        
        public BlockWorldState func_177679_a(final BlockPos p_177679_1_) {
            return new BlockWorldState(this.field_177680_a, p_177679_1_);
        }
        
        public Object load(final Object p_load_1_) {
            return this.func_177679_a((BlockPos)p_load_1_);
        }
    }
    
    public static class PatternHelper
    {
        private final BlockPos field_177674_a;
        private final EnumFacing field_177672_b;
        private final EnumFacing field_177673_c;
        private final LoadingCache field_177671_d;
        private static final String __OBFID = "CL_00002022";
        
        public PatternHelper(final BlockPos p_i45655_1_, final EnumFacing p_i45655_2_, final EnumFacing p_i45655_3_, final LoadingCache p_i45655_4_) {
            this.field_177674_a = p_i45655_1_;
            this.field_177672_b = p_i45655_2_;
            this.field_177673_c = p_i45655_3_;
            this.field_177671_d = p_i45655_4_;
        }
        
        public EnumFacing func_177669_b() {
            return this.field_177672_b;
        }
        
        public EnumFacing func_177668_c() {
            return this.field_177673_c;
        }
        
        public BlockWorldState func_177670_a(final int p_177670_1_, final int p_177670_2_, final int p_177670_3_) {
            return (BlockWorldState)this.field_177671_d.getUnchecked((Object)BlockPattern.func_177683_a(this.field_177674_a, this.func_177669_b(), this.func_177668_c(), p_177670_1_, p_177670_2_, p_177670_3_));
        }
    }
}
