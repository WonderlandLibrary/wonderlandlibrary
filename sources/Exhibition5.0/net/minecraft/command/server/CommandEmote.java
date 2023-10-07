// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command.server;

import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.command.CommandException;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandEmote extends CommandBase
{
    private static final String __OBFID = "CL_00000351";
    
    @Override
    public String getCommandName() {
        return "me";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.me.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length <= 0) {
            throw new WrongUsageException("commands.me.usage", new Object[0]);
        }
        final IChatComponent var3 = CommandBase.getChatComponentFromNthArg(sender, args, 0, !(sender instanceof EntityPlayer));
        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentTranslation("chat.type.emote", new Object[] { sender.getDisplayName(), var3 }));
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    }
}
