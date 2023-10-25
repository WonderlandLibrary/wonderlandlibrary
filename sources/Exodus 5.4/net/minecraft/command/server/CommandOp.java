/*
 * Decompiled with CFR 0.152.
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
    @Override
    public String getCommandName() {
        return "op";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.op.usage";
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        GameProfile gameProfile;
        MinecraftServer minecraftServer;
        if (stringArray.length == 1 && stringArray[0].length() > 0) {
            minecraftServer = MinecraftServer.getServer();
            gameProfile = minecraftServer.getPlayerProfileCache().getGameProfileForUsername(stringArray[0]);
            if (gameProfile == null) {
                throw new CommandException("commands.op.failed", stringArray[0]);
            }
        } else {
            throw new WrongUsageException("commands.op.usage", new Object[0]);
        }
        minecraftServer.getConfigurationManager().addOp(gameProfile);
        CommandOp.notifyOperators(iCommandSender, (ICommand)this, "commands.op.success", stringArray[0]);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        if (stringArray.length == 1) {
            String string = stringArray[stringArray.length - 1];
            ArrayList arrayList = Lists.newArrayList();
            GameProfile[] gameProfileArray = MinecraftServer.getServer().getGameProfiles();
            int n = gameProfileArray.length;
            int n2 = 0;
            while (n2 < n) {
                GameProfile gameProfile = gameProfileArray[n2];
                if (!MinecraftServer.getServer().getConfigurationManager().canSendCommands(gameProfile) && CommandOp.doesStringStartWith(string, gameProfile.getName())) {
                    arrayList.add(gameProfile.getName());
                }
                ++n2;
            }
            return arrayList;
        }
        return null;
    }
}

