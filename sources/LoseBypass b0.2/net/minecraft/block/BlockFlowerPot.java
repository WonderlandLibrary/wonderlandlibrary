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
    public static final PropertyInteger LEGACY_DATA = PropertyInteger.create("legacy_data", 0, 15);
    public static final PropertyEnum<EnumFlowerType> CONTENTS = PropertyEnum.create("contents", EnumFlowerType.class);

    public BlockFlowerPot() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(CONTENTS, EnumFlowerType.EMPTY).withProperty(LEGACY_DATA, 0));
        this.setBlockBoundsForItemRender();
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("item.flowerPot.name");
    }

    @Override
    public void setBlockBoundsForItemRender() {
        float f = 0.375f;
        float f1 = f / 2.0f;
        this.setBlockBounds(0.5f - f1, 0.0f, 0.5f - f1, 0.5f + f1, f, 0.5f + f1);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (!(tileentity instanceof TileEntityFlowerPot)) return 0xFFFFFF;
        Item item = ((TileEntityFlowerPot)tileentity).getFlowerPotItem();
        if (!(item instanceof ItemBlock)) return 0xFFFFFF;
        return Block.getBlockFromItem(item).colorMultiplier(worldIn, pos, renderPass);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = playerIn.inventory.getCurrentItem();
        if (itemstack == null) return false;
        if (!(itemstack.getItem() instanceof ItemBlock)) return false;
        TileEntityFlowerPot tileentityflowerpot = this.getTileEntity(worldIn, pos);
        if (tileentityflowerpot == null) {
            return false;
        }
        if (tileentityflowerpot.getFlowerPotItem() != null) {
            return false;
        }
        Block block = Block.getBlockFromItem(itemstack.getItem());
        if (!this.canNotContain(block, itemstack.getMetadata())) {
            return false;
        }
        tileentityflowerpot.setFlowerPotData(itemstack.getItem(), itemstack.getMetadata());
        tileentityflowerpot.markDirty();
        worldIn.markBlockForUpdate(pos);
        playerIn.triggerAchievement(StatList.field_181736_T);
        if (playerIn.capabilities.isCreativeMode) return true;
        if (--itemstack.stackSize > 0) return true;
        playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
        return true;
    }

    private boolean canNotContain(Block blockIn, int meta) {
        if (blockIn == Blocks.yellow_flower) return true;
        if (blockIn == Blocks.red_flower) return true;
        if (blockIn == Blocks.cactus) return true;
        if (blockIn == Blocks.brown_mushroom) return true;
        if (blockIn == Blocks.red_mushroom) return true;
        if (blockIn == Blocks.sapling) return true;
        if (blockIn == Blocks.deadbush) return true;
        if (blockIn != Blocks.tallgrass) return false;
        if (meta != BlockTallGrass.EnumType.FERN.getMeta()) return false;
        return true;
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        Item item;
        TileEntityFlowerPot tileentityflowerpot = this.getTileEntity(worldIn, pos);
        if (tileentityflowerpot != null && tileentityflowerpot.getFlowerPotItem() != null) {
            item = tileentityflowerpot.getFlowerPotItem();
            return item;
        }
        item = Items.flower_pot;
        return item;
    }

    @Override
    public int getDamageValue(World worldIn, BlockPos pos) {
        TileEntityFlowerPot tileentityflowerpot = this.getTileEntity(worldIn, pos);
        if (tileentityflowerpot == null) return 0;
        if (tileentityflowerpot.getFlowerPotItem() == null) return 0;
        int n = tileentityflowerpot.getFlowerPotData();
        return n;
    }

    @Override
    public boolean isFlowerPot() {
        return true;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        if (!super.canPlaceBlockAt(worldIn, pos)) return false;
        if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down())) return false;
        return true;
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (World.doesBlockHaveSolidTopSurface(worldIn, pos.down())) return;
        this.dropBlockAsItem(worldIn, pos, state, 0);
        worldIn.setBlockToAir(pos);
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
        super.onBlockHarvested(worldIn, pos, state, player);
        if (!player.capabilities.isCreativeMode) return;
        TileEntityFlowerPot tileentityflowerpot = this.getTileEntity(worldIn, pos);
        if (tileentityflowerpot == null) return;
        tileentityflowerpot.setFlowerPotData(null, 0);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.flower_pot;
    }

    private TileEntityFlowerPot getTileEntity(World worldIn, BlockPos pos) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (!(tileentity instanceof TileEntityFlowerPot)) return null;
        TileEntityFlowerPot tileEntityFlowerPot = (TileEntityFlowerPot)tileentity;
        return tileEntityFlowerPot;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        Block block = null;
        int i = 0;
        switch (meta) {
            case 1: {
                block = Blocks.red_flower;
                i = BlockFlower.EnumFlowerType.POPPY.getMeta();
                return new TileEntityFlowerPot(Item.getItemFromBlock(block), i);
            }
            case 2: {
                block = Blocks.yellow_flower;
                return new TileEntityFlowerPot(Item.getItemFromBlock(block), i);
            }
            case 3: {
                block = Blocks.sapling;
                i = BlockPlanks.EnumType.OAK.getMetadata();
                return new TileEntityFlowerPot(Item.getItemFromBlock(block), i);
            }
            case 4: {
                block = Blocks.sapling;
                i = BlockPlanks.EnumType.SPRUCE.getMetadata();
                return new TileEntityFlowerPot(Item.getItemFromBlock(block), i);
            }
            case 5: {
                block = Blocks.sapling;
                i = BlockPlanks.EnumType.BIRCH.getMetadata();
                return new TileEntityFlowerPot(Item.getItemFromBlock(block), i);
            }
            case 6: {
                block = Blocks.sapling;
                i = BlockPlanks.EnumType.JUNGLE.getMetadata();
                return new TileEntityFlowerPot(Item.getItemFromBlock(block), i);
            }
            case 7: {
                block = Blocks.red_mushroom;
                return new TileEntityFlowerPot(Item.getItemFromBlock(block), i);
            }
            case 8: {
                block = Blocks.brown_mushroom;
                return new TileEntityFlowerPot(Item.getItemFromBlock(block), i);
            }
            case 9: {
                block = Blocks.cactus;
                return new TileEntityFlowerPot(Item.getItemFromBlock(block), i);
            }
            case 10: {
                block = Blocks.deadbush;
                return new TileEntityFlowerPot(Item.getItemFromBlock(block), i);
            }
            case 11: {
                block = Blocks.tallgrass;
                i = BlockTallGrass.EnumType.FERN.getMeta();
                return new TileEntityFlowerPot(Item.getItemFromBlock(block), i);
            }
            case 12: {
                block = Blocks.sapling;
                i = BlockPlanks.EnumType.ACACIA.getMetadata();
                return new TileEntityFlowerPot(Item.getItemFromBlock(block), i);
            }
            case 13: {
                block = Blocks.sapling;
                i = BlockPlanks.EnumType.DARK_OAK.getMetadata();
                return new TileEntityFlowerPot(Item.getItemFromBlock(block), i);
            }
        }
        return new TileEntityFlowerPot(Item.getItemFromBlock(block), i);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, CONTENTS, LEGACY_DATA);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(LEGACY_DATA);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        EnumFlowerType blockflowerpot$enumflowertype = EnumFlowerType.EMPTY;
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (!(tileentity instanceof TileEntityFlowerPot)) return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
        TileEntityFlowerPot tileentityflowerpot = (TileEntityFlowerPot)tileentity;
        Item item = tileentityflowerpot.getFlowerPotItem();
        if (!(item instanceof ItemBlock)) return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
        int i = tileentityflowerpot.getFlowerPotData();
        Block block = Block.getBlockFromItem(item);
        if (block == Blocks.sapling) {
            switch (1.$SwitchMap$net$minecraft$block$BlockPlanks$EnumType[BlockPlanks.EnumType.byMetadata(i).ordinal()]) {
                case 1: {
                    blockflowerpot$enumflowertype = EnumFlowerType.OAK_SAPLING;
                    return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
                }
                case 2: {
                    blockflowerpot$enumflowertype = EnumFlowerType.SPRUCE_SAPLING;
                    return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
                }
                case 3: {
                    blockflowerpot$enumflowertype = EnumFlowerType.BIRCH_SAPLING;
                    return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
                }
                case 4: {
                    blockflowerpot$enumflowertype = EnumFlowerType.JUNGLE_SAPLING;
                    return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
                }
                case 5: {
                    blockflowerpot$enumflowertype = EnumFlowerType.ACACIA_SAPLING;
                    return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
                }
                case 6: {
                    blockflowerpot$enumflowertype = EnumFlowerType.DARK_OAK_SAPLING;
                    return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
                }
            }
            blockflowerpot$enumflowertype = EnumFlowerType.EMPTY;
            return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
        }
        if (block == Blocks.tallgrass) {
            switch (i) {
                case 0: {
                    blockflowerpot$enumflowertype = EnumFlowerType.DEAD_BUSH;
                    return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
                }
                case 2: {
                    blockflowerpot$enumflowertype = EnumFlowerType.FERN;
                    return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
                }
            }
            blockflowerpot$enumflowertype = EnumFlowerType.EMPTY;
            return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
        }
        if (block == Blocks.yellow_flower) {
            blockflowerpot$enumflowertype = EnumFlowerType.DANDELION;
            return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
        }
        if (block == Blocks.red_flower) {
            switch (BlockFlower.EnumFlowerType.getType(BlockFlower.EnumFlowerColor.RED, i)) {
                case POPPY: {
                    blockflowerpot$enumflowertype = EnumFlowerType.POPPY;
                    return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
                }
                case BLUE_ORCHID: {
                    blockflowerpot$enumflowertype = EnumFlowerType.BLUE_ORCHID;
                    return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
                }
                case ALLIUM: {
                    blockflowerpot$enumflowertype = EnumFlowerType.ALLIUM;
                    return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
                }
                case HOUSTONIA: {
                    blockflowerpot$enumflowertype = EnumFlowerType.HOUSTONIA;
                    return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
                }
                case RED_TULIP: {
                    blockflowerpot$enumflowertype = EnumFlowerType.RED_TULIP;
                    return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
                }
                case ORANGE_TULIP: {
                    blockflowerpot$enumflowertype = EnumFlowerType.ORANGE_TULIP;
                    return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
                }
                case WHITE_TULIP: {
                    blockflowerpot$enumflowertype = EnumFlowerType.WHITE_TULIP;
                    return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
                }
                case PINK_TULIP: {
                    blockflowerpot$enumflowertype = EnumFlowerType.PINK_TULIP;
                    return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
                }
                case OXEYE_DAISY: {
                    blockflowerpot$enumflowertype = EnumFlowerType.OXEYE_DAISY;
                    return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
                }
            }
            blockflowerpot$enumflowertype = EnumFlowerType.EMPTY;
            return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
        }
        if (block == Blocks.red_mushroom) {
            blockflowerpot$enumflowertype = EnumFlowerType.MUSHROOM_RED;
            return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
        }
        if (block == Blocks.brown_mushroom) {
            blockflowerpot$enumflowertype = EnumFlowerType.MUSHROOM_BROWN;
            return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
        }
        if (block == Blocks.deadbush) {
            blockflowerpot$enumflowertype = EnumFlowerType.DEAD_BUSH;
            return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
        }
        if (block != Blocks.cactus) return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
        blockflowerpot$enumflowertype = EnumFlowerType.CACTUS;
        return state.withProperty(CONTENTS, blockflowerpot$enumflowertype);
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
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

