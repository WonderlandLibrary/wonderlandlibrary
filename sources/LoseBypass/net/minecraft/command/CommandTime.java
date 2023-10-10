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
        if (args.length <= 1) throw new WrongUsageException("commands.time.usage", new Object[0]);
        if (args[0].equals("set")) {
            int l = args[1].equals("day") ? 1000 : (args[1].equals("night") ? 13000 : CommandTime.parseInt(args[1], 0));
            this.setTime(sender, l);
            CommandTime.notifyOperators(sender, (ICommand)this, "commands.time.set", l);
            return;
        }
        if (args[0].equals("add")) {
            int k = CommandTime.parseInt(args[1], 0);
            this.addTime(sender, k);
            CommandTime.notifyOperators(sender, (ICommand)this, "commands.time.added", k);
            return;
        }
        if (!args[0].equals("query")) throw new WrongUsageException("commands.time.usage", new Object[0]);
        if (args[1].equals("daytime")) {
            int j = (int)(sender.getEntityWorld().getWorldTime() % Integer.MAX_VALUE);
            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, j);
            CommandTime.notifyOperators(sender, (ICommand)this, "commands.time.query", j);
            return;
        }
        if (!args[1].equals("gametime")) throw new WrongUsageException("commands.time.usage", new Object[0]);
        int i = (int)(sender.getEntityWorld().getTotalWorldTime() % Integer.MAX_VALUE);
        sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, i);
        CommandTime.notifyOperators(sender, (ICommand)this, "commands.time.query", i);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        List<String> list;
        if (args.length == 1) {
            list = CommandTime.getListOfStringsMatchingLastWord(args, "set", "add", "query");
            return list;
        }
        if (args.length == 2 && args[0].equals("set")) {
            list = CommandTime.getListOfStringsMatchingLastWord(args, "day", "night");
            return list;
        }
        if (args.length != 2) return null;
        if (!args[0].equals("query")) return null;
        list = CommandTime.getListOfStringsMatchingLastWord(args, "daytime", "gametime");
        return list;
    }

    protected void setTime(ICommandSender p_71552_1_, int p_71552_2_) {
        int i = 0;
        while (i < MinecraftServer.getServer().worldServers.length) {
            MinecraftServer.getServer().worldServers[i].setWorldTime(p_71552_2_);
            ++i;
        }
    }

    protected void addTime(ICommandSender p_71553_1_, int p_71553_2_) {
        int i = 0;
        while (i < MinecraftServer.getServer().worldServers.length) {
            WorldServer worldserver = MinecraftServer.getServer().worldServers[i];
            worldserver.setWorldTime(worldserver.getWorldTime() + (long)p_71553_2_);
            ++i;
        }
    }
}

