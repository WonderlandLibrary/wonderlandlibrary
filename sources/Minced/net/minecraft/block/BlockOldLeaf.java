// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.stats.StatList;
import javax.annotation.Nullable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.item.Item;
import net.minecraft.util.NonNullList;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;

public class BlockOldLeaf extends BlockLeaves
{
    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT;
    
    public BlockOldLeaf() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty((IProperty<Comparable>)BlockOldLeaf.CHECK_DECAY, true).withProperty((IProperty<Comparable>)BlockOldLeaf.DECAYABLE, true));
    }
    
    @Override
    protected void dropApple(final World worldIn, final BlockPos pos, final IBlockState state, final int chance) {
        if (state.getValue(BlockOldLeaf.VARIANT) == BlockPlanks.EnumType.OAK && worldIn.rand.nextInt(chance) == 0) {
            Block.spawnAsEntity(worldIn, pos, new ItemStack(Items.APPLE));
        }
    }
    
    @Override
    protected int getSaplingDropChance(final IBlockState state) {
        return (state.getValue(BlockOldLeaf.VARIANT) == BlockPlanks.EnumType.JUNGLE) ? 40 : super.getSaplingDropChance(state);
    }
    
    @Override
    public void getSubBlocks(final CreativeTabs itemIn, final NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, BlockPlanks.EnumType.OAK.getMetadata()));
        items.add(new ItemStack(this, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
        items.add(new ItemStack(this, 1, BlockPlanks.EnumType.BIRCH.getMetadata()));
        items.add(new ItemStack(this, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()));
    }
    
    @Override
    protected ItemStack getSilkTouchDrop(final IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, state.getValue(BlockOldLeaf.VARIANT).getMetadata());
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockOldLeaf.VARIANT, this.getWoodType(meta)).withProperty((IProperty<Comparable>)BlockOldLeaf.DECAYABLE, (meta & 0x4) == 0x0).withProperty((IProperty<Comparable>)BlockOldLeaf.CHECK_DECAY, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue(BlockOldLeaf.VARIANT).getMetadata();
        if (!state.getValue((IProperty<Boolean>)BlockOldLeaf.DECAYABLE)) {
            i |= 0x4;
        }
        if (state.getValue((IProperty<Boolean>)BlockOldLeaf.CHECK_DECAY)) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    public BlockPlanks.EnumType getWoodType(final int meta) {
        return BlockPlanks.EnumType.byMetadata((meta & 0x3) % 4);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockOldLeaf.VARIANT, BlockOldLeaf.CHECK_DECAY, BlockOldLeaf.DECAYABLE });
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue(BlockOldLeaf.VARIANT).getMetadata();
    }
    
    @Override
    public void harvestBlock(final World worldIn, final EntityPlayer player, final BlockPos pos, final IBlockState state, @Nullable final TileEntity te, final ItemStack stack) {
        if (!worldIn.isRemote && stack.getItem() == Items.SHEARS) {
            player.addStat(StatList.getBlockStats(this));
            Block.spawnAsEntity(worldIn, pos, new ItemStack(Item.getItemFromBlock(this), 1, state.getValue(BlockOldLeaf.VARIANT).getMetadata()));
        }
        else {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
    }
    
    static {
        VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, (com.google.common.base.Predicate<BlockPlanks.EnumType>)new Predicate<BlockPlanks.EnumType>() {
            public boolean apply(@Nullable final BlockPlanks.EnumType p_apply_1_) {
                return p_apply_1_.getMetadata() < 4;
            }
        });
    }
}
