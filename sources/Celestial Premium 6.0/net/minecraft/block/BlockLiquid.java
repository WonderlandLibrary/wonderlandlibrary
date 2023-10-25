/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.celestial.client.event.EventManager;
import org.celestial.client.event.events.impl.player.EventLiquidSolid;

public abstract class BlockLiquid
extends Block {
    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 15);

    protected BlockLiquid(Material materialIn) {
        super(materialIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, 0));
        this.setTickRandomly(true);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FULL_BLOCK_AABB;
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        EventLiquidSolid event = new EventLiquidSolid(this, pos);
        EventManager.call(event);
        if (event.isCancelled()) {
            return FULL_BLOCK_AABB;
        }
        return NULL_AABB;
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return this.blockMaterial != Material.LAVA;
    }

    public static float getLiquidHeightPercent(int meta) {
        if (meta >= 8) {
            meta = 0;
        }
        return (float)(meta + 1) / 9.0f;
    }

    protected int getDepth(IBlockState p_189542_1_) {
        return p_189542_1_.getMaterial() == this.blockMaterial ? p_189542_1_.getValue(LEVEL) : -1;
    }

    protected int getRenderedDepth(IBlockState p_189545_1_) {
        int i = this.getDepth(p_189545_1_);
        return i >= 8 ? 0 : i;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
        return hitIfLiquid && state.getValue(LEVEL) == 0;
    }

    private boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();
        Material material = iblockstate.getMaterial();
        if (material == this.blockMaterial) {
            return false;
        }
        if (side == EnumFacing.UP) {
            return true;
        }
        if (material == Material.ICE) {
            return false;
        }
        boolean flag = BlockLiquid.func_193382_c(block) || block instanceof BlockStairs;
        return !flag && iblockstate.func_193401_d(worldIn, pos, side) == BlockFaceShape.SOLID;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        if (blockAccess.getBlockState(pos.offset(side)).getMaterial() == this.blockMaterial) {
            return false;
        }
        return side == EnumFacing.UP ? true : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    public boolean shouldRenderSides(IBlockAccess blockAccess, BlockPos pos) {
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                IBlockState iblockstate = blockAccess.getBlockState(pos.add(i, 0, j));
                if (iblockstate.getMaterial() == this.blockMaterial || iblockstate.isFullBlock()) continue;
                return true;
            }
        }
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.LIQUID;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.field_190931_a;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    protected Vec3d getFlow(IBlockAccess p_189543_1_, BlockPos p_189543_2_, IBlockState p_189543_3_) {
        double d0 = 0.0;
        double d1 = 0.0;
        double d2 = 0.0;
        int i = this.getRenderedDepth(p_189543_3_);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            blockpos$pooledmutableblockpos.setPos(p_189543_2_).move(enumfacing);
            int j = this.getRenderedDepth(p_189543_1_.getBlockState(blockpos$pooledmutableblockpos));
            if (j < 0) {
                if (p_189543_1_.getBlockState(blockpos$pooledmutableblockpos).getMaterial().blocksMovement() || (j = this.getRenderedDepth(p_189543_1_.getBlockState(blockpos$pooledmutableblockpos.down()))) < 0) continue;
                int k = j - (i - 8);
                d0 += (double)(enumfacing.getXOffset() * k);
                d1 += (double)(enumfacing.getYOffset() * k);
                d2 += (double)(enumfacing.getZOffset() * k);
                continue;
            }
            if (j < 0) continue;
            int l = j - i;
            d0 += (double)(enumfacing.getXOffset() * l);
            d1 += (double)(enumfacing.getYOffset() * l);
            d2 += (double)(enumfacing.getZOffset() * l);
        }
        Vec3d vec3d = new Vec3d(d0, d1, d2);
        if (p_189543_3_.getValue(LEVEL) >= 8) {
            for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL) {
                blockpos$pooledmutableblockpos.setPos(p_189543_2_).move(enumfacing1);
                if (!this.isBlockSolid(p_189543_1_, blockpos$pooledmutableblockpos, enumfacing1) && !this.isBlockSolid(p_189543_1_, blockpos$pooledmutableblockpos.up(), enumfacing1)) continue;
                vec3d = vec3d.normalize().add(0.0, -6.0, 0.0);
                break;
            }
        }
        blockpos$pooledmutableblockpos.release();
        return vec3d.normalize();
    }

    @Override
    public Vec3d modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3d motion) {
        return motion.add(this.getFlow(worldIn, pos, worldIn.getBlockState(pos)));
    }

    @Override
    public int tickRate(World worldIn) {
        if (this.blockMaterial == Material.WATER) {
            return 5;
        }
        if (this.blockMaterial == Material.LAVA) {
            return worldIn.provider.getHasNoSky() ? 10 : 30;
        }
        return 0;
    }

    @Override
    public int getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos) {
        int i = source.getCombinedLight(pos, 0);
        int j = source.getCombinedLight(pos.up(), 0);
        int k = i & 0xFF;
        int l = j & 0xFF;
        int i1 = i >> 16 & 0xFF;
        int j1 = j >> 16 & 0xFF;
        return (k > l ? k : l) | (i1 > j1 ? i1 : j1) << 16;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return this.blockMaterial == Material.WATER ? BlockRenderLayer.TRANSLUCENT : BlockRenderLayer.SOLID;
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        Material material;
        double d0 = pos.getX();
        double d1 = pos.getY();
        double d2 = pos.getZ();
        if (this.blockMaterial == Material.WATER) {
            int i = stateIn.getValue(LEVEL);
            if (i > 0 && i < 8) {
                if (rand.nextInt(64) == 0) {
                    worldIn.playSound(d0 + 0.5, d1 + 0.5, d2 + 0.5, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS, rand.nextFloat() * 0.25f + 0.75f, rand.nextFloat() + 0.5f, false);
                }
            } else if (rand.nextInt(10) == 0) {
                worldIn.spawnParticle(EnumParticleTypes.SUSPENDED, d0 + (double)rand.nextFloat(), d1 + (double)rand.nextFloat(), d2 + (double)rand.nextFloat(), 0.0, 0.0, 0.0, new int[0]);
            }
        }
        if (this.blockMaterial == Material.LAVA && worldIn.getBlockState(pos.up()).getMaterial() == Material.AIR && !worldIn.getBlockState(pos.up()).isOpaqueCube()) {
            if (rand.nextInt(100) == 0) {
                double d8 = d0 + (double)rand.nextFloat();
                double d4 = d1 + stateIn.getBoundingBox((IBlockAccess)worldIn, (BlockPos)pos).maxY;
                double d6 = d2 + (double)rand.nextFloat();
                worldIn.spawnParticle(EnumParticleTypes.LAVA, d8, d4, d6, 0.0, 0.0, 0.0, new int[0]);
                worldIn.playSound(d8, d4, d6, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 0.2f + rand.nextFloat() * 0.2f, 0.9f + rand.nextFloat() * 0.15f, false);
            }
            if (rand.nextInt(200) == 0) {
                worldIn.playSound(d0, d1, d2, SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, 0.2f + rand.nextFloat() * 0.2f, 0.9f + rand.nextFloat() * 0.15f, false);
            }
        }
        if (rand.nextInt(10) == 0 && worldIn.getBlockState(pos.down()).isFullyOpaque() && !(material = worldIn.getBlockState(pos.down(2)).getMaterial()).blocksMovement() && !material.isLiquid()) {
            double d3 = d0 + (double)rand.nextFloat();
            double d5 = d1 - 1.05;
            double d7 = d2 + (double)rand.nextFloat();
            if (this.blockMaterial == Material.WATER) {
                worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d3, d5, d7, 0.0, 0.0, 0.0, new int[0]);
            } else {
                worldIn.spawnParticle(EnumParticleTypes.DRIP_LAVA, d3, d5, d7, 0.0, 0.0, 0.0, new int[0]);
            }
        }
    }

    public static float getSlopeAngle(IBlockAccess p_189544_0_, BlockPos p_189544_1_, Material p_189544_2_, IBlockState p_189544_3_) {
        Vec3d vec3d = BlockLiquid.getFlowingBlock(p_189544_2_).getFlow(p_189544_0_, p_189544_1_, p_189544_3_);
        return vec3d.x == 0.0 && vec3d.z == 0.0 ? -1000.0f : (float)MathHelper.atan2(vec3d.z, vec3d.x) - 1.5707964f;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.checkForMixing(worldIn, pos, state);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
        this.checkForMixing(worldIn, pos, state);
    }

    public boolean checkForMixing(World worldIn, BlockPos pos, IBlockState state) {
        if (this.blockMaterial == Material.LAVA) {
            boolean flag = false;
            for (EnumFacing enumfacing : EnumFacing.values()) {
                if (enumfacing == EnumFacing.DOWN || worldIn.getBlockState(pos.offset(enumfacing)).getMaterial() != Material.WATER) continue;
                flag = true;
                break;
            }
            if (flag) {
                Integer integer = state.getValue(LEVEL);
                if (integer == 0) {
                    worldIn.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
                    this.triggerMixEffects(worldIn, pos);
                    return true;
                }
                if (integer <= 4) {
                    worldIn.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState());
                    this.triggerMixEffects(worldIn, pos);
                    return true;
                }
            }
        }
        return false;
    }

    protected void triggerMixEffects(World worldIn, BlockPos pos) {
        double d0 = pos.getX();
        double d1 = pos.getY();
        double d2 = pos.getZ();
        worldIn.playSound(null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 2.6f + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8f);
        for (int i = 0; i < 8; ++i) {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0 + Math.random(), d1 + 1.2, d2 + Math.random(), 0.0, 0.0, 0.0, new int[0]);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(LEVEL, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(LEVEL);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block)this, LEVEL);
    }

    public static BlockDynamicLiquid getFlowingBlock(Material materialIn) {
        if (materialIn == Material.WATER) {
            return Blocks.FLOWING_WATER;
        }
        if (materialIn == Material.LAVA) {
            return Blocks.FLOWING_LAVA;
        }
        throw new IllegalArgumentException("Invalid material");
    }

    public static BlockStaticLiquid getStaticBlock(Material materialIn) {
        if (materialIn == Material.WATER) {
            return Blocks.WATER;
        }
        if (materialIn == Material.LAVA) {
            return Blocks.LAVA;
        }
        throw new IllegalArgumentException("Invalid material");
    }

    public static float func_190973_f(IBlockState p_190973_0_, IBlockAccess p_190973_1_, BlockPos p_190973_2_) {
        int i = p_190973_0_.getValue(LEVEL);
        return (i & 7) == 0 && p_190973_1_.getBlockState(p_190973_2_.up()).getMaterial() == Material.WATER ? 1.0f : 1.0f - BlockLiquid.getLiquidHeightPercent(i);
    }

    public static float func_190972_g(IBlockState p_190972_0_, IBlockAccess p_190972_1_, BlockPos p_190972_2_) {
        return (float)p_190972_2_.getY() + BlockLiquid.func_190973_f(p_190972_0_, p_190972_1_, p_190972_2_);
    }

    @Override
    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
        return BlockFaceShape.UNDEFINED;
    }
}

