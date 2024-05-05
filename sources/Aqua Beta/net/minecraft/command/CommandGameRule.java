package net.minecraft.command;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.GameRules;

public class CommandGameRule
extends CommandBase {
    public String getCommandName() {
        return "gamerule";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.gamerule.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        GameRules gamerules = this.getGameRules();
        String s = args.length > 0 ? args[0] : "";
        String s1 = args.length > 1 ? CommandGameRule.buildString((String[])args, (int)1) : "";
        switch (args.length) {
            case 0: {
                sender.addChatMessage((IChatComponent)new ChatComponentText(CommandGameRule.joinNiceString((Object[])gamerules.getRules())));
                break;
            }
            case 1: {
                if (!gamerules.hasRule(s)) {
                    throw new CommandException("commands.gamerule.norule", new Object[]{s});
                }
                String s2 = gamerules.getString(s);
                sender.addChatMessage(new ChatComponentText(s).appendText(" = ").appendText(s2));
                sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, gamerules.getInt(s));
                break;
            }
            default: {
                if (gamerules.areSameType(s, GameRules.ValueType.BOOLEAN_VALUE) && !"true".equals((Object)s1) && !"false".equals((Object)s1)) {
                    throw new CommandException("commands.generic.boolean.invalid", new Object[]{s1});
                }
                gamerules.setOrCreateGameRule(s, s1);
                CommandGameRule.func_175773_a(gamerules, s);
                CommandGameRule.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.gamerule.success", (Object[])new Object[0]);
            }
        }
    }

    public static void func_175773_a(GameRules rules, String p_175773_1_) {
        if ("reducedDebugInfo".equals((Object)p_175773_1_)) {
            byte b0 = (byte)(rules.getBoolean(p_175773_1_) ? 22 : 23);
            for (EntityPlayerMP entityplayermp : MinecraftServer.getServer().getConfigurationManager().getPlayerList()) {
                entityplayermp.playerNetServerHandler.sendPacket((Packet)new S19PacketEntityStatus((Entity)entityplayermp, b0));
            }
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        GameRules gamerules;
        if (args.length == 1) {
            return CommandGameRule.getListOfStringsMatchingLastWord((String[])args, (String[])this.getGameRules().getRules());
        }
        if (args.length == 2 && (gamerules = this.getGameRules()).areSameType(args[0], GameRules.ValueType.BOOLEAN_VALUE)) {
            return CommandGameRule.getListOfStringsMatchingLastWord((String[])args, (String[])new String[]{"true", "false"});
        }
        return null;
    }

    private GameRules getGameRules() {
        return MinecraftServer.getServer().worldServerForDimension(0).getGameRules();
    }
}
