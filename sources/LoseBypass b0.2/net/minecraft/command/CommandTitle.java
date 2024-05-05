/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.command;

import com.google.gson.JsonParseException;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
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

    @Override
    public String getCommandName() {
        return "title";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.title.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.title.usage", new Object[0]);
        }
        if (args.length < 3) {
            if ("title".equals(args[1])) throw new WrongUsageException("commands.title.usage.title", new Object[0]);
            if ("subtitle".equals(args[1])) {
                throw new WrongUsageException("commands.title.usage.title", new Object[0]);
            }
            if ("times".equals(args[1])) {
                throw new WrongUsageException("commands.title.usage.times", new Object[0]);
            }
        }
        EntityPlayerMP entityplayermp = CommandTitle.getPlayer(sender, args[0]);
        S45PacketTitle.Type s45packettitle$type = S45PacketTitle.Type.byName(args[1]);
        if (s45packettitle$type != S45PacketTitle.Type.CLEAR && s45packettitle$type != S45PacketTitle.Type.RESET) {
            IChatComponent ichatcomponent;
            if (s45packettitle$type == S45PacketTitle.Type.TIMES) {
                if (args.length != 5) {
                    throw new WrongUsageException("commands.title.usage", new Object[0]);
                }
                int i = CommandTitle.parseInt(args[2]);
                int j = CommandTitle.parseInt(args[3]);
                int k = CommandTitle.parseInt(args[4]);
                S45PacketTitle s45packettitle2 = new S45PacketTitle(i, j, k);
                entityplayermp.playerNetServerHandler.sendPacket(s45packettitle2);
                CommandTitle.notifyOperators(sender, (ICommand)this, "commands.title.success", new Object[0]);
                return;
            }
            if (args.length < 3) {
                throw new WrongUsageException("commands.title.usage", new Object[0]);
            }
            String s = CommandTitle.buildString(args, 2);
            try {
                ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
            }
            catch (JsonParseException jsonparseexception) {
                Throwable throwable = ExceptionUtils.getRootCause(jsonparseexception);
                throw new SyntaxErrorException("commands.tellraw.jsonException", throwable == null ? "" : throwable.getMessage());
            }
            S45PacketTitle s45packettitle1 = new S45PacketTitle(s45packettitle$type, ChatComponentProcessor.processComponent(sender, ichatcomponent, entityplayermp));
            entityplayermp.playerNetServerHandler.sendPacket(s45packettitle1);
            CommandTitle.notifyOperators(sender, (ICommand)this, "commands.title.success", new Object[0]);
            return;
        }
        if (args.length != 2) {
            throw new WrongUsageException("commands.title.usage", new Object[0]);
        }
        S45PacketTitle s45packettitle = new S45PacketTitle(s45packettitle$type, null);
        entityplayermp.playerNetServerHandler.sendPacket(s45packettitle);
        CommandTitle.notifyOperators(sender, (ICommand)this, "commands.title.success", new Object[0]);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        List<String> list;
        if (args.length == 1) {
            list = CommandTitle.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
            return list;
        }
        if (args.length != 2) return null;
        list = CommandTitle.getListOfStringsMatchingLastWord(args, S45PacketTitle.Type.getNames());
        return list;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        if (index != 0) return false;
        return true;
    }
}

