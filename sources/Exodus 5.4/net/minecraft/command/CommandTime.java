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
    protected void addTime(ICommandSender iCommandSender, int n) {
        int n2 = 0;
        while (n2 < MinecraftServer.getServer().worldServers.length) {
            WorldServer worldServer = MinecraftServer.getServer().worldServers[n2];
            worldServer.setWorldTime(worldServer.getWorldTime() + (long)n);
            ++n2;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 1 ? CommandTime.getListOfStringsMatchingLastWord(stringArray, "set", "add", "query") : (stringArray.length == 2 && stringArray[0].equals("set") ? CommandTime.getListOfStringsMatchingLastWord(stringArray, "day", "night") : (stringArray.length == 2 && stringArray[0].equals("query") ? CommandTime.getListOfStringsMatchingLastWord(stringArray, "daytime", "gametime") : null));
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.time.usage";
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        if (stringArray.length > 1) {
            if (stringArray[0].equals("set")) {
                int n = stringArray[1].equals("day") ? 1000 : (stringArray[1].equals("night") ? 13000 : CommandTime.parseInt(stringArray[1], 0));
                this.setTime(iCommandSender, n);
                CommandTime.notifyOperators(iCommandSender, (ICommand)this, "commands.time.set", n);
                return;
            }
            if (stringArray[0].equals("add")) {
                int n = CommandTime.parseInt(stringArray[1], 0);
                this.addTime(iCommandSender, n);
                CommandTime.notifyOperators(iCommandSender, (ICommand)this, "commands.time.added", n);
                return;
            }
            if (stringArray[0].equals("query")) {
                if (stringArray[1].equals("daytime")) {
                    int n = (int)(iCommandSender.getEntityWorld().getWorldTime() % Integer.MAX_VALUE);
                    iCommandSender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, n);
                    CommandTime.notifyOperators(iCommandSender, (ICommand)this, "commands.time.query", n);
                    return;
                }
                if (stringArray[1].equals("gametime")) {
                    int n = (int)(iCommandSender.getEntityWorld().getTotalWorldTime() % Integer.MAX_VALUE);
                    iCommandSender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, n);
                    CommandTime.notifyOperators(iCommandSender, (ICommand)this, "commands.time.query", n);
                    return;
                }
            }
        }
        throw new WrongUsageException("commands.time.usage", new Object[0]);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandName() {
        return "time";
    }

    protected void setTime(ICommandSender iCommandSender, int n) {
        int n2 = 0;
        while (n2 < MinecraftServer.getServer().worldServers.length) {
            MinecraftServer.getServer().worldServers[n2].setWorldTime(n);
            ++n2;
        }
    }
}

