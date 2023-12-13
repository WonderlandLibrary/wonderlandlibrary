// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;

public class CommandWorldBorder extends CommandBase
{
    private static final String __OBFID = "CL_00002336";
    
    @Override
    public String getCommandName() {
        return "worldborder";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.worldborder.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
        }
        final WorldBorder var3 = this.getWorldBorder();
        if (args[0].equals("set")) {
            if (args.length != 2 && args.length != 3) {
                throw new WrongUsageException("commands.worldborder.set.usage", new Object[0]);
            }
            final double var4 = var3.getTargetSize();
            final double var5 = CommandBase.parseDouble(args[1], 1.0, 6.0E7);
            final long var6 = (args.length > 2) ? (CommandBase.parseLong(args[2], 0L, 9223372036854775L) * 1000L) : 0L;
            if (var6 > 0L) {
                var3.setTransition(var4, var5, var6);
                if (var4 > var5) {
                    CommandBase.notifyOperators(sender, this, "commands.worldborder.setSlowly.shrink.success", String.format("%.1f", var5), String.format("%.1f", var4), Long.toString(var6 / 1000L));
                }
                else {
                    CommandBase.notifyOperators(sender, this, "commands.worldborder.setSlowly.grow.success", String.format("%.1f", var5), String.format("%.1f", var4), Long.toString(var6 / 1000L));
                }
            }
            else {
                var3.setTransition(var5);
                CommandBase.notifyOperators(sender, this, "commands.worldborder.set.success", String.format("%.1f", var5), String.format("%.1f", var4));
            }
        }
        else if (args[0].equals("add")) {
            if (args.length != 2 && args.length != 3) {
                throw new WrongUsageException("commands.worldborder.add.usage", new Object[0]);
            }
            final double var4 = var3.getDiameter();
            final double var5 = var4 + CommandBase.parseDouble(args[1], -var4, 6.0E7 - var4);
            final long var6 = var3.getTimeUntilTarget() + ((args.length > 2) ? (CommandBase.parseLong(args[2], 0L, 9223372036854775L) * 1000L) : 0L);
            if (var6 > 0L) {
                var3.setTransition(var4, var5, var6);
                if (var4 > var5) {
                    CommandBase.notifyOperators(sender, this, "commands.worldborder.setSlowly.shrink.success", String.format("%.1f", var5), String.format("%.1f", var4), Long.toString(var6 / 1000L));
                }
                else {
                    CommandBase.notifyOperators(sender, this, "commands.worldborder.setSlowly.grow.success", String.format("%.1f", var5), String.format("%.1f", var4), Long.toString(var6 / 1000L));
                }
            }
            else {
                var3.setTransition(var5);
                CommandBase.notifyOperators(sender, this, "commands.worldborder.set.success", String.format("%.1f", var5), String.format("%.1f", var4));
            }
        }
        else if (args[0].equals("center")) {
            if (args.length != 3) {
                throw new WrongUsageException("commands.worldborder.center.usage", new Object[0]);
            }
            final BlockPos var7 = sender.getPosition();
            final double var8 = CommandBase.func_175761_b(var7.getX() + 0.5, args[1], true);
            final double var9 = CommandBase.func_175761_b(var7.getZ() + 0.5, args[2], true);
            var3.setCenter(var8, var9);
            CommandBase.notifyOperators(sender, this, "commands.worldborder.center.success", var8, var9);
        }
        else if (args[0].equals("damage")) {
            if (args.length < 2) {
                throw new WrongUsageException("commands.worldborder.damage.usage", new Object[0]);
            }
            if (args[1].equals("buffer")) {
                if (args.length != 3) {
                    throw new WrongUsageException("commands.worldborder.damage.buffer.usage", new Object[0]);
                }
                final double var4 = CommandBase.parseDouble(args[2], 0.0);
                final double var5 = var3.getDamageBuffer();
                var3.setDamageBuffer(var4);
                CommandBase.notifyOperators(sender, this, "commands.worldborder.damage.buffer.success", String.format("%.1f", var4), String.format("%.1f", var5));
            }
            else if (args[1].equals("amount")) {
                if (args.length != 3) {
                    throw new WrongUsageException("commands.worldborder.damage.amount.usage", new Object[0]);
                }
                final double var4 = CommandBase.parseDouble(args[2], 0.0);
                final double var5 = var3.func_177727_n();
                var3.func_177744_c(var4);
                CommandBase.notifyOperators(sender, this, "commands.worldborder.damage.amount.success", String.format("%.2f", var4), String.format("%.2f", var5));
            }
        }
        else if (args[0].equals("warning")) {
            if (args.length < 2) {
                throw new WrongUsageException("commands.worldborder.warning.usage", new Object[0]);
            }
            final int var10 = CommandBase.parseInt(args[2], 0);
            if (args[1].equals("time")) {
                if (args.length != 3) {
                    throw new WrongUsageException("commands.worldborder.warning.time.usage", new Object[0]);
                }
                final int var11 = var3.getWarningTime();
                var3.setWarningTime(var10);
                CommandBase.notifyOperators(sender, this, "commands.worldborder.warning.time.success", var10, var11);
            }
            else if (args[1].equals("distance")) {
                if (args.length != 3) {
                    throw new WrongUsageException("commands.worldborder.warning.distance.usage", new Object[0]);
                }
                final int var11 = var3.getWarningDistance();
                var3.setWarningDistance(var10);
                CommandBase.notifyOperators(sender, this, "commands.worldborder.warning.distance.success", var10, var11);
            }
        }
        else if (args[0].equals("get")) {
            final double var4 = var3.getDiameter();
            sender.func_174794_a(CommandResultStats.Type.QUERY_RESULT, MathHelper.floor_double(var4 + 0.5));
            sender.addChatMessage(new ChatComponentTranslation("commands.worldborder.get.success", new Object[] { String.format("%.0f", var4) }));
        }
    }
    
    protected WorldBorder getWorldBorder() {
        return MinecraftServer.getServer().worldServers[0].getWorldBorder();
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, "set", "center", "damage", "warning", "add", "get") : ((args.length == 2 && args[0].equals("damage")) ? CommandBase.getListOfStringsMatchingLastWord(args, "buffer", "amount") : ((args.length == 2 && args[0].equals("warning")) ? CommandBase.getListOfStringsMatchingLastWord(args, "time", "distance") : null));
    }
}
