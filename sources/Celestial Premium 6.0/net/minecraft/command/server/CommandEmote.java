/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command.server;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandEmote
extends CommandBase {
    @Override
    public String getCommandName() {
        return "me";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.me.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length <= 0) {
            throw new WrongUsageException("commands.me.usage", new Object[0]);
        }
        ITextComponent itextcomponent = CommandEmote.getChatComponentFromNthArg(sender, args, 0, !(sender instanceof EntityPlayer));
        server.getPlayerList().sendChatMsg(new TextComponentTranslation("chat.type.emote", sender.getDisplayName(), itextcomponent));
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return CommandEmote.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
    }
}

