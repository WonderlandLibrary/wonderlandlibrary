package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockNewLeaf
extends BlockLeaves {
    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create((String)"variant", BlockPlanks.EnumType.class, (Predicate)new /* Unavailable Anonymous Inner Class!! */);

    public BlockNewLeaf() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, (Comparable)BlockPlanks.EnumType.ACACIA).withProperty((IProperty)CHECK_DECAY, (Comparable)Boolean.valueOf((boolean)true)).withProperty((IProperty)DECAYABLE, (Comparable)Boolean.valueOf((boolean)true)));
    }

    protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance) {
        if (state.getValue(VARIANT) == BlockPlanks.EnumType.DARK_OAK && worldIn.rand.nextInt(chance) == 0) {
            BlockNewLeaf.spawnAsEntity((World)worldIn, (BlockPos)pos, (ItemStack)new ItemStack(Items.apple, 1, 0));
        }
    }

    public int damageDropped(IBlockState state) {
        return ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata();
    }

    public int getDamageValue(World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        return iblockstate.getBlock().getMetaFromState(iblockstate) & 3;
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        list.add((Object)new ItemStack(itemIn, 1, 0));
        list.add((Object)new ItemStack(itemIn, 1, 1));
    }

    protected ItemStack createStackedBlock(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock((Block)this), 1, ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata() - 4);
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, (Comparable)this.getWoodType(meta)).withProperty((IProperty)DECAYABLE, (Comparable)Boolean.valueOf(((meta & 4) == 0 ? 1 : 0) != 0)).withProperty((IProperty)CHECK_DECAY, (Comparable)Boolean.valueOf(((meta & 8) > 0 ? 1 : 0) != 0));
    }

    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata() - 4;
        if (!((Boolean)state.getValue((IProperty)DECAYABLE)).booleanValue()) {
            i |= 4;
        }
        if (((Boolean)state.getValue((IProperty)CHECK_DECAY)).booleanValue()) {
            i |= 8;
        }
        return i;
    }

    public BlockPlanks.EnumType getWoodType(int meta) {
        return BlockPlanks.EnumType.byMetadata((int)((meta & 3) + 4));
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{VARIANT, CHECK_DECAY, DECAYABLE});
    }

    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
        if (!worldIn.isRemote && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears) {
            player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock((Block)this)]);
            BlockNewLeaf.spawnAsEntity((World)worldIn, (BlockPos)pos, (ItemStack)new ItemStack(Item.getItemFromBlock((Block)this), 1, ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata() - 4));
        } else {
            super.harvestBlock(worldIn, player, pos, state, te);
        }
    }
}
