/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandGameMode;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.WorldSettings;

public class CommandDefaultGameMode
extends CommandGameMode {
    private static final String __OBFID = "CL_00000296";

    @Override
    public String getCommandName() {
        return "defaultgamemode";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.defaultgamemode.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length <= 0) {
            throw new WrongUsageException("commands.defaultgamemode.usage", new Object[0]);
        }
        WorldSettings.GameType var3 = this.getGameModeFromCommand(sender, args[0]);
        this.setGameType(var3);
        CommandDefaultGameMode.notifyOperators(sender, (ICommand)this, "commands.defaultgamemode.success", new ChatComponentTranslation("gameMode." + var3.getName(), new Object[0]));
    }

    protected void setGameType(WorldSettings.GameType p_71541_1_) {
        MinecraftServer var2 = MinecraftServer.getServer();
        var2.setGameType(p_71541_1_);
        if (var2.getForceGamemode()) {
            for (EntityPlayerMP var4 : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
                var4.setGameType(p_71541_1_);
                var4.fallDistance = 0.0f;
            }
        }
    }
}

