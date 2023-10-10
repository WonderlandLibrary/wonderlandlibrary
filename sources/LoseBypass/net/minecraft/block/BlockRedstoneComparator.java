/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
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
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneComparator
extends BlockRedstoneDiode
implements ITileEntityProvider {
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyEnum<Mode> MODE = PropertyEnum.create("mode", Mode.class);

    public BlockRedstoneComparator(boolean powered) {
        super(powered);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false).withProperty(MODE, Mode.COMPARE));
        this.isBlockContainer = true;
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("item.comparator.name");
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.comparator;
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return Items.comparator;
    }

    @Override
    protected int getDelay(IBlockState state) {
        return 2;
    }

    @Override
    protected IBlockState getPoweredState(IBlockState unpoweredState) {
        Boolean obool = unpoweredState.getValue(POWERED);
        Mode blockredstonecomparator$mode = unpoweredState.getValue(MODE);
        EnumFacing enumfacing = unpoweredState.getValue(FACING);
        return Blocks.powered_comparator.getDefaultState().withProperty(FACING, enumfacing).withProperty(POWERED, obool).withProperty(MODE, blockredstonecomparator$mode);
    }

    @Override
    protected IBlockState getUnpoweredState(IBlockState poweredState) {
        Boolean obool = poweredState.getValue(POWERED);
        Mode blockredstonecomparator$mode = poweredState.getValue(MODE);
        EnumFacing enumfacing = poweredState.getValue(FACING);
        return Blocks.unpowered_comparator.getDefaultState().withProperty(FACING, enumfacing).withProperty(POWERED, obool).withProperty(MODE, blockredstonecomparator$mode);
    }

    @Override
    protected boolean isPowered(IBlockState state) {
        if (this.isRepeaterPowered) return true;
        if (state.getValue(POWERED) != false) return true;
        return false;
    }

    @Override
    protected int getActiveSignal(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (!(tileentity instanceof TileEntityComparator)) return 0;
        int n = ((TileEntityComparator)tileentity).getOutputSignal();
        return n;
    }

    private int calculateOutput(World worldIn, BlockPos pos, IBlockState state) {
        int n;
        if (state.getValue(MODE) == Mode.SUBTRACT) {
            n = Math.max(this.calculateInputStrength(worldIn, pos, state) - this.getPowerOnSides(worldIn, pos, state), 0);
            return n;
        }
        n = this.calculateInputStrength(worldIn, pos, state);
        return n;
    }

    @Override
    protected boolean shouldBePowered(World worldIn, BlockPos pos, IBlockState state) {
        int i = this.calculateInputStrength(worldIn, pos, state);
        if (i >= 15) {
            return true;
        }
        if (i == 0) {
            return false;
        }
        int j = this.getPowerOnSides(worldIn, pos, state);
        if (j == 0) {
            return true;
        }
        if (i < j) return false;
        return true;
    }

    @Override
    protected int calculateInputStrength(World worldIn, BlockPos pos, IBlockState state) {
        int i = super.calculateInputStrength(worldIn, pos, state);
        EnumFacing enumfacing = state.getValue(FACING);
        BlockPos blockpos = pos.offset(enumfacing);
        Block block = worldIn.getBlockState(blockpos).getBlock();
        if (block.hasComparatorInputOverride()) {
            return block.getComparatorInputOverride(worldIn, blockpos);
        }
        if (i >= 15) return i;
        if (!block.isNormalCube()) return i;
        block = worldIn.getBlockState(blockpos = blockpos.offset(enumfacing)).getBlock();
        if (block.hasComparatorInputOverride()) {
            return block.getComparatorInputOverride(worldIn, blockpos);
        }
        if (block.getMaterial() != Material.air) return i;
        EntityItemFrame entityitemframe = this.findItemFrame(worldIn, enumfacing, blockpos);
        if (entityitemframe == null) return i;
        return entityitemframe.func_174866_q();
    }

    private EntityItemFrame findItemFrame(World worldIn, final EnumFacing facing, BlockPos pos) {
        List<Entity> list = worldIn.getEntitiesWithinAABB(EntityItemFrame.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1), new Predicate<Entity>(){

            @Override
            public boolean apply(Entity p_apply_1_) {
                if (p_apply_1_ == null) return false;
                if (p_apply_1_.getHorizontalFacing() != facing) return false;
                return true;
            }
        });
        if (list.size() != 1) return null;
        EntityItemFrame entityItemFrame = (EntityItemFrame)list.get(0);
        return entityItemFrame;
    }

    @Override
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

    @Override
    protected void updateState(World worldIn, BlockPos pos, IBlockState state) {
        int j;
        if (worldIn.isBlockTickPending(pos, this)) return;
        int i = this.calculateOutput(worldIn, pos, state);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        int n = j = tileentity instanceof TileEntityComparator ? ((TileEntityComparator)tileentity).getOutputSignal() : 0;
        if (i == j) {
            if (this.isPowered(state) == this.shouldBePowered(worldIn, pos, state)) return;
        }
        if (this.isFacingTowardsRepeater(worldIn, pos, state)) {
            worldIn.updateBlockTick(pos, this, 2, -1);
            return;
        }
        worldIn.updateBlockTick(pos, this, 2, 0);
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
        if (j == i) {
            if (state.getValue(MODE) != Mode.COMPARE) return;
        }
        boolean flag1 = this.shouldBePowered(worldIn, pos, state);
        boolean flag = this.isPowered(state);
        if (flag && !flag1) {
            worldIn.setBlockState(pos, state.withProperty(POWERED, false), 2);
        } else if (!flag && flag1) {
            worldIn.setBlockState(pos, state.withProperty(POWERED, true), 2);
        }
        this.notifyNeighbors(worldIn, pos, state);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (this.isRepeaterPowered) {
            worldIn.setBlockState(pos, this.getUnpoweredState(state).withProperty(POWERED, true), 4);
        }
        this.onStateChange(worldIn, pos, state);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        worldIn.setTileEntity(pos, this.createNewTileEntity(worldIn, 0));
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
        this.notifyNeighbors(worldIn, pos, state);
    }

    @Override
    public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam) {
        super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity == null) {
            return false;
        }
        boolean bl = tileentity.receiveClientEvent(eventID, eventParam);
        return bl;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityComparator();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        Mode mode;
        IBlockState iBlockState = this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta)).withProperty(POWERED, (meta & 8) > 0);
        if ((meta & 4) > 0) {
            mode = Mode.SUBTRACT;
            return iBlockState.withProperty(MODE, mode);
        }
        mode = Mode.COMPARE;
        return iBlockState.withProperty(MODE, mode);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= state.getValue(FACING).getHorizontalIndex();
        if (state.getValue(POWERED).booleanValue()) {
            i |= 8;
        }
        if (state.getValue(MODE) != Mode.SUBTRACT) return i;
        i |= 4;
        return i;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, MODE, POWERED);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(POWERED, false).withProperty(MODE, Mode.COMPARE);
    }

    public static enum Mode implements IStringSerializable
    {
        COMPARE("compare"),
        SUBTRACT("subtract");

        private final String name;

        private Mode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}

