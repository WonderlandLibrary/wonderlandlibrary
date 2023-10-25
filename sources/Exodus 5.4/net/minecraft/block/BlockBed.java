/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class BlockBed
extends BlockDirectional {
    public static final PropertyEnum<EnumPartType> PART = PropertyEnum.create("part", EnumPartType.class);
    public static final PropertyBool OCCUPIED = PropertyBool.create("occupied");

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        EnumFacing enumFacing = iBlockState.getValue(FACING);
        if (iBlockState.getValue(PART) == EnumPartType.HEAD) {
            if (world.getBlockState(blockPos.offset(enumFacing.getOpposite())).getBlock() != this) {
                world.setBlockToAir(blockPos);
            }
        } else if (world.getBlockState(blockPos.offset(enumFacing)).getBlock() != this) {
            world.setBlockToAir(blockPos);
            if (!world.isRemote) {
                this.dropBlockAsItem(world, blockPos, iBlockState, 0);
            }
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, BlockPos blockPos) {
        this.setBedBounds();
    }

    private void setBedBounds() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5625f, 1.0f);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumFacing enumFacing, float f, float f2, float f3) {
        if (world.isRemote) {
            return true;
        }
        if (iBlockState.getValue(PART) != EnumPartType.HEAD && (iBlockState = world.getBlockState(blockPos = blockPos.offset(iBlockState.getValue(FACING)))).getBlock() != this) {
            return true;
        }
        if (world.provider.canRespawnHere() && world.getBiomeGenForCoords(blockPos) != BiomeGenBase.hell) {
            Object object;
            if (iBlockState.getValue(OCCUPIED).booleanValue()) {
                object = this.getPlayerInBed(world, blockPos);
                if (object != null) {
                    entityPlayer.addChatComponentMessage(new ChatComponentTranslation("tile.bed.occupied", new Object[0]));
                    return true;
                }
                iBlockState = iBlockState.withProperty(OCCUPIED, false);
                world.setBlockState(blockPos, iBlockState, 4);
            }
            if ((object = entityPlayer.trySleep(blockPos)) == EntityPlayer.EnumStatus.OK) {
                iBlockState = iBlockState.withProperty(OCCUPIED, true);
                world.setBlockState(blockPos, iBlockState, 4);
                return true;
            }
            if (object == EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW) {
                entityPlayer.addChatComponentMessage(new ChatComponentTranslation("tile.bed.noSleep", new Object[0]));
            } else if (object == EntityPlayer.EnumStatus.NOT_SAFE) {
                entityPlayer.addChatComponentMessage(new ChatComponentTranslation("tile.bed.notSafe", new Object[0]));
            }
            return true;
        }
        world.setBlockToAir(blockPos);
        BlockPos blockPos2 = blockPos.offset(iBlockState.getValue(FACING).getOpposite());
        if (world.getBlockState(blockPos2).getBlock() == this) {
            world.setBlockToAir(blockPos2);
        }
        world.newExplosion(null, (double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5, 5.0f, true, true);
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        EnumFacing enumFacing = EnumFacing.getHorizontal(n);
        return (n & 8) > 0 ? this.getDefaultState().withProperty(PART, EnumPartType.HEAD).withProperty(FACING, enumFacing).withProperty(OCCUPIED, (n & 4) > 0) : this.getDefaultState().withProperty(PART, EnumPartType.FOOT).withProperty(FACING, enumFacing);
    }

    protected static boolean hasRoomForPlayer(World world, BlockPos blockPos) {
        return World.doesBlockHaveSolidTopSurface(world, blockPos.down()) && !world.getBlockState(blockPos).getBlock().getMaterial().isSolid() && !world.getBlockState(blockPos.up()).getBlock().getMaterial().isSolid();
    }

    public static BlockPos getSafeExitLocation(World world, BlockPos blockPos, int n) {
        EnumFacing enumFacing = world.getBlockState(blockPos).getValue(FACING);
        int n2 = blockPos.getX();
        int n3 = blockPos.getY();
        int n4 = blockPos.getZ();
        int n5 = 0;
        while (n5 <= 1) {
            int n6 = n2 - enumFacing.getFrontOffsetX() * n5 - 1;
            int n7 = n4 - enumFacing.getFrontOffsetZ() * n5 - 1;
            int n8 = n6 + 2;
            int n9 = n7 + 2;
            int n10 = n6;
            while (n10 <= n8) {
                int n11 = n7;
                while (n11 <= n9) {
                    BlockPos blockPos2 = new BlockPos(n10, n3, n11);
                    if (BlockBed.hasRoomForPlayer(world, blockPos2)) {
                        if (n <= 0) {
                            return blockPos2;
                        }
                        --n;
                    }
                    ++n11;
                }
                ++n10;
            }
            ++n5;
        }
        return null;
    }

    public BlockBed() {
        super(Material.cloth);
        this.setDefaultState(this.blockState.getBaseState().withProperty(PART, EnumPartType.FOOT).withProperty(OCCUPIED, false));
        this.setBedBounds();
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return iBlockState.getValue(PART) == EnumPartType.HEAD ? null : Items.bed;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getMobilityFlag() {
        return 1;
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public Item getItem(World world, BlockPos blockPos) {
        return Items.bed;
    }

    @Override
    public IBlockState getActualState(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockPos blockPos) {
        IBlockState iBlockState2;
        if (iBlockState.getValue(PART) == EnumPartType.FOOT && (iBlockState2 = iBlockAccess.getBlockState(blockPos.offset(iBlockState.getValue(FACING)))).getBlock() == this) {
            iBlockState = iBlockState.withProperty(OCCUPIED, iBlockState2.getValue(OCCUPIED));
        }
        return iBlockState;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, PART, OCCUPIED);
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        int n = 0;
        n |= iBlockState.getValue(FACING).getHorizontalIndex();
        if (iBlockState.getValue(PART) == EnumPartType.HEAD) {
            n |= 8;
            if (iBlockState.getValue(OCCUPIED).booleanValue()) {
                n |= 4;
            }
        }
        return n;
    }

    @Override
    public void dropBlockAsItemWithChance(World world, BlockPos blockPos, IBlockState iBlockState, float f, int n) {
        if (iBlockState.getValue(PART) == EnumPartType.FOOT) {
            super.dropBlockAsItemWithChance(world, blockPos, iBlockState, f, 0);
        }
    }

    private EntityPlayer getPlayerInBed(World world, BlockPos blockPos) {
        for (EntityPlayer entityPlayer : world.playerEntities) {
            if (!entityPlayer.isPlayerSleeping() || !entityPlayer.playerLocation.equals(blockPos)) continue;
            return entityPlayer;
        }
        return null;
    }

    @Override
    public void onBlockHarvested(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer) {
        BlockPos blockPos2;
        if (entityPlayer.capabilities.isCreativeMode && iBlockState.getValue(PART) == EnumPartType.HEAD && world.getBlockState(blockPos2 = blockPos.offset(iBlockState.getValue(FACING).getOpposite())).getBlock() == this) {
            world.setBlockToAir(blockPos2);
        }
    }

    public static enum EnumPartType implements IStringSerializable
    {
        HEAD("head"),
        FOOT("foot");

        private final String name;

        private EnumPartType(String string2) {
            this.name = string2;
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

