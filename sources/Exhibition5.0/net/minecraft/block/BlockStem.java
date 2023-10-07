// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.state.BlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.block.material.Material;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import java.util.Iterator;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;

public class BlockStem extends BlockBush implements IGrowable
{
    public static final PropertyInteger AGE_PROP;
    public static final PropertyDirection FACING_PROP;
    private final Block cropBlock;
    private static final String __OBFID = "CL_00000316";
    
    protected BlockStem(final Block p_i45430_1_) {
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockStem.AGE_PROP, 0).withProperty(BlockStem.FACING_PROP, EnumFacing.UP));
        this.cropBlock = p_i45430_1_;
        this.setTickRandomly(true);
        final float var2 = 0.125f;
        this.setBlockBounds(0.5f - var2, 0.0f, 0.5f - var2, 0.5f + var2, 0.25f, 0.5f + var2);
        this.setCreativeTab(null);
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        state = state.withProperty(BlockStem.FACING_PROP, EnumFacing.UP);
        for (final EnumFacing var5 : EnumFacing.Plane.HORIZONTAL) {
            if (worldIn.getBlockState(pos.offset(var5)).getBlock() == this.cropBlock) {
                state = state.withProperty(BlockStem.FACING_PROP, var5);
                break;
            }
        }
        return state;
    }
    
    @Override
    protected boolean canPlaceBlockOn(final Block ground) {
        return ground == Blocks.farmland;
    }
    
    @Override
    public void updateTick(final World worldIn, BlockPos pos, IBlockState state, final Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        if (worldIn.getLightFromNeighbors(pos.offsetUp()) >= 9) {
            final float var5 = BlockCrops.getGrowthChance(this, worldIn, pos);
            if (rand.nextInt((int)(25.0f / var5) + 1) == 0) {
                final int var6 = (int)state.getValue(BlockStem.AGE_PROP);
                if (var6 < 7) {
                    state = state.withProperty(BlockStem.AGE_PROP, var6 + 1);
                    worldIn.setBlockState(pos, state, 2);
                }
                else {
                    for (final EnumFacing var8 : EnumFacing.Plane.HORIZONTAL) {
                        if (worldIn.getBlockState(pos.offset(var8)).getBlock() == this.cropBlock) {
                            return;
                        }
                    }
                    pos = pos.offset(EnumFacing.Plane.HORIZONTAL.random(rand));
                    final Block var9 = worldIn.getBlockState(pos.offsetDown()).getBlock();
                    if (worldIn.getBlockState(pos).getBlock().blockMaterial == Material.air && (var9 == Blocks.farmland || var9 == Blocks.dirt || var9 == Blocks.grass)) {
                        worldIn.setBlockState(pos, this.cropBlock.getDefaultState());
                    }
                }
            }
        }
    }
    
    public void growStem(final World worldIn, final BlockPos p_176482_2_, final IBlockState p_176482_3_) {
        final int var4 = (int)p_176482_3_.getValue(BlockStem.AGE_PROP) + MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);
        worldIn.setBlockState(p_176482_2_, p_176482_3_.withProperty(BlockStem.AGE_PROP, Math.min(7, var4)), 2);
    }
    
    @Override
    public int getRenderColor(final IBlockState state) {
        if (state.getBlock() != this) {
            return super.getRenderColor(state);
        }
        final int var2 = (int)state.getValue(BlockStem.AGE_PROP);
        final int var3 = var2 * 32;
        final int var4 = 255 - var2 * 8;
        final int var5 = var2 * 4;
        return var3 << 16 | var4 << 8 | var5;
    }
    
    @Override
    public int colorMultiplier(final IBlockAccess worldIn, final BlockPos pos, final int renderPass) {
        return this.getRenderColor(worldIn.getBlockState(pos));
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
        final float var1 = 0.125f;
        this.setBlockBounds(0.5f - var1, 0.0f, 0.5f - var1, 0.5f + var1, 0.25f, 0.5f + var1);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        this.maxY = ((int)access.getBlockState(pos).getValue(BlockStem.AGE_PROP) * 2 + 2) / 16.0f;
        final float var3 = 0.125f;
        this.setBlockBounds(0.5f - var3, 0.0f, 0.5f - var3, 0.5f + var3, (float)this.maxY, 0.5f + var3);
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        if (!worldIn.isRemote) {
            final Item var6 = this.getSeedItem();
            if (var6 != null) {
                final int var7 = (int)state.getValue(BlockStem.AGE_PROP);
                for (int var8 = 0; var8 < 3; ++var8) {
                    if (worldIn.rand.nextInt(15) <= var7) {
                        Block.spawnAsEntity(worldIn, pos, new ItemStack(var6));
                    }
                }
            }
        }
    }
    
    protected Item getSeedItem() {
        return (this.cropBlock == Blocks.pumpkin) ? Items.pumpkin_seeds : ((this.cropBlock == Blocks.melon_block) ? Items.melon_seeds : null);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return null;
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        final Item var3 = this.getSeedItem();
        return (var3 != null) ? var3 : null;
    }
    
    @Override
    public boolean isStillGrowing(final World worldIn, final BlockPos p_176473_2_, final IBlockState p_176473_3_, final boolean p_176473_4_) {
        return (int)p_176473_3_.getValue(BlockStem.AGE_PROP) != 7;
    }
    
    @Override
    public boolean canUseBonemeal(final World worldIn, final Random p_180670_2_, final BlockPos p_180670_3_, final IBlockState p_180670_4_) {
        return true;
    }
    
    @Override
    public void grow(final World worldIn, final Random p_176474_2_, final BlockPos p_176474_3_, final IBlockState p_176474_4_) {
        this.growStem(worldIn, p_176474_3_, p_176474_4_);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockStem.AGE_PROP, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return (int)state.getValue(BlockStem.AGE_PROP);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockStem.AGE_PROP, BlockStem.FACING_PROP });
    }
    
    static {
        AGE_PROP = PropertyInteger.create("age", 0, 7);
        FACING_PROP = PropertyDirection.create("facing", (Predicate)new Predicate() {
            private static final String __OBFID = "CL_00002059";
            
            public boolean apply(final EnumFacing p_177218_1_) {
                return p_177218_1_ != EnumFacing.DOWN;
            }
            
            public boolean apply(final Object p_apply_1_) {
                return this.apply((EnumFacing)p_apply_1_);
            }
        });
    }
}
