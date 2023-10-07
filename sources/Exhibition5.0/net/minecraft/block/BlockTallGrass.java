// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public class BlockTallGrass extends BlockBush implements IGrowable
{
    public static final PropertyEnum field_176497_a;
    private static final String __OBFID = "CL_00000321";
    
    protected BlockTallGrass() {
        super(Material.vine);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockTallGrass.field_176497_a, EnumType.DEAD_BUSH));
        final float var1 = 0.4f;
        this.setBlockBounds(0.5f - var1, 0.0f, 0.5f - var1, 0.5f + var1, 0.8f, 0.5f + var1);
    }
    
    @Override
    public int getBlockColor() {
        return ColorizerGrass.getGrassColor(0.5, 1.0);
    }
    
    @Override
    public boolean canBlockStay(final World worldIn, final BlockPos p_180671_2_, final IBlockState p_180671_3_) {
        return this.canPlaceBlockOn(worldIn.getBlockState(p_180671_2_.offsetDown()).getBlock());
    }
    
    @Override
    public boolean isReplaceable(final World worldIn, final BlockPos pos) {
        return true;
    }
    
    @Override
    public int getRenderColor(final IBlockState state) {
        if (state.getBlock() != this) {
            return super.getRenderColor(state);
        }
        final EnumType var2 = (EnumType)state.getValue(BlockTallGrass.field_176497_a);
        return (var2 == EnumType.DEAD_BUSH) ? 16777215 : ColorizerGrass.getGrassColor(0.5, 1.0);
    }
    
    @Override
    public int colorMultiplier(final IBlockAccess worldIn, final BlockPos pos, final int renderPass) {
        return worldIn.getBiomeGenForCoords(pos).func_180627_b(pos);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return (rand.nextInt(8) == 0) ? Items.wheat_seeds : null;
    }
    
    @Override
    public int quantityDroppedWithBonus(final int fortune, final Random random) {
        return 1 + random.nextInt(fortune * 2 + 1);
    }
    
    @Override
    public void harvestBlock(final World worldIn, final EntityPlayer playerIn, final BlockPos pos, final IBlockState state, final TileEntity te) {
        if (!worldIn.isRemote && playerIn.getCurrentEquippedItem() != null && playerIn.getCurrentEquippedItem().getItem() == Items.shears) {
            playerIn.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
            Block.spawnAsEntity(worldIn, pos, new ItemStack(Blocks.tallgrass, 1, ((EnumType)state.getValue(BlockTallGrass.field_176497_a)).func_177044_a()));
        }
        else {
            super.harvestBlock(worldIn, playerIn, pos, state, te);
        }
    }
    
    @Override
    public int getDamageValue(final World worldIn, final BlockPos pos) {
        final IBlockState var3 = worldIn.getBlockState(pos);
        return var3.getBlock().getMetaFromState(var3);
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List list) {
        for (int var4 = 1; var4 < 3; ++var4) {
            list.add(new ItemStack(itemIn, 1, var4));
        }
    }
    
    @Override
    public boolean isStillGrowing(final World worldIn, final BlockPos p_176473_2_, final IBlockState p_176473_3_, final boolean p_176473_4_) {
        return p_176473_3_.getValue(BlockTallGrass.field_176497_a) != EnumType.DEAD_BUSH;
    }
    
    @Override
    public boolean canUseBonemeal(final World worldIn, final Random p_180670_2_, final BlockPos p_180670_3_, final IBlockState p_180670_4_) {
        return true;
    }
    
    @Override
    public void grow(final World worldIn, final Random p_176474_2_, final BlockPos p_176474_3_, final IBlockState p_176474_4_) {
        BlockDoublePlant.EnumPlantType var5 = BlockDoublePlant.EnumPlantType.GRASS;
        if (p_176474_4_.getValue(BlockTallGrass.field_176497_a) == EnumType.FERN) {
            var5 = BlockDoublePlant.EnumPlantType.FERN;
        }
        if (Blocks.double_plant.canPlaceBlockAt(worldIn, p_176474_3_)) {
            Blocks.double_plant.func_176491_a(worldIn, p_176474_3_, var5, 2);
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockTallGrass.field_176497_a, EnumType.func_177045_a(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((EnumType)state.getValue(BlockTallGrass.field_176497_a)).func_177044_a();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockTallGrass.field_176497_a });
    }
    
    @Override
    public EnumOffsetType getOffsetType() {
        return EnumOffsetType.XYZ;
    }
    
    static {
        field_176497_a = PropertyEnum.create("type", EnumType.class);
    }
    
    public enum EnumType implements IStringSerializable
    {
        DEAD_BUSH("DEAD_BUSH", 0, 0, "dead_bush"), 
        GRASS("GRASS", 1, 1, "tall_grass"), 
        FERN("FERN", 2, 2, "fern");
        
        private static final EnumType[] field_177048_d;
        private final int field_177049_e;
        private final String field_177046_f;
        private static final EnumType[] $VALUES;
        private static final String __OBFID = "CL_00002055";
        
        private EnumType(final String p_i45676_1_, final int p_i45676_2_, final int p_i45676_3_, final String p_i45676_4_) {
            this.field_177049_e = p_i45676_3_;
            this.field_177046_f = p_i45676_4_;
        }
        
        public int func_177044_a() {
            return this.field_177049_e;
        }
        
        @Override
        public String toString() {
            return this.field_177046_f;
        }
        
        public static EnumType func_177045_a(int p_177045_0_) {
            if (p_177045_0_ < 0 || p_177045_0_ >= EnumType.field_177048_d.length) {
                p_177045_0_ = 0;
            }
            return EnumType.field_177048_d[p_177045_0_];
        }
        
        @Override
        public String getName() {
            return this.field_177046_f;
        }
        
        static {
            field_177048_d = new EnumType[values().length];
            $VALUES = new EnumType[] { EnumType.DEAD_BUSH, EnumType.GRASS, EnumType.FERN };
            for (final EnumType var4 : values()) {
                EnumType.field_177048_d[var4.func_177044_a()] = var4;
            }
        }
    }
}
