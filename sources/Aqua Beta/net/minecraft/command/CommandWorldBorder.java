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
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.border.WorldBorder;

public class CommandWorldBorder
extends CommandBase {
    public String getCommandName() {
        return "worldborder";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.worldborder.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
        }
        WorldBorder worldborder = this.getWorldBorder();
        if (args[0].equals((Object)"set")) {
            long i;
            if (args.length != 2 && args.length != 3) {
                throw new WrongUsageException("commands.worldborder.set.usage", new Object[0]);
            }
            double d0 = worldborder.getTargetSize();
            double d2 = CommandWorldBorder.parseDouble((String)args[1], (double)1.0, (double)6.0E7);
            long l = i = args.length > 2 ? CommandWorldBorder.parseLong((String)args[2], (long)0L, (long)9223372036854775L) * 1000L : 0L;
            if (i > 0L) {
                worldborder.setTransition(d0, d2, i);
                if (d0 > d2) {
                    CommandWorldBorder.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.worldborder.setSlowly.shrink.success", (Object[])new Object[]{String.format((String)"%.1f", (Object[])new Object[]{d2}), String.format((String)"%.1f", (Object[])new Object[]{d0}), Long.toString((long)(i / 1000L))});
                } else {
                    CommandWorldBorder.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.worldborder.setSlowly.grow.success", (Object[])new Object[]{String.format((String)"%.1f", (Object[])new Object[]{d2}), String.format((String)"%.1f", (Object[])new Object[]{d0}), Long.toString((long)(i / 1000L))});
                }
            } else {
                worldborder.setTransition(d2);
                CommandWorldBorder.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.worldborder.set.success", (Object[])new Object[]{String.format((String)"%.1f", (Object[])new Object[]{d2}), String.format((String)"%.1f", (Object[])new Object[]{d0})});
            }
        } else if (args[0].equals((Object)"add")) {
            if (args.length != 2 && args.length != 3) {
                throw new WrongUsageException("commands.worldborder.add.usage", new Object[0]);
            }
            double d4 = worldborder.getDiameter();
            double d8 = d4 + CommandWorldBorder.parseDouble((String)args[1], (double)(-d4), (double)(6.0E7 - d4));
            long i1 = worldborder.getTimeUntilTarget() + (args.length > 2 ? CommandWorldBorder.parseLong((String)args[2], (long)0L, (long)9223372036854775L) * 1000L : 0L);
            if (i1 > 0L) {
                worldborder.setTransition(d4, d8, i1);
                if (d4 > d8) {
                    CommandWorldBorder.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.worldborder.setSlowly.shrink.success", (Object[])new Object[]{String.format((String)"%.1f", (Object[])new Object[]{d8}), String.format((String)"%.1f", (Object[])new Object[]{d4}), Long.toString((long)(i1 / 1000L))});
                } else {
                    CommandWorldBorder.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.worldborder.setSlowly.grow.success", (Object[])new Object[]{String.format((String)"%.1f", (Object[])new Object[]{d8}), String.format((String)"%.1f", (Object[])new Object[]{d4}), Long.toString((long)(i1 / 1000L))});
                }
            } else {
                worldborder.setTransition(d8);
                CommandWorldBorder.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.worldborder.set.success", (Object[])new Object[]{String.format((String)"%.1f", (Object[])new Object[]{d8}), String.format((String)"%.1f", (Object[])new Object[]{d4})});
            }
        } else if (args[0].equals((Object)"center")) {
            if (args.length != 3) {
                throw new WrongUsageException("commands.worldborder.center.usage", new Object[0]);
            }
            BlockPos blockpos = sender.getPosition();
            double d1 = CommandWorldBorder.parseDouble((double)((double)blockpos.getX() + 0.5), (String)args[1], (boolean)true);
            double d3 = CommandWorldBorder.parseDouble((double)((double)blockpos.getZ() + 0.5), (String)args[2], (boolean)true);
            worldborder.setCenter(d1, d3);
            CommandWorldBorder.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.worldborder.center.success", (Object[])new Object[]{d1, d3});
        } else if (args[0].equals((Object)"damage")) {
            if (args.length < 2) {
                throw new WrongUsageException("commands.worldborder.damage.usage", new Object[0]);
            }
            if (args[1].equals((Object)"buffer")) {
                if (args.length != 3) {
                    throw new WrongUsageException("commands.worldborder.damage.buffer.usage", new Object[0]);
                }
                double d5 = CommandWorldBorder.parseDouble((String)args[2], (double)0.0);
                double d9 = worldborder.getDamageBuffer();
                worldborder.setDamageBuffer(d5);
                CommandWorldBorder.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.worldborder.damage.buffer.success", (Object[])new Object[]{String.format((String)"%.1f", (Object[])new Object[]{d5}), String.format((String)"%.1f", (Object[])new Object[]{d9})});
            } else if (args[1].equals((Object)"amount")) {
                if (args.length != 3) {
                    throw new WrongUsageException("commands.worldborder.damage.amount.usage", new Object[0]);
                }
                double d6 = CommandWorldBorder.parseDouble((String)args[2], (double)0.0);
                double d10 = worldborder.getDamageAmount();
                worldborder.setDamageAmount(d6);
                CommandWorldBorder.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.worldborder.damage.amount.success", (Object[])new Object[]{String.format((String)"%.2f", (Object[])new Object[]{d6}), String.format((String)"%.2f", (Object[])new Object[]{d10})});
            }
        } else if (args[0].equals((Object)"warning")) {
            if (args.length < 2) {
                throw new WrongUsageException("commands.worldborder.warning.usage", new Object[0]);
            }
            int j = CommandWorldBorder.parseInt((String)args[2], (int)0);
            if (args[1].equals((Object)"time")) {
                if (args.length != 3) {
                    throw new WrongUsageException("commands.worldborder.warning.time.usage", new Object[0]);
                }
                int k = worldborder.getWarningTime();
                worldborder.setWarningTime(j);
                CommandWorldBorder.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.worldborder.warning.time.success", (Object[])new Object[]{j, k});
            } else if (args[1].equals((Object)"distance")) {
                if (args.length != 3) {
                    throw new WrongUsageException("commands.worldborder.warning.distance.usage", new Object[0]);
                }
                int l = worldborder.getWarningDistance();
                worldborder.setWarningDistance(j);
                CommandWorldBorder.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.worldborder.warning.distance.success", (Object[])new Object[]{j, l});
            }
        } else {
            if (!args[0].equals((Object)"get")) {
                throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
            }
            double d7 = worldborder.getDiameter();
            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, MathHelper.floor_double((double)(d7 + 0.5)));
            sender.addChatMessage((IChatComponent)new ChatComponentTranslation("commands.worldborder.get.success", new Object[]{String.format((String)"%.0f", (Object[])new Object[]{d7})}));
        }
    }

    protected WorldBorder getWorldBorder() {
        return MinecraftServer.getServer().worldServers[0].getWorldBorder();
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandWorldBorder.getListOfStringsMatchingLastWord((String[])args, (String[])new String[]{"set", "center", "damage", "warning", "add", "get"}) : (args.length == 2 && args[0].equals((Object)"damage") ? CommandWorldBorder.getListOfStringsMatchingLastWord((String[])args, (String[])new String[]{"buffer", "amount"}) : (args.length >= 2 && args.length <= 3 && args[0].equals((Object)"center") ? CommandWorldBorder.func_181043_b((String[])args, (int)1, (BlockPos)pos) : (args.length == 2 && args[0].equals((Object)"warning") ? CommandWorldBorder.getListOfStringsMatchingLastWord((String[])args, (String[])new String[]{"time", "distance"}) : null)));
    }
}
