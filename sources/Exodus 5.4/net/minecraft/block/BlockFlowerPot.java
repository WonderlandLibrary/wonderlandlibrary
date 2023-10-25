/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
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
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFlowerPot
extends BlockContainer {
    public static final PropertyEnum<EnumFlowerType> CONTENTS;
    public static final PropertyInteger LEGACY_DATA;

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return iBlockState.getValue(LEGACY_DATA);
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState iBlockState) {
        TileEntityFlowerPot tileEntityFlowerPot = this.getTileEntity(world, blockPos);
        if (tileEntityFlowerPot != null && tileEntityFlowerPot.getFlowerPotItem() != null) {
            BlockFlowerPot.spawnAsEntity(world, blockPos, new ItemStack(tileEntityFlowerPot.getFlowerPotItem(), 1, tileEntityFlowerPot.getFlowerPotData()));
        }
        super.breakBlock(world, blockPos, iBlockState);
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        if (!World.doesBlockHaveSolidTopSurface(world, blockPos.down())) {
            this.dropBlockAsItem(world, blockPos, iBlockState, 0);
            world.setBlockToAir(blockPos);
        }
    }

    @Override
    public void onBlockHarvested(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer) {
        TileEntityFlowerPot tileEntityFlowerPot;
        super.onBlockHarvested(world, blockPos, iBlockState, entityPlayer);
        if (entityPlayer.capabilities.isCreativeMode && (tileEntityFlowerPot = this.getTileEntity(world, blockPos)) != null) {
            tileEntityFlowerPot.setFlowerPotData(null, 0);
        }
    }

    public BlockFlowerPot() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(CONTENTS, EnumFlowerType.EMPTY).withProperty(LEGACY_DATA, 0));
        this.setBlockBoundsForItemRender();
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public Item getItem(World world, BlockPos blockPos) {
        TileEntityFlowerPot tileEntityFlowerPot = this.getTileEntity(world, blockPos);
        return tileEntityFlowerPot != null && tileEntityFlowerPot.getFlowerPotItem() != null ? tileEntityFlowerPot.getFlowerPotItem() : Items.flower_pot;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    static {
        LEGACY_DATA = PropertyInteger.create("legacy_data", 0, 15);
        CONTENTS = PropertyEnum.create("contents", EnumFlowerType.class);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumFacing enumFacing, float f, float f2, float f3) {
        ItemStack itemStack = entityPlayer.inventory.getCurrentItem();
        if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
            TileEntityFlowerPot tileEntityFlowerPot = this.getTileEntity(world, blockPos);
            if (tileEntityFlowerPot == null) {
                return false;
            }
            if (tileEntityFlowerPot.getFlowerPotItem() != null) {
                return false;
            }
            Block block = Block.getBlockFromItem(itemStack.getItem());
            if (!this.canNotContain(block, itemStack.getMetadata())) {
                return false;
            }
            tileEntityFlowerPot.setFlowerPotData(itemStack.getItem(), itemStack.getMetadata());
            tileEntityFlowerPot.markDirty();
            world.markBlockForUpdate(blockPos);
            entityPlayer.triggerAchievement(StatList.field_181736_T);
            if (!entityPlayer.capabilities.isCreativeMode && --itemStack.stackSize <= 0) {
                entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, null);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos blockPos) {
        return super.canPlaceBlockAt(world, blockPos) && World.doesBlockHaveSolidTopSurface(world, blockPos.down());
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, CONTENTS, LEGACY_DATA);
    }

    @Override
    public int getDamageValue(World world, BlockPos blockPos) {
        TileEntityFlowerPot tileEntityFlowerPot = this.getTileEntity(world, blockPos);
        return tileEntityFlowerPot != null && tileEntityFlowerPot.getFlowerPotItem() != null ? tileEntityFlowerPot.getFlowerPotData() : 0;
    }

    @Override
    public int colorMultiplier(IBlockAccess iBlockAccess, BlockPos blockPos, int n) {
        Item item;
        TileEntity tileEntity = iBlockAccess.getTileEntity(blockPos);
        if (tileEntity instanceof TileEntityFlowerPot && (item = ((TileEntityFlowerPot)tileEntity).getFlowerPotItem()) instanceof ItemBlock) {
            return Block.getBlockFromItem(item).colorMultiplier(iBlockAccess, blockPos, n);
        }
        return 0xFFFFFF;
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return Items.flower_pot;
    }

    @Override
    public void setBlockBoundsForItemRender() {
        float f = 0.375f;
        float f2 = f / 2.0f;
        this.setBlockBounds(0.5f - f2, 0.0f, 0.5f - f2, 0.5f + f2, f, 0.5f + f2);
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int n) {
        Block block = null;
        int n2 = 0;
        switch (n) {
            case 1: {
                block = Blocks.red_flower;
                n2 = BlockFlower.EnumFlowerType.POPPY.getMeta();
                break;
            }
            case 2: {
                block = Blocks.yellow_flower;
                break;
            }
            case 3: {
                block = Blocks.sapling;
                n2 = BlockPlanks.EnumType.OAK.getMetadata();
                break;
            }
            case 4: {
                block = Blocks.sapling;
                n2 = BlockPlanks.EnumType.SPRUCE.getMetadata();
                break;
            }
            case 5: {
                block = Blocks.sapling;
                n2 = BlockPlanks.EnumType.BIRCH.getMetadata();
                break;
            }
            case 6: {
                block = Blocks.sapling;
                n2 = BlockPlanks.EnumType.JUNGLE.getMetadata();
                break;
            }
            case 7: {
                block = Blocks.red_mushroom;
                break;
            }
            case 8: {
                block = Blocks.brown_mushroom;
                break;
            }
            case 9: {
                block = Blocks.cactus;
                break;
            }
            case 10: {
                block = Blocks.deadbush;
                break;
            }
            case 11: {
                block = Blocks.tallgrass;
                n2 = BlockTallGrass.EnumType.FERN.getMeta();
                break;
            }
            case 12: {
                block = Blocks.sapling;
                n2 = BlockPlanks.EnumType.ACACIA.getMetadata();
                break;
            }
            case 13: {
                block = Blocks.sapling;
                n2 = BlockPlanks.EnumType.DARK_OAK.getMetadata();
            }
        }
        return new TileEntityFlowerPot(Item.getItemFromBlock(block), n2);
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("item.flowerPot.name");
    }

    @Override
    public boolean isFlowerPot() {
        return true;
    }

    private TileEntityFlowerPot getTileEntity(World world, BlockPos blockPos) {
        TileEntity tileEntity = world.getTileEntity(blockPos);
        return tileEntity instanceof TileEntityFlowerPot ? (TileEntityFlowerPot)tileEntity : null;
    }

    private boolean canNotContain(Block block, int n) {
        return block != Blocks.yellow_flower && block != Blocks.red_flower && block != Blocks.cactus && block != Blocks.brown_mushroom && block != Blocks.red_mushroom && block != Blocks.sapling && block != Blocks.deadbush ? block == Blocks.tallgrass && n == BlockTallGrass.EnumType.FERN.getMeta() : true;
    }

    @Override
    public IBlockState getActualState(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockPos blockPos) {
        TileEntityFlowerPot tileEntityFlowerPot;
        Item item;
        EnumFlowerType enumFlowerType = EnumFlowerType.EMPTY;
        TileEntity tileEntity = iBlockAccess.getTileEntity(blockPos);
        if (tileEntity instanceof TileEntityFlowerPot && (item = (tileEntityFlowerPot = (TileEntityFlowerPot)tileEntity).getFlowerPotItem()) instanceof ItemBlock) {
            int n = tileEntityFlowerPot.getFlowerPotData();
            Block block = Block.getBlockFromItem(item);
            if (block == Blocks.sapling) {
                switch (BlockPlanks.EnumType.byMetadata(n)) {
                    case OAK: {
                        enumFlowerType = EnumFlowerType.OAK_SAPLING;
                        break;
                    }
                    case SPRUCE: {
                        enumFlowerType = EnumFlowerType.SPRUCE_SAPLING;
                        break;
                    }
                    case BIRCH: {
                        enumFlowerType = EnumFlowerType.BIRCH_SAPLING;
                        break;
                    }
                    case JUNGLE: {
                        enumFlowerType = EnumFlowerType.JUNGLE_SAPLING;
                        break;
                    }
                    case ACACIA: {
                        enumFlowerType = EnumFlowerType.ACACIA_SAPLING;
                        break;
                    }
                    case DARK_OAK: {
                        enumFlowerType = EnumFlowerType.DARK_OAK_SAPLING;
                        break;
                    }
                    default: {
                        enumFlowerType = EnumFlowerType.EMPTY;
                        break;
                    }
                }
            } else if (block == Blocks.tallgrass) {
                switch (n) {
                    case 0: {
                        enumFlowerType = EnumFlowerType.DEAD_BUSH;
                        break;
                    }
                    case 2: {
                        enumFlowerType = EnumFlowerType.FERN;
                        break;
                    }
                    default: {
                        enumFlowerType = EnumFlowerType.EMPTY;
                        break;
                    }
                }
            } else if (block == Blocks.yellow_flower) {
                enumFlowerType = EnumFlowerType.DANDELION;
            } else if (block == Blocks.red_flower) {
                switch (BlockFlower.EnumFlowerType.getType(BlockFlower.EnumFlowerColor.RED, n)) {
                    case POPPY: {
                        enumFlowerType = EnumFlowerType.POPPY;
                        break;
                    }
                    case BLUE_ORCHID: {
                        enumFlowerType = EnumFlowerType.BLUE_ORCHID;
                        break;
                    }
                    case ALLIUM: {
                        enumFlowerType = EnumFlowerType.ALLIUM;
                        break;
                    }
                    case HOUSTONIA: {
                        enumFlowerType = EnumFlowerType.HOUSTONIA;
                        break;
                    }
                    case RED_TULIP: {
                        enumFlowerType = EnumFlowerType.RED_TULIP;
                        break;
                    }
                    case ORANGE_TULIP: {
                        enumFlowerType = EnumFlowerType.ORANGE_TULIP;
                        break;
                    }
                    case WHITE_TULIP: {
                        enumFlowerType = EnumFlowerType.WHITE_TULIP;
                        break;
                    }
                    case PINK_TULIP: {
                        enumFlowerType = EnumFlowerType.PINK_TULIP;
                        break;
                    }
                    case OXEYE_DAISY: {
                        enumFlowerType = EnumFlowerType.OXEYE_DAISY;
                        break;
                    }
                    default: {
                        enumFlowerType = EnumFlowerType.EMPTY;
                        break;
                    }
                }
            } else if (block == Blocks.red_mushroom) {
                enumFlowerType = EnumFlowerType.MUSHROOM_RED;
            } else if (block == Blocks.brown_mushroom) {
                enumFlowerType = EnumFlowerType.MUSHROOM_BROWN;
            } else if (block == Blocks.deadbush) {
                enumFlowerType = EnumFlowerType.DEAD_BUSH;
            } else if (block == Blocks.cactus) {
                enumFlowerType = EnumFlowerType.CACTUS;
            }
        }
        return iBlockState.withProperty(CONTENTS, enumFlowerType);
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

        @Override
        public String getName() {
            return this.name;
        }

        public String toString() {
            return this.name;
        }

        private EnumFlowerType(String string2) {
            this.name = string2;
        }
    }
}

