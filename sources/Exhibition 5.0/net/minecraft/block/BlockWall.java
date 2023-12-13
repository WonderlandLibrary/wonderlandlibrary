// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyBool;

public class BlockWall extends Block
{
    public static final PropertyBool field_176256_a;
    public static final PropertyBool field_176254_b;
    public static final PropertyBool field_176257_M;
    public static final PropertyBool field_176258_N;
    public static final PropertyBool field_176259_O;
    public static final PropertyEnum field_176255_P;
    private static final String __OBFID = "CL_00000331";
    
    public BlockWall(final Block p_i45435_1_) {
        super(p_i45435_1_.blockMaterial);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockWall.field_176256_a, false).withProperty(BlockWall.field_176254_b, false).withProperty(BlockWall.field_176257_M, false).withProperty(BlockWall.field_176258_N, false).withProperty(BlockWall.field_176259_O, false).withProperty(BlockWall.field_176255_P, EnumType.NORMAL));
        this.setHardness(p_i45435_1_.blockHardness);
        this.setResistance(p_i45435_1_.blockResistance / 3.0f);
        this.setStepSound(p_i45435_1_.stepSound);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public boolean isPassable(final IBlockAccess blockAccess, final BlockPos pos) {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        final boolean var3 = this.func_176253_e(access, pos.offsetNorth());
        final boolean var4 = this.func_176253_e(access, pos.offsetSouth());
        final boolean var5 = this.func_176253_e(access, pos.offsetWest());
        final boolean var6 = this.func_176253_e(access, pos.offsetEast());
        float var7 = 0.25f;
        float var8 = 0.75f;
        float var9 = 0.25f;
        float var10 = 0.75f;
        float var11 = 1.0f;
        if (var3) {
            var9 = 0.0f;
        }
        if (var4) {
            var10 = 1.0f;
        }
        if (var5) {
            var7 = 0.0f;
        }
        if (var6) {
            var8 = 1.0f;
        }
        if (var3 && var4 && !var5 && !var6) {
            var11 = 0.8125f;
            var7 = 0.3125f;
            var8 = 0.6875f;
        }
        else if (!var3 && !var4 && var5 && var6) {
            var11 = 0.8125f;
            var9 = 0.3125f;
            var10 = 0.6875f;
        }
        this.setBlockBounds(var7, 0.0f, var9, var8, var11, var10);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        this.maxY = 1.5;
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }
    
    public boolean func_176253_e(final IBlockAccess p_176253_1_, final BlockPos p_176253_2_) {
        final Block var3 = p_176253_1_.getBlockState(p_176253_2_).getBlock();
        return var3 != Blocks.barrier && (var3 == this || var3 instanceof BlockFenceGate || (var3.blockMaterial.isOpaque() && var3.isFullCube() && var3.blockMaterial != Material.gourd));
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List list) {
        for (final EnumType var7 : EnumType.values()) {
            list.add(new ItemStack(itemIn, 1, var7.func_176657_a()));
        }
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return ((EnumType)state.getValue(BlockWall.field_176255_P)).func_176657_a();
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return side != EnumFacing.DOWN || super.shouldSideBeRendered(worldIn, pos, side);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockWall.field_176255_P, EnumType.func_176660_a(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((EnumType)state.getValue(BlockWall.field_176255_P)).func_176657_a();
    }
    
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return state.withProperty(BlockWall.field_176256_a, !worldIn.isAirBlock(pos.offsetUp())).withProperty(BlockWall.field_176254_b, this.func_176253_e(worldIn, pos.offsetNorth())).withProperty(BlockWall.field_176257_M, this.func_176253_e(worldIn, pos.offsetEast())).withProperty(BlockWall.field_176258_N, this.func_176253_e(worldIn, pos.offsetSouth())).withProperty(BlockWall.field_176259_O, this.func_176253_e(worldIn, pos.offsetWest()));
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockWall.field_176256_a, BlockWall.field_176254_b, BlockWall.field_176257_M, BlockWall.field_176259_O, BlockWall.field_176258_N, BlockWall.field_176255_P });
    }
    
    static {
        field_176256_a = PropertyBool.create("up");
        field_176254_b = PropertyBool.create("north");
        field_176257_M = PropertyBool.create("east");
        field_176258_N = PropertyBool.create("south");
        field_176259_O = PropertyBool.create("west");
        field_176255_P = PropertyEnum.create("variant", EnumType.class);
    }
    
    public enum EnumType implements IStringSerializable
    {
        NORMAL("NORMAL", 0, 0, "cobblestone", "normal"), 
        MOSSY("MOSSY", 1, 1, "mossy_cobblestone", "mossy");
        
        private static final EnumType[] field_176666_c;
        private final int field_176663_d;
        private final String field_176664_e;
        private String field_176661_f;
        private static final EnumType[] $VALUES;
        private static final String __OBFID = "CL_00002048";
        
        private EnumType(final String p_i45673_1_, final int p_i45673_2_, final int p_i45673_3_, final String p_i45673_4_, final String p_i45673_5_) {
            this.field_176663_d = p_i45673_3_;
            this.field_176664_e = p_i45673_4_;
            this.field_176661_f = p_i45673_5_;
        }
        
        public int func_176657_a() {
            return this.field_176663_d;
        }
        
        @Override
        public String toString() {
            return this.field_176664_e;
        }
        
        public static EnumType func_176660_a(int p_176660_0_) {
            if (p_176660_0_ < 0 || p_176660_0_ >= EnumType.field_176666_c.length) {
                p_176660_0_ = 0;
            }
            return EnumType.field_176666_c[p_176660_0_];
        }
        
        @Override
        public String getName() {
            return this.field_176664_e;
        }
        
        public String func_176659_c() {
            return this.field_176661_f;
        }
        
        static {
            field_176666_c = new EnumType[values().length];
            $VALUES = new EnumType[] { EnumType.NORMAL, EnumType.MOSSY };
            for (final EnumType var4 : values()) {
                EnumType.field_176666_c[var4.func_176657_a()] = var4;
            }
        }
    }
}
