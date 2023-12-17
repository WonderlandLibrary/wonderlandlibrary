// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.entity.EntityLivingBase;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.entity.Entity;
import net.minecraft.init.Biomes;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;

public class BlockBed extends BlockHorizontal implements ITileEntityProvider
{
    public static final PropertyEnum<EnumPartType> PART;
    public static final PropertyBool OCCUPIED;
    protected static final AxisAlignedBB BED_AABB;
    
    public BlockBed() {
        super(Material.CLOTH);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockBed.PART, EnumPartType.FOOT).withProperty((IProperty<Comparable>)BlockBed.OCCUPIED, false));
        this.hasTileEntity = true;
    }
    
    @Override
    @Deprecated
    public MapColor getMapColor(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        if (state.getValue(BlockBed.PART) == EnumPartType.FOOT) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityBed) {
                final EnumDyeColor enumdyecolor = ((TileEntityBed)tileentity).getColor();
                return MapColor.getBlockColor(enumdyecolor);
            }
        }
        return MapColor.CLOTH;
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, BlockPos pos, IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        if (state.getValue(BlockBed.PART) != EnumPartType.HEAD) {
            pos = pos.offset(state.getValue((IProperty<EnumFacing>)BlockBed.FACING));
            state = worldIn.getBlockState(pos);
            if (state.getBlock() != this) {
                return true;
            }
        }
        if (!worldIn.provider.canRespawnHere() || worldIn.getBiome(pos) == Biomes.HELL) {
            worldIn.setBlockToAir(pos);
            final BlockPos blockpos = pos.offset(state.getValue((IProperty<EnumFacing>)BlockBed.FACING).getOpposite());
            if (worldIn.getBlockState(blockpos).getBlock() == this) {
                worldIn.setBlockToAir(blockpos);
            }
            worldIn.newExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5.0f, true, true);
            return true;
        }
        if (state.getValue((IProperty<Boolean>)BlockBed.OCCUPIED)) {
            final EntityPlayer entityplayer = this.getPlayerInBed(worldIn, pos);
            if (entityplayer != null) {
                playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.occupied", new Object[0]), true);
                return true;
            }
            state = state.withProperty((IProperty<Comparable>)BlockBed.OCCUPIED, false);
            worldIn.setBlockState(pos, state, 4);
        }
        final EntityPlayer.SleepResult entityplayer$sleepresult = playerIn.trySleep(pos);
        if (entityplayer$sleepresult == EntityPlayer.SleepResult.OK) {
            state = state.withProperty((IProperty<Comparable>)BlockBed.OCCUPIED, true);
            worldIn.setBlockState(pos, state, 4);
            return true;
        }
        if (entityplayer$sleepresult == EntityPlayer.SleepResult.NOT_POSSIBLE_NOW) {
            playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.noSleep", new Object[0]), true);
        }
        else if (entityplayer$sleepresult == EntityPlayer.SleepResult.NOT_SAFE) {
            playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.notSafe", new Object[0]), true);
        }
        else if (entityplayer$sleepresult == EntityPlayer.SleepResult.TOO_FAR_AWAY) {
            playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.tooFarAway", new Object[0]), true);
        }
        return true;
    }
    
    @Nullable
    private EntityPlayer getPlayerInBed(final World worldIn, final BlockPos pos) {
        for (final EntityPlayer entityplayer : worldIn.playerEntities) {
            if (entityplayer.isPlayerSleeping() && entityplayer.bedLocation.equals(pos)) {
                return entityplayer;
            }
        }
        return null;
    }
    
    @Override
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public void onFallenUpon(final World worldIn, final BlockPos pos, final Entity entityIn, final float fallDistance) {
        super.onFallenUpon(worldIn, pos, entityIn, fallDistance * 0.5f);
    }
    
    @Override
    public void onLanded(final World worldIn, final Entity entityIn) {
        if (entityIn.isSneaking()) {
            super.onLanded(worldIn, entityIn);
        }
        else if (entityIn.motionY < 0.0) {
            entityIn.motionY = -entityIn.motionY * 0.6600000262260437;
            if (!(entityIn instanceof EntityLivingBase)) {
                entityIn.motionY *= 0.8;
            }
        }
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockBed.FACING);
        if (state.getValue(BlockBed.PART) == EnumPartType.FOOT) {
            if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() != this) {
                worldIn.setBlockToAir(pos);
            }
        }
        else if (worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock() != this) {
            if (!worldIn.isRemote) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
            }
            worldIn.setBlockToAir(pos);
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return (state.getValue(BlockBed.PART) == EnumPartType.FOOT) ? Items.AIR : Items.BED;
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return BlockBed.BED_AABB;
    }
    
    @Override
    @Deprecated
    public boolean hasCustomBreakingProgress(final IBlockState state) {
        return true;
    }
    
    @Nullable
    public static BlockPos getSafeExitLocation(final World worldIn, final BlockPos pos, int tries) {
        final EnumFacing enumfacing = worldIn.getBlockState(pos).getValue((IProperty<EnumFacing>)BlockBed.FACING);
        final int i = pos.getX();
        final int j = pos.getY();
        final int k = pos.getZ();
        for (int l = 0; l <= 1; ++l) {
            final int i2 = i - enumfacing.getXOffset() * l - 1;
            final int j2 = k - enumfacing.getZOffset() * l - 1;
            final int k2 = i2 + 2;
            final int l2 = j2 + 2;
            for (int i3 = i2; i3 <= k2; ++i3) {
                for (int j3 = j2; j3 <= l2; ++j3) {
                    final BlockPos blockpos = new BlockPos(i3, j, j3);
                    if (hasRoomForPlayer(worldIn, blockpos)) {
                        if (tries <= 0) {
                            return blockpos;
                        }
                        --tries;
                    }
                }
            }
        }
        return null;
    }
    
    protected static boolean hasRoomForPlayer(final World worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos.down()).isTopSolid() && !worldIn.getBlockState(pos).getMaterial().isSolid() && !worldIn.getBlockState(pos.up()).getMaterial().isSolid();
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        if (state.getValue(BlockBed.PART) == EnumPartType.HEAD) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            final EnumDyeColor enumdyecolor = (tileentity instanceof TileEntityBed) ? ((TileEntityBed)tileentity).getColor() : EnumDyeColor.RED;
            Block.spawnAsEntity(worldIn, pos, new ItemStack(Items.BED, 1, enumdyecolor.getMetadata()));
        }
    }
    
    @Override
    @Deprecated
    public EnumPushReaction getPushReaction(final IBlockState state) {
        return EnumPushReaction.DESTROY;
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    @Deprecated
    public EnumBlockRenderType getRenderType(final IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        BlockPos blockpos = pos;
        if (state.getValue(BlockBed.PART) == EnumPartType.FOOT) {
            blockpos = pos.offset(state.getValue((IProperty<EnumFacing>)BlockBed.FACING));
        }
        final TileEntity tileentity = worldIn.getTileEntity(blockpos);
        final EnumDyeColor enumdyecolor = (tileentity instanceof TileEntityBed) ? ((TileEntityBed)tileentity).getColor() : EnumDyeColor.RED;
        return new ItemStack(Items.BED, 1, enumdyecolor.getMetadata());
    }
    
    @Override
    public void onBlockHarvested(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer player) {
        if (player.capabilities.isCreativeMode && state.getValue(BlockBed.PART) == EnumPartType.FOOT) {
            final BlockPos blockpos = pos.offset(state.getValue((IProperty<EnumFacing>)BlockBed.FACING));
            if (worldIn.getBlockState(blockpos).getBlock() == this) {
                worldIn.setBlockToAir(blockpos);
            }
        }
    }
    
    @Override
    public void harvestBlock(final World worldIn, final EntityPlayer player, final BlockPos pos, final IBlockState state, final TileEntity te, final ItemStack stack) {
        if (state.getValue(BlockBed.PART) == EnumPartType.HEAD && te instanceof TileEntityBed) {
            final TileEntityBed tileentitybed = (TileEntityBed)te;
            final ItemStack itemstack = tileentitybed.getItemStack();
            Block.spawnAsEntity(worldIn, pos, itemstack);
        }
        else {
            super.harvestBlock(worldIn, player, pos, state, null, stack);
        }
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        final EnumFacing enumfacing = EnumFacing.byHorizontalIndex(meta);
        return ((meta & 0x8) > 0) ? this.getDefaultState().withProperty(BlockBed.PART, EnumPartType.HEAD).withProperty((IProperty<Comparable>)BlockBed.FACING, enumfacing).withProperty((IProperty<Comparable>)BlockBed.OCCUPIED, (meta & 0x4) > 0) : this.getDefaultState().withProperty(BlockBed.PART, EnumPartType.FOOT).withProperty((IProperty<Comparable>)BlockBed.FACING, enumfacing);
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        if (state.getValue(BlockBed.PART) == EnumPartType.FOOT) {
            final IBlockState iblockstate = worldIn.getBlockState(pos.offset(state.getValue((IProperty<EnumFacing>)BlockBed.FACING)));
            if (iblockstate.getBlock() == this) {
                state = state.withProperty((IProperty<Comparable>)BlockBed.OCCUPIED, (Comparable)iblockstate.getValue((IProperty<V>)BlockBed.OCCUPIED));
            }
        }
        return state;
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockBed.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockBed.FACING)));
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue((IProperty<EnumFacing>)BlockBed.FACING)));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue((IProperty<EnumFacing>)BlockBed.FACING).getHorizontalIndex();
        if (state.getValue(BlockBed.PART) == EnumPartType.HEAD) {
            i |= 0x8;
            if (state.getValue((IProperty<Boolean>)BlockBed.OCCUPIED)) {
                i |= 0x4;
            }
        }
        return i;
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockBed.FACING, BlockBed.PART, BlockBed.OCCUPIED });
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityBed();
    }
    
    public static boolean isHeadPiece(final int metadata) {
        return (metadata & 0x8) != 0x0;
    }
    
    static {
        PART = PropertyEnum.create("part", EnumPartType.class);
        OCCUPIED = PropertyBool.create("occupied");
        BED_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5625, 1.0);
    }
    
    public enum EnumPartType implements IStringSerializable
    {
        HEAD("head"), 
        FOOT("foot");
        
        private final String name;
        
        private EnumPartType(final String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
    }
}
