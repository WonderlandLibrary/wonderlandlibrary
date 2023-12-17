// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.server.MinecraftServer;

public class CommandWorldBorder extends CommandBase
{
    @Override
    public String getName() {
        return "worldborder";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.worldborder.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
        }
        final WorldBorder worldborder = this.getWorldBorder(server);
        if ("set".equals(args[0])) {
            if (args.length != 2 && args.length != 3) {
                throw new WrongUsageException("commands.worldborder.set.usage", new Object[0]);
            }
            final double d0 = worldborder.getTargetSize();
            final double d2 = CommandBase.parseDouble(args[1], 1.0, 6.0E7);
            final long i = (args.length > 2) ? (CommandBase.parseLong(args[2], 0L, 9223372036854775L) * 1000L) : 0L;
            if (i > 0L) {
                worldborder.setTransition(d0, d2, i);
                if (d0 > d2) {
                    CommandBase.notifyCommandListener(sender, this, "commands.worldborder.setSlowly.shrink.success", String.format("%.1f", d2), String.format("%.1f", d0), Long.toString(i / 1000L));
                }
                else {
                    CommandBase.notifyCommandListener(sender, this, "commands.worldborder.setSlowly.grow.success", String.format("%.1f", d2), String.format("%.1f", d0), Long.toString(i / 1000L));
                }
            }
            else {
                worldborder.setTransition(d2);
                CommandBase.notifyCommandListener(sender, this, "commands.worldborder.set.success", String.format("%.1f", d2), String.format("%.1f", d0));
            }
        }
        else if ("add".equals(args[0])) {
            if (args.length != 2 && args.length != 3) {
                throw new WrongUsageException("commands.worldborder.add.usage", new Object[0]);
            }
            final double d3 = worldborder.getDiameter();
            final double d4 = d3 + CommandBase.parseDouble(args[1], -d3, 6.0E7 - d3);
            final long j1 = worldborder.getTimeUntilTarget() + ((args.length > 2) ? (CommandBase.parseLong(args[2], 0L, 9223372036854775L) * 1000L) : 0L);
            if (j1 > 0L) {
                worldborder.setTransition(d3, d4, j1);
                if (d3 > d4) {
                    CommandBase.notifyCommandListener(sender, this, "commands.worldborder.setSlowly.shrink.success", String.format("%.1f", d4), String.format("%.1f", d3), Long.toString(j1 / 1000L));
                }
                else {
                    CommandBase.notifyCommandListener(sender, this, "commands.worldborder.setSlowly.grow.success", String.format("%.1f", d4), String.format("%.1f", d3), Long.toString(j1 / 1000L));
                }
            }
            else {
                worldborder.setTransition(d4);
                CommandBase.notifyCommandListener(sender, this, "commands.worldborder.set.success", String.format("%.1f", d4), String.format("%.1f", d3));
            }
        }
        else if ("center".equals(args[0])) {
            if (args.length != 3) {
                throw new WrongUsageException("commands.worldborder.center.usage", new Object[0]);
            }
            final BlockPos blockpos = sender.getPosition();
            final double d5 = CommandBase.parseDouble(blockpos.getX() + 0.5, args[1], true);
            final double d6 = CommandBase.parseDouble(blockpos.getZ() + 0.5, args[2], true);
            worldborder.setCenter(d5, d6);
            CommandBase.notifyCommandListener(sender, this, "commands.worldborder.center.success", d5, d6);
        }
        else if ("damage".equals(args[0])) {
            if (args.length < 2) {
                throw new WrongUsageException("commands.worldborder.damage.usage", new Object[0]);
            }
            if ("buffer".equals(args[1])) {
                if (args.length != 3) {
                    throw new WrongUsageException("commands.worldborder.damage.buffer.usage", new Object[0]);
                }
                final double d7 = CommandBase.parseDouble(args[2], 0.0);
                final double d8 = worldborder.getDamageBuffer();
                worldborder.setDamageBuffer(d7);
                CommandBase.notifyCommandListener(sender, this, "commands.worldborder.damage.buffer.success", String.format("%.1f", d7), String.format("%.1f", d8));
            }
            else if ("amount".equals(args[1])) {
                if (args.length != 3) {
                    throw new WrongUsageException("commands.worldborder.damage.amount.usage", new Object[0]);
                }
                final double d9 = CommandBase.parseDouble(args[2], 0.0);
                final double d10 = worldborder.getDamageAmount();
                worldborder.setDamageAmount(d9);
                CommandBase.notifyCommandListener(sender, this, "commands.worldborder.damage.amount.success", String.format("%.2f", d9), String.format("%.2f", d10));
            }
        }
        else if ("warning".equals(args[0])) {
            if (args.length < 2) {
                throw new WrongUsageException("commands.worldborder.warning.usage", new Object[0]);
            }
            if ("time".equals(args[1])) {
                if (args.length != 3) {
                    throw new WrongUsageException("commands.worldborder.warning.time.usage", new Object[0]);
                }
                final int k = CommandBase.parseInt(args[2], 0);
                final int l = worldborder.getWarningTime();
                worldborder.setWarningTime(k);
                CommandBase.notifyCommandListener(sender, this, "commands.worldborder.warning.time.success", k, l);
            }
            else if ("distance".equals(args[1])) {
                if (args.length != 3) {
                    throw new WrongUsageException("commands.worldborder.warning.distance.usage", new Object[0]);
                }
                final int m = CommandBase.parseInt(args[2], 0);
                final int i2 = worldborder.getWarningDistance();
                worldborder.setWarningDistance(m);
                CommandBase.notifyCommandListener(sender, this, "commands.worldborder.warning.distance.success", m, i2);
            }
        }
        else {
            if (!"get".equals(args[0])) {
                throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
            }
            final double d11 = worldborder.getDiameter();
            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, MathHelper.floor(d11 + 0.5));
            sender.sendMessage(new TextComponentTranslation("commands.worldborder.get.success", new Object[] { String.format("%.0f", d11) }));
        }
    }
    
    protected WorldBorder getWorldBorder(final MinecraftServer server) {
        return server.worlds[0].getWorldBorder();
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "set", "center", "damage", "warning", "add", "get");
        }
        if (args.length == 2 && "damage".equals(args[0])) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "buffer", "amount");
        }
        if (args.length >= 2 && args.length <= 3 && "center".equals(args[0])) {
            return CommandBase.getTabCompletionCoordinateXZ(args, 1, targetPos);
        }
        return (args.length == 2 && "warning".equals(args[0])) ? CommandBase.getListOfStringsMatchingLastWord(args, "time", "distance") : Collections.emptyList();
    }
}
