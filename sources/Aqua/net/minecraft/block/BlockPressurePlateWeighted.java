package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockPressurePlateWeighted
extends BlockBasePressurePlate {
    public static final PropertyInteger POWER = PropertyInteger.create((String)"power", (int)0, (int)15);
    private final int field_150068_a;

    protected BlockPressurePlateWeighted(Material p_i46379_1_, int p_i46379_2_) {
        this(p_i46379_1_, p_i46379_2_, p_i46379_1_.getMaterialMapColor());
    }

    protected BlockPressurePlateWeighted(Material p_i46380_1_, int p_i46380_2_, MapColor p_i46380_3_) {
        super(p_i46380_1_, p_i46380_3_);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)POWER, (Comparable)Integer.valueOf((int)0)));
        this.field_150068_a = p_i46380_2_;
    }

    protected int computeRedstoneStrength(World worldIn, BlockPos pos) {
        int i = Math.min((int)worldIn.getEntitiesWithinAABB(Entity.class, this.getSensitiveAABB(pos)).size(), (int)this.field_150068_a);
        if (i > 0) {
            float f = (float)Math.min((int)this.field_150068_a, (int)i) / (float)this.field_150068_a;
            return MathHelper.ceiling_float_int((float)(f * 15.0f));
        }
        return 0;
    }

    protected int getRedstoneStrength(IBlockState state) {
        return (Integer)state.getValue((IProperty)POWER);
    }

    protected IBlockState setRedstoneStrength(IBlockState state, int strength) {
        return state.withProperty((IProperty)POWER, (Comparable)Integer.valueOf((int)strength));
    }

    public int tickRate(World worldIn) {
        return 10;
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty((IProperty)POWER, (Comparable)Integer.valueOf((int)meta));
    }

    public int getMetaFromState(IBlockState state) {
        return (Integer)state.getValue((IProperty)POWER);
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{POWER});
    }
}
