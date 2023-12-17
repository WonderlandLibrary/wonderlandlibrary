// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.world.IBlockAccess;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyInteger;

public class BlockBeetroot extends BlockCrops
{
    public static final PropertyInteger BEETROOT_AGE;
    private static final AxisAlignedBB[] BEETROOT_AABB;
    
    @Override
    protected PropertyInteger getAgeProperty() {
        return BlockBeetroot.BEETROOT_AGE;
    }
    
    @Override
    public int getMaxAge() {
        return 3;
    }
    
    @Override
    protected Item getSeed() {
        return Items.BEETROOT_SEEDS;
    }
    
    @Override
    protected Item getCrop() {
        return Items.BEETROOT;
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (rand.nextInt(3) == 0) {
            this.checkAndDropBlock(worldIn, pos, state);
        }
        else {
            super.updateTick(worldIn, pos, state, rand);
        }
    }
    
    @Override
    protected int getBonemealAgeIncrease(final World worldIn) {
        return super.getBonemealAgeIncrease(worldIn) / 3;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockBeetroot.BEETROOT_AGE });
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return BlockBeetroot.BEETROOT_AABB[state.getValue((IProperty<Integer>)this.getAgeProperty())];
    }
    
    static {
        BEETROOT_AGE = PropertyInteger.create("age", 0, 3);
        BEETROOT_AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.125, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.25, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.375, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0) };
    }
}
