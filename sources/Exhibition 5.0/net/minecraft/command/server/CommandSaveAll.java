// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command.server;

import net.minecraft.command.CommandException;
import net.minecraft.world.WorldServer;
import net.minecraft.world.MinecraftException;
import net.minecraft.command.ICommand;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandSaveAll extends CommandBase
{
    private static final String __OBFID = "CL_00000826";
    
    @Override
    public String getCommandName() {
        return "save-all";
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.save.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        final MinecraftServer var3 = MinecraftServer.getServer();
        sender.addChatMessage(new ChatComponentTranslation("commands.save.start", new Object[0]));
        if (var3.getConfigurationManager() != null) {
            var3.getConfigurationManager().saveAllPlayerData();
        }
        try {
            for (int var4 = 0; var4 < var3.worldServers.length; ++var4) {
                if (var3.worldServers[var4] != null) {
                    final WorldServer var5 = var3.worldServers[var4];
                    final boolean var6 = var5.disableLevelSaving;
                    var5.disableLevelSaving = false;
                    var5.saveAllChunks(true, null);
                    var5.disableLevelSaving = var6;
                }
            }
            if (args.length > 0 && "flush".equals(args[0])) {
                sender.addChatMessage(new ChatComponentTranslation("commands.save.flushStart", new Object[0]));
                for (int var4 = 0; var4 < var3.worldServers.length; ++var4) {
                    if (var3.worldServers[var4] != null) {
                        final WorldServer var5 = var3.worldServers[var4];
                        final boolean var6 = var5.disableLevelSaving;
                        var5.disableLevelSaving = false;
                        var5.saveChunkData();
                        var5.disableLevelSaving = var6;
                    }
                }
                sender.addChatMessage(new ChatComponentTranslation("commands.save.flushEnd", new Object[0]));
            }
        }
        catch (MinecraftException var7) {
            CommandBase.notifyOperators(sender, this, "commands.save.failed", var7.getMessage());
            return;
        }
        CommandBase.notifyOperators(sender, this, "commands.save.success", new Object[0]);
    }
}
