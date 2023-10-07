// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;

public class BlockPressurePlateWeighted extends BlockBasePressurePlate
{
    public static final PropertyInteger POWER;
    private final int field_150068_a;
    private static final String __OBFID = "CL_00000334";
    
    protected BlockPressurePlateWeighted(final String p_i45436_1_, final Material p_i45436_2_, final int p_i45436_3_) {
        super(p_i45436_2_);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockPressurePlateWeighted.POWER, 0));
        this.field_150068_a = p_i45436_3_;
    }
    
    @Override
    protected int computeRedstoneStrength(final World worldIn, final BlockPos pos) {
        final int var3 = Math.min(worldIn.getEntitiesWithinAABB(Entity.class, this.getSensitiveAABB(pos)).size(), this.field_150068_a);
        if (var3 > 0) {
            final float var4 = Math.min(this.field_150068_a, var3) / this.field_150068_a;
            return MathHelper.ceiling_float_int(var4 * 15.0f);
        }
        return 0;
    }
    
    @Override
    protected int getRedstoneStrength(final IBlockState p_176576_1_) {
        return (int)p_176576_1_.getValue(BlockPressurePlateWeighted.POWER);
    }
    
    @Override
    protected IBlockState setRedstoneStrength(final IBlockState p_176575_1_, final int p_176575_2_) {
        return p_176575_1_.withProperty(BlockPressurePlateWeighted.POWER, p_176575_2_);
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return 10;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockPressurePlateWeighted.POWER, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return (int)state.getValue(BlockPressurePlateWeighted.POWER);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockPressurePlateWeighted.POWER });
    }
    
    static {
        POWER = PropertyInteger.create("power", 0, 15);
    }
}
