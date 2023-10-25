/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandStats
extends CommandBase {
    @Override
    public String getCommandName() {
        return "stats";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.stats.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        CommandResultStats commandresultstats;
        CommandResultStats.Type commandresultstats$type;
        int i;
        boolean flag;
        if (args.length < 1) {
            throw new WrongUsageException("commands.stats.usage", new Object[0]);
        }
        if ("entity".equals(args[0])) {
            flag = false;
        } else {
            if (!"block".equals(args[0])) {
                throw new WrongUsageException("commands.stats.usage", new Object[0]);
            }
            flag = true;
        }
        if (flag) {
            if (args.length < 5) {
                throw new WrongUsageException("commands.stats.block.usage", new Object[0]);
            }
            i = 4;
        } else {
            if (args.length < 3) {
                throw new WrongUsageException("commands.stats.entity.usage", new Object[0]);
            }
            i = 2;
        }
        String s = args[i++];
        if ("set".equals(s)) {
            if (args.length < i + 3) {
                if (i == 5) {
                    throw new WrongUsageException("commands.stats.block.set.usage", new Object[0]);
                }
                throw new WrongUsageException("commands.stats.entity.set.usage", new Object[0]);
            }
        } else {
            if (!"clear".equals(s)) {
                throw new WrongUsageException("commands.stats.usage", new Object[0]);
            }
            if (args.length < i + 1) {
                if (i == 5) {
                    throw new WrongUsageException("commands.stats.block.clear.usage", new Object[0]);
                }
                throw new WrongUsageException("commands.stats.entity.clear.usage", new Object[0]);
            }
        }
        if ((commandresultstats$type = CommandResultStats.Type.getTypeByName(args[i++])) == null) {
            throw new CommandException("commands.stats.failed", new Object[0]);
        }
        World world = sender.getEntityWorld();
        if (flag) {
            BlockPos blockpos = CommandStats.parseBlockPos(sender, args, 1, false);
            TileEntity tileentity = world.getTileEntity(blockpos);
            if (tileentity == null) {
                throw new CommandException("commands.stats.noCompatibleBlock", blockpos.getX(), blockpos.getY(), blockpos.getZ());
            }
            if (tileentity instanceof TileEntityCommandBlock) {
                commandresultstats = ((TileEntityCommandBlock)tileentity).getCommandResultStats();
            } else {
                if (!(tileentity instanceof TileEntitySign)) {
                    throw new CommandException("commands.stats.noCompatibleBlock", blockpos.getX(), blockpos.getY(), blockpos.getZ());
                }
                commandresultstats = ((TileEntitySign)tileentity).getStats();
            }
        } else {
            Entity entity = CommandStats.getEntity(server, sender, args[1]);
            commandresultstats = entity.getCommandStats();
        }
        if ("set".equals(s)) {
            String s1 = args[i++];
            String s2 = args[i];
            if (s1.isEmpty() || s2.isEmpty()) {
                throw new CommandException("commands.stats.failed", new Object[0]);
            }
            CommandResultStats.setScoreBoardStat(commandresultstats, commandresultstats$type, s1, s2);
            CommandStats.notifyCommandListener(sender, (ICommand)this, "commands.stats.success", commandresultstats$type.getTypeName(), s2, s1);
        } else if ("clear".equals(s)) {
            CommandResultStats.setScoreBoardStat(commandresultstats, commandresultstats$type, null, null);
            CommandStats.notifyCommandListener(sender, (ICommand)this, "commands.stats.cleared", commandresultstats$type.getTypeName());
        }
        if (flag) {
            BlockPos blockpos1 = CommandStats.parseBlockPos(sender, args, 1, false);
            TileEntity tileentity1 = world.getTileEntity(blockpos1);
            tileentity1.markDirty();
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            return CommandStats.getListOfStringsMatchingLastWord(args, "entity", "block");
        }
        if (args.length == 2 && "entity".equals(args[0])) {
            return CommandStats.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
        }
        if (args.length >= 2 && args.length <= 4 && "block".equals(args[0])) {
            return CommandStats.getTabCompletionCoordinate(args, 1, pos);
        }
        if (!(args.length == 3 && "entity".equals(args[0]) || args.length == 5 && "block".equals(args[0]))) {
            if (!(args.length == 4 && "entity".equals(args[0]) || args.length == 6 && "block".equals(args[0]))) {
                return !(args.length == 6 && "entity".equals(args[0]) || args.length == 8 && "block".equals(args[0])) ? Collections.emptyList() : CommandStats.getListOfStringsMatchingLastWord(args, this.getObjectiveNames(server));
            }
            return CommandStats.getListOfStringsMatchingLastWord(args, CommandResultStats.Type.getTypeNames());
        }
        return CommandStats.getListOfStringsMatchingLastWord(args, "set", "clear");
    }

    protected List<String> getObjectiveNames(MinecraftServer server) {
        Collection<ScoreObjective> collection = server.getWorld(0).getScoreboard().getScoreObjectives();
        ArrayList<String> list = Lists.newArrayList();
        for (ScoreObjective scoreobjective : collection) {
            if (scoreobjective.getCriteria().isReadOnly()) continue;
            list.add(scoreobjective.getName());
        }
        return list;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return args.length > 0 && "entity".equals(args[0]) && index == 1;
    }
}

