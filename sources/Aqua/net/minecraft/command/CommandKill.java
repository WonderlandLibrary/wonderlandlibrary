package net.minecraft.command;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandKill
extends CommandBase {
    public String getCommandName() {
        return "kill";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.kill.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            EntityPlayerMP entityplayer = CommandKill.getCommandSenderAsPlayer((ICommandSender)sender);
            entityplayer.onKillCommand();
            CommandKill.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.kill.successful", (Object[])new Object[]{entityplayer.getDisplayName()});
        } else {
            Entity entity = CommandKill.getEntity((ICommandSender)sender, (String)args[0]);
            entity.onKillCommand();
            CommandKill.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.kill.successful", (Object[])new Object[]{entity.getDisplayName()});
        }
    }

    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandKill.getListOfStringsMatchingLastWord((String[])args, (String[])MinecraftServer.getServer().getAllUsernames()) : null;
    }
}
