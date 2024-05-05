package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/*
 * Exception performing whole class analysis ignored.
 */
public class BlockTallGrass
extends BlockBush
implements IGrowable {
    public static final PropertyEnum<EnumType> TYPE = PropertyEnum.create((String)"type", EnumType.class);

    protected BlockTallGrass() {
        super(Material.vine);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, (Comparable)EnumType.DEAD_BUSH));
        float f = 0.4f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, 0.8f, 0.5f + f);
    }

    public int getBlockColor() {
        return ColorizerGrass.getGrassColor((double)0.5, (double)1.0);
    }

    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
        return this.canPlaceBlockOn(worldIn.getBlockState(pos.down()).getBlock());
    }

    public boolean isReplaceable(World worldIn, BlockPos pos) {
        return true;
    }

    public int getRenderColor(IBlockState state) {
        if (state.getBlock() != this) {
            return super.getRenderColor(state);
        }
        EnumType blocktallgrass$enumtype = (EnumType)state.getValue(TYPE);
        return blocktallgrass$enumtype == EnumType.DEAD_BUSH ? 0xFFFFFF : ColorizerGrass.getGrassColor((double)0.5, (double)1.0);
    }

    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        return worldIn.getBiomeGenForCoords(pos).getGrassColorAtPos(pos);
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return rand.nextInt(8) == 0 ? Items.wheat_seeds : null;
    }

    public int quantityDroppedWithBonus(int fortune, Random random) {
        return 1 + random.nextInt(fortune * 2 + 1);
    }

    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
        if (!worldIn.isRemote && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears) {
            player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock((Block)this)]);
            BlockTallGrass.spawnAsEntity((World)worldIn, (BlockPos)pos, (ItemStack)new ItemStack((Block)Blocks.tallgrass, 1, ((EnumType)state.getValue(TYPE)).getMeta()));
        } else {
            super.harvestBlock(worldIn, player, pos, state, te);
        }
    }

    public int getDamageValue(World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        return iblockstate.getBlock().getMetaFromState(iblockstate);
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 1; i < 3; ++i) {
            list.add((Object)new ItemStack(itemIn, 1, i));
        }
    }

    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return state.getValue(TYPE) != EnumType.DEAD_BUSH;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return true;
    }

    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = BlockDoublePlant.EnumPlantType.GRASS;
        if (state.getValue(TYPE) == EnumType.FERN) {
            blockdoubleplant$enumplanttype = BlockDoublePlant.EnumPlantType.FERN;
        }
        if (Blocks.double_plant.canPlaceBlockAt(worldIn, pos)) {
            Blocks.double_plant.placeAt(worldIn, pos, blockdoubleplant$enumplanttype, 2);
        }
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, (Comparable)EnumType.byMetadata((int)meta));
    }

    public int getMetaFromState(IBlockState state) {
        return ((EnumType)state.getValue(TYPE)).getMeta();
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{TYPE});
    }

    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.XYZ;
    }
}
