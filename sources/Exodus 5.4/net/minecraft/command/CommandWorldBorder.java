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
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.world.border.WorldBorder;

public class CommandWorldBorder
extends CommandBase {
    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.worldborder.usage";
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        if (stringArray.length < 1) {
            throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
        }
        WorldBorder worldBorder = this.getWorldBorder();
        if (stringArray[0].equals("set")) {
            long l;
            if (stringArray.length != 2 && stringArray.length != 3) {
                throw new WrongUsageException("commands.worldborder.set.usage", new Object[0]);
            }
            double d = worldBorder.getTargetSize();
            double d2 = CommandWorldBorder.parseDouble(stringArray[1], 1.0, 6.0E7);
            long l2 = l = stringArray.length > 2 ? CommandWorldBorder.parseLong(stringArray[2], 0L, 9223372036854775L) * 1000L : 0L;
            if (l > 0L) {
                worldBorder.setTransition(d, d2, l);
                if (d > d2) {
                    CommandWorldBorder.notifyOperators(iCommandSender, (ICommand)this, "commands.worldborder.setSlowly.shrink.success", String.format("%.1f", d2), String.format("%.1f", d), Long.toString(l / 1000L));
                } else {
                    CommandWorldBorder.notifyOperators(iCommandSender, (ICommand)this, "commands.worldborder.setSlowly.grow.success", String.format("%.1f", d2), String.format("%.1f", d), Long.toString(l / 1000L));
                }
            } else {
                worldBorder.setTransition(d2);
                CommandWorldBorder.notifyOperators(iCommandSender, (ICommand)this, "commands.worldborder.set.success", String.format("%.1f", d2), String.format("%.1f", d));
            }
        } else if (stringArray[0].equals("add")) {
            if (stringArray.length != 2 && stringArray.length != 3) {
                throw new WrongUsageException("commands.worldborder.add.usage", new Object[0]);
            }
            double d = worldBorder.getDiameter();
            double d3 = d + CommandWorldBorder.parseDouble(stringArray[1], -d, 6.0E7 - d);
            long l = worldBorder.getTimeUntilTarget() + (stringArray.length > 2 ? CommandWorldBorder.parseLong(stringArray[2], 0L, 9223372036854775L) * 1000L : 0L);
            if (l > 0L) {
                worldBorder.setTransition(d, d3, l);
                if (d > d3) {
                    CommandWorldBorder.notifyOperators(iCommandSender, (ICommand)this, "commands.worldborder.setSlowly.shrink.success", String.format("%.1f", d3), String.format("%.1f", d), Long.toString(l / 1000L));
                } else {
                    CommandWorldBorder.notifyOperators(iCommandSender, (ICommand)this, "commands.worldborder.setSlowly.grow.success", String.format("%.1f", d3), String.format("%.1f", d), Long.toString(l / 1000L));
                }
            } else {
                worldBorder.setTransition(d3);
                CommandWorldBorder.notifyOperators(iCommandSender, (ICommand)this, "commands.worldborder.set.success", String.format("%.1f", d3), String.format("%.1f", d));
            }
        } else if (stringArray[0].equals("center")) {
            if (stringArray.length != 3) {
                throw new WrongUsageException("commands.worldborder.center.usage", new Object[0]);
            }
            BlockPos blockPos = iCommandSender.getPosition();
            double d = CommandWorldBorder.parseDouble((double)blockPos.getX() + 0.5, stringArray[1], true);
            double d4 = CommandWorldBorder.parseDouble((double)blockPos.getZ() + 0.5, stringArray[2], true);
            worldBorder.setCenter(d, d4);
            CommandWorldBorder.notifyOperators(iCommandSender, (ICommand)this, "commands.worldborder.center.success", d, d4);
        } else if (stringArray[0].equals("damage")) {
            if (stringArray.length < 2) {
                throw new WrongUsageException("commands.worldborder.damage.usage", new Object[0]);
            }
            if (stringArray[1].equals("buffer")) {
                if (stringArray.length != 3) {
                    throw new WrongUsageException("commands.worldborder.damage.buffer.usage", new Object[0]);
                }
                double d = CommandWorldBorder.parseDouble(stringArray[2], 0.0);
                double d5 = worldBorder.getDamageBuffer();
                worldBorder.setDamageBuffer(d);
                CommandWorldBorder.notifyOperators(iCommandSender, (ICommand)this, "commands.worldborder.damage.buffer.success", String.format("%.1f", d), String.format("%.1f", d5));
            } else if (stringArray[1].equals("amount")) {
                if (stringArray.length != 3) {
                    throw new WrongUsageException("commands.worldborder.damage.amount.usage", new Object[0]);
                }
                double d = CommandWorldBorder.parseDouble(stringArray[2], 0.0);
                double d6 = worldBorder.getDamageAmount();
                worldBorder.setDamageAmount(d);
                CommandWorldBorder.notifyOperators(iCommandSender, (ICommand)this, "commands.worldborder.damage.amount.success", String.format("%.2f", d), String.format("%.2f", d6));
            }
        } else if (stringArray[0].equals("warning")) {
            if (stringArray.length < 2) {
                throw new WrongUsageException("commands.worldborder.warning.usage", new Object[0]);
            }
            int n = CommandWorldBorder.parseInt(stringArray[2], 0);
            if (stringArray[1].equals("time")) {
                if (stringArray.length != 3) {
                    throw new WrongUsageException("commands.worldborder.warning.time.usage", new Object[0]);
                }
                int n2 = worldBorder.getWarningTime();
                worldBorder.setWarningTime(n);
                CommandWorldBorder.notifyOperators(iCommandSender, (ICommand)this, "commands.worldborder.warning.time.success", n, n2);
            } else if (stringArray[1].equals("distance")) {
                if (stringArray.length != 3) {
                    throw new WrongUsageException("commands.worldborder.warning.distance.usage", new Object[0]);
                }
                int n3 = worldBorder.getWarningDistance();
                worldBorder.setWarningDistance(n);
                CommandWorldBorder.notifyOperators(iCommandSender, (ICommand)this, "commands.worldborder.warning.distance.success", n, n3);
            }
        } else {
            if (!stringArray[0].equals("get")) {
                throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
            }
            double d = worldBorder.getDiameter();
            iCommandSender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, MathHelper.floor_double(d + 0.5));
            iCommandSender.addChatMessage(new ChatComponentTranslation("commands.worldborder.get.success", String.format("%.0f", d)));
        }
    }

    @Override
    public String getCommandName() {
        return "worldborder";
    }

    protected WorldBorder getWorldBorder() {
        return MinecraftServer.getServer().worldServers[0].getWorldBorder();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 1 ? CommandWorldBorder.getListOfStringsMatchingLastWord(stringArray, "set", "center", "damage", "warning", "add", "get") : (stringArray.length == 2 && stringArray[0].equals("damage") ? CommandWorldBorder.getListOfStringsMatchingLastWord(stringArray, "buffer", "amount") : (stringArray.length >= 2 && stringArray.length <= 3 && stringArray[0].equals("center") ? CommandWorldBorder.func_181043_b(stringArray, 1, blockPos) : (stringArray.length == 2 && stringArray[0].equals("warning") ? CommandWorldBorder.getListOfStringsMatchingLastWord(stringArray, "time", "distance") : null)));
    }
}

