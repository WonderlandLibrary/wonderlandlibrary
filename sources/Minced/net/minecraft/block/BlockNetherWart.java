// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyInteger;

public class BlockNetherWart extends BlockBush
{
    public static final PropertyInteger AGE;
    private static final AxisAlignedBB[] NETHER_WART_AABB;
    
    protected BlockNetherWart() {
        super(Material.PLANTS, MapColor.RED);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockNetherWart.AGE, 0));
        this.setTickRandomly(true);
        this.setCreativeTab(null);
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return BlockNetherWart.NETHER_WART_AABB[state.getValue((IProperty<Integer>)BlockNetherWart.AGE)];
    }
    
    @Override
    protected boolean canSustainBush(final IBlockState state) {
        return state.getBlock() == Blocks.SOUL_SAND;
    }
    
    @Override
    public boolean canBlockStay(final World worldIn, final BlockPos pos, final IBlockState state) {
        return this.canSustainBush(worldIn.getBlockState(pos.down()));
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, IBlockState state, final Random rand) {
        final int i = state.getValue((IProperty<Integer>)BlockNetherWart.AGE);
        if (i < 3 && rand.nextInt(10) == 0) {
            state = state.withProperty((IProperty<Comparable>)BlockNetherWart.AGE, i + 1);
            worldIn.setBlockState(pos, state, 2);
        }
        super.updateTick(worldIn, pos, state, rand);
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        if (!worldIn.isRemote) {
            int i = 1;
            if (state.getValue((IProperty<Integer>)BlockNetherWart.AGE) >= 3) {
                i = 2 + worldIn.rand.nextInt(3);
                if (fortune > 0) {
                    i += worldIn.rand.nextInt(fortune + 1);
                }
            }
            for (int j = 0; j < i; ++j) {
                Block.spawnAsEntity(worldIn, pos, new ItemStack(Items.NETHER_WART));
            }
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.AIR;
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(Items.NETHER_WART);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockNetherWart.AGE, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockNetherWart.AGE);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockNetherWart.AGE });
    }
    
    static {
        AGE = PropertyInteger.create("age", 0, 3);
        NETHER_WART_AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.3125, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.6875, 1.0), new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.875, 1.0) };
    }
}
