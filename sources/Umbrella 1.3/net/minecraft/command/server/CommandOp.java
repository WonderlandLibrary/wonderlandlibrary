/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.mojang.authlib.GameProfile
 */
package net.minecraft.command.server;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandOp
extends CommandBase {
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
    public String getCommandUsage(ICommandSender sender) {
        return "commands.op.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        GameProfile var4;
        MinecraftServer var3;
        if (args.length == 1 && args[0].length() > 0) {
            var3 = MinecraftServer.getServer();
            var4 = var3.getPlayerProfileCache().getGameProfileForUsername(args[0]);
            if (var4 == null) {
                throw new CommandException("commands.op.failed", args[0]);
            }
        } else {
            throw new WrongUsageException("commands.op.usage", new Object[0]);
        }
        var3.getConfigurationManager().addOp(var4);
        CommandOp.notifyOperators(sender, (ICommand)this, "commands.op.success", args[0]);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            String var4 = args[args.length - 1];
            ArrayList var5 = Lists.newArrayList();
            for (GameProfile var9 : MinecraftServer.getServer().getGameProfiles()) {
                if (MinecraftServer.getServer().getConfigurationManager().canSendCommands(var9) || !CommandOp.doesStringStartWith(var4, var9.getName())) continue;
                var5.add(var9.getName());
            }
            return var5;
        }
        return null;
    }
}

