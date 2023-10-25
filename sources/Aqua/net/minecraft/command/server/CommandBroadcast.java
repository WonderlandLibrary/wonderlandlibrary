package net.minecraft.command.server;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class CommandBroadcast
extends CommandBase {
    public String getCommandName() {
        return "say";
    }

    public int getRequiredPermissionLevel() {
        return 1;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.say.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length <= 0 || args[0].length() <= 0) {
            throw new WrongUsageException("commands.say.usage", new Object[0]);
        }
        IChatComponent ichatcomponent = CommandBroadcast.getChatComponentFromNthArg((ICommandSender)sender, (String[])args, (int)0, (boolean)true);
        MinecraftServer.getServer().getConfigurationManager().sendChatMsg((IChatComponent)new ChatComponentTranslation("chat.type.announcement", new Object[]{sender.getDisplayName(), ichatcomponent}));
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length >= 1 ? CommandBroadcast.getListOfStringsMatchingLastWord((String[])args, (String[])MinecraftServer.getServer().getAllUsernames()) : null;
    }
}
