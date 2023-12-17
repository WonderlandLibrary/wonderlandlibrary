// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.command.CommandException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;

public class CommandMessage extends CommandBase
{
    @Override
    public List<String> getAliases() {
        return Arrays.asList("w", "msg");
    }
    
    @Override
    public String getName() {
        return "tell";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.message.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.message.usage", new Object[0]);
        }
        final EntityPlayer entityplayer = CommandBase.getPlayer(server, sender, args[0]);
        if (entityplayer == sender) {
            throw new PlayerNotFoundException("commands.message.sameTarget");
        }
        final ITextComponent itextcomponent = CommandBase.getChatComponentFromNthArg(sender, args, 1, !(sender instanceof EntityPlayer));
        final TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("commands.message.display.incoming", new Object[] { sender.getDisplayName(), itextcomponent.createCopy() });
        final TextComponentTranslation textcomponenttranslation2 = new TextComponentTranslation("commands.message.display.outgoing", new Object[] { entityplayer.getDisplayName(), itextcomponent.createCopy() });
        textcomponenttranslation.getStyle().setColor(TextFormatting.GRAY).setItalic(true);
        textcomponenttranslation2.getStyle().setColor(TextFormatting.GRAY).setItalic(true);
        entityplayer.sendMessage(textcomponenttranslation);
        sender.sendMessage(textcomponenttranslation2);
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        return CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
