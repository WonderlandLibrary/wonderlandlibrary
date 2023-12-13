// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyBool;

public abstract class BlockStoneSlab extends BlockSlab
{
    public static final PropertyBool field_176555_b;
    public static final PropertyEnum field_176556_M;
    private static final String __OBFID = "CL_00000320";
    
    public BlockStoneSlab() {
        super(Material.rock);
        IBlockState var1 = this.blockState.getBaseState();
        if (this.isDouble()) {
            var1 = var1.withProperty(BlockStoneSlab.field_176555_b, false);
        }
        else {
            var1 = var1.withProperty(BlockSlab.HALF_PROP, EnumBlockHalf.BOTTOM);
        }
        this.setDefaultState(var1.withProperty(BlockStoneSlab.field_176556_M, EnumType.STONE));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(Blocks.stone_slab);
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Item.getItemFromBlock(Blocks.stone_slab);
    }
    
    @Override
    public String getFullSlabName(final int p_150002_1_) {
        return super.getUnlocalizedName() + "." + EnumType.func_176625_a(p_150002_1_).func_176627_c();
    }
    
    @Override
    public IProperty func_176551_l() {
        return BlockStoneSlab.field_176556_M;
    }
    
    @Override
    public Object func_176553_a(final ItemStack p_176553_1_) {
        return EnumType.func_176625_a(p_176553_1_.getMetadata() & 0x7);
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List list) {
        if (itemIn != Item.getItemFromBlock(Blocks.double_stone_slab)) {
            for (final EnumType var7 : EnumType.values()) {
                if (var7 != EnumType.WOOD) {
                    list.add(new ItemStack(itemIn, 1, var7.func_176624_a()));
                }
            }
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        IBlockState var2 = this.getDefaultState().withProperty(BlockStoneSlab.field_176556_M, EnumType.func_176625_a(meta & 0x7));
        if (this.isDouble()) {
            var2 = var2.withProperty(BlockStoneSlab.field_176555_b, (meta & 0x8) != 0x0);
        }
        else {
            var2 = var2.withProperty(BlockSlab.HALF_PROP, ((meta & 0x8) == 0x0) ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
        }
        return var2;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        final byte var2 = 0;
        int var3 = var2 | ((EnumType)state.getValue(BlockStoneSlab.field_176556_M)).func_176624_a();
        if (this.isDouble()) {
            if (state.getValue(BlockStoneSlab.field_176555_b)) {
                var3 |= 0x8;
            }
        }
        else if (state.getValue(BlockSlab.HALF_PROP) == EnumBlockHalf.TOP) {
            var3 |= 0x8;
        }
        return var3;
    }
    
    @Override
    protected BlockState createBlockState() {
        return this.isDouble() ? new BlockState(this, new IProperty[] { BlockStoneSlab.field_176555_b, BlockStoneSlab.field_176556_M }) : new BlockState(this, new IProperty[] { BlockSlab.HALF_PROP, BlockStoneSlab.field_176556_M });
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return ((EnumType)state.getValue(BlockStoneSlab.field_176556_M)).func_176624_a();
    }
    
    static {
        field_176555_b = PropertyBool.create("seamless");
        field_176556_M = PropertyEnum.create("variant", EnumType.class);
    }
    
    public enum EnumType implements IStringSerializable
    {
        STONE("STONE", 0, 0, "stone"), 
        SAND("SAND", 1, 1, "sandstone", "sand"), 
        WOOD("WOOD", 2, 2, "wood_old", "wood"), 
        COBBLESTONE("COBBLESTONE", 3, 3, "cobblestone", "cobble"), 
        BRICK("BRICK", 4, 4, "brick"), 
        SMOOTHBRICK("SMOOTHBRICK", 5, 5, "stone_brick", "smoothStoneBrick"), 
        NETHERBRICK("NETHERBRICK", 6, 6, "nether_brick", "netherBrick"), 
        QUARTZ("QUARTZ", 7, 7, "quartz");
        
        private static final EnumType[] field_176640_i;
        private final int field_176637_j;
        private final String field_176638_k;
        private final String field_176635_l;
        private static final EnumType[] $VALUES;
        private static final String __OBFID = "CL_00002056";
        
        private EnumType(final String p_i45677_1_, final int p_i45677_2_, final int p_i45677_3_, final String p_i45677_4_) {
            this(p_i45677_1_, p_i45677_2_, p_i45677_3_, p_i45677_4_, p_i45677_4_);
        }
        
        private EnumType(final String p_i45678_1_, final int p_i45678_2_, final int p_i45678_3_, final String p_i45678_4_, final String p_i45678_5_) {
            this.field_176637_j = p_i45678_3_;
            this.field_176638_k = p_i45678_4_;
            this.field_176635_l = p_i45678_5_;
        }
        
        public int func_176624_a() {
            return this.field_176637_j;
        }
        
        @Override
        public String toString() {
            return this.field_176638_k;
        }
        
        public static EnumType func_176625_a(int p_176625_0_) {
            if (p_176625_0_ < 0 || p_176625_0_ >= EnumType.field_176640_i.length) {
                p_176625_0_ = 0;
            }
            return EnumType.field_176640_i[p_176625_0_];
        }
        
        @Override
        public String getName() {
            return this.field_176638_k;
        }
        
        public String func_176627_c() {
            return this.field_176635_l;
        }
        
        static {
            field_176640_i = new EnumType[values().length];
            $VALUES = new EnumType[] { EnumType.STONE, EnumType.SAND, EnumType.WOOD, EnumType.COBBLESTONE, EnumType.BRICK, EnumType.SMOOTHBRICK, EnumType.NETHERBRICK, EnumType.QUARTZ };
            for (final EnumType var4 : values()) {
                EnumType.field_176640_i[var4.func_176624_a()] = var4;
            }
        }
    }
}
