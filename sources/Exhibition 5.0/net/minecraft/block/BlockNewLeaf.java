// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.BlockState;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;

public class BlockNewLeaf extends BlockLeaves
{
    public static final PropertyEnum field_176240_P;
    private static final String __OBFID = "CL_00000276";
    
    public BlockNewLeaf() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockNewLeaf.field_176240_P, BlockPlanks.EnumType.ACACIA).withProperty(BlockLeaves.field_176236_b, true).withProperty(BlockLeaves.field_176237_a, true));
    }
    
    @Override
    protected void func_176234_a(final World worldIn, final BlockPos p_176234_2_, final IBlockState p_176234_3_, final int p_176234_4_) {
        if (p_176234_3_.getValue(BlockNewLeaf.field_176240_P) == BlockPlanks.EnumType.DARK_OAK && worldIn.rand.nextInt(p_176234_4_) == 0) {
            Block.spawnAsEntity(worldIn, p_176234_2_, new ItemStack(Items.apple, 1, 0));
        }
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return ((BlockPlanks.EnumType)state.getValue(BlockNewLeaf.field_176240_P)).func_176839_a();
    }
    
    @Override
    public int getDamageValue(final World worldIn, final BlockPos pos) {
        final IBlockState var3 = worldIn.getBlockState(pos);
        return var3.getBlock().getMetaFromState(var3) & 0x3;
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List list) {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
    }
    
    @Override
    protected ItemStack createStackedBlock(final IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType)state.getValue(BlockNewLeaf.field_176240_P)).func_176839_a() - 4);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockNewLeaf.field_176240_P, this.func_176233_b(meta)).withProperty(BlockLeaves.field_176237_a, (meta & 0x4) == 0x0).withProperty(BlockLeaves.field_176236_b, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        final byte var2 = 0;
        int var3 = var2 | ((BlockPlanks.EnumType)state.getValue(BlockNewLeaf.field_176240_P)).func_176839_a() - 4;
        if (!(boolean)state.getValue(BlockLeaves.field_176237_a)) {
            var3 |= 0x4;
        }
        if (state.getValue(BlockLeaves.field_176236_b)) {
            var3 |= 0x8;
        }
        return var3;
    }
    
    @Override
    public BlockPlanks.EnumType func_176233_b(final int p_176233_1_) {
        return BlockPlanks.EnumType.func_176837_a((p_176233_1_ & 0x3) + 4);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockNewLeaf.field_176240_P, BlockLeaves.field_176236_b, BlockLeaves.field_176237_a });
    }
    
    @Override
    public void harvestBlock(final World worldIn, final EntityPlayer playerIn, final BlockPos pos, final IBlockState state, final TileEntity te) {
        if (!worldIn.isRemote && playerIn.getCurrentEquippedItem() != null && playerIn.getCurrentEquippedItem().getItem() == Items.shears) {
            playerIn.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
            Block.spawnAsEntity(worldIn, pos, new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType)state.getValue(BlockNewLeaf.field_176240_P)).func_176839_a() - 4));
        }
        else {
            super.harvestBlock(worldIn, playerIn, pos, state, te);
        }
    }
    
    static {
        field_176240_P = PropertyEnum.create("variant", BlockPlanks.EnumType.class, (Predicate)new Predicate() {
            private static final String __OBFID = "CL_00002090";
            
            public boolean func_180195_a(final BlockPlanks.EnumType p_180195_1_) {
                return p_180195_1_.func_176839_a() >= 4;
            }
            
            public boolean apply(final Object p_apply_1_) {
                return this.func_180195_a((BlockPlanks.EnumType)p_apply_1_);
            }
        });
    }
}
