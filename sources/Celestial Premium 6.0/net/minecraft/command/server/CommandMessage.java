/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command.server;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CommandMessage
extends CommandBase {
    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("w", "msg");
    }

    @Override
    public String getCommandName() {
        return "tell";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.message.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.message.usage", new Object[0]);
        }
        EntityPlayerMP entityplayer = CommandMessage.getPlayer(server, sender, args[0]);
        if (entityplayer == sender) {
            throw new PlayerNotFoundException("commands.message.sameTarget");
        }
        ITextComponent itextcomponent = CommandMessage.getChatComponentFromNthArg(sender, args, 1, !(sender instanceof EntityPlayer));
        TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("commands.message.display.incoming", sender.getDisplayName(), itextcomponent.createCopy());
        TextComponentTranslation textcomponenttranslation1 = new TextComponentTranslation("commands.message.display.outgoing", entityplayer.getDisplayName(), itextcomponent.createCopy());
        textcomponenttranslation.getStyle().setColor(TextFormatting.GRAY).setItalic(true);
        textcomponenttranslation1.getStyle().setColor(TextFormatting.GRAY).setItalic(true);
        ((Entity)entityplayer).addChatMessage(textcomponenttranslation);
        sender.addChatMessage(textcomponenttranslation1);
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return CommandMessage.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}

