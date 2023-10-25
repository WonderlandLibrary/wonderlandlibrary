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
    public String getCommandName() {
        return "time";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.time.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 1) {
            if (args[0].equals((Object)"set")) {
                int l = args[1].equals((Object)"day") ? 1000 : (args[1].equals((Object)"night") ? 13000 : CommandTime.parseInt((String)args[1], (int)0));
                this.setTime(sender, l);
                CommandTime.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.time.set", (Object[])new Object[]{l});
                return;
            }
            if (args[0].equals((Object)"add")) {
                int k = CommandTime.parseInt((String)args[1], (int)0);
                this.addTime(sender, k);
                CommandTime.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.time.added", (Object[])new Object[]{k});
                return;
            }
            if (args[0].equals((Object)"query")) {
                if (args[1].equals((Object)"daytime")) {
                    int j = (int)(sender.getEntityWorld().getWorldTime() % Integer.MAX_VALUE);
                    sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, j);
                    CommandTime.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.time.query", (Object[])new Object[]{j});
                    return;
                }
                if (args[1].equals((Object)"gametime")) {
                    int i = (int)(sender.getEntityWorld().getTotalWorldTime() % Integer.MAX_VALUE);
                    sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, i);
                    CommandTime.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.time.query", (Object[])new Object[]{i});
                    return;
                }
            }
        }
        throw new WrongUsageException("commands.time.usage", new Object[0]);
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandTime.getListOfStringsMatchingLastWord((String[])args, (String[])new String[]{"set", "add", "query"}) : (args.length == 2 && args[0].equals((Object)"set") ? CommandTime.getListOfStringsMatchingLastWord((String[])args, (String[])new String[]{"day", "night"}) : (args.length == 2 && args[0].equals((Object)"query") ? CommandTime.getListOfStringsMatchingLastWord((String[])args, (String[])new String[]{"daytime", "gametime"}) : null));
    }

    protected void setTime(ICommandSender sender, int time) {
        for (int i = 0; i < MinecraftServer.getServer().worldServers.length; ++i) {
            MinecraftServer.getServer().worldServers[i].setWorldTime((long)time);
        }
    }

    protected void addTime(ICommandSender sender, int time) {
        for (int i = 0; i < MinecraftServer.getServer().worldServers.length; ++i) {
            WorldServer worldserver = MinecraftServer.getServer().worldServers[i];
            worldserver.setWorldTime(worldserver.getWorldTime() + (long)time);
        }
    }
}
