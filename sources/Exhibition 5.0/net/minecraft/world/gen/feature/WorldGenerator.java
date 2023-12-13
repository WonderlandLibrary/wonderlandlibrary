// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;

public abstract class WorldGenerator
{
    private final boolean doBlockNotify;
    private static final String __OBFID = "CL_00000409";
    
    public WorldGenerator() {
        this(false);
    }
    
    public WorldGenerator(final boolean p_i2013_1_) {
        this.doBlockNotify = p_i2013_1_;
    }
    
    public abstract boolean generate(final World p0, final Random p1, final BlockPos p2);
    
    public void func_175904_e() {
    }
    
    protected void func_175906_a(final World worldIn, final BlockPos p_175906_2_, final Block p_175906_3_) {
        this.func_175905_a(worldIn, p_175906_2_, p_175906_3_, 0);
    }
    
    protected void func_175905_a(final World worldIn, final BlockPos p_175905_2_, final Block p_175905_3_, final int p_175905_4_) {
        this.func_175903_a(worldIn, p_175905_2_, p_175905_3_.getStateFromMeta(p_175905_4_));
    }
    
    protected void func_175903_a(final World worldIn, final BlockPos p_175903_2_, final IBlockState p_175903_3_) {
        if (this.doBlockNotify) {
            worldIn.setBlockState(p_175903_2_, p_175903_3_, 3);
        }
        else {
            worldIn.setBlockState(p_175903_2_, p_175903_3_, 2);
        }
    }
}
