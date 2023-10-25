/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandSetBlock
extends CommandBase {
    @Override
    public String getCommandName() {
        return "setblock";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.setblock.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        TileEntity tileentity;
        TileEntity tileentity1;
        if (args.length < 4) {
            throw new WrongUsageException("commands.setblock.usage", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        BlockPos blockpos = CommandSetBlock.parseBlockPos(sender, args, 0, false);
        Block block = CommandBase.getBlockByText(sender, args[3]);
        IBlockState iblockstate = args.length >= 5 ? CommandSetBlock.func_190794_a(block, args[4]) : block.getDefaultState();
        World world = sender.getEntityWorld();
        if (!world.isBlockLoaded(blockpos)) {
            throw new CommandException("commands.setblock.outOfWorld", new Object[0]);
        }
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        boolean flag = false;
        if (args.length >= 7 && block.hasTileEntity()) {
            String s = CommandSetBlock.buildString(args, 6);
            try {
                nbttagcompound = JsonToNBT.getTagFromJson(s);
                flag = true;
            }
            catch (NBTException nbtexception) {
                throw new CommandException("commands.setblock.tagError", nbtexception.getMessage());
            }
        }
        if (args.length >= 6) {
            if ("destroy".equals(args[5])) {
                world.destroyBlock(blockpos, true);
                if (block == Blocks.AIR) {
                    CommandSetBlock.notifyCommandListener(sender, (ICommand)this, "commands.setblock.success", new Object[0]);
                    return;
                }
            } else if ("keep".equals(args[5]) && !world.isAirBlock(blockpos)) {
                throw new CommandException("commands.setblock.noChange", new Object[0]);
            }
        }
        if ((tileentity1 = world.getTileEntity(blockpos)) != null && tileentity1 instanceof IInventory) {
            ((IInventory)((Object)tileentity1)).clear();
        }
        if (!world.setBlockState(blockpos, iblockstate, 2)) {
            throw new CommandException("commands.setblock.noChange", new Object[0]);
        }
        if (flag && (tileentity = world.getTileEntity(blockpos)) != null) {
            nbttagcompound.setInteger("x", blockpos.getX());
            nbttagcompound.setInteger("y", blockpos.getY());
            nbttagcompound.setInteger("z", blockpos.getZ());
            tileentity.readFromNBT(nbttagcompound);
        }
        world.notifyNeighborsRespectDebug(blockpos, iblockstate.getBlock(), false);
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
        CommandSetBlock.notifyCommandListener(sender, (ICommand)this, "commands.setblock.success", new Object[0]);
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length > 0 && args.length <= 3) {
            return CommandSetBlock.getTabCompletionCoordinate(args, 0, pos);
        }
        if (args.length == 4) {
            return CommandSetBlock.getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys());
        }
        return args.length == 6 ? CommandSetBlock.getListOfStringsMatchingLastWord(args, "replace", "destroy", "keep") : Collections.emptyList();
    }
}

