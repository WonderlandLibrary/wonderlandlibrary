/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.block;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;

public class BlockFire
extends Block {
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
    private final Map<Block, Integer> flammabilities;
    public static final PropertyBool WEST;
    public static final PropertyBool SOUTH;
    private final Map<Block, Integer> encouragements = Maps.newIdentityHashMap();
    public static final PropertyBool FLIP;
    public static final PropertyBool EAST;
    public static final PropertyBool ALT;
    public static final PropertyInteger UPPER;
    public static final PropertyBool NORTH;

    @Override
    public IBlockState getActualState(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockPos blockPos) {
        int n = blockPos.getX();
        int n2 = blockPos.getY();
        int n3 = blockPos.getZ();
        if (!World.doesBlockHaveSolidTopSurface(iBlockAccess, blockPos.down()) && !Blocks.fire.canCatchFire(iBlockAccess, blockPos.down())) {
            boolean bl = (n + n2 + n3 & 1) == 1;
            boolean bl2 = (n / 2 + n2 / 2 + n3 / 2 & 1) == 1;
            int n4 = 0;
            if (this.canCatchFire(iBlockAccess, blockPos.up())) {
                n4 = bl ? 1 : 2;
            }
            return iBlockState.withProperty(NORTH, this.canCatchFire(iBlockAccess, blockPos.north())).withProperty(EAST, this.canCatchFire(iBlockAccess, blockPos.east())).withProperty(SOUTH, this.canCatchFire(iBlockAccess, blockPos.south())).withProperty(WEST, this.canCatchFire(iBlockAccess, blockPos.west())).withProperty(UPPER, n4).withProperty(FLIP, bl2).withProperty(ALT, bl);
        }
        return this.getDefaultState();
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos blockPos) {
        return World.doesBlockHaveSolidTopSurface(world, blockPos.down()) || this.canNeighborCatchFire(world, blockPos);
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    private int getEncouragement(Block block) {
        Integer n = this.encouragements.get(block);
        return n == null ? 0 : n;
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return iBlockState.getValue(AGE);
    }

    public boolean canCatchFire(IBlockAccess iBlockAccess, BlockPos blockPos) {
        return this.getEncouragement(iBlockAccess.getBlockState(blockPos).getBlock()) > 0;
    }

    @Override
    public boolean requiresUpdates() {
        return false;
    }

    protected BlockFire() {
        super(Material.fire);
        this.flammabilities = Maps.newIdentityHashMap();
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0).withProperty(FLIP, false).withProperty(ALT, false).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false).withProperty(UPPER, 0));
        this.setTickRandomly(true);
    }

    @Override
    public MapColor getMapColor(IBlockState iBlockState) {
        return MapColor.tntColor;
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        if (world.getGameRules().getBoolean("doFireTick")) {
            Block block;
            boolean bl;
            if (!this.canPlaceBlockAt(world, blockPos)) {
                world.setBlockToAir(blockPos);
            }
            boolean bl2 = bl = (block = world.getBlockState(blockPos.down()).getBlock()) == Blocks.netherrack;
            if (world.provider instanceof WorldProviderEnd && block == Blocks.bedrock) {
                bl = true;
            }
            if (!bl && world.isRaining() && this.canDie(world, blockPos)) {
                world.setBlockToAir(blockPos);
            } else {
                int n = iBlockState.getValue(AGE);
                if (n < 15) {
                    iBlockState = iBlockState.withProperty(AGE, n + random.nextInt(3) / 2);
                    world.setBlockState(blockPos, iBlockState, 4);
                }
                world.scheduleUpdate(blockPos, this, this.tickRate(world) + random.nextInt(10));
                if (!bl) {
                    if (!this.canNeighborCatchFire(world, blockPos)) {
                        if (!World.doesBlockHaveSolidTopSurface(world, blockPos.down()) || n > 3) {
                            world.setBlockToAir(blockPos);
                        }
                        return;
                    }
                    if (!this.canCatchFire(world, blockPos.down()) && n == 15 && random.nextInt(4) == 0) {
                        world.setBlockToAir(blockPos);
                        return;
                    }
                }
                boolean bl3 = world.isBlockinHighHumidity(blockPos);
                int n2 = 0;
                if (bl3) {
                    n2 = -50;
                }
                this.catchOnFire(world, blockPos.east(), 300 + n2, random, n);
                this.catchOnFire(world, blockPos.west(), 300 + n2, random, n);
                this.catchOnFire(world, blockPos.down(), 250 + n2, random, n);
                this.catchOnFire(world, blockPos.up(), 250 + n2, random, n);
                this.catchOnFire(world, blockPos.north(), 300 + n2, random, n);
                this.catchOnFire(world, blockPos.south(), 300 + n2, random, n);
                int n3 = -1;
                while (n3 <= 1) {
                    int n4 = -1;
                    while (n4 <= 1) {
                        int n5 = -1;
                        while (n5 <= 4) {
                            if (n3 != 0 || n5 != 0 || n4 != 0) {
                                BlockPos blockPos2;
                                int n6;
                                int n7 = 100;
                                if (n5 > 1) {
                                    n7 += (n5 - 1) * 100;
                                }
                                if ((n6 = this.getNeighborEncouragement(world, blockPos2 = blockPos.add(n3, n5, n4))) > 0) {
                                    int n8 = (n6 + 40 + world.getDifficulty().getDifficultyId() * 7) / (n + 30);
                                    if (bl3) {
                                        n8 /= 2;
                                    }
                                    if (!(n8 <= 0 || random.nextInt(n7) > n8 || world.isRaining() && this.canDie(world, blockPos2))) {
                                        int n9 = n + random.nextInt(5) / 4;
                                        if (n9 > 15) {
                                            n9 = 15;
                                        }
                                        world.setBlockState(blockPos2, iBlockState.withProperty(AGE, n9), 3);
                                    }
                                }
                            }
                            ++n5;
                        }
                        ++n4;
                    }
                    ++n3;
                }
            }
        }
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    protected boolean canDie(World world, BlockPos blockPos) {
        return world.canLightningStrike(blockPos) || world.canLightningStrike(blockPos.west()) || world.canLightningStrike(blockPos.east()) || world.canLightningStrike(blockPos.north()) || world.canLightningStrike(blockPos.south());
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    public void setFireInfo(Block block, int n, int n2) {
        this.encouragements.put(block, n);
        this.flammabilities.put(block, n2);
    }

    public static void init() {
        Blocks.fire.setFireInfo(Blocks.planks, 5, 20);
        Blocks.fire.setFireInfo(Blocks.double_wooden_slab, 5, 20);
        Blocks.fire.setFireInfo(Blocks.wooden_slab, 5, 20);
        Blocks.fire.setFireInfo(Blocks.oak_fence_gate, 5, 20);
        Blocks.fire.setFireInfo(Blocks.spruce_fence_gate, 5, 20);
        Blocks.fire.setFireInfo(Blocks.birch_fence_gate, 5, 20);
        Blocks.fire.setFireInfo(Blocks.jungle_fence_gate, 5, 20);
        Blocks.fire.setFireInfo(Blocks.dark_oak_fence_gate, 5, 20);
        Blocks.fire.setFireInfo(Blocks.acacia_fence_gate, 5, 20);
        Blocks.fire.setFireInfo(Blocks.oak_fence, 5, 20);
        Blocks.fire.setFireInfo(Blocks.spruce_fence, 5, 20);
        Blocks.fire.setFireInfo(Blocks.birch_fence, 5, 20);
        Blocks.fire.setFireInfo(Blocks.jungle_fence, 5, 20);
        Blocks.fire.setFireInfo(Blocks.dark_oak_fence, 5, 20);
        Blocks.fire.setFireInfo(Blocks.acacia_fence, 5, 20);
        Blocks.fire.setFireInfo(Blocks.oak_stairs, 5, 20);
        Blocks.fire.setFireInfo(Blocks.birch_stairs, 5, 20);
        Blocks.fire.setFireInfo(Blocks.spruce_stairs, 5, 20);
        Blocks.fire.setFireInfo(Blocks.jungle_stairs, 5, 20);
        Blocks.fire.setFireInfo(Blocks.log, 5, 5);
        Blocks.fire.setFireInfo(Blocks.log2, 5, 5);
        Blocks.fire.setFireInfo(Blocks.leaves, 30, 60);
        Blocks.fire.setFireInfo(Blocks.leaves2, 30, 60);
        Blocks.fire.setFireInfo(Blocks.bookshelf, 30, 20);
        Blocks.fire.setFireInfo(Blocks.tnt, 15, 100);
        Blocks.fire.setFireInfo(Blocks.tallgrass, 60, 100);
        Blocks.fire.setFireInfo(Blocks.double_plant, 60, 100);
        Blocks.fire.setFireInfo(Blocks.yellow_flower, 60, 100);
        Blocks.fire.setFireInfo(Blocks.red_flower, 60, 100);
        Blocks.fire.setFireInfo(Blocks.deadbush, 60, 100);
        Blocks.fire.setFireInfo(Blocks.wool, 30, 60);
        Blocks.fire.setFireInfo(Blocks.vine, 15, 100);
        Blocks.fire.setFireInfo(Blocks.coal_block, 5, 5);
        Blocks.fire.setFireInfo(Blocks.hay_block, 60, 20);
        Blocks.fire.setFireInfo(Blocks.carpet, 60, 20);
    }

    private boolean canNeighborCatchFire(World world, BlockPos blockPos) {
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing enumFacing = enumFacingArray[n2];
            if (this.canCatchFire(world, blockPos.offset(enumFacing))) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(AGE, n);
    }

    private void catchOnFire(World world, BlockPos blockPos, int n, Random random, int n2) {
        int n3 = this.getFlammability(world.getBlockState(blockPos).getBlock());
        if (random.nextInt(n) < n3) {
            IBlockState iBlockState = world.getBlockState(blockPos);
            if (random.nextInt(n2 + 10) < 5 && !world.canLightningStrike(blockPos)) {
                int n4 = n2 + random.nextInt(5) / 4;
                if (n4 > 15) {
                    n4 = 15;
                }
                world.setBlockState(blockPos, this.getDefaultState().withProperty(AGE, n4), 3);
            } else {
                world.setBlockToAir(blockPos);
            }
            if (iBlockState.getBlock() == Blocks.tnt) {
                Blocks.tnt.onBlockDestroyedByPlayer(world, blockPos, iBlockState.withProperty(BlockTNT.EXPLODE, true));
            }
        }
    }

    @Override
    public int tickRate(World world) {
        return 30;
    }

    @Override
    public void randomDisplayTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        block12: {
            block11: {
                double d;
                double d2;
                double d3;
                int n;
                if (random.nextInt(24) == 0) {
                    world.playSound((float)blockPos.getX() + 0.5f, (float)blockPos.getY() + 0.5f, (float)blockPos.getZ() + 0.5f, "fire.fire", 1.0f + random.nextFloat(), random.nextFloat() * 0.7f + 0.3f, false);
                }
                if (World.doesBlockHaveSolidTopSurface(world, blockPos.down()) || Blocks.fire.canCatchFire(world, blockPos.down())) break block11;
                if (Blocks.fire.canCatchFire(world, blockPos.west())) {
                    n = 0;
                    while (n < 2) {
                        d3 = (double)blockPos.getX() + random.nextDouble() * (double)0.1f;
                        d2 = (double)blockPos.getY() + random.nextDouble();
                        d = (double)blockPos.getZ() + random.nextDouble();
                        world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d3, d2, d, 0.0, 0.0, 0.0, new int[0]);
                        ++n;
                    }
                }
                if (Blocks.fire.canCatchFire(world, blockPos.east())) {
                    n = 0;
                    while (n < 2) {
                        d3 = (double)(blockPos.getX() + 1) - random.nextDouble() * (double)0.1f;
                        d2 = (double)blockPos.getY() + random.nextDouble();
                        d = (double)blockPos.getZ() + random.nextDouble();
                        world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d3, d2, d, 0.0, 0.0, 0.0, new int[0]);
                        ++n;
                    }
                }
                if (Blocks.fire.canCatchFire(world, blockPos.north())) {
                    n = 0;
                    while (n < 2) {
                        d3 = (double)blockPos.getX() + random.nextDouble();
                        d2 = (double)blockPos.getY() + random.nextDouble();
                        d = (double)blockPos.getZ() + random.nextDouble() * (double)0.1f;
                        world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d3, d2, d, 0.0, 0.0, 0.0, new int[0]);
                        ++n;
                    }
                }
                if (Blocks.fire.canCatchFire(world, blockPos.south())) {
                    n = 0;
                    while (n < 2) {
                        d3 = (double)blockPos.getX() + random.nextDouble();
                        d2 = (double)blockPos.getY() + random.nextDouble();
                        d = (double)(blockPos.getZ() + 1) - random.nextDouble() * (double)0.1f;
                        world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d3, d2, d, 0.0, 0.0, 0.0, new int[0]);
                        ++n;
                    }
                }
                if (!Blocks.fire.canCatchFire(world, blockPos.up())) break block12;
                n = 0;
                while (n < 2) {
                    d3 = (double)blockPos.getX() + random.nextDouble();
                    d2 = (double)(blockPos.getY() + 1) - random.nextDouble() * (double)0.1f;
                    d = (double)blockPos.getZ() + random.nextDouble();
                    world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d3, d2, d, 0.0, 0.0, 0.0, new int[0]);
                    ++n;
                }
                break block12;
            }
            int n = 0;
            while (n < 3) {
                double d = (double)blockPos.getX() + random.nextDouble();
                double d4 = (double)blockPos.getY() + random.nextDouble() * 0.5 + 0.5;
                double d5 = (double)blockPos.getZ() + random.nextDouble();
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d, d4, d5, 0.0, 0.0, 0.0, new int[0]);
                ++n;
            }
        }
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, AGE, NORTH, EAST, SOUTH, WEST, UPPER, FLIP, ALT);
    }

    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (world.provider.getDimensionId() > 0 || !Blocks.portal.func_176548_d(world, blockPos)) {
            if (!World.doesBlockHaveSolidTopSurface(world, blockPos.down()) && !this.canNeighborCatchFire(world, blockPos)) {
                world.setBlockToAir(blockPos);
            } else {
                world.scheduleUpdate(blockPos, this, this.tickRate(world) + world.rand.nextInt(10));
            }
        }
    }

    static {
        FLIP = PropertyBool.create("flip");
        ALT = PropertyBool.create("alt");
        NORTH = PropertyBool.create("north");
        EAST = PropertyBool.create("east");
        SOUTH = PropertyBool.create("south");
        WEST = PropertyBool.create("west");
        UPPER = PropertyInteger.create("upper", 0, 2);
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        if (!World.doesBlockHaveSolidTopSurface(world, blockPos.down()) && !this.canNeighborCatchFire(world, blockPos)) {
            world.setBlockToAir(blockPos);
        }
    }

    private int getFlammability(Block block) {
        Integer n = this.flammabilities.get(block);
        return n == null ? 0 : n;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockPos, IBlockState iBlockState) {
        return null;
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    private int getNeighborEncouragement(World world, BlockPos blockPos) {
        if (!world.isAirBlock(blockPos)) {
            return 0;
        }
        int n = 0;
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n2 = enumFacingArray.length;
        int n3 = 0;
        while (n3 < n2) {
            EnumFacing enumFacing = enumFacingArray[n3];
            n = Math.max(this.getEncouragement(world.getBlockState(blockPos.offset(enumFacing)).getBlock()), n);
            ++n3;
        }
        return n;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
}

