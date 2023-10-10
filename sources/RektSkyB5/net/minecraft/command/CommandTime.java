/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;

public class CommandTime
extends CommandBase {
    @Override
    public String getCommandName() {
        return "time";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.time.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 1) {
            if (args[0].equals("set")) {
                int l2 = args[1].equals("day") ? 1000 : (args[1].equals("night") ? 13000 : CommandTime.parseInt(args[1], 0));
                this.setTime(sender, l2);
                CommandTime.notifyOperators(sender, (ICommand)this, "commands.time.set", l2);
                return;
            }
            if (args[0].equals("add")) {
                int k2 = CommandTime.parseInt(args[1], 0);
                this.addTime(sender, k2);
                CommandTime.notifyOperators(sender, (ICommand)this, "commands.time.added", k2);
                return;
            }
            if (args[0].equals("query")) {
                if (args[1].equals("daytime")) {
                    int j2 = (int)(sender.getEntityWorld().getWorldTime() % Integer.MAX_VALUE);
                    sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, j2);
                    CommandTime.notifyOperators(sender, (ICommand)this, "commands.time.query", j2);
                    return;
                }
                if (args[1].equals("gametime")) {
                    int i2 = (int)(sender.getEntityWorld().getTotalWorldTime() % Integer.MAX_VALUE);
                    sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, i2);
                    CommandTime.notifyOperators(sender, (ICommand)this, "commands.time.query", i2);
                    return;
                }
            }
        }
        throw new WrongUsageException("commands.time.usage", new Object[0]);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandTime.getListOfStringsMatchingLastWord(args, "set", "add", "query") : (args.length == 2 && args[0].equals("set") ? CommandTime.getListOfStringsMatchingLastWord(args, "day", "night") : (args.length == 2 && args[0].equals("query") ? CommandTime.getListOfStringsMatchingLastWord(args, "daytime", "gametime") : null));
    }

    protected void setTime(ICommandSender p_71552_1_, int p_71552_2_) {
        for (int i2 = 0; i2 < MinecraftServer.getServer().worldServers.length; ++i2) {
            MinecraftServer.getServer().worldServers[i2].setWorldTime(p_71552_2_);
        }
    }

    protected void addTime(ICommandSender p_71553_1_, int p_71553_2_) {
        for (int i2 = 0; i2 < MinecraftServer.getServer().worldServers.length; ++i2) {
            WorldServer worldserver = MinecraftServer.getServer().worldServers[i2];
            worldserver.setWorldTime(worldserver.getWorldTime() + (long)p_71553_2_);
        }
    }
}

