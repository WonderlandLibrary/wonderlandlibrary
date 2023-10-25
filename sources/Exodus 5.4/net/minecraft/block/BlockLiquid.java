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

    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState iBlockState) {
        this.checkForMixing(world, blockPos, iBlockState);
    }

    protected int getLevel(IBlockAccess iBlockAccess, BlockPos blockPos) {
        return iBlockAccess.getBlockState(blockPos).getBlock().getMaterial() == this.blockMaterial ? iBlockAccess.getBlockState(blockPos).getValue(LEVEL) : -1;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    public static BlockStaticLiquid getStaticBlock(Material material) {
        if (material == Material.water) {
            return Blocks.water;
        }
        if (material == Material.lava) {
            return Blocks.lava;
        }
        throw new IllegalArgumentException("Invalid material");
    }

    public boolean checkForMixing(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (this.blockMaterial == Material.lava) {
            Object object;
            boolean bl = false;
            EnumFacing[] enumFacingArray = EnumFacing.values();
            int n = enumFacingArray.length;
            int n2 = 0;
            while (n2 < n) {
                object = enumFacingArray[n2];
                if (object != EnumFacing.DOWN && world.getBlockState(blockPos.offset((EnumFacing)object)).getBlock().getMaterial() == Material.water) {
                    bl = true;
                    break;
                }
                ++n2;
            }
            if (bl) {
                object = iBlockState.getValue(LEVEL);
                if ((Integer)object == 0) {
                    world.setBlockState(blockPos, Blocks.obsidian.getDefaultState());
                    this.triggerMixEffects(world, blockPos);
                    return true;
                }
                if ((Integer)object <= 4) {
                    world.setBlockState(blockPos, Blocks.cobblestone.getDefaultState());
                    this.triggerMixEffects(world, blockPos);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean canCollideCheck(IBlockState iBlockState, boolean bl) {
        return bl && iBlockState.getValue(LEVEL) == 0;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess iBlockAccess, BlockPos blockPos, EnumFacing enumFacing) {
        return iBlockAccess.getBlockState(blockPos).getBlock().getMaterial() == this.blockMaterial ? false : (enumFacing == EnumFacing.UP ? true : super.shouldSideBeRendered(iBlockAccess, blockPos, enumFacing));
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        this.checkForMixing(world, blockPos, iBlockState);
    }

    @Override
    public boolean isPassable(IBlockAccess iBlockAccess, BlockPos blockPos) {
        return this.blockMaterial != Material.lava;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess iBlockAccess, BlockPos blockPos, EnumFacing enumFacing) {
        Material material = iBlockAccess.getBlockState(blockPos).getBlock().getMaterial();
        return material == this.blockMaterial ? false : (enumFacing == EnumFacing.UP ? true : (material == Material.ice ? false : super.isBlockSolid(iBlockAccess, blockPos, enumFacing)));
    }

    protected int getEffectiveFlowDecay(IBlockAccess iBlockAccess, BlockPos blockPos) {
        int n = this.getLevel(iBlockAccess, blockPos);
        return n >= 8 ? 0 : n;
    }

    @Override
    public void randomDisplayTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        Material material;
        double d = blockPos.getX();
        double d2 = blockPos.getY();
        double d3 = blockPos.getZ();
        if (this.blockMaterial == Material.water) {
            int n = iBlockState.getValue(LEVEL);
            if (n > 0 && n < 8) {
                if (random.nextInt(64) == 0) {
                    world.playSound(d + 0.5, d2 + 0.5, d3 + 0.5, "liquid.water", random.nextFloat() * 0.25f + 0.75f, random.nextFloat() * 1.0f + 0.5f, false);
                }
            } else if (random.nextInt(10) == 0) {
                world.spawnParticle(EnumParticleTypes.SUSPENDED, d + (double)random.nextFloat(), d2 + (double)random.nextFloat(), d3 + (double)random.nextFloat(), 0.0, 0.0, 0.0, new int[0]);
            }
        }
        if (this.blockMaterial == Material.lava && world.getBlockState(blockPos.up()).getBlock().getMaterial() == Material.air && !world.getBlockState(blockPos.up()).getBlock().isOpaqueCube()) {
            if (random.nextInt(100) == 0) {
                double d4 = d + (double)random.nextFloat();
                double d5 = d2 + this.maxY;
                double d6 = d3 + (double)random.nextFloat();
                world.spawnParticle(EnumParticleTypes.LAVA, d4, d5, d6, 0.0, 0.0, 0.0, new int[0]);
                world.playSound(d4, d5, d6, "liquid.lavapop", 0.2f + random.nextFloat() * 0.2f, 0.9f + random.nextFloat() * 0.15f, false);
            }
            if (random.nextInt(200) == 0) {
                world.playSound(d, d2, d3, "liquid.lava", 0.2f + random.nextFloat() * 0.2f, 0.9f + random.nextFloat() * 0.15f, false);
            }
        }
        if (random.nextInt(10) == 0 && World.doesBlockHaveSolidTopSurface(world, blockPos.down()) && !(material = world.getBlockState(blockPos.down(2)).getBlock().getMaterial()).blocksMovement() && !material.isLiquid()) {
            double d7 = d + (double)random.nextFloat();
            double d8 = d2 - 1.05;
            double d9 = d3 + (double)random.nextFloat();
            if (this.blockMaterial == Material.water) {
                world.spawnParticle(EnumParticleTypes.DRIP_WATER, d7, d8, d9, 0.0, 0.0, 0.0, new int[0]);
            } else {
                world.spawnParticle(EnumParticleTypes.DRIP_LAVA, d7, d8, d9, 0.0, 0.0, 0.0, new int[0]);
            }
        }
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return this.blockMaterial == Material.water ? EnumWorldBlockLayer.TRANSLUCENT : EnumWorldBlockLayer.SOLID;
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return iBlockState.getValue(LEVEL);
    }

    @Override
    public int getMixedBrightnessForBlock(IBlockAccess iBlockAccess, BlockPos blockPos) {
        int n = iBlockAccess.getCombinedLight(blockPos, 0);
        int n2 = iBlockAccess.getCombinedLight(blockPos.up(), 0);
        int n3 = n & 0xFF;
        int n4 = n2 & 0xFF;
        int n5 = n >> 16 & 0xFF;
        int n6 = n2 >> 16 & 0xFF;
        return (n3 > n4 ? n3 : n4) | (n5 > n6 ? n5 : n6) << 16;
    }

    public static BlockDynamicLiquid getFlowingBlock(Material material) {
        if (material == Material.water) {
            return Blocks.flowing_water;
        }
        if (material == Material.lava) {
            return Blocks.flowing_lava;
        }
        throw new IllegalArgumentException("Invalid material");
    }

    @Override
    public int colorMultiplier(IBlockAccess iBlockAccess, BlockPos blockPos, int n) {
        return this.blockMaterial == Material.water ? BiomeColorHelper.getWaterColorAtPos(iBlockAccess, blockPos) : 0xFFFFFF;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int tickRate(World world) {
        return this.blockMaterial == Material.water ? 5 : (this.blockMaterial == Material.lava ? (world.provider.getHasNoSky() ? 10 : 30) : 0);
    }

    @Override
    public Vec3 modifyAcceleration(World world, BlockPos blockPos, Entity entity, Vec3 vec3) {
        return vec3.add(this.getFlowVector(world, blockPos));
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return null;
    }

    protected Vec3 getFlowVector(IBlockAccess iBlockAccess, BlockPos blockPos) {
        BlockPos blockPos2;
        Vec3 vec3 = new Vec3(0.0, 0.0, 0.0);
        int n = this.getEffectiveFlowDecay(iBlockAccess, blockPos);
        for (EnumFacing enumFacing : EnumFacing.Plane.HORIZONTAL) {
            int n2;
            blockPos2 = blockPos.offset(enumFacing);
            int n3 = this.getEffectiveFlowDecay(iBlockAccess, blockPos2);
            if (n3 < 0) {
                if (iBlockAccess.getBlockState(blockPos2).getBlock().getMaterial().blocksMovement() || (n3 = this.getEffectiveFlowDecay(iBlockAccess, blockPos2.down())) < 0) continue;
                n2 = n3 - (n - 8);
                vec3 = vec3.addVector((blockPos2.getX() - blockPos.getX()) * n2, (blockPos2.getY() - blockPos.getY()) * n2, (blockPos2.getZ() - blockPos.getZ()) * n2);
                continue;
            }
            if (n3 < 0) continue;
            n2 = n3 - n;
            vec3 = vec3.addVector((blockPos2.getX() - blockPos.getX()) * n2, (blockPos2.getY() - blockPos.getY()) * n2, (blockPos2.getZ() - blockPos.getZ()) * n2);
        }
        if (iBlockAccess.getBlockState(blockPos).getValue(LEVEL) >= 8) {
            for (EnumFacing enumFacing : EnumFacing.Plane.HORIZONTAL) {
                blockPos2 = blockPos.offset(enumFacing);
                if (!this.isBlockSolid(iBlockAccess, blockPos2, enumFacing) && !this.isBlockSolid(iBlockAccess, blockPos2.up(), enumFacing)) continue;
                vec3 = vec3.normalize().addVector(0.0, -6.0, 0.0);
                break;
            }
        }
        return vec3.normalize();
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public int getRenderType() {
        return 1;
    }

    public static double getFlowDirection(IBlockAccess iBlockAccess, BlockPos blockPos, Material material) {
        Vec3 vec3 = BlockLiquid.getFlowingBlock(material).getFlowVector(iBlockAccess, blockPos);
        return vec3.xCoord == 0.0 && vec3.zCoord == 0.0 ? -1000.0 : MathHelper.func_181159_b(vec3.zCoord, vec3.xCoord) - 1.5707963267948966;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockPos, IBlockState iBlockState) {
        return null;
    }

    protected void triggerMixEffects(World world, BlockPos blockPos) {
        double d = blockPos.getX();
        double d2 = blockPos.getY();
        double d3 = blockPos.getZ();
        world.playSoundEffect(d + 0.5, d2 + 0.5, d3 + 0.5, "random.fizz", 0.5f, 2.6f + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8f);
        int n = 0;
        while (n < 8) {
            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d + Math.random(), d2 + 1.2, d3 + Math.random(), 0.0, 0.0, 0.0, new int[0]);
            ++n;
        }
    }

    public boolean func_176364_g(IBlockAccess iBlockAccess, BlockPos blockPos) {
        int n = -1;
        while (n <= 1) {
            int n2 = -1;
            while (n2 <= 1) {
                IBlockState iBlockState = iBlockAccess.getBlockState(blockPos.add(n, 0, n2));
                Block block = iBlockState.getBlock();
                Material material = block.getMaterial();
                if (material != this.blockMaterial && !block.isFullBlock()) {
                    return true;
                }
                ++n2;
            }
            ++n;
        }
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(LEVEL, n);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, LEVEL);
    }

    public static float getLiquidHeightPercent(int n) {
        if (n >= 8) {
            n = 0;
        }
        return (float)(n + 1) / 9.0f;
    }

    protected BlockLiquid(Material material) {
        super(material);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, 0));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        this.setTickRandomly(true);
    }
}

