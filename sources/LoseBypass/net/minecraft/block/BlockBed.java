/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Iterator;
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

    public BlockBed() {
        super(Material.cloth);
        this.setDefaultState(this.blockState.getBaseState().withProperty(PART, EnumPartType.FOOT).withProperty(OCCUPIED, false));
        this.setBedBounds();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        if (state.getValue(PART) != EnumPartType.HEAD && (state = worldIn.getBlockState(pos = pos.offset(state.getValue(FACING)))).getBlock() != this) {
            return true;
        }
        if (worldIn.provider.canRespawnHere() && worldIn.getBiomeGenForCoords(pos) != BiomeGenBase.hell) {
            EntityPlayer.EnumStatus entityplayer$enumstatus;
            if (state.getValue(OCCUPIED).booleanValue()) {
                EntityPlayer entityplayer = this.getPlayerInBed(worldIn, pos);
                if (entityplayer != null) {
                    playerIn.addChatComponentMessage(new ChatComponentTranslation("tile.bed.occupied", new Object[0]));
                    return true;
                }
                state = state.withProperty(OCCUPIED, false);
                worldIn.setBlockState(pos, state, 4);
            }
            if ((entityplayer$enumstatus = playerIn.trySleep(pos)) == EntityPlayer.EnumStatus.OK) {
                state = state.withProperty(OCCUPIED, true);
                worldIn.setBlockState(pos, state, 4);
                return true;
            }
            if (entityplayer$enumstatus == EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW) {
                playerIn.addChatComponentMessage(new ChatComponentTranslation("tile.bed.noSleep", new Object[0]));
                return true;
            }
            if (entityplayer$enumstatus != EntityPlayer.EnumStatus.NOT_SAFE) return true;
            playerIn.addChatComponentMessage(new ChatComponentTranslation("tile.bed.notSafe", new Object[0]));
            return true;
        }
        worldIn.setBlockToAir(pos);
        BlockPos blockpos = pos.offset(state.getValue(FACING).getOpposite());
        if (worldIn.getBlockState(blockpos).getBlock() == this) {
            worldIn.setBlockToAir(blockpos);
        }
        worldIn.newExplosion(null, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, 5.0f, true, true);
        return true;
    }

    private EntityPlayer getPlayerInBed(World worldIn, BlockPos pos) {
        EntityPlayer entityplayer;
        Iterator<EntityPlayer> iterator = worldIn.playerEntities.iterator();
        do {
            if (!iterator.hasNext()) return null;
        } while (!(entityplayer = iterator.next()).isPlayerSleeping() || !entityplayer.playerLocation.equals(pos));
        return entityplayer;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        this.setBedBounds();
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        EnumFacing enumfacing = state.getValue(FACING);
        if (state.getValue(PART) == EnumPartType.HEAD) {
            if (worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock() == this) return;
            worldIn.setBlockToAir(pos);
            return;
        }
        if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == this) return;
        worldIn.setBlockToAir(pos);
        if (worldIn.isRemote) return;
        this.dropBlockAsItem(worldIn, pos, state, 0);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        if (state.getValue(PART) == EnumPartType.HEAD) {
            return null;
        }
        Item item = Items.bed;
        return item;
    }

    private void setBedBounds() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5625f, 1.0f);
    }

    public static BlockPos getSafeExitLocation(World worldIn, BlockPos pos, int tries) {
        EnumFacing enumfacing = worldIn.getBlockState(pos).getValue(FACING);
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        int l = 0;
        block0: while (l <= 1) {
            int i1 = i - enumfacing.getFrontOffsetX() * l - 1;
            int j1 = k - enumfacing.getFrontOffsetZ() * l - 1;
            int k1 = i1 + 2;
            int l1 = j1 + 2;
            int i2 = i1;
            while (true) {
                if (i2 <= k1) {
                } else {
                    ++l;
                    continue block0;
                }
                for (int j2 = j1; j2 <= l1; ++j2) {
                    BlockPos blockpos = new BlockPos(i2, j, j2);
                    if (!BlockBed.hasRoomForPlayer(worldIn, blockpos)) continue;
                    if (tries <= 0) {
                        return blockpos;
                    }
                    --tries;
                }
                ++i2;
            }
            break;
        }
        return null;
    }

    protected static boolean hasRoomForPlayer(World worldIn, BlockPos pos) {
        if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down())) return false;
        if (worldIn.getBlockState(pos).getBlock().getMaterial().isSolid()) return false;
        if (worldIn.getBlockState(pos.up()).getBlock().getMaterial().isSolid()) return false;
        return true;
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (state.getValue(PART) != EnumPartType.FOOT) return;
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
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
    public Item getItem(World worldIn, BlockPos pos) {
        return Items.bed;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!player.capabilities.isCreativeMode) return;
        if (state.getValue(PART) != EnumPartType.HEAD) return;
        BlockPos blockpos = pos.offset(state.getValue(FACING).getOpposite());
        if (worldIn.getBlockState(blockpos).getBlock() != this) return;
        worldIn.setBlockToAir(blockpos);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState iBlockState;
        EnumFacing enumfacing = EnumFacing.getHorizontal(meta);
        if ((meta & 8) <= 0) {
            iBlockState = this.getDefaultState().withProperty(PART, EnumPartType.FOOT).withProperty(FACING, enumfacing);
            return iBlockState;
        }
        iBlockState = this.getDefaultState().withProperty(PART, EnumPartType.HEAD).withProperty(FACING, enumfacing).withProperty(OCCUPIED, (meta & 4) > 0);
        return iBlockState;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (state.getValue(PART) != EnumPartType.FOOT) return state;
        IBlockState iblockstate = worldIn.getBlockState(pos.offset(state.getValue(FACING)));
        if (iblockstate.getBlock() != this) return state;
        return state.withProperty(OCCUPIED, iblockstate.getValue(OCCUPIED));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        if (state.getValue(PART) != EnumPartType.HEAD) return i |= state.getValue(FACING).getHorizontalIndex();
        i |= 8;
        if (state.getValue(OCCUPIED) == false) return i |= state.getValue(FACING).getHorizontalIndex();
        i |= 4;
        return i |= state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, PART, OCCUPIED);
    }

    public static enum EnumPartType implements IStringSerializable
    {
        HEAD("head"),
        FOOT("foot");

        private final String name;

        private EnumPartType(String name) {
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

