/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command.server;

import com.google.gson.JsonParseException;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentUtils;

public class CommandMessageRaw
extends CommandBase {
    @Override
    public String getCommandName() {
        return "tellraw";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.tellraw.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.tellraw.usage", new Object[0]);
        }
        EntityPlayerMP entityplayer = CommandMessageRaw.getPlayer(server, sender, args[0]);
        String s = CommandMessageRaw.buildString(args, 1);
        try {
            ITextComponent itextcomponent = ITextComponent.Serializer.jsonToComponent(s);
            ((Entity)entityplayer).addChatMessage(TextComponentUtils.processComponent(sender, itextcomponent, entityplayer));
        }
        catch (JsonParseException jsonparseexception) {
            throw CommandMessageRaw.toSyntaxException(jsonparseexception);
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return args.length == 1 ? CommandMessageRaw.getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}

