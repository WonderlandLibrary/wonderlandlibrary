/*
 * Decompiled with CFR 0.150.
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
    private static final String __OBFID = "CL_00000826";

    @Override
    public String getCommandName() {
        return "save-all";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.save.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        MinecraftServer var3 = MinecraftServer.getServer();
        sender.addChatMessage(new ChatComponentTranslation("commands.save.start", new Object[0]));
        if (var3.getConfigurationManager() != null) {
            var3.getConfigurationManager().saveAllPlayerData();
        }
        try {
            boolean var6;
            WorldServer var5;
            int var4;
            for (var4 = 0; var4 < var3.worldServers.length; ++var4) {
                if (var3.worldServers[var4] == null) continue;
                var5 = var3.worldServers[var4];
                var6 = var5.disableLevelSaving;
                var5.disableLevelSaving = false;
                var5.saveAllChunks(true, null);
                var5.disableLevelSaving = var6;
            }
            if (args.length > 0 && "flush".equals(args[0])) {
                sender.addChatMessage(new ChatComponentTranslation("commands.save.flushStart", new Object[0]));
                for (var4 = 0; var4 < var3.worldServers.length; ++var4) {
                    if (var3.worldServers[var4] == null) continue;
                    var5 = var3.worldServers[var4];
                    var6 = var5.disableLevelSaving;
                    var5.disableLevelSaving = false;
                    var5.saveChunkData();
                    var5.disableLevelSaving = var6;
                }
                sender.addChatMessage(new ChatComponentTranslation("commands.save.flushEnd", new Object[0]));
            }
        }
        catch (MinecraftException var7) {
            CommandSaveAll.notifyOperators(sender, (ICommand)this, "commands.save.failed", var7.getMessage());
            return;
        }
        CommandSaveAll.notifyOperators(sender, (ICommand)this, "commands.save.success", new Object[0]);
    }
}

