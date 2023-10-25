/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonParseException
 *  org.apache.commons.lang3.exception.ExceptionUtils
 */
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
    @Override
    public String getCommandName() {
        return "tellraw";
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 1 ? CommandMessageRaw.getListOfStringsMatchingLastWord(stringArray, MinecraftServer.getServer().getAllUsernames()) : null;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.tellraw.usage";
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        if (stringArray.length < 2) {
            throw new WrongUsageException("commands.tellraw.usage", new Object[0]);
        }
        EntityPlayerMP entityPlayerMP = CommandMessageRaw.getPlayer(iCommandSender, stringArray[0]);
        String string = CommandMessageRaw.buildString(stringArray, 1);
        try {
            IChatComponent iChatComponent = IChatComponent.Serializer.jsonToComponent(string);
            ((Entity)entityPlayerMP).addChatMessage(ChatComponentProcessor.processComponent(iCommandSender, iChatComponent, entityPlayerMP));
        }
        catch (JsonParseException jsonParseException) {
            Throwable throwable = ExceptionUtils.getRootCause((Throwable)jsonParseException);
            throw new SyntaxErrorException("commands.tellraw.jsonException", throwable == null ? "" : throwable.getMessage());
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public boolean isUsernameIndex(String[] stringArray, int n) {
        return n == 0;
    }
}

