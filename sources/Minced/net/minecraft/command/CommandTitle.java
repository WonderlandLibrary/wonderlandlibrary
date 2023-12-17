// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import org.apache.logging.log4j.LogManager;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.TextComponentUtils;
import com.google.gson.JsonParseException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;

public class CommandTitle extends CommandBase
{
    private static final Logger LOGGER;
    
    @Override
    public String getName() {
        return "title";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.title.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
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
        final EntityPlayerMP entityplayermp = CommandBase.getPlayer(server, sender, args[0]);
        final SPacketTitle.Type spackettitle$type = SPacketTitle.Type.byName(args[1]);
        if (spackettitle$type != SPacketTitle.Type.CLEAR && spackettitle$type != SPacketTitle.Type.RESET) {
            if (spackettitle$type == SPacketTitle.Type.TIMES) {
                if (args.length != 5) {
                    throw new WrongUsageException("commands.title.usage", new Object[0]);
                }
                final int i = CommandBase.parseInt(args[2]);
                final int j = CommandBase.parseInt(args[3]);
                final int k = CommandBase.parseInt(args[4]);
                final SPacketTitle spackettitle2 = new SPacketTitle(i, j, k);
                entityplayermp.connection.sendPacket(spackettitle2);
                CommandBase.notifyCommandListener(sender, this, "commands.title.success", new Object[0]);
            }
            else {
                if (args.length < 3) {
                    throw new WrongUsageException("commands.title.usage", new Object[0]);
                }
                final String s = CommandBase.buildString(args, 2);
                ITextComponent itextcomponent;
                try {
                    itextcomponent = ITextComponent.Serializer.jsonToComponent(s);
                }
                catch (JsonParseException jsonparseexception) {
                    throw CommandBase.toSyntaxException(jsonparseexception);
                }
                final SPacketTitle spackettitle3 = new SPacketTitle(spackettitle$type, TextComponentUtils.processComponent(sender, itextcomponent, entityplayermp));
                entityplayermp.connection.sendPacket(spackettitle3);
                CommandBase.notifyCommandListener(sender, this, "commands.title.success", new Object[0]);
            }
        }
        else {
            if (args.length != 2) {
                throw new WrongUsageException("commands.title.usage", new Object[0]);
            }
            final SPacketTitle spackettitle4 = new SPacketTitle(spackettitle$type, null);
            entityplayermp.connection.sendPacket(spackettitle4);
            CommandBase.notifyCommandListener(sender, this, "commands.title.success", new Object[0]);
        }
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        return (args.length == 2) ? CommandBase.getListOfStringsMatchingLastWord(args, SPacketTitle.Type.getNames()) : Collections.emptyList();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
