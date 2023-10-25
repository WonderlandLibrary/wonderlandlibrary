/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;

public class CommandSaveAll
extends CommandBase {
    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        MinecraftServer minecraftServer = MinecraftServer.getServer();
        iCommandSender.addChatMessage(new ChatComponentTranslation("commands.save.start", new Object[0]));
        if (minecraftServer.getConfigurationManager() != null) {
            minecraftServer.getConfigurationManager().saveAllPlayerData();
        }
        try {
            boolean bl;
            WorldServer worldServer;
            int n = 0;
            while (n < minecraftServer.worldServers.length) {
                if (minecraftServer.worldServers[n] != null) {
                    worldServer = minecraftServer.worldServers[n];
                    bl = worldServer.disableLevelSaving;
                    worldServer.disableLevelSaving = false;
                    worldServer.saveAllChunks(true, null);
                    worldServer.disableLevelSaving = bl;
                }
                ++n;
            }
            if (stringArray.length > 0 && "flush".equals(stringArray[0])) {
                iCommandSender.addChatMessage(new ChatComponentTranslation("commands.save.flushStart", new Object[0]));
                n = 0;
                while (n < minecraftServer.worldServers.length) {
                    if (minecraftServer.worldServers[n] != null) {
                        worldServer = minecraftServer.worldServers[n];
                        bl = worldServer.disableLevelSaving;
                        worldServer.disableLevelSaving = false;
                        worldServer.saveChunkData();
                        worldServer.disableLevelSaving = bl;
                    }
                    ++n;
                }
                iCommandSender.addChatMessage(new ChatComponentTranslation("commands.save.flushEnd", new Object[0]));
            }
        }
        catch (MinecraftException minecraftException) {
            CommandSaveAll.notifyOperators(iCommandSender, (ICommand)this, "commands.save.failed", minecraftException.getMessage());
            return;
        }
        CommandSaveAll.notifyOperators(iCommandSender, (ICommand)this, "commands.save.success", new Object[0]);
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.save.usage";
    }

    @Override
    public String getCommandName() {
        return "save-all";
    }
}

