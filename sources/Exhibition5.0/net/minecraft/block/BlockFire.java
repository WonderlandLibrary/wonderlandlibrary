// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.WorldProviderEnd;
import java.util.Random;
import net.minecraft.util.AxisAlignedBB;
import com.google.common.collect.Maps;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import java.util.Map;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;

public class BlockFire extends Block
{
    public static final PropertyInteger field_176543_a;
    public static final PropertyBool field_176540_b;
    public static final PropertyBool field_176544_M;
    public static final PropertyBool field_176545_N;
    public static final PropertyBool field_176546_O;
    public static final PropertyBool field_176541_P;
    public static final PropertyBool field_176539_Q;
    public static final PropertyInteger field_176542_R;
    private final Map field_149849_a;
    private final Map field_149848_b;
    private static final String __OBFID = "CL_00000245";
    
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        final int var4 = pos.getX();
        final int var5 = pos.getY();
        final int var6 = pos.getZ();
        if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) && !Blocks.fire.func_176535_e(worldIn, pos.offsetDown())) {
            final boolean var7 = (var4 + var5 + var6 & 0x1) == 0x1;
            final boolean var8 = (var4 / 2 + var5 / 2 + var6 / 2 & 0x1) == 0x1;
            int var9 = 0;
            if (this.func_176535_e(worldIn, pos.offsetUp())) {
                var9 = (var7 ? 1 : 2);
            }
            return state.withProperty(BlockFire.field_176545_N, this.func_176535_e(worldIn, pos.offsetNorth())).withProperty(BlockFire.field_176546_O, this.func_176535_e(worldIn, pos.offsetEast())).withProperty(BlockFire.field_176541_P, this.func_176535_e(worldIn, pos.offsetSouth())).withProperty(BlockFire.field_176539_Q, this.func_176535_e(worldIn, pos.offsetWest())).withProperty(BlockFire.field_176542_R, var9).withProperty(BlockFire.field_176540_b, var8).withProperty(BlockFire.field_176544_M, var7);
        }
        return this.getDefaultState();
    }
    
    protected BlockFire() {
        super(Material.fire);
        this.field_149849_a = Maps.newIdentityHashMap();
        this.field_149848_b = Maps.newIdentityHashMap();
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockFire.field_176543_a, 0).withProperty(BlockFire.field_176540_b, false).withProperty(BlockFire.field_176544_M, false).withProperty(BlockFire.field_176545_N, false).withProperty(BlockFire.field_176546_O, false).withProperty(BlockFire.field_176541_P, false).withProperty(BlockFire.field_176539_Q, false).withProperty(BlockFire.field_176542_R, 0));
        this.setTickRandomly(true);
    }
    
    public static void func_149843_e() {
        Blocks.fire.func_180686_a(Blocks.planks, 5, 20);
        Blocks.fire.func_180686_a(Blocks.double_wooden_slab, 5, 20);
        Blocks.fire.func_180686_a(Blocks.wooden_slab, 5, 20);
        Blocks.fire.func_180686_a(Blocks.oak_fence_gate, 5, 20);
        Blocks.fire.func_180686_a(Blocks.spruce_fence_gate, 5, 20);
        Blocks.fire.func_180686_a(Blocks.birch_fence_gate, 5, 20);
        Blocks.fire.func_180686_a(Blocks.jungle_fence_gate, 5, 20);
        Blocks.fire.func_180686_a(Blocks.dark_oak_fence_gate, 5, 20);
        Blocks.fire.func_180686_a(Blocks.acacia_fence_gate, 5, 20);
        Blocks.fire.func_180686_a(Blocks.oak_fence, 5, 20);
        Blocks.fire.func_180686_a(Blocks.spruce_fence, 5, 20);
        Blocks.fire.func_180686_a(Blocks.birch_fence, 5, 20);
        Blocks.fire.func_180686_a(Blocks.jungle_fence, 5, 20);
        Blocks.fire.func_180686_a(Blocks.dark_oak_fence, 5, 20);
        Blocks.fire.func_180686_a(Blocks.acacia_fence, 5, 20);
        Blocks.fire.func_180686_a(Blocks.oak_stairs, 5, 20);
        Blocks.fire.func_180686_a(Blocks.birch_stairs, 5, 20);
        Blocks.fire.func_180686_a(Blocks.spruce_stairs, 5, 20);
        Blocks.fire.func_180686_a(Blocks.jungle_stairs, 5, 20);
        Blocks.fire.func_180686_a(Blocks.log, 5, 5);
        Blocks.fire.func_180686_a(Blocks.log2, 5, 5);
        Blocks.fire.func_180686_a(Blocks.leaves, 30, 60);
        Blocks.fire.func_180686_a(Blocks.leaves2, 30, 60);
        Blocks.fire.func_180686_a(Blocks.bookshelf, 30, 20);
        Blocks.fire.func_180686_a(Blocks.tnt, 15, 100);
        Blocks.fire.func_180686_a(Blocks.tallgrass, 60, 100);
        Blocks.fire.func_180686_a(Blocks.double_plant, 60, 100);
        Blocks.fire.func_180686_a(Blocks.yellow_flower, 60, 100);
        Blocks.fire.func_180686_a(Blocks.red_flower, 60, 100);
        Blocks.fire.func_180686_a(Blocks.deadbush, 60, 100);
        Blocks.fire.func_180686_a(Blocks.wool, 30, 60);
        Blocks.fire.func_180686_a(Blocks.vine, 15, 100);
        Blocks.fire.func_180686_a(Blocks.coal_block, 5, 5);
        Blocks.fire.func_180686_a(Blocks.hay_block, 60, 20);
        Blocks.fire.func_180686_a(Blocks.carpet, 60, 20);
    }
    
    public void func_180686_a(final Block p_180686_1_, final int p_180686_2_, final int p_180686_3_) {
        this.field_149849_a.put(p_180686_1_, p_180686_2_);
        this.field_149848_b.put(p_180686_1_, p_180686_3_);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return null;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return 30;
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, IBlockState state, final Random rand) {
        if (worldIn.getGameRules().getGameRuleBooleanValue("doFireTick")) {
            if (!this.canPlaceBlockAt(worldIn, pos)) {
                worldIn.setBlockToAir(pos);
            }
            final Block var5 = worldIn.getBlockState(pos.offsetDown()).getBlock();
            boolean var6 = var5 == Blocks.netherrack;
            if (worldIn.provider instanceof WorldProviderEnd && var5 == Blocks.bedrock) {
                var6 = true;
            }
            if (!var6 && worldIn.isRaining() && this.func_176537_d(worldIn, pos)) {
                worldIn.setBlockToAir(pos);
            }
            else {
                final int var7 = (int)state.getValue(BlockFire.field_176543_a);
                if (var7 < 15) {
                    state = state.withProperty(BlockFire.field_176543_a, var7 + rand.nextInt(3) / 2);
                    worldIn.setBlockState(pos, state, 4);
                }
                worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn) + rand.nextInt(10));
                if (!var6) {
                    if (!this.func_176533_e(worldIn, pos)) {
                        if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) || var7 > 3) {
                            worldIn.setBlockToAir(pos);
                        }
                        return;
                    }
                    if (!this.func_176535_e(worldIn, pos.offsetDown()) && var7 == 15 && rand.nextInt(4) == 0) {
                        worldIn.setBlockToAir(pos);
                        return;
                    }
                }
                final boolean var8 = worldIn.func_180502_D(pos);
                byte var9 = 0;
                if (var8) {
                    var9 = -50;
                }
                this.func_176536_a(worldIn, pos.offsetEast(), 300 + var9, rand, var7);
                this.func_176536_a(worldIn, pos.offsetWest(), 300 + var9, rand, var7);
                this.func_176536_a(worldIn, pos.offsetDown(), 250 + var9, rand, var7);
                this.func_176536_a(worldIn, pos.offsetUp(), 250 + var9, rand, var7);
                this.func_176536_a(worldIn, pos.offsetNorth(), 300 + var9, rand, var7);
                this.func_176536_a(worldIn, pos.offsetSouth(), 300 + var9, rand, var7);
                for (int var10 = -1; var10 <= 1; ++var10) {
                    for (int var11 = -1; var11 <= 1; ++var11) {
                        for (int var12 = -1; var12 <= 4; ++var12) {
                            if (var10 != 0 || var12 != 0 || var11 != 0) {
                                int var13 = 100;
                                if (var12 > 1) {
                                    var13 += (var12 - 1) * 100;
                                }
                                final BlockPos var14 = pos.add(var10, var12, var11);
                                final int var15 = this.func_176538_m(worldIn, var14);
                                if (var15 > 0) {
                                    int var16 = (var15 + 40 + worldIn.getDifficulty().getDifficultyId() * 7) / (var7 + 30);
                                    if (var8) {
                                        var16 /= 2;
                                    }
                                    if (var16 > 0 && rand.nextInt(var13) <= var16 && (!worldIn.isRaining() || !this.func_176537_d(worldIn, var14))) {
                                        int var17 = var7 + rand.nextInt(5) / 4;
                                        if (var17 > 15) {
                                            var17 = 15;
                                        }
                                        worldIn.setBlockState(var14, state.withProperty(BlockFire.field_176543_a, var17), 3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    protected boolean func_176537_d(final World worldIn, final BlockPos p_176537_2_) {
        return worldIn.func_175727_C(p_176537_2_) || worldIn.func_175727_C(p_176537_2_.offsetWest()) || worldIn.func_175727_C(p_176537_2_.offsetEast()) || worldIn.func_175727_C(p_176537_2_.offsetNorth()) || worldIn.func_175727_C(p_176537_2_.offsetSouth());
    }
    
    @Override
    public boolean requiresUpdates() {
        return false;
    }
    
    private int func_176532_c(final Block p_176532_1_) {
        final Integer var2 = this.field_149848_b.get(p_176532_1_);
        return (var2 == null) ? 0 : var2;
    }
    
    private int func_176534_d(final Block p_176534_1_) {
        final Integer var2 = this.field_149849_a.get(p_176534_1_);
        return (var2 == null) ? 0 : var2;
    }
    
    private void func_176536_a(final World worldIn, final BlockPos p_176536_2_, final int p_176536_3_, final Random p_176536_4_, final int p_176536_5_) {
        final int var6 = this.func_176532_c(worldIn.getBlockState(p_176536_2_).getBlock());
        if (p_176536_4_.nextInt(p_176536_3_) < var6) {
            final IBlockState var7 = worldIn.getBlockState(p_176536_2_);
            if (p_176536_4_.nextInt(p_176536_5_ + 10) < 5 && !worldIn.func_175727_C(p_176536_2_)) {
                int var8 = p_176536_5_ + p_176536_4_.nextInt(5) / 4;
                if (var8 > 15) {
                    var8 = 15;
                }
                worldIn.setBlockState(p_176536_2_, this.getDefaultState().withProperty(BlockFire.field_176543_a, var8), 3);
            }
            else {
                worldIn.setBlockToAir(p_176536_2_);
            }
            if (var7.getBlock() == Blocks.tnt) {
                Blocks.tnt.onBlockDestroyedByPlayer(worldIn, p_176536_2_, var7.withProperty(BlockTNT.field_176246_a, true));
            }
        }
    }
    
    private boolean func_176533_e(final World worldIn, final BlockPos p_176533_2_) {
        for (final EnumFacing var6 : EnumFacing.values()) {
            if (this.func_176535_e(worldIn, p_176533_2_.offset(var6))) {
                return true;
            }
        }
        return false;
    }
    
    private int func_176538_m(final World worldIn, final BlockPos p_176538_2_) {
        if (!worldIn.isAirBlock(p_176538_2_)) {
            return 0;
        }
        int var3 = 0;
        for (final EnumFacing var7 : EnumFacing.values()) {
            var3 = Math.max(this.func_176534_d(worldIn.getBlockState(p_176538_2_.offset(var7)).getBlock()), var3);
        }
        return var3;
    }
    
    @Override
    public boolean isCollidable() {
        return false;
    }
    
    public boolean func_176535_e(final IBlockAccess p_176535_1_, final BlockPos p_176535_2_) {
        return this.func_176534_d(p_176535_1_.getBlockState(p_176535_2_).getBlock()) > 0;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) || this.func_176533_e(worldIn, pos);
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) && !this.func_176533_e(worldIn, pos)) {
            worldIn.setBlockToAir(pos);
        }
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (worldIn.provider.getDimensionId() > 0 || !Blocks.portal.func_176548_d(worldIn, pos)) {
            if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) && !this.func_176533_e(worldIn, pos)) {
                worldIn.setBlockToAir(pos);
            }
            else {
                worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn) + worldIn.rand.nextInt(10));
            }
        }
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (rand.nextInt(24) == 0) {
            worldIn.playSound(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, "fire.fire", 1.0f + rand.nextFloat(), rand.nextFloat() * 0.7f + 0.3f, false);
        }
        if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) && !Blocks.fire.func_176535_e(worldIn, pos.offsetDown())) {
            if (Blocks.fire.func_176535_e(worldIn, pos.offsetWest())) {
                for (int var5 = 0; var5 < 2; ++var5) {
                    final double var6 = pos.getX() + rand.nextDouble() * 0.10000000149011612;
                    final double var7 = pos.getY() + rand.nextDouble();
                    final double var8 = pos.getZ() + rand.nextDouble();
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, var6, var7, var8, 0.0, 0.0, 0.0, new int[0]);
                }
            }
            if (Blocks.fire.func_176535_e(worldIn, pos.offsetEast())) {
                for (int var5 = 0; var5 < 2; ++var5) {
                    final double var6 = pos.getX() + 1 - rand.nextDouble() * 0.10000000149011612;
                    final double var7 = pos.getY() + rand.nextDouble();
                    final double var8 = pos.getZ() + rand.nextDouble();
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, var6, var7, var8, 0.0, 0.0, 0.0, new int[0]);
                }
            }
            if (Blocks.fire.func_176535_e(worldIn, pos.offsetNorth())) {
                for (int var5 = 0; var5 < 2; ++var5) {
                    final double var6 = pos.getX() + rand.nextDouble();
                    final double var7 = pos.getY() + rand.nextDouble();
                    final double var8 = pos.getZ() + rand.nextDouble() * 0.10000000149011612;
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, var6, var7, var8, 0.0, 0.0, 0.0, new int[0]);
                }
            }
            if (Blocks.fire.func_176535_e(worldIn, pos.offsetSouth())) {
                for (int var5 = 0; var5 < 2; ++var5) {
                    final double var6 = pos.getX() + rand.nextDouble();
                    final double var7 = pos.getY() + rand.nextDouble();
                    final double var8 = pos.getZ() + 1 - rand.nextDouble() * 0.10000000149011612;
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, var6, var7, var8, 0.0, 0.0, 0.0, new int[0]);
                }
            }
            if (Blocks.fire.func_176535_e(worldIn, pos.offsetUp())) {
                for (int var5 = 0; var5 < 2; ++var5) {
                    final double var6 = pos.getX() + rand.nextDouble();
                    final double var7 = pos.getY() + 1 - rand.nextDouble() * 0.10000000149011612;
                    final double var8 = pos.getZ() + rand.nextDouble();
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, var6, var7, var8, 0.0, 0.0, 0.0, new int[0]);
                }
            }
        }
        else {
            for (int var5 = 0; var5 < 3; ++var5) {
                final double var6 = pos.getX() + rand.nextDouble();
                final double var7 = pos.getY() + rand.nextDouble() * 0.5 + 0.5;
                final double var8 = pos.getZ() + rand.nextDouble();
                worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, var6, var7, var8, 0.0, 0.0, 0.0, new int[0]);
            }
        }
    }
    
    @Override
    public MapColor getMapColor(final IBlockState state) {
        return MapColor.tntColor;
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockFire.field_176543_a, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return (int)state.getValue(BlockFire.field_176543_a);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockFire.field_176543_a, BlockFire.field_176545_N, BlockFire.field_176546_O, BlockFire.field_176541_P, BlockFire.field_176539_Q, BlockFire.field_176542_R, BlockFire.field_176540_b, BlockFire.field_176544_M });
    }
    
    static {
        field_176543_a = PropertyInteger.create("age", 0, 15);
        field_176540_b = PropertyBool.create("flip");
        field_176544_M = PropertyBool.create("alt");
        field_176545_N = PropertyBool.create("north");
        field_176546_O = PropertyBool.create("east");
        field_176541_P = PropertyBool.create("south");
        field_176539_Q = PropertyBool.create("west");
        field_176542_R = PropertyInteger.create("upper", 0, 2);
    }
}
