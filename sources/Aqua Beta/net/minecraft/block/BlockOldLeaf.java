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
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockOldLeaf
extends BlockLeaves {
    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create((String)"variant", BlockPlanks.EnumType.class, (Predicate)new /* Unavailable Anonymous Inner Class!! */);

    public BlockOldLeaf() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, (Comparable)BlockPlanks.EnumType.OAK).withProperty((IProperty)CHECK_DECAY, (Comparable)Boolean.valueOf((boolean)true)).withProperty((IProperty)DECAYABLE, (Comparable)Boolean.valueOf((boolean)true)));
    }

    public int getRenderColor(IBlockState state) {
        if (state.getBlock() != this) {
            return super.getRenderColor(state);
        }
        BlockPlanks.EnumType blockplanks$enumtype = (BlockPlanks.EnumType)state.getValue(VARIANT);
        return blockplanks$enumtype == BlockPlanks.EnumType.SPRUCE ? ColorizerFoliage.getFoliageColorPine() : (blockplanks$enumtype == BlockPlanks.EnumType.BIRCH ? ColorizerFoliage.getFoliageColorBirch() : super.getRenderColor(state));
    }

    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() == this) {
            BlockPlanks.EnumType blockplanks$enumtype = (BlockPlanks.EnumType)iblockstate.getValue(VARIANT);
            if (blockplanks$enumtype == BlockPlanks.EnumType.SPRUCE) {
                return ColorizerFoliage.getFoliageColorPine();
            }
            if (blockplanks$enumtype == BlockPlanks.EnumType.BIRCH) {
                return ColorizerFoliage.getFoliageColorBirch();
            }
        }
        return super.colorMultiplier(worldIn, pos, renderPass);
    }

    protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance) {
        if (state.getValue(VARIANT) == BlockPlanks.EnumType.OAK && worldIn.rand.nextInt(chance) == 0) {
            BlockOldLeaf.spawnAsEntity((World)worldIn, (BlockPos)pos, (ItemStack)new ItemStack(Items.apple, 1, 0));
        }
    }

    protected int getSaplingDropChance(IBlockState state) {
        return state.getValue(VARIANT) == BlockPlanks.EnumType.JUNGLE ? 40 : super.getSaplingDropChance(state);
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        list.add((Object)new ItemStack(itemIn, 1, BlockPlanks.EnumType.OAK.getMetadata()));
        list.add((Object)new ItemStack(itemIn, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
        list.add((Object)new ItemStack(itemIn, 1, BlockPlanks.EnumType.BIRCH.getMetadata()));
        list.add((Object)new ItemStack(itemIn, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()));
    }

    protected ItemStack createStackedBlock(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock((Block)this), 1, ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata());
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, (Comparable)this.getWoodType(meta)).withProperty((IProperty)DECAYABLE, (Comparable)Boolean.valueOf(((meta & 4) == 0 ? 1 : 0) != 0)).withProperty((IProperty)CHECK_DECAY, (Comparable)Boolean.valueOf(((meta & 8) > 0 ? 1 : 0) != 0));
    }

    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata();
        if (!((Boolean)state.getValue((IProperty)DECAYABLE)).booleanValue()) {
            i |= 4;
        }
        if (((Boolean)state.getValue((IProperty)CHECK_DECAY)).booleanValue()) {
            i |= 8;
        }
        return i;
    }

    public BlockPlanks.EnumType getWoodType(int meta) {
        return BlockPlanks.EnumType.byMetadata((int)((meta & 3) % 4));
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{VARIANT, CHECK_DECAY, DECAYABLE});
    }

    public int damageDropped(IBlockState state) {
        return ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata();
    }

    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
        if (!worldIn.isRemote && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears) {
            player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock((Block)this)]);
            BlockOldLeaf.spawnAsEntity((World)worldIn, (BlockPos)pos, (ItemStack)new ItemStack(Item.getItemFromBlock((Block)this), 1, ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata()));
        } else {
            super.harvestBlock(worldIn, player, pos, state, te);
        }
    }
}
