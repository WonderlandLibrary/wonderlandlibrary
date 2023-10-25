/*
 * Decompiled with CFR 0.152.
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
    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        GameProfile gameProfile;
        MinecraftServer minecraftServer;
        if (stringArray.length == 1 && stringArray[0].length() > 0) {
            minecraftServer = MinecraftServer.getServer();
            gameProfile = minecraftServer.getConfigurationManager().getOppedPlayers().getGameProfileFromName(stringArray[0]);
            if (gameProfile == null) {
                throw new CommandException("commands.deop.failed", stringArray[0]);
            }
        } else {
            throw new WrongUsageException("commands.deop.usage", new Object[0]);
        }
        minecraftServer.getConfigurationManager().removeOp(gameProfile);
        CommandDeOp.notifyOperators(iCommandSender, (ICommand)this, "commands.deop.success", stringArray[0]);
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.deop.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getCommandName() {
        return "deop";
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 1 ? CommandDeOp.getListOfStringsMatchingLastWord(stringArray, MinecraftServer.getServer().getConfigurationManager().getOppedPlayerNames()) : null;
    }
}

