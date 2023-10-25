/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

public class CommandListPlayers
extends CommandBase {
    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.players.usage";
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        int n = MinecraftServer.getServer().getCurrentPlayerCount();
        iCommandSender.addChatMessage(new ChatComponentTranslation("commands.players.list", n, MinecraftServer.getServer().getMaxPlayers()));
        iCommandSender.addChatMessage(new ChatComponentText(MinecraftServer.getServer().getConfigurationManager().func_181058_b(stringArray.length > 0 && "uuids".equalsIgnoreCase(stringArray[0]))));
        iCommandSender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, n);
    }

    @Override
    public String getCommandName() {
        return "list";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}

