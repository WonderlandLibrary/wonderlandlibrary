/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandBlockData
extends CommandBase {
    @Override
    public String getCommandName() {
        return "blockdata";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.blockdata.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        NBTTagCompound nbttagcompound2;
        if (args.length < 4) {
            throw new WrongUsageException("commands.blockdata.usage", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        BlockPos blockpos = CommandBlockData.parseBlockPos(sender, args, 0, false);
        World world = sender.getEntityWorld();
        if (!world.isBlockLoaded(blockpos)) {
            throw new CommandException("commands.blockdata.outOfWorld", new Object[0]);
        }
        IBlockState iblockstate = world.getBlockState(blockpos);
        TileEntity tileentity = world.getTileEntity(blockpos);
        if (tileentity == null) {
            throw new CommandException("commands.blockdata.notValid", new Object[0]);
        }
        NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());
        NBTTagCompound nbttagcompound1 = nbttagcompound.copy();
        try {
            nbttagcompound2 = JsonToNBT.getTagFromJson(CommandBlockData.buildString(args, 3));
        }
        catch (NBTException nbtexception) {
            throw new CommandException("commands.blockdata.tagError", nbtexception.getMessage());
        }
        nbttagcompound.merge(nbttagcompound2);
        nbttagcompound.setInteger("x", blockpos.getX());
        nbttagcompound.setInteger("y", blockpos.getY());
        nbttagcompound.setInteger("z", blockpos.getZ());
        if (nbttagcompound.equals(nbttagcompound1)) {
            throw new CommandException("commands.blockdata.failed", nbttagcompound.toString());
        }
        tileentity.readFromNBT(nbttagcompound);
        tileentity.markDirty();
        world.notifyBlockUpdate(blockpos, iblockstate, iblockstate, 3);
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
        CommandBlockData.notifyCommandListener(sender, (ICommand)this, "commands.blockdata.success", nbttagcompound.toString());
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return args.length > 0 && args.length <= 3 ? CommandBlockData.getTabCompletionCoordinate(args, 0, pos) : Collections.emptyList();
    }
}

