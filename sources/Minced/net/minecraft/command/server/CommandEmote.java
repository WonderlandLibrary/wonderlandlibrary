// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.command.CommandException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandEmote extends CommandBase
{
    @Override
    public String getName() {
        return "me";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.me.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length <= 0) {
            throw new WrongUsageException("commands.me.usage", new Object[0]);
        }
        final ITextComponent itextcomponent = CommandBase.getChatComponentFromNthArg(sender, args, 0, !(sender instanceof EntityPlayer));
        server.getPlayerList().sendMessage(new TextComponentTranslation("chat.type.emote", new Object[] { sender.getDisplayName(), itextcomponent }));
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        return CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
    }
}
