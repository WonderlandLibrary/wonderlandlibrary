// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command.server;

import java.util.ArrayList;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.util.BlockPos;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.ICommand;
import net.minecraft.command.CommandException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandOp extends CommandBase
{
    private static final String __OBFID = "CL_00000694";
    
    @Override
    public String getCommandName() {
        return "op";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.op.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length != 1 || args[0].length() <= 0) {
            throw new WrongUsageException("commands.op.usage", new Object[0]);
        }
        final MinecraftServer var3 = MinecraftServer.getServer();
        final GameProfile var4 = var3.getPlayerProfileCache().getGameProfileForUsername(args[0]);
        if (var4 == null) {
            throw new CommandException("commands.op.failed", new Object[] { args[0] });
        }
        var3.getConfigurationManager().addOp(var4);
        CommandBase.notifyOperators(sender, this, "commands.op.success", args[0]);
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        if (args.length == 1) {
            final String var4 = args[args.length - 1];
            final ArrayList var5 = Lists.newArrayList();
            for (final GameProfile var9 : MinecraftServer.getServer().getGameProfiles()) {
                if (!MinecraftServer.getServer().getConfigurationManager().canSendCommands(var9) && CommandBase.doesStringStartWith(var4, var9.getName())) {
                    var5.add(var9.getName());
                }
            }
            return var5;
        }
        return null;
    }
}
