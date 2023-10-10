/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Iterator;
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
        if (this.blockMaterial == Material.lava) return false;
        return true;
    }

    @Override
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        if (this.blockMaterial != Material.water) return 0xFFFFFF;
        int n = BiomeColorHelper.getWaterColorAtPos(worldIn, pos);
        return n;
    }

    public static float getLiquidHeightPercent(int meta) {
        if (meta < 8) return (float)(meta + 1) / 9.0f;
        return 0.11111111f;
    }

    protected int getLevel(IBlockAccess worldIn, BlockPos pos) {
        if (worldIn.getBlockState(pos).getBlock().getMaterial() != this.blockMaterial) return -1;
        int n = worldIn.getBlockState(pos).getValue(LEVEL);
        return n;
    }

    protected int getEffectiveFlowDecay(IBlockAccess worldIn, BlockPos pos) {
        int i = this.getLevel(worldIn, pos);
        if (i >= 8) {
            return 0;
        }
        int n = i;
        return n;
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
        if (!hitIfLiquid) return false;
        if (state.getValue(LEVEL) != 0) return false;
        return true;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        Material material = worldIn.getBlockState(pos).getBlock().getMaterial();
        if (material == this.blockMaterial) {
            return false;
        }
        if (side == EnumFacing.UP) {
            return true;
        }
        if (material == Material.ice) {
            return false;
        }
        boolean bl = super.isBlockSolid(worldIn, pos, side);
        return bl;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        if (worldIn.getBlockState(pos).getBlock().getMaterial() == this.blockMaterial) {
            return false;
        }
        if (side == EnumFacing.UP) {
            return true;
        }
        boolean bl = super.shouldSideBeRendered(worldIn, pos, side);
        return bl;
    }

    public boolean func_176364_g(IBlockAccess blockAccess, BlockPos pos) {
        int i = -1;
        while (i <= 1) {
            for (int j = -1; j <= 1; ++j) {
                IBlockState iblockstate = blockAccess.getBlockState(pos.add(i, 0, j));
                Block block = iblockstate.getBlock();
                Material material = block.getMaterial();
                if (material == this.blockMaterial || block.isFullBlock()) continue;
                return true;
            }
            ++i;
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
        EnumFacing enumfacing1;
        BlockPos blockpos1;
        Vec3 vec3 = new Vec3(0.0, 0.0, 0.0);
        int i = this.getEffectiveFlowDecay(worldIn, pos);
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            BlockPos blockpos = pos.offset(enumfacing);
            int j = this.getEffectiveFlowDecay(worldIn, blockpos);
            if (j < 0) {
                if (worldIn.getBlockState(blockpos).getBlock().getMaterial().blocksMovement() || (j = this.getEffectiveFlowDecay(worldIn, blockpos.down())) < 0) continue;
                int k = j - (i - 8);
                vec3 = vec3.addVector((blockpos.getX() - pos.getX()) * k, (blockpos.getY() - pos.getY()) * k, (blockpos.getZ() - pos.getZ()) * k);
                continue;
            }
            if (j < 0) continue;
            int l = j - i;
            vec3 = vec3.addVector((blockpos.getX() - pos.getX()) * l, (blockpos.getY() - pos.getY()) * l, (blockpos.getZ() - pos.getZ()) * l);
        }
        if (worldIn.getBlockState(pos).getValue(LEVEL) < 8) return vec3.normalize();
        Iterator<EnumFacing> iterator = EnumFacing.Plane.HORIZONTAL.iterator();
        do {
            if (!iterator.hasNext()) return vec3.normalize();
        } while (!this.isBlockSolid(worldIn, blockpos1 = pos.offset(enumfacing1 = iterator.next()), enumfacing1) && !this.isBlockSolid(worldIn, blockpos1.up(), enumfacing1));
        vec3 = vec3.normalize().addVector(0.0, -6.0, 0.0);
        return vec3.normalize();
    }

    @Override
    public Vec3 modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3 motion) {
        return motion.add(this.getFlowVector(worldIn, pos));
    }

    @Override
    public int tickRate(World worldIn) {
        if (this.blockMaterial == Material.water) {
            return 5;
        }
        if (this.blockMaterial != Material.lava) {
            return 0;
        }
        if (!worldIn.provider.getHasNoSky()) return 30;
        return 10;
    }

    @Override
    public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos) {
        int n;
        int i = worldIn.getCombinedLight(pos, 0);
        int j = worldIn.getCombinedLight(pos.up(), 0);
        int k = i & 0xFF;
        int l = j & 0xFF;
        int i1 = i >> 16 & 0xFF;
        int j1 = j >> 16 & 0xFF;
        int n2 = k > l ? k : l;
        if (i1 > j1) {
            n = i1;
            return n2 | n << 16;
        }
        n = j1;
        return n2 | n << 16;
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        EnumWorldBlockLayer enumWorldBlockLayer;
        if (this.blockMaterial == Material.water) {
            enumWorldBlockLayer = EnumWorldBlockLayer.TRANSLUCENT;
            return enumWorldBlockLayer;
        }
        enumWorldBlockLayer = EnumWorldBlockLayer.SOLID;
        return enumWorldBlockLayer;
    }

    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        double d0 = pos.getX();
        double d1 = pos.getY();
        double d2 = pos.getZ();
        if (this.blockMaterial == Material.water) {
            int i = state.getValue(LEVEL);
            if (i > 0 && i < 8) {
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
        if (rand.nextInt(10) != 0) return;
        if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down())) return;
        Material material = worldIn.getBlockState(pos.down(2)).getBlock().getMaterial();
        if (material.blocksMovement()) return;
        if (material.isLiquid()) return;
        double d3 = d0 + (double)rand.nextFloat();
        double d5 = d1 - 1.05;
        double d7 = d2 + (double)rand.nextFloat();
        if (this.blockMaterial == Material.water) {
            worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d3, d5, d7, 0.0, 0.0, 0.0, new int[0]);
            return;
        }
        worldIn.spawnParticle(EnumParticleTypes.DRIP_LAVA, d3, d5, d7, 0.0, 0.0, 0.0, new int[0]);
    }

    public static double getFlowDirection(IBlockAccess worldIn, BlockPos pos, Material materialIn) {
        Vec3 vec3 = BlockLiquid.getFlowingBlock(materialIn).getFlowVector(worldIn, pos);
        if (vec3.xCoord == 0.0 && vec3.zCoord == 0.0) {
            return -1000.0;
        }
        double d = MathHelper.func_181159_b(vec3.zCoord, vec3.xCoord) - 1.5707963267948966;
        return d;
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
        if (this.blockMaterial != Material.lava) return false;
        boolean flag = false;
        for (EnumFacing enumfacing : EnumFacing.values()) {
            if (enumfacing == EnumFacing.DOWN || worldIn.getBlockState(pos.offset(enumfacing)).getBlock().getMaterial() != Material.water) continue;
            flag = true;
            break;
        }
        if (!flag) return false;
        Integer integer = state.getValue(LEVEL);
        if (integer == 0) {
            worldIn.setBlockState(pos, Blocks.obsidian.getDefaultState());
            this.triggerMixEffects(worldIn, pos);
            return true;
        }
        if (integer > 4) return false;
        worldIn.setBlockState(pos, Blocks.cobblestone.getDefaultState());
        this.triggerMixEffects(worldIn, pos);
        return true;
    }

    protected void triggerMixEffects(World worldIn, BlockPos pos) {
        double d0 = pos.getX();
        double d1 = pos.getY();
        double d2 = pos.getZ();
        worldIn.playSoundEffect(d0 + 0.5, d1 + 0.5, d2 + 0.5, "random.fizz", 0.5f, 2.6f + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8f);
        int i = 0;
        while (i < 8) {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0 + Math.random(), d1 + 1.2, d2 + Math.random(), 0.0, 0.0, 0.0, new int[0]);
            ++i;
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
        if (materialIn != Material.lava) throw new IllegalArgumentException("Invalid material");
        return Blocks.flowing_lava;
    }

    public static BlockStaticLiquid getStaticBlock(Material materialIn) {
        if (materialIn == Material.water) {
            return Blocks.water;
        }
        if (materialIn != Material.lava) throw new IllegalArgumentException("Invalid material");
        return Blocks.lava;
    }
}

