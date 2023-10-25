/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command.server;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
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

public class CommandOp
extends CommandBase {
    @Override
    public String getCommandName() {
        return "op";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.op.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        GameProfile gameprofile;
        if (args.length == 1 && args[0].length() > 0) {
            gameprofile = server.getPlayerProfileCache().getGameProfileForUsername(args[0]);
            if (gameprofile == null) {
                throw new CommandException("commands.op.failed", args[0]);
            }
        } else {
            throw new WrongUsageException("commands.op.usage", new Object[0]);
        }
        server.getPlayerList().addOp(gameprofile);
        CommandOp.notifyCommandListener(sender, (ICommand)this, "commands.op.success", args[0]);
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            String s = args[args.length - 1];
            ArrayList<String> list = Lists.newArrayList();
            for (GameProfile gameprofile : server.getGameProfiles()) {
                if (server.getPlayerList().canSendCommands(gameprofile) || !CommandOp.doesStringStartWith(s, gameprofile.getName())) continue;
                list.add(gameprofile.getName());
            }
            return list;
        }
        return Collections.emptyList();
    }
}

