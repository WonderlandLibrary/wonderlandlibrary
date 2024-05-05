package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CommandStats
extends CommandBase {
    public String getCommandName() {
        return "stats";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.stats.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        CommandResultStats commandresultstats;
        CommandResultStats.Type commandresultstats$type;
        int i;
        boolean flag;
        if (args.length < 1) {
            throw new WrongUsageException("commands.stats.usage", new Object[0]);
        }
        if (args[0].equals((Object)"entity")) {
            flag = false;
        } else {
            if (!args[0].equals((Object)"block")) {
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
        if ("set".equals((Object)s)) {
            if (args.length < i + 3) {
                if (i == 5) {
                    throw new WrongUsageException("commands.stats.block.set.usage", new Object[0]);
                }
                throw new WrongUsageException("commands.stats.entity.set.usage", new Object[0]);
            }
        } else {
            if (!"clear".equals((Object)s)) {
                throw new WrongUsageException("commands.stats.usage", new Object[0]);
            }
            if (args.length < i + 1) {
                if (i == 5) {
                    throw new WrongUsageException("commands.stats.block.clear.usage", new Object[0]);
                }
                throw new WrongUsageException("commands.stats.entity.clear.usage", new Object[0]);
            }
        }
        if ((commandresultstats$type = CommandResultStats.Type.getTypeByName((String)args[i++])) == null) {
            throw new CommandException("commands.stats.failed", new Object[0]);
        }
        World world = sender.getEntityWorld();
        if (flag) {
            BlockPos blockpos = CommandStats.parseBlockPos((ICommandSender)sender, (String[])args, (int)1, (boolean)false);
            TileEntity tileentity = world.getTileEntity(blockpos);
            if (tileentity == null) {
                throw new CommandException("commands.stats.noCompatibleBlock", new Object[]{blockpos.getX(), blockpos.getY(), blockpos.getZ()});
            }
            if (tileentity instanceof TileEntityCommandBlock) {
                commandresultstats = ((TileEntityCommandBlock)tileentity).getCommandResultStats();
            } else {
                if (!(tileentity instanceof TileEntitySign)) {
                    throw new CommandException("commands.stats.noCompatibleBlock", new Object[]{blockpos.getX(), blockpos.getY(), blockpos.getZ()});
                }
                commandresultstats = ((TileEntitySign)tileentity).getStats();
            }
        } else {
            Entity entity = CommandStats.getEntity((ICommandSender)sender, (String)args[1]);
            commandresultstats = entity.getCommandStats();
        }
        if ("set".equals((Object)s)) {
            String s1 = args[i++];
            String s2 = args[i];
            if (s1.length() == 0 || s2.length() == 0) {
                throw new CommandException("commands.stats.failed", new Object[0]);
            }
            CommandResultStats.setScoreBoardStat((CommandResultStats)commandresultstats, (CommandResultStats.Type)commandresultstats$type, (String)s1, (String)s2);
            CommandStats.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.stats.success", (Object[])new Object[]{commandresultstats$type.getTypeName(), s2, s1});
        } else if ("clear".equals((Object)s)) {
            CommandResultStats.setScoreBoardStat((CommandResultStats)commandresultstats, (CommandResultStats.Type)commandresultstats$type, (String)null, (String)null);
            CommandStats.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.stats.cleared", (Object[])new Object[]{commandresultstats$type.getTypeName()});
        }
        if (flag) {
            BlockPos blockpos1 = CommandStats.parseBlockPos((ICommandSender)sender, (String[])args, (int)1, (boolean)false);
            TileEntity tileentity1 = world.getTileEntity(blockpos1);
            tileentity1.markDirty();
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandStats.getListOfStringsMatchingLastWord((String[])args, (String[])new String[]{"entity", "block"}) : (args.length == 2 && args[0].equals((Object)"entity") ? CommandStats.getListOfStringsMatchingLastWord((String[])args, (String[])this.func_175776_d()) : (args.length >= 2 && args.length <= 4 && args[0].equals((Object)"block") ? CommandStats.func_175771_a((String[])args, (int)1, (BlockPos)pos) : (!(args.length == 3 && args[0].equals((Object)"entity") || args.length == 5 && args[0].equals((Object)"block")) ? (!(args.length == 4 && args[0].equals((Object)"entity") || args.length == 6 && args[0].equals((Object)"block")) ? (!(args.length == 6 && args[0].equals((Object)"entity") || args.length == 8 && args[0].equals((Object)"block")) ? null : CommandStats.getListOfStringsMatchingLastWord((String[])args, this.func_175777_e())) : CommandStats.getListOfStringsMatchingLastWord((String[])args, (String[])CommandResultStats.Type.getTypeNames())) : CommandStats.getListOfStringsMatchingLastWord((String[])args, (String[])new String[]{"set", "clear"}))));
    }

    protected String[] func_175776_d() {
        return MinecraftServer.getServer().getAllUsernames();
    }

    protected List<String> func_175777_e() {
        Collection collection = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard().getScoreObjectives();
        ArrayList list = Lists.newArrayList();
        for (ScoreObjective scoreobjective : collection) {
            if (scoreobjective.getCriteria().isReadOnly()) continue;
            list.add((Object)scoreobjective.getName());
        }
        return list;
    }

    public boolean isUsernameIndex(String[] args, int index) {
        return args.length > 0 && args[0].equals((Object)"entity") && index == 1;
    }
}
