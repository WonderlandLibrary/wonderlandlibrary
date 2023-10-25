/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command.server;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class CommandEmote
extends CommandBase {
    @Override
    public String getCommandName() {
        return "me";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.me.usage";
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return CommandEmote.getListOfStringsMatchingLastWord(stringArray, MinecraftServer.getServer().getAllUsernames());
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        if (stringArray.length <= 0) {
            throw new WrongUsageException("commands.me.usage", new Object[0]);
        }
        IChatComponent iChatComponent = CommandEmote.getChatComponentFromNthArg(iCommandSender, stringArray, 0, !(iCommandSender instanceof EntityPlayer));
        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentTranslation("chat.type.emote", iCommandSender.getDisplayName(), iChatComponent));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}

