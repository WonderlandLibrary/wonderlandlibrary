package net.minecraft.command;

import com.google.gson.JsonParseException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.List;

public class CommandTitle extends CommandBase {

    /**
     * Gets the name of the command
     */
    public String getCommandName() {
        return "title";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel() {
        return 2;
    }

    /**
     * Gets the usage string for the command.
     */
    public String getCommandUsage(ICommandSender sender) {
        return "commands.title.usage";
    }

    /**
     * Callback when the command is invoked
     */
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.title.usage");
        } else {
            if (args.length < 3) {
                if ("title".equals(args[1]) || "subtitle".equals(args[1])) {
                    throw new WrongUsageException("commands.title.usage.title");
                }

                if ("times".equals(args[1])) {
                    throw new WrongUsageException("commands.title.usage.times");
                }
            }

            final EntityPlayerMP entityplayermp = getPlayer(sender, args[0]);
            final S45PacketTitle.Type type = S45PacketTitle.Type.byName(args[1]);

            if (type != S45PacketTitle.Type.CLEAR && type != S45PacketTitle.Type.RESET) {
                if (type == S45PacketTitle.Type.TIMES) {
                    if (args.length != 5) {
                        throw new WrongUsageException("commands.title.usage");
                    } else {
                        final int i = parseInt(args[2]);
                        final int j = parseInt(args[3]);
                        final int k = parseInt(args[4]);
                        final S45PacketTitle s45PacketTitle = new S45PacketTitle(i, j, k);
                        entityplayermp.playerNetServerHandler.sendPacket(s45PacketTitle);
                        notifyOperators(sender, this, "commands.title.success");
                    }
                } else if (args.length < 3) {
                    throw new WrongUsageException("commands.title.usage");
                } else {
                    final String s = buildString(args, 2);
                    final IChatComponent ichatcomponent;

                    try {
                        ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
                    } catch (JsonParseException jsonparseexception) {
                        final Throwable throwable = ExceptionUtils.getRootCause(jsonparseexception);
                        throw new SyntaxErrorException("commands.tellraw.jsonException", throwable == null ? "" : throwable.getMessage());
                    }

                    final S45PacketTitle s45packettitle1 = new S45PacketTitle(type, ChatComponentProcessor.processComponent(sender, ichatcomponent, entityplayermp));
                    entityplayermp.playerNetServerHandler.sendPacket(s45packettitle1);
                    notifyOperators(sender, this, "commands.title.success");
                }
            } else if (args.length != 2) {
                throw new WrongUsageException("commands.title.usage");
            } else {
                final S45PacketTitle s45packettitle = new S45PacketTitle(type, null);
                entityplayermp.playerNetServerHandler.sendPacket(s45packettitle);
                notifyOperators(sender, this, "commands.title.success");
            }
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : args.length == 2 ? getListOfStringsMatchingLastWord(args, S45PacketTitle.Type.getNames()) : null;
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }

}
