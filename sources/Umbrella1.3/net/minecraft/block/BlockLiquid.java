/*
 * Decompiled with CFR 0.150.
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
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

public abstract class BlockLiquid
extends Block {
    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 15);
    private static final String __OBFID = "CL_00000265";

    protected BlockLiquid(Material p_i45413_1_) {
        super(p_i45413_1_);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, Integer.valueOf(0)));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        this.setTickRandomly(true);
    }

    @Override
    public boolean isPassable(IBlockAccess blockAccess, BlockPos pos) {
        return this.blockMaterial != Material.lava;
    }

    @Override
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        return this.blockMaterial == Material.water ? BiomeColorHelper.func_180288_c(worldIn, pos) : 0xFFFFFF;
    }

    public static float getLiquidHeightPercent(int p_149801_0_) {
        if (p_149801_0_ >= 8) {
            p_149801_0_ = 0;
        }
        return (float)(p_149801_0_ + 1) / 9.0f;
    }

    protected int func_176362_e(IBlockAccess p_176362_1_, BlockPos p_176362_2_) {
        return p_176362_1_.getBlockState(p_176362_2_).getBlock().getMaterial() == this.blockMaterial ? (Integer)p_176362_1_.getBlockState(p_176362_2_).getValue(LEVEL) : -1;
    }

    protected int func_176366_f(IBlockAccess p_176366_1_, BlockPos p_176366_2_) {
        int var3 = this.func_176362_e(p_176366_1_, p_176366_2_);
        return var3 >= 8 ? 0 : var3;
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
    public boolean canCollideCheck(IBlockState state, boolean p_176209_2_) {
        return p_176209_2_ && (Integer)state.getValue(LEVEL) == 0;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        Material var4 = worldIn.getBlockState(pos).getBlock().getMaterial();
        return var4 == this.blockMaterial ? false : (side == EnumFacing.UP ? true : (var4 == Material.ice ? false : super.isBlockSolid(worldIn, pos, side)));
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return worldIn.getBlockState(pos).getBlock().getMaterial() == this.blockMaterial ? false : (side == EnumFacing.UP ? true : super.shouldSideBeRendered(worldIn, pos, side));
    }

    public boolean func_176364_g(IBlockAccess p_176364_1_, BlockPos p_176364_2_) {
        for (int var3 = -1; var3 <= 1; ++var3) {
            for (int var4 = -1; var4 <= 1; ++var4) {
                IBlockState var5 = p_176364_1_.getBlockState(p_176364_2_.add(var3, 0, var4));
                Block var6 = var5.getBlock();
                Material var7 = var6.getMaterial();
                if (var7 == this.blockMaterial || var6.isFullBlock()) continue;
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

    protected Vec3 func_180687_h(IBlockAccess p_180687_1_, BlockPos p_180687_2_) {
        BlockPos var7;
        Vec3 var3 = new Vec3(0.0, 0.0, 0.0);
        int var4 = this.func_176366_f(p_180687_1_, p_180687_2_);
        for (EnumFacing var6 : EnumFacing.Plane.HORIZONTAL) {
            int var9;
            var7 = p_180687_2_.offset(var6);
            int var8 = this.func_176366_f(p_180687_1_, var7);
            if (var8 < 0) {
                if (p_180687_1_.getBlockState(var7).getBlock().getMaterial().blocksMovement() || (var8 = this.func_176366_f(p_180687_1_, var7.offsetDown())) < 0) continue;
                var9 = var8 - (var4 - 8);
                var3 = var3.addVector((var7.getX() - p_180687_2_.getX()) * var9, (var7.getY() - p_180687_2_.getY()) * var9, (var7.getZ() - p_180687_2_.getZ()) * var9);
                continue;
            }
            if (var8 < 0) continue;
            var9 = var8 - var4;
            var3 = var3.addVector((var7.getX() - p_180687_2_.getX()) * var9, (var7.getY() - p_180687_2_.getY()) * var9, (var7.getZ() - p_180687_2_.getZ()) * var9);
        }
        if ((Integer)p_180687_1_.getBlockState(p_180687_2_).getValue(LEVEL) >= 8) {
            for (EnumFacing var6 : EnumFacing.Plane.HORIZONTAL) {
                var7 = p_180687_2_.offset(var6);
                if (!this.isBlockSolid(p_180687_1_, var7, var6) && !this.isBlockSolid(p_180687_1_, var7.offsetUp(), var6)) continue;
                var3 = var3.normalize().addVector(0.0, -6.0, 0.0);
                break;
            }
        }
        return var3.normalize();
    }

    @Override
    public Vec3 modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3 motion) {
        return motion.add(this.func_180687_h(worldIn, pos));
    }

    @Override
    public int tickRate(World worldIn) {
        return this.blockMaterial == Material.water ? 5 : (this.blockMaterial == Material.lava ? (worldIn.provider.getHasNoSky() ? 10 : 30) : 0);
    }

    @Override
    public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos) {
        int var3 = worldIn.getCombinedLight(pos, 0);
        int var4 = worldIn.getCombinedLight(pos.offsetUp(), 0);
        int var5 = var3 & 0xFF;
        int var6 = var4 & 0xFF;
        int var7 = var3 >> 16 & 0xFF;
        int var8 = var4 >> 16 & 0xFF;
        return (var5 > var6 ? var5 : var6) | (var7 > var8 ? var7 : var8) << 16;
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return this.blockMaterial == Material.water ? EnumWorldBlockLayer.TRANSLUCENT : EnumWorldBlockLayer.SOLID;
    }

    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        Material var19;
        double var5 = pos.getX();
        double var7 = pos.getY();
        double var9 = pos.getZ();
        if (this.blockMaterial == Material.water) {
            int var11 = (Integer)state.getValue(LEVEL);
            if (var11 > 0 && var11 < 8) {
                if (rand.nextInt(64) == 0) {
                    worldIn.playSound(var5 + 0.5, var7 + 0.5, var9 + 0.5, "liquid.water", rand.nextFloat() * 0.25f + 0.75f, rand.nextFloat() * 1.0f + 0.5f, false);
                }
            } else if (rand.nextInt(10) == 0) {
                worldIn.spawnParticle(EnumParticleTypes.SUSPENDED, var5 + (double)rand.nextFloat(), var7 + (double)rand.nextFloat(), var9 + (double)rand.nextFloat(), 0.0, 0.0, 0.0, new int[0]);
            }
        }
        if (this.blockMaterial == Material.lava && worldIn.getBlockState(pos.offsetUp()).getBlock().getMaterial() == Material.air && !worldIn.getBlockState(pos.offsetUp()).getBlock().isOpaqueCube()) {
            if (rand.nextInt(100) == 0) {
                double var18 = var5 + (double)rand.nextFloat();
                double var13 = var7 + this.maxY;
                double var15 = var9 + (double)rand.nextFloat();
                worldIn.spawnParticle(EnumParticleTypes.LAVA, var18, var13, var15, 0.0, 0.0, 0.0, new int[0]);
                worldIn.playSound(var18, var13, var15, "liquid.lavapop", 0.2f + rand.nextFloat() * 0.2f, 0.9f + rand.nextFloat() * 0.15f, false);
            }
            if (rand.nextInt(200) == 0) {
                worldIn.playSound(var5, var7, var9, "liquid.lava", 0.2f + rand.nextFloat() * 0.2f, 0.9f + rand.nextFloat() * 0.15f, false);
            }
        }
        if (rand.nextInt(10) == 0 && World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) && !(var19 = worldIn.getBlockState(pos.offsetDown(2)).getBlock().getMaterial()).blocksMovement() && !var19.isLiquid()) {
            double var12 = var5 + (double)rand.nextFloat();
            double var14 = var7 - 1.05;
            double var16 = var9 + (double)rand.nextFloat();
            if (this.blockMaterial == Material.water) {
                worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, var12, var14, var16, 0.0, 0.0, 0.0, new int[0]);
            } else {
                worldIn.spawnParticle(EnumParticleTypes.DRIP_LAVA, var12, var14, var16, 0.0, 0.0, 0.0, new int[0]);
            }
        }
    }

    public static double func_180689_a(IBlockAccess p_180689_0_, BlockPos p_180689_1_, Material p_180689_2_) {
        Vec3 var3 = BlockLiquid.getDynamicLiquidForMaterial(p_180689_2_).func_180687_h(p_180689_0_, p_180689_1_);
        return var3.xCoord == 0.0 && var3.zCoord == 0.0 ? -1000.0 : Math.atan2(var3.zCoord, var3.xCoord) - 1.5707963267948966;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.func_176365_e(worldIn, pos, state);
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        this.func_176365_e(worldIn, pos, state);
    }

    public boolean func_176365_e(World worldIn, BlockPos p_176365_2_, IBlockState p_176365_3_) {
        if (this.blockMaterial == Material.lava) {
            boolean var4 = false;
            for (EnumFacing var8 : EnumFacing.values()) {
                if (var8 == EnumFacing.DOWN || worldIn.getBlockState(p_176365_2_.offset(var8)).getBlock().getMaterial() != Material.water) continue;
                var4 = true;
                break;
            }
            if (var4) {
                Integer var9 = (Integer)p_176365_3_.getValue(LEVEL);
                if (var9 == 0) {
                    worldIn.setBlockState(p_176365_2_, Blocks.obsidian.getDefaultState());
                    this.func_180688_d(worldIn, p_176365_2_);
                    return true;
                }
                if (var9 <= 4) {
                    worldIn.setBlockState(p_176365_2_, Blocks.cobblestone.getDefaultState());
                    this.func_180688_d(worldIn, p_176365_2_);
                    return true;
                }
            }
        }
        return false;
    }

    protected void func_180688_d(World worldIn, BlockPos p_180688_2_) {
        double var3 = p_180688_2_.getX();
        double var5 = p_180688_2_.getY();
        double var7 = p_180688_2_.getZ();
        worldIn.playSoundEffect(var3 + 0.5, var5 + 0.5, var7 + 0.5, "random.fizz", 0.5f, 2.6f + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8f);
        for (int var9 = 0; var9 < 8; ++var9) {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, var3 + Math.random(), var5 + 1.2, var7 + Math.random(), 0.0, 0.0, 0.0, new int[0]);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(LEVEL, Integer.valueOf(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (Integer)state.getValue(LEVEL);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, LEVEL);
    }

    public static BlockDynamicLiquid getDynamicLiquidForMaterial(Material p_176361_0_) {
        if (p_176361_0_ == Material.water) {
            return Blocks.flowing_water;
        }
        if (p_176361_0_ == Material.lava) {
            return Blocks.flowing_lava;
        }
        throw new IllegalArgumentException("Invalid material");
    }

    public static BlockStaticLiquid getStaticLiquidForMaterial(Material p_176363_0_) {
        if (p_176363_0_ == Material.water) {
            return Blocks.water;
        }
        if (p_176363_0_ == Material.lava) {
            return Blocks.lava;
        }
        throw new IllegalArgumentException("Invalid material");
    }
}

