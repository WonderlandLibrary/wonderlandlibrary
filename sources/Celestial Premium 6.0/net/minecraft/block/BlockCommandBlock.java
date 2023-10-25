/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlockCommandBlock
extends BlockContainer {
    private static final Logger field_193388_c = LogManager.getLogger();
    public static final PropertyDirection FACING = BlockDirectional.FACING;
    public static final PropertyBool CONDITIONAL = PropertyBool.create("conditional");

    public BlockCommandBlock(MapColor color) {
        super(Material.IRON, color);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(CONDITIONAL, false));
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        TileEntityCommandBlock tileentitycommandblock = new TileEntityCommandBlock();
        tileentitycommandblock.setAuto(this == Blocks.CHAIN_COMMAND_BLOCK);
        return tileentitycommandblock;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
        TileEntity tileentity;
        if (!worldIn.isRemote && (tileentity = worldIn.getTileEntity(pos)) instanceof TileEntityCommandBlock) {
            TileEntityCommandBlock tileentitycommandblock = (TileEntityCommandBlock)tileentity;
            boolean flag = worldIn.isBlockPowered(pos);
            boolean flag1 = tileentitycommandblock.isPowered();
            tileentitycommandblock.setPowered(flag);
            if (!flag1 && !tileentitycommandblock.isAuto() && tileentitycommandblock.getMode() != TileEntityCommandBlock.Mode.SEQUENCE && flag) {
                tileentitycommandblock.setConditionMet();
                worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
            }
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        TileEntity tileentity;
        if (!worldIn.isRemote && (tileentity = worldIn.getTileEntity(pos)) instanceof TileEntityCommandBlock) {
            TileEntityCommandBlock tileentitycommandblock = (TileEntityCommandBlock)tileentity;
            CommandBlockBaseLogic commandblockbaselogic = tileentitycommandblock.getCommandBlockLogic();
            boolean flag = !StringUtils.isNullOrEmpty(commandblockbaselogic.getCommand());
            TileEntityCommandBlock.Mode tileentitycommandblock$mode = tileentitycommandblock.getMode();
            boolean flag1 = tileentitycommandblock.isConditionMet();
            if (tileentitycommandblock$mode == TileEntityCommandBlock.Mode.AUTO) {
                tileentitycommandblock.setConditionMet();
                if (flag1) {
                    this.func_193387_a(state, worldIn, pos, commandblockbaselogic, flag);
                } else if (tileentitycommandblock.isConditional()) {
                    commandblockbaselogic.setSuccessCount(0);
                }
                if (tileentitycommandblock.isPowered() || tileentitycommandblock.isAuto()) {
                    worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
                }
            } else if (tileentitycommandblock$mode == TileEntityCommandBlock.Mode.REDSTONE) {
                if (flag1) {
                    this.func_193387_a(state, worldIn, pos, commandblockbaselogic, flag);
                } else if (tileentitycommandblock.isConditional()) {
                    commandblockbaselogic.setSuccessCount(0);
                }
            }
            worldIn.updateComparatorOutputLevel(pos, this);
        }
    }

    private void func_193387_a(IBlockState p_193387_1_, World p_193387_2_, BlockPos p_193387_3_, CommandBlockBaseLogic p_193387_4_, boolean p_193387_5_) {
        if (p_193387_5_) {
            p_193387_4_.trigger(p_193387_2_);
        } else {
            p_193387_4_.setSuccessCount(0);
        }
        BlockCommandBlock.func_193386_c(p_193387_2_, p_193387_3_, p_193387_1_.getValue(FACING));
    }

    @Override
    public int tickRate(World worldIn) {
        return 1;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityCommandBlock && playerIn.canUseCommandBlock()) {
            playerIn.displayGuiCommandBlock((TileEntityCommandBlock)tileentity);
            return true;
        }
        return false;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity instanceof TileEntityCommandBlock ? ((TileEntityCommandBlock)tileentity).getCommandBlockLogic().getSuccessCount() : 0;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityCommandBlock) {
            TileEntityCommandBlock tileentitycommandblock = (TileEntityCommandBlock)tileentity;
            CommandBlockBaseLogic commandblockbaselogic = tileentitycommandblock.getCommandBlockLogic();
            if (stack.hasDisplayName()) {
                commandblockbaselogic.setName(stack.getDisplayName());
            }
            if (!worldIn.isRemote) {
                NBTTagCompound nbttagcompound = stack.getTagCompound();
                if (nbttagcompound == null || !nbttagcompound.hasKey("BlockEntityTag", 10)) {
                    commandblockbaselogic.setTrackOutput(worldIn.getGameRules().getBoolean("sendCommandFeedback"));
                    tileentitycommandblock.setAuto(this == Blocks.CHAIN_COMMAND_BLOCK);
                }
                if (tileentitycommandblock.getMode() == TileEntityCommandBlock.Mode.SEQUENCE) {
                    boolean flag = worldIn.isBlockPowered(pos);
                    tileentitycommandblock.setPowered(flag);
                }
            }
        }
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7)).withProperty(CONDITIONAL, (meta & 8) != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex() | (state.getValue(CONDITIONAL) != false ? 8 : 0);
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block)this, FACING, CONDITIONAL);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.func_190914_a(pos, placer)).withProperty(CONDITIONAL, false);
    }

    private static void func_193386_c(World p_193386_0_, BlockPos p_193386_1_, EnumFacing p_193386_2_) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(p_193386_1_);
        GameRules gamerules = p_193386_0_.getGameRules();
        int i = gamerules.getInt("maxCommandChainLength");
        while (i-- > 0) {
            TileEntityCommandBlock tileentitycommandblock;
            TileEntity tileentity;
            blockpos$mutableblockpos.move(p_193386_2_);
            IBlockState iblockstate = p_193386_0_.getBlockState(blockpos$mutableblockpos);
            Block block = iblockstate.getBlock();
            if (block != Blocks.CHAIN_COMMAND_BLOCK || !((tileentity = p_193386_0_.getTileEntity(blockpos$mutableblockpos)) instanceof TileEntityCommandBlock) || (tileentitycommandblock = (TileEntityCommandBlock)tileentity).getMode() != TileEntityCommandBlock.Mode.SEQUENCE) break;
            if (tileentitycommandblock.isPowered() || tileentitycommandblock.isAuto()) {
                CommandBlockBaseLogic commandblockbaselogic = tileentitycommandblock.getCommandBlockLogic();
                if (tileentitycommandblock.setConditionMet()) {
                    if (!commandblockbaselogic.trigger(p_193386_0_)) break;
                    p_193386_0_.updateComparatorOutputLevel(blockpos$mutableblockpos, block);
                } else if (tileentitycommandblock.isConditional()) {
                    commandblockbaselogic.setSuccessCount(0);
                }
            }
            p_193386_2_ = iblockstate.getValue(FACING);
        }
        if (i <= 0) {
            int j = Math.max(gamerules.getInt("maxCommandChainLength"), 0);
            field_193388_c.warn("Commandblock chain tried to execure more than " + j + " steps!");
        }
    }
}

