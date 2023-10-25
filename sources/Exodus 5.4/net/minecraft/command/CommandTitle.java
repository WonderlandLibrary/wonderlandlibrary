/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonParseException
 *  org.apache.commons.lang3.exception.ExceptionUtils
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
    public boolean isUsernameIndex(String[] stringArray, int n) {
        return n == 0;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 1 ? CommandTitle.getListOfStringsMatchingLastWord(stringArray, MinecraftServer.getServer().getAllUsernames()) : (stringArray.length == 2 ? CommandTitle.getListOfStringsMatchingLastWord(stringArray, S45PacketTitle.Type.getNames()) : null);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.title.usage";
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        if (stringArray.length < 2) {
            throw new WrongUsageException("commands.title.usage", new Object[0]);
        }
        if (stringArray.length < 3) {
            if ("title".equals(stringArray[1]) || "subtitle".equals(stringArray[1])) {
                throw new WrongUsageException("commands.title.usage.title", new Object[0]);
            }
            if ("times".equals(stringArray[1])) {
                throw new WrongUsageException("commands.title.usage.times", new Object[0]);
            }
        }
        EntityPlayerMP entityPlayerMP = CommandTitle.getPlayer(iCommandSender, stringArray[0]);
        S45PacketTitle.Type type = S45PacketTitle.Type.byName(stringArray[1]);
        if (type != S45PacketTitle.Type.CLEAR && type != S45PacketTitle.Type.RESET) {
            if (type == S45PacketTitle.Type.TIMES) {
                if (stringArray.length != 5) {
                    throw new WrongUsageException("commands.title.usage", new Object[0]);
                }
                int n = CommandTitle.parseInt(stringArray[2]);
                int n2 = CommandTitle.parseInt(stringArray[3]);
                int n3 = CommandTitle.parseInt(stringArray[4]);
                S45PacketTitle s45PacketTitle = new S45PacketTitle(n, n2, n3);
                entityPlayerMP.playerNetServerHandler.sendPacket(s45PacketTitle);
                CommandTitle.notifyOperators(iCommandSender, (ICommand)this, "commands.title.success", new Object[0]);
            } else {
                IChatComponent iChatComponent;
                if (stringArray.length < 3) {
                    throw new WrongUsageException("commands.title.usage", new Object[0]);
                }
                String string = CommandTitle.buildString(stringArray, 2);
                try {
                    iChatComponent = IChatComponent.Serializer.jsonToComponent(string);
                }
                catch (JsonParseException jsonParseException) {
                    Throwable throwable = ExceptionUtils.getRootCause((Throwable)jsonParseException);
                    throw new SyntaxErrorException("commands.tellraw.jsonException", throwable == null ? "" : throwable.getMessage());
                }
                S45PacketTitle s45PacketTitle = new S45PacketTitle(type, ChatComponentProcessor.processComponent(iCommandSender, iChatComponent, entityPlayerMP));
                entityPlayerMP.playerNetServerHandler.sendPacket(s45PacketTitle);
                CommandTitle.notifyOperators(iCommandSender, (ICommand)this, "commands.title.success", new Object[0]);
            }
        } else {
            if (stringArray.length != 2) {
                throw new WrongUsageException("commands.title.usage", new Object[0]);
            }
            S45PacketTitle s45PacketTitle = new S45PacketTitle(type, null);
            entityPlayerMP.playerNetServerHandler.sendPacket(s45PacketTitle);
            CommandTitle.notifyOperators(iCommandSender, (ICommand)this, "commands.title.success", new Object[0]);
        }
    }

    @Override
    public String getCommandName() {
        return "title";
    }
}

