/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.GameRules;

public class CommandGameRule
extends CommandBase {
    private static final String __OBFID = "CL_00000475";

    @Override
    public String getCommandName() {
        return "gamerule";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.gamerule.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        GameRules var3 = this.getGameRules();
        String var4 = args.length > 0 ? args[0] : "";
        String var5 = args.length > 1 ? CommandGameRule.func_180529_a(args, 1) : "";
        switch (args.length) {
            case 0: {
                sender.addChatMessage(new ChatComponentText(CommandGameRule.joinNiceString(var3.getRules())));
                break;
            }
            case 1: {
                if (!var3.hasRule(var4)) {
                    throw new CommandException("commands.gamerule.norule", var4);
                }
                String var6 = var3.getGameRuleStringValue(var4);
                sender.addChatMessage(new ChatComponentText(var4).appendText(" = ").appendText(var6));
                sender.func_174794_a(CommandResultStats.Type.QUERY_RESULT, var3.getInt(var4));
                break;
            }
            default: {
                if (var3.areSameType(var4, GameRules.ValueType.BOOLEAN_VALUE) && !"true".equals(var5) && !"false".equals(var5)) {
                    throw new CommandException("commands.generic.boolean.invalid", var5);
                }
                var3.setOrCreateGameRule(var4, var5);
                CommandGameRule.func_175773_a(var3, var4);
                CommandGameRule.notifyOperators(sender, (ICommand)this, "commands.gamerule.success", new Object[0]);
            }
        }
    }

    public static void func_175773_a(GameRules p_175773_0_, String p_175773_1_) {
        if ("reducedDebugInfo".equals(p_175773_1_)) {
            int var2 = p_175773_0_.getGameRuleBooleanValue(p_175773_1_) ? 22 : 23;
            for (EntityPlayerMP var4 : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
                var4.playerNetServerHandler.sendPacket(new S19PacketEntityStatus(var4, (byte)var2));
            }
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        GameRules var4;
        if (args.length == 1) {
            return CommandGameRule.getListOfStringsMatchingLastWord(args, this.getGameRules().getRules());
        }
        if (args.length == 2 && (var4 = this.getGameRules()).areSameType(args[0], GameRules.ValueType.BOOLEAN_VALUE)) {
            return CommandGameRule.getListOfStringsMatchingLastWord(args, "true", "false");
        }
        return null;
    }

    private GameRules getGameRules() {
        return MinecraftServer.getServer().worldServerForDimension(0).getGameRules();
    }
}

