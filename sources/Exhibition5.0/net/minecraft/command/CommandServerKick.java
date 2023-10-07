// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandServerKick extends CommandBase
{
    private static final String __OBFID = "CL_00000550";
    
    @Override
    public String getCommandName() {
        return "kick";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.kick.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length <= 0 || args[0].length() <= 1) {
            throw new WrongUsageException("commands.kick.usage", new Object[0]);
        }
        final EntityPlayerMP var3 = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(args[0]);
        String var4 = "Kicked by an operator.";
        boolean var5 = false;
        if (var3 == null) {
            throw new PlayerNotFoundException();
        }
        if (args.length >= 2) {
            var4 = CommandBase.getChatComponentFromNthArg(sender, args, 1).getUnformattedText();
            var5 = true;
        }
        var3.playerNetServerHandler.kickPlayerFromServer(var4);
        if (var5) {
            CommandBase.notifyOperators(sender, this, "commands.kick.success.reason", var3.getName(), var4);
        }
        else {
            CommandBase.notifyOperators(sender, this, "commands.kick.success", var3.getName());
        }
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length >= 1) ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
    }
}
