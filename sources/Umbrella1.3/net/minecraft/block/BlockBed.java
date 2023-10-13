/*
 * Decompiled with CFR 0.150.
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
    public static final PropertyEnum PART_PROP = PropertyEnum.create("part", EnumPartType.class);
    public static final PropertyBool OCCUPIED_PROP = PropertyBool.create("occupied");
    private static final String __OBFID = "CL_00000198";

    public BlockBed() {
        super(Material.cloth);
        this.setDefaultState(this.blockState.getBaseState().withProperty(PART_PROP, (Comparable)((Object)EnumPartType.FOOT)).withProperty(OCCUPIED_PROP, Boolean.valueOf(false)));
        this.setBedBounds();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        if (state.getValue(PART_PROP) != EnumPartType.HEAD && (state = worldIn.getBlockState(pos = pos.offset((EnumFacing)((Object)state.getValue(AGE))))).getBlock() != this) {
            return true;
        }
        if (worldIn.provider.canRespawnHere() && worldIn.getBiomeGenForCoords(pos) != BiomeGenBase.hell) {
            EntityPlayer.EnumStatus var11;
            if (((Boolean)state.getValue(OCCUPIED_PROP)).booleanValue()) {
                EntityPlayer var10 = this.func_176470_e(worldIn, pos);
                if (var10 != null) {
                    playerIn.addChatComponentMessage(new ChatComponentTranslation("tile.bed.occupied", new Object[0]));
                    return true;
                }
                state = state.withProperty(OCCUPIED_PROP, Boolean.valueOf(false));
                worldIn.setBlockState(pos, state, 4);
            }
            if ((var11 = playerIn.func_180469_a(pos)) == EntityPlayer.EnumStatus.OK) {
                state = state.withProperty(OCCUPIED_PROP, Boolean.valueOf(true));
                worldIn.setBlockState(pos, state, 4);
                return true;
            }
            if (var11 == EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW) {
                playerIn.addChatComponentMessage(new ChatComponentTranslation("tile.bed.noSleep", new Object[0]));
            } else if (var11 == EntityPlayer.EnumStatus.NOT_SAFE) {
                playerIn.addChatComponentMessage(new ChatComponentTranslation("tile.bed.notSafe", new Object[0]));
            }
            return true;
        }
        worldIn.setBlockToAir(pos);
        BlockPos var9 = pos.offset(((EnumFacing)((Object)state.getValue(AGE))).getOpposite());
        if (worldIn.getBlockState(var9).getBlock() == this) {
            worldIn.setBlockToAir(var9);
        }
        worldIn.newExplosion(null, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, 5.0f, true, true);
        return true;
    }

    private EntityPlayer func_176470_e(World worldIn, BlockPos p_176470_2_) {
        EntityPlayer var4;
        Iterator var3 = worldIn.playerEntities.iterator();
        do {
            if (var3.hasNext()) continue;
            return null;
        } while (!(var4 = (EntityPlayer)var3.next()).isPlayerSleeping() || !var4.playerLocation.equals(p_176470_2_));
        return var4;
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
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        this.setBedBounds();
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        EnumFacing var5 = (EnumFacing)((Object)state.getValue(AGE));
        if (state.getValue(PART_PROP) == EnumPartType.HEAD) {
            if (worldIn.getBlockState(pos.offset(var5.getOpposite())).getBlock() != this) {
                worldIn.setBlockToAir(pos);
            }
        } else if (worldIn.getBlockState(pos.offset(var5)).getBlock() != this) {
            worldIn.setBlockToAir(pos);
            if (!worldIn.isRemote) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
            }
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(PART_PROP) == EnumPartType.HEAD ? null : Items.bed;
    }

    private void setBedBounds() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5625f, 1.0f);
    }

    public static BlockPos getSafeExitLocation(World worldIn, BlockPos p_176468_1_, int p_176468_2_) {
        EnumFacing var3 = (EnumFacing)((Object)worldIn.getBlockState(p_176468_1_).getValue(AGE));
        int var4 = p_176468_1_.getX();
        int var5 = p_176468_1_.getY();
        int var6 = p_176468_1_.getZ();
        for (int var7 = 0; var7 <= 1; ++var7) {
            int var8 = var4 - var3.getFrontOffsetX() * var7 - 1;
            int var9 = var6 - var3.getFrontOffsetZ() * var7 - 1;
            int var10 = var8 + 2;
            int var11 = var9 + 2;
            for (int var12 = var8; var12 <= var10; ++var12) {
                for (int var13 = var9; var13 <= var11; ++var13) {
                    BlockPos var14 = new BlockPos(var12, var5, var13);
                    if (!BlockBed.func_176469_d(worldIn, var14)) continue;
                    if (p_176468_2_ <= 0) {
                        return var14;
                    }
                    --p_176468_2_;
                }
            }
        }
        return null;
    }

    protected static boolean func_176469_d(World worldIn, BlockPos p_176469_1_) {
        return World.doesBlockHaveSolidTopSurface(worldIn, p_176469_1_.offsetDown()) && !worldIn.getBlockState(p_176469_1_).getBlock().getMaterial().isSolid() && !worldIn.getBlockState(p_176469_1_.offsetUp()).getBlock().getMaterial().isSolid();
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (state.getValue(PART_PROP) == EnumPartType.FOOT) {
            super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
        }
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
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
        BlockPos var5;
        if (playerIn.capabilities.isCreativeMode && state.getValue(PART_PROP) == EnumPartType.HEAD && worldIn.getBlockState(var5 = pos.offset(((EnumFacing)((Object)state.getValue(AGE))).getOpposite())).getBlock() == this) {
            worldIn.setBlockToAir(var5);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing var2 = EnumFacing.getHorizontal(meta);
        return (meta & 8) > 0 ? this.getDefaultState().withProperty(PART_PROP, (Comparable)((Object)EnumPartType.HEAD)).withProperty(AGE, (Comparable)((Object)var2)).withProperty(OCCUPIED_PROP, Boolean.valueOf((meta & 4) > 0)) : this.getDefaultState().withProperty(PART_PROP, (Comparable)((Object)EnumPartType.FOOT)).withProperty(AGE, (Comparable)((Object)var2));
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        IBlockState var4;
        if (state.getValue(PART_PROP) == EnumPartType.FOOT && (var4 = worldIn.getBlockState(pos.offset((EnumFacing)((Object)state.getValue(AGE))))).getBlock() == this) {
            state = state.withProperty(OCCUPIED_PROP, var4.getValue(OCCUPIED_PROP));
        }
        return state;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int var2 = 0;
        int var3 = var2 | ((EnumFacing)((Object)state.getValue(AGE))).getHorizontalIndex();
        if (state.getValue(PART_PROP) == EnumPartType.HEAD) {
            var3 |= 8;
            if (((Boolean)state.getValue(OCCUPIED_PROP)).booleanValue()) {
                var3 |= 4;
            }
        }
        return var3;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, AGE, PART_PROP, OCCUPIED_PROP);
    }

    public static enum EnumPartType implements IStringSerializable
    {
        HEAD("HEAD", 0, "head"),
        FOOT("FOOT", 1, "foot");

        private final String field_177036_c;
        private static final EnumPartType[] $VALUES;
        private static final String __OBFID = "CL_00002134";

        static {
            $VALUES = new EnumPartType[]{HEAD, FOOT};
        }

        private EnumPartType(String p_i45735_1_, int p_i45735_2_, String p_i45735_3_) {
            this.field_177036_c = p_i45735_3_;
        }

        public String toString() {
            return this.field_177036_c;
        }

        @Override
        public String getName() {
            return this.field_177036_c;
        }
    }
}

