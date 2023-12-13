/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandSetSpawnpoint
extends CommandBase {
    private static final String __OBFID = "CL_00001026";

    @Override
    public String getCommandName() {
        return "spawnpoint";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.spawnpoint.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        BlockPos var4;
        if (args.length > 0 && args.length < 4) {
            throw new WrongUsageException("commands.spawnpoint.usage", new Object[0]);
        }
        EntityPlayerMP var3 = args.length > 0 ? CommandSetSpawnpoint.getPlayer(sender, args[0]) : CommandSetSpawnpoint.getCommandSenderAsPlayer(sender);
        BlockPos blockPos = var4 = args.length > 3 ? CommandSetSpawnpoint.func_175757_a(sender, args, 1, true) : var3.getPosition();
        if (var3.worldObj != null) {
            var3.func_180473_a(var4, true);
            CommandSetSpawnpoint.notifyOperators(sender, (ICommand)this, "commands.spawnpoint.success", var3.getName(), var4.getX(), var4.getY(), var4.getZ());
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandSetSpawnpoint.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : (args.length > 1 && args.length <= 4 ? CommandSetSpawnpoint.func_175771_a(args, 1, pos) : null);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}

