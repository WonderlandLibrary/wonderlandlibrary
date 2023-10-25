/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import com.google.gson.JsonParseException;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandTitle
extends CommandBase {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public String getCommandName() {
        return "title";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.title.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.title.usage", new Object[0]);
        }
        if (args.length < 3) {
            if ("title".equals(args[1]) || "subtitle".equals(args[1]) || "actionbar".equals(args[1])) {
                throw new WrongUsageException("commands.title.usage.title", new Object[0]);
            }
            if ("times".equals(args[1])) {
                throw new WrongUsageException("commands.title.usage.times", new Object[0]);
            }
        }
        EntityPlayerMP entityplayermp = CommandTitle.getPlayer(server, sender, args[0]);
        SPacketTitle.Type spackettitle$type = SPacketTitle.Type.byName(args[1]);
        if (spackettitle$type != SPacketTitle.Type.CLEAR && spackettitle$type != SPacketTitle.Type.RESET) {
            if (spackettitle$type == SPacketTitle.Type.TIMES) {
                if (args.length != 5) {
                    throw new WrongUsageException("commands.title.usage", new Object[0]);
                }
                int i = CommandTitle.parseInt(args[2]);
                int j = CommandTitle.parseInt(args[3]);
                int k = CommandTitle.parseInt(args[4]);
                SPacketTitle spackettitle2 = new SPacketTitle(i, j, k);
                entityplayermp.connection.sendPacket(spackettitle2);
                CommandTitle.notifyCommandListener(sender, (ICommand)this, "commands.title.success", new Object[0]);
            } else {
                ITextComponent itextcomponent;
                if (args.length < 3) {
                    throw new WrongUsageException("commands.title.usage", new Object[0]);
                }
                String s = CommandTitle.buildString(args, 2);
                try {
                    itextcomponent = ITextComponent.Serializer.jsonToComponent(s);
                }
                catch (JsonParseException jsonparseexception) {
                    throw CommandTitle.toSyntaxException(jsonparseexception);
                }
                SPacketTitle spackettitle1 = new SPacketTitle(spackettitle$type, TextComponentUtils.processComponent(sender, itextcomponent, entityplayermp));
                entityplayermp.connection.sendPacket(spackettitle1);
                CommandTitle.notifyCommandListener(sender, (ICommand)this, "commands.title.success", new Object[0]);
            }
        } else {
            if (args.length != 2) {
                throw new WrongUsageException("commands.title.usage", new Object[0]);
            }
            SPacketTitle spackettitle = new SPacketTitle(spackettitle$type, null);
            entityplayermp.connection.sendPacket(spackettitle);
            CommandTitle.notifyCommandListener(sender, (ICommand)this, "commands.title.success", new Object[0]);
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            return CommandTitle.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
        }
        return args.length == 2 ? CommandTitle.getListOfStringsMatchingLastWord(args, SPacketTitle.Type.getNames()) : Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}

