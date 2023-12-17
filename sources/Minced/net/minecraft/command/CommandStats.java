// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Iterator;
import net.minecraft.scoreboard.ScoreObjective;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.server.MinecraftServer;

public class CommandStats extends CommandBase
{
    @Override
    public String getName() {
        return "stats";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.stats.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.stats.usage", new Object[0]);
        }
        boolean flag;
        if ("entity".equals(args[0])) {
            flag = false;
        }
        else {
            if (!"block".equals(args[0])) {
                throw new WrongUsageException("commands.stats.usage", new Object[0]);
            }
            flag = true;
        }
        int i;
        if (flag) {
            if (args.length < 5) {
                throw new WrongUsageException("commands.stats.block.usage", new Object[0]);
            }
            i = 4;
        }
        else {
            if (args.length < 3) {
                throw new WrongUsageException("commands.stats.entity.usage", new Object[0]);
            }
            i = 2;
        }
        final String s = args[i++];
        if ("set".equals(s)) {
            if (args.length < i + 3) {
                if (i == 5) {
                    throw new WrongUsageException("commands.stats.block.set.usage", new Object[0]);
                }
                throw new WrongUsageException("commands.stats.entity.set.usage", new Object[0]);
            }
        }
        else {
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
        final CommandResultStats.Type commandresultstats$type = CommandResultStats.Type.getTypeByName(args[i++]);
        if (commandresultstats$type == null) {
            throw new CommandException("commands.stats.failed", new Object[0]);
        }
        final World world = sender.getEntityWorld();
        CommandResultStats commandresultstats;
        if (flag) {
            final BlockPos blockpos = CommandBase.parseBlockPos(sender, args, 1, false);
            final TileEntity tileentity = world.getTileEntity(blockpos);
            if (tileentity == null) {
                throw new CommandException("commands.stats.noCompatibleBlock", new Object[] { blockpos.getX(), blockpos.getY(), blockpos.getZ() });
            }
            if (tileentity instanceof TileEntityCommandBlock) {
                commandresultstats = ((TileEntityCommandBlock)tileentity).getCommandResultStats();
            }
            else {
                if (!(tileentity instanceof TileEntitySign)) {
                    throw new CommandException("commands.stats.noCompatibleBlock", new Object[] { blockpos.getX(), blockpos.getY(), blockpos.getZ() });
                }
                commandresultstats = ((TileEntitySign)tileentity).getStats();
            }
        }
        else {
            final Entity entity = CommandBase.getEntity(server, sender, args[1]);
            commandresultstats = entity.getCommandStats();
        }
        if ("set".equals(s)) {
            final String s2 = args[i++];
            final String s3 = args[i];
            if (s2.isEmpty() || s3.isEmpty()) {
                throw new CommandException("commands.stats.failed", new Object[0]);
            }
            CommandResultStats.setScoreBoardStat(commandresultstats, commandresultstats$type, s2, s3);
            CommandBase.notifyCommandListener(sender, this, "commands.stats.success", commandresultstats$type.getTypeName(), s3, s2);
        }
        else if ("clear".equals(s)) {
            CommandResultStats.setScoreBoardStat(commandresultstats, commandresultstats$type, null, null);
            CommandBase.notifyCommandListener(sender, this, "commands.stats.cleared", commandresultstats$type.getTypeName());
        }
        if (flag) {
            final BlockPos blockpos2 = CommandBase.parseBlockPos(sender, args, 1, false);
            final TileEntity tileentity2 = world.getTileEntity(blockpos2);
            tileentity2.markDirty();
        }
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "entity", "block");
        }
        if (args.length == 2 && "entity".equals(args[0])) {
            return CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        if (args.length >= 2 && args.length <= 4 && "block".equals(args[0])) {
            return CommandBase.getTabCompletionCoordinate(args, 1, targetPos);
        }
        if ((args.length == 3 && "entity".equals(args[0])) || (args.length == 5 && "block".equals(args[0]))) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "set", "clear");
        }
        if ((args.length != 4 || !"entity".equals(args[0])) && (args.length != 6 || !"block".equals(args[0]))) {
            return ((args.length != 6 || !"entity".equals(args[0])) && (args.length != 8 || !"block".equals(args[0]))) ? Collections.emptyList() : CommandBase.getListOfStringsMatchingLastWord(args, this.getObjectiveNames(server));
        }
        return CommandBase.getListOfStringsMatchingLastWord(args, CommandResultStats.Type.getTypeNames());
    }
    
    protected List<String> getObjectiveNames(final MinecraftServer server) {
        final Collection<ScoreObjective> collection = server.getWorld(0).getScoreboard().getScoreObjectives();
        final List<String> list = (List<String>)Lists.newArrayList();
        for (final ScoreObjective scoreobjective : collection) {
            if (!scoreobjective.getCriteria().isReadOnly()) {
                list.add(scoreobjective.getName());
            }
        }
        return list;
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return args.length > 0 && "entity".equals(args[0]) && index == 1;
    }
}
