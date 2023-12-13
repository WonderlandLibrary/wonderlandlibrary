// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;

public class BlockNetherWart extends BlockBush
{
    public static final PropertyInteger AGE_PROP;
    private static final String __OBFID = "CL_00000274";
    
    protected BlockNetherWart() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockNetherWart.AGE_PROP, 0));
        this.setTickRandomly(true);
        final float var1 = 0.5f;
        this.setBlockBounds(0.5f - var1, 0.0f, 0.5f - var1, 0.5f + var1, 0.25f, 0.5f + var1);
        this.setCreativeTab(null);
    }
    
    @Override
    protected boolean canPlaceBlockOn(final Block ground) {
        return ground == Blocks.soul_sand;
    }
    
    @Override
    public boolean canBlockStay(final World worldIn, final BlockPos p_180671_2_, final IBlockState p_180671_3_) {
        return this.canPlaceBlockOn(worldIn.getBlockState(p_180671_2_.offsetDown()).getBlock());
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, IBlockState state, final Random rand) {
        final int var5 = (int)state.getValue(BlockNetherWart.AGE_PROP);
        if (var5 < 3 && rand.nextInt(10) == 0) {
            state = state.withProperty(BlockNetherWart.AGE_PROP, var5 + 1);
            worldIn.setBlockState(pos, state, 2);
        }
        super.updateTick(worldIn, pos, state, rand);
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        if (!worldIn.isRemote) {
            int var6 = 1;
            if ((int)state.getValue(BlockNetherWart.AGE_PROP) >= 3) {
                var6 = 2 + worldIn.rand.nextInt(3);
                if (fortune > 0) {
                    var6 += worldIn.rand.nextInt(fortune + 1);
                }
            }
            for (int var7 = 0; var7 < var6; ++var7) {
                Block.spawnAsEntity(worldIn, pos, new ItemStack(Items.nether_wart));
            }
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return null;
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Items.nether_wart;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockNetherWart.AGE_PROP, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return (int)state.getValue(BlockNetherWart.AGE_PROP);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockNetherWart.AGE_PROP });
    }
    
    static {
        AGE_PROP = PropertyInteger.create("age", 0, 3);
    }
}
