// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.ChunkCache;
import net.minecraft.block.state.BlockStateContainer;
import javax.annotation.Nullable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;

public class BlockFlowerPot extends BlockContainer
{
    public static final PropertyInteger LEGACY_DATA;
    public static final PropertyEnum<EnumFlowerType> CONTENTS;
    protected static final AxisAlignedBB FLOWER_POT_AABB;
    
    public BlockFlowerPot() {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockFlowerPot.CONTENTS, EnumFlowerType.EMPTY).withProperty((IProperty<Comparable>)BlockFlowerPot.LEGACY_DATA, 0));
    }
    
    @Override
    public String getLocalizedName() {
        return I18n.translateToLocal("item.flowerPot.name");
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return BlockFlowerPot.FLOWER_POT_AABB;
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public EnumBlockRenderType getRenderType(final IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
    
    @Override
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        final ItemStack itemstack = playerIn.getHeldItem(hand);
        final TileEntityFlowerPot tileentityflowerpot = this.getTileEntity(worldIn, pos);
        if (tileentityflowerpot == null) {
            return false;
        }
        final ItemStack itemstack2 = tileentityflowerpot.getFlowerItemStack();
        if (itemstack2.isEmpty()) {
            if (!this.canBePotted(itemstack)) {
                return false;
            }
            tileentityflowerpot.setItemStack(itemstack);
            playerIn.addStat(StatList.FLOWER_POTTED);
            if (!playerIn.capabilities.isCreativeMode) {
                itemstack.shrink(1);
            }
        }
        else {
            if (itemstack.isEmpty()) {
                playerIn.setHeldItem(hand, itemstack2);
            }
            else if (!playerIn.addItemStackToInventory(itemstack2)) {
                playerIn.dropItem(itemstack2, false);
            }
            tileentityflowerpot.setItemStack(ItemStack.EMPTY);
        }
        tileentityflowerpot.markDirty();
        worldIn.notifyBlockUpdate(pos, state, state, 3);
        return true;
    }
    
    private boolean canBePotted(final ItemStack stack) {
        final Block block = Block.getBlockFromItem(stack.getItem());
        if (block != Blocks.YELLOW_FLOWER && block != Blocks.RED_FLOWER && block != Blocks.CACTUS && block != Blocks.BROWN_MUSHROOM && block != Blocks.RED_MUSHROOM && block != Blocks.SAPLING && block != Blocks.DEADBUSH) {
            final int i = stack.getMetadata();
            return block == Blocks.TALLGRASS && i == BlockTallGrass.EnumType.FERN.getMeta();
        }
        return true;
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        final TileEntityFlowerPot tileentityflowerpot = this.getTileEntity(worldIn, pos);
        if (tileentityflowerpot != null) {
            final ItemStack itemstack = tileentityflowerpot.getFlowerItemStack();
            if (!itemstack.isEmpty()) {
                return itemstack;
            }
        }
        return new ItemStack(Items.FLOWER_POT);
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && worldIn.getBlockState(pos.down()).isTopSolid();
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (!worldIn.getBlockState(pos.down()).isTopSolid()) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        final TileEntityFlowerPot tileentityflowerpot = this.getTileEntity(worldIn, pos);
        if (tileentityflowerpot != null && tileentityflowerpot.getFlowerPotItem() != null) {
            Block.spawnAsEntity(worldIn, pos, new ItemStack(tileentityflowerpot.getFlowerPotItem(), 1, tileentityflowerpot.getFlowerPotData()));
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public void onBlockHarvested(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer player) {
        super.onBlockHarvested(worldIn, pos, state, player);
        if (player.capabilities.isCreativeMode) {
            final TileEntityFlowerPot tileentityflowerpot = this.getTileEntity(worldIn, pos);
            if (tileentityflowerpot != null) {
                tileentityflowerpot.setItemStack(ItemStack.EMPTY);
            }
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.FLOWER_POT;
    }
    
    @Nullable
    private TileEntityFlowerPot getTileEntity(final World worldIn, final BlockPos pos) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        return (tileentity instanceof TileEntityFlowerPot) ? ((TileEntityFlowerPot)tileentity) : null;
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        Block block = null;
        int i = 0;
        switch (meta) {
            case 1: {
                block = Blocks.RED_FLOWER;
                i = BlockFlower.EnumFlowerType.POPPY.getMeta();
                break;
            }
            case 2: {
                block = Blocks.YELLOW_FLOWER;
                break;
            }
            case 3: {
                block = Blocks.SAPLING;
                i = BlockPlanks.EnumType.OAK.getMetadata();
                break;
            }
            case 4: {
                block = Blocks.SAPLING;
                i = BlockPlanks.EnumType.SPRUCE.getMetadata();
                break;
            }
            case 5: {
                block = Blocks.SAPLING;
                i = BlockPlanks.EnumType.BIRCH.getMetadata();
                break;
            }
            case 6: {
                block = Blocks.SAPLING;
                i = BlockPlanks.EnumType.JUNGLE.getMetadata();
                break;
            }
            case 7: {
                block = Blocks.RED_MUSHROOM;
                break;
            }
            case 8: {
                block = Blocks.BROWN_MUSHROOM;
                break;
            }
            case 9: {
                block = Blocks.CACTUS;
                break;
            }
            case 10: {
                block = Blocks.DEADBUSH;
                break;
            }
            case 11: {
                block = Blocks.TALLGRASS;
                i = BlockTallGrass.EnumType.FERN.getMeta();
                break;
            }
            case 12: {
                block = Blocks.SAPLING;
                i = BlockPlanks.EnumType.ACACIA.getMetadata();
                break;
            }
            case 13: {
                block = Blocks.SAPLING;
                i = BlockPlanks.EnumType.DARK_OAK.getMetadata();
                break;
            }
        }
        return new TileEntityFlowerPot(Item.getItemFromBlock(block), i);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockFlowerPot.CONTENTS, BlockFlowerPot.LEGACY_DATA });
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockFlowerPot.LEGACY_DATA);
    }
    
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        EnumFlowerType blockflowerpot$enumflowertype = EnumFlowerType.EMPTY;
        final TileEntity tileentity = (worldIn instanceof ChunkCache) ? ((ChunkCache)worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityFlowerPot) {
            final TileEntityFlowerPot tileentityflowerpot = (TileEntityFlowerPot)tileentity;
            final Item item = tileentityflowerpot.getFlowerPotItem();
            if (item instanceof ItemBlock) {
                final int i = tileentityflowerpot.getFlowerPotData();
                final Block block = Block.getBlockFromItem(item);
                if (block == Blocks.SAPLING) {
                    switch (BlockPlanks.EnumType.byMetadata(i)) {
                        case OAK: {
                            blockflowerpot$enumflowertype = EnumFlowerType.OAK_SAPLING;
                            break;
                        }
                        case SPRUCE: {
                            blockflowerpot$enumflowertype = EnumFlowerType.SPRUCE_SAPLING;
                            break;
                        }
                        case BIRCH: {
                            blockflowerpot$enumflowertype = EnumFlowerType.BIRCH_SAPLING;
                            break;
                        }
                        case JUNGLE: {
                            blockflowerpot$enumflowertype = EnumFlowerType.JUNGLE_SAPLING;
                            break;
                        }
                        case ACACIA: {
                            blockflowerpot$enumflowertype = EnumFlowerType.ACACIA_SAPLING;
                            break;
                        }
                        case DARK_OAK: {
                            blockflowerpot$enumflowertype = EnumFlowerType.DARK_OAK_SAPLING;
                            break;
                        }
                        default: {
                            blockflowerpot$enumflowertype = EnumFlowerType.EMPTY;
                            break;
                        }
                    }
                }
                else if (block == Blocks.TALLGRASS) {
                    switch (i) {
                        case 0: {
                            blockflowerpot$enumflowertype = EnumFlowerType.DEAD_BUSH;
                            break;
                        }
                        case 2: {
                            blockflowerpot$enumflowertype = EnumFlowerType.FERN;
                            break;
                        }
                        default: {
                            blockflowerpot$enumflowertype = EnumFlowerType.EMPTY;
                            break;
                        }
                    }
                }
                else if (block == Blocks.YELLOW_FLOWER) {
                    blockflowerpot$enumflowertype = EnumFlowerType.DANDELION;
                }
                else if (block == Blocks.RED_FLOWER) {
                    switch (BlockFlower.EnumFlowerType.getType(BlockFlower.EnumFlowerColor.RED, i)) {
                        case POPPY: {
                            blockflowerpot$enumflowertype = EnumFlowerType.POPPY;
                            break;
                        }
                        case BLUE_ORCHID: {
                            blockflowerpot$enumflowertype = EnumFlowerType.BLUE_ORCHID;
                            break;
                        }
                        case ALLIUM: {
                            blockflowerpot$enumflowertype = EnumFlowerType.ALLIUM;
                            break;
                        }
                        case HOUSTONIA: {
                            blockflowerpot$enumflowertype = EnumFlowerType.HOUSTONIA;
                            break;
                        }
                        case RED_TULIP: {
                            blockflowerpot$enumflowertype = EnumFlowerType.RED_TULIP;
                            break;
                        }
                        case ORANGE_TULIP: {
                            blockflowerpot$enumflowertype = EnumFlowerType.ORANGE_TULIP;
                            break;
                        }
                        case WHITE_TULIP: {
                            blockflowerpot$enumflowertype = EnumFlowerType.WHITE_TULIP;
                            break;
                        }
                        case PINK_TULIP: {
                            blockflowerpot$enumflowertype = EnumFlowerType.PINK_TULIP;
                            break;
                        }
                        case OXEYE_DAISY: {
                            blockflowerpot$enumflowertype = EnumFlowerType.OXEYE_DAISY;
                            break;
                        }
                        default: {
                            blockflowerpot$enumflowertype = EnumFlowerType.EMPTY;
                            break;
                        }
                    }
                }
                else if (block == Blocks.RED_MUSHROOM) {
                    blockflowerpot$enumflowertype = EnumFlowerType.MUSHROOM_RED;
                }
                else if (block == Blocks.BROWN_MUSHROOM) {
                    blockflowerpot$enumflowertype = EnumFlowerType.MUSHROOM_BROWN;
                }
                else if (block == Blocks.DEADBUSH) {
                    blockflowerpot$enumflowertype = EnumFlowerType.DEAD_BUSH;
                }
                else if (block == Blocks.CACTUS) {
                    blockflowerpot$enumflowertype = EnumFlowerType.CACTUS;
                }
            }
        }
        return state.withProperty(BlockFlowerPot.CONTENTS, blockflowerpot$enumflowertype);
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        LEGACY_DATA = PropertyInteger.create("legacy_data", 0, 15);
        CONTENTS = PropertyEnum.create("contents", EnumFlowerType.class);
        FLOWER_POT_AABB = new AxisAlignedBB(0.3125, 0.0, 0.3125, 0.6875, 0.375, 0.6875);
    }
    
    public enum EnumFlowerType implements IStringSerializable
    {
        EMPTY("empty"), 
        POPPY("rose"), 
        BLUE_ORCHID("blue_orchid"), 
        ALLIUM("allium"), 
        HOUSTONIA("houstonia"), 
        RED_TULIP("red_tulip"), 
        ORANGE_TULIP("orange_tulip"), 
        WHITE_TULIP("white_tulip"), 
        PINK_TULIP("pink_tulip"), 
        OXEYE_DAISY("oxeye_daisy"), 
        DANDELION("dandelion"), 
        OAK_SAPLING("oak_sapling"), 
        SPRUCE_SAPLING("spruce_sapling"), 
        BIRCH_SAPLING("birch_sapling"), 
        JUNGLE_SAPLING("jungle_sapling"), 
        ACACIA_SAPLING("acacia_sapling"), 
        DARK_OAK_SAPLING("dark_oak_sapling"), 
        MUSHROOM_RED("mushroom_red"), 
        MUSHROOM_BROWN("mushroom_brown"), 
        DEAD_BUSH("dead_bush"), 
        FERN("fern"), 
        CACTUS("cactus");
        
        private final String name;
        
        private EnumFlowerType(final String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
    }
}
