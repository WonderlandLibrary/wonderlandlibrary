// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import net.minecraft.world.WorldServer;
import net.minecraft.server.MinecraftServer;
import java.util.List;
import net.minecraft.util.BlockPos;

public class CommandTime extends CommandBase
{
    private static final String __OBFID = "CL_00001183";
    
    @Override
    public String getCommandName() {
        return "time";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.time.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length > 1) {
            if (args[0].equals("set")) {
                int var3;
                if (args[1].equals("day")) {
                    var3 = 1000;
                }
                else if (args[1].equals("night")) {
                    var3 = 13000;
                }
                else {
                    var3 = CommandBase.parseInt(args[1], 0);
                }
                this.setTime(sender, var3);
                CommandBase.notifyOperators(sender, this, "commands.time.set", var3);
                return;
            }
            if (args[0].equals("add")) {
                final int var3 = CommandBase.parseInt(args[1], 0);
                this.addTime(sender, var3);
                CommandBase.notifyOperators(sender, this, "commands.time.added", var3);
                return;
            }
            if (args[0].equals("query")) {
                if (args[1].equals("daytime")) {
                    final int var3 = (int)(sender.getEntityWorld().getWorldTime() % 2147483647L);
                    sender.func_174794_a(CommandResultStats.Type.QUERY_RESULT, var3);
                    CommandBase.notifyOperators(sender, this, "commands.time.query", var3);
                    return;
                }
                if (args[1].equals("gametime")) {
                    final int var3 = (int)(sender.getEntityWorld().getTotalWorldTime() % 2147483647L);
                    sender.func_174794_a(CommandResultStats.Type.QUERY_RESULT, var3);
                    CommandBase.notifyOperators(sender, this, "commands.time.query", var3);
                    return;
                }
            }
        }
        throw new WrongUsageException("commands.time.usage", new Object[0]);
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, "set", "add", "query") : ((args.length == 2 && args[0].equals("set")) ? CommandBase.getListOfStringsMatchingLastWord(args, "day", "night") : ((args.length == 2 && args[0].equals("query")) ? CommandBase.getListOfStringsMatchingLastWord(args, "daytime", "gametime") : null));
    }
    
    protected void setTime(final ICommandSender p_71552_1_, final int p_71552_2_) {
        for (final WorldServer worldServer : MinecraftServer.getServer().worldServers) {
            worldServer.setWorldTime(p_71552_2_);
        }
    }
    
    protected void addTime(final ICommandSender p_71553_1_, final int p_71553_2_) {
        for (final WorldServer var4 : MinecraftServer.getServer().worldServers) {
            var4.setWorldTime(var4.getWorldTime() + p_71553_2_);
        }
    }
}
