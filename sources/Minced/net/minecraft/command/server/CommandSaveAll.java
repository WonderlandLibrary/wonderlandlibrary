// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.command.CommandException;
import net.minecraft.world.WorldServer;
import net.minecraft.world.MinecraftException;
import net.minecraft.command.ICommand;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandSaveAll extends CommandBase
{
    @Override
    public String getName() {
        return "save-all";
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.save.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        sender.sendMessage(new TextComponentTranslation("commands.save.start", new Object[0]));
        if (server.getPlayerList() != null) {
            server.getPlayerList().saveAllPlayerData();
        }
        try {
            for (int i = 0; i < server.worlds.length; ++i) {
                if (server.worlds[i] != null) {
                    final WorldServer worldserver = server.worlds[i];
                    final boolean flag = worldserver.disableLevelSaving;
                    worldserver.disableLevelSaving = false;
                    worldserver.saveAllChunks(true, null);
                    worldserver.disableLevelSaving = flag;
                }
            }
            if (args.length > 0 && "flush".equals(args[0])) {
                sender.sendMessage(new TextComponentTranslation("commands.save.flushStart", new Object[0]));
                for (int j = 0; j < server.worlds.length; ++j) {
                    if (server.worlds[j] != null) {
                        final WorldServer worldserver2 = server.worlds[j];
                        final boolean flag2 = worldserver2.disableLevelSaving;
                        worldserver2.disableLevelSaving = false;
                        worldserver2.flushToDisk();
                        worldserver2.disableLevelSaving = flag2;
                    }
                }
                sender.sendMessage(new TextComponentTranslation("commands.save.flushEnd", new Object[0]));
            }
        }
        catch (MinecraftException minecraftexception) {
            CommandBase.notifyCommandListener(sender, this, "commands.save.failed", minecraftexception.getMessage());
            return;
        }
        CommandBase.notifyCommandListener(sender, this, "commands.save.success", new Object[0]);
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, "flush") : Collections.emptyList();
    }
}
