/*
 * Decompiled with CFR 0.150.
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFlowerPot
extends BlockContainer {
    public static final PropertyInteger field_176444_a = PropertyInteger.create("legacy_data", 0, 15);
    public static final PropertyEnum field_176443_b = PropertyEnum.create("contents", EnumFlowerType.class);
    private static final String __OBFID = "CL_00000247";

    public BlockFlowerPot() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176443_b, (Comparable)((Object)EnumFlowerType.EMPTY)).withProperty(field_176444_a, Integer.valueOf(0)));
        this.setBlockBoundsForItemRender();
    }

    @Override
    public void setBlockBoundsForItemRender() {
        float var1 = 0.375f;
        float var2 = var1 / 2.0f;
        this.setBlockBounds(0.5f - var2, 0.0f, 0.5f - var2, 0.5f + var2, var1, 0.5f + var2);
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
        Item var5;
        TileEntity var4 = worldIn.getTileEntity(pos);
        if (var4 instanceof TileEntityFlowerPot && (var5 = ((TileEntityFlowerPot)var4).getFlowerPotItem()) instanceof ItemBlock) {
            return Block.getBlockFromItem(var5).colorMultiplier(worldIn, pos, renderPass);
        }
        return 0xFFFFFF;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack var9 = playerIn.inventory.getCurrentItem();
        if (var9 != null && var9.getItem() instanceof ItemBlock) {
            TileEntityFlowerPot var10 = this.func_176442_d(worldIn, pos);
            if (var10 == null) {
                return false;
            }
            if (var10.getFlowerPotItem() != null) {
                return false;
            }
            Block var11 = Block.getBlockFromItem(var9.getItem());
            if (!this.func_149928_a(var11, var9.getMetadata())) {
                return false;
            }
            var10.func_145964_a(var9.getItem(), var9.getMetadata());
            var10.markDirty();
            worldIn.markBlockForUpdate(pos);
            if (!playerIn.capabilities.isCreativeMode && --var9.stackSize <= 0) {
                playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
            }
            return true;
        }
        return false;
    }

    private boolean func_149928_a(Block p_149928_1_, int p_149928_2_) {
        return p_149928_1_ != Blocks.yellow_flower && p_149928_1_ != Blocks.red_flower && p_149928_1_ != Blocks.cactus && p_149928_1_ != Blocks.brown_mushroom && p_149928_1_ != Blocks.red_mushroom && p_149928_1_ != Blocks.sapling && p_149928_1_ != Blocks.deadbush ? p_149928_1_ == Blocks.tallgrass && p_149928_2_ == BlockTallGrass.EnumType.FERN.func_177044_a() : true;
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        TileEntityFlowerPot var3 = this.func_176442_d(worldIn, pos);
        return var3 != null && var3.getFlowerPotItem() != null ? var3.getFlowerPotItem() : Items.flower_pot;
    }

    @Override
    public int getDamageValue(World worldIn, BlockPos pos) {
        TileEntityFlowerPot var3 = this.func_176442_d(worldIn, pos);
        return var3 != null && var3.getFlowerPotItem() != null ? var3.getFlowerPotData() : 0;
    }

    @Override
    public boolean isFlowerPot() {
        return true;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown());
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown())) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntityFlowerPot var4 = this.func_176442_d(worldIn, pos);
        if (var4 != null && var4.getFlowerPotItem() != null) {
            BlockFlowerPot.spawnAsEntity(worldIn, pos, new ItemStack(var4.getFlowerPotItem(), 1, var4.getFlowerPotData()));
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
        TileEntityFlowerPot var5;
        super.onBlockHarvested(worldIn, pos, state, playerIn);
        if (playerIn.capabilities.isCreativeMode && (var5 = this.func_176442_d(worldIn, pos)) != null) {
            var5.func_145964_a(null, 0);
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.flower_pot;
    }

    private TileEntityFlowerPot func_176442_d(World worldIn, BlockPos p_176442_2_) {
        TileEntity var3 = worldIn.getTileEntity(p_176442_2_);
        return var3 instanceof TileEntityFlowerPot ? (TileEntityFlowerPot)var3 : null;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        Block var3 = null;
        int var4 = 0;
        switch (meta) {
            case 1: {
                var3 = Blocks.red_flower;
                var4 = BlockFlower.EnumFlowerType.POPPY.func_176968_b();
                break;
            }
            case 2: {
                var3 = Blocks.yellow_flower;
                break;
            }
            case 3: {
                var3 = Blocks.sapling;
                var4 = BlockPlanks.EnumType.OAK.func_176839_a();
                break;
            }
            case 4: {
                var3 = Blocks.sapling;
                var4 = BlockPlanks.EnumType.SPRUCE.func_176839_a();
                break;
            }
            case 5: {
                var3 = Blocks.sapling;
                var4 = BlockPlanks.EnumType.BIRCH.func_176839_a();
                break;
            }
            case 6: {
                var3 = Blocks.sapling;
                var4 = BlockPlanks.EnumType.JUNGLE.func_176839_a();
                break;
            }
            case 7: {
                var3 = Blocks.red_mushroom;
                break;
            }
            case 8: {
                var3 = Blocks.brown_mushroom;
                break;
            }
            case 9: {
                var3 = Blocks.cactus;
                break;
            }
            case 10: {
                var3 = Blocks.deadbush;
                break;
            }
            case 11: {
                var3 = Blocks.tallgrass;
                var4 = BlockTallGrass.EnumType.FERN.func_177044_a();
                break;
            }
            case 12: {
                var3 = Blocks.sapling;
                var4 = BlockPlanks.EnumType.ACACIA.func_176839_a();
                break;
            }
            case 13: {
                var3 = Blocks.sapling;
                var4 = BlockPlanks.EnumType.DARK_OAK.func_176839_a();
            }
        }
        return new TileEntityFlowerPot(Item.getItemFromBlock(var3), var4);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, field_176443_b, field_176444_a);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (Integer)state.getValue(field_176444_a);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntityFlowerPot var6;
        Item var7;
        EnumFlowerType var4 = EnumFlowerType.EMPTY;
        TileEntity var5 = worldIn.getTileEntity(pos);
        if (var5 instanceof TileEntityFlowerPot && (var7 = (var6 = (TileEntityFlowerPot)var5).getFlowerPotItem()) instanceof ItemBlock) {
            int var8 = var6.getFlowerPotData();
            Block var9 = Block.getBlockFromItem(var7);
            if (var9 == Blocks.sapling) {
                switch (BlockPlanks.EnumType.func_176837_a(var8)) {
                    case OAK: {
                        var4 = EnumFlowerType.OAK_SAPLING;
                        break;
                    }
                    case SPRUCE: {
                        var4 = EnumFlowerType.SPRUCE_SAPLING;
                        break;
                    }
                    case BIRCH: {
                        var4 = EnumFlowerType.BIRCH_SAPLING;
                        break;
                    }
                    case JUNGLE: {
                        var4 = EnumFlowerType.JUNGLE_SAPLING;
                        break;
                    }
                    case ACACIA: {
                        var4 = EnumFlowerType.ACACIA_SAPLING;
                        break;
                    }
                    case DARK_OAK: {
                        var4 = EnumFlowerType.DARK_OAK_SAPLING;
                        break;
                    }
                    default: {
                        var4 = EnumFlowerType.EMPTY;
                        break;
                    }
                }
            } else if (var9 == Blocks.tallgrass) {
                switch (var8) {
                    case 0: {
                        var4 = EnumFlowerType.DEAD_BUSH;
                        break;
                    }
                    case 2: {
                        var4 = EnumFlowerType.FERN;
                        break;
                    }
                    default: {
                        var4 = EnumFlowerType.EMPTY;
                        break;
                    }
                }
            } else if (var9 == Blocks.yellow_flower) {
                var4 = EnumFlowerType.DANDELION;
            } else if (var9 == Blocks.red_flower) {
                switch (BlockFlower.EnumFlowerType.func_176967_a(BlockFlower.EnumFlowerColor.RED, var8)) {
                    case POPPY: {
                        var4 = EnumFlowerType.POPPY;
                        break;
                    }
                    case BLUE_ORCHID: {
                        var4 = EnumFlowerType.BLUE_ORCHID;
                        break;
                    }
                    case ALLIUM: {
                        var4 = EnumFlowerType.ALLIUM;
                        break;
                    }
                    case HOUSTONIA: {
                        var4 = EnumFlowerType.HOUSTONIA;
                        break;
                    }
                    case RED_TULIP: {
                        var4 = EnumFlowerType.RED_TULIP;
                        break;
                    }
                    case ORANGE_TULIP: {
                        var4 = EnumFlowerType.ORANGE_TULIP;
                        break;
                    }
                    case WHITE_TULIP: {
                        var4 = EnumFlowerType.WHITE_TULIP;
                        break;
                    }
                    case PINK_TULIP: {
                        var4 = EnumFlowerType.PINK_TULIP;
                        break;
                    }
                    case OXEYE_DAISY: {
                        var4 = EnumFlowerType.OXEYE_DAISY;
                        break;
                    }
                    default: {
                        var4 = EnumFlowerType.EMPTY;
                        break;
                    }
                }
            } else if (var9 == Blocks.red_mushroom) {
                var4 = EnumFlowerType.MUSHROOM_RED;
            } else if (var9 == Blocks.brown_mushroom) {
                var4 = EnumFlowerType.MUSHROOM_BROWN;
            } else if (var9 == Blocks.deadbush) {
                var4 = EnumFlowerType.DEAD_BUSH;
            } else if (var9 == Blocks.cactus) {
                var4 = EnumFlowerType.CACTUS;
            }
        }
        return state.withProperty(field_176443_b, (Comparable)((Object)var4));
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    public static enum EnumFlowerType implements IStringSerializable
    {
        EMPTY("EMPTY", 0, "empty"),
        POPPY("POPPY", 1, "rose"),
        BLUE_ORCHID("BLUE_ORCHID", 2, "blue_orchid"),
        ALLIUM("ALLIUM", 3, "allium"),
        HOUSTONIA("HOUSTONIA", 4, "houstonia"),
        RED_TULIP("RED_TULIP", 5, "red_tulip"),
        ORANGE_TULIP("ORANGE_TULIP", 6, "orange_tulip"),
        WHITE_TULIP("WHITE_TULIP", 7, "white_tulip"),
        PINK_TULIP("PINK_TULIP", 8, "pink_tulip"),
        OXEYE_DAISY("OXEYE_DAISY", 9, "oxeye_daisy"),
        DANDELION("DANDELION", 10, "dandelion"),
        OAK_SAPLING("OAK_SAPLING", 11, "oak_sapling"),
        SPRUCE_SAPLING("SPRUCE_SAPLING", 12, "spruce_sapling"),
        BIRCH_SAPLING("BIRCH_SAPLING", 13, "birch_sapling"),
        JUNGLE_SAPLING("JUNGLE_SAPLING", 14, "jungle_sapling"),
        ACACIA_SAPLING("ACACIA_SAPLING", 15, "acacia_sapling"),
        DARK_OAK_SAPLING("DARK_OAK_SAPLING", 16, "dark_oak_sapling"),
        MUSHROOM_RED("MUSHROOM_RED", 17, "mushroom_red"),
        MUSHROOM_BROWN("MUSHROOM_BROWN", 18, "mushroom_brown"),
        DEAD_BUSH("DEAD_BUSH", 19, "dead_bush"),
        FERN("FERN", 20, "fern"),
        CACTUS("CACTUS", 21, "cactus");

        private final String field_177006_w;
        private static final EnumFlowerType[] $VALUES;
        private static final String __OBFID = "CL_00002115";

        static {
            $VALUES = new EnumFlowerType[]{EMPTY, POPPY, BLUE_ORCHID, ALLIUM, HOUSTONIA, RED_TULIP, ORANGE_TULIP, WHITE_TULIP, PINK_TULIP, OXEYE_DAISY, DANDELION, OAK_SAPLING, SPRUCE_SAPLING, BIRCH_SAPLING, JUNGLE_SAPLING, ACACIA_SAPLING, DARK_OAK_SAPLING, MUSHROOM_RED, MUSHROOM_BROWN, DEAD_BUSH, FERN, CACTUS};
        }

        private EnumFlowerType(String p_i45715_1_, int p_i45715_2_, String p_i45715_3_) {
            this.field_177006_w = p_i45715_3_;
        }

        public String toString() {
            return this.field_177006_w;
        }

        @Override
        public String getName() {
            return this.field_177006_w;
        }
    }
}

