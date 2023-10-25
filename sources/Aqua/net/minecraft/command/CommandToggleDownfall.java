package net.minecraft.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.WorldInfo;

public class CommandToggleDownfall
extends CommandBase {
    public String getCommandName() {
        return "toggledownfall";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.downfall.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        this.toggleDownfall();
        CommandToggleDownfall.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.downfall.success", (Object[])new Object[0]);
    }

    protected void toggleDownfall() {
        WorldInfo worldinfo;
        worldinfo.setRaining(!(worldinfo = MinecraftServer.getServer().worldServers[0].getWorldInfo()).isRaining());
    }
}
