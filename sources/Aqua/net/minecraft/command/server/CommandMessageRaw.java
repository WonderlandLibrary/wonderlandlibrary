package net.minecraft.command.server;

import com.google.gson.JsonParseException;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class CommandMessageRaw
extends CommandBase {
    public String getCommandName() {
        return "tellraw";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.tellraw.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.tellraw.usage", new Object[0]);
        }
        EntityPlayerMP entityplayer = CommandMessageRaw.getPlayer((ICommandSender)sender, (String)args[0]);
        String s = CommandMessageRaw.buildString((String[])args, (int)1);
        try {
            IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent((String)s);
            entityplayer.addChatMessage(ChatComponentProcessor.processComponent((ICommandSender)sender, (IChatComponent)ichatcomponent, (Entity)entityplayer));
        }
        catch (JsonParseException jsonparseexception) {
            Throwable throwable = ExceptionUtils.getRootCause((Throwable)jsonparseexception);
            throw new SyntaxErrorException("commands.tellraw.jsonException", new Object[]{throwable == null ? "" : throwable.getMessage()});
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandMessageRaw.getListOfStringsMatchingLastWord((String[])args, (String[])MinecraftServer.getServer().getAllUsernames()) : null;
    }

    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}
