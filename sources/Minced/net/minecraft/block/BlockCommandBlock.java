// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import org.apache.logging.log4j.LogManager;
import net.minecraft.world.GameRules;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.StringUtils;
import java.util.Random;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import org.apache.logging.log4j.Logger;

public class BlockCommandBlock extends BlockContainer
{
    private static final Logger LOGGER;
    public static final PropertyDirection FACING;
    public static final PropertyBool CONDITIONAL;
    
    public BlockCommandBlock(final MapColor color) {
        super(Material.IRON, color);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockCommandBlock.FACING, EnumFacing.NORTH).withProperty((IProperty<Comparable>)BlockCommandBlock.CONDITIONAL, false));
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        final TileEntityCommandBlock tileentitycommandblock = new TileEntityCommandBlock();
        tileentitycommandblock.setAuto(this == Blocks.CHAIN_COMMAND_BLOCK);
        return tileentitycommandblock;
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (!worldIn.isRemote) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityCommandBlock) {
                final TileEntityCommandBlock tileentitycommandblock = (TileEntityCommandBlock)tileentity;
                final boolean flag = worldIn.isBlockPowered(pos);
                final boolean flag2 = tileentitycommandblock.isPowered();
                tileentitycommandblock.setPowered(flag);
                if (!flag2 && !tileentitycommandblock.isAuto() && tileentitycommandblock.getMode() != TileEntityCommandBlock.Mode.SEQUENCE && flag) {
                    tileentitycommandblock.setConditionMet();
                    worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
                }
            }
        }
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityCommandBlock) {
                final TileEntityCommandBlock tileentitycommandblock = (TileEntityCommandBlock)tileentity;
                final CommandBlockBaseLogic commandblockbaselogic = tileentitycommandblock.getCommandBlockLogic();
                final boolean flag = !StringUtils.isNullOrEmpty(commandblockbaselogic.getCommand());
                final TileEntityCommandBlock.Mode tileentitycommandblock$mode = tileentitycommandblock.getMode();
                final boolean flag2 = tileentitycommandblock.isConditionMet();
                if (tileentitycommandblock$mode == TileEntityCommandBlock.Mode.AUTO) {
                    tileentitycommandblock.setConditionMet();
                    if (flag2) {
                        this.execute(state, worldIn, pos, commandblockbaselogic, flag);
                    }
                    else if (tileentitycommandblock.isConditional()) {
                        commandblockbaselogic.setSuccessCount(0);
                    }
                    if (tileentitycommandblock.isPowered() || tileentitycommandblock.isAuto()) {
                        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
                    }
                }
                else if (tileentitycommandblock$mode == TileEntityCommandBlock.Mode.REDSTONE) {
                    if (flag2) {
                        this.execute(state, worldIn, pos, commandblockbaselogic, flag);
                    }
                    else if (tileentitycommandblock.isConditional()) {
                        commandblockbaselogic.setSuccessCount(0);
                    }
                }
                worldIn.updateComparatorOutputLevel(pos, this);
            }
        }
    }
    
    private void execute(final IBlockState p_193387_1_, final World p_193387_2_, final BlockPos p_193387_3_, final CommandBlockBaseLogic p_193387_4_, final boolean p_193387_5_) {
        if (p_193387_5_) {
            p_193387_4_.trigger(p_193387_2_);
        }
        else {
            p_193387_4_.setSuccessCount(0);
        }
        executeChain(p_193387_2_, p_193387_3_, p_193387_1_.getValue((IProperty<EnumFacing>)BlockCommandBlock.FACING));
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return 1;
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityCommandBlock && playerIn.canUseCommandBlock()) {
            playerIn.displayGuiCommandBlock((TileEntityCommandBlock)tileentity);
            return true;
        }
        return false;
    }
    
    @Override
    @Deprecated
    public boolean hasComparatorInputOverride(final IBlockState state) {
        return true;
    }
    
    @Override
    @Deprecated
    public int getComparatorInputOverride(final IBlockState blockState, final World worldIn, final BlockPos pos) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        return (tileentity instanceof TileEntityCommandBlock) ? ((TileEntityCommandBlock)tileentity).getCommandBlockLogic().getSuccessCount() : 0;
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityCommandBlock) {
            final TileEntityCommandBlock tileentitycommandblock = (TileEntityCommandBlock)tileentity;
            final CommandBlockBaseLogic commandblockbaselogic = tileentitycommandblock.getCommandBlockLogic();
            if (stack.hasDisplayName()) {
                commandblockbaselogic.setName(stack.getDisplayName());
            }
            if (!worldIn.isRemote) {
                final NBTTagCompound nbttagcompound = stack.getTagCompound();
                if (nbttagcompound == null || !nbttagcompound.hasKey("BlockEntityTag", 10)) {
                    commandblockbaselogic.setTrackOutput(worldIn.getGameRules().getBoolean("sendCommandFeedback"));
                    tileentitycommandblock.setAuto(this == Blocks.CHAIN_COMMAND_BLOCK);
                }
                if (tileentitycommandblock.getMode() == TileEntityCommandBlock.Mode.SEQUENCE) {
                    final boolean flag = worldIn.isBlockPowered(pos);
                    tileentitycommandblock.setPowered(flag);
                }
            }
        }
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    @Deprecated
    public EnumBlockRenderType getRenderType(final IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockCommandBlock.FACING, EnumFacing.byIndex(meta & 0x7)).withProperty((IProperty<Comparable>)BlockCommandBlock.CONDITIONAL, (meta & 0x8) != 0x0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<EnumFacing>)BlockCommandBlock.FACING).getIndex() | (state.getValue((IProperty<Boolean>)BlockCommandBlock.CONDITIONAL) ? 8 : 0);
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty((IProperty<Comparable>)BlockCommandBlock.FACING, rot.rotate(state.getValue((IProperty<EnumFacing>)BlockCommandBlock.FACING)));
    }
    
    @Override
    @Deprecated
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue((IProperty<EnumFacing>)BlockCommandBlock.FACING)));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockCommandBlock.FACING, BlockCommandBlock.CONDITIONAL });
    }
    
    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockCommandBlock.FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)).withProperty((IProperty<Comparable>)BlockCommandBlock.CONDITIONAL, false);
    }
    
    private static void executeChain(final World p_193386_0_, final BlockPos p_193386_1_, EnumFacing p_193386_2_) {
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(p_193386_1_);
        final GameRules gamerules = p_193386_0_.getGameRules();
        int i = gamerules.getInt("maxCommandChainLength");
        while (i-- > 0) {
            blockpos$mutableblockpos.move(p_193386_2_);
            final IBlockState iblockstate = p_193386_0_.getBlockState(blockpos$mutableblockpos);
            final Block block = iblockstate.getBlock();
            if (block != Blocks.CHAIN_COMMAND_BLOCK) {
                break;
            }
            final TileEntity tileentity = p_193386_0_.getTileEntity(blockpos$mutableblockpos);
            if (!(tileentity instanceof TileEntityCommandBlock)) {
                break;
            }
            final TileEntityCommandBlock tileentitycommandblock = (TileEntityCommandBlock)tileentity;
            if (tileentitycommandblock.getMode() != TileEntityCommandBlock.Mode.SEQUENCE) {
                break;
            }
            if (tileentitycommandblock.isPowered() || tileentitycommandblock.isAuto()) {
                final CommandBlockBaseLogic commandblockbaselogic = tileentitycommandblock.getCommandBlockLogic();
                if (tileentitycommandblock.setConditionMet()) {
                    if (!commandblockbaselogic.trigger(p_193386_0_)) {
                        break;
                    }
                    p_193386_0_.updateComparatorOutputLevel(blockpos$mutableblockpos, block);
                }
                else if (tileentitycommandblock.isConditional()) {
                    commandblockbaselogic.setSuccessCount(0);
                }
            }
            p_193386_2_ = iblockstate.getValue((IProperty<EnumFacing>)BlockCommandBlock.FACING);
        }
        if (i <= 0) {
            final int j = Math.max(gamerules.getInt("maxCommandChainLength"), 0);
            BlockCommandBlock.LOGGER.warn("Commandblock chain tried to execure more than " + j + " steps!");
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        FACING = BlockDirectional.FACING;
        CONDITIONAL = PropertyBool.create("conditional");
    }
}
