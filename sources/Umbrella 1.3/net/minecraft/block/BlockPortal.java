/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPortal
extends BlockBreakable {
    public static final PropertyEnum field_176550_a = PropertyEnum.create("axis", EnumFacing.Axis.class, EnumFacing.Axis.X, EnumFacing.Axis.Z);
    private static final String __OBFID = "CL_00000284";

    public BlockPortal() {
        super(Material.portal, false);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176550_a, (Comparable)((Object)EnumFacing.Axis.X)));
        this.setTickRandomly(true);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        if (worldIn.provider.isSurfaceWorld() && worldIn.getGameRules().getGameRuleBooleanValue("doMobSpawning") && rand.nextInt(2000) < worldIn.getDifficulty().getDifficultyId()) {
            Entity var7;
            int var5 = pos.getY();
            BlockPos var6 = pos;
            while (!World.doesBlockHaveSolidTopSurface(worldIn, var6) && var6.getY() > 0) {
                var6 = var6.offsetDown();
            }
            if (var5 > 0 && !worldIn.getBlockState(var6.offsetUp()).getBlock().isNormalCube() && (var7 = ItemMonsterPlacer.spawnCreature(worldIn, 57, (double)var6.getX() + 0.5, (double)var6.getY() + 1.1, (double)var6.getZ() + 0.5)) != null) {
                var7.timeUntilPortal = var7.getPortalCooldown();
            }
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        EnumFacing.Axis var3 = (EnumFacing.Axis)((Object)access.getBlockState(pos).getValue(field_176550_a));
        float var4 = 0.125f;
        float var5 = 0.125f;
        if (var3 == EnumFacing.Axis.X) {
            var4 = 0.5f;
        }
        if (var3 == EnumFacing.Axis.Z) {
            var5 = 0.5f;
        }
        this.setBlockBounds(0.5f - var4, 0.0f, 0.5f - var5, 0.5f + var4, 1.0f, 0.5f + var5);
    }

    public static int func_176549_a(EnumFacing.Axis p_176549_0_) {
        return p_176549_0_ == EnumFacing.Axis.X ? 1 : (p_176549_0_ == EnumFacing.Axis.Z ? 2 : 0);
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    public boolean func_176548_d(World worldIn, BlockPos p_176548_2_) {
        Size var3 = new Size(worldIn, p_176548_2_, EnumFacing.Axis.X);
        if (var3.func_150860_b() && var3.field_150864_e == 0) {
            var3.func_150859_c();
            return true;
        }
        Size var4 = new Size(worldIn, p_176548_2_, EnumFacing.Axis.Z);
        if (var4.func_150860_b() && var4.field_150864_e == 0) {
            var4.func_150859_c();
            return true;
        }
        return false;
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        Size var6;
        EnumFacing.Axis var5 = (EnumFacing.Axis)((Object)state.getValue(field_176550_a));
        if (var5 == EnumFacing.Axis.X) {
            Size var62 = new Size(worldIn, pos, EnumFacing.Axis.X);
            if (!var62.func_150860_b() || var62.field_150864_e < var62.field_150868_h * var62.field_150862_g) {
                worldIn.setBlockState(pos, Blocks.air.getDefaultState());
            }
        } else if (!(var5 != EnumFacing.Axis.Z || (var6 = new Size(worldIn, pos, EnumFacing.Axis.Z)).func_150860_b() && var6.field_150864_e >= var6.field_150868_h * var6.field_150862_g)) {
            worldIn.setBlockState(pos, Blocks.air.getDefaultState());
        }
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        boolean var11;
        EnumFacing.Axis var4 = null;
        IBlockState var5 = worldIn.getBlockState(pos);
        if (worldIn.getBlockState(pos).getBlock() == this) {
            var4 = (EnumFacing.Axis)((Object)var5.getValue(field_176550_a));
            if (var4 == null) {
                return false;
            }
            if (var4 == EnumFacing.Axis.Z && side != EnumFacing.EAST && side != EnumFacing.WEST) {
                return false;
            }
            if (var4 == EnumFacing.Axis.X && side != EnumFacing.SOUTH && side != EnumFacing.NORTH) {
                return false;
            }
        }
        boolean var6 = worldIn.getBlockState(pos.offsetWest()).getBlock() == this && worldIn.getBlockState(pos.offsetWest(2)).getBlock() != this;
        boolean var7 = worldIn.getBlockState(pos.offsetEast()).getBlock() == this && worldIn.getBlockState(pos.offsetEast(2)).getBlock() != this;
        boolean var8 = worldIn.getBlockState(pos.offsetNorth()).getBlock() == this && worldIn.getBlockState(pos.offsetNorth(2)).getBlock() != this;
        boolean var9 = worldIn.getBlockState(pos.offsetSouth()).getBlock() == this && worldIn.getBlockState(pos.offsetSouth(2)).getBlock() != this;
        boolean var10 = var6 || var7 || var4 == EnumFacing.Axis.X;
        boolean bl = var11 = var8 || var9 || var4 == EnumFacing.Axis.Z;
        return var10 && side == EnumFacing.WEST ? true : (var10 && side == EnumFacing.EAST ? true : (var11 && side == EnumFacing.NORTH ? true : var11 && side == EnumFacing.SOUTH));
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (entityIn.ridingEntity == null && entityIn.riddenByEntity == null) {
            entityIn.setInPortal();
        }
    }

    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (rand.nextInt(100) == 0) {
            worldIn.playSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, "portal.portal", 0.5f, rand.nextFloat() * 0.4f + 0.8f, false);
        }
        for (int var5 = 0; var5 < 4; ++var5) {
            double var6 = (float)pos.getX() + rand.nextFloat();
            double var8 = (float)pos.getY() + rand.nextFloat();
            double var10 = (float)pos.getZ() + rand.nextFloat();
            double var12 = ((double)rand.nextFloat() - 0.5) * 0.5;
            double var14 = ((double)rand.nextFloat() - 0.5) * 0.5;
            double var16 = ((double)rand.nextFloat() - 0.5) * 0.5;
            int var18 = rand.nextInt(2) * 2 - 1;
            if (worldIn.getBlockState(pos.offsetWest()).getBlock() != this && worldIn.getBlockState(pos.offsetEast()).getBlock() != this) {
                var6 = (double)pos.getX() + 0.5 + 0.25 * (double)var18;
                var12 = rand.nextFloat() * 2.0f * (float)var18;
            } else {
                var10 = (double)pos.getZ() + 0.5 + 0.25 * (double)var18;
                var16 = rand.nextFloat() * 2.0f * (float)var18;
            }
            worldIn.spawnParticle(EnumParticleTypes.PORTAL, var6, var8, var10, var12, var14, var16, new int[0]);
        }
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return null;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(field_176550_a, (Comparable)((Object)((meta & 3) == 2 ? EnumFacing.Axis.Z : EnumFacing.Axis.X)));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return BlockPortal.func_176549_a((EnumFacing.Axis)((Object)state.getValue(field_176550_a)));
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, field_176550_a);
    }

    public static class Size {
        private final World field_150867_a;
        private final EnumFacing.Axis field_150865_b;
        private final EnumFacing field_150866_c;
        private final EnumFacing field_150863_d;
        private int field_150864_e = 0;
        private BlockPos field_150861_f;
        private int field_150862_g;
        private int field_150868_h;
        private static final String __OBFID = "CL_00000285";

        public Size(World worldIn, BlockPos p_i45694_2_, EnumFacing.Axis p_i45694_3_) {
            this.field_150867_a = worldIn;
            this.field_150865_b = p_i45694_3_;
            if (p_i45694_3_ == EnumFacing.Axis.X) {
                this.field_150863_d = EnumFacing.EAST;
                this.field_150866_c = EnumFacing.WEST;
            } else {
                this.field_150863_d = EnumFacing.NORTH;
                this.field_150866_c = EnumFacing.SOUTH;
            }
            BlockPos var4 = p_i45694_2_;
            while (p_i45694_2_.getY() > var4.getY() - 21 && p_i45694_2_.getY() > 0 && this.func_150857_a(worldIn.getBlockState(p_i45694_2_.offsetDown()).getBlock())) {
                p_i45694_2_ = p_i45694_2_.offsetDown();
            }
            int var5 = this.func_180120_a(p_i45694_2_, this.field_150863_d) - 1;
            if (var5 >= 0) {
                this.field_150861_f = p_i45694_2_.offset(this.field_150863_d, var5);
                this.field_150868_h = this.func_180120_a(this.field_150861_f, this.field_150866_c);
                if (this.field_150868_h < 2 || this.field_150868_h > 21) {
                    this.field_150861_f = null;
                    this.field_150868_h = 0;
                }
            }
            if (this.field_150861_f != null) {
                this.field_150862_g = this.func_150858_a();
            }
        }

        protected int func_180120_a(BlockPos p_180120_1_, EnumFacing p_180120_2_) {
            Block var5;
            int var3;
            for (var3 = 0; var3 < 22; ++var3) {
                BlockPos var4 = p_180120_1_.offset(p_180120_2_, var3);
                if (!this.func_150857_a(this.field_150867_a.getBlockState(var4).getBlock()) || this.field_150867_a.getBlockState(var4.offsetDown()).getBlock() != Blocks.obsidian) break;
            }
            return (var5 = this.field_150867_a.getBlockState(p_180120_1_.offset(p_180120_2_, var3)).getBlock()) == Blocks.obsidian ? var3 : 0;
        }

        protected int func_150858_a() {
            int var1;
            this.field_150862_g = 0;
            block0: while (this.field_150862_g < 21) {
                for (var1 = 0; var1 < this.field_150868_h; ++var1) {
                    BlockPos var2 = this.field_150861_f.offset(this.field_150866_c, var1).offsetUp(this.field_150862_g);
                    Block var3 = this.field_150867_a.getBlockState(var2).getBlock();
                    if (!this.func_150857_a(var3)) break block0;
                    if (var3 == Blocks.portal) {
                        ++this.field_150864_e;
                    }
                    if (var1 == 0 ? (var3 = this.field_150867_a.getBlockState(var2.offset(this.field_150863_d)).getBlock()) != Blocks.obsidian : var1 == this.field_150868_h - 1 && (var3 = this.field_150867_a.getBlockState(var2.offset(this.field_150866_c)).getBlock()) != Blocks.obsidian) break block0;
                }
                ++this.field_150862_g;
            }
            for (var1 = 0; var1 < this.field_150868_h; ++var1) {
                if (this.field_150867_a.getBlockState(this.field_150861_f.offset(this.field_150866_c, var1).offsetUp(this.field_150862_g)).getBlock() == Blocks.obsidian) continue;
                this.field_150862_g = 0;
                break;
            }
            if (this.field_150862_g <= 21 && this.field_150862_g >= 3) {
                return this.field_150862_g;
            }
            this.field_150861_f = null;
            this.field_150868_h = 0;
            this.field_150862_g = 0;
            return 0;
        }

        protected boolean func_150857_a(Block p_150857_1_) {
            return p_150857_1_.blockMaterial == Material.air || p_150857_1_ == Blocks.fire || p_150857_1_ == Blocks.portal;
        }

        public boolean func_150860_b() {
            return this.field_150861_f != null && this.field_150868_h >= 2 && this.field_150868_h <= 21 && this.field_150862_g >= 3 && this.field_150862_g <= 21;
        }

        public void func_150859_c() {
            for (int var1 = 0; var1 < this.field_150868_h; ++var1) {
                BlockPos var2 = this.field_150861_f.offset(this.field_150866_c, var1);
                for (int var3 = 0; var3 < this.field_150862_g; ++var3) {
                    this.field_150867_a.setBlockState(var2.offsetUp(var3), Blocks.portal.getDefaultState().withProperty(field_176550_a, (Comparable)((Object)this.field_150865_b)), 2);
                }
            }
        }
    }
}

