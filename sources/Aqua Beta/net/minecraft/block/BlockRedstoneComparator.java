package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneComparator
extends BlockRedstoneDiode
implements ITileEntityProvider {
    public static final PropertyBool POWERED = PropertyBool.create((String)"powered");
    public static final PropertyEnum<Mode> MODE = PropertyEnum.create((String)"mode", Mode.class);

    public BlockRedstoneComparator(boolean powered) {
        super(powered);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)false)).withProperty(MODE, (Comparable)Mode.COMPARE));
        this.isBlockContainer = true;
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal((String)"item.comparator.name");
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.comparator;
    }

    public Item getItem(World worldIn, BlockPos pos) {
        return Items.comparator;
    }

    protected int getDelay(IBlockState state) {
        return 2;
    }

    protected IBlockState getPoweredState(IBlockState unpoweredState) {
        Boolean obool = (Boolean)unpoweredState.getValue((IProperty)POWERED);
        Mode blockredstonecomparator$mode = (Mode)unpoweredState.getValue(MODE);
        EnumFacing enumfacing = (EnumFacing)unpoweredState.getValue((IProperty)FACING);
        return Blocks.powered_comparator.getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing).withProperty((IProperty)POWERED, (Comparable)obool).withProperty(MODE, (Comparable)blockredstonecomparator$mode);
    }

    protected IBlockState getUnpoweredState(IBlockState poweredState) {
        Boolean obool = (Boolean)poweredState.getValue((IProperty)POWERED);
        Mode blockredstonecomparator$mode = (Mode)poweredState.getValue(MODE);
        EnumFacing enumfacing = (EnumFacing)poweredState.getValue((IProperty)FACING);
        return Blocks.unpowered_comparator.getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing).withProperty((IProperty)POWERED, (Comparable)obool).withProperty(MODE, (Comparable)blockredstonecomparator$mode);
    }

    protected boolean isPowered(IBlockState state) {
        return this.isRepeaterPowered || (Boolean)state.getValue((IProperty)POWERED) != false;
    }

    protected int getActiveSignal(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity instanceof TileEntityComparator ? ((TileEntityComparator)tileentity).getOutputSignal() : 0;
    }

    private int calculateOutput(World worldIn, BlockPos pos, IBlockState state) {
        return state.getValue(MODE) == Mode.SUBTRACT ? Math.max((int)(this.calculateInputStrength(worldIn, pos, state) - this.getPowerOnSides((IBlockAccess)worldIn, pos, state)), (int)0) : this.calculateInputStrength(worldIn, pos, state);
    }

    protected boolean shouldBePowered(World worldIn, BlockPos pos, IBlockState state) {
        int i = this.calculateInputStrength(worldIn, pos, state);
        if (i >= 15) {
            return true;
        }
        if (i == 0) {
            return false;
        }
        int j = this.getPowerOnSides((IBlockAccess)worldIn, pos, state);
        return j == 0 ? true : i >= j;
    }

    protected int calculateInputStrength(World worldIn, BlockPos pos, IBlockState state) {
        int i = super.calculateInputStrength(worldIn, pos, state);
        EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
        BlockPos blockpos = pos.offset(enumfacing);
        Block block = worldIn.getBlockState(blockpos).getBlock();
        if (block.hasComparatorInputOverride()) {
            i = block.getComparatorInputOverride(worldIn, blockpos);
        } else if (i < 15 && block.isNormalCube()) {
            EntityItemFrame entityitemframe;
            block = worldIn.getBlockState(blockpos = blockpos.offset(enumfacing)).getBlock();
            if (block.hasComparatorInputOverride()) {
                i = block.getComparatorInputOverride(worldIn, blockpos);
            } else if (block.getMaterial() == Material.air && (entityitemframe = this.findItemFrame(worldIn, enumfacing, blockpos)) != null) {
                i = entityitemframe.func_174866_q();
            }
        }
        return i;
    }

    private EntityItemFrame findItemFrame(World worldIn, EnumFacing facing, BlockPos pos) {
        List list = worldIn.getEntitiesWithinAABB(EntityItemFrame.class, new AxisAlignedBB((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)(pos.getX() + 1), (double)(pos.getY() + 1), (double)(pos.getZ() + 1)), (Predicate)new /* Unavailable Anonymous Inner Class!! */);
        return list.size() == 1 ? (EntityItemFrame)list.get(0) : null;
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!playerIn.capabilities.allowEdit) {
            return false;
        }
        state = state.cycleProperty(MODE);
        worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, "random.click", 0.3f, state.getValue(MODE) == Mode.SUBTRACT ? 0.55f : 0.5f);
        worldIn.setBlockState(pos, state, 2);
        this.onStateChange(worldIn, pos, state);
        return true;
    }

    protected void updateState(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isBlockTickPending(pos, (Block)this)) {
            int j;
            int i = this.calculateOutput(worldIn, pos, state);
            TileEntity tileentity = worldIn.getTileEntity(pos);
            int n = j = tileentity instanceof TileEntityComparator ? ((TileEntityComparator)tileentity).getOutputSignal() : 0;
            if (i != j || this.isPowered(state) != this.shouldBePowered(worldIn, pos, state)) {
                if (this.isFacingTowardsRepeater(worldIn, pos, state)) {
                    worldIn.updateBlockTick(pos, (Block)this, 2, -1);
                } else {
                    worldIn.updateBlockTick(pos, (Block)this, 2, 0);
                }
            }
        }
    }

    private void onStateChange(World worldIn, BlockPos pos, IBlockState state) {
        int i = this.calculateOutput(worldIn, pos, state);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        int j = 0;
        if (tileentity instanceof TileEntityComparator) {
            TileEntityComparator tileentitycomparator = (TileEntityComparator)tileentity;
            j = tileentitycomparator.getOutputSignal();
            tileentitycomparator.setOutputSignal(i);
        }
        if (j != i || state.getValue(MODE) == Mode.COMPARE) {
            boolean flag1 = this.shouldBePowered(worldIn, pos, state);
            boolean flag = this.isPowered(state);
            if (flag && !flag1) {
                worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)false)), 2);
            } else if (!flag && flag1) {
                worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)true)), 2);
            }
            this.notifyNeighbors(worldIn, pos, state);
        }
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (this.isRepeaterPowered) {
            worldIn.setBlockState(pos, this.getUnpoweredState(state).withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)true)), 4);
        }
        this.onStateChange(worldIn, pos, state);
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        worldIn.setTileEntity(pos, this.createNewTileEntity(worldIn, 0));
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
        this.notifyNeighbors(worldIn, pos, state);
    }

    public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam) {
        super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity == null ? false : tileentity.receiveClientEvent(eventID, eventParam);
    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityComparator();
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.getHorizontal((int)meta)).withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf(((meta & 8) > 0 ? 1 : 0) != 0)).withProperty(MODE, (Comparable)((meta & 4) > 0 ? Mode.SUBTRACT : Mode.COMPARE));
    }

    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= ((EnumFacing)state.getValue((IProperty)FACING)).getHorizontalIndex();
        if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
            i |= 8;
        }
        if (state.getValue(MODE) == Mode.SUBTRACT) {
            i |= 4;
        }
        return i;
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{FACING, MODE, POWERED});
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty)FACING, (Comparable)placer.getHorizontalFacing().getOpposite()).withProperty((IProperty)POWERED, (Comparable)Boolean.valueOf((boolean)false)).withProperty(MODE, (Comparable)Mode.COMPARE);
    }
}
