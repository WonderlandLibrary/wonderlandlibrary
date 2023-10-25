/*
 * Decompiled with CFR 0.152.
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
    public static void func_175773_a(GameRules gameRules, String string) {
        if ("reducedDebugInfo".equals(string)) {
            byte by = (byte)(gameRules.getBoolean(string) ? 22 : 23);
            for (EntityPlayerMP entityPlayerMP : MinecraftServer.getServer().getConfigurationManager().func_181057_v()) {
                entityPlayerMP.playerNetServerHandler.sendPacket(new S19PacketEntityStatus(entityPlayerMP, by));
            }
        }
    }

    @Override
    public String getCommandName() {
        return "gamerule";
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        GameRules gameRules = this.getGameRules();
        String string = stringArray.length > 0 ? stringArray[0] : "";
        String string2 = stringArray.length > 1 ? CommandGameRule.buildString(stringArray, 1) : "";
        switch (stringArray.length) {
            case 0: {
                iCommandSender.addChatMessage(new ChatComponentText(CommandGameRule.joinNiceString(gameRules.getRules())));
                break;
            }
            case 1: {
                if (!gameRules.hasRule(string)) {
                    throw new CommandException("commands.gamerule.norule", string);
                }
                String string3 = gameRules.getString(string);
                iCommandSender.addChatMessage(new ChatComponentText(string).appendText(" = ").appendText(string3));
                iCommandSender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, gameRules.getInt(string));
                break;
            }
            default: {
                if (gameRules.areSameType(string, GameRules.ValueType.BOOLEAN_VALUE) && !"true".equals(string2) && !"false".equals(string2)) {
                    throw new CommandException("commands.generic.boolean.invalid", string2);
                }
                gameRules.setOrCreateGameRule(string, string2);
                CommandGameRule.func_175773_a(gameRules, string);
                CommandGameRule.notifyOperators(iCommandSender, (ICommand)this, "commands.gamerule.success", new Object[0]);
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        GameRules gameRules;
        if (stringArray.length == 1) {
            return CommandGameRule.getListOfStringsMatchingLastWord(stringArray, this.getGameRules().getRules());
        }
        if (stringArray.length == 2 && (gameRules = this.getGameRules()).areSameType(stringArray[0], GameRules.ValueType.BOOLEAN_VALUE)) {
            return CommandGameRule.getListOfStringsMatchingLastWord(stringArray, "true", "false");
        }
        return null;
    }

    private GameRules getGameRules() {
        return MinecraftServer.getServer().worldServerForDimension(0).getGameRules();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.gamerule.usage";
    }
}

