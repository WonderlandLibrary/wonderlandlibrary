// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import net.minecraft.entity.Entity;
import com.google.common.base.Predicate;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.world.IBlockAccess;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyBool;

public class BlockRedstoneComparator extends BlockRedstoneDiode implements ITileEntityProvider
{
    public static final PropertyBool field_176464_a;
    public static final PropertyEnum field_176463_b;
    private static final String __OBFID = "CL_00000220";
    
    public BlockRedstoneComparator(final boolean p_i45399_1_) {
        super(p_i45399_1_);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockDirectional.AGE, EnumFacing.NORTH).withProperty(BlockRedstoneComparator.field_176464_a, false).withProperty(BlockRedstoneComparator.field_176463_b, Mode.COMPARE));
        this.isBlockContainer = true;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.comparator;
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Items.comparator;
    }
    
    @Override
    protected int func_176403_d(final IBlockState p_176403_1_) {
        return 2;
    }
    
    @Override
    protected IBlockState func_180674_e(final IBlockState p_180674_1_) {
        final Boolean var2 = (Boolean)p_180674_1_.getValue(BlockRedstoneComparator.field_176464_a);
        final Mode var3 = (Mode)p_180674_1_.getValue(BlockRedstoneComparator.field_176463_b);
        final EnumFacing var4 = (EnumFacing)p_180674_1_.getValue(BlockDirectional.AGE);
        return Blocks.powered_comparator.getDefaultState().withProperty(BlockDirectional.AGE, var4).withProperty(BlockRedstoneComparator.field_176464_a, var2).withProperty(BlockRedstoneComparator.field_176463_b, var3);
    }
    
    @Override
    protected IBlockState func_180675_k(final IBlockState p_180675_1_) {
        final Boolean var2 = (Boolean)p_180675_1_.getValue(BlockRedstoneComparator.field_176464_a);
        final Mode var3 = (Mode)p_180675_1_.getValue(BlockRedstoneComparator.field_176463_b);
        final EnumFacing var4 = (EnumFacing)p_180675_1_.getValue(BlockDirectional.AGE);
        return Blocks.unpowered_comparator.getDefaultState().withProperty(BlockDirectional.AGE, var4).withProperty(BlockRedstoneComparator.field_176464_a, var2).withProperty(BlockRedstoneComparator.field_176463_b, var3);
    }
    
    @Override
    protected boolean func_176406_l(final IBlockState p_176406_1_) {
        return this.isRepeaterPowered || (boolean)p_176406_1_.getValue(BlockRedstoneComparator.field_176464_a);
    }
    
    @Override
    protected int func_176408_a(final IBlockAccess p_176408_1_, final BlockPos p_176408_2_, final IBlockState p_176408_3_) {
        final TileEntity var4 = p_176408_1_.getTileEntity(p_176408_2_);
        return (var4 instanceof TileEntityComparator) ? ((TileEntityComparator)var4).getOutputSignal() : 0;
    }
    
    private int func_176460_j(final World worldIn, final BlockPos p_176460_2_, final IBlockState p_176460_3_) {
        return (p_176460_3_.getValue(BlockRedstoneComparator.field_176463_b) == Mode.SUBTRACT) ? Math.max(this.func_176397_f(worldIn, p_176460_2_, p_176460_3_) - this.func_176407_c(worldIn, p_176460_2_, p_176460_3_), 0) : this.func_176397_f(worldIn, p_176460_2_, p_176460_3_);
    }
    
    @Override
    protected boolean func_176404_e(final World worldIn, final BlockPos p_176404_2_, final IBlockState p_176404_3_) {
        final int var4 = this.func_176397_f(worldIn, p_176404_2_, p_176404_3_);
        if (var4 >= 15) {
            return true;
        }
        if (var4 == 0) {
            return false;
        }
        final int var5 = this.func_176407_c(worldIn, p_176404_2_, p_176404_3_);
        return var5 == 0 || var4 >= var5;
    }
    
    @Override
    protected int func_176397_f(final World worldIn, final BlockPos p_176397_2_, final IBlockState p_176397_3_) {
        int var4 = super.func_176397_f(worldIn, p_176397_2_, p_176397_3_);
        final EnumFacing var5 = (EnumFacing)p_176397_3_.getValue(BlockDirectional.AGE);
        BlockPos var6 = p_176397_2_.offset(var5);
        Block var7 = worldIn.getBlockState(var6).getBlock();
        if (var7.hasComparatorInputOverride()) {
            var4 = var7.getComparatorInputOverride(worldIn, var6);
        }
        else if (var4 < 15 && var7.isNormalCube()) {
            var6 = var6.offset(var5);
            var7 = worldIn.getBlockState(var6).getBlock();
            if (var7.hasComparatorInputOverride()) {
                var4 = var7.getComparatorInputOverride(worldIn, var6);
            }
            else if (var7.getMaterial() == Material.air) {
                final EntityItemFrame var8 = this.func_176461_a(worldIn, var5, var6);
                if (var8 != null) {
                    var4 = var8.func_174866_q();
                }
            }
        }
        return var4;
    }
    
    private EntityItemFrame func_176461_a(final World worldIn, final EnumFacing p_176461_2_, final BlockPos p_176461_3_) {
        final List var4 = worldIn.func_175647_a(EntityItemFrame.class, new AxisAlignedBB(p_176461_3_.getX(), p_176461_3_.getY(), p_176461_3_.getZ(), p_176461_3_.getX() + 1, p_176461_3_.getY() + 1, p_176461_3_.getZ() + 1), (Predicate)new Predicate() {
            private static final String __OBFID = "CL_00002129";
            
            public boolean func_180416_a(final Entity p_180416_1_) {
                return p_180416_1_ != null && p_180416_1_.func_174811_aO() == p_176461_2_;
            }
            
            public boolean apply(final Object p_apply_1_) {
                return this.func_180416_a((Entity)p_apply_1_);
            }
        });
        return (var4.size() == 1) ? var4.get(0) : null;
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (!playerIn.capabilities.allowEdit) {
            return false;
        }
        state = state.cycleProperty(BlockRedstoneComparator.field_176463_b);
        worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "random.click", 0.3f, (state.getValue(BlockRedstoneComparator.field_176463_b) == Mode.SUBTRACT) ? 0.55f : 0.5f);
        worldIn.setBlockState(pos, state, 2);
        this.func_176462_k(worldIn, pos, state);
        return true;
    }
    
    @Override
    protected void func_176398_g(final World worldIn, final BlockPos p_176398_2_, final IBlockState p_176398_3_) {
        if (!worldIn.isBlockTickPending(p_176398_2_, this)) {
            final int var4 = this.func_176460_j(worldIn, p_176398_2_, p_176398_3_);
            final TileEntity var5 = worldIn.getTileEntity(p_176398_2_);
            final int var6 = (var5 instanceof TileEntityComparator) ? ((TileEntityComparator)var5).getOutputSignal() : 0;
            if (var4 != var6 || this.func_176406_l(p_176398_3_) != this.func_176404_e(worldIn, p_176398_2_, p_176398_3_)) {
                if (this.func_176402_i(worldIn, p_176398_2_, p_176398_3_)) {
                    worldIn.func_175654_a(p_176398_2_, this, 2, -1);
                }
                else {
                    worldIn.func_175654_a(p_176398_2_, this, 2, 0);
                }
            }
        }
    }
    
    private void func_176462_k(final World worldIn, final BlockPos p_176462_2_, final IBlockState p_176462_3_) {
        final int var4 = this.func_176460_j(worldIn, p_176462_2_, p_176462_3_);
        final TileEntity var5 = worldIn.getTileEntity(p_176462_2_);
        int var6 = 0;
        if (var5 instanceof TileEntityComparator) {
            final TileEntityComparator var7 = (TileEntityComparator)var5;
            var6 = var7.getOutputSignal();
            var7.setOutputSignal(var4);
        }
        if (var6 != var4 || p_176462_3_.getValue(BlockRedstoneComparator.field_176463_b) == Mode.COMPARE) {
            final boolean var8 = this.func_176404_e(worldIn, p_176462_2_, p_176462_3_);
            final boolean var9 = this.func_176406_l(p_176462_3_);
            if (var9 && !var8) {
                worldIn.setBlockState(p_176462_2_, p_176462_3_.withProperty(BlockRedstoneComparator.field_176464_a, false), 2);
            }
            else if (!var9 && var8) {
                worldIn.setBlockState(p_176462_2_, p_176462_3_.withProperty(BlockRedstoneComparator.field_176464_a, true), 2);
            }
            this.func_176400_h(worldIn, p_176462_2_, p_176462_3_);
        }
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (this.isRepeaterPowered) {
            worldIn.setBlockState(pos, this.func_180675_k(state).withProperty(BlockRedstoneComparator.field_176464_a, true), 4);
        }
        this.func_176462_k(worldIn, pos, state);
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        worldIn.setTileEntity(pos, this.createNewTileEntity(worldIn, 0));
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
        this.func_176400_h(worldIn, pos, state);
    }
    
    @Override
    public boolean onBlockEventReceived(final World worldIn, final BlockPos pos, final IBlockState state, final int eventID, final int eventParam) {
        super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);
        final TileEntity var6 = worldIn.getTileEntity(pos);
        return var6 != null && var6.receiveClientEvent(eventID, eventParam);
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityComparator();
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockDirectional.AGE, EnumFacing.getHorizontal(meta)).withProperty(BlockRedstoneComparator.field_176464_a, (meta & 0x8) > 0).withProperty(BlockRedstoneComparator.field_176463_b, ((meta & 0x4) > 0) ? Mode.SUBTRACT : Mode.COMPARE);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        final byte var2 = 0;
        int var3 = var2 | ((EnumFacing)state.getValue(BlockDirectional.AGE)).getHorizontalIndex();
        if (state.getValue(BlockRedstoneComparator.field_176464_a)) {
            var3 |= 0x8;
        }
        if (state.getValue(BlockRedstoneComparator.field_176463_b) == Mode.SUBTRACT) {
            var3 |= 0x4;
        }
        return var3;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockDirectional.AGE, BlockRedstoneComparator.field_176463_b, BlockRedstoneComparator.field_176464_a });
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty(BlockDirectional.AGE, placer.func_174811_aO().getOpposite()).withProperty(BlockRedstoneComparator.field_176464_a, false).withProperty(BlockRedstoneComparator.field_176463_b, Mode.COMPARE);
    }
    
    static {
        field_176464_a = PropertyBool.create("powered");
        field_176463_b = PropertyEnum.create("mode", Mode.class);
    }
    
    public enum Mode implements IStringSerializable
    {
        COMPARE("COMPARE", 0, "compare"), 
        SUBTRACT("SUBTRACT", 1, "subtract");
        
        private final String field_177041_c;
        private static final Mode[] $VALUES;
        private static final String __OBFID = "CL_00002128";
        
        private Mode(final String p_i45731_1_, final int p_i45731_2_, final String p_i45731_3_) {
            this.field_177041_c = p_i45731_3_;
        }
        
        @Override
        public String toString() {
            return this.field_177041_c;
        }
        
        @Override
        public String getName() {
            return this.field_177041_c;
        }
        
        static {
            $VALUES = new Mode[] { Mode.COMPARE, Mode.SUBTRACT };
        }
    }
}
