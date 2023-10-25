/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
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

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("item.comparator.name");
    }

    private int calculateOutput(World world, BlockPos blockPos, IBlockState iBlockState) {
        return iBlockState.getValue(MODE) == Mode.SUBTRACT ? Math.max(this.calculateInputStrength(world, blockPos, iBlockState) - this.getPowerOnSides(world, blockPos, iBlockState), 0) : this.calculateInputStrength(world, blockPos, iBlockState);
    }

    @Override
    protected int getDelay(IBlockState iBlockState) {
        return 2;
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState iBlockState) {
        super.breakBlock(world, blockPos, iBlockState);
        world.removeTileEntity(blockPos);
        this.notifyNeighbors(world, blockPos, iBlockState);
    }

    @Override
    public boolean onBlockEventReceived(World world, BlockPos blockPos, IBlockState iBlockState, int n, int n2) {
        super.onBlockEventReceived(world, blockPos, iBlockState, n, n2);
        TileEntity tileEntity = world.getTileEntity(blockPos);
        return tileEntity == null ? false : tileEntity.receiveClientEvent(n, n2);
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3, int n, EntityLivingBase entityLivingBase) {
        return this.getDefaultState().withProperty(FACING, entityLivingBase.getHorizontalFacing().getOpposite()).withProperty(POWERED, false).withProperty(MODE, Mode.COMPARE);
    }

    private EntityItemFrame findItemFrame(World world, final EnumFacing enumFacing, BlockPos blockPos) {
        List<Entity> list = world.getEntitiesWithinAABB(EntityItemFrame.class, new AxisAlignedBB(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1), new Predicate<Entity>(){

            public boolean apply(Entity entity) {
                return entity != null && entity.getHorizontalFacing() == enumFacing;
            }
        });
        return list.size() == 1 ? (EntityItemFrame)list.get(0) : null;
    }

    @Override
    protected IBlockState getUnpoweredState(IBlockState iBlockState) {
        Boolean bl = iBlockState.getValue(POWERED);
        Mode mode = iBlockState.getValue(MODE);
        EnumFacing enumFacing = iBlockState.getValue(FACING);
        return Blocks.unpowered_comparator.getDefaultState().withProperty(FACING, enumFacing).withProperty(POWERED, bl).withProperty(MODE, mode);
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        if (this.isRepeaterPowered) {
            world.setBlockState(blockPos, this.getUnpoweredState(iBlockState).withProperty(POWERED, true), 4);
        }
        this.onStateChange(world, blockPos, iBlockState);
    }

    public BlockRedstoneComparator(boolean bl) {
        super(bl);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false).withProperty(MODE, Mode.COMPARE));
        this.isBlockContainer = true;
    }

    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState iBlockState) {
        super.onBlockAdded(world, blockPos, iBlockState);
        world.setTileEntity(blockPos, this.createNewTileEntity(world, 0));
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(n)).withProperty(POWERED, (n & 8) > 0).withProperty(MODE, (n & 4) > 0 ? Mode.SUBTRACT : Mode.COMPARE);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumFacing enumFacing, float f, float f2, float f3) {
        if (!entityPlayer.capabilities.allowEdit) {
            return false;
        }
        iBlockState = iBlockState.cycleProperty(MODE);
        world.playSoundEffect((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5, "random.click", 0.3f, iBlockState.getValue(MODE) == Mode.SUBTRACT ? 0.55f : 0.5f);
        world.setBlockState(blockPos, iBlockState, 2);
        this.onStateChange(world, blockPos, iBlockState);
        return true;
    }

    @Override
    protected void updateState(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (!world.isBlockTickPending(blockPos, this)) {
            int n;
            int n2 = this.calculateOutput(world, blockPos, iBlockState);
            TileEntity tileEntity = world.getTileEntity(blockPos);
            int n3 = n = tileEntity instanceof TileEntityComparator ? ((TileEntityComparator)tileEntity).getOutputSignal() : 0;
            if (n2 != n || this.isPowered(iBlockState) != this.shouldBePowered(world, blockPos, iBlockState)) {
                if (this.isFacingTowardsRepeater(world, blockPos, iBlockState)) {
                    world.updateBlockTick(blockPos, this, 2, -1);
                } else {
                    world.updateBlockTick(blockPos, this, 2, 0);
                }
            }
        }
    }

    @Override
    protected int calculateInputStrength(World world, BlockPos blockPos, IBlockState iBlockState) {
        int n = super.calculateInputStrength(world, blockPos, iBlockState);
        EnumFacing enumFacing = iBlockState.getValue(FACING);
        BlockPos blockPos2 = blockPos.offset(enumFacing);
        Block block = world.getBlockState(blockPos2).getBlock();
        if (block.hasComparatorInputOverride()) {
            n = block.getComparatorInputOverride(world, blockPos2);
        } else if (n < 15 && block.isNormalCube()) {
            EntityItemFrame entityItemFrame;
            block = world.getBlockState(blockPos2 = blockPos2.offset(enumFacing)).getBlock();
            if (block.hasComparatorInputOverride()) {
                n = block.getComparatorInputOverride(world, blockPos2);
            } else if (block.getMaterial() == Material.air && (entityItemFrame = this.findItemFrame(world, enumFacing, blockPos2)) != null) {
                n = entityItemFrame.func_174866_q();
            }
        }
        return n;
    }

    @Override
    protected IBlockState getPoweredState(IBlockState iBlockState) {
        Boolean bl = iBlockState.getValue(POWERED);
        Mode mode = iBlockState.getValue(MODE);
        EnumFacing enumFacing = iBlockState.getValue(FACING);
        return Blocks.powered_comparator.getDefaultState().withProperty(FACING, enumFacing).withProperty(POWERED, bl).withProperty(MODE, mode);
    }

    @Override
    public Item getItem(World world, BlockPos blockPos) {
        return Items.comparator;
    }

    @Override
    protected boolean shouldBePowered(World world, BlockPos blockPos, IBlockState iBlockState) {
        int n = this.calculateInputStrength(world, blockPos, iBlockState);
        if (n >= 15) {
            return true;
        }
        if (n == 0) {
            return false;
        }
        int n2 = this.getPowerOnSides(world, blockPos, iBlockState);
        return n2 == 0 ? true : n >= n2;
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        int n = 0;
        n |= iBlockState.getValue(FACING).getHorizontalIndex();
        if (iBlockState.getValue(POWERED).booleanValue()) {
            n |= 8;
        }
        if (iBlockState.getValue(MODE) == Mode.SUBTRACT) {
            n |= 4;
        }
        return n;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, MODE, POWERED);
    }

    private void onStateChange(World world, BlockPos blockPos, IBlockState iBlockState) {
        int n = this.calculateOutput(world, blockPos, iBlockState);
        TileEntity tileEntity = world.getTileEntity(blockPos);
        int n2 = 0;
        if (tileEntity instanceof TileEntityComparator) {
            TileEntityComparator tileEntityComparator = (TileEntityComparator)tileEntity;
            n2 = tileEntityComparator.getOutputSignal();
            tileEntityComparator.setOutputSignal(n);
        }
        if (n2 != n || iBlockState.getValue(MODE) == Mode.COMPARE) {
            boolean bl = this.shouldBePowered(world, blockPos, iBlockState);
            boolean bl2 = this.isPowered(iBlockState);
            if (bl2 && !bl) {
                world.setBlockState(blockPos, iBlockState.withProperty(POWERED, false), 2);
            } else if (!bl2 && bl) {
                world.setBlockState(blockPos, iBlockState.withProperty(POWERED, true), 2);
            }
            this.notifyNeighbors(world, blockPos, iBlockState);
        }
    }

    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return Items.comparator;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int n) {
        return new TileEntityComparator();
    }

    @Override
    protected int getActiveSignal(IBlockAccess iBlockAccess, BlockPos blockPos, IBlockState iBlockState) {
        TileEntity tileEntity = iBlockAccess.getTileEntity(blockPos);
        return tileEntity instanceof TileEntityComparator ? ((TileEntityComparator)tileEntity).getOutputSignal() : 0;
    }

    @Override
    protected boolean isPowered(IBlockState iBlockState) {
        return this.isRepeaterPowered || iBlockState.getValue(POWERED) != false;
    }

    public static enum Mode implements IStringSerializable
    {
        COMPARE("compare"),
        SUBTRACT("subtract");

        private final String name;

        private Mode(String string2) {
            this.name = string2;
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

