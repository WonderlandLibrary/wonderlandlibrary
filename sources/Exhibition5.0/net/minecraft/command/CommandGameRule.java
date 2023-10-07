// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import java.util.List;
import net.minecraft.util.BlockPos;
import java.util.Iterator;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;

public class CommandGameRule extends CommandBase
{
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
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.gamerule.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        final GameRules var3 = this.getGameRules();
        final String var4 = (args.length > 0) ? args[0] : "";
        final String var5 = (args.length > 1) ? CommandBase.func_180529_a(args, 1) : "";
        switch (args.length) {
            case 0: {
                sender.addChatMessage(new ChatComponentText(CommandBase.joinNiceString(var3.getRules())));
                break;
            }
            case 1: {
                if (!var3.hasRule(var4)) {
                    throw new CommandException("commands.gamerule.norule", new Object[] { var4 });
                }
                final String var6 = var3.getGameRuleStringValue(var4);
                sender.addChatMessage(new ChatComponentText(var4).appendText(" = ").appendText(var6));
                sender.func_174794_a(CommandResultStats.Type.QUERY_RESULT, var3.getInt(var4));
                break;
            }
            default: {
                if (var3.areSameType(var4, GameRules.ValueType.BOOLEAN_VALUE) && !"true".equals(var5) && !"false".equals(var5)) {
                    throw new CommandException("commands.generic.boolean.invalid", new Object[] { var5 });
                }
                var3.setOrCreateGameRule(var4, var5);
                func_175773_a(var3, var4);
                CommandBase.notifyOperators(sender, this, "commands.gamerule.success", new Object[0]);
                break;
            }
        }
    }
    
    public static void func_175773_a(final GameRules p_175773_0_, final String p_175773_1_) {
        if ("reducedDebugInfo".equals(p_175773_1_)) {
            final int var2 = p_175773_0_.getGameRuleBooleanValue(p_175773_1_) ? 22 : 23;
            for (final EntityPlayerMP var4 : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
                var4.playerNetServerHandler.sendPacket(new S19PacketEntityStatus(var4, (byte)var2));
            }
        }
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, this.getGameRules().getRules());
        }
        if (args.length == 2) {
            final GameRules var4 = this.getGameRules();
            if (var4.areSameType(args[0], GameRules.ValueType.BOOLEAN_VALUE)) {
                return CommandBase.getListOfStringsMatchingLastWord(args, "true", "false");
            }
        }
        return null;
    }
    
    private GameRules getGameRules() {
        return MinecraftServer.getServer().worldServerForDimension(0).getGameRules();
    }
}
