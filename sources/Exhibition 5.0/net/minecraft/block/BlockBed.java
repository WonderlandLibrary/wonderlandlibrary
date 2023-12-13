// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.world.IBlockAccess;
import java.util.Iterator;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.entity.Entity;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;

public class BlockBed extends BlockDirectional
{
    public static final PropertyEnum PART_PROP;
    public static final PropertyBool OCCUPIED_PROP;
    private static final String __OBFID = "CL_00000198";
    
    public BlockBed() {
        super(Material.cloth);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockBed.PART_PROP, EnumPartType.FOOT).withProperty(BlockBed.OCCUPIED_PROP, false));
        this.setBedBounds();
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, BlockPos pos, IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        if (state.getValue(BlockBed.PART_PROP) != EnumPartType.HEAD) {
            pos = pos.offset((EnumFacing)state.getValue(BlockDirectional.AGE));
            state = worldIn.getBlockState(pos);
            if (state.getBlock() != this) {
                return true;
            }
        }
        if (!worldIn.provider.canRespawnHere() || worldIn.getBiomeGenForCoords(pos) == BiomeGenBase.hell) {
            worldIn.setBlockToAir(pos);
            final BlockPos var9 = pos.offset(((EnumFacing)state.getValue(BlockDirectional.AGE)).getOpposite());
            if (worldIn.getBlockState(var9).getBlock() == this) {
                worldIn.setBlockToAir(var9);
            }
            worldIn.newExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5.0f, true, true);
            return true;
        }
        if (state.getValue(BlockBed.OCCUPIED_PROP)) {
            final EntityPlayer var10 = this.func_176470_e(worldIn, pos);
            if (var10 != null) {
                playerIn.addChatComponentMessage(new ChatComponentTranslation("tile.bed.occupied", new Object[0]));
                return true;
            }
            state = state.withProperty(BlockBed.OCCUPIED_PROP, false);
            worldIn.setBlockState(pos, state, 4);
        }
        final EntityPlayer.EnumStatus var11 = playerIn.func_180469_a(pos);
        if (var11 == EntityPlayer.EnumStatus.OK) {
            state = state.withProperty(BlockBed.OCCUPIED_PROP, true);
            worldIn.setBlockState(pos, state, 4);
            return true;
        }
        if (var11 == EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW) {
            playerIn.addChatComponentMessage(new ChatComponentTranslation("tile.bed.noSleep", new Object[0]));
        }
        else if (var11 == EntityPlayer.EnumStatus.NOT_SAFE) {
            playerIn.addChatComponentMessage(new ChatComponentTranslation("tile.bed.notSafe", new Object[0]));
        }
        return true;
    }
    
    private EntityPlayer func_176470_e(final World worldIn, final BlockPos p_176470_2_) {
        for (final EntityPlayer var4 : worldIn.playerEntities) {
            if (var4.isPlayerSleeping() && var4.playerLocation.equals(p_176470_2_)) {
                return var4;
            }
        }
        return null;
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
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        this.setBedBounds();
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        final EnumFacing var5 = (EnumFacing)state.getValue(BlockDirectional.AGE);
        if (state.getValue(BlockBed.PART_PROP) == EnumPartType.HEAD) {
            if (worldIn.getBlockState(pos.offset(var5.getOpposite())).getBlock() != this) {
                worldIn.setBlockToAir(pos);
            }
        }
        else if (worldIn.getBlockState(pos.offset(var5)).getBlock() != this) {
            worldIn.setBlockToAir(pos);
            if (!worldIn.isRemote) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
            }
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return (state.getValue(BlockBed.PART_PROP) == EnumPartType.HEAD) ? null : Items.bed;
    }
    
    private void setBedBounds() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5625f, 1.0f);
    }
    
    public static BlockPos getSafeExitLocation(final World worldIn, final BlockPos p_176468_1_, int p_176468_2_) {
        final EnumFacing var3 = (EnumFacing)worldIn.getBlockState(p_176468_1_).getValue(BlockDirectional.AGE);
        final int var4 = p_176468_1_.getX();
        final int var5 = p_176468_1_.getY();
        final int var6 = p_176468_1_.getZ();
        for (int var7 = 0; var7 <= 1; ++var7) {
            final int var8 = var4 - var3.getFrontOffsetX() * var7 - 1;
            final int var9 = var6 - var3.getFrontOffsetZ() * var7 - 1;
            final int var10 = var8 + 2;
            final int var11 = var9 + 2;
            for (int var12 = var8; var12 <= var10; ++var12) {
                for (int var13 = var9; var13 <= var11; ++var13) {
                    final BlockPos var14 = new BlockPos(var12, var5, var13);
                    if (func_176469_d(worldIn, var14)) {
                        if (p_176468_2_ <= 0) {
                            return var14;
                        }
                        --p_176468_2_;
                    }
                }
            }
        }
        return null;
    }
    
    protected static boolean func_176469_d(final World worldIn, final BlockPos p_176469_1_) {
        return World.doesBlockHaveSolidTopSurface(worldIn, p_176469_1_.offsetDown()) && !worldIn.getBlockState(p_176469_1_).getBlock().getMaterial().isSolid() && !worldIn.getBlockState(p_176469_1_.offsetUp()).getBlock().getMaterial().isSolid();
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        if (state.getValue(BlockBed.PART_PROP) == EnumPartType.FOOT) {
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
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Items.bed;
    }
    
    @Override
    public void onBlockHarvested(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn) {
        if (playerIn.capabilities.isCreativeMode && state.getValue(BlockBed.PART_PROP) == EnumPartType.HEAD) {
            final BlockPos var5 = pos.offset(((EnumFacing)state.getValue(BlockDirectional.AGE)).getOpposite());
            if (worldIn.getBlockState(var5).getBlock() == this) {
                worldIn.setBlockToAir(var5);
            }
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        final EnumFacing var2 = EnumFacing.getHorizontal(meta);
        return ((meta & 0x8) > 0) ? this.getDefaultState().withProperty(BlockBed.PART_PROP, EnumPartType.HEAD).withProperty(BlockDirectional.AGE, var2).withProperty(BlockBed.OCCUPIED_PROP, (meta & 0x4) > 0) : this.getDefaultState().withProperty(BlockBed.PART_PROP, EnumPartType.FOOT).withProperty(BlockDirectional.AGE, var2);
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        if (state.getValue(BlockBed.PART_PROP) == EnumPartType.FOOT) {
            final IBlockState var4 = worldIn.getBlockState(pos.offset((EnumFacing)state.getValue(BlockDirectional.AGE)));
            if (var4.getBlock() == this) {
                state = state.withProperty(BlockBed.OCCUPIED_PROP, var4.getValue(BlockBed.OCCUPIED_PROP));
            }
        }
        return state;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        final byte var2 = 0;
        int var3 = var2 | ((EnumFacing)state.getValue(BlockDirectional.AGE)).getHorizontalIndex();
        if (state.getValue(BlockBed.PART_PROP) == EnumPartType.HEAD) {
            var3 |= 0x8;
            if (state.getValue(BlockBed.OCCUPIED_PROP)) {
                var3 |= 0x4;
            }
        }
        return var3;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockDirectional.AGE, BlockBed.PART_PROP, BlockBed.OCCUPIED_PROP });
    }
    
    static {
        PART_PROP = PropertyEnum.create("part", EnumPartType.class);
        OCCUPIED_PROP = PropertyBool.create("occupied");
    }
    
    public enum EnumPartType implements IStringSerializable
    {
        HEAD("HEAD", 0, "head"), 
        FOOT("FOOT", 1, "foot");
        
        private final String field_177036_c;
        private static final EnumPartType[] $VALUES;
        private static final String __OBFID = "CL_00002134";
        
        private EnumPartType(final String p_i45735_1_, final int p_i45735_2_, final String p_i45735_3_) {
            this.field_177036_c = p_i45735_3_;
        }
        
        @Override
        public String toString() {
            return this.field_177036_c;
        }
        
        @Override
        public String getName() {
            return this.field_177036_c;
        }
        
        static {
            $VALUES = new EnumPartType[] { EnumPartType.HEAD, EnumPartType.FOOT };
        }
    }
}
