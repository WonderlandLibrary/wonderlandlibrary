// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import java.util.Iterator;
import net.minecraft.util.EnumFacing;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.util.IStringSerializable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class BlockRailBase extends Block
{
    protected final boolean isPowered;
    private static final String __OBFID = "CL_00000195";
    
    public static boolean func_176562_d(final World worldIn, final BlockPos p_176562_1_) {
        return func_176563_d(worldIn.getBlockState(p_176562_1_));
    }
    
    public static boolean func_176563_d(final IBlockState p_176563_0_) {
        final Block var1 = p_176563_0_.getBlock();
        return var1 == Blocks.rail || var1 == Blocks.golden_rail || var1 == Blocks.detector_rail || var1 == Blocks.activator_rail;
    }
    
    protected BlockRailBase(final boolean p_i45389_1_) {
        super(Material.circuits);
        this.isPowered = p_i45389_1_;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
        this.setCreativeTab(CreativeTabs.tabTransport);
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
    public MovingObjectPosition collisionRayTrace(final World worldIn, final BlockPos pos, final Vec3 start, final Vec3 end) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.collisionRayTrace(worldIn, pos, start, end);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        final IBlockState var3 = access.getBlockState(pos);
        final EnumRailDirection var4 = (var3.getBlock() == this) ? ((EnumRailDirection)var3.getValue(this.func_176560_l())) : null;
        if (var4 != null && var4.func_177018_c()) {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.625f, 1.0f);
        }
        else {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
        }
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown());
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            state = this.func_176564_a(worldIn, pos, state, true);
            if (this.isPowered) {
                this.onNeighborBlockChange(worldIn, pos, state, this);
            }
        }
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!worldIn.isRemote) {
            final EnumRailDirection var5 = (EnumRailDirection)state.getValue(this.func_176560_l());
            boolean var6 = false;
            if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown())) {
                var6 = true;
            }
            if (var5 == EnumRailDirection.ASCENDING_EAST && !World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetEast())) {
                var6 = true;
            }
            else if (var5 == EnumRailDirection.ASCENDING_WEST && !World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetWest())) {
                var6 = true;
            }
            else if (var5 == EnumRailDirection.ASCENDING_NORTH && !World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetNorth())) {
                var6 = true;
            }
            else if (var5 == EnumRailDirection.ASCENDING_SOUTH && !World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetSouth())) {
                var6 = true;
            }
            if (var6) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
            else {
                this.func_176561_b(worldIn, pos, state, neighborBlock);
            }
        }
    }
    
    protected void func_176561_b(final World worldIn, final BlockPos p_176561_2_, final IBlockState p_176561_3_, final Block p_176561_4_) {
    }
    
    protected IBlockState func_176564_a(final World worldIn, final BlockPos p_176564_2_, final IBlockState p_176564_3_, final boolean p_176564_4_) {
        return worldIn.isRemote ? p_176564_3_ : new Rail(worldIn, p_176564_2_, p_176564_3_).func_180364_a(worldIn.isBlockPowered(p_176564_2_), p_176564_4_).func_180362_b();
    }
    
    @Override
    public int getMobilityFlag() {
        return 0;
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        if (((EnumRailDirection)state.getValue(this.func_176560_l())).func_177018_c()) {
            worldIn.notifyNeighborsOfStateChange(pos.offsetUp(), this);
        }
        if (this.isPowered) {
            worldIn.notifyNeighborsOfStateChange(pos, this);
            worldIn.notifyNeighborsOfStateChange(pos.offsetDown(), this);
        }
    }
    
    public abstract IProperty func_176560_l();
    
    public enum EnumRailDirection implements IStringSerializable
    {
        NORTH_SOUTH("NORTH_SOUTH", 0, 0, "north_south"), 
        EAST_WEST("EAST_WEST", 1, 1, "east_west"), 
        ASCENDING_EAST("ASCENDING_EAST", 2, 2, "ascending_east"), 
        ASCENDING_WEST("ASCENDING_WEST", 3, 3, "ascending_west"), 
        ASCENDING_NORTH("ASCENDING_NORTH", 4, 4, "ascending_north"), 
        ASCENDING_SOUTH("ASCENDING_SOUTH", 5, 5, "ascending_south"), 
        SOUTH_EAST("SOUTH_EAST", 6, 6, "south_east"), 
        SOUTH_WEST("SOUTH_WEST", 7, 7, "south_west"), 
        NORTH_WEST("NORTH_WEST", 8, 8, "north_west"), 
        NORTH_EAST("NORTH_EAST", 9, 9, "north_east");
        
        private static final EnumRailDirection[] field_177030_k;
        private final int field_177027_l;
        private final String field_177028_m;
        private static final EnumRailDirection[] $VALUES;
        private static final String __OBFID = "CL_00002137";
        
        private EnumRailDirection(final String p_i45738_1_, final int p_i45738_2_, final int p_i45738_3_, final String p_i45738_4_) {
            this.field_177027_l = p_i45738_3_;
            this.field_177028_m = p_i45738_4_;
        }
        
        public int func_177015_a() {
            return this.field_177027_l;
        }
        
        @Override
        public String toString() {
            return this.field_177028_m;
        }
        
        public boolean func_177018_c() {
            return this == EnumRailDirection.ASCENDING_NORTH || this == EnumRailDirection.ASCENDING_EAST || this == EnumRailDirection.ASCENDING_SOUTH || this == EnumRailDirection.ASCENDING_WEST;
        }
        
        public static EnumRailDirection func_177016_a(int p_177016_0_) {
            if (p_177016_0_ < 0 || p_177016_0_ >= EnumRailDirection.field_177030_k.length) {
                p_177016_0_ = 0;
            }
            return EnumRailDirection.field_177030_k[p_177016_0_];
        }
        
        @Override
        public String getName() {
            return this.field_177028_m;
        }
        
        static {
            field_177030_k = new EnumRailDirection[values().length];
            $VALUES = new EnumRailDirection[] { EnumRailDirection.NORTH_SOUTH, EnumRailDirection.EAST_WEST, EnumRailDirection.ASCENDING_EAST, EnumRailDirection.ASCENDING_WEST, EnumRailDirection.ASCENDING_NORTH, EnumRailDirection.ASCENDING_SOUTH, EnumRailDirection.SOUTH_EAST, EnumRailDirection.SOUTH_WEST, EnumRailDirection.NORTH_WEST, EnumRailDirection.NORTH_EAST };
            for (final EnumRailDirection var4 : values()) {
                EnumRailDirection.field_177030_k[var4.func_177015_a()] = var4;
            }
        }
    }
    
    public class Rail
    {
        private final World field_150660_b;
        private final BlockPos field_180367_c;
        private final BlockRailBase field_180365_d;
        private IBlockState field_180366_e;
        private final boolean field_150656_f;
        private final List field_150657_g;
        private static final String __OBFID = "CL_00000196";
        
        public Rail(final World worldIn, final BlockPos p_i45739_3_, final IBlockState p_i45739_4_) {
            this.field_150657_g = Lists.newArrayList();
            this.field_150660_b = worldIn;
            this.field_180367_c = p_i45739_3_;
            this.field_180366_e = p_i45739_4_;
            this.field_180365_d = (BlockRailBase)p_i45739_4_.getBlock();
            final EnumRailDirection var5 = (EnumRailDirection)p_i45739_4_.getValue(BlockRailBase.this.func_176560_l());
            this.field_150656_f = this.field_180365_d.isPowered;
            this.func_180360_a(var5);
        }
        
        private void func_180360_a(final EnumRailDirection p_180360_1_) {
            this.field_150657_g.clear();
            switch (SwitchEnumRailDirection.field_180371_a[p_180360_1_.ordinal()]) {
                case 1: {
                    this.field_150657_g.add(this.field_180367_c.offsetNorth());
                    this.field_150657_g.add(this.field_180367_c.offsetSouth());
                    break;
                }
                case 2: {
                    this.field_150657_g.add(this.field_180367_c.offsetWest());
                    this.field_150657_g.add(this.field_180367_c.offsetEast());
                    break;
                }
                case 3: {
                    this.field_150657_g.add(this.field_180367_c.offsetWest());
                    this.field_150657_g.add(this.field_180367_c.offsetEast().offsetUp());
                    break;
                }
                case 4: {
                    this.field_150657_g.add(this.field_180367_c.offsetWest().offsetUp());
                    this.field_150657_g.add(this.field_180367_c.offsetEast());
                    break;
                }
                case 5: {
                    this.field_150657_g.add(this.field_180367_c.offsetNorth().offsetUp());
                    this.field_150657_g.add(this.field_180367_c.offsetSouth());
                    break;
                }
                case 6: {
                    this.field_150657_g.add(this.field_180367_c.offsetNorth());
                    this.field_150657_g.add(this.field_180367_c.offsetSouth().offsetUp());
                    break;
                }
                case 7: {
                    this.field_150657_g.add(this.field_180367_c.offsetEast());
                    this.field_150657_g.add(this.field_180367_c.offsetSouth());
                    break;
                }
                case 8: {
                    this.field_150657_g.add(this.field_180367_c.offsetWest());
                    this.field_150657_g.add(this.field_180367_c.offsetSouth());
                    break;
                }
                case 9: {
                    this.field_150657_g.add(this.field_180367_c.offsetWest());
                    this.field_150657_g.add(this.field_180367_c.offsetNorth());
                    break;
                }
                case 10: {
                    this.field_150657_g.add(this.field_180367_c.offsetEast());
                    this.field_150657_g.add(this.field_180367_c.offsetNorth());
                    break;
                }
            }
        }
        
        private void func_150651_b() {
            for (int var1 = 0; var1 < this.field_150657_g.size(); ++var1) {
                final Rail var2 = this.func_180697_b(this.field_150657_g.get(var1));
                if (var2 != null && var2.func_150653_a(this)) {
                    this.field_150657_g.set(var1, var2.field_180367_c);
                }
                else {
                    this.field_150657_g.remove(var1--);
                }
            }
        }
        
        private boolean func_180359_a(final BlockPos p_180359_1_) {
            return BlockRailBase.func_176562_d(this.field_150660_b, p_180359_1_) || BlockRailBase.func_176562_d(this.field_150660_b, p_180359_1_.offsetUp()) || BlockRailBase.func_176562_d(this.field_150660_b, p_180359_1_.offsetDown());
        }
        
        private Rail func_180697_b(final BlockPos p_180697_1_) {
            IBlockState var3 = this.field_150660_b.getBlockState(p_180697_1_);
            if (BlockRailBase.func_176563_d(var3)) {
                return new Rail(this.field_150660_b, p_180697_1_, var3);
            }
            BlockPos var4 = p_180697_1_.offsetUp();
            var3 = this.field_150660_b.getBlockState(var4);
            if (BlockRailBase.func_176563_d(var3)) {
                return new Rail(this.field_150660_b, var4, var3);
            }
            var4 = p_180697_1_.offsetDown();
            var3 = this.field_150660_b.getBlockState(var4);
            return BlockRailBase.func_176563_d(var3) ? new Rail(this.field_150660_b, var4, var3) : null;
        }
        
        private boolean func_150653_a(final Rail p_150653_1_) {
            return this.func_180363_c(p_150653_1_.field_180367_c);
        }
        
        private boolean func_180363_c(final BlockPos p_180363_1_) {
            for (int var2 = 0; var2 < this.field_150657_g.size(); ++var2) {
                final BlockPos var3 = this.field_150657_g.get(var2);
                if (var3.getX() == p_180363_1_.getX() && var3.getZ() == p_180363_1_.getZ()) {
                    return true;
                }
            }
            return false;
        }
        
        protected int countAdjacentRails() {
            int var1 = 0;
            for (final EnumFacing var3 : EnumFacing.Plane.HORIZONTAL) {
                if (this.func_180359_a(this.field_180367_c.offset(var3))) {
                    ++var1;
                }
            }
            return var1;
        }
        
        private boolean func_150649_b(final Rail p_150649_1_) {
            return this.func_150653_a(p_150649_1_) || this.field_150657_g.size() != 2;
        }
        
        private void func_150645_c(final Rail p_150645_1_) {
            this.field_150657_g.add(p_150645_1_.field_180367_c);
            final BlockPos var2 = this.field_180367_c.offsetNorth();
            final BlockPos var3 = this.field_180367_c.offsetSouth();
            final BlockPos var4 = this.field_180367_c.offsetWest();
            final BlockPos var5 = this.field_180367_c.offsetEast();
            final boolean var6 = this.func_180363_c(var2);
            final boolean var7 = this.func_180363_c(var3);
            final boolean var8 = this.func_180363_c(var4);
            final boolean var9 = this.func_180363_c(var5);
            EnumRailDirection var10 = null;
            if (var6 || var7) {
                var10 = EnumRailDirection.NORTH_SOUTH;
            }
            if (var8 || var9) {
                var10 = EnumRailDirection.EAST_WEST;
            }
            if (!this.field_150656_f) {
                if (var7 && var9 && !var6 && !var8) {
                    var10 = EnumRailDirection.SOUTH_EAST;
                }
                if (var7 && var8 && !var6 && !var9) {
                    var10 = EnumRailDirection.SOUTH_WEST;
                }
                if (var6 && var8 && !var7 && !var9) {
                    var10 = EnumRailDirection.NORTH_WEST;
                }
                if (var6 && var9 && !var7 && !var8) {
                    var10 = EnumRailDirection.NORTH_EAST;
                }
            }
            if (var10 == EnumRailDirection.NORTH_SOUTH) {
                if (BlockRailBase.func_176562_d(this.field_150660_b, var2.offsetUp())) {
                    var10 = EnumRailDirection.ASCENDING_NORTH;
                }
                if (BlockRailBase.func_176562_d(this.field_150660_b, var3.offsetUp())) {
                    var10 = EnumRailDirection.ASCENDING_SOUTH;
                }
            }
            if (var10 == EnumRailDirection.EAST_WEST) {
                if (BlockRailBase.func_176562_d(this.field_150660_b, var5.offsetUp())) {
                    var10 = EnumRailDirection.ASCENDING_EAST;
                }
                if (BlockRailBase.func_176562_d(this.field_150660_b, var4.offsetUp())) {
                    var10 = EnumRailDirection.ASCENDING_WEST;
                }
            }
            if (var10 == null) {
                var10 = EnumRailDirection.NORTH_SOUTH;
            }
            this.field_180366_e = this.field_180366_e.withProperty(this.field_180365_d.func_176560_l(), var10);
            this.field_150660_b.setBlockState(this.field_180367_c, this.field_180366_e, 3);
        }
        
        private boolean func_180361_d(final BlockPos p_180361_1_) {
            final Rail var2 = this.func_180697_b(p_180361_1_);
            if (var2 == null) {
                return false;
            }
            var2.func_150651_b();
            return var2.func_150649_b(this);
        }
        
        public Rail func_180364_a(final boolean p_180364_1_, final boolean p_180364_2_) {
            final BlockPos var3 = this.field_180367_c.offsetNorth();
            final BlockPos var4 = this.field_180367_c.offsetSouth();
            final BlockPos var5 = this.field_180367_c.offsetWest();
            final BlockPos var6 = this.field_180367_c.offsetEast();
            final boolean var7 = this.func_180361_d(var3);
            final boolean var8 = this.func_180361_d(var4);
            final boolean var9 = this.func_180361_d(var5);
            final boolean var10 = this.func_180361_d(var6);
            EnumRailDirection var11 = null;
            if ((var7 || var8) && !var9 && !var10) {
                var11 = EnumRailDirection.NORTH_SOUTH;
            }
            if ((var9 || var10) && !var7 && !var8) {
                var11 = EnumRailDirection.EAST_WEST;
            }
            if (!this.field_150656_f) {
                if (var8 && var10 && !var7 && !var9) {
                    var11 = EnumRailDirection.SOUTH_EAST;
                }
                if (var8 && var9 && !var7 && !var10) {
                    var11 = EnumRailDirection.SOUTH_WEST;
                }
                if (var7 && var9 && !var8 && !var10) {
                    var11 = EnumRailDirection.NORTH_WEST;
                }
                if (var7 && var10 && !var8 && !var9) {
                    var11 = EnumRailDirection.NORTH_EAST;
                }
            }
            if (var11 == null) {
                if (var7 || var8) {
                    var11 = EnumRailDirection.NORTH_SOUTH;
                }
                if (var9 || var10) {
                    var11 = EnumRailDirection.EAST_WEST;
                }
                if (!this.field_150656_f) {
                    if (p_180364_1_) {
                        if (var8 && var10) {
                            var11 = EnumRailDirection.SOUTH_EAST;
                        }
                        if (var9 && var8) {
                            var11 = EnumRailDirection.SOUTH_WEST;
                        }
                        if (var10 && var7) {
                            var11 = EnumRailDirection.NORTH_EAST;
                        }
                        if (var7 && var9) {
                            var11 = EnumRailDirection.NORTH_WEST;
                        }
                    }
                    else {
                        if (var7 && var9) {
                            var11 = EnumRailDirection.NORTH_WEST;
                        }
                        if (var10 && var7) {
                            var11 = EnumRailDirection.NORTH_EAST;
                        }
                        if (var9 && var8) {
                            var11 = EnumRailDirection.SOUTH_WEST;
                        }
                        if (var8 && var10) {
                            var11 = EnumRailDirection.SOUTH_EAST;
                        }
                    }
                }
            }
            if (var11 == EnumRailDirection.NORTH_SOUTH) {
                if (BlockRailBase.func_176562_d(this.field_150660_b, var3.offsetUp())) {
                    var11 = EnumRailDirection.ASCENDING_NORTH;
                }
                if (BlockRailBase.func_176562_d(this.field_150660_b, var4.offsetUp())) {
                    var11 = EnumRailDirection.ASCENDING_SOUTH;
                }
            }
            if (var11 == EnumRailDirection.EAST_WEST) {
                if (BlockRailBase.func_176562_d(this.field_150660_b, var6.offsetUp())) {
                    var11 = EnumRailDirection.ASCENDING_EAST;
                }
                if (BlockRailBase.func_176562_d(this.field_150660_b, var5.offsetUp())) {
                    var11 = EnumRailDirection.ASCENDING_WEST;
                }
            }
            if (var11 == null) {
                var11 = EnumRailDirection.NORTH_SOUTH;
            }
            this.func_180360_a(var11);
            this.field_180366_e = this.field_180366_e.withProperty(this.field_180365_d.func_176560_l(), var11);
            if (p_180364_2_ || this.field_150660_b.getBlockState(this.field_180367_c) != this.field_180366_e) {
                this.field_150660_b.setBlockState(this.field_180367_c, this.field_180366_e, 3);
                for (int var12 = 0; var12 < this.field_150657_g.size(); ++var12) {
                    final Rail var13 = this.func_180697_b(this.field_150657_g.get(var12));
                    if (var13 != null) {
                        var13.func_150651_b();
                        if (var13.func_150649_b(this)) {
                            var13.func_150645_c(this);
                        }
                    }
                }
            }
            return this;
        }
        
        public IBlockState func_180362_b() {
            return this.field_180366_e;
        }
    }
    
    static final class SwitchEnumRailDirection
    {
        static final int[] field_180371_a;
        private static final String __OBFID = "CL_00002138";
        
        static {
            field_180371_a = new int[EnumRailDirection.values().length];
            try {
                SwitchEnumRailDirection.field_180371_a[EnumRailDirection.NORTH_SOUTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumRailDirection.field_180371_a[EnumRailDirection.EAST_WEST.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumRailDirection.field_180371_a[EnumRailDirection.ASCENDING_EAST.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumRailDirection.field_180371_a[EnumRailDirection.ASCENDING_WEST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                SwitchEnumRailDirection.field_180371_a[EnumRailDirection.ASCENDING_NORTH.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
            try {
                SwitchEnumRailDirection.field_180371_a[EnumRailDirection.ASCENDING_SOUTH.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError6) {}
            try {
                SwitchEnumRailDirection.field_180371_a[EnumRailDirection.SOUTH_EAST.ordinal()] = 7;
            }
            catch (NoSuchFieldError noSuchFieldError7) {}
            try {
                SwitchEnumRailDirection.field_180371_a[EnumRailDirection.SOUTH_WEST.ordinal()] = 8;
            }
            catch (NoSuchFieldError noSuchFieldError8) {}
            try {
                SwitchEnumRailDirection.field_180371_a[EnumRailDirection.NORTH_WEST.ordinal()] = 9;
            }
            catch (NoSuchFieldError noSuchFieldError9) {}
            try {
                SwitchEnumRailDirection.field_180371_a[EnumRailDirection.NORTH_EAST.ordinal()] = 10;
            }
            catch (NoSuchFieldError noSuchFieldError10) {}
        }
    }
}
