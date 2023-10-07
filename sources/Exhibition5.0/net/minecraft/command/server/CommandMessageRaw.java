// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command.server;

import net.minecraft.server.MinecraftServer;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonParseException;
import net.minecraft.command.SyntaxErrorException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.IChatComponent;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandMessageRaw extends CommandBase
{
    private static final String __OBFID = "CL_00000667";
    
    @Override
    public String getCommandName() {
        return "tellraw";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.tellraw.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.tellraw.usage", new Object[0]);
        }
        final EntityPlayerMP var3 = CommandBase.getPlayer(sender, args[0]);
        final String var4 = CommandBase.func_180529_a(args, 1);
        try {
            final IChatComponent var5 = IChatComponent.Serializer.jsonToComponent(var4);
            var3.addChatMessage(ChatComponentProcessor.func_179985_a(sender, var5, var3));
        }
        catch (JsonParseException var7) {
            final Throwable var6 = ExceptionUtils.getRootCause((Throwable)var7);
            throw new SyntaxErrorException("commands.tellraw.jsonException", new Object[] { (var6 == null) ? "" : var6.getMessage() });
        }
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
