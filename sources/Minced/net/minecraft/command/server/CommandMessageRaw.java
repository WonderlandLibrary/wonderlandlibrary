// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayer;
import com.google.gson.JsonParseException;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandMessageRaw extends CommandBase
{
    @Override
    public String getName() {
        return "tellraw";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.tellraw.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.tellraw.usage", new Object[0]);
        }
        final EntityPlayer entityplayer = CommandBase.getPlayer(server, sender, args[0]);
        final String s = CommandBase.buildString(args, 1);
        try {
            final ITextComponent itextcomponent = ITextComponent.Serializer.jsonToComponent(s);
            entityplayer.sendMessage(TextComponentUtils.processComponent(sender, itextcomponent, entityplayer));
        }
        catch (JsonParseException jsonparseexception) {
            throw CommandBase.toSyntaxException(jsonparseexception);
        }
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
