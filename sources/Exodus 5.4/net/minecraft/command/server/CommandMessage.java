/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command.server;

import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class CommandMessage
extends CommandBase {
    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        if (stringArray.length < 2) {
            throw new WrongUsageException("commands.message.usage", new Object[0]);
        }
        EntityPlayerMP entityPlayerMP = CommandMessage.getPlayer(iCommandSender, stringArray[0]);
        if (entityPlayerMP == iCommandSender) {
            throw new PlayerNotFoundException("commands.message.sameTarget", new Object[0]);
        }
        IChatComponent iChatComponent = CommandMessage.getChatComponentFromNthArg(iCommandSender, stringArray, 1, !(iCommandSender instanceof EntityPlayer));
        ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("commands.message.display.incoming", iCommandSender.getDisplayName(), iChatComponent.createCopy());
        ChatComponentTranslation chatComponentTranslation2 = new ChatComponentTranslation("commands.message.display.outgoing", entityPlayerMP.getDisplayName(), iChatComponent.createCopy());
        chatComponentTranslation.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true);
        chatComponentTranslation2.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true);
        ((Entity)entityPlayerMP).addChatMessage(chatComponentTranslation);
        iCommandSender.addChatMessage(chatComponentTranslation2);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return CommandMessage.getListOfStringsMatchingLastWord(stringArray, MinecraftServer.getServer().getAllUsernames());
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("w", "msg");
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.message.usage";
    }

    @Override
    public boolean isUsernameIndex(String[] stringArray, int n) {
        return n == 0;
    }

    @Override
    public String getCommandName() {
        return "tell";
    }
}

