// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command.server;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.ICommandSender;
import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;

public class CommandMessage extends CommandBase
{
    private static final String __OBFID = "CL_00000641";
    
    @Override
    public List getCommandAliases() {
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
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.message.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.message.usage", new Object[0]);
        }
        final EntityPlayerMP var3 = CommandBase.getPlayer(sender, args[0]);
        if (var3 == sender) {
            throw new PlayerNotFoundException("commands.message.sameTarget", new Object[0]);
        }
        final IChatComponent var4 = CommandBase.getChatComponentFromNthArg(sender, args, 1, !(sender instanceof EntityPlayer));
        final ChatComponentTranslation var5 = new ChatComponentTranslation("commands.message.display.incoming", new Object[] { sender.getDisplayName(), var4.createCopy() });
        final ChatComponentTranslation var6 = new ChatComponentTranslation("commands.message.display.outgoing", new Object[] { var3.getDisplayName(), var4.createCopy() });
        var5.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true);
        var6.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true);
        var3.addChatMessage(var5);
        sender.addChatMessage(var6);
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
