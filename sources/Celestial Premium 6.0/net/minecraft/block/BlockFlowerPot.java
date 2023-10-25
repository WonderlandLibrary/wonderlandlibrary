/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BlockFlowerPot
extends BlockContainer {
    public static final PropertyInteger LEGACY_DATA = PropertyInteger.create("legacy_data", 0, 15);
    public static final PropertyEnum<EnumFlowerType> CONTENTS = PropertyEnum.create("contents", EnumFlowerType.class);
    protected static final AxisAlignedBB FLOWER_POT_AABB = new AxisAlignedBB(0.3125, 0.0, 0.3125, 0.6875, 0.375, 0.6875);

    public BlockFlowerPot() {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(CONTENTS, EnumFlowerType.EMPTY).withProperty(LEGACY_DATA, 0));
    }

    @Override
    public String getLocalizedName() {
        return I18n.translateToLocal("item.flowerPot.name");
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FLOWER_POT_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
        ItemStack itemstack = playerIn.getHeldItem(hand);
        TileEntityFlowerPot tileentityflowerpot = this.getTileEntity(worldIn, pos);
        if (tileentityflowerpot == null) {
            return false;
        }
        ItemStack itemstack1 = tileentityflowerpot.getFlowerItemStack();
        if (itemstack1.isEmpty()) {
            if (!this.func_190951_a(itemstack)) {
                return false;
            }
            tileentityflowerpot.func_190614_a(itemstack);
            playerIn.addStat(StatList.FLOWER_POTTED);
            if (!playerIn.capabilities.isCreativeMode) {
                itemstack.func_190918_g(1);
            }
        } else {
            if (itemstack.isEmpty()) {
                playerIn.setHeldItem(hand, itemstack1);
            } else if (!playerIn.func_191521_c(itemstack1)) {
                playerIn.dropItem(itemstack1, false);
            }
            tileentityflowerpot.func_190614_a(ItemStack.field_190927_a);
        }
        tileentityflowerpot.markDirty();
        worldIn.notifyBlockUpdate(pos, state, state, 3);
        return true;
    }

    private boolean func_190951_a(ItemStack p_190951_1_) {
        Block block = Block.getBlockFromItem(p_190951_1_.getItem());
        if (block != Blocks.YELLOW_FLOWER && block != Blocks.RED_FLOWER && block != Blocks.CACTUS && block != Blocks.BROWN_MUSHROOM && block != Blocks.RED_MUSHROOM && block != Blocks.SAPLING && block != Blocks.DEADBUSH) {
            int i = p_190951_1_.getMetadata();
            return block == Blocks.TALLGRASS && i == BlockTallGrass.EnumType.FERN.getMeta();
        }
        return true;
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        ItemStack itemstack;
        TileEntityFlowerPot tileentityflowerpot = this.getTileEntity(worldIn, pos);
        if (tileentityflowerpot != null && !(itemstack = tileentityflowerpot.getFlowerItemStack()).isEmpty()) {
            return itemstack;
        }
        return new ItemStack(Items.FLOWER_POT);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && worldIn.getBlockState(pos.down()).isFullyOpaque();
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
        if (!worldIn.getBlockState(pos.down()).isFullyOpaque()) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntityFlowerPot tileentityflowerpot = this.getTileEntity(worldIn, pos);
        if (tileentityflowerpot != null && tileentityflowerpot.getFlowerPotItem() != null) {
            BlockFlowerPot.spawnAsEntity(worldIn, pos, new ItemStack(tileentityflowerpot.getFlowerPotItem(), 1, tileentityflowerpot.getFlowerPotData()));
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        TileEntityFlowerPot tileentityflowerpot;
        super.onBlockHarvested(worldIn, pos, state, player);
        if (player.capabilities.isCreativeMode && (tileentityflowerpot = this.getTileEntity(worldIn, pos)) != null) {
            tileentityflowerpot.func_190614_a(ItemStack.field_190927_a);
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.FLOWER_POT;
    }

    @Nullable
    private TileEntityFlowerPot getTileEntity(World worldIn, BlockPos pos) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity instanceof TileEntityFlowerPot ? (TileEntityFlowerPot)tileentity : null;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
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
            }
        }
        return new TileEntityFlowerPot(Item.getItemFromBlock(block), i);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block)this, CONTENTS, LEGACY_DATA);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(LEGACY_DATA);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntityFlowerPot tileentityflowerpot;
        Item item;
        TileEntity tileentity;
        EnumFlowerType blockflowerpot$enumflowertype = EnumFlowerType.EMPTY;
        TileEntity tileEntity = tileentity = worldIn instanceof ChunkCache ? ((ChunkCache)worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityFlowerPot && (item = (tileentityflowerpot = (TileEntityFlowerPot)tileentity).getFlowerPotItem()) instanceof ItemBlock) {
            int i = tileentityflowerpot.getFlowerPotData();
            Block block = Block.getBlockFromItem(item);
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
            } else if (block == Blocks.TALLGRASS) {
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
            } else if (block == Blocks.YELLOW_FLOWER) {
                blockflowerpot$enumflowertype = EnumFlowerType.DANDELION;
            } else if (block == Blocks.RED_FLOWER) {
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
            } else if (block == Blocks.RED_MUSHROOM) {
                blockflowerpot$enumflowertype = EnumFlowerType.MUSHROOM_RED;
            } else if (block == Blocks.BROWN_MUSHROOM) {
                blockflowerpot$enumflowertype = EnumFlowerType.MUSHROOM_BROWN;
            } else if (block == Blocks.DEADBUSH) {
                blockflowerpot$enumflowertype = EnumFlowerType.DEAD_BUSH;
            } else if (block == Blocks.CACTUS) {
                blockflowerpot$enumflowertype = EnumFlowerType.CACTUS;
            }
        }
        return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
        return BlockFaceShape.UNDEFINED;
    }

    public static enum EnumFlowerType implements IStringSerializable
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

        private EnumFlowerType(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}

