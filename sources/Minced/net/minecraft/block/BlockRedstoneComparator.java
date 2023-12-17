// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import com.google.common.base.Predicate;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.world.IBlockAccess;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyBool;

public class BlockRedstoneComparator extends BlockRedstoneDiode implements ITileEntityProvider
{
    public static final PropertyBool POWERED;
    public static final PropertyEnum<Mode> MODE;
    
    public BlockRedstoneComparator(final boolean powered) {
        super(powered);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockRedstoneComparator.FACING, EnumFacing.NORTH).withProperty((IProperty<Comparable>)BlockRedstoneComparator.POWERED, false).withProperty(BlockRedstoneComparator.MODE, Mode.COMPARE));
        this.hasTileEntity = true;
    }
    
    @Override
    public String getLocalizedName() {
        return I18n.translateToLocal("item.comparator.name");
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.COMPARATOR;
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(Items.COMPARATOR);
    }
    
    @Override
    protected int getDelay(final IBlockState state) {
        return 2;
    }
    
    @Override
    protected IBlockState getPoweredState(final IBlockState unpoweredState) {
        final Boolean obool = unpoweredState.getValue((IProperty<Boolean>)BlockRedstoneComparator.POWERED);
        final Mode blockredstonecomparator$mode = unpoweredState.getValue(BlockRedstoneComparator.MODE);
        final EnumFacing enumfacing = unpoweredState.getValue((IProperty<EnumFacing>)BlockRedstoneComparator.FACING);
        return Blocks.POWERED_COMPARATOR.getDefaultState().withProperty((IProperty<Comparable>)BlockRedstoneComparator.FACING, enumfacing).withProperty((IProperty<Comparable>)BlockRedstoneComparator.POWERED, obool).withProperty(BlockRedstoneComparator.MODE, blockredstonecomparator$mode);
    }
    
    @Override
    protected IBlockState getUnpoweredState(final IBlockState poweredState) {
        final Boolean obool = poweredState.getValue((IProperty<Boolean>)BlockRedstoneComparator.POWERED);
        final Mode blockredstonecomparator$mode = poweredState.getValue(BlockRedstoneComparator.MODE);
        final EnumFacing enumfacing = poweredState.getValue((IProperty<EnumFacing>)BlockRedstoneComparator.FACING);
        return Blocks.UNPOWERED_COMPARATOR.getDefaultState().withProperty((IProperty<Comparable>)BlockRedstoneComparator.FACING, enumfacing).withProperty((IProperty<Comparable>)BlockRedstoneComparator.POWERED, obool).withProperty(BlockRedstoneComparator.MODE, blockredstonecomparator$mode);
    }
    
    @Override
    protected boolean isPowered(final IBlockState state) {
        return this.isRepeaterPowered || state.getValue((IProperty<Boolean>)BlockRedstoneComparator.POWERED);
    }
    
    @Override
    protected int getActiveSignal(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        return (tileentity instanceof TileEntityComparator) ? ((TileEntityComparator)tileentity).getOutputSignal() : 0;
    }
    
    private int calculateOutput(final World worldIn, final BlockPos pos, final IBlockState state) {
        return (state.getValue(BlockRedstoneComparator.MODE) == Mode.SUBTRACT) ? Math.max(this.calculateInputStrength(worldIn, pos, state) - this.getPowerOnSides(worldIn, pos, state), 0) : this.calculateInputStrength(worldIn, pos, state);
    }
    
    @Override
    protected boolean shouldBePowered(final World worldIn, final BlockPos pos, final IBlockState state) {
        final int i = this.calculateInputStrength(worldIn, pos, state);
        if (i >= 15) {
            return true;
        }
        if (i == 0) {
            return false;
        }
        final int j = this.getPowerOnSides(worldIn, pos, state);
        return j == 0 || i >= j;
    }
    
    @Override
    protected int calculateInputStrength(final World worldIn, final BlockPos pos, final IBlockState state) {
        int i = super.calculateInputStrength(worldIn, pos, state);
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockRedstoneComparator.FACING);
        BlockPos blockpos = pos.offset(enumfacing);
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        if (iblockstate.hasComparatorInputOverride()) {
            i = iblockstate.getComparatorInputOverride(worldIn, blockpos);
        }
        else if (i < 15 && iblockstate.isNormalCube()) {
            blockpos = blockpos.offset(enumfacing);
            iblockstate = worldIn.getBlockState(blockpos);
            if (iblockstate.hasComparatorInputOverride()) {
                i = iblockstate.getComparatorInputOverride(worldIn, blockpos);
            }
            else if (iblockstate.getMaterial() == Material.AIR) {
                final EntityItemFrame entityitemframe = this.findItemFrame(worldIn, enumfacing, blockpos);
                if (entityitemframe != null) {
                    i = entityitemframe.getAnalogOutput();
                }
            }
        }
        return i;
    }
    
    @Nullable
    private EntityItemFrame findItemFrame(final World worldIn, final EnumFacing facing, final BlockPos pos) {
        final List<EntityItemFrame> list = worldIn.getEntitiesWithinAABB((Class<? extends EntityItemFrame>)EntityItemFrame.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1), (com.google.common.base.Predicate<? super EntityItemFrame>)new Predicate<Entity>() {
            public boolean apply(@Nullable final Entity p_apply_1_) {
                return p_apply_1_ != null && p_apply_1_.getHorizontalFacing() == facing;
            }
        });
        return (list.size() == 1) ? list.get(0) : null;
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (!playerIn.capabilities.allowEdit) {
            return false;
        }
        state = state.cycleProperty(BlockRedstoneComparator.MODE);
        final float f = (state.getValue(BlockRedstoneComparator.MODE) == Mode.SUBTRACT) ? 0.55f : 0.5f;
        worldIn.playSound(playerIn, pos, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3f, f);
        worldIn.setBlockState(pos, state, 2);
        this.onStateChange(worldIn, pos, state);
        return true;
    }
    
    @Override
    protected void updateState(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!worldIn.isBlockTickPending(pos, this)) {
            final int i = this.calculateOutput(worldIn, pos, state);
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            final int j = (tileentity instanceof TileEntityComparator) ? ((TileEntityComparator)tileentity).getOutputSignal() : 0;
            if (i != j || this.isPowered(state) != this.shouldBePowered(worldIn, pos, state)) {
                if (this.isFacingTowardsRepeater(worldIn, pos, state)) {
                    worldIn.updateBlockTick(pos, this, 2, -1);
                }
                else {
                    worldIn.updateBlockTick(pos, this, 2, 0);
                }
            }
        }
    }
    
    private void onStateChange(final World worldIn, final BlockPos pos, final IBlockState state) {
        final int i = this.calculateOutput(worldIn, pos, state);
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        int j = 0;
        if (tileentity instanceof TileEntityComparator) {
            final TileEntityComparator tileentitycomparator = (TileEntityComparator)tileentity;
            j = tileentitycomparator.getOutputSignal();
            tileentitycomparator.setOutputSignal(i);
        }
        if (j != i || state.getValue(BlockRedstoneComparator.MODE) == Mode.COMPARE) {
            final boolean flag1 = this.shouldBePowered(worldIn, pos, state);
            final boolean flag2 = this.isPowered(state);
            if (flag2 && !flag1) {
                worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockRedstoneComparator.POWERED, false), 2);
            }
            else if (!flag2 && flag1) {
                worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockRedstoneComparator.POWERED, true), 2);
            }
            this.notifyNeighbors(worldIn, pos, state);
        }
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (this.isRepeaterPowered) {
            worldIn.setBlockState(pos, this.getUnpoweredState(state).withProperty((IProperty<Comparable>)BlockRedstoneComparator.POWERED, true), 4);
        }
        this.onStateChange(worldIn, pos, state);
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
        this.notifyNeighbors(worldIn, pos, state);
    }
    
    @Override
    @Deprecated
    public boolean eventReceived(final IBlockState state, final World worldIn, final BlockPos pos, final int id, final int param) {
        super.eventReceived(state, worldIn, pos, id, param);
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityComparator();
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockRedstoneComparator.FACING, EnumFacing.byHorizontalIndex(meta)).withProperty((IProperty<Comparable>)BlockRedstoneComparator.POWERED, (meta & 0x8) > 0).withProperty(BlockRedstoneComparator.MODE, ((meta & 0x4) > 0) ? Mode.SUBTRACT : Mode.COMPARE);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue((IProperty<EnumFacing>)BlockRedstoneComparator.FACING).getHorizontalIndex();
        if (state.getValue((IProperty<Boolean>)BlockRedstoneComparator.POWERED)) {
            i |= 0x8;
        }
        if (state.getValue(BlockRedstoneComparator.MODE) == Mode.SUBTRACT) {
            i |= 0x4;
        }
        return i;
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockRedstoneComparator.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockRedstoneComparator.FACING)));
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue((IProperty<EnumFacing>)BlockRedstoneComparator.FACING)));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockRedstoneComparator.FACING, BlockRedstoneComparator.MODE, BlockRedstoneComparator.POWERED });
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockRedstoneComparator.FACING, placer.getHorizontalFacing().getOpposite()).withProperty((IProperty<Comparable>)BlockRedstoneComparator.POWERED, false).withProperty(BlockRedstoneComparator.MODE, Mode.COMPARE);
    }
    
    static {
        POWERED = PropertyBool.create("powered");
        MODE = PropertyEnum.create("mode", Mode.class);
    }
    
    public enum Mode implements IStringSerializable
    {
        COMPARE("compare"), 
        SUBTRACT("subtract");
        
        private final String name;
        
        private Mode(final String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
    }
}
