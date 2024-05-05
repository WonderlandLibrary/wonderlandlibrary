package net.minecraft.command;

import com.google.gson.JsonParseException;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandTitle
extends CommandBase {
    private static final Logger LOGGER = LogManager.getLogger();

    public String getCommandName() {
        return "title";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.title.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.title.usage", new Object[0]);
        }
        if (args.length < 3) {
            if ("title".equals((Object)args[1]) || "subtitle".equals((Object)args[1])) {
                throw new WrongUsageException("commands.title.usage.title", new Object[0]);
            }
            if ("times".equals((Object)args[1])) {
                throw new WrongUsageException("commands.title.usage.times", new Object[0]);
            }
        }
        EntityPlayerMP entityplayermp = CommandTitle.getPlayer((ICommandSender)sender, (String)args[0]);
        S45PacketTitle.Type s45packettitle$type = S45PacketTitle.Type.byName((String)args[1]);
        if (s45packettitle$type != S45PacketTitle.Type.CLEAR && s45packettitle$type != S45PacketTitle.Type.RESET) {
            if (s45packettitle$type == S45PacketTitle.Type.TIMES) {
                if (args.length != 5) {
                    throw new WrongUsageException("commands.title.usage", new Object[0]);
                }
                int i = CommandTitle.parseInt((String)args[2]);
                int j = CommandTitle.parseInt((String)args[3]);
                int k = CommandTitle.parseInt((String)args[4]);
                S45PacketTitle s45packettitle2 = new S45PacketTitle(i, j, k);
                entityplayermp.playerNetServerHandler.sendPacket((Packet)s45packettitle2);
                CommandTitle.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.title.success", (Object[])new Object[0]);
            } else {
                IChatComponent ichatcomponent;
                if (args.length < 3) {
                    throw new WrongUsageException("commands.title.usage", new Object[0]);
                }
                String s = CommandTitle.buildString((String[])args, (int)2);
                try {
                    ichatcomponent = IChatComponent.Serializer.jsonToComponent((String)s);
                }
                catch (JsonParseException jsonparseexception) {
                    Throwable throwable = ExceptionUtils.getRootCause((Throwable)jsonparseexception);
                    throw new SyntaxErrorException("commands.tellraw.jsonException", new Object[]{throwable == null ? "" : throwable.getMessage()});
                }
                S45PacketTitle s45packettitle1 = new S45PacketTitle(s45packettitle$type, ChatComponentProcessor.processComponent((ICommandSender)sender, (IChatComponent)ichatcomponent, (Entity)entityplayermp));
                entityplayermp.playerNetServerHandler.sendPacket((Packet)s45packettitle1);
                CommandTitle.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.title.success", (Object[])new Object[0]);
            }
        } else {
            if (args.length != 2) {
                throw new WrongUsageException("commands.title.usage", new Object[0]);
            }
            S45PacketTitle s45packettitle = new S45PacketTitle(s45packettitle$type, (IChatComponent)null);
            entityplayermp.playerNetServerHandler.sendPacket((Packet)s45packettitle);
            CommandTitle.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.title.success", (Object[])new Object[0]);
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandTitle.getListOfStringsMatchingLastWord((String[])args, (String[])MinecraftServer.getServer().getAllUsernames()) : (args.length == 2 ? CommandTitle.getListOfStringsMatchingLastWord((String[])args, (String[])S45PacketTitle.Type.getNames()) : null);
    }

    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}
