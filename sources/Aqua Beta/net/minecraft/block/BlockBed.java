package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class BlockBed
extends BlockDirectional {
    public static final PropertyEnum<EnumPartType> PART = PropertyEnum.create((String)"part", EnumPartType.class);
    public static final PropertyBool OCCUPIED = PropertyBool.create((String)"occupied");

    public BlockBed() {
        super(Material.cloth);
        this.setDefaultState(this.blockState.getBaseState().withProperty(PART, (Comparable)EnumPartType.FOOT).withProperty((IProperty)OCCUPIED, (Comparable)Boolean.valueOf((boolean)false)));
        this.setBedBounds();
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        if (state.getValue(PART) != EnumPartType.HEAD && (state = worldIn.getBlockState(pos = pos.offset((EnumFacing)state.getValue((IProperty)FACING)))).getBlock() != this) {
            return true;
        }
        if (worldIn.provider.canRespawnHere() && worldIn.getBiomeGenForCoords(pos) != BiomeGenBase.hell) {
            EntityPlayer.EnumStatus entityplayer$enumstatus;
            if (((Boolean)state.getValue((IProperty)OCCUPIED)).booleanValue()) {
                EntityPlayer entityplayer = this.getPlayerInBed(worldIn, pos);
                if (entityplayer != null) {
                    playerIn.addChatComponentMessage((IChatComponent)new ChatComponentTranslation("tile.bed.occupied", new Object[0]));
                    return true;
                }
                state = state.withProperty((IProperty)OCCUPIED, (Comparable)Boolean.valueOf((boolean)false));
                worldIn.setBlockState(pos, state, 4);
            }
            if ((entityplayer$enumstatus = playerIn.trySleep(pos)) == EntityPlayer.EnumStatus.OK) {
                state = state.withProperty((IProperty)OCCUPIED, (Comparable)Boolean.valueOf((boolean)true));
                worldIn.setBlockState(pos, state, 4);
                return true;
            }
            if (entityplayer$enumstatus == EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW) {
                playerIn.addChatComponentMessage((IChatComponent)new ChatComponentTranslation("tile.bed.noSleep", new Object[0]));
            } else if (entityplayer$enumstatus == EntityPlayer.EnumStatus.NOT_SAFE) {
                playerIn.addChatComponentMessage((IChatComponent)new ChatComponentTranslation("tile.bed.notSafe", new Object[0]));
            }
            return true;
        }
        worldIn.setBlockToAir(pos);
        BlockPos blockpos = pos.offset(((EnumFacing)state.getValue((IProperty)FACING)).getOpposite());
        if (worldIn.getBlockState(blockpos).getBlock() == this) {
            worldIn.setBlockToAir(blockpos);
        }
        worldIn.newExplosion((Entity)null, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, 5.0f, true, true);
        return true;
    }

    private EntityPlayer getPlayerInBed(World worldIn, BlockPos pos) {
        for (EntityPlayer entityplayer : worldIn.playerEntities) {
            if (!entityplayer.isPlayerSleeping() || !entityplayer.playerLocation.equals((Object)pos)) continue;
            return entityplayer;
        }
        return null;
    }

    public boolean isFullCube() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        this.setBedBounds();
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
        if (state.getValue(PART) == EnumPartType.HEAD) {
            if (worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock() != this) {
                worldIn.setBlockToAir(pos);
            }
        } else if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() != this) {
            worldIn.setBlockToAir(pos);
            if (!worldIn.isRemote) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
            }
        }
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(PART) == EnumPartType.HEAD ? null : Items.bed;
    }

    private void setBedBounds() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5625f, 1.0f);
    }

    public static BlockPos getSafeExitLocation(World worldIn, BlockPos pos, int tries) {
        EnumFacing enumfacing = (EnumFacing)worldIn.getBlockState(pos).getValue((IProperty)FACING);
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        for (int l = 0; l <= 1; ++l) {
            int i1 = i - enumfacing.getFrontOffsetX() * l - 1;
            int j1 = k - enumfacing.getFrontOffsetZ() * l - 1;
            int k1 = i1 + 2;
            int l1 = j1 + 2;
            for (int i2 = i1; i2 <= k1; ++i2) {
                for (int j2 = j1; j2 <= l1; ++j2) {
                    BlockPos blockpos = new BlockPos(i2, j, j2);
                    if (!BlockBed.hasRoomForPlayer(worldIn, blockpos)) continue;
                    if (tries <= 0) {
                        return blockpos;
                    }
                    --tries;
                }
            }
        }
        return null;
    }

    protected static boolean hasRoomForPlayer(World worldIn, BlockPos pos) {
        return World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, (BlockPos)pos.down()) && !worldIn.getBlockState(pos).getBlock().getMaterial().isSolid() && !worldIn.getBlockState(pos.up()).getBlock().getMaterial().isSolid();
    }

    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (state.getValue(PART) == EnumPartType.FOOT) {
            super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
        }
    }

    public int getMobilityFlag() {
        return 1;
    }

    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    public Item getItem(World worldIn, BlockPos pos) {
        return Items.bed;
    }

    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        BlockPos blockpos;
        if (player.capabilities.isCreativeMode && state.getValue(PART) == EnumPartType.HEAD && worldIn.getBlockState(blockpos = pos.offset(((EnumFacing)state.getValue((IProperty)FACING)).getOpposite())).getBlock() == this) {
            worldIn.setBlockToAir(blockpos);
        }
    }

    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.getHorizontal((int)meta);
        return (meta & 8) > 0 ? this.getDefaultState().withProperty(PART, (Comparable)EnumPartType.HEAD).withProperty((IProperty)FACING, (Comparable)enumfacing).withProperty((IProperty)OCCUPIED, (Comparable)Boolean.valueOf(((meta & 4) > 0 ? 1 : 0) != 0)) : this.getDefaultState().withProperty(PART, (Comparable)EnumPartType.FOOT).withProperty((IProperty)FACING, (Comparable)enumfacing);
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        IBlockState iblockstate;
        if (state.getValue(PART) == EnumPartType.FOOT && (iblockstate = worldIn.getBlockState(pos.offset((EnumFacing)state.getValue((IProperty)FACING)))).getBlock() == this) {
            state = state.withProperty((IProperty)OCCUPIED, iblockstate.getValue((IProperty)OCCUPIED));
        }
        return state;
    }

    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= ((EnumFacing)state.getValue((IProperty)FACING)).getHorizontalIndex();
        if (state.getValue(PART) == EnumPartType.HEAD) {
            i |= 8;
            if (((Boolean)state.getValue((IProperty)OCCUPIED)).booleanValue()) {
                i |= 4;
            }
        }
        return i;
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{FACING, PART, OCCUPIED});
    }
}
