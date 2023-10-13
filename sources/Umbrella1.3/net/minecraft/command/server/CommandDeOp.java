/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandDeOp
extends CommandBase {
    private static final String __OBFID = "CL_00000244";

    @Override
    public String getCommandName() {
        return "deop";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.deop.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        GameProfile var4;
        MinecraftServer var3;
        if (args.length == 1 && args[0].length() > 0) {
            var3 = MinecraftServer.getServer();
            var4 = var3.getConfigurationManager().getOppedPlayers().getGameProfileFromName(args[0]);
            if (var4 == null) {
                throw new CommandException("commands.deop.failed", args[0]);
            }
        } else {
            throw new WrongUsageException("commands.deop.usage", new Object[0]);
        }
        var3.getConfigurationManager().removeOp(var4);
        CommandDeOp.notifyOperators(sender, (ICommand)this, "commands.deop.success", args[0]);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandDeOp.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getConfigurationManager().getOppedPlayerNames()) : null;
    }
}

