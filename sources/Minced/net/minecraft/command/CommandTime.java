// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import net.minecraft.world.WorldServer;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.server.MinecraftServer;

public class CommandTime extends CommandBase
{
    @Override
    public String getName() {
        return "time";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.time.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length > 1) {
            if ("set".equals(args[0])) {
                int i1;
                if ("day".equals(args[1])) {
                    i1 = 1000;
                }
                else if ("night".equals(args[1])) {
                    i1 = 13000;
                }
                else {
                    i1 = CommandBase.parseInt(args[1], 0);
                }
                this.setAllWorldTimes(server, i1);
                CommandBase.notifyCommandListener(sender, this, "commands.time.set", i1);
                return;
            }
            if ("add".equals(args[0])) {
                final int l = CommandBase.parseInt(args[1], 0);
                this.incrementAllWorldTimes(server, l);
                CommandBase.notifyCommandListener(sender, this, "commands.time.added", l);
                return;
            }
            if ("query".equals(args[0])) {
                if ("daytime".equals(args[1])) {
                    final int k = (int)(sender.getEntityWorld().getWorldTime() % 24000L);
                    sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, k);
                    CommandBase.notifyCommandListener(sender, this, "commands.time.query", k);
                    return;
                }
                if ("day".equals(args[1])) {
                    final int j = (int)(sender.getEntityWorld().getWorldTime() / 24000L % 2147483647L);
                    sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, j);
                    CommandBase.notifyCommandListener(sender, this, "commands.time.query", j);
                    return;
                }
                if ("gametime".equals(args[1])) {
                    final int m = (int)(sender.getEntityWorld().getTotalWorldTime() % 2147483647L);
                    sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, m);
                    CommandBase.notifyCommandListener(sender, this, "commands.time.query", m);
                    return;
                }
            }
        }
        throw new WrongUsageException("commands.time.usage", new Object[0]);
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "set", "add", "query");
        }
        if (args.length == 2 && "set".equals(args[0])) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "day", "night");
        }
        return (args.length == 2 && "query".equals(args[0])) ? CommandBase.getListOfStringsMatchingLastWord(args, "daytime", "gametime", "day") : Collections.emptyList();
    }
    
    protected void setAllWorldTimes(final MinecraftServer server, final int time) {
        for (int i = 0; i < server.worlds.length; ++i) {
            server.worlds[i].setWorldTime(time);
        }
    }
    
    protected void incrementAllWorldTimes(final MinecraftServer server, final int amount) {
        for (int i = 0; i < server.worlds.length; ++i) {
            final WorldServer worldserver = server.worlds[i];
            worldserver.setWorldTime(worldserver.getWorldTime() + amount);
        }
    }
}
