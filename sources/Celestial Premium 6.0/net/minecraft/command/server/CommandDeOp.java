/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandDeOp
extends CommandBase {
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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        GameProfile gameprofile;
        if (args.length == 1 && args[0].length() > 0) {
            gameprofile = server.getPlayerList().getOppedPlayers().getGameProfileFromName(args[0]);
            if (gameprofile == null) {
                throw new CommandException("commands.deop.failed", args[0]);
            }
        } else {
            throw new WrongUsageException("commands.deop.usage", new Object[0]);
        }
        server.getPlayerList().removeOp(gameprofile);
        CommandDeOp.notifyCommandListener(sender, (ICommand)this, "commands.deop.success", args[0]);
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return args.length == 1 ? CommandDeOp.getListOfStringsMatchingLastWord(args, server.getPlayerList().getOppedPlayerNames()) : Collections.emptyList();
    }
}

