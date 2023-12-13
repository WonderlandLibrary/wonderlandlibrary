/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

public abstract class BlockLiquid
extends Block {
    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 15);

    protected BlockLiquid(Material materialIn) {
        super(materialIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, 0));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        this.setTickRandomly(true);
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return this.blockMaterial != Material.lava;
    }

    @Override
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        return this.blockMaterial == Material.water ? BiomeColorHelper.getWaterColorAtPos(worldIn, pos) : 0xFFFFFF;
    }

    public static float getLiquidHeightPercent(int meta) {
        if (meta >= 8) {
            meta = 0;
        }
        return (float)(meta + 1) / 9.0f;
    }

    protected int getLevel(IBlockAccess worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getBlock().getMaterial() == this.blockMaterial ? worldIn.getBlockState(pos).getValue(LEVEL) : -1;
    }

    protected int getEffectiveFlowDecay(IBlockAccess worldIn, BlockPos pos) {
        int i2 = this.getLevel(worldIn, pos);
        return i2 >= 8 ? 0 : i2;
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
    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
        return hitIfLiquid && state.getValue(LEVEL) == 0;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        Material material = worldIn.getBlockState(pos).getBlock().getMaterial();
        return material == this.blockMaterial ? false : (side == EnumFacing.UP ? true : (material == Material.ice ? false : super.isBlockSolid(worldIn, pos, side)));
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return worldIn.getBlockState(pos).getBlock().getMaterial() == this.blockMaterial ? false : (side == EnumFacing.UP ? true : super.shouldSideBeRendered(worldIn, pos, side));
    }

    public boolean func_176364_g(IBlockAccess blockAccess, BlockPos pos) {
        for (int i2 = -1; i2 <= 1; ++i2) {
            for (int j2 = -1; j2 <= 1; ++j2) {
                IBlockState iblockstate = blockAccess.getBlockState(pos.add(i2, 0, j2));
                Block block = iblockstate.getBlock();
                Material material = block.getMaterial();
                if (material == this.blockMaterial || block.isFullBlock()) continue;
                return true;
            }
        }
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    @Override
    public int getRenderType() {
        return 1;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    protected Vec3 getFlowVector(IBlockAccess worldIn, BlockPos pos) {
        Vec3 vec3 = new Vec3(0.0, 0.0, 0.0);
        int i2 = this.getEffectiveFlowDecay(worldIn, pos);
        for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL) {
            EnumFacing enumfacing = (EnumFacing)enumfacing0;
            BlockPos blockpos = pos.offset(enumfacing);
            int j2 = this.getEffectiveFlowDecay(worldIn, blockpos);
            if (j2 < 0) {
                if (worldIn.getBlockState(blockpos).getBlock().getMaterial().blocksMovement() || (j2 = this.getEffectiveFlowDecay(worldIn, blockpos.down())) < 0) continue;
                int k2 = j2 - (i2 - 8);
                vec3 = vec3.addVector((blockpos.getX() - pos.getX()) * k2, (blockpos.getY() - pos.getY()) * k2, (blockpos.getZ() - pos.getZ()) * k2);
                continue;
            }
            if (j2 < 0) continue;
            int l2 = j2 - i2;
            vec3 = vec3.addVector((blockpos.getX() - pos.getX()) * l2, (blockpos.getY() - pos.getY()) * l2, (blockpos.getZ() - pos.getZ()) * l2);
        }
        if (worldIn.getBlockState(pos).getValue(LEVEL) >= 8) {
            for (Object enumfacing10 : EnumFacing.Plane.HORIZONTAL) {
                EnumFacing enumfacing1 = (EnumFacing)enumfacing10;
                BlockPos blockpos1 = pos.offset(enumfacing1);
                if (!this.isBlockSolid(worldIn, blockpos1, enumfacing1) && !this.isBlockSolid(worldIn, blockpos1.up(), enumfacing1)) continue;
                vec3 = vec3.normalize().addVector(0.0, -6.0, 0.0);
                break;
            }
        }
        return vec3.normalize();
    }

    @Override
    public Vec3 modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3 motion) {
        return motion.add(this.getFlowVector(worldIn, pos));
    }

    @Override
    public int tickRate(World worldIn) {
        return this.blockMaterial == Material.water ? 5 : (this.blockMaterial == Material.lava ? (worldIn.provider.getHasNoSky() ? 10 : 30) : 0);
    }

    @Override
    public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos) {
        int i2 = worldIn.getCombinedLight(pos, 0);
        int j2 = worldIn.getCombinedLight(pos.up(), 0);
        int k2 = i2 & 0xFF;
        int l2 = j2 & 0xFF;
        int i1 = i2 >> 16 & 0xFF;
        int j1 = j2 >> 16 & 0xFF;
        return (k2 > l2 ? k2 : l2) | (i1 > j1 ? i1 : j1) << 16;
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return this.blockMaterial == Material.water ? EnumWorldBlockLayer.TRANSLUCENT : EnumWorldBlockLayer.SOLID;
    }

    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        Material material;
        double d0 = pos.getX();
        double d1 = pos.getY();
        double d2 = pos.getZ();
        if (this.blockMaterial == Material.water) {
            int i2 = state.getValue(LEVEL);
            if (i2 > 0 && i2 < 8) {
                if (rand.nextInt(64) == 0) {
                    worldIn.playSound(d0 + 0.5, d1 + 0.5, d2 + 0.5, "liquid.water", rand.nextFloat() * 0.25f + 0.75f, rand.nextFloat() * 1.0f + 0.5f, false);
                }
            } else if (rand.nextInt(10) == 0) {
                worldIn.spawnParticle(EnumParticleTypes.SUSPENDED, d0 + (double)rand.nextFloat(), d1 + (double)rand.nextFloat(), d2 + (double)rand.nextFloat(), 0.0, 0.0, 0.0, new int[0]);
            }
        }
        if (this.blockMaterial == Material.lava && worldIn.getBlockState(pos.up()).getBlock().getMaterial() == Material.air && !worldIn.getBlockState(pos.up()).getBlock().isOpaqueCube()) {
            if (rand.nextInt(100) == 0) {
                double d8 = d0 + (double)rand.nextFloat();
                double d4 = d1 + this.maxY;
                double d6 = d2 + (double)rand.nextFloat();
                worldIn.spawnParticle(EnumParticleTypes.LAVA, d8, d4, d6, 0.0, 0.0, 0.0, new int[0]);
                worldIn.playSound(d8, d4, d6, "liquid.lavapop", 0.2f + rand.nextFloat() * 0.2f, 0.9f + rand.nextFloat() * 0.15f, false);
            }
            if (rand.nextInt(200) == 0) {
                worldIn.playSound(d0, d1, d2, "liquid.lava", 0.2f + rand.nextFloat() * 0.2f, 0.9f + rand.nextFloat() * 0.15f, false);
            }
        }
        if (rand.nextInt(10) == 0 && World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) && !(material = worldIn.getBlockState(pos.down(2)).getBlock().getMaterial()).blocksMovement() && !material.isLiquid()) {
            double d3 = d0 + (double)rand.nextFloat();
            double d5 = d1 - 1.05;
            double d7 = d2 + (double)rand.nextFloat();
            if (this.blockMaterial == Material.water) {
                worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d3, d5, d7, 0.0, 0.0, 0.0, new int[0]);
            } else {
                worldIn.spawnParticle(EnumParticleTypes.DRIP_LAVA, d3, d5, d7, 0.0, 0.0, 0.0, new int[0]);
            }
        }
    }

    public static double getFlowDirection(IBlockAccess worldIn, BlockPos pos, Material materialIn) {
        Vec3 vec3 = BlockLiquid.getFlowingBlock(materialIn).getFlowVector(worldIn, pos);
        return vec3.xCoord == 0.0 && vec3.zCoord == 0.0 ? -1000.0 : MathHelper.func_181159_b(vec3.zCoord, vec3.xCoord) - 1.5707963267948966;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.checkForMixing(worldIn, pos, state);
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        this.checkForMixing(worldIn, pos, state);
    }

    public boolean checkForMixing(World worldIn, BlockPos pos, IBlockState state) {
        if (this.blockMaterial == Material.lava) {
            boolean flag = false;
            for (EnumFacing enumfacing : EnumFacing.values()) {
                if (enumfacing == EnumFacing.DOWN || worldIn.getBlockState(pos.offset(enumfacing)).getBlock().getMaterial() != Material.water) continue;
                flag = true;
                break;
            }
            if (flag) {
                Integer integer = state.getValue(LEVEL);
                if (integer == 0) {
                    worldIn.setBlockState(pos, Blocks.obsidian.getDefaultState());
                    this.triggerMixEffects(worldIn, pos);
                    return true;
                }
                if (integer <= 4) {
                    worldIn.setBlockState(pos, Blocks.cobblestone.getDefaultState());
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
        worldIn.playSoundEffect(d0 + 0.5, d1 + 0.5, d2 + 0.5, "random.fizz", 0.5f, 2.6f + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8f);
        for (int i2 = 0; i2 < 8; ++i2) {
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
    protected BlockState createBlockState() {
        return new BlockState(this, LEVEL);
    }

    public static BlockDynamicLiquid getFlowingBlock(Material materialIn) {
        if (materialIn == Material.water) {
            return Blocks.flowing_water;
        }
        if (materialIn == Material.lava) {
            return Blocks.flowing_lava;
        }
        throw new IllegalArgumentException("Invalid material");
    }

    public static BlockStaticLiquid getStaticBlock(Material materialIn) {
        if (materialIn == Material.water) {
            return Blocks.water;
        }
        if (materialIn == Material.lava) {
            return Blocks.lava;
        }
        throw new IllegalArgumentException("Invalid material");
    }
}

