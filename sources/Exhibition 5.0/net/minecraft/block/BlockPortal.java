// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.world.IBlockAccess;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public class BlockPortal extends BlockBreakable
{
    public static final PropertyEnum field_176550_a;
    private static final String __OBFID = "CL_00000284";
    
    public BlockPortal() {
        super(Material.portal, false);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockPortal.field_176550_a, EnumFacing.Axis.X));
        this.setTickRandomly(true);
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        if (worldIn.provider.isSurfaceWorld() && worldIn.getGameRules().getGameRuleBooleanValue("doMobSpawning") && rand.nextInt(2000) < worldIn.getDifficulty().getDifficultyId()) {
            final int var5 = pos.getY();
            BlockPos var6;
            for (var6 = pos; !World.doesBlockHaveSolidTopSurface(worldIn, var6) && var6.getY() > 0; var6 = var6.offsetDown()) {}
            if (var5 > 0 && !worldIn.getBlockState(var6.offsetUp()).getBlock().isNormalCube()) {
                final Entity var7 = ItemMonsterPlacer.spawnCreature(worldIn, 57, var6.getX() + 0.5, var6.getY() + 1.1, var6.getZ() + 0.5);
                if (var7 != null) {
                    var7.timeUntilPortal = var7.getPortalCooldown();
                }
            }
        }
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return null;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        final EnumFacing.Axis var3 = (EnumFacing.Axis)access.getBlockState(pos).getValue(BlockPortal.field_176550_a);
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
    
    public static int func_176549_a(final EnumFacing.Axis p_176549_0_) {
        return (p_176549_0_ == EnumFacing.Axis.X) ? 1 : ((p_176549_0_ == EnumFacing.Axis.Z) ? 2 : 0);
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    public boolean func_176548_d(final World worldIn, final BlockPos p_176548_2_) {
        final Size var3 = new Size(worldIn, p_176548_2_, EnumFacing.Axis.X);
        if (var3.func_150860_b() && var3.field_150864_e == 0) {
            var3.func_150859_c();
            return true;
        }
        final Size var4 = new Size(worldIn, p_176548_2_, EnumFacing.Axis.Z);
        if (var4.func_150860_b() && var4.field_150864_e == 0) {
            var4.func_150859_c();
            return true;
        }
        return false;
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        final EnumFacing.Axis var5 = (EnumFacing.Axis)state.getValue(BlockPortal.field_176550_a);
        if (var5 == EnumFacing.Axis.X) {
            final Size var6 = new Size(worldIn, pos, EnumFacing.Axis.X);
            if (!var6.func_150860_b() || var6.field_150864_e < var6.field_150868_h * var6.field_150862_g) {
                worldIn.setBlockState(pos, Blocks.air.getDefaultState());
            }
        }
        else if (var5 == EnumFacing.Axis.Z) {
            final Size var6 = new Size(worldIn, pos, EnumFacing.Axis.Z);
            if (!var6.func_150860_b() || var6.field_150864_e < var6.field_150868_h * var6.field_150862_g) {
                worldIn.setBlockState(pos, Blocks.air.getDefaultState());
            }
        }
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        EnumFacing.Axis var4 = null;
        final IBlockState var5 = worldIn.getBlockState(pos);
        if (worldIn.getBlockState(pos).getBlock() == this) {
            var4 = (EnumFacing.Axis)var5.getValue(BlockPortal.field_176550_a);
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
        final boolean var6 = worldIn.getBlockState(pos.offsetWest()).getBlock() == this && worldIn.getBlockState(pos.offsetWest(2)).getBlock() != this;
        final boolean var7 = worldIn.getBlockState(pos.offsetEast()).getBlock() == this && worldIn.getBlockState(pos.offsetEast(2)).getBlock() != this;
        final boolean var8 = worldIn.getBlockState(pos.offsetNorth()).getBlock() == this && worldIn.getBlockState(pos.offsetNorth(2)).getBlock() != this;
        final boolean var9 = worldIn.getBlockState(pos.offsetSouth()).getBlock() == this && worldIn.getBlockState(pos.offsetSouth(2)).getBlock() != this;
        final boolean var10 = var6 || var7 || var4 == EnumFacing.Axis.X;
        final boolean var11 = var8 || var9 || var4 == EnumFacing.Axis.Z;
        return (var10 && side == EnumFacing.WEST) || (var10 && side == EnumFacing.EAST) || (var11 && side == EnumFacing.NORTH) || (var11 && side == EnumFacing.SOUTH);
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }
    
    @Override
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        if (entityIn.ridingEntity == null && entityIn.riddenByEntity == null) {
            entityIn.setInPortal();
        }
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (rand.nextInt(100) == 0) {
            worldIn.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "portal.portal", 0.5f, rand.nextFloat() * 0.4f + 0.8f, false);
        }
        for (int var5 = 0; var5 < 4; ++var5) {
            double var6 = pos.getX() + rand.nextFloat();
            final double var7 = pos.getY() + rand.nextFloat();
            double var8 = pos.getZ() + rand.nextFloat();
            double var9 = (rand.nextFloat() - 0.5) * 0.5;
            final double var10 = (rand.nextFloat() - 0.5) * 0.5;
            double var11 = (rand.nextFloat() - 0.5) * 0.5;
            final int var12 = rand.nextInt(2) * 2 - 1;
            if (worldIn.getBlockState(pos.offsetWest()).getBlock() != this && worldIn.getBlockState(pos.offsetEast()).getBlock() != this) {
                var6 = pos.getX() + 0.5 + 0.25 * var12;
                var9 = rand.nextFloat() * 2.0f * var12;
            }
            else {
                var8 = pos.getZ() + 0.5 + 0.25 * var12;
                var11 = rand.nextFloat() * 2.0f * var12;
            }
            worldIn.spawnParticle(EnumParticleTypes.PORTAL, var6, var7, var8, var9, var10, var11, new int[0]);
        }
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return null;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockPortal.field_176550_a, ((meta & 0x3) == 0x2) ? EnumFacing.Axis.Z : EnumFacing.Axis.X);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return func_176549_a((EnumFacing.Axis)state.getValue(BlockPortal.field_176550_a));
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockPortal.field_176550_a });
    }
    
    static {
        field_176550_a = PropertyEnum.create("axis", EnumFacing.Axis.class, EnumFacing.Axis.X, EnumFacing.Axis.Z);
    }
    
    public static class Size
    {
        private final World field_150867_a;
        private final EnumFacing.Axis field_150865_b;
        private final EnumFacing field_150866_c;
        private final EnumFacing field_150863_d;
        private int field_150864_e;
        private BlockPos field_150861_f;
        private int field_150862_g;
        private int field_150868_h;
        private static final String __OBFID = "CL_00000285";
        
        public Size(final World worldIn, BlockPos p_i45694_2_, final EnumFacing.Axis p_i45694_3_) {
            this.field_150864_e = 0;
            this.field_150867_a = worldIn;
            this.field_150865_b = p_i45694_3_;
            if (p_i45694_3_ == EnumFacing.Axis.X) {
                this.field_150863_d = EnumFacing.EAST;
                this.field_150866_c = EnumFacing.WEST;
            }
            else {
                this.field_150863_d = EnumFacing.NORTH;
                this.field_150866_c = EnumFacing.SOUTH;
            }
            for (BlockPos var4 = p_i45694_2_; p_i45694_2_.getY() > var4.getY() - 21 && p_i45694_2_.getY() > 0 && this.func_150857_a(worldIn.getBlockState(p_i45694_2_.offsetDown()).getBlock()); p_i45694_2_ = p_i45694_2_.offsetDown()) {}
            final int var5 = this.func_180120_a(p_i45694_2_, this.field_150863_d) - 1;
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
        
        protected int func_180120_a(final BlockPos p_180120_1_, final EnumFacing p_180120_2_) {
            int var3;
            for (var3 = 0; var3 < 22; ++var3) {
                final BlockPos var4 = p_180120_1_.offset(p_180120_2_, var3);
                if (!this.func_150857_a(this.field_150867_a.getBlockState(var4).getBlock())) {
                    break;
                }
                if (this.field_150867_a.getBlockState(var4.offsetDown()).getBlock() != Blocks.obsidian) {
                    break;
                }
            }
            final Block var5 = this.field_150867_a.getBlockState(p_180120_1_.offset(p_180120_2_, var3)).getBlock();
            return (var5 == Blocks.obsidian) ? var3 : 0;
        }
        
        protected int func_150858_a() {
            this.field_150862_g = 0;
        Label_0181:
            while (this.field_150862_g < 21) {
                for (int var1 = 0; var1 < this.field_150868_h; ++var1) {
                    final BlockPos var2 = this.field_150861_f.offset(this.field_150866_c, var1).offsetUp(this.field_150862_g);
                    Block var3 = this.field_150867_a.getBlockState(var2).getBlock();
                    if (!this.func_150857_a(var3)) {
                        break Label_0181;
                    }
                    if (var3 == Blocks.portal) {
                        ++this.field_150864_e;
                    }
                    if (var1 == 0) {
                        var3 = this.field_150867_a.getBlockState(var2.offset(this.field_150863_d)).getBlock();
                        if (var3 != Blocks.obsidian) {
                            break Label_0181;
                        }
                    }
                    else if (var1 == this.field_150868_h - 1) {
                        var3 = this.field_150867_a.getBlockState(var2.offset(this.field_150866_c)).getBlock();
                        if (var3 != Blocks.obsidian) {
                            break Label_0181;
                        }
                    }
                }
                ++this.field_150862_g;
            }
            for (int var1 = 0; var1 < this.field_150868_h; ++var1) {
                if (this.field_150867_a.getBlockState(this.field_150861_f.offset(this.field_150866_c, var1).offsetUp(this.field_150862_g)).getBlock() != Blocks.obsidian) {
                    this.field_150862_g = 0;
                    break;
                }
            }
            if (this.field_150862_g <= 21 && this.field_150862_g >= 3) {
                return this.field_150862_g;
            }
            this.field_150861_f = null;
            this.field_150868_h = 0;
            return this.field_150862_g = 0;
        }
        
        protected boolean func_150857_a(final Block p_150857_1_) {
            return p_150857_1_.blockMaterial == Material.air || p_150857_1_ == Blocks.fire || p_150857_1_ == Blocks.portal;
        }
        
        public boolean func_150860_b() {
            return this.field_150861_f != null && this.field_150868_h >= 2 && this.field_150868_h <= 21 && this.field_150862_g >= 3 && this.field_150862_g <= 21;
        }
        
        public void func_150859_c() {
            for (int var1 = 0; var1 < this.field_150868_h; ++var1) {
                final BlockPos var2 = this.field_150861_f.offset(this.field_150866_c, var1);
                for (int var3 = 0; var3 < this.field_150862_g; ++var3) {
                    this.field_150867_a.setBlockState(var2.offsetUp(var3), Blocks.portal.getDefaultState().withProperty(BlockPortal.field_176550_a, this.field_150865_b), 2);
                }
            }
        }
    }
}
