/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 */
package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.Random;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3i;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSkull
extends BlockContainer {
    public static final PropertyBool NODROP;
    private BlockPattern witherBasePattern;
    private static final Predicate<BlockWorldState> IS_WITHER_SKELETON;
    private BlockPattern witherPattern;
    public static final PropertyDirection FACING;

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (!world.isRemote) {
            TileEntity tileEntity;
            if (!iBlockState.getValue(NODROP).booleanValue() && (tileEntity = world.getTileEntity(blockPos)) instanceof TileEntitySkull) {
                TileEntitySkull tileEntitySkull = (TileEntitySkull)tileEntity;
                ItemStack itemStack = new ItemStack(Items.skull, 1, this.getDamageValue(world, blockPos));
                if (tileEntitySkull.getSkullType() == 3 && tileEntitySkull.getPlayerProfile() != null) {
                    itemStack.setTagCompound(new NBTTagCompound());
                    NBTTagCompound nBTTagCompound = new NBTTagCompound();
                    NBTUtil.writeGameProfile(nBTTagCompound, tileEntitySkull.getPlayerProfile());
                    itemStack.getTagCompound().setTag("SkullOwner", nBTTagCompound);
                }
                BlockSkull.spawnAsEntity(world, blockPos, itemStack);
            }
            super.breakBlock(world, blockPos, iBlockState);
        }
    }

    @Override
    public Item getItem(World world, BlockPos blockPos) {
        return Items.skull;
    }

    protected BlockPattern getWitherBasePattern() {
        if (this.witherBasePattern == null) {
            this.witherBasePattern = FactoryBlockPattern.start().aisle("   ", "###", "~#~").where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.soul_sand))).where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
        }
        return this.witherBasePattern;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int n) {
        return new TileEntitySkull();
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        int n = 0;
        n |= iBlockState.getValue(FACING).getIndex();
        if (iBlockState.getValue(NODROP).booleanValue()) {
            n |= 8;
        }
        return n;
    }

    protected BlockSkull() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(NODROP, false));
        this.setBlockBounds(0.25f, 0.0f, 0.25f, 0.75f, 0.5f, 0.75f);
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3, int n, EntityLivingBase entityLivingBase) {
        return this.getDefaultState().withProperty(FACING, entityLivingBase.getHorizontalFacing()).withProperty(NODROP, false);
    }

    static {
        FACING = PropertyDirection.create("facing");
        NODROP = PropertyBool.create("nodrop");
        IS_WITHER_SKELETON = new Predicate<BlockWorldState>(){

            public boolean apply(BlockWorldState blockWorldState) {
                return blockWorldState.getBlockState() != null && blockWorldState.getBlockState().getBlock() == Blocks.skull && blockWorldState.getTileEntity() instanceof TileEntitySkull && ((TileEntitySkull)blockWorldState.getTileEntity()).getSkullType() == 1;
            }
        };
    }

    public void checkWitherSpawn(World world, BlockPos blockPos, TileEntitySkull tileEntitySkull) {
        BlockPattern blockPattern;
        BlockPattern.PatternHelper patternHelper;
        if (tileEntitySkull.getSkullType() == 1 && blockPos.getY() >= 2 && world.getDifficulty() != EnumDifficulty.PEACEFUL && !world.isRemote && (patternHelper = (blockPattern = this.getWitherPattern()).match(world, blockPos)) != null) {
            Object object;
            Object object2;
            int n = 0;
            while (n < 3) {
                object2 = patternHelper.translateOffset(n, 0, 0);
                world.setBlockState(((BlockWorldState)object2).getPos(), ((BlockWorldState)object2).getBlockState().withProperty(NODROP, true), 2);
                ++n;
            }
            n = 0;
            while (n < blockPattern.getPalmLength()) {
                int n2 = 0;
                while (n2 < blockPattern.getThumbLength()) {
                    object = patternHelper.translateOffset(n, n2, 0);
                    world.setBlockState(((BlockWorldState)object).getPos(), Blocks.air.getDefaultState(), 2);
                    ++n2;
                }
                ++n;
            }
            BlockPos blockPos2 = patternHelper.translateOffset(1, 0, 0).getPos();
            object2 = new EntityWither(world);
            object = patternHelper.translateOffset(1, 2, 0).getPos();
            ((Entity)object2).setLocationAndAngles((double)((Vec3i)object).getX() + 0.5, (double)((Vec3i)object).getY() + 0.55, (double)((Vec3i)object).getZ() + 0.5, patternHelper.getFinger().getAxis() == EnumFacing.Axis.X ? 0.0f : 90.0f, 0.0f);
            ((EntityWither)object2).renderYawOffset = patternHelper.getFinger().getAxis() == EnumFacing.Axis.X ? 0.0f : 90.0f;
            ((EntityWither)object2).func_82206_m();
            for (EntityPlayer entityPlayer : world.getEntitiesWithinAABB(EntityPlayer.class, ((Entity)object2).getEntityBoundingBox().expand(50.0, 50.0, 50.0))) {
                entityPlayer.triggerAchievement(AchievementList.spawnWither);
            }
            world.spawnEntityInWorld((Entity)object2);
            int n3 = 0;
            while (n3 < 120) {
                world.spawnParticle(EnumParticleTypes.SNOWBALL, (double)blockPos2.getX() + world.rand.nextDouble(), (double)(blockPos2.getY() - 2) + world.rand.nextDouble() * 3.9, (double)blockPos2.getZ() + world.rand.nextDouble(), 0.0, 0.0, 0.0, new int[0]);
                ++n3;
            }
            n3 = 0;
            while (n3 < blockPattern.getPalmLength()) {
                int n4 = 0;
                while (n4 < blockPattern.getThumbLength()) {
                    BlockWorldState blockWorldState = patternHelper.translateOffset(n3, n4, 0);
                    world.notifyNeighborsRespectDebug(blockWorldState.getPos(), Blocks.air);
                    ++n4;
                }
                ++n3;
            }
        }
    }

    @Override
    public void onBlockHarvested(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer) {
        if (entityPlayer.capabilities.isCreativeMode) {
            iBlockState = iBlockState.withProperty(NODROP, true);
            world.setBlockState(blockPos, iBlockState, 4);
        }
        super.onBlockHarvested(world, blockPos, iBlockState, entityPlayer);
    }

    @Override
    public void dropBlockAsItemWithChance(World world, BlockPos blockPos, IBlockState iBlockState, float f, int n) {
    }

    @Override
    public int getDamageValue(World world, BlockPos blockPos) {
        TileEntity tileEntity = world.getTileEntity(blockPos);
        return tileEntity instanceof TileEntitySkull ? ((TileEntitySkull)tileEntity).getSkullType() : super.getDamageValue(world, blockPos);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, BlockPos blockPos) {
        switch (iBlockAccess.getBlockState(blockPos).getValue(FACING)) {
            default: {
                this.setBlockBounds(0.25f, 0.0f, 0.25f, 0.75f, 0.5f, 0.75f);
                break;
            }
            case NORTH: {
                this.setBlockBounds(0.25f, 0.25f, 0.5f, 0.75f, 0.75f, 1.0f);
                break;
            }
            case SOUTH: {
                this.setBlockBounds(0.25f, 0.25f, 0.0f, 0.75f, 0.75f, 0.5f);
                break;
            }
            case WEST: {
                this.setBlockBounds(0.5f, 0.25f, 0.25f, 1.0f, 0.75f, 0.75f);
                break;
            }
            case EAST: {
                this.setBlockBounds(0.0f, 0.25f, 0.25f, 0.5f, 0.75f, 0.75f);
            }
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockPos, IBlockState iBlockState) {
        this.setBlockBoundsBasedOnState(world, blockPos);
        return super.getCollisionBoundingBox(world, blockPos, iBlockState);
    }

    protected BlockPattern getWitherPattern() {
        if (this.witherPattern == null) {
            this.witherPattern = FactoryBlockPattern.start().aisle("^^^", "###", "~#~").where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.soul_sand))).where('^', IS_WITHER_SKELETON).where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
        }
        return this.witherPattern;
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("tile.skull.skeleton.name");
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, NODROP);
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return Items.skull;
    }

    public boolean canDispenserPlace(World world, BlockPos blockPos, ItemStack itemStack) {
        return itemStack.getMetadata() == 1 && blockPos.getY() >= 2 && world.getDifficulty() != EnumDifficulty.PEACEFUL && !world.isRemote ? this.getWitherBasePattern().match(world, blockPos) != null : false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(n & 7)).withProperty(NODROP, (n & 8) > 0);
    }

    @Override
    public boolean isFullCube() {
        return false;
    }
}

