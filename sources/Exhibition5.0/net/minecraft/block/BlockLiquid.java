// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.entity.Entity;
import java.util.Iterator;
import net.minecraft.util.Vec3;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;

public abstract class BlockLiquid extends Block
{
    public static final PropertyInteger LEVEL;
    private static final String __OBFID = "CL_00000265";
    
    protected BlockLiquid(final Material p_i45413_1_) {
        super(p_i45413_1_);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockLiquid.LEVEL, 0));
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        this.setTickRandomly(true);
    }
    
    @Override
    public boolean isPassable(final IBlockAccess blockAccess, final BlockPos pos) {
        return this.blockMaterial != Material.lava;
    }
    
    @Override
    public int colorMultiplier(final IBlockAccess worldIn, final BlockPos pos, final int renderPass) {
        return (this.blockMaterial == Material.water) ? BiomeColorHelper.func_180288_c(worldIn, pos) : 16777215;
    }
    
    public static float getLiquidHeightPercent(int p_149801_0_) {
        if (p_149801_0_ >= 8) {
            p_149801_0_ = 0;
        }
        return (p_149801_0_ + 1) / 9.0f;
    }
    
    protected int func_176362_e(final IBlockAccess p_176362_1_, final BlockPos p_176362_2_) {
        return (int)((p_176362_1_.getBlockState(p_176362_2_).getBlock().getMaterial() == this.blockMaterial) ? p_176362_1_.getBlockState(p_176362_2_).getValue(BlockLiquid.LEVEL) : -1);
    }
    
    protected int func_176366_f(final IBlockAccess p_176366_1_, final BlockPos p_176366_2_) {
        final int var3 = this.func_176362_e(p_176366_1_, p_176366_2_);
        return (var3 >= 8) ? 0 : var3;
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
    public boolean canCollideCheck(final IBlockState state, final boolean p_176209_2_) {
        return p_176209_2_ && (int)state.getValue(BlockLiquid.LEVEL) == 0;
    }
    
    @Override
    public boolean isBlockSolid(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        final Material var4 = worldIn.getBlockState(pos).getBlock().getMaterial();
        return var4 != this.blockMaterial && (side == EnumFacing.UP || (var4 != Material.ice && super.isBlockSolid(worldIn, pos, side)));
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return worldIn.getBlockState(pos).getBlock().getMaterial() != this.blockMaterial && (side == EnumFacing.UP || super.shouldSideBeRendered(worldIn, pos, side));
    }
    
    public boolean func_176364_g(final IBlockAccess p_176364_1_, final BlockPos p_176364_2_) {
        for (int var3 = -1; var3 <= 1; ++var3) {
            for (int var4 = -1; var4 <= 1; ++var4) {
                final IBlockState var5 = p_176364_1_.getBlockState(p_176364_2_.add(var3, 0, var4));
                final Block var6 = var5.getBlock();
                final Material var7 = var6.getMaterial();
                if (var7 != this.blockMaterial && !var6.isFullBlock()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return null;
    }
    
    @Override
    public int getRenderType() {
        return 1;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return null;
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    protected Vec3 func_180687_h(final IBlockAccess p_180687_1_, final BlockPos p_180687_2_) {
        Vec3 var3 = new Vec3(0.0, 0.0, 0.0);
        final int var4 = this.func_176366_f(p_180687_1_, p_180687_2_);
        for (final EnumFacing var6 : EnumFacing.Plane.HORIZONTAL) {
            final BlockPos var7 = p_180687_2_.offset(var6);
            int var8 = this.func_176366_f(p_180687_1_, var7);
            if (var8 < 0) {
                if (p_180687_1_.getBlockState(var7).getBlock().getMaterial().blocksMovement()) {
                    continue;
                }
                var8 = this.func_176366_f(p_180687_1_, var7.offsetDown());
                if (var8 < 0) {
                    continue;
                }
                final int var9 = var8 - (var4 - 8);
                var3 = var3.addVector((var7.getX() - p_180687_2_.getX()) * var9, (var7.getY() - p_180687_2_.getY()) * var9, (var7.getZ() - p_180687_2_.getZ()) * var9);
            }
            else {
                if (var8 < 0) {
                    continue;
                }
                final int var9 = var8 - var4;
                var3 = var3.addVector((var7.getX() - p_180687_2_.getX()) * var9, (var7.getY() - p_180687_2_.getY()) * var9, (var7.getZ() - p_180687_2_.getZ()) * var9);
            }
        }
        if ((int)p_180687_1_.getBlockState(p_180687_2_).getValue(BlockLiquid.LEVEL) >= 8) {
            for (final EnumFacing var6 : EnumFacing.Plane.HORIZONTAL) {
                final BlockPos var7 = p_180687_2_.offset(var6);
                if (this.isBlockSolid(p_180687_1_, var7, var6) || this.isBlockSolid(p_180687_1_, var7.offsetUp(), var6)) {
                    var3 = var3.normalize().addVector(0.0, -6.0, 0.0);
                    break;
                }
            }
        }
        return var3.normalize();
    }
    
    @Override
    public Vec3 modifyAcceleration(final World worldIn, final BlockPos pos, final Entity entityIn, final Vec3 motion) {
        return motion.add(this.func_180687_h(worldIn, pos));
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return (this.blockMaterial == Material.water) ? 5 : ((this.blockMaterial == Material.lava) ? (worldIn.provider.getHasNoSky() ? 10 : 30) : 0);
    }
    
    @Override
    public int getMixedBrightnessForBlock(final IBlockAccess worldIn, final BlockPos pos) {
        final int var3 = worldIn.getCombinedLight(pos, 0);
        final int var4 = worldIn.getCombinedLight(pos.offsetUp(), 0);
        final int var5 = var3 & 0xFF;
        final int var6 = var4 & 0xFF;
        final int var7 = var3 >> 16 & 0xFF;
        final int var8 = var4 >> 16 & 0xFF;
        return ((var5 > var6) ? var5 : var6) | ((var7 > var8) ? var7 : var8) << 16;
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return (this.blockMaterial == Material.water) ? EnumWorldBlockLayer.TRANSLUCENT : EnumWorldBlockLayer.SOLID;
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        final double var5 = pos.getX();
        final double var6 = pos.getY();
        final double var7 = pos.getZ();
        if (this.blockMaterial == Material.water) {
            final int var8 = (int)state.getValue(BlockLiquid.LEVEL);
            if (var8 > 0 && var8 < 8) {
                if (rand.nextInt(64) == 0) {
                    worldIn.playSound(var5 + 0.5, var6 + 0.5, var7 + 0.5, "liquid.water", rand.nextFloat() * 0.25f + 0.75f, rand.nextFloat() * 1.0f + 0.5f, false);
                }
            }
            else if (rand.nextInt(10) == 0) {
                worldIn.spawnParticle(EnumParticleTypes.SUSPENDED, var5 + rand.nextFloat(), var6 + rand.nextFloat(), var7 + rand.nextFloat(), 0.0, 0.0, 0.0, new int[0]);
            }
        }
        if (this.blockMaterial == Material.lava && worldIn.getBlockState(pos.offsetUp()).getBlock().getMaterial() == Material.air && !worldIn.getBlockState(pos.offsetUp()).getBlock().isOpaqueCube()) {
            if (rand.nextInt(100) == 0) {
                final double var9 = var5 + rand.nextFloat();
                final double var10 = var6 + this.maxY;
                final double var11 = var7 + rand.nextFloat();
                worldIn.spawnParticle(EnumParticleTypes.LAVA, var9, var10, var11, 0.0, 0.0, 0.0, new int[0]);
                worldIn.playSound(var9, var10, var11, "liquid.lavapop", 0.2f + rand.nextFloat() * 0.2f, 0.9f + rand.nextFloat() * 0.15f, false);
            }
            if (rand.nextInt(200) == 0) {
                worldIn.playSound(var5, var6, var7, "liquid.lava", 0.2f + rand.nextFloat() * 0.2f, 0.9f + rand.nextFloat() * 0.15f, false);
            }
        }
        if (rand.nextInt(10) == 0 && World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown())) {
            final Material var12 = worldIn.getBlockState(pos.offsetDown(2)).getBlock().getMaterial();
            if (!var12.blocksMovement() && !var12.isLiquid()) {
                final double var13 = var5 + rand.nextFloat();
                final double var14 = var6 - 1.05;
                final double var15 = var7 + rand.nextFloat();
                if (this.blockMaterial == Material.water) {
                    worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, var13, var14, var15, 0.0, 0.0, 0.0, new int[0]);
                }
                else {
                    worldIn.spawnParticle(EnumParticleTypes.DRIP_LAVA, var13, var14, var15, 0.0, 0.0, 0.0, new int[0]);
                }
            }
        }
    }
    
    public static double func_180689_a(final IBlockAccess p_180689_0_, final BlockPos p_180689_1_, final Material p_180689_2_) {
        final Vec3 var3 = getDynamicLiquidForMaterial(p_180689_2_).func_180687_h(p_180689_0_, p_180689_1_);
        return (var3.xCoord == 0.0 && var3.zCoord == 0.0) ? -1000.0 : (Math.atan2(var3.zCoord, var3.xCoord) - 1.5707963267948966);
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.func_176365_e(worldIn, pos, state);
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        this.func_176365_e(worldIn, pos, state);
    }
    
    public boolean func_176365_e(final World worldIn, final BlockPos p_176365_2_, final IBlockState p_176365_3_) {
        if (this.blockMaterial == Material.lava) {
            boolean var4 = false;
            for (final EnumFacing var8 : EnumFacing.values()) {
                if (var8 != EnumFacing.DOWN && worldIn.getBlockState(p_176365_2_.offset(var8)).getBlock().getMaterial() == Material.water) {
                    var4 = true;
                    break;
                }
            }
            if (var4) {
                final Integer var9 = (Integer)p_176365_3_.getValue(BlockLiquid.LEVEL);
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
    
    protected void func_180688_d(final World worldIn, final BlockPos p_180688_2_) {
        final double var3 = p_180688_2_.getX();
        final double var4 = p_180688_2_.getY();
        final double var5 = p_180688_2_.getZ();
        worldIn.playSoundEffect(var3 + 0.5, var4 + 0.5, var5 + 0.5, "random.fizz", 0.5f, 2.6f + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8f);
        for (int var6 = 0; var6 < 8; ++var6) {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, var3 + Math.random(), var4 + 1.2, var5 + Math.random(), 0.0, 0.0, 0.0, new int[0]);
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockLiquid.LEVEL, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return (int)state.getValue(BlockLiquid.LEVEL);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockLiquid.LEVEL });
    }
    
    public static BlockDynamicLiquid getDynamicLiquidForMaterial(final Material p_176361_0_) {
        if (p_176361_0_ == Material.water) {
            return Blocks.flowing_water;
        }
        if (p_176361_0_ == Material.lava) {
            return Blocks.flowing_lava;
        }
        throw new IllegalArgumentException("Invalid material");
    }
    
    public static BlockStaticLiquid getStaticLiquidForMaterial(final Material p_176363_0_) {
        if (p_176363_0_ == Material.water) {
            return Blocks.water;
        }
        if (p_176363_0_ == Material.lava) {
            return Blocks.lava;
        }
        throw new IllegalArgumentException("Invalid material");
    }
    
    static {
        LEVEL = PropertyInteger.create("level", 0, 15);
    }
}
