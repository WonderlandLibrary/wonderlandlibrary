/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandServerKick
extends CommandBase {
    @Override
    public String getCommandName() {
        return "kick";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.kick.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0 && args[0].length() > 1) {
            EntityPlayerMP entityplayermp = server.getPlayerList().getPlayerByUsername(args[0]);
            if (entityplayermp == null) {
                throw new PlayerNotFoundException("commands.generic.player.notFound", args[0]);
            }
            if (args.length >= 2) {
                ITextComponent itextcomponent = CommandServerKick.getChatComponentFromNthArg(sender, args, 1);
                entityplayermp.connection.func_194028_b(itextcomponent);
                CommandServerKick.notifyCommandListener(sender, (ICommand)this, "commands.kick.success.reason", entityplayermp.getName(), itextcomponent.getUnformattedText());
            } else {
                entityplayermp.connection.func_194028_b(new TextComponentTranslation("multiplayer.disconnect.kicked", new Object[0]));
                CommandServerKick.notifyCommandListener(sender, (ICommand)this, "commands.kick.success", entityplayermp.getName());
            }
        } else {
            throw new WrongUsageException("commands.kick.usage", new Object[0]);
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return args.length >= 1 ? CommandServerKick.getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.emptyList();
    }
}

