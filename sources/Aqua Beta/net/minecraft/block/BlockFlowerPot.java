package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
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
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFlowerPot
extends BlockContainer {
    public static final PropertyInteger LEGACY_DATA = PropertyInteger.create((String)"legacy_data", (int)0, (int)15);
    public static final PropertyEnum<EnumFlowerType> CONTENTS = PropertyEnum.create((String)"contents", EnumFlowerType.class);

    public BlockFlowerPot() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(CONTENTS, (Comparable)EnumFlowerType.EMPTY).withProperty((IProperty)LEGACY_DATA, (Comparable)Integer.valueOf((int)0)));
        this.setBlockBoundsForItemRender();
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal((String)"item.flowerPot.name");
    }

    public void setBlockBoundsForItemRender() {
        float f = 0.375f;
        float f1 = f / 2.0f;
        this.setBlockBounds(0.5f - f1, 0.0f, 0.5f - f1, 0.5f + f1, f, 0.5f + f1);
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public int getRenderType() {
        return 3;
    }

    public boolean isFullCube() {
        return false;
    }

    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        Item item;
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityFlowerPot && (item = ((TileEntityFlowerPot)tileentity).getFlowerPotItem()) instanceof ItemBlock) {
            return Block.getBlockFromItem((Item)item).colorMultiplier(worldIn, pos, renderPass);
        }
        return 0xFFFFFF;
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = playerIn.inventory.getCurrentItem();
        if (itemstack != null && itemstack.getItem() instanceof ItemBlock) {
            TileEntityFlowerPot tileentityflowerpot = this.getTileEntity(worldIn, pos);
            if (tileentityflowerpot == null) {
                return false;
            }
            if (tileentityflowerpot.getFlowerPotItem() != null) {
                return false;
            }
            Block block = Block.getBlockFromItem((Item)itemstack.getItem());
            if (!this.canNotContain(block, itemstack.getMetadata())) {
                return false;
            }
            tileentityflowerpot.setFlowerPotData(itemstack.getItem(), itemstack.getMetadata());
            tileentityflowerpot.markDirty();
            worldIn.markBlockForUpdate(pos);
            playerIn.triggerAchievement(StatList.field_181736_T);
            if (!playerIn.capabilities.isCreativeMode && --itemstack.stackSize <= 0) {
                playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, (ItemStack)null);
            }
            return true;
        }
        return false;
    }

    private boolean canNotContain(Block blockIn, int meta) {
        return blockIn != Blocks.yellow_flower && blockIn != Blocks.red_flower && blockIn != Blocks.cactus && blockIn != Blocks.brown_mushroom && blockIn != Blocks.red_mushroom && blockIn != Blocks.sapling && blockIn != Blocks.deadbush ? blockIn == Blocks.tallgrass && meta == BlockTallGrass.EnumType.FERN.getMeta() : true;
    }

    public Item getItem(World worldIn, BlockPos pos) {
        TileEntityFlowerPot tileentityflowerpot = this.getTileEntity(worldIn, pos);
        return tileentityflowerpot != null && tileentityflowerpot.getFlowerPotItem() != null ? tileentityflowerpot.getFlowerPotItem() : Items.flower_pot;
    }

    public int getDamageValue(World worldIn, BlockPos pos) {
        TileEntityFlowerPot tileentityflowerpot = this.getTileEntity(worldIn, pos);
        return tileentityflowerpot != null && tileentityflowerpot.getFlowerPotItem() != null ? tileentityflowerpot.getFlowerPotData() : 0;
    }

    public boolean isFlowerPot() {
        return true;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, (BlockPos)pos.down());
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, (BlockPos)pos.down())) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntityFlowerPot tileentityflowerpot = this.getTileEntity(worldIn, pos);
        if (tileentityflowerpot != null && tileentityflowerpot.getFlowerPotItem() != null) {
            BlockFlowerPot.spawnAsEntity((World)worldIn, (BlockPos)pos, (ItemStack)new ItemStack(tileentityflowerpot.getFlowerPotItem(), 1, tileentityflowerpot.getFlowerPotData()));
        }
        super.breakBlock(worldIn, pos, state);
    }

    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        TileEntityFlowerPot tileentityflowerpot;
        super.onBlockHarvested(worldIn, pos, state, player);
        if (player.capabilities.isCreativeMode && (tileentityflowerpot = this.getTileEntity(worldIn, pos)) != null) {
            tileentityflowerpot.setFlowerPotData((Item)null, 0);
        }
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.flower_pot;
    }

    private TileEntityFlowerPot getTileEntity(World worldIn, BlockPos pos) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity instanceof TileEntityFlowerPot ? (TileEntityFlowerPot)tileentity : null;
    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        BlockFlower block = null;
        int i = 0;
        switch (meta) {
            case 1: {
                block = Blocks.red_flower;
                i = BlockFlower.EnumFlowerType.POPPY.getMeta();
                break;
            }
            case 2: {
                block = Blocks.yellow_flower;
                break;
            }
            case 3: {
                block = Blocks.sapling;
                i = BlockPlanks.EnumType.OAK.getMetadata();
                break;
            }
            case 4: {
                block = Blocks.sapling;
                i = BlockPlanks.EnumType.SPRUCE.getMetadata();
                break;
            }
            case 5: {
                block = Blocks.sapling;
                i = BlockPlanks.EnumType.BIRCH.getMetadata();
                break;
            }
            case 6: {
                block = Blocks.sapling;
                i = BlockPlanks.EnumType.JUNGLE.getMetadata();
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
                i = BlockTallGrass.EnumType.FERN.getMeta();
                break;
            }
            case 12: {
                block = Blocks.sapling;
                i = BlockPlanks.EnumType.ACACIA.getMetadata();
                break;
            }
            case 13: {
                block = Blocks.sapling;
                i = BlockPlanks.EnumType.DARK_OAK.getMetadata();
            }
        }
        return new TileEntityFlowerPot(Item.getItemFromBlock((Block)block), i);
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{CONTENTS, LEGACY_DATA});
    }

    public int getMetaFromState(IBlockState state) {
        return (Integer)state.getValue((IProperty)LEGACY_DATA);
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntityFlowerPot tileentityflowerpot;
        Item item;
        EnumFlowerType blockflowerpot$enumflowertype = EnumFlowerType.EMPTY;
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityFlowerPot && (item = (tileentityflowerpot = (TileEntityFlowerPot)tileentity).getFlowerPotItem()) instanceof ItemBlock) {
            int i = tileentityflowerpot.getFlowerPotData();
            Block block = Block.getBlockFromItem((Item)item);
            if (block == Blocks.sapling) {
                switch (1.$SwitchMap$net$minecraft$block$BlockPlanks$EnumType[BlockPlanks.EnumType.byMetadata((int)i).ordinal()]) {
                    case 1: {
                        blockflowerpot$enumflowertype = EnumFlowerType.OAK_SAPLING;
                        break;
                    }
                    case 2: {
                        blockflowerpot$enumflowertype = EnumFlowerType.SPRUCE_SAPLING;
                        break;
                    }
                    case 3: {
                        blockflowerpot$enumflowertype = EnumFlowerType.BIRCH_SAPLING;
                        break;
                    }
                    case 4: {
                        blockflowerpot$enumflowertype = EnumFlowerType.JUNGLE_SAPLING;
                        break;
                    }
                    case 5: {
                        blockflowerpot$enumflowertype = EnumFlowerType.ACACIA_SAPLING;
                        break;
                    }
                    case 6: {
                        blockflowerpot$enumflowertype = EnumFlowerType.DARK_OAK_SAPLING;
                        break;
                    }
                    default: {
                        blockflowerpot$enumflowertype = EnumFlowerType.EMPTY;
                        break;
                    }
                }
            } else if (block == Blocks.tallgrass) {
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
            } else if (block == Blocks.yellow_flower) {
                blockflowerpot$enumflowertype = EnumFlowerType.DANDELION;
            } else if (block == Blocks.red_flower) {
                switch (1.$SwitchMap$net$minecraft$block$BlockFlower$EnumFlowerType[BlockFlower.EnumFlowerType.getType((BlockFlower.EnumFlowerColor)BlockFlower.EnumFlowerColor.RED, (int)i).ordinal()]) {
                    case 1: {
                        blockflowerpot$enumflowertype = EnumFlowerType.POPPY;
                        break;
                    }
                    case 2: {
                        blockflowerpot$enumflowertype = EnumFlowerType.BLUE_ORCHID;
                        break;
                    }
                    case 3: {
                        blockflowerpot$enumflowertype = EnumFlowerType.ALLIUM;
                        break;
                    }
                    case 4: {
                        blockflowerpot$enumflowertype = EnumFlowerType.HOUSTONIA;
                        break;
                    }
                    case 5: {
                        blockflowerpot$enumflowertype = EnumFlowerType.RED_TULIP;
                        break;
                    }
                    case 6: {
                        blockflowerpot$enumflowertype = EnumFlowerType.ORANGE_TULIP;
                        break;
                    }
                    case 7: {
                        blockflowerpot$enumflowertype = EnumFlowerType.WHITE_TULIP;
                        break;
                    }
                    case 8: {
                        blockflowerpot$enumflowertype = EnumFlowerType.PINK_TULIP;
                        break;
                    }
                    case 9: {
                        blockflowerpot$enumflowertype = EnumFlowerType.OXEYE_DAISY;
                        break;
                    }
                    default: {
                        blockflowerpot$enumflowertype = EnumFlowerType.EMPTY;
                        break;
                    }
                }
            } else if (block == Blocks.red_mushroom) {
                blockflowerpot$enumflowertype = EnumFlowerType.MUSHROOM_RED;
            } else if (block == Blocks.brown_mushroom) {
                blockflowerpot$enumflowertype = EnumFlowerType.MUSHROOM_BROWN;
            } else if (block == Blocks.deadbush) {
                blockflowerpot$enumflowertype = EnumFlowerType.DEAD_BUSH;
            } else if (block == Blocks.cactus) {
                blockflowerpot$enumflowertype = EnumFlowerType.CACTUS;
            }
        }
        return state.withProperty(CONTENTS, (Comparable)blockflowerpot$enumflowertype);
    }

    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
}
