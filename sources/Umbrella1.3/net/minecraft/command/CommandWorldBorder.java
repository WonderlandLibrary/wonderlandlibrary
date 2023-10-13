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
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.world.border.WorldBorder;

public class CommandWorldBorder
extends CommandBase {
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
    public String getCommandUsage(ICommandSender sender) {
        return "commands.worldborder.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
        }
        WorldBorder var3 = this.getWorldBorder();
        if (args[0].equals("set")) {
            long var8;
            if (args.length != 2 && args.length != 3) {
                throw new WrongUsageException("commands.worldborder.set.usage", new Object[0]);
            }
            double var4 = var3.getTargetSize();
            double var6 = CommandWorldBorder.parseDouble(args[1], 1.0, 6.0E7);
            long l = var8 = args.length > 2 ? CommandWorldBorder.parseLong(args[2], 0L, 9223372036854775L) * 1000L : 0L;
            if (var8 > 0L) {
                var3.setTransition(var4, var6, var8);
                if (var4 > var6) {
                    CommandWorldBorder.notifyOperators(sender, (ICommand)this, "commands.worldborder.setSlowly.shrink.success", String.format("%.1f", var6), String.format("%.1f", var4), Long.toString(var8 / 1000L));
                } else {
                    CommandWorldBorder.notifyOperators(sender, (ICommand)this, "commands.worldborder.setSlowly.grow.success", String.format("%.1f", var6), String.format("%.1f", var4), Long.toString(var8 / 1000L));
                }
            } else {
                var3.setTransition(var6);
                CommandWorldBorder.notifyOperators(sender, (ICommand)this, "commands.worldborder.set.success", String.format("%.1f", var6), String.format("%.1f", var4));
            }
        } else if (args[0].equals("add")) {
            if (args.length != 2 && args.length != 3) {
                throw new WrongUsageException("commands.worldborder.add.usage", new Object[0]);
            }
            double var4 = var3.getDiameter();
            double var6 = var4 + CommandWorldBorder.parseDouble(args[1], -var4, 6.0E7 - var4);
            long var8 = var3.getTimeUntilTarget() + (args.length > 2 ? CommandWorldBorder.parseLong(args[2], 0L, 9223372036854775L) * 1000L : 0L);
            if (var8 > 0L) {
                var3.setTransition(var4, var6, var8);
                if (var4 > var6) {
                    CommandWorldBorder.notifyOperators(sender, (ICommand)this, "commands.worldborder.setSlowly.shrink.success", String.format("%.1f", var6), String.format("%.1f", var4), Long.toString(var8 / 1000L));
                } else {
                    CommandWorldBorder.notifyOperators(sender, (ICommand)this, "commands.worldborder.setSlowly.grow.success", String.format("%.1f", var6), String.format("%.1f", var4), Long.toString(var8 / 1000L));
                }
            } else {
                var3.setTransition(var6);
                CommandWorldBorder.notifyOperators(sender, (ICommand)this, "commands.worldborder.set.success", String.format("%.1f", var6), String.format("%.1f", var4));
            }
        } else if (args[0].equals("center")) {
            if (args.length != 3) {
                throw new WrongUsageException("commands.worldborder.center.usage", new Object[0]);
            }
            BlockPos var10 = sender.getPosition();
            double var5 = CommandWorldBorder.func_175761_b((double)var10.getX() + 0.5, args[1], true);
            double var7 = CommandWorldBorder.func_175761_b((double)var10.getZ() + 0.5, args[2], true);
            var3.setCenter(var5, var7);
            CommandWorldBorder.notifyOperators(sender, (ICommand)this, "commands.worldborder.center.success", var5, var7);
        } else if (args[0].equals("damage")) {
            if (args.length < 2) {
                throw new WrongUsageException("commands.worldborder.damage.usage", new Object[0]);
            }
            if (args[1].equals("buffer")) {
                if (args.length != 3) {
                    throw new WrongUsageException("commands.worldborder.damage.buffer.usage", new Object[0]);
                }
                double var4 = CommandWorldBorder.parseDouble(args[2], 0.0);
                double var6 = var3.getDamageBuffer();
                var3.setDamageBuffer(var4);
                CommandWorldBorder.notifyOperators(sender, (ICommand)this, "commands.worldborder.damage.buffer.success", String.format("%.1f", var4), String.format("%.1f", var6));
            } else if (args[1].equals("amount")) {
                if (args.length != 3) {
                    throw new WrongUsageException("commands.worldborder.damage.amount.usage", new Object[0]);
                }
                double var4 = CommandWorldBorder.parseDouble(args[2], 0.0);
                double var6 = var3.func_177727_n();
                var3.func_177744_c(var4);
                CommandWorldBorder.notifyOperators(sender, (ICommand)this, "commands.worldborder.damage.amount.success", String.format("%.2f", var4), String.format("%.2f", var6));
            }
        } else if (args[0].equals("warning")) {
            if (args.length < 2) {
                throw new WrongUsageException("commands.worldborder.warning.usage", new Object[0]);
            }
            int var11 = CommandWorldBorder.parseInt(args[2], 0);
            if (args[1].equals("time")) {
                if (args.length != 3) {
                    throw new WrongUsageException("commands.worldborder.warning.time.usage", new Object[0]);
                }
                int var12 = var3.getWarningTime();
                var3.setWarningTime(var11);
                CommandWorldBorder.notifyOperators(sender, (ICommand)this, "commands.worldborder.warning.time.success", var11, var12);
            } else if (args[1].equals("distance")) {
                if (args.length != 3) {
                    throw new WrongUsageException("commands.worldborder.warning.distance.usage", new Object[0]);
                }
                int var12 = var3.getWarningDistance();
                var3.setWarningDistance(var11);
                CommandWorldBorder.notifyOperators(sender, (ICommand)this, "commands.worldborder.warning.distance.success", var11, var12);
            }
        } else if (args[0].equals("get")) {
            double var4 = var3.getDiameter();
            sender.func_174794_a(CommandResultStats.Type.QUERY_RESULT, MathHelper.floor_double(var4 + 0.5));
            sender.addChatMessage(new ChatComponentTranslation("commands.worldborder.get.success", String.format("%.0f", var4)));
        }
    }

    protected WorldBorder getWorldBorder() {
        return MinecraftServer.getServer().worldServers[0].getWorldBorder();
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandWorldBorder.getListOfStringsMatchingLastWord(args, "set", "center", "damage", "warning", "add", "get") : (args.length == 2 && args[0].equals("damage") ? CommandWorldBorder.getListOfStringsMatchingLastWord(args, "buffer", "amount") : (args.length == 2 && args[0].equals("warning") ? CommandWorldBorder.getListOfStringsMatchingLastWord(args, "time", "distance") : null));
    }
}

